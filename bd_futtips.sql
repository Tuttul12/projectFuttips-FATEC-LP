--=======================================================--
----===============QUERY BD_FUTTIPS======================--
--=======================================================--

create database futtips
go

use futtips
go

create table pessoas (
    id                int                not null    identity        primary key,
    nome            varchar(50)        not null,
    cpf                varchar(14)        not null    unique,
    senha            varchar(255)    not null,
    email            varchar(254)    not null    unique
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
    id_funcionario    int                not null,
    tipo_camisa        int                not null,
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
INSERT INTO cargo (permissao) VALUES ('Admin')
INSERT INTO cargo (permissao) VALUES ('Funcionario')
GO

-- ========================================
-- PESSOAS
-- ========================================
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Luiz Evangelista',    '111.111.111-11', 'senha123', 'vitor@futtips.com')
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Vitor Emanuel',        '222.222.222-22', 'senha123', 'luiz@futtips.com')
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Bruno Martins',   '333.333.333-33', 'senha123', 'bruno@futtips.com')
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Fernanda Costa',  '444.444.444-44', 'senha123', 'fernanda@futtips.com')
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Lucas Pereira',   '555.555.555-55', 'senha123', 'lucas@futtips.com')
INSERT INTO pessoas (nome, cpf, senha, email) VALUES ('Juliana Ramos',   '666.666.666-66', 'senha123', 'juliana@futtips.com')
GO

-- ========================================
-- FUNCIONARIOS (ids 1 e 2 = Carlos e Ana)
-- ========================================
INSERT INTO funcionarios (id_pessoa, salario, codigo_cargo) VALUES (1, 4500.00, 1) -- Carlos  → Admin
INSERT INTO funcionarios (id_pessoa, salario, codigo_cargo) VALUES (2, 2800.00, 2) -- Ana     → Funcionario
GO

-- ========================================
-- CLIENTES (ids 3, 4, 5 e 6)
-- ========================================
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (3, '1995-06-15', '(11) 91111-1111', GETDATE()) -- Bruno
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (4, '1990-03-22', '(11) 92222-2222', GETDATE()) -- Fernanda
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (5, '2000-11-08', '(11) 93333-3333', GETDATE()) -- Lucas
INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro) VALUES (6, '1988-07-30', '(11) 94444-4444', GETDATE()) -- Juliana
GO

-- ========================================
-- TIPOS DE CAMISA
-- ========================================
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Home',  'Nike')
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Away',  'Nike')
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Home',  'Adidas')
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Away',  'Adidas')
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Home',  'Puma')
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Away',  'Puma')
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Third', 'Adidas')
INSERT INTO tipo_camisas (modelo, fabricante) VALUES ('Camisa Third', 'Nike')
GO

-- ========================================
-- CAMISAS (registradas pelo funcionario id=2, Ana)
-- ========================================

-- Flamengo
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Flamengo Home 2024',  'M',  2, 1)
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Flamengo Home 2024',  'G',  2, 1)
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Flamengo Away 2024',  'M',  2, 2)

-- Corinthians
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Corinthians Home 2024', 'P',  2, 3)
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Corinthians Away 2024', 'GG', 2, 4)

-- Palmeiras
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Palmeiras Home 2024',  'M',  2, 3)
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Palmeiras Away 2024',  'G',  2, 4)
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Palmeiras Third 2024', 'G',  2, 7)

-- São Paulo
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa São Paulo Home 2024',  'M',  2, 5)
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa São Paulo Away 2024',  'P',  2, 6)

-- Seleção Brasileira
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Brasil Home 2024',    'M',  2, 1)
INSERT INTO camisas (descricao, tamanho, id_funcionario, tipo_camisa) VALUES ('Camisa Brasil Away 2024',    'G',  2, 8)
GO

