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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.dsl.BooleanExpression;

import br.org.casa.pedidosimples.exception.ParametroBuscaParseException;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;
import br.org.casa.pedidosimples.util.EnumUtil;
import br.org.casa.pedidosimples.util.BigDecimalUtil;

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

	/**
	 * Nome de exibicao para esta entidade. Usado principalmente no retorno
	 * de mensagens de erro.
	 */
	public static final String NOME_EXIBICAO_ENTIDADE = "Item de Pedido";

	@Column(name = "valor")
	@NotNull
	@DecimalMin(value = "0.00", inclusive = true)
	private BigDecimal valor;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pedido_id", nullable = false)
	@NotNull
	@JsonIgnore
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
	void setValor(BigDecimal valor) {
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

	/**
	 * Atualizar o valor deste ItemPedido. O valor atualizado corresponde ao valor base
	 * do {@link ItemVenda} associado, aplicando o percentual de desconto do {@link Pedido},
	 * modificado pelo {@link TipoItemVenda tipo do ItemVenda}, ao qual este item pertence.
	 *
	 * <p>Por exemplo, se o ItemVenda é de um tipo com fator de desconto 1.00, possui valor base
	 * igual a 200.00 e o Pedido tem um desconto de 30%, o valor do ItemPedido será:</p>
	 *
	 * <code>200.00 - (200.00 * (0.30 * 1.00)) = 140.00</code>
	 *
	 * <p>Já se o ItemVenda é de um tipo com fator de desconto 0.00, possuir valor base igual a
	 * 150.00 e o pedido tem um desconto de 50%, o valor do ItemPedido será:</p>
	 *
	 * <code> 150.00 - (150 * (0.50 * 0.00)) = 150.00</code>
	 *
	 * @return o valor atualizado deste ItemPedido.
	 */
	public BigDecimal calcularValor() {
		BigDecimal fatorDescontoEfetivo = this.getPedido().getFatorDesconto()
				.multiply(this.getItemVenda().getTipo().getFatorAplicacaoDesconto());
		BigDecimal valorAtualizado = this.getItemVenda().getValorBase()
				.subtract(this.getItemVenda().getValorBase().multiply(fatorDescontoEfetivo));

		this.setValor(BigDecimalUtil.setEscalaPadrao(valorAtualizado));
		return this.valor;
	}

	public enum ParametroBuscaItemPedido {
		/**
		 * Busca {@link ItemPedido}s com valor mínimo informado.
		 */
		VALOR_MINIMO("valorMinimo") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				try {
					return QItemPedido.itemPedido.valor.goe(new BigDecimal(valor));
				} catch (NumberFormatException e) {
					throw new ParametroBuscaParseException(e, NOME_EXIBICAO_ENTIDADE, this.getNomeParametro(), valor, "0.00");
				}
			}
		},

		/**
		 * Busca {@link ItemPedido}s com valor máximo informado.
		 */
		VALOR_MAXIMO("valorMaximo") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				try {
					return QItemPedido.itemPedido.valor.loe(new BigDecimal(valor));
				} catch (NumberFormatException e) {
					throw new ParametroBuscaParseException(e, NOME_EXIBICAO_ENTIDADE, this.getNomeParametro(), valor, "0.00");
				}
			}
		},

		/**
		 * Busca {@link ItemPedido}s cujo {@link ItemVenda}s associados possuam nome contendo
		 * o valor informado.
		 */
		ITEM_VENDA("itemVenda") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				return QItemPedido.itemPedido.itemVenda.nome.containsIgnoreCase(valor);
			}
		};

		private String nomeParametro;

		private ParametroBuscaItemPedido(String nomeParametro) {
			this.nomeParametro = nomeParametro;
		}

		public abstract BooleanExpression getPredicate(String valor);

		/**
		 * Retorna o valor atual do campo nomeParametro.
		 *
		 * @return valor de nomeParametro
		 */
		public String getNomeParametro() {
			return nomeParametro;
		}

		/**
		 * Verifica se o nomeParametro informado corresponde a algum dos elementos
		 * deste enum.
		 *
		 * @param nomeParametro o nome do parâmetro a se buscar
		 * @return {@code true}, se corresponder a algum elemento do enum, {@code false}
		 * caso contrário
		 */
		public static boolean isParametroBuscaItemPedido(String nomeParametro) {
			return EnumUtil.isEnumFromValue(ParametroBuscaItemPedido.class, nomeParametro, ParametroBuscaItemPedido::getNomeParametro);
		}

		/**
		 * Converte um valor qualquer no elemento de {@link ParametroBuscaItemPedido} que o possui
		 * como nomeParametro.
		 *
		 * @param valor o valor a ser convertido
		 * @return o elemento correspondente do enum
		 * @throws IllegalArgumentException se não há nenhum elemento do enum com nomeParametro igual
		 * ao valor informado
		 * @throws NullPointerException se valor for {@code null}.
		 */
		public static ParametroBuscaItemPedido fromValor(String valor) {
			return EnumUtil.enumFromValue(ParametroBuscaItemPedido.class, valor, ParametroBuscaItemPedido::getNomeParametro);
		}
	}
}
