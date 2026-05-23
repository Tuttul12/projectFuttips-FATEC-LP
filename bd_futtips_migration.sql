use futtips
GO

-- Adiciona controle de ativo/inativo para pessoas.
IF COL_LENGTH('pessoas', 'ativo') IS NULL
BEGIN
    ALTER TABLE pessoas ADD ativo bit NOT NULL CONSTRAINT df_pessoas_ativo DEFAULT 1
END
GO

-- Adiciona controle de estoque para camisas.
IF COL_LENGTH('camisas', 'quantidade') IS NULL
BEGIN
    ALTER TABLE camisas ADD quantidade int NOT NULL CONSTRAINT df_camisas_quantidade DEFAULT 0
END
GO

-- Garante que o estoque não fique negativo.
IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'ck_camisas_quantidade')
BEGIN
    ALTER TABLE camisas ADD CONSTRAINT ck_camisas_quantidade CHECK (quantidade >= 0)
END
GO

-- Garante que itens de pedido sempre tenham quantidade positiva.
IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'ck_itens_pedidos_qtd')
BEGIN
    ALTER TABLE itens_pedidos ADD CONSTRAINT ck_itens_pedidos_qtd CHECK (qtd > 0)
END
GO

-- Ajuste inicial opcional de estoque para registros já cadastrados.
-- Altere os valores conforme o estoque real antes de liberar a venda.
-- Exemplo:
-- UPDATE camisas SET quantidade = 10 WHERE quantidade = 0;
