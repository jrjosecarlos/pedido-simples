/**
 *
 */
package br.org.casa.pedidosimples.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.exception.OperacaoInvalidaException;
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;

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
	 * Atualiza os valores de todos os {@link ItemPedido} associados ao Pedido informado.
	 *
	 * @param pedido o Pedido que terá os valores dos itens atualizados
	 * @throws EntidadeNaoEncontradaException se não existir o Pedido informado.
	 * @throws OperacaoInvalidaException se o Pedido estiver com situação
	 * {@link SituacaoPedido#FECHADO} ou se estiver vinculado a um {@link ItemVenda} desativado
	 */
	void atualizarValores(Pedido pedido);

	/**
	 * Atualiza os valores de todos os {@link ItemPedido} associados ao {@link ItemVenda}
	 * informados e que sejam de Pedidos em aberto.
	 *
	 * @param itemVenda o itemVenda que se deseja buscar
	 * @throws EntidadeNaoEncontradaException se o itemVenda informado não existir.
	 * @throws OperacaoInvalidaException se o itemVenda não estiver ativo
	 */
	void atualizarValores(ItemVenda itemVenda);

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
	 * {@link ItemVenda}. Seu valor é calculado em função do valor do itemVenda, do fator de
	 * desconto relacionado ao tipo do itemVenda e do fator de desconto do Pedido.
	 *
	 * @param uuidPedido o Pedido que terá um itemPedido incluído
	 * @param itemVenda o ItemVenda a ser incluído no pedido
	 * @return a versão persistida (com uuid preenchido) deste itemPedido
	 * @throws EntidadeNaoEncontradaException se {@code uuidPedido} ou {@code itemVenda} não corresponderem
	 * a nenhuma entidade existente
	 * @throws OperacaoInvalidaException se o pedido em questão estiver fechado ou se o itemVenda em
	 * questão estiver inativo
	 */
	ItemPedido incluir(UUID uuidPedido, ItemVenda itemVenda);

	/**
	 * Exclui um ItemPedido existente.
	 *
	 * @param uuid id do ItemPedido a ser excluído
	 * @throws EntidadeNaoEncontradaException se não existir ItemPedido com o uuid informado
	 */
	void excluir(UUID uuid);

	/**
	 * Retorna o número de {@link ItemPedido} associados ao {@link ItemVenda} informado
	 * e que sejam de {@link Pedido}s em {@link SituacaoPedido#ABERTO aberto}.
	 *
	 * @param itemVenda o itemVenda que se deseja buscar
	 * @return a quantidade de ItemPedido associada a este itemVenda e que estejam em
	 * pedidos em aberto. Maior ou igual a zero.
	 */
	long contarPorItemVendaEPedidoAtivo(ItemVenda itemVenda);

	/**
	 * Retorna o número de {@link ItemPedido} associados ao {@link ItemVenda} informado,
	 * independente da situação do {@link Pedido} associado.
	 *
	 * @param itemVenda o itemVenda que se deseja buscar
	 * @return a quantidade de ItemPedido associada a este itemVenda. Maior ou igual a zero.
	 */
	long contarPorItemVenda(ItemVenda itemVenda);

	/**
	 * Exclui todos os {@link ItemPedido}s de um certo {@link Pedido}.
	 *
	 * @param pedido o pedido que terá os ItemPedido excluídos
	 * @return o número de itensPedido excluídos
	 */
	long excluirPorPedido(Pedido pedido);

	/**
	 * Retorna o número de {@link ItemPedido} associados ao {@link Pedido} informado e que
	 * também estejam associados a {@link ItemVenda} que estejam inativos
	 *
	 * @param pedido o pedido que se deseja buscar
	 * @return a quantidade de ItemPedido associada a ItemVenda inativo. Pode ser zero.
	 */
	long contarPorPedidoEItemVendaInativo(Pedido pedido);

	/**
	 * Retorna os {@link ItemPedido} associados ao Pedido informado.
	 *
	 * @param pedido o pedido que se deseja buscar
	 * @return uma Lista com os ItemPedido associados ao pedido informado
	 * @apiNote Este método possui uso mais interno à camada de Serviço do que os demais, assim
	 * fica a cargo do chamador do método verificar se o Pedido informado realmente existe.
	 * Caso não exista, será simplesmente retornada uma lista vazia.
	 */
	List<ItemPedido> buscarPorPedido(Pedido pedido);

}