-- ========================================
-- PEDIDOS (clientes: Bruno=3, Fernanda=4, Lucas=5, Juliana=6)
-- ========================================
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00001', 259.90, GETDATE(), 3) -- Bruno
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00002', 189.90, GETDATE(), 4) -- Fernanda
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00003', 519.70, GETDATE(), 5) -- Lucas
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00004', 129.90, GETDATE(), 6) -- Juliana
INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente) VALUES ('PED-20260513-00005', 389.80, GETDATE(), 3) -- Bruno (2º pedido)
GO

-- ========================================
-- ITENS DOS PEDIDOS
-- ========================================

-- Pedido 1 - Bruno: Flamengo Home M + Flamengo Away M
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 1, 1)  -- Flamengo Home M     (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 1, 3)  -- Flamengo Away M     (R$ 129.90) → total R$ 259.90

-- Pedido 2 - Fernanda: Corinthians Home P
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 2, 4)  -- Corinthians Home P  (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 2, 5)  -- Corinthians Away GG (R$ 59.90) → total R$ 189.80

-- Pedido 3 - Lucas: Palmeiras Home + Away + Third
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 3, 6)  -- Palmeiras Home M    (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 3, 7)  -- Palmeiras Away G    (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (2, 3, 8)  -- Palmeiras Third G   (qtd 2 × R$ 129.90) → total R$ 519.70 (aprox)

-- Pedido 4 - Juliana: Brasil Away G
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 4, 12) -- Brasil Away G       (R$ 129.90)

-- Pedido 5 - Bruno (2º pedido): São Paulo Home + Away
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (1, 5, 10) -- São Paulo Home M    (R$ 129.90)
INSERT INTO itens_pedidos (qtd, id_pedido, id_camisa) VALUES (2, 5, 11) -- São Paulo Away P    (qtd 2 × R$ 129.90) → total R$ 389.70 (aprox)
GO

-- ========================================
-- VIEWS
-- ========================================

--View para relatório que busca clientes, quantidade de pedidos e valor gasto

CREATE VIEW vw_relatorio_clientes
AS
SELECT 
    p.id                    AS id_pessoa,
    p.nome                  AS nome_cliente,
    p.cpf                   AS cpf,
    COUNT(pe.codigo)        AS total_pedidos,
    ISNULL(SUM(pe.valor), 0) AS valor_total_gasto
FROM pessoas p
INNER JOIN clientes c 
    ON c.id_clientes = p.id
LEFT JOIN pedidos pe 
    ON pe.id_cliente = c.id_clientes
GROUP BY 
    p.id,
    p.nome,
    p.cpf
GO
--
select * from vw_relatorio_clientes



-- ========================================
-- Procedures
-- ========================================

--Procedure cria uma pessoa cliente e endereço vinculado ao cliente

CREATE PROCEDURE sp_criar_pessoa_cliente
    @nome           VARCHAR(50),
    @cpf            VARCHAR(14),
    @email          VARCHAR(254),
    @senha          VARCHAR(255),
    @nascimento     DATE,
    @telefone       VARCHAR(20),
    @rua            VARCHAR(100),
    @numero         VARCHAR(10),
    @bairro         VARCHAR(100),
    @cidade         VARCHAR(100),
    @estado         VARCHAR(100),
    @cep            VARCHAR(12)
AS
BEGIN
    SET NOCOUNT ON

    BEGIN TRY
        BEGIN TRANSACTION

        IF EXISTS (SELECT 1 FROM pessoas WHERE cpf = @cpf)
        BEGIN
            ROLLBACK TRANSACTION
            RAISERROR('CPF já cadastrado', 16, 1)
            RETURN
        END

        IF EXISTS (SELECT 1 FROM pessoas WHERE email = @email)
        BEGIN
            ROLLBACK TRANSACTION
            RAISERROR('E-mail já cadastrado', 16, 1)
            RETURN
        END

        INSERT INTO pessoas (nome, cpf, email, senha)
        VALUES (@nome, @cpf, @email, @senha)

        DECLARE @id_gerado INT = SCOPE_IDENTITY()

        INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro)
        VALUES (@id_gerado, @nascimento, @telefone, GETDATE())

        INSERT INTO enderecos (
            rua, numero, bairro, cidade, estado, cep, pessoa_id
        )
        VALUES (
            @rua, @numero, @bairro, @cidade, @estado, @cep, @id_gerado
        )

        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION

        DECLARE @ErroMsg NVARCHAR(4000) = ERROR_MESSAGE()
        RAISERROR('Erro ao cadastrar: %s', 16, 1, @ErroMsg)
    END CATCH
