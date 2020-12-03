/**
 *
 */
package br.org.casa.pedidosimples.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.casa.pedidosimples.PedidosimplesApplication;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;

/**
 * Testes de integração envolvendo {@link ItemVendaController}.
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = PedidosimplesApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemVendaControllerIntegrationTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testCrudItemVenda() throws Exception {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setAtivo(true);
		itemVenda.setNome("item-teste");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("10.00"));

		MvcResult result = mvc.perform(post("/item-venda")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(itemVenda))
				)
			.andExpect(status().isCreated())
			.andReturn();

		ItemVenda retornado = mapper.readValue(result.getResponse().getContentAsString(), ItemVenda.class);
		assertThat(retornado)
			.satisfies(r -> {
				assertThat(r.getNome())
					.isEqualTo(itemVenda.getNome());
			});

		UUID uuid = retornado.getId();

		mvc.perform(get("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(uuid.toString())));

		retornado.setNome("item-teste-alterado");

		mvc.perform(put("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(retornado)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome", is("item-teste-alterado")));

		mvc.perform(get("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome", is("item-teste-alterado")));

		mvc.perform(delete("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());

		mvc.perform(get("/item-venda/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
}
