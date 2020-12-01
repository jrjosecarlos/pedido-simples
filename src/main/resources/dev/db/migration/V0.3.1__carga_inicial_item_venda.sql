-- Adiciona alguns dados iniciais para desenvolvimento de testes
insert into pedido_simples.item_venda (id_item_venda, nome, tipo, valor_base, ativo) values
('d1293916-d146-4805-8756-ec1527d2c3a2', 'Produto Ativo 1', 'P', 10.00, true),
('0f371515-71ca-4f39-acb5-2f9ce923edc7', 'Serviço Ativo 1', 'S', 25.44, true),
('a97245b4-566d-4cfd-9d89-7492dc5c6a48', 'Produto Ativo 2', 'P', 55.00, true),
('f19c4431-4192-4daa-a2f5-8c6d8b29798c', 'Produto Inativo 1', 'P', 35.98, false),
('7e4f480f-d06a-42c7-a9ea-4de63a22c5c5', 'Serviço Inativo 1', 'S', 2300.00, false)
