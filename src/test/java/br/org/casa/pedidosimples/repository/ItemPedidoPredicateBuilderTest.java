/**
 *
 */
package br.org.casa.pedidosimples.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import br.org.casa.pedidosimples.exception.ParametroBuscaParseException;
import br.org.casa.pedidosimples.model.QItemPedido;

/**
 * Classe de testes para {@link ItemPedidoPredicateBuilder}.
 *
 * @author jrjosecarlos
 *
 */
public class ItemPedidoPredicateBuilderTest {
	@Test
	public void testItemPedidoPredicateBuilderOfComMapaVazio() {
		Map<String, String> parametros = new HashMap<>();

		ItemPedidoPredicateBuilder builder = ItemPedidoPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue();

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemPedidoPredicateBuilderOfComItemVenda() {
		Map<String, String> parametros = Collections.singletonMap("itemVenda", "teste");

		ItemPedidoPredicateBuilder builder = ItemPedidoPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemPedido.itemPedido.itemVenda.nome.containsIgnoreCase("teste"));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemPedidoPredicateBuilderOfComValorMinimo() {
		Map<String, String> parametros = Collections.singletonMap("valorMinimo", "1.23");

		ItemPedidoPredicateBuilder builder = ItemPedidoPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemPedido.itemPedido.valor.goe(new BigDecimal("1.23")));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemPedidoPredicateBuilderOfComValorMinimoInvalido() {
		Map<String, String> parametros = Collections.singletonMap("valorMinimo", "abc");

		ItemPedidoPredicateBuilder builder = ItemPedidoPredicateBuilder.of(parametros);

		assertThatExceptionOfType(ParametroBuscaParseException.class)
			.isThrownBy(() -> builder.build())
			.withMessageContaining("valorMinimo")
			.satisfies(ex -> {
				assertThat(ex.getDetailedMessage())
					.contains("abc");
			});
	}

	@Test
	public void testItemPedidoPredicateBuilderOfComValorMaximo() {
		Map<String, String> parametros = Collections.singletonMap("valorMaximo", "3.21");

		ItemPedidoPredicateBuilder builder = ItemPedidoPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemPedido.itemPedido.valor.loe(new BigDecimal("3.21")));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemPedidoPredicateBuilderOfComValorMaximoInvalido() {
		Map<String, String> parametros = Collections.singletonMap("valorMinimo", "xyz");

		ItemPedidoPredicateBuilder builder = ItemPedidoPredicateBuilder.of(parametros);

		assertThatExceptionOfType(ParametroBuscaParseException.class)
			.isThrownBy(() -> builder.build())
			.withMessageContaining("valorMinimo")
			.satisfies(ex -> {
				assertThat(ex.getDetailedMessage())
					.contains("xyz");
			});
	}
}
