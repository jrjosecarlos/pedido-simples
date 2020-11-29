package br.org.casa.pedidosimples.model;

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

	private String sigla;

	private SituacaoPedido(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * Retorna o valor atual do campo sigla.
	 *
	 * @return valor de sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Converte uma sigla informada no {@link SituacaoPedido} correspondente.
	 *
	 * @param sigla a sigla a ser convertido no elemento do enum correspondente
	 * @return o SituacaoPedido correspondente à sigla informado
	 * @throws NullPointerException se {@code sigla} for {@code null}
	 * @throws IllegalArgumentException se não houver nenhum SituacaoPedido com
	 * a sigla informada
	 */
	public static SituacaoPedido fromSigla(String sigla) {
		return EnumUtil.enumFromValue(SituacaoPedido.class, sigla, SituacaoPedido::getSigla);
	}
}
