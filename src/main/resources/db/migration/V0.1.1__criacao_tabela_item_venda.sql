create table pedido_simples.item_venda (
	id_item_venda uuid not null constraint pk_item_venda primary key,
	nome varchar(100) not null,
	tipo varchar(1) not null,
	valor_base numeric(15, 2) not null,
	ativo boolean not null
)
