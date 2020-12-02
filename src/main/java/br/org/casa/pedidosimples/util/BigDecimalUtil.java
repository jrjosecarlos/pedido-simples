/**
 *
 */
package br.org.casa.pedidosimples.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe com facilidades e constantes relacionadas a operações com {@link BigDecimal},
 * para garantir uniformidade no tratamento de valores em todo o sistema.
 *
 * @author jrjosecarlos
 *
 */
public class BigDecimalUtil {
	/**
	 * Escala padrão para os operações. Corresponde ao número de casas decimais.
	 */
	public static final int DEFAULT_SCALE = 2;

	/**
	 * Método padrão para arredondamento. É utilizado o {@link RoundingMode#HALF_EVEN}, correspondente
	 * à "Regra do Banqueiro", por minimizar erros quando o número de operações é grande
	 */
	public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

	private BigDecimalUtil() {
		// Construtor padrão privado, por se tratar de uma classe de constantes
	}

	/**
	 * Atualiza um BigDecimal para a escala padrão, usando o método de arredondamento padrão.
	 *
	 * @param valor o BigDecimal a ser ajustado
	 * @return um novo BigDecimal, com escala redefinida para o padrão.
	 */
	public static BigDecimal setEscalaPadrao(BigDecimal valor) {
		return valor.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
	}
}
