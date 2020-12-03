# pedido-simples

Uma aplicação simples para cadastro de Pedidos e Itens de Venda, utilizando Spring (Spring Rest, Spring Data Jpa, dentre outros), Bean Validation e QueryDSL.

Desenvolvida em Java 8, está preparada para execução em ambiente de Desenvolvimento com um banco de dados em memória (H2) e em ambiente de Produção com banco de dados PostgreSQL.

## Instalação e execução

Siga os passos abaixo, dependendo do ambiente:

### Execução local/em desenvolvimento (banco H2)

```
./mvnw spring-boot:run
```

### Execução local, com configurações de produção:

```
./mvnw spring-boot:run -Pprod -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:postgresql://<sua-url>:<sua-porta>/ --spring.datasource.username=<seu-usuario> --spring.datasource.password=<sua-senha>"
```

### Criação de executável para deploy:

```
./mvnw package
```

O compilado será gerado na pasta /target, com o nome pedidosimples-{versao-atual}.jar.

### Execução do jar:

Considerando que o console se encontra na mesma pasta do pedidosimples.jar:

```
java -jar target/pedidosimples-{versao}.jar --spring.profiles.active=prod --spring.datasource.url=jdbc:postgresql://<sua-url>:<sua-porta>/ --spring.datasource.username=<seu-usuario> --spring.datasource.password=<sua-senha>
```

## Tecnologias utilizadas

- Spring (através do SpringBoot) com Java 8, utilizando a separação em camadas (Controller/Service/Repository/Model);
- JPA/Hibernate (através do Spring Data);
- QueryDSL (queries tipadas e dinâmicas, usada principalmente nos filtros);
- Bancos de dados H2 (desenvolvimento) e PostgreSQL (produção);
- Flyway (migração e versionamento da estrutura do banco de dados);
- Maven (gestão de dependências);
- Bean Validation (validação de entidades);
- Mockito, em paralelo com as facilidades de teste do próprio SpringBoot (mocks para a implementação de testes unitários);
- GitHub Actions (para integração contínua);


## Funcionamento do projeto

O projeto consiste de endpoints REST, acessíveis em {url}:{porta}/pedidosimples/api. Para o ambiente de desenvolvimento, a url padrão é localhost e a porta padrão é 8080. A migração de banco de dados é feita via Flyway, cuja verificação é feita a cada inicialização do programa. No modo desenvolvimento, por se tratar de um banco em memória, a migração completa da estrutura de banco de dados e a execução alguns scripts para carga são executadas a cada reinício.

Existem três entidades no sistema:

- ItemVenda: representa os itens que podem ser vendidos, ou seja, que podem compôr Pedidos. Podem ser classificados atualmente como Produtos ou Serviços, e possui um valor base;
- Pedido: agrupa um conjunto de ItensVenda em particular. Tem os status Aberto e Fechado; ao chegar neste último não são permitidas mais alterações. Possui também um fator de desconto, a ser aplicado em seus itens;
- ItemPedido: representa a associação entre um ItemVenda e um Pedido. Contém o valor efetivamente calculado, que é função do fator de desconto do Pedido e do tipo do ItemVenda (na configuração atual, os descontos de Pedido não se aplicam nos valores de Serviços).

Para facilitar a utilização, foi adicionada na pasta postman coleções a serem importadas no Postman, contendo todos os endpoints da aplicação.

Os endpoints disponíveis são:

### Pedido

#### GET /pedidos

Recupera os pedidos atuais do sistema, retornando numa estrutura de página (usando o recurso de Paginação do Spring Data) através dos query params page, size e sort. Permite também os seguintes parâmetros para filtrar a listagem:

- codigo (string): filtra os pedidos pelo código informado ou parte dele, um campo para identificação única de um pedido, que é preenchido pelo usuário;
- situacao ("A" | "F"): filtra os pedidos pela situação: Aberto ou Fechado, respectivamente;

#### GET /pedido/{uuid}

Busca um pedido em particular, através de seu identificador (uuid);

#### POST /pedido

Inclui um novo pedido;

#### PUT /pedido/{uuid}

Altera um pedido existente. Nem todos os campos podem ser alterados; alguns possuem operações específicas, descritas mais abaixo.

#### PUT /pedido/{uuid}/aplicar-desconto

Aplica um desconto a esse pedido, alterando os valores atuais dos itens de pedido associados a ele e impactando nos próximos que forem adicionados. Não pode ser aplicado se o pedido estiver fechado;

#### POST /pedido/{uuid}/fechar

Fecha um pedido, impedindo que novos itens sejam adicionados e que o percentual de desconto seja alterado;

#### DELETE /pedido/{uuid}

Exclui um pedido e seus itens.

### ItemVenda

#### GET /itens-venda

Busca os Itens de Venda existentes no sistema. Também possui paginação padrão e os seguintes campos para filtragem:

- nome (string): filtra pelo nome do item de venda ou por parte dele;
- valorMinimo (numérico, no formato "0.00"): exibe apenas itens de venda no valor informado ou maior;
- valorMaximo (numérico, formato "0.00"): exibe apenas itens de venda no valor informado ou menor;
- tipo ("P" | "S"): exibe apenas itens do tipo Produto ou Serviço;
- ativo ("S" | "N"): exibe apenas itens ativos ou inativos.

#### GET /item-venda/{uuid}

Busca um item de venda específico, pelo seu id.

#### POST /item-venda

Inclui um novo item de venda.

#### PUT /item-venda/{uuid}

Altera um item de venda existente.

#### DELETE /item-venda/{uuid}

Exclui um item de venda existente.

### ItemPedido

#### GET /pedido/{uuidPedido}/itens-pedido

Busca os itens do pedido com id informado. Também possui paginação padrão e os seguintes campos para filtragem:

- valorMinimo (numérico, no formato "0.00"): exibe apenas itens de pedido no valor informado ou maior;
- valorMaximo (numérico, formato "0.00"): exibe apenas itens de pedido no valor informado ou menor;
- itemVenda (string): exibe apenas os itens de pedido associados a itens de venda com o nome ou parte do nome informado

#### GET /item-pedido/{uuid}

Busca um item de pedido específico pelo seu id.

#### POST /pedido/{uuidPedido}/item-pedido

Adiciona um novo item de venda a um pedido.

#### DELETE /item-pedido/{uuid}

Exclui um item de venda.

Erros comuns desses endpoints, como falhas de validação ou requisições inválidas, são tratadas pelo sistema através de um ControllerAdvice, de forma a retornar um payload padronizado e descritivo.

O sistema conta ainda com testes unitários de todas as camadas.
