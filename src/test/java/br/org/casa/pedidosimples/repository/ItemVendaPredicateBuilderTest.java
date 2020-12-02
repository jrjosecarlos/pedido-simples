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
import br.org.casa.pedidosimples.model.QItemVenda;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;

/**
 * Classe de testes para {@link ItemVendaPredicateBuilder}.
 *
 * @author jrjosecarlos
 *
 */
public class ItemVendaPredicateBuilderTest {
	@Test
	public void testItemVendaPredicateBuilderOfComMapaVazio() {
		Map<String, String> parametros = new HashMap<>();

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue();

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemVendaPredicateBuilderOfComNome() {
		Map<String, String> parametros = Collections.singletonMap("nome", "teste");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemVenda.itemVenda.nome.containsIgnoreCase("teste"));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemVendaPredicateBuilderOfComValorMinimo() {
		Map<String, String> parametros = Collections.singletonMap("valorMinimo", "1.23");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemVenda.itemVenda.valorBase.goe(new BigDecimal("1.23")));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemVendaPredicateBuilderOfComValorMinimoInvalido() {
		Map<String, String> parametros = Collections.singletonMap("valorMinimo", "abc");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		assertThatExceptionOfType(ParametroBuscaParseException.class)
			.isThrownBy(() -> builder.build())
			.withMessageContaining("valorMinimo")
			.satisfies(ex -> {
				assertThat(ex.getDetailedMessage())
					.contains("abc");
			});
	}

	@Test
	public void testItemVendaPredicateBuilderOfComValorMaximo() {
		Map<String, String> parametros = Collections.singletonMap("valorMaximo", "3.21");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemVenda.itemVenda.valorBase.loe(new BigDecimal("3.21")));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemVendaPredicateBuilderOfComValorMaximoInvalido() {
		Map<String, String> parametros = Collections.singletonMap("valorMinimo", "xyz");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		assertThatExceptionOfType(ParametroBuscaParseException.class)
			.isThrownBy(() -> builder.build())
			.withMessageContaining("valorMinimo")
			.satisfies(ex -> {
				assertThat(ex.getDetailedMessage())
					.contains("xyz");
			});
	}

	@Test
	public void testItemVendaPredicateBuilderOfComTipo() {
		Map<String, String> parametros = Collections.singletonMap("tipo", "S");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemVenda.itemVenda.tipo.eq(TipoItemVenda.SERVICO));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemVendaPredicateBuilderOfComTipoInvalido() {
		Map<String, String> parametros = Collections.singletonMap("tipo", "W");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		assertThatExceptionOfType(ParametroBuscaParseException.class)
			.isThrownBy(() -> builder.build())
			.withMessageContaining("tipo")
			.satisfies(ex -> {
				assertThat(ex.getDetailedMessage())
					.contains("W");
			});
	}

	@Test
	public void testItemVendaPredicateBuilderOfComAtivoSim() {
		Map<String, String> parametros = Collections.singletonMap("ativo", "S");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemVenda.itemVenda.ativo.eq(Expressions.asBoolean(true)));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemVendaPredicateBuilderOfComAtivoNao() {
		Map<String, String> parametros = Collections.singletonMap("ativo", "N");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QItemVenda.itemVenda.ativo.eq(Expressions.asBoolean(false)));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testItemVendaPredicateBuilderOfComAtivoInvalido() {
		Map<String, String> parametros = Collections.singletonMap("ativo", "Y");

		ItemVendaPredicateBuilder builder = ItemVendaPredicateBuilder.of(parametros);

		assertThatExceptionOfType(ParametroBuscaParseException.class)
			.isThrownBy(() -> builder.build())
			.withMessageContaining("ativo")
			.satisfies(ex -> {
				assertThat(ex.getDetailedMessage())
					.contains("Y");
			});
	}
}
