/**
 *
 */
package br.org.casa.pedidosimples.model;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Entidade que representa os {@link ItemVenda} associados a um {@link Pedido}
 * em particular.
 *
 * @author jrjosecarlos
 *
 */
@Entity
@Table(schema = "pedido_simples", name = "item_pedido")
@AttributeOverride(name = "id",
	column = @Column(name = "id_item_pedido")
)
public class ItemPedido extends BaseEntity {
	@Column(name = "valor")
	@NotNull
	@DecimalMin(value = "0.00", inclusive = true)
	private BigDecimal valor;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pedido_id", nullable = false)
	@NotNull
	private Pedido pedido;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "item_venda_id", nullable = false)
	@NotNull
	private ItemVenda itemVenda;

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
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * Retorna o valor atual do campo pedido.
	 *
	 * @return valor de pedido
	 */
	public Pedido getPedido() {
		return pedido;
	}

	/**
	 * Define um novo valor para o campo pedido
	 *
	 * @param pedido o novo valor de pedido
	 */
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	/**
	 * Retorna o valor atual do campo itemVenda.
	 *
	 * @return valor de itemVenda
	 */
	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	/**
	 * Define um novo valor para o campo itemVenda
	 *
	 * @param itemVenda o novo valor de itemVenda
	 */
	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}

}
