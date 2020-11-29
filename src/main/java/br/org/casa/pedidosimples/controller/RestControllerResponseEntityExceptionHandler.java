/**
 *
 */
package br.org.casa.pedidosimples.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.org.casa.pedidosimples.model.ErroHttpSimples;

/**
 * Implementação de {@link ControllerAdvice} para tratamento específico de algumas exceções lançadas
 * pelo sistema.
 *
 * @author jrjosecarlos
 *
 */
@ControllerAdvice
public class RestControllerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErroHttpSimples erro = new ErroHttpSimples(status, "Erro na leitura da requisição", ex.getMessage());
		return super.handleExceptionInternal(ex, erro, headers, status, request);
	}

}
