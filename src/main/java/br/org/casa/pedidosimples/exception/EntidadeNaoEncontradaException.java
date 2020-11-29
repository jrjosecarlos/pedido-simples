/**
 *
 */
package br.org.casa.pedidosimples.exception;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Exceção para indicar que a busca de uma determinada entidade não encontrou resultados.
 *
 * @author jrjosecarlos
 *
 */
public class EntidadeNaoEncontradaException extends RuntimeException {

	/**
	 * Serial version da classe, conforme implementação de {@link Serializable}.
	 */
	private static final long serialVersionUID = 5029049363922897108L;

	private String nomeEntidade;

	private UUID uuid;

	/**
	 * Cria uma nova exceção, com valores iniciais para nomeEntidade e uuid.
	 *
	 * @param nomeEntidade valor inicial para nomeEntidade. Não pode ser {@code null}.
	 * @param uuid valor inicial do uuid. Não pode ser {@code null}.
	 */
	public EntidadeNaoEncontradaException(String nomeEntidade, UUID uuid) {
		Objects.requireNonNull(nomeEntidade);
		Objects.requireNonNull(uuid);

		this.nomeEntidade = nomeEntidade;
		this.uuid = uuid;
	}

	@Override
	public String getMessage() {
		return String.format("Entidade %s com uuid %s não encontrada", nomeEntidade, uuid.toString());
	};

}
