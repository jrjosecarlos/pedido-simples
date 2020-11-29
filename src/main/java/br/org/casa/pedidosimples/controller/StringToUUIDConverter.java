/**
 *
 */
package br.org.casa.pedidosimples.controller;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link Converter} com a principal finalidade de tratar
 * os possíveis {@link UUID}s recebidos como parâmetros em URLs de Serviço.
 *
 * @author jrjosecarlos
 *
 */
@Component
public class StringToUUIDConverter implements Converter<String, UUID>{

	@Override
	public UUID convert(String source) {
		return UUID.fromString(source);
	}

}
