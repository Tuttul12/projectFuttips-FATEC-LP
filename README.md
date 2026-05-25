# FutTips API

API REST desenvolvida com **Spring Boot** e **SQL Server** para gerenciamento de uma loja de camisas de times de futebol.

---

## Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- SQL Server
- Lombok
- Maven

---

## Configuração do Banco de Dados

Configure o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost;databaseName=futtips;encrypt=false
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## Modelo de Dados

```
pessoas
  ├── clientes
  └── funcionarios ── cargo
        │
      camisas ── tipo_camisas
        │
   itens_pedidos ── pedidos ── clientes
        
enderecos ── pessoas
```

### Tabelas

| Tabela | Descrição |
|---|---|
| `pessoas` | Base para clientes e funcionários (herança) |
| `clientes` | Herda de pessoas, realiza pedidos |
| `funcionarios` | Herda de pessoas, registra camisas |
| `cargo` | Cargo do funcionário (Admin, Funcionario) |
| `enderecos` | Endereços vinculados a uma pessoa |
| `tipo_camisas` | Modelo e fabricante das camisas |
| `camisas` | Camisas disponíveis no estoque |
| `pedidos` | Pedidos realizados pelos clientes |
| `itens_pedidos` | Camisas vinculadas a cada pedido |

---

## Endpoints

### Pessoas
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/pessoas` | Lista todas as pessoas |
| GET | `/pessoas/{id}` | Busca pessoa por id |

### Clientes
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/clientes` | Lista todos os clientes |
| GET | `/clientes/{id}` | Busca cliente por id |
| POST | `/clientes` | Cadastra novo cliente |
| PUT | `/clientes/{id}` | Atualiza cliente |
| DELETE | `/clientes/{id}` | Remove cliente |
| POST | `/clientes/converter/funcionario-para-cliente` | Converte funcionário em cliente |

### Funcionários
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/funcionarios` | Lista todos os funcionários |
| GET | `/funcionarios/{id}` | Busca funcionário por id |
| POST | `/funcionarios` | Cadastra novo funcionário |
| PUT | `/funcionarios/{id}` | Atualiza funcionário |
| DELETE | `/funcionarios/{id}` | Remove funcionário |
| POST | `/funcionarios/converter/cliente-para-funcionario` | Converte cliente em funcionário |

### Cargos
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/cargo` | Lista todos os cargos |
| GET | `/cargo/{id}` | Busca cargo por id |
| GET | `/cargo/{id}/funcionarios` | Lista funcionários de um cargo |

### Endereços
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/enderecos` | Lista todos os endereços |
| GET | `/enderecos/pessoa/{pessoaId}` | Lista endereços de uma pessoa |
| GET | `/enderecos/{idEndereco}/pessoa/{pessoaId}` | Busca endereço específico |
| POST | `/enderecos` | Cadastra novo endereço |
| PUT | `/enderecos/{idEndereco}/pessoa/{pessoaId}` | Atualiza endereço |
| DELETE | `/enderecos/{idEndereco}/pessoa/{pessoaId}` | Remove endereço |

### Tipo de Camisas
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/tipo-camisas` | Lista todos os tipos |
| GET | `/tipo-camisas/{id}` | Busca tipo por id |
| GET | `/tipo-camisas/fabricante/{fabricante}` | Filtra por fabricante |
| GET | `/tipo-camisas/modelo/{modelo}` | Filtra por modelo |
| POST | `/tipo-camisas` | Cadastra novo tipo |
| PUT | `/tipo-camisas/{id}` | Atualiza tipo |
| DELETE | `/tipo-camisas/{id}` | Remove tipo |

### Camisas
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/camisas` | Lista todas as camisas |
| GET | `/camisas/{id}` | Busca camisa por id |
| GET | `/camisas/tipo/{idTipo}` | Filtra por tipo |
| GET | `/camisas/funcionario/{idFuncionario}` | Filtra por funcionário |
| GET | `/camisas/tamanho/{tamanho}` | Filtra por tamanho |
| POST | `/camisas` | Cadastra nova camisa |
| PUT | `/camisas/{id}` | Atualiza camisa |
| DELETE | `/camisas/{id}` | Remove camisa |

### Pedidos
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/pedidos` | Lista todos os pedidos |
| GET | `/pedidos/{id}` | Busca pedido por id |
| GET | `/pedidos/cliente/{clienteId}` | Lista pedidos de um cliente |
| POST | `/pedidos` | Cadastra novo pedido com itens |
| PUT | `/pedidos/{id}` | Atualiza pedido |
| DELETE | `/pedidos/{id}` | Remove pedido |

