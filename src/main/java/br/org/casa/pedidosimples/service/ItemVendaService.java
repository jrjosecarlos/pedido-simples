/**
 *
 */
package br.org.casa.pedidosimples.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.casa.pedidosimples.model.ItemVenda;

/**
 * Contrato para os serviços relacionados a {@link ItemVenda}.
 *
 * @author jrjosecarlos
 *
 */
public interface ItemVendaService {
	/**
	 * Busca todos os {@link ItemVenda} existentes, podendo incluir opções de paginação e parâmetros de busca
	 * para retornar registros específicos.
	 *
	 * @param pageable interface de definição da paginação da busca
	 * @param parametrosBusca parâmetros para filtragem dos resultados encontrados
	 * @return um {@link Page} contendo os registros da página atual, de acordo com os parâmetros de busca
	 */
	Page<ItemVenda> buscarTodosItensVenda(Pageable pageable, Map<String, String> parametrosBusca);
}
