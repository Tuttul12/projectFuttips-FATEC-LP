const DEFAULT_API = "http://localhost:8081";
let API = localStorage.getItem("futtips_api") || DEFAULT_API;
let TOKEN = localStorage.getItem("futtips_token") || "";
let SESSION = null;
try {
    SESSION = JSON.parse(localStorage.getItem("futtips_session") || "null");
} catch {
    SESSION = null;
}

const apiStatus = document.getElementById("apiStatus");

// ---------- HTTP helpers ----------
async function api(path, opts = {}) {
    const headers = { "Content-Type": "application/json", ...(opts.headers || {}) };
    if (TOKEN) headers.Authorization = `Bearer ${TOKEN}`;
    const res = await fetch(`${API}${path}`, { ...opts, headers });
    const text = await res.text();
    let body = null;
    try { body = text ? JSON.parse(text) : null; } catch { body = text; }
    if (!res.ok) {
        let msg = `${res.status} ${res.statusText}`;
        if (body && typeof body === "object") {
            if (body.mensagem) {
                msg = body.mensagem;
                if (body.erros && typeof body.erros === "object") {
                    const detail = Object.entries(body.erros)
                        .filter(([k, v]) => v !== body.mensagem)
                        .map(([k, v]) => `${k}: ${v}`)
                        .join(", ");
                    if (detail) msg += ` (${detail})`;
                }
            } else if (body.message) {
                msg = body.message;
            } else if (body.error) {
                msg = body.error;
            }
        }
        throw new Error(msg);
    }
    // Desembrulha ApiResponse { status, message, data }
    if (
        body &&
        typeof body === "object" &&
        "dados" in body &&
        "mensagem" in body
    ) {
        return body.dados;
    }

    return body;
}
const get = (p) => api(p);
const post = (p, b) => api(p, { method: "POST", body: JSON.stringify(b) });
const put = (p, b) => api(p, { method: "PUT", body: JSON.stringify(b) });
const del = (p) => api(p, { method: "DELETE" });

async function pingApi() {
    try {
        const r = await fetch(`${API}/camisas`);
        apiStatus.textContent = r.ok ? "conectado" : "offline";
        apiStatus.className = `status ${r.ok ? "ok" : "err"}`;
    } catch {
        apiStatus.textContent = "offline";
        apiStatus.className = "status err";
    }
}

function updateAuthUI() {
    const loginContainer = document.getElementById("login-container");
    const appContainer = document.getElementById("app-container");
    const authInfo = document.getElementById("auth-info");

    if (SESSION) {
        loginContainer.style.display = "none";
        appContainer.style.display = "block";
        if (authInfo) {
            authInfo.textContent = `Olá, ${SESSION.nome} (${SESSION.perfil})`;
            authInfo.className = "status ok";
        }
    } else {
        loginContainer.style.display = "flex";
        appContainer.style.display = "none";
        document.getElementById("login-card").style.display = "block";
        document.getElementById("register-card").style.display = "none";
        if (authInfo) {
            authInfo.textContent = "Não autenticado";
            authInfo.className = "status err";
        }
    }
}

// ---------- Toast ----------
const toastEl = document.getElementById("toast");
let toastTimer;
function toast(msg, type = "ok") {
    toastEl.textContent = msg;
    toastEl.className = `toast show ${type}`;
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => (toastEl.className = "toast"), 3500);
}

// ---------- Tabs ----------
const tabs = document.querySelectorAll(".tab");
const panels = document.querySelectorAll(".panel");
tabs.forEach((t) =>
    t.addEventListener("click", () => {
        tabs.forEach((x) => x.classList.remove("active"));
        panels.forEach((x) => x.classList.remove("active"));
        t.classList.add("active");
        document.getElementById(`tab-${t.dataset.tab}`).classList.add("active");
        loadActiveTab();
    })
);
function activeTab() { return document.querySelector(".tab.active").dataset.tab; }
function loadActiveTab() {
    const map = {
        camisas: loadCamisas, tipos: loadTipos, clientes: loadClientes,
        funcionarios: loadFuncionarios, cargos: loadCargos, pedidos: loadPedidos,
        "itens-pedidos": loadItensPedidos, relatorios: loadRelatorios,
    };
    map[activeTab()]?.();
}