END
--====
EXEC sp_criar_pessoa_cliente
    @nome       = 'Teste Silva',
    @cpf        = '888.888.888-88',
    @email      = 'teste@futtips.com',
    @senha      = 'senha123',
    @nascimento = '1995-05-10',
    @telefone   = '17 98888-8888'


--======================================================================

--Procedure que criar pessoa,e cadastra essa pessoa como funcionario

CREATE sp_criar_pessoa_funcionario
    @nome           VARCHAR(50),
    @cpf            VARCHAR(14),
    @email          VARCHAR(254),
    @senha          VARCHAR(255),
    @salario        MONEY,
    @codigo_cargo   INT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION


        IF EXISTS (SELECT 1 FROM pessoas WHERE cpf = @cpf)
        BEGIN
            RAISERROR('CPF já cadastrado', 16, 1)
            RETURN
        END

        IF EXISTS (SELECT 1 FROM pessoas WHERE email = @email)
        BEGIN
            RAISERROR('E-mail já cadastrado', 16, 1)
            RETURN
        END


        IF NOT EXISTS (SELECT 1 FROM cargo WHERE codigo = @codigo_cargo)
        BEGIN
            RAISERROR('Cargo não encontrado', 16, 1)
            RETURN
        END


        INSERT INTO pessoas (nome, cpf, email, senha)
        VALUES (@nome, @cpf, @email, @senha)

        DECLARE @id_gerado INT = SCOPE_IDENTITY()


        INSERT INTO funcionarios (id_pessoa, salario, codigo_cargo)
        VALUES (@id_gerado, @salario, @codigo_cargo)

        COMMIT TRANSACTION

        SELECT
            p.id,
            p.nome,
            p.cpf,
            p.email,
            f.salario,
            c.codigo    AS id_cargo,
            c.permissao AS cargo
        FROM funcionarios f
        INNER JOIN pessoas p ON f.id_pessoa    = p.id
        INNER JOIN cargo   c ON f.codigo_cargo = c.codigo
        WHERE p.id = @id_gerado

    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION

        DECLARE @erro VARCHAR(500) = ERROR_MESSAGE()
        RAISERROR(@erro, 16, 1)
    END CATCH
END

--=========

EXEC sp_criar_pessoa_funcionario
    @nome         = 'Maria Souza',
    @cpf          = '999.999.123-12',
    @email        = 'maria@futtips.com',
    @senha        = 'senha123',
    @salario      = 3200.00,
    @codigo_cargo = 2
go

--======================================================================

CREATE sp_cliente_para_funcionario
    @id_pessoa      INT,
    @salario        MONEY,
    @codigo_cargo   INT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION

        -- Verifica se a pessoa existe
        IF NOT EXISTS (SELECT 1 FROM pessoas WHERE id = @id_pessoa)
        BEGIN
            RAISERROR('Pessoa não encontrada', 16, 1)
            RETURN
        END

        -- Verifica se é realmente um cliente
        IF NOT EXISTS (SELECT 1 FROM clientes WHERE id_clientes = @id_pessoa)
        BEGIN
            RAISERROR('Essa pessoa não é um cliente', 16, 1)
            RETURN
        END

        -- Verifica se já é funcionário
        IF EXISTS (SELECT 1 FROM funcionarios WHERE id_pessoa = @id_pessoa)
        BEGIN
            RAISERROR('Essa pessoa já é um funcionário', 16, 1)
            RETURN
        END

        -- Verifica se o cargo existe
        IF NOT EXISTS (SELECT 1 FROM cargo WHERE codigo = @codigo_cargo)
        BEGIN
            RAISERROR('Cargo não encontrado', 16, 1)
            RETURN
        END

        -- Insere em funcionarios mantendo o mesmo id
        INSERT INTO funcionarios (id_pessoa, salario, codigo_cargo)
        VALUES (@id_pessoa, @salario, @codigo_cargo)

        COMMIT TRANSACTION

        -- Retorna os dados completos
        SELECT
            p.id,
            p.nome,
            p.cpf,
            p.email,
            f.salario,
            c.permissao AS cargo
        FROM funcionarios f
        INNER JOIN pessoas p ON f.id_pessoa    = p.id
        INNER JOIN cargo   c ON f.codigo_cargo = c.codigo
        WHERE p.id = @id_pessoa

    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION
        DECLARE @erro VARCHAR(500) = ERROR_MESSAGE()
        RAISERROR(@erro, 16, 1)
    END CATCH
