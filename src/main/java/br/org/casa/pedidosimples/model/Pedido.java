package br.org.casa.pedidosimples.model;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entidade que representa os pedidos a serem gerados no sistema.
 *
 * @author jrjosecarlos
 *
 */
@Entity
@Table(name="pedido")
@AttributeOverride(name = "id",
	column = @Column(name="id_pedido")
)
public class Pedido extends BaseEntity {

	@Column(name = "codigo", unique = true, nullable = false)
	@NotNull
	@Size(min = 8, max = 8)
	private String codigo;

	@Column(name = "fator_desconto", nullable = false)
	@NotNull
	@DecimalMin(value = "0.00", inclusive = true)
	@DecimalMax(value = "1.00", inclusive = true)
	@Digits(integer = 0, fraction = 2)
	private BigDecimal fatorDesconto;

	@Column(name = "situacao", nullable = false)
	@NotNull
	private SituacaoPedido situacao;

	/**
	 * Retorna o valor atual do campo codigo.
	 *
	 * @return valor de codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Define um novo valor para o campo codigo
	 *
	 * @param codigo o novo valor de codigo
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	 * Define um novo valor para o campo fatorDesconto
	 *
	 * @param fatorDesconto o novo valor de fatorDesconto
	 */
	public void setFatorDesconto(BigDecimal fatorDesconto) {
		this.fatorDesconto = fatorDesconto;
	}

	/**
	 * Retorna o valor atual do campo situacao.
	 *
	 * @return valor de situacao
	 */
	public SituacaoPedido getSituacao() {
		return situacao;
	}

	/**
	 * Define um novo valor para o campo situacao
	 *
	 * @param situacao o novo valor de situacao
	 */
	public void setSituacao(SituacaoPedido situacao) {
		this.situacao = situacao;
	}

}
