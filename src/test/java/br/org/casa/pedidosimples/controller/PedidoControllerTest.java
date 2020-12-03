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
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;
import br.org.casa.pedidosimples.service.PedidoService;

/**
 * Classe de testes de {@link PedidoController}.
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private PedidoService service;

	@Test
	public void testGetPedidos() throws Exception {
		Pedido pedido = new Pedido();
		pedido.setId(UUID.randomUUID());

		Page<Pedido> page = new PageImpl<>(Arrays.asList(pedido));

		when(service.buscarTodos(any(), any()))
			.thenReturn(page);

		mvc.perform(get("/pedidos", pedido.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content[0].id", is(pedido.getId().toString())));
	}

	@Test
	public void testGetPedidoComSucesso() throws Exception {
		Pedido pedido = new Pedido();
		pedido.setId(UUID.randomUUID());
		pedido.setFatorDesconto(new BigDecimal("0.00"));

		ItemVenda itemVenda1 = new ItemVenda();
		itemVenda1.setTipo(TipoItemVenda.PRODUTO);
		itemVenda1.setValorBase(new BigDecimal("5.00"));

		ItemPedido itemPedido1 = new ItemPedido();
		itemPedido1.setPedido(pedido);
		itemPedido1.setItemVenda(itemVenda1);
		itemPedido1.calcularValor();

		ItemVenda itemVenda2 = new ItemVenda();
		itemVenda2.setTipo(TipoItemVenda.SERVICO);
		itemVenda2.setValorBase(new BigDecimal("15.00"));

		ItemPedido itemPedido2 = new ItemPedido();
		itemPedido2.setPedido(pedido);
		itemPedido2.setItemVenda(itemVenda2);
		itemPedido2.calcularValor();

		pedido.setItensPedido(Arrays.asList(itemPedido1, itemPedido2));

		when(service.buscarPorId(pedido.getId()))
			.thenReturn(Optional.of(pedido));

		mvc.perform(get("/pedido/{uuid}", pedido.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(pedido.getId().toString())))
			.andExpect(jsonPath("$.valorTotal", is(20.00)));
	}

	@Test
	public void testGetPedidoInexistente() throws Exception {
		UUID uuid = UUID.randomUUID();

		when(service.buscarPorId(uuid))
			.thenReturn(Optional.empty());

		mvc.perform(get("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPostPedidoErroValidacao() throws Exception {
		String payload = "{"
				+ "    \"fatorDesconto\": 0.75,"
				+ "    \"situacao\": \"A\""
				+ "}";

		mvc.perform(post("/pedido")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Erro de validação")));
	}

	@Test
	public void testPostPedidoComSucesso() throws Exception {
		Pedido pedido = new Pedido();
		pedido.setId(UUID.randomUUID());
		pedido.setCodigo("00010002");
		pedido.setFatorDesconto(new BigDecimal("0.75"));
		pedido.setSituacao(SituacaoPedido.ABERTO);

		String payload = "{"
				+ "    \"codigo\": \"00010002\","
				+ "    \"fatorDesconto\": 0.75,"
				+ "    \"situacao\": \"A\""
				+ "}";

		when(service.incluir(any()))
			.thenReturn(pedido);

		mvc.perform(post("/pedido")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(pedido.getId().toString())))
			.andExpect(header().string("Location", endsWith("/pedido/" + pedido.getId())) );
	}

	@Test
	public void testPutPedidoIdUrlDiferente() throws Exception {
		UUID uuid = UUID.fromString("c92a17bc-b0cb-4f37-baf8-177d4a2b83ba");

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"codigo\": \"00010003\","
				+ "    \"fatorDesconto\": 0.75,"
				+ "    \"situacao\": \"A\""
				+ "}";


		mvc.perform(put("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Requisição inválida")));
	}

	@Test
	public void testPutPedidoErroValidacao() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"fatorDesconto\": 0.75,"
				+ "    \"situacao\": \"A\""
				+ "}";


		mvc.perform(put("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Erro de validação")));
	}

	@Test
	public void testPutPedidoEntidadeNaoEncontrada() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"codigo\": \"00010003\","
				+ "    \"fatorDesconto\": 0.75,"
				+ "    \"situacao\": \"A\""
				+ "}";

		when(service.alterar(any(), any()))
			.thenThrow(new EntidadeNaoEncontradaException("EntidadeTeste", uuid));

		mvc.perform(put("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPutPedidoOperacaoInvalida() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"codigo\": \"00010003\","
				+ "    \"fatorDesconto\": 0.75,"
				+ "    \"situacao\": \"A\""
				+ "}";

		when(service.alterar(any(), any()))
			.thenThrow(new OperacaoInvalidaException("mensagem de teste"));

		mvc.perform(put("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", containsString("Operação inválida")))
			.andExpect(jsonPath("$.descricaoDetalhada", is("mensagem de teste")));
	}

	@Test
	public void testPutPedidoComSucesso() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		Pedido pedido = new Pedido();
		pedido.setId(uuid);
		pedido.setCodigo("00010003");
		pedido.setFatorDesconto(new BigDecimal("0.75"));
		pedido.setSituacao(SituacaoPedido.ABERTO);

		String payload = "{"
				+ "    \"id\": \"a97245b4-566d-4cfd-9d89-7492dc5c6a48\","
				+ "    \"codigo\": \"00010003\","
				+ "    \"fatorDesconto\": 0.75,"
				+ "    \"situacao\": \"A\""
				+ "}";

		when(service.alterar(any(), any()))
			.thenReturn(pedido);

		mvc.perform(put("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(pedido.getId().toString())));
	}

	@Test
	public void testPutPedidoAplicarDescontoErroValidacao() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		String payload = "{"
				+ "    \"fatorDesconto\": 2.00"
				+ "}";

		mvc.perform(put("/pedido/{uuid}/aplicar-desconto", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", is("Erro de validação")));
	}

	@Test
	public void testPutPedidoAplicarDescontoEntidadeNaoEncontrada() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		String payload = "{"
				+ "    \"fatorDesconto\": 0.20"
				+ "}";

		when(service.aplicarDesconto(any(), any()))
			.thenThrow(new EntidadeNaoEncontradaException("EntidadeTeste", uuid));

		mvc.perform(put("/pedido/{uuid}/aplicar-desconto", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPutPedidoAplicarDescontoOperacaoInvalida() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		String payload = "{"
				+ "    \"fatorDesconto\": 0.20"
				+ "}";

		when(service.aplicarDesconto(any(), any()))
			.thenThrow(new OperacaoInvalidaException("mensagem de teste"));

		mvc.perform(put("/pedido/{uuid}/aplicar-desconto", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", containsString("Operação inválida")))
			.andExpect(jsonPath("$.descricaoDetalhada", is("mensagem de teste")));
	}

	@Test
	public void testPutPedidoAplicarDescontoComSucesso() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		Pedido pedido = new Pedido();
		pedido.setId(uuid);

		String payload = "{"
				+ "    \"fatorDesconto\": 0.20"
				+ "}";

		when(service.aplicarDesconto(any(), any()))
			.thenReturn(pedido);

		mvc.perform(put("/pedido/{uuid}/aplicar-desconto", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(pedido.getId().toString())));
	}

	@Test
	public void testPostPedidoFecharEntidadeNaoEncontrada() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		when(service.fechar(any()))
			.thenThrow(new EntidadeNaoEncontradaException("EntidadeTeste", uuid));

		mvc.perform(post("/pedido/{uuid}/fechar", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testPostPedidoFecharOperacaoInvalida() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		when(service.fechar(any()))
			.thenThrow(new OperacaoInvalidaException("mensagem de teste"));

		mvc.perform(post("/pedido/{uuid}/fechar", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codigoStatus", is(400)))
			.andExpect(jsonPath("$.mensagem", containsString("Operação inválida")))
			.andExpect(jsonPath("$.descricaoDetalhada", is("mensagem de teste")));
	}

	@Test
	public void testPostPedidoFecharComSucesso() throws Exception {
		UUID uuid = UUID.fromString("a97245b4-566d-4cfd-9d89-7492dc5c6a48");

		Pedido pedido = new Pedido();
		pedido.setId(uuid);

		when(service.fechar(any()))
			.thenReturn(pedido);

		mvc.perform(post("/pedido/{uuid}/fechar", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(pedido.getId().toString())));
	}

	@Test
	public void testExcluirPedidoInexistente() throws Exception {
		UUID uuid = UUID.randomUUID();

		doThrow(new EntidadeNaoEncontradaException("Pedido", uuid))
			.when(service).excluir(uuid);

		mvc.perform(delete("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.codigoStatus", is(404)))
			.andExpect(jsonPath("$.mensagem", containsString("não encontrada")));
	}

	@Test
	public void testExcluirPedidoComSucesso() throws Exception {
		UUID uuid = UUID.randomUUID();

		doNothing().when(service).excluir(uuid);

		mvc.perform(delete("/pedido/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$").doesNotExist());
	}
}