END

--======================================================================

CREATE sp_funcionario_para_cliente
    @id_pessoa      INT,
    @nascimento     DATE,
    @telefone       VARCHAR(20)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION

        -- Verifica se a pessoa existe
        IF NOT EXISTS (SELECT 1 FROM pessoas WHERE id = @id_pessoa)
        BEGIN
            RAISERROR('Pessoa não encontrada', 16, 1)
            RETURN
        END

        -- Verifica se é realmente um funcionário
        IF NOT EXISTS (SELECT 1 FROM funcionarios WHERE id_pessoa = @id_pessoa)
        BEGIN
            RAISERROR('Essa pessoa não é um funcionário', 16, 1)
            RETURN
        END

        -- Verifica se já é cliente
        IF EXISTS (SELECT 1 FROM clientes WHERE id_clientes = @id_pessoa)
        BEGIN
            RAISERROR('Essa pessoa já é um cliente', 16, 1)
            RETURN
        END

        -- Insere em clientes mantendo o mesmo id
        INSERT INTO clientes (id_clientes, nascimento, telefone, data_cadastro)
        VALUES (@id_pessoa, @nascimento, @telefone, GETDATE())

        COMMIT TRANSACTION

        -- Retorna os dados completos
        SELECT
            p.id,
            p.nome,
            p.cpf,
            p.email,
            c.nascimento,
            c.telefone,
            c.data_cadastro
        FROM clientes c
        INNER JOIN pessoas p ON c.id_clientes = p.id
        WHERE p.id = @id_pessoa

    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION
        DECLARE @erro VARCHAR(500) = ERROR_MESSAGE()
        RAISERROR(@erro, 16, 1)
    END CATCH
END


