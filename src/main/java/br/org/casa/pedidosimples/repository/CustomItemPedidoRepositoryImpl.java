/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.QItemPedido;

/**
 * Implementação das queries customizadas para {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
public class CustomItemPedidoRepositoryImpl extends QuerydslRepositorySupport
	implements CustomItemPedidoRepository {

	private static QItemPedido itemPedido = QItemPedido.itemPedido;

	CustomItemPedidoRepositoryImpl() {
		super(ItemPedido.class);
	}

	@Override
	public Page<ItemPedido> findByPedido(Pedido pedido, BooleanExpression predicate, Pageable pageable) {
		BooleanExpression comPedidoPredicate = predicate.and(itemPedido.pedido.eq(pedido));

		JPQLQuery<ItemPedido> query = getQuerydsl()
			.applyPagination(pageable, from(itemPedido)
					.where(comPedidoPredicate)
					.innerJoin(itemPedido.pedido).fetchJoin()
					.innerJoin(itemPedido.itemVenda).fetchJoin());


		return new PageImpl<ItemPedido>(query.fetch(), pageable, query.fetchCount());
	}

	@Override
	public Optional<ItemPedido> findById(UUID id) {
		return Optional.ofNullable(from(itemPedido)
			.where(itemPedido.id.eq(id))
			.innerJoin(itemPedido.pedido).fetchJoin()
			.innerJoin(itemPedido.itemVenda).fetchJoin()
			.fetchOne() );
	}

}
