/**
 *
 */
package br.org.casa.pedidosimples.controller;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.casa.pedidosimples.model.Pedido;

/**
 * Um DTO simples para a aplicação de fatores de desconto em {@link Pedido}.
 *
 * @author jrjosecarlos
 *
 */
public class FatorDescontoDTO {

	@NotNull
	@DecimalMin(value = "0.00", inclusive = true)
	@DecimalMax(value = "1.00", inclusive = true)
	@Digits(integer = 1, fraction = 2)
	@JsonProperty("fatorDesconto")
	private BigDecimal valor;

	/**
	 * Retorna o valor atual do campo valor.
	 *
	 * @return valor de valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * Define um novo valor para o campo valor
	 *
	 * @param valor o novo valor de valor
	 */
	public void setFatorDesconto(BigDecimal valor) {
		this.valor = valor;
	}

}
