/**
 *
 */
package br.org.casa.pedidosimples.model;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.Expressions;

import br.org.casa.pedidosimples.exception.ParametroBuscaParseException;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;
import br.org.casa.pedidosimples.util.EnumUtil;

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

	/**
	 * Nome de exibição para esta entidade. Usado principalmente no retorno
	 * de mensagens de erro
	 */
	public static final String NOME_EXIBICAO_ENTIDADE = "Item de Venda";

	@Column(name = "nome", nullable = false)
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
	public Boolean isAtivo() {
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

	/**
	 * Enumeração que contém os possíveis parâmetros de busca para {@link ItemVenda}.
	 *
	 * @author jrjosecarlos
	 *
	 */
	public enum ParametroBuscaItemVenda {
		/**
		 * Busca por qualquer pedaço do nome, case insensitivo. O valor informado é do tipo {@link String}.
		 */
		NOME("nome") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				return QItemVenda.itemVenda.nome.containsIgnoreCase(valor);
			}
		},

		/**
		 * Busca por ItensVenda com valor maior ou igual ao informado. O valor esperado é do tipo
		 * {@link BigDecimal}.
		 */
		VALOR_MINIMO("valorMinimo") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				try {
					return QItemVenda.itemVenda.valorBase.goe(new BigDecimal(valor));
				} catch (NumberFormatException e) {
					throw new ParametroBuscaParseException(e, NOME_EXIBICAO_ENTIDADE, this.getNomeParametro(), valor, "0.00");
				}
			}
		},

		/**
		 * Busca por ItensVenda com valor menor ou igual ao informado. O valor informado é do tipo
		 * {@link BigDecimal}.
		 */
		VALOR_MAXIMO("valorMaximo") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				try {
					return QItemVenda.itemVenda.valorBase.loe(new BigDecimal(valor));
				} catch (NumberFormatException e) {
					throw new ParametroBuscaParseException(e, NOME_EXIBICAO_ENTIDADE, this.getNomeParametro(), valor, "0.00");
				}
			}
		},

		/**
		 * Busca por ItensVenda do tipo informado. O valor informado corresponde aos valores de
		 * {@link TipoItemVenda}.
		 */
		TIPO("tipo") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				try {
					return QItemVenda.itemVenda.tipo.eq(TipoItemVenda.fromValor(valor));
				} catch (IllegalArgumentException e) {
					throw new ParametroBuscaParseException(e, NOME_EXIBICAO_ENTIDADE, this.getNomeParametro(), valor,
							EnumSet.allOf(TipoItemVenda.class)
								.stream()
								.map(TipoItemVenda::getValor)
								.collect(Collectors.joining(" | "))
							);
				}
			}
		},

		/**
		 * Busca por ItensVenda ativos ou inativos. O valor informado corresponde às letras S ou N
		 * (case insensitivo) para ativo ou inativo, respectivamente.
		 */
		ATIVO("ativo") {
			@Override
			public BooleanExpression getPredicate(String valor) {
				BooleanPath ativo = QItemVenda.itemVenda.ativo;

				if("s".equalsIgnoreCase(valor)) {
					return ativo.eq(Expressions.asBoolean(true));
				} else if("n".equalsIgnoreCase(valor)) {
					return ativo.eq(Expressions.asBoolean(false));
				} else {
					throw new ParametroBuscaParseException(null, NOME_EXIBICAO_ENTIDADE, this.getNomeParametro(), valor,
							"S | N");
				}
			}
		};

		private String nomeParametro;

		private ParametroBuscaItemVenda(String nomeParametro) {
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
		public static boolean isParametroBuscaItemVenda(String nomeParametro) {
			return EnumUtil.isEnumFromValue(ParametroBuscaItemVenda.class, nomeParametro, ParametroBuscaItemVenda::getNomeParametro);
		}

		/**
		 * Converte um valor qualquer no elemento de {@link ParametroBuscaItemVenda} que o possui
		 * como nomeParametro.
		 *
		 * @param valor o valor a ser convertido
		 * @return o elemento correspondente do enum
		 * @throws IllegalArgumentException se não há nenhum elemento do enum com nomeParametro igual
		 * ao valor informado
		 * @throws NullPointerException se valor for {@code null}.
		 */
		public static ParametroBuscaItemVenda fromValor(String valor) {
			return EnumUtil.enumFromValue(ParametroBuscaItemVenda.class, valor, ParametroBuscaItemVenda::getNomeParametro);
		}
	}

}