// ---------- Modals ----------
document.querySelectorAll("[data-open]").forEach((b) =>
    b.addEventListener("click", async () => {
        const dlg = document.getElementById(b.dataset.open);
        const form = dlg.querySelector("form");
        form.reset();
        form.querySelectorAll('input[type="hidden"]').forEach((i) => (i.value = ""));
        
        // Reseta o título do diálogo caso estivesse no modo edição
        const title = dlg.querySelector("h3");
        if (title) {
            if (b.dataset.open === "form-camisa") title.textContent = "Nova camisa";
            if (b.dataset.open === "form-tipo") title.textContent = "Novo tipo";
            if (b.dataset.open === "form-cliente") title.textContent = "Novo cliente";
            if (b.dataset.open === "form-funcionario") title.textContent = "Novo funcionário";
            if (b.dataset.open === "form-pedido") title.textContent = "Novo pedido";
        }

        await prepareForm(b.dataset.open);
        dlg.showModal();
    })
);
document.addEventListener("click", (e) => {
    if (e.target.matches("[data-close]")) e.target.closest("dialog").close();
});

async function prepareForm(dialogId) {
    try {
        if (dialogId === "form-camisa") {
            const [tipos, funcs] = await Promise.all([get("/tipo-camisas"), get("/funcionarios")]);
            fillSelect("form-camisa", "tipoCamisaId", tipos, (t) => [t.idTipo, `${t.modelo} (${t.fabricante})`]);
            fillSelect("form-camisa", "funcionarioId", funcs, (f) => [f.id ?? f.idPessoa, f.nome ?? f.pessoasEntity?.nome ?? `Func #${f.id}`]);
        } else if (dialogId === "form-funcionario") {
            const cargos = await get("/cargo");
            fillSelect("form-funcionario", "codigoCargo", cargos, (c) => [c.codigo, c.permissao]);
        } else if (dialogId === "form-pedido") {
            const [clientes, camisas] = await Promise.all([get("/clientes"), get("/camisas")]);
            fillSelect("form-pedido", "idCliente", clientes, (c) => [c.id ?? c.idPessoa, c.nome ?? c.pessoasEntity?.nome ?? `Cliente #${c.id}`]);
            window.__camisasCache = camisas;
            document.getElementById("itens-pedido").innerHTML = "";
            addItemRow();
        }
    } catch (e) { toast("Erro ao carregar opções: " + e.message, "err"); }
}
function fillSelect(formId, name, items, mapFn) {
    const sel = document.querySelector(`#${formId} [name="${name}"]`);
    sel.innerHTML = `<option value="">— selecione —</option>` +
        (items || []).map((it) => { const [v, l] = mapFn(it); return `<option value="${v}">${escape(l)}</option>`; }).join("");
}
function escape(s) { return String(s ?? "").replace(/[&<>"]/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" }[c])); }

// ---------- Form submissions ----------
document.querySelectorAll("[data-form]").forEach((form) => {
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const data = Object.fromEntries(new FormData(form).entries());
        try {
            await handlers[form.dataset.form](data, form);
            const dlg = form.closest("dialog");
            if (dlg) dlg.close();
            
            if (form.dataset.form === "login") {
                toast(`Bem-vindo(a), ${SESSION.nome}! Login realizado com sucesso.`, "ok");
            } else if (form.dataset.form === "register") {
                toast("Conta criada com sucesso! Agora você pode fazer o login.", "ok");
            } else {
                toast("Salvo com sucesso");
                loadActiveTab();
            }
        } catch (err) {
            let friendlyError = err.message;
            if (form.dataset.form === "login") {
                friendlyError = "Falha no login: " + err.message;
            } else if (form.dataset.form === "register") {
                friendlyError = "Erro no cadastro: " + err.message;
            }
            toast(friendlyError, "err");
        }
    });
});

