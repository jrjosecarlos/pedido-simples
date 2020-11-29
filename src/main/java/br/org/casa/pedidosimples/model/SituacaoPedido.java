package br.org.casa.pedidosimples.model;

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
}
