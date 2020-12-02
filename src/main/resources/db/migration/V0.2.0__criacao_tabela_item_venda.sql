CREATE TABLE pedido_simples.item_venda (
	id_item_venda uuid NOT NULL CONSTRAINT pk_item_venda PRIMARY KEY,
	nome varchar(100) NOT NULL,
	tipo varchar(1) NOT NULL,
	valor_base numeric(15, 2) NOT NULL,
	ativo boolean NOT NULL
)
