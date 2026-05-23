--=======================================================--
----===============QUERY BD_FUTTIPS======================--
--=======================================================--

create database futtips
go

use futtips
go

-- ALTERAÇÕES APLICADAS:
-- 1) pessoas.ativo: permite ativar/desativar usuários sem excluir registros.
-- 2) camisas.quantidade: estoque disponível usado na validação de pedidos.
-- 3) check constraints para impedir estoque negativo e item com qtd <= 0.


create table pessoas (
    id                int                not null    identity        primary key,
    nome            varchar(50)        not null,
    cpf                varchar(14)        not null    unique,
    senha            varchar(255)    not null,
    email            varchar(254)    not null    unique,
    ativo            bit             not null    default 1
)
go

create table enderecos (
    id_endereco        int                not null    identity,
    rua                varchar(100)    not null,
    numero            varchar(10)        not null,
    bairro            varchar(100)    not null,
    cidade            varchar(100)    not null,
    estado            varchar(100)    not null,
    cep                varchar(12)        not null,
    --FK--
    pessoa_id        int                not null    references        pessoas(id),
    primary key (id_endereco, pessoa_id)
)
go

create table cargo(
    codigo            int                not null    identity        primary key,
    permissao        varchar(20)        not null
)
go

create table funcionarios(
    id_pessoa        int                not null    primary key,
    salario            money            not null,
    codigo_cargo    int                not null,
    constraint fk_funcionario_pessoa foreign key (id_pessoa)    references pessoas(id),
    constraint fk_funcionario_cargo foreign key (codigo_cargo)        references cargo(codigo)
)
go


create table clientes (
    id_clientes        int                not null    primary key,
    nascimento        date            not null,
    telefone        varchar(20)        not null,
    data_cadastro    date            not null,
    foreign key (id_clientes)        references pessoas(id)
)
go

create table tipo_camisas (
    id_tipo            int                not null    identity        primary key,
    modelo            varchar(50)        not null,
    fabricante        varchar(20)        not null
)
go

create table camisas (
    id_camisa        int                not null    identity        primary key,
    descricao        varchar(100)    not null,
    tamanho            varchar(5)        not null,
    quantidade        int             not null    default 0,
    id_funcionario    int                not null,
    tipo_camisa        int                not null,
    constraint ck_camisas_quantidade check (quantidade >= 0),
    constraint fk_funcionario_id    foreign key (id_funcionario)    references funcionarios(id_pessoa),
    constraint fk_tipo_camisa        foreign key    (tipo_camisa)        references tipo_camisas(id_tipo)
)
go

create table pedidos (
    codigo            int                not null    identity        primary key,
    protocolo        varchar(30)        not null    unique,
    valor            money            not null,
    data_pedido        date            not null,
    id_cliente        int                not null,
    constraint fk_cliente_id        foreign key (id_cliente)        references clientes(id_clientes)
)
go

create table itens_pedidos (
    id                int                not null    identity        primary key,
    qtd                int                not null,
    id_pedido        int                not null,
    id_camisa        int                not null,
    constraint ck_itens_pedidos_qtd check (qtd > 0),
    constraint fk_item_pedido        foreign key (id_pedido)            references pedidos(codigo),
    constraint fk_item_camisa        foreign key (id_camisa)            references camisas(id_camisa)
)
go

--=======================================================--
--=====================INSERTS===========================--
--=======================================================--

-- ========================================
-- CARGOS
-- ========================================
INSERT INTO cargo (permissao) VALUES ('Admin');
INSERT INTO cargo (permissao) VALUES ('Funcionario');
GO

-- ========================================
-- PESSOAS
-- ========================================
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Carlos Souza',    '111.111.111-11', 'senha123', 'carlos@futtips.com');
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Ana Lima',        '222.222.222-22', 'senha123', 'ana@futtips.com');
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Bruno Martins',   '333.333.333-33', 'senha123', 'bruno@futtips.com');
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Fernanda Costa',  '444.444.444-44', 'senha123', 'fernanda@futtips.com');
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Lucas Pereira',   '555.555.555-55', 'senha123', 'lucas@futtips.com');
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Juliana Ramos',   '666.666.666-66', 'senha123', 'juliana@futtips.com');
GO

