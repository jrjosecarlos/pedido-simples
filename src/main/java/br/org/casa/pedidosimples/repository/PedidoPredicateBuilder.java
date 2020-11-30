/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.Pedido.ParametroBuscaPedido;

/**
 * Criador de predicados para {@link Pedido}.
 *
 * @author jrjosecarlos
 *
 */
public class PedidoPredicateBuilder {
	private Map<ParametroBuscaPedido, String> mapaParametros;

	/**
	 * Cria um novo PredicateBuilder baseado num mapa de parâmetros String/String.
	 * São removidos todos os possíveis parâmetros que não estão de acordo com
	 * {@link ParametroBuscaPedido}, assim como os com valores vazios.
	 *
	 * @param mapaParametros um mapa de parâmetros que servirá de base para o
	 * PredicateBuilder gerado
	 * @return um novo PredicateBuilder preparado para gerar os predicados correspondentes
	 */
	public static PedidoPredicateBuilder of(Map<String, String> mapaParametros) {
		return new PedidoPredicateBuilder(mapaParametros.entrySet().stream()
				.filter(entry -> !entry.getValue().isEmpty())
				.filter(entry -> ParametroBuscaPedido.isParametroBuscaPedido(entry.getKey()))
				.collect(Collectors.toMap(
						entry -> ParametroBuscaPedido.fromValor(entry.getKey()),
						Entry::getValue)
				)
			);
	}

	protected PedidoPredicateBuilder(Map<ParametroBuscaPedido, String> mapaParametros) {
		this.mapaParametros = mapaParametros;
	}

	/**
	 * Cria o conjunto de predicados correspondente ao mapa de parâmetros informado.
	 *
	 * @return um {@link BooleanExpression} pronto para ser utilizado em queries de busca,
	 * contendo todos os critérios de busca associados aos parâmetros informados.
	 */
	public BooleanExpression build() {
		BooleanExpression resultado = Expressions.asBoolean(true).isTrue(); // 1 = 1

		for(Entry<ParametroBuscaPedido, String> entry : this.mapaParametros.entrySet()) {
			resultado = resultado.and(entry.getKey().getPredicate(entry.getValue()));
		}

		return resultado;
	}
}
