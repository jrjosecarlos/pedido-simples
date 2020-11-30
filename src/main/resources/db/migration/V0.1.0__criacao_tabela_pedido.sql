CREATE TABLE pedido_simples.pedido (
	id_pedido uuid NOT NULL CONSTRAINT pk_pedido PRIMARY KEY,
	codigo VARCHAR(8) NOT NULL CONSTRAINT uq_pedido_codigo_pedido UNIQUE,
	fator_desconto NUMERIC(3, 2) NOT NULL,
	situacao VARCHAR(1) NOT NULL
)
