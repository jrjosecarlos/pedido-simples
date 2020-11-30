/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.ItemVenda.ParametroBuscaItemVenda;

/**
 * Criador de predicados para {@link ItemVenda}.
 *
 * @author jrjosecarlos
 *
 */
public class ItemVendaPredicateBuilder {

	private Map<ParametroBuscaItemVenda, String> mapaParametros;

	/**
	 * Cria um novo PredicateBuilder baseado num mapa de parâmetros String/String.
	 * São removidos todos os possíveis parâmetros que não estão de acordo com
	 * {@link ParametroBuscaItemVenda}, assim como os com valores vazios.
	 *
	 * @param mapaParametros um mapa de parâmetros que servirá de base para o
	 * PredicateBuilder gerado
	 * @return um novo PredicateBuilder preparado para gerar os predicados correspondentes
	 */
	public static ItemVendaPredicateBuilder of(Map<String, String> mapaParametros) {
		return new ItemVendaPredicateBuilder(mapaParametros.entrySet().stream()
				.filter(entry -> !entry.getValue().isEmpty())
				.filter(entry -> ParametroBuscaItemVenda.isParametroBuscaVenda(entry.getKey()))
				.collect(Collectors.toMap(
						entry -> ParametroBuscaItemVenda.fromValor(entry.getKey()),
						Entry::getValue)
				)
			);
	}

	protected ItemVendaPredicateBuilder(Map<ParametroBuscaItemVenda, String> mapaParametros) {
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

		for(Entry<ParametroBuscaItemVenda, String> entry : this.mapaParametros.entrySet()) {
			resultado = resultado.and(entry.getKey().getPredicate(entry.getValue()));
		}

		return resultado;
	}
}
