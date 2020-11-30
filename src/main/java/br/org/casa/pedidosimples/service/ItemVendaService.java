/**
 *
 */
package br.org.casa.pedidosimples.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
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
	Page<ItemVenda> buscarTodos(Pageable pageable, Map<String, String> parametrosBusca);

	/**
	 * Busca um {@link ItemVenda} pelo seu uuid.
	 *
	 * @param uuid id do ItemVenda a ser buscado.
	 * @return um Optional contendo o ItemVenda com o id informado, se existir, ou um Optional
	 * vazio, caso não exista.
	 */
	Optional<ItemVenda> buscarPorId(UUID uuid);

	/**
	 * Inclui um novo ItemVenda.
	 *
	 * @param itemVenda ItemVenda a ser incluído.
	 * @return a versão persistida (com uuid preenchido) deste itemVenda
	 */
	ItemVenda incluir(ItemVenda itemVenda);

	/**
	 * Altera um ItemVenda existente.
	 *
	 * @param uuid o id do ItemVenda a ser alterado
	 * @param itemVenda os novos dados para o itemVenda
	 * @return a versão persistida deste itemVenda
	 * @throws EntidadeNaoEncontradaException se não existir ItemVenda com o uuid informado
	 */
	ItemVenda alterar(UUID uuid, ItemVenda itemVenda);

	/**
	 * Exclui um ItemVenda existente.
	 *
	 * @param uuid id do ItemVenda a ser excluído
	 * @throws EntidadeNaoEncontradaException se não existir ItemVenda com o uuid informado
	 */
	void excluir(UUID uuid);
}
