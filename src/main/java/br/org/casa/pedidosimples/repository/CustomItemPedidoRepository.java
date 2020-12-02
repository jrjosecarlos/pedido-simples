/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.BooleanExpression;

import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;

/**
 * Interface para as queries customizadas que envolvem {@link ItemPedido}. A principal diferença envolve
 * a necessidade de fetch joins envolvendo {@link Pedido} e {@link ItemPedido}.
 *
 * @param <S> tipo de marcação. É esperado que seja sempre {@link ItemPedido}
 * @param <ID> tipo de marcação. É esperado que seja sempre {@link UUID}.
 * @author jrjosecarlos
 * @implNote Apesar de este repositório ser específico para ItemPedido, é necessário definir os tipos genéricos
 * para que a sobreposição de métodos prevista pelo Spring Data funcione corretamente, de modo a evitar o erro
 * "ambiguous reference" ao compilar o projeto com Maven. Veja https://stackoverflow.com/a/29006761.
 *
 */
public interface CustomItemPedidoRepository<S extends ItemPedido, ID> {

	Page<S> findByPedido(Pedido pedido, BooleanExpression predicate, Pageable pageable);

	List<S> findByPedido(Pedido pedido);

	Optional<S> findById(ID id);

	long deleteByPedido(Pedido pedido);

	long contarPorItemVendaEPedidoAtivo(ItemVenda itemVenda);

	List<S> buscarPorItemVendaEPedidoAtivo(ItemVenda itemVenda);

}
