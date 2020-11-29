/**
 *
 */
package br.org.casa.pedidosimples.model;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Representa um item que pode ser vendido, ou mais especificamente que pode ser incluso em
 * {@link Pedido}s.
 *
 * @author jrjosecarlos
 *
 */
@Entity
@Table(schema = "pedido_simples", name = "item_venda")
@AttributeOverride(name = "id",
	column = @Column(name = "id_item_venda")
)
public class ItemVenda extends BaseEntity {

	@Column(name = "nome")
	@NotNull
	@Size(min = 1, max = 100)
	private String nome;

	@Column(name = "tipo", nullable = false)
	@NotNull
	private TipoItemVenda tipo;

	@Column(name = "valor_base", nullable = false)
	@NotNull
	@DecimalMin(value = "0.00", inclusive = true)
	@Digits(integer = 13, fraction = 2)
	private BigDecimal valorBase = new BigDecimal("0.00");

	@Column(name = "ativo", nullable = false)
	@NotNull
	private Boolean ativo = true;

	/**
	 * Retorna o valor atual do campo nome.
	 *
	 * @return valor de nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Define um novo valor para o campo nome
	 *
	 * @param nome o novo valor de nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Retorna o valor atual do campo tipo.
	 *
	 * @return valor de tipo
	 */
	public TipoItemVenda getTipo() {
		return tipo;
	}

	/**
	 * Define um novo valor para o campo tipo
	 *
	 * @param tipo o novo valor de tipo
	 */
	public void setTipo(TipoItemVenda tipo) {
		this.tipo = tipo;
	}

	/**
	 * Retorna o valor atual do campo valorBase.
	 *
	 * @return valor de valorBase
	 */
	public BigDecimal getValorBase() {
		return valorBase;
	}

	/**
	 * Define um novo valor para o campo valorBase
	 *
	 * @param valorBase o novo valor de valorBase
	 */
	public void setValorBase(BigDecimal valorBase) {
		this.valorBase = valorBase;
	}

	/**
	 * Retorna o valor atual do campo ativo.
	 *
	 * @return valor de ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * Define um novo valor para o campo ativo
	 *
	 * @param ativo o novo valor de ativo
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
