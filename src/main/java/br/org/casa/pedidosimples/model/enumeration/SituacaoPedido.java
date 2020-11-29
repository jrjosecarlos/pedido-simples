package br.org.casa.pedidosimples.model.enumeration;

import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.util.EnumUtil;

/**
 * Representa o estado de um {@link Pedido}. Determinadas operações para Pedidos
 * só podem executadas se ele estiver num certo estado.
 *
 * @author jrjosecarlos
 *
 */
public enum SituacaoPedido {

	/**
	 * Representa um pedido que está em aberto. Nesse estado, os itens do pedido podem
	 * ser editados, assim como o fator de desconto aplicado.
	 */
	ABERTO("A"),

	/**
	 * Representa um pedido já finalizado. Nesse estado não é possível alterar os itens
	 * ou o fator de desconto aplicado. Os valores dos itens de pedido também não podem
	 * ser alterados.
	 */
	FECHADO("F");

	private String valor;

	private SituacaoPedido(String valor) {
		this.valor = valor;
	}

	/**
	 * Retorna o valor atual do campo valor.
	 *
	 * @return valor de valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Converte um valor informado no {@link SituacaoPedido} correspondente.
	 *
	 * @param valor o valor a ser convertido no elemento do enum correspondente
	 * @return o SituacaoPedido correspondente ao valor informado
	 * @throws NullPointerException se {@code valor} for {@code null}
	 * @throws IllegalArgumentException se não houver nenhum SituacaoPedido com
	 * o valor informado
	 */
	public static SituacaoPedido fromValor(String valor) {
		return EnumUtil.enumFromValue(SituacaoPedido.class, valor, SituacaoPedido::getValor);
	}
}
