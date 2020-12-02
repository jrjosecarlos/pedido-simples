/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import br.org.casa.pedidosimples.model.ItemPedido;

/**
 * Definição de repositório para {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID>, QuerydslPredicateExecutor<ItemPedido>,
	CustomItemPedidoRepository<ItemPedido, UUID> {

}
