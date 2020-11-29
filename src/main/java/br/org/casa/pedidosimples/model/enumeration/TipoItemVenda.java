/**
 *
 */
package br.org.casa.pedidosimples.model.enumeration;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonValue;

import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.util.EnumUtil;

/**
 * Define os tipos de {@link ItemVenda} existentes. A principal diferença entre eles
 * se refere à aplicação do desconto associado ao {@link Pedido}.
 *
 * @author jrjosecarlos
 *
 */
public enum TipoItemVenda {

	/**
	 * Representa um produto, item de venda cujo fator de desconto é 1, ou seja,
	 * o desconto do {@link Pedido} é aplicado integralmente no valor do item.
	 */
	PRODUTO("P", new BigDecimal("1.00")),

	/**
	 * Representa um serviço, item de venda cujo fator de desconto é zero, ou seja,
	 * o desconto do {@link Pedido} não é aplicado no valor do item
	 */
	SERVICO("S", new BigDecimal("0.00"));

	private String valor;

	private BigDecimal fatorDesconto;

	private TipoItemVenda(String valor, BigDecimal fatorDesconto) {
		this.valor = valor;
		this.fatorDesconto = fatorDesconto;
	}

	/**
	 * Retorna o valor atual do campo valor.
	 *
	 * @return valor de valor
	 */
	@JsonValue
	public String getValor() {
		return valor;
	}

	/**
	 * Retorna o valor atual do campo fatorDesconto.
	 *
	 * @return valor de fatorDesconto
	 */
	public BigDecimal getFatorDesconto() {
		return fatorDesconto;
	}

	/**
	 * Converte um valor informado no {@link TipoItemVenda} que possui este valor
	 * como correspondente.
	 *
	 * @param valor o valor a ser convertido no elemento do enum correspondente
	 * @return o TipoItemVenda correspondente ao valor informado
	 * @throws NullPointerException se {@code value} for {@code null}
	 * @throws IllegalArgumentException se não houver nenhum SituacaoPedido com
	 * o valor informado
	 */
	public static TipoItemVenda fromValor(String valor) {
		return EnumUtil.enumFromValue(TipoItemVenda.class, valor, TipoItemVenda::getValor);
	}
}
