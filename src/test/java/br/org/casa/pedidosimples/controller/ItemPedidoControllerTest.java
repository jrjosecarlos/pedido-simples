/**
 *
 */
package br.org.casa.pedidosimples.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.exception.OperacaoInvalidaException;
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;
import br.org.casa.pedidosimples.service.ItemPedidoService;

/**
 * Classe de testes para {@link ItemPedidoController}.
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ItemPedidoController.class)
public class ItemPedidoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ItemPedidoService service;

	@Test
	public void testGetItensPedido() throws Exception {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setId(UUID.randomUUID());

		Page<ItemPedido> page = new PageImpl<>(Arrays.asList(itemPedido));

		when(service.buscarTodos(any(), any(), any()))
			.thenReturn(page);

		mvc.perform(get("/pedido/{uuidPedido}/itens-pedido", itemPedido.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content[0].id", is(itemPedido.getId().toString())));

	}

	@Test
	public void testGetItensPedidoComPedidoInexistente() throws Exception {
		UUID uuid = UUID.randomUUID();

		when(service.buscarTodos(any(), any(), any()))
			.thenThrow(new EntidadeNaoEncontradaException("Teste", uuid));

		mvc.perform(get("/pedido/{uuidPedido}/itens-pedido", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testGetItemPedidoComSucesso() throws Exception {
		Pedido pedido = new Pedido();
		pedido.setId(UUID.randomUUID());

		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setId(UUID.randomUUID());
		itemPedido.setPedido(pedido);
		itemPedido.setItemVenda(itemVenda);

		when(service.buscarPorId(itemPedido.getId()))
			.thenReturn(Optional.of(itemPedido));

		mvc.perform(get("/item-pedido/{uuid}", itemPedido.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(itemPedido.getId().toString())))
			.andExpect(jsonPath("$.itemVenda").exists())
			.andExpect(jsonPath("$.pedido").doesNotExist());
	}

	@Test
	public void testGetItemPedidoInexistente() throws Exception {
		UUID uuid = UUID.randomUUID();

		when(service.buscarPorId(uuid))
			.thenReturn(Optional.empty());

		mvc.perform(get("/item-pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPostItemPedidoEntidadeNaoEncontrada() throws Exception {
		UUID uuidPedido = UUID.randomUUID();

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		when(service.incluir(any(), any()))
			.thenThrow(new EntidadeNaoEncontradaException("ItemVenda", UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48")));

		mvc.perform(post("/pedido/{uuidPedido}/item-pedido", uuidPedido)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPostItemPedidoOperacaoInvalida() throws Exception {
		UUID uuidPedido = UUID.randomUUID();

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		when(service.incluir(any(), any()))
			.thenThrow(new OperacaoInvalidaException("mensagem de teste"));

		mvc.perform(post("/pedido/{uuidPedido}/item-pedido", uuidPedido)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", containsString("Operação inválida")))
			.andExpect(jsonPath("$.descricaoDetalhada", is("mensagem de teste")));
	}

	@Test
	public void testPostItemPedidoErroValidacao() throws Exception {
		UUID uuidPedido = UUID.randomUUID();

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		mvc.perform(post("/pedido/{uuidPedido}/item-pedido", uuidPedido)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Erro de validação")));
	}

	@Test
	public void testPostItemPedidoComSucesso() throws Exception {
		UUID uuidPedido = UUID.randomUUID();

		Pedido pedido = new Pedido();
		pedido.setId(uuidPedido);
		pedido.setSituacao(SituacaoPedido.ABERTO);
		pedido.setFatorDesconto(BigDecimal.ZERO);

		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48"));
		itemVenda.setNome("Produto Ativo 2");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("55.00"));
		itemVenda.setAtivo(true);

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setId(UUID.randomUUID());
		itemPedido.setItemVenda(itemVenda);
		itemPedido.setPedido(pedido);
		itemPedido.calcularValor();

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		when(service.incluir(any(), any()))
			.thenReturn(itemPedido);

		mvc.perform(post("/pedido/{uuidPedido}/item-pedido", uuidPedido)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(itemPedido.getId().toString())))
			.andExpect(header().string("Location", endsWith("/item-pedido/" + itemPedido.getId())) );
	}

	@Test
	public void testExcluirItemPedidoInexistente() throws Exception {
		UUID uuid = UUID.randomUUID();

		doThrow(new EntidadeNaoEncontradaException("ItemPedido", uuid))
			.when(service).excluir(uuid);

		mvc.perform(delete("/item-pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testExcluirItemPedidoComSucesso() throws Exception {
		UUID uuid = UUID.randomUUID();

		doNothing().when(service).excluir(uuid);

		mvc.perform(delete("/item-pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$").doesNotExist());
	}

}