--======================================================================
CREATE sp_criar_pedido
    @id_cliente     INT,
    @valor          MONEY,
    @itens          NVARCHAR(MAX)  -- JSON com os itens do pedido
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION

        -- Verifica se o cliente existe
        IF NOT EXISTS (SELECT 1 FROM clientes WHERE id_clientes = @id_cliente)
        BEGIN
            RAISERROR('Cliente não encontrado', 16, 1)
            RETURN
        END

        -- Gera o protocolo automaticamente
        DECLARE @protocolo VARCHAR(30)
        DECLARE @sequencial INT

        SELECT @sequencial = COUNT(*) + 1 FROM pedidos

        SET @protocolo = 'PED-' + FORMAT(GETDATE(), 'yyyyMMdd') + '-' + 
                         RIGHT('00000' + CAST(@sequencial AS VARCHAR), 5)

        -- Insere o pedido
        INSERT INTO pedidos (protocolo, valor, data_pedido, id_cliente)
        VALUES (@protocolo, @valor, GETDATE(), @id_cliente)

        DECLARE @id_pedido INT = SCOPE_IDENTITY()

        -- Insere os itens a partir do JSON recebido
        -- Formato esperado: [{"idCamisa": 1, "qtd": 2}, {"idCamisa": 3, "qtd": 1}]
        INSERT INTO itens_pedidos (id_pedido, id_camisa, qtd)
        SELECT 
            @id_pedido,
            JSON_VALUE(item.value, '$.idCamisa'),
            JSON_VALUE(item.value, '$.qtd')
        FROM OPENJSON(@itens) AS item

        -- Verifica se todas as camisas existem
        IF EXISTS (
            SELECT 1 FROM itens_pedidos ip
            LEFT JOIN camisas c ON ip.id_camisa = c.id_camisa
            WHERE ip.id_pedido = @id_pedido AND c.id_camisa IS NULL
        )
        BEGIN
            RAISERROR('Uma ou mais camisas não foram encontradas', 16, 1)
            RETURN
        END

        COMMIT TRANSACTION

        -- Retorna o pedido completo com itens
        SELECT
            p.codigo,
            p.protocolo,
            p.valor,
            p.data_pedido,
            pe.nome         AS cliente,
            c.descricao     AS camisa,
            tc.modelo       AS modelo,
            tc.fabricante   AS fabricante,
            ip.qtd
        FROM pedidos p
        INNER JOIN clientes     cl  ON p.id_cliente   = cl.id_clientes
        INNER JOIN pessoas      pe  ON cl.id_clientes  = pe.id
        INNER JOIN itens_pedidos ip  ON ip.id_pedido   = p.codigo
        INNER JOIN camisas      c   ON ip.id_camisa    = c.id_camisa
        INNER JOIN tipo_camisas tc  ON c.tipo_camisa   = tc.id_tipo
        WHERE p.codigo = @id_pedido

    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;
        DECLARE @erro VARCHAR(500) = ERROR_MESSAGE()
        RAISERROR(@erro, 16, 1)
    END CATCH
END


--======================================================================


CREATE sp_atualizar_itens_pedido
    @id_pedido  INT,
    @itens      NVARCHAR(MAX)  -- JSON com os novos itens
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION

        -- Verifica se o pedido existe
        IF NOT EXISTS (SELECT 1 FROM pedidos WHERE codigo = @id_pedido)
        BEGIN
            RAISERROR('Pedido não encontrado', 16, 1)
            RETURN;
        END

        -- Verifica se todas as camisas do JSON existem
        IF EXISTS (
            SELECT 1
            FROM OPENJSON(@itens) AS item
            LEFT JOIN camisas c ON c.id_camisa = JSON_VALUE(item.value, '$.idCamisa')
            WHERE c.id_camisa IS NULL
        )
        BEGIN
            RAISERROR('Uma ou mais camisas não foram encontradas', 16, 1)
            RETURN
        END

        -- Remove os itens antigos do pedido
        DELETE FROM itens_pedidos WHERE id_pedido = @id_pedido

        -- Insere os novos itens
        INSERT INTO itens_pedidos (id_pedido, id_camisa, qtd)
        SELECT
            @id_pedido,
            JSON_VALUE(item.value, '$.idCamisa'),
            JSON_VALUE(item.value, '$.qtd')
        FROM OPENJSON(@itens) AS item

        COMMIT TRANSACTION;

        -- Retorna os itens atualizados
        SELECT
            ip.id,
            ip.qtd,
            c.descricao     AS camisa,
            tc.modelo       AS modelo,
            tc.fabricante   AS fabricante,
            c.tamanho
        FROM itens_pedidos ip
        INNER JOIN camisas      c   ON ip.id_camisa  = c.id_camisa
        INNER JOIN tipo_camisas tc  ON c.tipo_camisa = tc.id_tipo
        WHERE ip.id_pedido = @id_pedido

    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION
        DECLARE @erro VARCHAR(500) = ERROR_MESSAGE()
        RAISERROR(@erro, 16, 1)
    END CATCH
END

--======

EXEC sp_atualizar_itens_pedido
    @id_pedido = 1,
    @itens = '[{"idCamisa": 2, "qtd": 1}, {"idCamisa": 4, "qtd": 3}]'

--======================================================================


--Edita cliente e endereço

