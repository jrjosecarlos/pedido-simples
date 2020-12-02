/**
 *
 */
package br.org.casa.pedidosimples.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.Pedido;

/**
 * Contrato para serviços relacionados a {@link Pedido}.
 *
 * @author jrjosecarlos
 *
 */
public interface PedidoService {

	/**
	 * Busca todos os {@link Pedido}s existentes, podendo incluir opções de paginação e parâmetros de busca
	 * para retornar registros específicos.
	 *
	 * @param pageable interface de definição da paginação da busca
	 * @param parametrosBusca parâmetros para filtragem dos resultados encontrados
	 * @return um {@link Page} contendo os registros da página atual, de acordo com os parâmetros de busca
	 */
	Page<Pedido> buscarTodos(Pageable pageable, Map<String, String> parametrosBusca);

	/**
	 * Busca um {@link Pedido} pelo seu uuid.
	 *
	 * @param uuid id do Pedido a ser buscado.
	 * @return um Optional contendo o Pedido com o id informado, se existir, ou um Optional
	 * vazio, caso não exista.
	 */
	Optional<Pedido> buscarPorId(UUID uuid);

	/**
	 * Inclui um novo Pedido.
	 *
	 * @param pedido Pedido a ser incluído.
	 * @return a versão persistida (com uuid preenchido) deste Pedido
	 */
	Pedido incluir(Pedido pedido);

	/**
	 * Altera um Pedido existente.
	 *
	 * @param uuid o id do Pedido a ser alterado
	 * @param pedido os novos dados para o Pedido
	 * @return a versão persistida deste pedido
	 * @throws EntidadeNaoEncontradaException se não existir Pedido com o uuid informado
	 */
	Pedido alterar(UUID uuid, Pedido pedido);

	/**
	 * Exclui um Pedido existente.
	 *
	 * @param uuid id do Pedido a ser excluído
	 * @throws EntidadeNaoEncontradaException se não existir Pedido com o uuid informado
	 */
	void excluir(UUID uuid);

	/**
	 * Aplica um fator de desconto num dado {@link Pedido}, atualizando os valores dos
	 * seus {@link ItemPedido}s.
	 *
	 * @param uuid o uuid do Pedido a se aplicar o fator de desconto
	 * @param fatorDesconto o novo fator de desconto
	 * @return o Pedido, com o novo valor de fator de desconto aplicado
	 */
	Pedido aplicarDesconto(UUID uuid, BigDecimal fatorDesconto);

}
