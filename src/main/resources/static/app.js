const DEFAULT_API = "http://localhost:8080";
let API = localStorage.getItem("futtips_api") || DEFAULT_API;
let TOKEN = localStorage.getItem("futtips_token") || "";

const apiInput = document.getElementById("apiUrl");
const apiStatus = document.getElementById("apiStatus");
apiInput.value = API;
apiInput.addEventListener("change", () => {
    API = apiInput.value.replace(/\/$/, "");
    localStorage.setItem("futtips_api", API);
    pingApi();
    loadActiveTab();
});

// ---------- HTTP helpers ----------
async function api(path, opts = {}) {
    const headers = { "Content-Type": "application/json", ...(opts.headers || {}) };
    if (TOKEN) headers.Authorization = `Bearer ${TOKEN}`;
    const res = await fetch(`${API}${path}`, { ...opts, headers });
    const text = await res.text();
    let body = null;
    try { body = text ? JSON.parse(text) : null; } catch { body = text; }
    if (!res.ok) {
        const msg = (body && (body.message || body.error)) || `${res.status} ${res.statusText}`;
        throw new Error(msg);
    }
    // Desembrulha ApiResponse { status, message, data }
    if (body && typeof body === "object" && "data" in body && "message" in body) return body.data;
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
        pessoas: loadPessoas, relatorios: loadRelatorios,
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
            toast("Salvo com sucesso");
            loadActiveTab();
        } catch (err) { toast("Erro: " + err.message, "err"); }
    });
});

const handlers = {
    async login(d) {
        const data = await post("/auth/login", { email: d.email, senha: d.senha });
        TOKEN = data?.token || data?.accessToken || "";
        if (TOKEN) localStorage.setItem("futtips_token", TOKEN);
        document.getElementById("auth-info").textContent = TOKEN ? `Autenticado` : `Login OK`;
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
        return post("/clientes", {
            nome: d.nome, cpf: d.cpf, email: d.email, senha: d.senha,
            telefone: d.telefone, nascimento: d.nascimento,
            rua: d.rua, numero: d.numero, bairro: d.bairro,
            cidade: d.cidade, estado: d.estado, cep: d.cep,
        });
    },
    async funcionario(d) {
        return post("/funcionarios", {
            nome: d.nome, cpf: d.cpf, email: d.email, senha: d.senha,
            salario: Number(d.salario), codigoCargo: Number(d.codigoCargo),
        });
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
        return post("/pedidos", { idCliente: Number(d.idCliente), valor: valorInformado, itens });
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
          <button class="btn-delete" onclick="removeItem('/camisas/${c.idCamisa}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

async function loadTipos() {
    try {
        const items = await get("/tipo-camisas");
        renderCards("list-tipos", items, (t) => `
      <div class="card">
        <h3>${escape(t.modelo)}</h3>
        <div class="meta"><div>Fabricante: ${escape(t.fabricante)}</div></div>
        <div class="row-actions">
          <button class="btn-delete" onclick="removeItem('/tipo-camisas/${t.idTipo}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

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
          <button class="btn-delete" onclick="removeItem('/funcionarios/${id}')">Excluir</button>
        </div>
      </div>`;
        });
    } catch (e) { toast(e.message, "err"); }
}

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
          <button class="btn-delete" onclick="removeItem('/pedidos/${p.codigo}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}

async function loadPessoas() {
    try {
        const items = await get("/pessoas");
        renderCards("list-pessoas", items, (p) => `
      <div class="card">
        <h3>${escape(p.nome)}</h3>
        <div class="meta">
          <div>CPF: ${escape(p.cpf ?? "-")}</div>
          <div>Email: ${escape(p.email ?? "-")}</div>
          <div>Status: ${p.ativo === false ? "inativo" : "ativo"}</div>
        </div>
        <div class="row-actions">
          <button class="btn-ghost" onclick="togglePessoa(${p.id}, ${p.ativo === false})">${p.ativo === false ? "Ativar" : "Desativar"}</button>
          <button class="btn-delete" onclick="removeItem('/pessoas/${p.id}')">Excluir</button>
        </div>
      </div>`);
    } catch (e) { toast(e.message, "err"); }
}
window.togglePessoa = async (id, ativar) => {
    try { await put(`/pessoas/${id}/${ativar ? "ativar" : "desativar"}`, {}); toast("Atualizado"); loadActiveTab(); }
    catch (e) { toast("Erro: " + e.message, "err"); }
};

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
    TOKEN = ""; localStorage.removeItem("futtips_token");
    document.getElementById("auth-info").textContent = "Não autenticado";
    toast("Sessão encerrada");
});
document.getElementById("auth-info").textContent = TOKEN ? "Autenticado" : "Não autenticado";

// ---------- Init ----------
pingApi();
loadCamisas();