CREATE sp_editar_cliente
    @id_cliente     INT,
    @nome           VARCHAR(50),
    @cpf            VARCHAR(14),
    @email          VARCHAR(254),
    @senha          VARCHAR(255),
    @nascimento     DATE,
    @telefone       VARCHAR(20),
    @rua            VARCHAR(100),
    @numero         VARCHAR(10),
    @bairro         VARCHAR(100),
    @cidade         VARCHAR(100),
    @estado         VARCHAR(100),
    @cep            VARCHAR(12)
AS
BEGIN
    SET NOCOUNT ON

    BEGIN TRY
        BEGIN TRANSACTION

        IF NOT EXISTS (SELECT 1 FROM clientes WHERE id_clientes = @id_cliente)
        BEGIN
            RAISERROR('Cliente não encontrado', 16, 1)
            ROLLBACK TRANSACTION
            RETURN
        END

        IF EXISTS (SELECT 1 FROM pessoas WHERE cpf = @cpf AND id <> @id_cliente)
        BEGIN
            RAISERROR('CPF já cadastrado para outra pessoa', 16, 1)
            ROLLBACK TRANSACTION
            RETURN
        END

        IF EXISTS (SELECT 1 FROM pessoas WHERE email = @email AND id <> @id_cliente)
        BEGIN
            RAISERROR('E-mail já cadastrado para outra pessoa', 16, 1)
            ROLLBACK TRANSACTION;
            RETURN
        END

        UPDATE pessoas
        SET 
            nome = @nome,
            cpf = @cpf,
            email = @email,
            senha = @senha
        WHERE id = @id_cliente

        UPDATE clientes
        SET 
            nascimento = @nascimento,
            telefone = @telefone
        WHERE id_clientes = @id_cliente

        IF EXISTS (SELECT 1 FROM enderecos WHERE pessoa_id = @id_cliente)
        BEGIN
            UPDATE enderecos
            SET
                rua = @rua,
                numero = @numero,
                bairro = @bairro,
                cidade = @cidade,
                estado = @estado,
                cep = @cep
            WHERE pessoa_id = @id_cliente
        END
        ELSE
        BEGIN
            INSERT INTO enderecos (
                rua, numero, bairro, cidade, estado, cep, pessoa_id
            )
            VALUES (
                @rua, @numero, @bairro, @cidade, @estado, @cep, @id_cliente
            )
        END

        COMMIT TRANSACTION

    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION

        DECLARE @erro VARCHAR(500) = ERROR_MESSAGE()
        RAISERROR(@erro, 16, 1)
    END CATCH
END
GO
---

EXEC sp_editar_cliente
    @id_cliente = 3,
    @nome = 'Bruno Martins Atualizado',
    @cpf = '333.333.333-33',
    @email = 'bruno.novo@futtips.com',
    @senha = 'novaSenha123',
    @nascimento = '1995-06-15',
    @telefone = '(17) 99999-9999',
    @rua = 'Rua Nova',
    @numero = '123',
    @bairro = 'Centro',
    @cidade = 'São José do Rio Preto',
    @estado = 'SP',
    @cep = '15000-000'


--========================================
--TRIGGERS
--========================================
--TABELA PARA LOGS
CREATE TABLE log_pedidos (
    id INT IDENTITY PRIMARY KEY NOT NULL,
    id_pedido INT NOT NULL,
    protocolo VARCHAR(30) NOT NULL,
    valor MONEY NOT NULL,
    id_cliente INT NOT NULL,
    nome_cliente VARCHAR(50) NOT NULL,
    data_pedido DATE NOT NULL,
    registrado_em DATETIME NOT NULL DEFAULT GETDATE(),
    operacao VARCHAR(10) NOT NULL -- INSERT, UPDATE, DELETE
);
GO