-- ========================================
-- FUNCIONARIOS (ids 1 e 2 = Carlos e Ana)
-- ========================================
INSERT INTO funcionarios (id_pessoa, salario, codigo_cargo) VALUES (1, 4500.00, 1); -- Carlos  → Admin
INSERT INTO funcionarios (id_pessoa, salario, codigo_cargo) VALUES (2, 2800.00, 2); -- Ana     → Funcionario
GO

-- ========================================
-- CLIENTES (ids 3, 4, 5 e 6)
-- ========================================
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (3, '1995-06-15', '(11) 91111-1111', GETDATE()); -- Bruno
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (4, '1990-03-22', '(11) 92222-2222', GETDATE()); -- Fernanda
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (5, '2000-11-08', '(11) 93333-3333', GETDATE()); -- Lucas
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (6, '1988-07-30', '(11) 94444-4444', GETDATE()); -- Juliana
GO

-- ========================================
-- TIPOS DE CAMISA
-- ========================================
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Home',  'Nike');
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Away',  'Nike');
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Home',  'Adidas');
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Away',  'Adidas');
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Home',  'Puma');
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Away',  'Puma');
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Third', 'Adidas');
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Third', 'Nike');
GO

-- ========================================
-- CAMISAS (registradas pelo funcionario id=2, Ana)
-- ========================================

-- Flamengo
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Flamengo Home 2024',  'M',  10, 2, 1);
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Flamengo Home 2024',  'G',  10, 2, 1);
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Flamengo Away 2024',  'M',  10, 2, 2);

-- Corinthians
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Corinthians Home 2024', 'P',  10, 2, 3);
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Corinthians Away 2024', 'GG', 10, 2, 4);

-- Palmeiras
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Palmeiras Home 2024',  'M',  10, 2, 3);
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Palmeiras Away 2024',  'G',  10, 2, 4);
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Palmeiras Third 2024', 'G',  10, 2, 7);

-- São Paulo
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa São Paulo Home 2024',  'M',  10, 2, 5);
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa São Paulo Away 2024',  'P',  10, 2, 6);

-- Seleção Brasileira
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Brasil Home 2024',    'M',  10, 2, 1);
INSERT INTO camisas (descricao, tamanho, quantidade, id_funcionario, tipo_camisa) VALUES ('Camisa Brasil Away 2024',    'G',  10, 2, 8);
GO

-- ========================================
-- PEDIDOS (clientes: Bruno=3, Fernanda=4, Lucas=5, Juliana=6)
-- ========================================
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00001', 259.90, GETDATE(), 3); -- Bruno
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00002', 189.90, GETDATE(), 4); -- Fernanda
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00003', 519.70, GETDATE(), 5); -- Lucas
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00004', 129.90, GETDATE(), 6); -- Juliana
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00005', 389.80, GETDATE(), 3); -- Bruno (2º pedido)
GO

-- ========================================
-- ITENS DOS PEDIDOS
-- ========================================

-- Pedido 1 - Bruno: Flamengo Home M + Flamengo Away M
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 1, 1);  -- Flamengo Home M     (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 1, 3);  -- Flamengo Away M     (R$ 129.90) → total R$ 259.90

-- Pedido 2 - Fernanda: Corinthians Home P
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 2, 4);  -- Corinthians Home P  (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 2, 5);  -- Corinthians Away GG (R$ 59.90) → total R$ 189.80

-- Pedido 3 - Lucas: Palmeiras Home + Away + Third
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 3, 6);  -- Palmeiras Home M    (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 3, 7);  -- Palmeiras Away G    (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (2, 3, 8);  -- Palmeiras Third G   (qtd 2 × R$ 129.90) → total R$ 519.70 (aprox)

-- Pedido 4 - Juliana: Brasil Away G
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 4, 12); -- Brasil Away G       (R$ 129.90)

-- Pedido 5 - Bruno (2º pedido): São Paulo Home + Away
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 5, 10); -- São Paulo Home M    (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (2, 5, 11); -- São Paulo Away P    (qtd 2 × R$ 129.90) → total R$ 389.70 (aprox)
GO

