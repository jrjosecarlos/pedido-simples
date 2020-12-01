package br.org.casa.pedidosimples.model;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.querydsl.core.types.dsl.BooleanExpression;

import br.org.casa.pedidosimples.exception.ParametroBuscaParseException;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;
import br.org.casa.pedidosimples.util.EnumUtil;

/**
 * Entidade que representa os pedidos a serem gerados no sistema.
 *
 * @author jrjosecarlos
 *
 */
@Entity
@Table(schema = "pedido_simples", name = "pedido")
@AttributeOverride(name = "id",
	column = @Column(name="id_pedido")
)
public class Pedido extends BaseEntity {

	/**
	 * Nome de exibição para esta entidade. Usado principalmente no retorno
	 * de mensagens de erro
	 */
	public static final String NOME_EXIBICAO_ENTIDADE = "Pedido";

	@Column(name = "codigo", unique = true, nullable = false)
	@NotNull
	@Size(min = 8, max = 8)
	private String codigo;

	@Column(name = "fator_desconto", nullable = false)
	@NotNull
	@DecimalMin(value = "0.00", inclusive = true)
	@DecimalMax(value = "1.00", inclusive = true)
	@Digits(integer = 1, fraction = 2)
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

	public enum ParametroBuscaPedido {
		CODIGO("codigo") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				return QPedido.pedido.codigo.containsIgnoreCase(valor);
			}
		},

		SITUACAO("situacao") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				try {
					return QPedido.pedido.situacao.eq(SituacaoPedido.fromValor(valor));
				} catch (IllegalArgumentException e) {
					throw new ParametroBuscaParseException(e, NOME_EXIBICAO_ENTIDADE, this.getNomeParametro(), valor,
							EnumSet.allOf(SituacaoPedido.class)
								.stream()
								.map(SituacaoPedido::getValor)
								.collect(Collectors.joining(" | "))
							);
				}
			}
		};

		private String nomeParametro;

		private ParametroBuscaPedido(String nomeParametro) {
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
		public static boolean isParametroBuscaPedido(String nomeParametro) {
			return EnumUtil.isEnumFromValue(ParametroBuscaPedido.class, nomeParametro, ParametroBuscaPedido::getNomeParametro);
		}

		/**
		 * Converte um valor qualquer no elemento de {@link ParametroBuscaPedido} que o possui
		 * como nomeParametro.
		 *
		 * @param valor o valor a ser convertido
		 * @return o elemento correspondente do enum
		 * @throws IllegalArgumentException se não há nenhum elemento do enum com nomeParametro igual
		 * ao valor informado
		 * @throws NullPointerException se valor for {@code null}.
		 */
		public static ParametroBuscaPedido fromValor(String valor) {
			return EnumUtil.enumFromValue(ParametroBuscaPedido.class, valor, ParametroBuscaPedido::getNomeParametro);
		}
	}

}
