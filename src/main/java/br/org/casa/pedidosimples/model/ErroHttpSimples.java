/**
 *
 */
package br.org.casa.pedidosimples.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Classe para representação de erros HTTP simples.
 *
 * @author jrjosecarlos
 *
 */
@JsonPropertyOrder({
	"codigoStatus",
	"mensagemStatus",
	"timestamp",
	"mensagem",
	"descricaoDetalhada"
})
public class ErroHttpSimples {
	private HttpStatus status;

	private String mensagem;

	private String descricaoDetalhada;

	private LocalDateTime timestamp = LocalDateTime.now();

	/**
	 * Constrói um novo ErroHttpSimples definindo valores para status, mensagem e descricaoDetalhada.
	 *
	 * @param status valor inicial de status
	 * @param mensagem valor inicial de mensagem
	 * @param descricaoDetalhada valor inicial de descricaoDetalhada
	 */
	public ErroHttpSimples(HttpStatus status, String mensagem, String descricaoDetalhada) {
		this.status = status;
		this.mensagem = mensagem;
		this.descricaoDetalhada = descricaoDetalhada;
	}

	/**
	 * Retorna o valor atual do campo status.
	 *
	 * @return valor de status
	 */
	@JsonIgnore
	public HttpStatus getStatus() {
		return status;
	}

	/**
	 * Retorna o valor numérico do código HTTP correspondente ao campo {@code status}.
	 *
	 * @return um Integer que corresponde ao código HTTP do status atual. Caso não haja
	 * status, retorna {@code null}.
	 */
	public Integer getCodigoStatus() {
		return this.status != null ? this.status.value() : null;
	}

	/**
	 * Retorna a descrição do código HTTP correspondente ao campo {@code status}.
	 *
	 * @return a descrição textual do código HTTP do status atual. Caso não haja
	 * status, retorna {@code null}.
	 */
	public String getMensagemStatus() {
		return this.status != null ? this.status.getReasonPhrase() : null;
	}

	/**
	 * Retorna o valor atual do campo mensagem.
	 *
	 * @return valor de mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Retorna o valor atual do campo descricaoDetalhada.
	 *
	 * @return valor de descricaoDetalhada
	 */
	public String getDescricaoDetalhada() {
		return descricaoDetalhada;
	}

	/**
	 * Retorna o valor atual do campo timestamp.
	 *
	 * @return valor de timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}


}