const handlers = {
    async login(d) {
        const data = await post("/auth/login", { email: d.email, senha: d.senha });
        SESSION = data;
        localStorage.setItem("futtips_session", JSON.stringify(SESSION));
        TOKEN = "mock-token-" + SESSION.idPessoa;
        localStorage.setItem("futtips_token", TOKEN);
        updateAuthUI();
        loadActiveTab();
        toast(`Bem-vindo(a), ${SESSION.nome}!`);
    },
    async register(d) {
        await post("/clientes", {
            nome: d.nome,
            cpf: d.cpf,
            email: d.email,
            senha: d.senha,
            telefone: d.telefone,
            nascimento: d.nascimento,
            rua: d.rua,
            numero: d.numero,
            bairro: d.bairro,
            cidade: d.cidade,
            estado: d.estado,
            cep: d.cep
        });
        toast("Conta criada com sucesso! Faça login.", "ok");
        document.getElementById("register-card").style.display = "none";
        document.getElementById("login-card").style.display = "block";
    },
    async camisa(d) {
        const body = {
            descricao: d.descricao,
            tamanho: d.tamanho,
            quantidade: d.quantidade ? Number(d.quantidade) : null,
            tipoCamisasEntity: { idTipo: Number(d.tipoCamisaId) },
            funcionariosEntity: { id: Number(d.funcionarioId) },
        };
        if (d.idCamisa) return put(`/camisas/${d.idCamisa}`, body);
        return post("/camisas", body);
    },
    async tipo(d) {
        const body = { modelo: d.modelo, fabricante: d.fabricante };
        if (d.idTipo) return put(`/tipo-camisas/${d.idTipo}`, body);
        return post("/tipo-camisas", body);
    },
    async cliente(d) {
        const body = {
            nome: d.nome, cpf: d.cpf, email: d.email, senha: d.senha,
            telefone: d.telefone, nascimento: d.nascimento,
            rua: d.rua, numero: d.numero, bairro: d.bairro,
            cidade: d.cidade, estado: d.estado, cep: d.cep,
        };
        if (d.idCliente) {
            await put(`/clientes/${d.idCliente}`, body);
            try {
                const enderecos = await get(`/enderecos/pessoa/${d.idCliente}`);
                if (enderecos && enderecos.length > 0) {
                    const end = enderecos[0];
                    await put(`/enderecos/${end.id.idEndereco}/pessoa/${d.idCliente}`, {
                        id: { idEndereco: end.id.idEndereco, pessoaId: Number(d.idCliente) },
                        rua: d.rua,
                        numero: Number(d.numero),
                        bairro: d.bairro,
                        cidade: d.cidade,
                        estado: d.estado,
                        cep: d.cep
                    });
                }
            } catch (err) { console.error("Erro ao atualizar endereço:", err); }
            return;
        }
        return post("/clientes", body);
    },
    async funcionario(d) {
        const body = {
            nome: d.nome, cpf: d.cpf, email: d.email, senha: d.senha,
            salario: Number(d.salario), cargo: { codigo: Number(d.codigoCargo) },
        };
        if (d.idFuncionario) return put(`/funcionarios/${d.idFuncionario}`, body);
        return post("/funcionarios", body);
    },
    async cargo(d) {
        const body = { permissao: d.permissao };
        if (d.codigo) return put(`/cargo/${d.codigo}`, body);
        return post("/cargo", body);
    },
    async pedido(d, form) {
        const rows = form.querySelectorAll(".item-row");
        const itens = [];
        let valor = 0;
        rows.forEach((r) => {
            const idCamisa = Number(r.querySelector('[name="idCamisa"]').value);
            const qtd = Number(r.querySelector('[name="qtd"]').value);
            if (idCamisa && qtd > 0) { itens.push({ idCamisa, qtd }); valor += qtd * 50; }
        });
        if (!itens.length) throw new Error("Adicione ao menos 1 item");
        const valorInformado = d.valor ? Number(d.valor) : valor;
        const body = { idCliente: Number(d.idCliente), valor: valorInformado, itens };
        if (d.codigo) return put(`/pedidos/${d.codigo}`, body);
        return post("/pedidos", body);
    },
};

// ---------- Itens de pedido (dinâmico) ----------
document.getElementById("add-item").addEventListener("click", addItemRow);
function addItemRow() {
    const cont = document.getElementById("itens-pedido");
    const camisas = window.__camisasCache || [];
    const row = document.createElement("div");
    row.className = "item-row";
    row.innerHTML = `
    <label>Camisa
      <select name="idCamisa" required>
        <option value="">— selecione —</option>
        ${camisas.map((c) => `<option value="${c.idCamisa}">${escape(c.descricao)} (${escape(c.tamanho)})</option>`).join("")}
      </select>
    </label>
    <label>Qtd<input name="qtd" type="number" min="1" value="1" required /></label>
    <button type="button" class="btn-x" title="Remover">×</button>`;
    row.querySelector(".btn-x").addEventListener("click", () => row.remove());
    cont.appendChild(row);
}

// ---------- LIST + DELETE ----------
function renderCards(containerId, items, renderFn) {
    const cont = document.getElementById(containerId);
    if (!items || items.length === 0) { cont.innerHTML = `<div class="empty">Nada por aqui ainda.</div>`; return; }
    cont.innerHTML = items.map(renderFn).join("");
}

