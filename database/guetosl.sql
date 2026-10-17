CREATE DATABASE guetosl;
GO
 
USE guetosl;
GO
 
CREATE TABLE Cliente ( 
	id		INT IDENTITY(1,1) PRIMARY KEY,
	nome	VARCHAR(100) NOT NULL,
	email   VARCHAR(150) NOT NULL UNIQUE,
	telefone VARCHAR(20) NOT NULL
	);
 
 
	CREATE TABLE Categoria (
		id	INT IDENTITY(1,1) PRIMARY KEY,
		nome	VARCHAR(50) NOT NULL UNIQUE
);
 
 
CREATE TABLE Produto (
	id		INT	IDENTITY(1,1) PRIMARY KEY,
	nome	VARCHAR(100) NOT NULL,
	descricao VARCHAR(255) NOT NULL,
	preco	DECIMAL(10,2) NOT NULL CHECK (preco >=0),
	tamanho VARCHAR NOT NULL CHECK (tamanho IN ('P', 'M', 'G', 'GG')),
	cor		VARCHAR(30) NOT NULL,
	estoque	INT NOT NULL CHECK (estoque >= 0),
	categoria_id INT NOT NULL,
 
	CONSTRAINT FK_Produto_Categoria
		FOREIGN KEY (categoria_id)
		REFERENCES Categoria(id)
);
 
CREATE TABLE Pedido (
	id		INT IDENTITY(1,1) PRIMARY KEY,
	data_pedido		DATETIME NOT NULL DEFAULT GETDATE(),
	situacao VARCHAR(20) NOT NULL,
	cliente_id int NOT NULL,
 
		CONSTRAINT FK_Pedido_Cliente
			FOREIGN KEY (cliente_id)
			REFERENCES Cliente(id)
);
 
 
CREATE TABLE ItemPedido (
	id		INT IDENTITY(1,1) PRIMARY KEY,
	quantidade	INT NOT NULL CHECK (quantidade > 0),
	preco_unitario	DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
	pedido_id INT NOT NULL,
	produto_id INT NOT NULL,
 
		CONSTRAINT FK_ItemPedido_Pedido
			FOREIGN KEY (pedido_id)
			REFERENCES Pedido(id),
 
		CONSTRAINT FK_ItemPedido_Produto
			FOREIGN KEY (produto_id)
			REFERENCES Produto(id)
);

-- Cria a tabela de imagens associadas aos produtos.
CREATE TABLE dbo.Imagem (
    id INT IDENTITY(1,1) PRIMARY KEY,

    -- Caminho do arquivo salvo pelo backend.
    caminho NVARCHAR(500) NOT NULL,

    -- Identifica o produto dono da imagem.
    produto_id INT NOT NULL,

    -- Cada produto pode ter, no máximo, uma imagem.
    CONSTRAINT UQ_Imagem_Produto UNIQUE (produto_id),

    -- Relaciona a imagem com a tabela Produto.
    CONSTRAINT FK_Imagem_Produto
        FOREIGN KEY (produto_id)
        REFERENCES dbo.Produto(id)
);
 
 
/* Pesquisar as tabelas*/
SELECT * FROM Cliente
SELECT * FROM Categoria
SELECT * FROM Produto
SELECT * FROM Pedido
SELECT * FROM ItemPedido
 
 
 
INSERT INTO Cliente (nome, email, telefone)
VALUES
('João Silva', 'joao@email.com', '11999999999'),
('Maria Santos', 'maria@email.com', '11988888888');
 
 
INSERT INTO Produto
(nome, descricao, preco, tamanho, cor, estoque, categoria_id)
VALUES
('Moletom Preto', 'Moletom na cor preta', 119.90, 'M', 'Preto', 10, 3),
('Moletom Branco', 'Moletom na cor branca', 119.90, 'M', 'Branco', 10, 3),
('Moletom Cinza', 'Moletom na cor cinza', 119.90, 'M', 'Cinza', 10, 3); 
 
INSERT INTO Categoria (nome)
VALUES
	('Bermuda'),
	('Camiseta'),
	('Moletom');



INSERT INTO Pedido (situacao, cliente_id)
VALUES ('Pendente', 1);
	


	INSERT INTO ItemPedido (quantidade, preco_unitario, pedido_id, produto_id)
SELECT 2, preco, 1, id
FROM Produto
WHERE id = 2;




	SELECT 
    COLUMN_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Produto';

SELECT id, nome, preco
FROM Produto;



SELECT
    id,
    nome,
    categoria_id
FROM Produto;





-- Seleciona o banco de dados da GuetoSL.
USE guetosl;
GO

-- Corrige a cor da Camiseta Cinza (produto ID 13).
UPDATE dbo.Produto
SET cor = 'Cinza'
WHERE id = 13
  AND nome = 'Camiseta cinza';

-- Confere se a alteração foi realizada.
SELECT id, nome, cor
FROM dbo.Produto
WHERE id = 13;
GO

ALTER TABLE dbo.Cliente
ADD senha_hash VARCHAR(100) NULL;
GO

CREATE TABLE dbo.MensagemContato (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    mensagem VARCHAR(2000) NOT NULL,
    data_envio DATETIME NOT NULL DEFAULT GETDATE()
);
GO

SELECT *
FROM dbo.MensagemContato
ORDER BY id DESC;

ALTER TABLE dbo.Imagem
DROP CONSTRAINT UQ_Imagem_Produto;