### Itens dos Pedidos
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/itens-pedidos` | Lista todos os itens |
| GET | `/itens-pedidos/{id}` | Busca item por id |
| GET | `/itens-pedidos/pedido/{pedidoCodigo}` | Lista itens de um pedido |
| GET | `/itens-pedidos/camisa/{idCamisa}` | Lista itens por camisa |
| POST | `/itens-pedidos` | Cadastra item |
| PUT | `/itens-pedidos/atualizar` | Atualiza itens de um pedido |
| DELETE | `/itens-pedidos/{id}` | Remove item |

---

## Exemplos de Requisições

### Cadastrar novo cliente
`POST /clientes`
```json
{
    "nome":       "Pedro Henrique",
    "cpf":        "888.888.888-88",
    "email":      "pedro@futtips.com",
    "senha":      "senha123",
    "nascimento": "1995-05-10",
    "telefone":   "17 98888-8888"
}
```

### Cadastrar novo funcionário
`POST /funcionarios`
```json
{
    "nome":        "Maria Souza",
    "cpf":         "999.999.999-99",
    "email":       "maria@futtips.com",
    "senha":       "senha123",
    "salario":     3200.00,
    "codigoCargo": 2
}
```

### Converter cliente em funcionário
`POST /funcionarios/converter/cliente-para-funcionario`
```json
{
    "idPessoa":    3,
    "salario":     2500.00,
    "codigoCargo": 2
}
```

### Cadastrar camisa
`POST /camisas`
```json
{
    "descricao": "Camisa Flamengo Home 2026",
    "tamanho":   "M",
    "quantidade": 10,
    "funcionariosEntity": { "id": 2 },
    "tipoCamisasEntity":  { "idTipo": 1 }
}
```

### Criar pedido com itens
`POST /pedidos`
```json
{
    "idCliente": 3,
    "valor":     389.70,
    "itens": [
        { "idCamisa": 1, "qtd": 2 },
        { "idCamisa": 3, "qtd": 1 }
    ]
}
```

### Atualizar itens de um pedido
`PUT /itens-pedidos/atualizar`
```json
{
    "idPedido": 1,
    "itens": [
        { "idCamisa": 2, "qtd": 1 },
        { "idCamisa": 4, "qtd": 3 }
    ]
}
```

---

## Procedures

| Procedure | Descrição |
|---|---|
| `sp_criar_pessoa_cliente` | Cadastra pessoa e cliente numa transação |
| `sp_criar_pessoa_funcionario` | Cadastra pessoa e funcionário numa transação |
| `sp_cliente_para_funcionario` | Converte cliente existente em funcionário |
| `sp_funcionario_para_cliente` | Converte funcionário existente em cliente |
| `sp_criar_pedido` | Cria pedido e insere todos os itens numa transação |
| `sp_atualizar_itens_pedido` | Remove itens antigos e reinsere os novos numa transação |

---

## Estrutura do Projeto

```
src/main/java/com/futtips/project/
│
├── controllers/
│   ├── PessoasController.java
│   ├── ClientesController.java
│   ├── FuncionariosController.java
│   ├── CargoController.java
│   ├── EnderecosController.java
│   ├── TipoCamisasController.java
│   ├── CamisasController.java
│   ├── PedidosController.java
│   └── ItensPedidosController.java
│
├── services/
│   ├── PessoasService.java
│   ├── ClientesService.java
│   ├── FuncionariosService.java
│   ├── CargoService.java
│   ├── EnderecosService.java
│   ├── TipoCamisasService.java
│   ├── CamisasService.java
│   ├── PedidosService.java
│   └── ItensPedidosService.java
│
├── repositories/
│   ├── PessoasRepository.java
│   ├── ClientesRepository.java
│   ├── FuncionariosRepository.java
│   ├── CargoRepository.java
│   ├── EnderecosRepository.java
│   ├── TipoCamisasRepository.java
│   ├── CamisasRepository.java
│   ├── PedidosRepository.java
│   └── ItensPedidosRepository.java
│
├── entities/
│   ├── PessoasEntity.java
│   ├── ClientesEntity.java
│   ├── FuncionariosEntity.java
│   ├── CargoEntity.java
│   ├── EnderecosEntity.java
│   ├── TipoCamisasEntity.java
│   ├── CamisasEntity.java
│   ├── PedidosEntity.java
│   ├── ItensPedidosEntity.java
│   └── pk/
│       └── EnderecoPK.java
│
└── dto/
    ├── CriarClienteDTO.java
    ├── CriarFuncionarioDTO.java
    ├── ClienteParaFuncionarioDTO.java
    ├── FuncionarioParaClienteDTO.java
    ├── CriarPedidoDTO.java
    └── AtualizarItensPedidoDTO.java
```

---

## Como Executar

1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/futtips.git
```

2. Configure o `application.properties` com suas credenciais do SQL Server

3. Execute o script SQL para criar o banco e as tabelas

4. Execute as procedures no SQL Server

5. Rode o projeto
```bash
./mvnw spring-boot:run
```

6. Acesse a API em `http://localhost:8080`

---

## Autor

Desenvolvido como projeto acadêmico.
