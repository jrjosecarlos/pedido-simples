/**
 *
 */
package br.org.casa.pedidosimples.exception;

/**
 * Exceção para representar os casos de erros na sintaxe da requisição identificados pelo sistema,
 * como ids inconsistentes nas requisições com PathParams e payloads.
 *
 * @author jrjosecarlos
 *
 */
public class RequisicaoInvalidaException extends RuntimeException {

	/**
	 * Serial ID da classe
	 */
	private static final long serialVersionUID = -2344297462279251714L;

	/**
	 * Constrói uma nova exceção com uma mensagem.
	 *
	 * @param mensagem formato de mensagem descrevendo o motivo de a requisição ser inválida
	 * @param args variáveis a serem inseridas no formato de mensagem. Seguem o mesmo padrão
	 * de {@link String#format(String, Object...)}.
	 */
	public RequisicaoInvalidaException(String mensagem, Object... args) {
		super(String.format(mensagem, args));
	}
}
