/**
 *
 */
package br.org.casa.pedidosimples.service.impl;

import javax.persistence.EntityManager;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Interface de suporte a testes, para facilitar a geração de {@link Answer}s que
 * fazem mocks retornar o próprio parâmetro.
 *

 * @author jrjosecarlos
 *
 */
public interface ParameterAsAnswer {
	/**
	 * Cria um novo {@link Answer} que indica à mock que deve retornar o primeiro parâmetro
	 * da sua invocação como retorno. Úteis em métodos que retornam o próprio objeto, como
	 * os derivados de {@link EntityManager#merge(Object)}
	 *
	 * @param <T> o tipo de dados de retorno do Answer.
	 * @return um novo Answer configurado para retornar o primeiro parâmetro da invocação
	 */
	default <T> Answer<T> getParameterAsAnswer() {
		return new Answer<T>() {
    		@SuppressWarnings("unchecked")
			public T answer(InvocationOnMock invocation) throws Throwable {
		         return (T) invocation.getArguments()[0];
		     }
    	};
	}
}