CREATE TRIGGER trg_log_pedidos
ON pedidos
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    -- ========================================
    -- INSERT: novo pedido criado
    -- ========================================
    IF EXISTS (SELECT 1 FROM inserted)
       AND NOT EXISTS (SELECT 1 FROM deleted)
    BEGIN
        INSERT INTO log_pedidos (
            id_pedido,
            protocolo,
            valor,
            id_cliente,
            nome_cliente,
            data_pedido,
            operacao
        )
        SELECT
            i.codigo,
            i.protocolo,
            i.valor,
            i.id_cliente,
            p.nome,
            i.data_pedido,
            'INSERT'
        FROM inserted i
        INNER JOIN clientes c
            ON i.id_cliente = c.id_clientes
        INNER JOIN pessoas p
            ON c.id_clientes = p.id;
    END

    -- ========================================
    -- UPDATE: pedido atualizado
    -- ========================================
    IF EXISTS (SELECT 1 FROM inserted)
       AND EXISTS (SELECT 1 FROM deleted)
    BEGIN
        INSERT INTO log_pedidos (
            id_pedido,
            protocolo,
            valor,
            id_cliente,
            nome_cliente,
            data_pedido,
            operacao
        )
        SELECT
            i.codigo,
            i.protocolo,
            i.valor,
            i.id_cliente,
            p.nome,
            i.data_pedido,
            'UPDATE'
        FROM inserted i
        INNER JOIN clientes c
            ON i.id_cliente = c.id_clientes
        INNER JOIN pessoas p
            ON c.id_clientes = p.id;
    END

    -- ========================================
    -- DELETE: pedido removido
    -- ========================================
    IF NOT EXISTS (SELECT 1 FROM inserted)
       AND EXISTS (SELECT 1 FROM deleted)
    BEGIN
        INSERT INTO log_pedidos (
            id_pedido,
            protocolo,
            valor,
            id_cliente,
            nome_cliente,
            data_pedido,
            operacao
        )
        SELECT
            d.codigo,
            d.protocolo,
            d.valor,
            d.id_cliente,
            p.nome,
            d.data_pedido,
            'DELETE'
        FROM deleted d
        INNER JOIN clientes c
            ON d.id_cliente = c.id_clientes
        INNER JOIN pessoas p
            ON c.id_clientes = p.id;
    END
END;
GO


-- ========================================
-- BACKUP COMPLETO
-- ========================================
BACKUP DATABASE futtips
TO DISK = 'C:\Backup\futtips_backup_completo.bak'
WITH
FORMAT,
MEDIANAME = 'FuttipsBackup',
NAME = 'Backup Completo - Futtips',
DESCRIPTION = 'Backup completo do banco futtips',
STATS = 10;
GO
-- ========================================
-- BACKUP COM DATA NO NOME DO ARQUIVO
-- (útil para backups automáticos diários)
-- ========================================
DECLARE @caminho VARCHAR(255);
SET @caminho = 'C:\Backup\futtips_' +
FORMAT(GETDATE(), 'yyyyMMdd_HHmmss') +
'.bak';
BACKUP DATABASE futtips
TO DISK = @caminho
WITH
FORMAT,
NAME = 'Backup Diário - Futtips',
STATS = 10;
GO
-- ========================================
-- BACKUP DIFERENCIAL
-- (só salva o que mudou desde o último backup completo)
-- ========================================
BACKUP DATABASE futtips
TO DISK = 'C:\Backup\futtips_diferencial.bak'
WITH
DIFFERENTIAL,
NAME = 'Backup Diferencial - Futtips',
STATS = 10;
GO
-- ========================================
-- BACKUP DO LOG DE TRANSAÇÕES
-- ========================================
BACKUP LOG futtips
TO DISK = 'C:\Backup\futtips_log.bak'
WITH
NAME = 'Backup Log - Futtips',
STATS = 10;
GO
-- ========================================
-- RESTORE EM OUTRO SERVIDOR
34-- (muda o caminho dos arquivos)
-- ========================================
ALTER DATABASE futtips SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO
RESTORE DATABASE futtips
FROM DISK = 'C:\Backup\futtips_backup_completo.bak'
WITH
REPLACE,
MOVE 'futtips'
TO 'C:\SQLServer\Data\futtips.mdf',
MOVE 'futtips_log' TO 'C:\SQLServer\Log\futtips_log.ldf',
RECOVERY,
STATS = 10;
GO
ALTER DATABASE futtips SET MULTI_USER;
GO