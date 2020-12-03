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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;
import br.org.casa.pedidosimples.service.ItemVendaService;

/**
 * Classe de testes para {@link ItemVendaController}.
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ItemVendaController.class)
public class ItemVendaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ItemVendaService service;

	@Test
	public void testGetItensVenda() throws Exception {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());

		Page<ItemVenda> page = new PageImpl<>(Arrays.asList(itemVenda));

		when(service.buscarTodos(any(), any()))
			.thenReturn(page);

		mvc.perform(get("/itens-venda", itemVenda.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content[0].id", is(itemVenda.getId().toString())));
	}

	@Test
	public void testGetItemVendaComSucesso() throws Exception {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());

		when(service.buscarPorId(itemVenda.getId()))
			.thenReturn(Optional.of(itemVenda));

		mvc.perform(get("/item-venda/{uuid}", itemVenda.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(itemVenda.getId().toString())));
	}

	@Test
	public void testGetItemVendaInexistente() throws Exception {
		UUID uuid = UUID.randomUUID();

		when(service.buscarPorId(uuid))
			.thenReturn(Optional.empty());

		mvc.perform(get("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPostItemVendaErroValidacao() throws Exception {
		String payload = "{"
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";
		mvc.perform(post("/item-venda")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Erro de validação")));
	}

	@Test
	public void testPostItemVendaComSucesso() throws Exception {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());
		itemVenda.setNome("Produto Ativo 2");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("55.00"));
		itemVenda.setAtivo(true);

		String payload = "{"
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		when(service.incluir(any()))
			.thenReturn(itemVenda);

		mvc.perform(post("/item-venda")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(itemVenda.getId().toString())))
			.andExpect(header().string("Location", endsWith("/item-venda/" + itemVenda.getId())) );
	}

	@Test
	public void testPutItemVendaIdUrlDiferente() throws Exception {
		UUID uuid = UUID.fromString("c92a17bc-b0cb-4f37-baf8-177d4a2b83ba");

		/*ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());
		itemVenda.setNome("Produto Ativo 2");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("55.00"));
		itemVenda.setAtivo(true);*/

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";


		mvc.perform(put("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Requisição inválida")));
	}

	@Test
	public void testPutItemVendaErroValidacao() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		/*ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());
		itemVenda.setNome("Produto Ativo 2");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("55.00"));
		itemVenda.setAtivo(true);*/

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";


		mvc.perform(put("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Erro de validação")));
	}

	@Test
	public void testPutItemVendaEntidadeNaoEncontrada() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		/*ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());
		itemVenda.setNome("Produto Ativo 2");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("55.00"));
		itemVenda.setAtivo(true);*/

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		when(service.alterar(any(), any()))
			.thenThrow(new EntidadeNaoEncontradaException("EntidadeTeste", uuid));

		mvc.perform(put("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPutItemVendaOperacaoInvalida() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		/*ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(UUID.randomUUID());
		itemVenda.setNome("Produto Ativo 2");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("55.00"));
		itemVenda.setAtivo(true);*/

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		when(service.alterar(any(), any()))
			.thenThrow(new OperacaoInvalidaException("mensagem de teste"));

		mvc.perform(put("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", containsString("Operação inválida")))
			.andExpect(jsonPath("$.descricaoDetalhada", is("mensagem de teste")));
	}

	@Test
	public void testPutItemVendaComSucesso() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setId(uuid);
		itemVenda.setNome("Produto Ativo 2");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("55.00"));
		itemVenda.setAtivo(true);

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"nome\": \"Produto Ativo 2\","
				+ "    \"tipo\": \"P\","
				+ "    \"valorBase\": 55.00,"
				+ "    \"ativo\": true"
				+ "}";

		when(service.alterar(any(), any()))
			.thenReturn(itemVenda);

		mvc.perform(put("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(itemVenda.getId().toString())));
	}

	@Test
	public void testExcluirItemVendaInexistente() throws Exception {
		UUID uuid = UUID.randomUUID();

		doThrow(new EntidadeNaoEncontradaException("ItemVenda", uuid))
			.when(service).excluir(uuid);

		mvc.perform(delete("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testExcluirItemVendaComSucesso() throws Exception {
		UUID uuid = UUID.randomUUID();

		doNothing().when(service).excluir(uuid);

		mvc.perform(delete("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$").doesNotExist());
	}

}