async function loadCamisas() {
    try {
        const items = await get("/camisas");
        renderCards("list-camisas", items, (c) => `
      <div class="card">
        <h3>${escape(c.descricao)}</h3>
        <div class="meta">
          <div>Tamanho: <strong>${escape(c.tamanho)}</strong></div>
          <div>Quantidade: ${c.quantidade ?? 0}</div>
          <div>Tipo: ${escape(c.tipoCamisasEntity?.modelo ?? "-")}</div>
        </div>
        <div class="row-actions">
          <button class="btn-edit" onclick="editCamisa(${c.idCamisa})">Editar</button>
          <button class="btn-delete" onclick="removeItem('/camisas/${c.idCamisa}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

window.editCamisa = async (id) => {
    try {
        const c = await get(`/camisas/${id}`);
        const dlg = document.getElementById("form-camisa");
        const form = dlg.querySelector("form");
        form.reset();
        await prepareForm("form-camisa");
        
        const title = dlg.querySelector("h3");
        if (title) title.textContent = "Editar camisa";
        
        form.idCamisa.value = c.idCamisa;
        form.descricao.value = c.descricao;
        form.tamanho.value = c.tamanho;
        form.quantidade.value = c.quantidade ?? 0;
        form.tipoCamisaId.value = c.tipoCamisasEntity?.idTipo ?? "";
        form.funcionarioId.value = c.funcionariosEntity?.id ?? c.funcionariosEntity?.idPessoa ?? "";
        
        dlg.showModal();
    } catch (e) { toast("Erro ao carregar dados: " + e.message, "err"); }
};

async function loadTipos() {
    try {
        const items = await get("/tipo-camisas");
        renderCards("list-tipos", items, (t) => `
      <div class="card">
        <h3>${escape(t.modelo)}</h3>
        <div class="meta"><div>Fabricante: ${escape(t.fabricante)}</div></div>
        <div class="row-actions">
          <button class="btn-edit" onclick="editTipo(${t.idTipo})">Editar</button>
          <button class="btn-delete" onclick="removeItem('/tipo-camisas/${t.idTipo}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

window.editTipo = async (id) => {
    try {
        const t = await get(`/tipo-camisas/${id}`);
        const dlg = document.getElementById("form-tipo");
        const form = dlg.querySelector("form");
        form.reset();
        const title = dlg.querySelector("h3");
        if (title) title.textContent = "Editar tipo de camisa";
        
        form.idTipo.value = t.idTipo;
        form.modelo.value = t.modelo;
        form.fabricante.value = t.fabricante;
        
        dlg.showModal();
    } catch (e) { toast("Erro ao carregar dados: " + e.message, "err"); }
};

async function loadClientes() {
    try {
        const items = await get("/clientes");
        renderCards("list-clientes", items, (c) => {
            const p = c.pessoasEntity || c;
            const id = c.id ?? c.idPessoa ?? p.id;
            return `
      <div class="card">
        <h3>${escape(p.nome ?? c.nome ?? "Cliente")}</h3>
        <div class="meta">
          <div>CPF: ${escape(p.cpf ?? c.cpf ?? "-")}</div>
          <div>Email: ${escape(p.email ?? c.email ?? "-")}</div>
          <div>Tel: ${escape(p.telefone ?? c.telefone ?? "-")}</div>
        </div>
        <div class="row-actions">
          <button class="btn-ghost" onclick="verTotalGasto(${id})">Total gasto</button>
          <button class="btn-edit" onclick="editCliente(${id})">Editar</button>
          <button class="btn-delete" onclick="removeItem('/clientes/${id}')">Excluir</button>
        </div>
      </div>`;
        });
    } catch (e) { toast(e.message, "err"); }
}
window.verTotalGasto = async (id) => {
    try { const v = await get(`/clientes/${id}/total-gasto`); toast(`Total gasto: R$ ${Number(v ?? 0).toFixed(2)}`); }
    catch (e) { toast("Erro: " + e.message, "err"); }
};

window.editCliente = async (id) => {
    try {
        const c = await get(`/clientes/${id}`);
        const dlg = document.getElementById("form-cliente");
        const form = dlg.querySelector("form");
        form.reset();
        await prepareForm("form-cliente");
        const title = dlg.querySelector("h3");
        if (title) title.textContent = "Editar cliente";
        
        const p = c.pessoasEntity || c;
        form.idCliente.value = id;
        form.nome.value = p.nome ?? c.nome ?? "";
        form.cpf.value = p.cpf ?? c.cpf ?? "";
        form.telefone.value = c.telefone ?? "";
        form.email.value = p.email ?? c.email ?? "";
        form.senha.value = p.senha ?? c.senha ?? "";
        if (c.nascimento) {
            const dt = new Date(c.nascimento);
            form.nascimento.value = dt.toISOString().split("T")[0];
        }
        
        try {
            const enderecos = await get(`/enderecos/pessoa/${id}`);
            if (enderecos && enderecos.length > 0) {
                const end = enderecos[0];
                form.rua.value = end.rua ?? "";
                form.numero.value = end.numero ?? "";
                form.bairro.value = end.bairro ?? "";
                form.cep.value = end.cep ?? "";
                form.cidade.value = end.cidade ?? "";
                form.estado.value = end.estado ?? "";
            }
        } catch (err) { console.error("Sem endereço:", err); }
        
        dlg.showModal();
    } catch (e) { toast("Erro ao carregar dados: " + e.message, "err"); }
};

async function loadFuncionarios() {
    try {
        const items = await get("/funcionarios");
        renderCards("list-funcionarios", items, (f) => {
            const p = f.pessoasEntity || f;
            const id = f.id ?? f.idPessoa ?? p.id;
            return `
      <div class="card">
        <h3>${escape(p.nome ?? f.nome ?? "Funcionário")}</h3>
        <div class="meta">
          <div>CPF: ${escape(p.cpf ?? f.cpf ?? "-")}</div>
          <div>Email: ${escape(p.email ?? f.email ?? "-")}</div>
          <div>Salário: R$ ${Number(f.salario ?? 0).toFixed(2)}</div>
          <div>Cargo: ${escape(f.cargoEntity?.permissao ?? f.cargo?.permissao ?? "-")}</div>
        </div>
        <div class="row-actions">
          <button class="btn-edit" onclick="editFuncionario(${id})">Editar</button>
          <button class="btn-delete" onclick="removeItem('/funcionarios/${id}')">Excluir</button>
        </div>
      </div>`;
        });
    } catch (e) { toast(e.message, "err"); }
}

window.editFuncionario = async (id) => {
    try {
        const f = await get(`/funcionarios/${id}`);
        const dlg = document.getElementById("form-funcionario");
        const form = dlg.querySelector("form");
        form.reset();
        await prepareForm("form-funcionario");
        const title = dlg.querySelector("h3");
        if (title) title.textContent = "Editar funcionário";
        
        const p = f.pessoasEntity || f;
        form.idFuncionario.value = id;
        form.nome.value = p.nome ?? f.nome ?? "";
        form.cpf.value = p.cpf ?? f.cpf ?? "";
        form.email.value = p.email ?? f.email ?? "";
        form.senha.value = p.senha ?? f.senha ?? "";
        form.salario.value = f.salario ?? "";
        form.codigoCargo.value = f.cargoEntity?.codigo ?? f.cargo?.codigo ?? "";
        
        dlg.showModal();
    } catch (e) { toast("Erro ao carregar dados: " + e.message, "err"); }
};

async function loadCargos() {
    try {
        const items = await get("/cargo");
        renderCards("list-cargos", items, (c) => `
      <div class="card">
        <h3>${escape(c.permissao)}</h3>
        <div class="meta"><div>Código: ${c.codigo}</div></div>
        <div class="row-actions">
          <button class="btn-delete" onclick="removeItem('/cargo/${c.codigo}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

async function loadPedidos() {
    try {
        const items = await get("/pedidos");
        renderCards("list-pedidos", items, (p) => `
      <div class="card">
        <h3>Pedido #${p.codigo}</h3>
        <div class="meta">
          <div>Protocolo: ${escape(p.protocolo ?? "-")}</div>
          <div>Cliente: ${escape(p.clientesEntity?.pessoasEntity?.nome ?? p.clientesEntity?.nome ?? "-")}</div>
          <div>Valor: R$ ${Number(p.valor ?? 0).toFixed(2)}</div>
          <div>Data: ${p.dataPedido ? new Date(p.dataPedido).toLocaleDateString("pt-BR") : "-"}</div>
        </div>
        <div class="row-actions">
          <button class="btn-edit" onclick="editPedido(${p.codigo})">Editar</button>
          <button class="btn-delete" onclick="removeItem('/pedidos/${p.codigo}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

window.editPedido = async (id) => {
    try {
        const [p, items] = await Promise.all([
            get(`/pedidos/${id}`),
            get(`/itens-pedidos/pedido/${id}`)
        ]);
        
        const dlg = document.getElementById("form-pedido");
        const form = dlg.querySelector("form");
        form.reset();
        await prepareForm("form-pedido");
        
        const title = dlg.querySelector("h3");
        if (title) title.textContent = `Editar pedido #${id}`;
        
        form.codigo.value = id;
        form.idCliente.value = p.clientesEntity?.id ?? p.clientesEntity?.idPessoa ?? "";
        form.valor.value = p.valor ?? "";
        
        const cont = document.getElementById("itens-pedido");
        cont.innerHTML = "";
        
        if (items && items.length > 0) {
            items.forEach((item) => {
                const row = document.createElement("div");
                row.className = "item-row";
                row.innerHTML = `
                <label>Camisa
                  <select name="idCamisa" required>
                    <option value="">— selecione —</option>
                    ${window.__camisasCache.map((c) => `<option value="${c.idCamisa}">${escape(c.descricao)} (${escape(c.tamanho)})</option>`).join("")}
                  </select>
                </label>
                <label>Qtd<input name="qtd" type="number" min="1" required /></label>
                <button type="button" class="btn-x" title="Remover">×</button>`;
                row.querySelector(".btn-x").addEventListener("click", () => row.remove());
                row.querySelector('[name="idCamisa"]').value = item.camisa?.idCamisa ?? "";
                row.querySelector('[name="qtd"]').value = item.qtd ?? 1;
                cont.appendChild(row);
            });
        } else {
            addItemRow();
        }
        
        dlg.showModal();
    } catch (e) { toast("Erro ao carregar dados: " + e.message, "err"); }
};

async function loadItensPedidos() {
    try {
        const items = await get("/itens-pedidos");
        renderCards("list-itens-pedidos", items, (item) => {
            const pedidoCodigo = item.pedido?.codigo ?? "-";
            const clienteNome = item.pedido?.clientesEntity?.pessoasEntity?.nome ?? item.pedido?.clientesEntity?.nome ?? "-";
            const camisaDesc = item.camisa?.descricao ?? "-";
            const camisaTamanho = item.camisa?.tamanho ?? "-";
            return `
      <div class="card">
        <h3>Item de Pedido #${item.id}</h3>
        <div class="meta">
          <div>Pedido: <strong>#${pedidoCodigo}</strong></div>
          <div>Cliente: ${escape(clienteNome)}</div>
          <div>Camisa: ${escape(camisaDesc)} (${escape(camisaTamanho)})</div>
          <div>Quantidade: ${item.qtd}</div>
        </div>
        <div class="row-actions">
          <button class="btn-delete" onclick="removeItem('/itens-pedidos/${item.id}')">Excluir</button>
        </div>
      </div>`;
        });
    } catch (e) { toast(e.message, "err"); }
}

async function loadRelatorios() {
    try {
        const items = await get("/relatorios");
        renderCards("list-relatorios", items, (r) => `
      <div class="card">
        <h3>${escape(r.nome ?? r.nomeCliente ?? "Cliente")}</h3>
        <div class="meta">
          ${Object.entries(r).filter(([k]) => !["nome", "nomeCliente"].includes(k))
                .map(([k, v]) => `<div>${escape(k)}: ${escape(typeof v === "number" ? (k.toLowerCase().includes("valor") || k.toLowerCase().includes("gasto") ? `R$ ${v.toFixed(2)}` : v) : v ?? "-")}</div>`).join("")}
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

window.removeItem = async (path) => {
    if (!confirm("Confirmar exclusão?")) return;
    try { await del(path); toast("Removido"); loadActiveTab(); }
    catch (e) { toast("Erro: " + e.message, "err"); }
};

// Logout
document.getElementById("btn-logout")?.addEventListener("click", () => {
    SESSION = null;
    TOKEN = "";
    localStorage.removeItem("futtips_session");
    localStorage.removeItem("futtips_token");
    updateAuthUI();
    toast("Sessão encerrada");
});

// Alternância de Cards na tela de Autenticação
document.getElementById("go-to-register")?.addEventListener("click", (e) => {
    e.preventDefault();
    document.getElementById("login-card").style.display = "none";
    document.getElementById("register-card").style.display = "block";
});

document.getElementById("go-to-login")?.addEventListener("click", (e) => {
    e.preventDefault();
    document.getElementById("register-card").style.display = "none";
    document.getElementById("login-card").style.display = "block";
});

// ---------- Init ----------
pingApi();
updateAuthUI();
if (SESSION) {
    loadActiveTab();
}