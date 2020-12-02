/**
 *
 */
package br.org.casa.pedidosimples.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import br.org.casa.pedidosimples.exception.ParametroBuscaParseException;
import br.org.casa.pedidosimples.model.QPedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;

/**
 * Classe de testes para {@link PedidoPredicateBuilder}.
 *
 * @author jrjosecarlos
 *
 */
public class PedidoPredicateBuilderTest {
	@Test
	public void testPedidoPredicateBuilderOfComMapaVazio() {
		Map<String, String> parametros = new HashMap<>();

		PedidoPredicateBuilder builder = PedidoPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue();

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testPedidoPredicateBuilderOfComCodigo() {
		Map<String, String> parametros = Collections.singletonMap("codigo", "00012");

		PedidoPredicateBuilder builder = PedidoPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QPedido.pedido.codigo.containsIgnoreCase("00012"));

		assertThat(predicate)
			.isEqualTo(expected);
	}
	@Test
	public void testPedidoPredicateBuilderOfComSituacao() {
		Map<String, String> parametros = Collections.singletonMap("situacao", "A");

		PedidoPredicateBuilder builder = PedidoPredicateBuilder.of(parametros);

		BooleanExpression predicate = builder.build();

		BooleanExpression expected = Expressions.asBoolean(true).isTrue()
				.and(QPedido.pedido.situacao.eq(SituacaoPedido.ABERTO));

		assertThat(predicate)
			.isEqualTo(expected);
	}

	@Test
	public void testPedidoPredicateBuilderOfComSituacaoInvalida() {
		Map<String, String> parametros = Collections.singletonMap("situacao", "Z");

		PedidoPredicateBuilder builder = PedidoPredicateBuilder.of(parametros);

		assertThatExceptionOfType(ParametroBuscaParseException.class)
			.isThrownBy(() -> builder.build())
			.withMessageContaining("situacao")
			.satisfies(ex -> {
				assertThat(ex.getDetailedMessage())
					.contains("Z");
			});
	}
}
