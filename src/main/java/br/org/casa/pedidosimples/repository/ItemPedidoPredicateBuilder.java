/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemPedido.ParametroBuscaItemPedido;

/**
 * Criador de predicados para {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
public class ItemPedidoPredicateBuilder {
	private Map<ParametroBuscaItemPedido, String> mapaParametros;

	/**
	 * Cria um novo PredicateBuilder baseado num mapa de parâmetros String/String.
	 * São removidos todos os possíveis parâmetros que não estão de acordo com
	 * {@link ParametroBuscaItemPedido}, assim como os com valores vazios.
	 *
	 * @param mapaParametros um mapa de parâmetros que servirá de base para o
	 * PredicateBuilder gerado
	 * @return um novo PredicateBuilder preparado para gerar os predicados correspondentes
	 */
	public static ItemPedidoPredicateBuilder of(Map<String, String> mapaParametros) {
		return new ItemPedidoPredicateBuilder(mapaParametros.entrySet().stream()
				.filter(entry -> !entry.getValue().isEmpty())
				.filter(entry -> ParametroBuscaItemPedido.isParametroBuscaItemPedido(entry.getKey()))
				.collect(Collectors.toMap(
						entry -> ParametroBuscaItemPedido.fromValor(entry.getKey()),
						Entry::getValue)
				)
			);
	}

	protected ItemPedidoPredicateBuilder(Map<ParametroBuscaItemPedido, String> mapaParametros) {
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

		for(Entry<ParametroBuscaItemPedido, String> entry : this.mapaParametros.entrySet()) {
			resultado = resultado.and(entry.getKey().getPredicate(entry.getValue()));
		}

		return resultado;
	}
}
