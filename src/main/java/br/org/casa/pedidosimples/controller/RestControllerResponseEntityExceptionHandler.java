/**
 *
 */
package br.org.casa.pedidosimples.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.exception.OperacaoInvalidaException;
import br.org.casa.pedidosimples.exception.ParametroBuscaParseException;
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

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		FieldError fieldError = ex.getFieldError();

		ErroHttpSimples erro = new ErroHttpSimples(status, "Erro de validação",
				String.format("Campo [ %s ] (valor recebido [ %s ]). Mensagem de erro: %s",
						fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));

		return super.handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(value = {EntidadeNaoEncontradaException.class} )
	protected ResponseEntity<Object> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex,
			WebRequest request) {
		ErroHttpSimples erro = new ErroHttpSimples(HttpStatus.NOT_FOUND, ex.getMessage());

		return super.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = {ParametroBuscaParseException.class})
	protected ResponseEntity<Object> handleParametroBuscaParse(ParametroBuscaParseException ex,
			WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroHttpSimples erro = new ErroHttpSimples(status, ex.getMessage(), ex.getDetailedMessage());

		return super.handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(value = {OperacaoInvalidaException.class})
	protected ResponseEntity<Object> handleOperacaoInvalida(OperacaoInvalidaException ex,
			WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroHttpSimples erro = new ErroHttpSimples(status, "Operação inválida", ex.getMessage());

		return super.handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);

	}
}
