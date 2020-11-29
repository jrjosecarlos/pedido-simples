CREATE TABLE pedido_simples.item_pedido (
	id_item_pedido uuid NOT NULL CONSTRAINT pk_item_pedido PRIMARY KEY,
	pedido_id uuid NOT NULL,
	item_venda_id uuid NOT NULL,
	valor numeric(15, 2) NOT NULL,
	CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id)
		REFERENCES pedido_simples.pedido(id_pedido),
	CONSTRAINT fk_item_pedido_item_venda FOREIGN KEY (item_venda_id)
		REFERENCES pedido_simples.item_venda(id_item_venda)
)
