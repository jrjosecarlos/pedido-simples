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
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;

/**
 * Contrato para os serviços relacionados a {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
public interface ItemPedidoService {
	/**
	 * Busca todos os {@link ItemPedido} existentes para um certo {@link Pedido} com uuid informado,
	 * podendo incluir opções de paginação e parâmetros de busca para retornar registros específicos.
	 *
	 * @param uuidPedido uuid do Pedido que se deseja buscar os itens
	 * @param pageable interface de definição da paginação da busca
	 * @param parametrosBusca parâmetros para filtragem dos resultados encontrados
	 * @return um {@link Page} contendo os registros da página atual, de acordo com os parâmetros de busca
	 * @throws EntidadeNaoEncontradaException se {@code uuidPedido} não corresponder a nenhum Pedido existente
	 */
	Page<ItemPedido> buscarTodos(UUID uuidPedido, Pageable pageable, Map<String, String> parametrosBusca);

	/**
	 * Busca um {@link ItemPedido} pelo seu uuid.
	 *
	 * @param uuid id do ItemPedido a ser buscado.
	 * @return um Optional contendo o ItemPedido com o id informado, se existir, ou um Optional
	 * vazio, caso não exista.
	 */
	Optional<ItemPedido> buscarPorId(UUID uuid);

	/**
	 * Inclui um novo ItemPedido para o {@link Pedido} com uuid informado, relacionado ao
	 * {@link ItemVenda}.
	 *
	 * @param uuidPedido o Pedido que terá um itemPedido incluído
	 * @param itemVenda o ItemVenda a ser incluído no pedido
	 * @return a versão persistida (com uuid preenchido) deste itemPedido
	 * @throws EntidadeNaoEncontradaException se {@code uuidPedido} ou {@code itemVenda} não corresponderem
	 * a nenhuma entidade existente
	 */
	ItemPedido incluir(UUID uuidPedido, ItemVenda itemVenda);

	/**
	 * Exclui um ItemPedido existente.
	 *
	 * @param uuid id do ItemPedido a ser excluído
	 * @throws EntidadeNaoEncontradaException se não existir ItemPedido com o uuid informado
	 */
	void excluir(UUID uuid);
}