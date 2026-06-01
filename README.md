# FutTips API ⚽👕

API REST desenvolvida com **Java + Spring Boot + SQL Server** para
gerenciamento de uma loja de camisas de times de futebol.

O sistema permite:

-   cadastro e gerenciamento de clientes
-   cadastro e gerenciamento de funcionários
-   controle de camisas e estoque
-   criação de pedidos com múltiplos itens
-   relatórios com views e functions
-   uso de procedures SQL Server para operações transacionais

## Tecnologias utilizadas

-   Java 17+
-   Spring Boot
-   Spring Data JPA / Hibernate
-   SQL Server
-   Lombok
-   Maven
-   Jakarta Validation
-   Postman

## Banco de Dados

Banco utilizado:

``` sql
futtips
```

Configuração do `application.properties`:

``` properties
spring.datasource.url=jdbc:sqlserver://localhost;databaseName=futtips;encrypt=false
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Modelo Relacional

``` text
pessoas
 ├── clientes
 └── funcionarios ── cargo
       │
     camisas ── tipo_camisas
       │
  itens_pedidos ── pedidos ── clientes

enderecos ── pessoas
```

## Principais Procedures

-   `sp_criar_pessoa_cliente`
-   `sp_editar_cliente`
-   `sp_criar_pessoa_funcionario`
-   `sp_cliente_para_funcionario`
-   `sp_funcionario_para_cliente`
-   `sp_criar_pedido`
-   `sp_atualizar_itens_pedido`

## Views

-   `vw_relatorio_clientes`

## Functions

-   `fn_total_gasto_cliente`

## Como executar

### 1. Clonar

``` bash
git clone https://github.com/seu-usuario/futtips.git
```

### 2. Criar banco

Executar o script SQL completo do projeto.

### 3. Configurar credenciais

Editar:

``` properties
src/main/resources/application.properties
```

### 4. Rodar

Windows:

``` bash
mvnw spring-boot:run
```

Linux/macOS:

``` bash
./mvnw spring-boot:run
```

### 5. Acessar

``` txt
http://localhost:8081
```

## Autor

Projeto acadêmico --- FATEC Rio Preto

Desenvolvido por
**Luiz Evangelista**,
**Vitor Emanuel**,
**Heitor Gallina**,
**Diego Marcato**.
