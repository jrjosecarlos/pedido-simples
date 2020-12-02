/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.BooleanExpression;

import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.Pedido;

/**
 * Interface para as queries customizadas que envolvem {@link ItemPedido}. A principal diferença envolve
 * a necessidade de fetch joins envolvendo {@link Pedido} e {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
public interface CustomItemPedidoRepository {

	Page<ItemPedido> findByPedido(Pedido pedido, BooleanExpression predicate, Pageable pageable);

	Optional<ItemPedido> findById(UUID id);
}
