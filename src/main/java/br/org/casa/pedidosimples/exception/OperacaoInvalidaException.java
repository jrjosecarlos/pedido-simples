/**
 *
 */
package br.org.casa.pedidosimples.exception;

import java.io.Serializable;

/**
 * Exceção que representa operações inválidas no domínio da aplicação.
 *
 * @author jrjosecarlos
 *
 */
public class OperacaoInvalidaException extends RuntimeException {

	/**
	 * Serial da classe, conforme {@link Serializable}
	 */
	private static final long serialVersionUID = 6273594237838382959L;

	/**
	 * Constrói uma nova exceção, com uma mensagem que descreva o motivo de a operação
	 * ser inválida.
	 *
	 * @param mensagem motivo da operação ser inválida
	 */
	public OperacaoInvalidaException(String mensagem) {
		super(mensagem);
	}

}
