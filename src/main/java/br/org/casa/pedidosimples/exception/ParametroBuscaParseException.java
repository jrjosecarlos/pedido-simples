/**
 *
 */
package br.org.casa.pedidosimples.exception;

/**
 * Exceção para informar erros de parse no processamento de parâmetros para a busca
 * de entidades.
 *
 * @author jrjosecarlos
 *
 */
public class ParametroBuscaParseException extends RuntimeException {

	/**
	 * Serial ID da classe.
	 */
	private static final long serialVersionUID = 3328511328948207234L;

	private String nomeEntidade;

	private String nomeParametro;

	private String valorRecebido;

	private String padraoEsperado;

	/**
	 * Constrói uma nova instância da exceção, informando os valores iniciais para nomeEntidade, valorRecebido
	 * e padraoEsperado. Esses valores serão exibidos na mensagem de erro correspondente.
	 *
	 * @param cause o {@link Throwable} original relacionado ao erro de parse. Pode ser {@code null}
	 * @param nomeEntidade valor inicial do nome da entidade
	 * @param nomeParametro valor inicial de nome do parâmetro, que corresponde ao parâmetro no qual
	 * o erro ocorreu
	 * @param valorRecebido valor inicial do valorRecebido. Este valor é o que provocou o erro de parse
	 * @param padraoEsperado valor inicial do padrão esperado. É o formato esperado para que não ocorra
	 * erros de parse. Pode ser {@code null}.
	 */
	public ParametroBuscaParseException(Throwable cause, String nomeEntidade, String nomeParametro, String valorRecebido,
			String padraoEsperado) {
		super(cause);

		this.nomeEntidade = nomeEntidade;
		this.nomeParametro = nomeParametro;
		this.valorRecebido = valorRecebido;
		this.padraoEsperado = padraoEsperado;
	}

	@Override
	public String getMessage() {
		return String.format("Erro na conversão do parâmetro [ %s ] na busca de dados da entidade %s.", nomeParametro, nomeEntidade);
	}

	public String getDetailedMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Valor recebido: [ %s ].", valorRecebido));

		if(padraoEsperado != null) {
			sb.append(String.format(" O valor deve seguir o padrão de formato [ %s ].", padraoEsperado));
		}

		return sb.toString();
	}

	/**
	 * Retorna o valor atual do campo nomeEntidade.
	 *
	 * @return valor de nomeEntidade
	 */
	public String getNomeEntidade() {
		return nomeEntidade;
	}

	/**
	 * Retorna o valor atual do campo nomeParametro.
	 *
	 * @return valor de nomeParametro
	 */
	public String getNomeParametro() {
		return nomeParametro;
	}

	/**
	 * Retorna o valor atual do campo valorRecebido.
	 *
	 * @return valor de valorRecebido
	 */
	public String getValorRecebido() {
		return valorRecebido;
	}

	/**
	 * Retorna o valor atual do campo padraoEsperado.
	 *
	 * @return valor de padraoEsperado
	 */
	public String getPadraoEsperado() {
		return padraoEsperado;
	}

}
