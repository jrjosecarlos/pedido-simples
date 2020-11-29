package br.org.casa.pedidosimples.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

/**
 * Entidade que representa os pedidos a serem gerados no sistema.
 *
 * @author jrjosecarlos
 *
 */
@Entity
@Table(name="pedido")
public class Pedido {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name="id_pedido", updatable = false, nullable = false)
	@NotNull
	private UUID idPedido;

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
	 * Retorna o valor atual do campo idPedido.
	 *
	 * @return valor de idPedido
	 */
	public UUID getIdPedido() {
		return idPedido;
	}

	/**
	 * Define um novo valor para o campo idPedido
	 *
	 * @param idPedido o novo valor de idPedido
	 */
	public void setIdPedido(UUID idPedido) {
		this.idPedido = idPedido;
	}

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
