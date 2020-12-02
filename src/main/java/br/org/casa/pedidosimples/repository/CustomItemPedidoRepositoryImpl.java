/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.QItemPedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;

/**
 * Implementação das queries customizadas para {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
public class CustomItemPedidoRepositoryImpl extends QuerydslRepositorySupport
	implements CustomItemPedidoRepository<ItemPedido, UUID> {

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
	public List<ItemPedido> findByPedido(Pedido pedido) {
		return from(itemPedido)
				.where(itemPedido.pedido.eq(pedido))
				.innerJoin(itemPedido.pedido).fetchJoin()
				.innerJoin(itemPedido.itemVenda).fetchJoin()
				.fetch();
	}

	@Override
	public Optional<ItemPedido> findById(UUID id) {
		return Optional.ofNullable(from(itemPedido)
			.where(itemPedido.id.eq(id))
			.innerJoin(itemPedido.pedido).fetchJoin()
			.innerJoin(itemPedido.itemVenda).fetchJoin()
			.fetchOne() );
	}

	@Override
	public long contarPorItemVendaEPedidoAtivo(ItemVenda itemVenda) {
		return from(itemPedido)
				.where(itemPedido.itemVenda.eq(itemVenda)
						.and(itemPedido.pedido.situacao.eq(SituacaoPedido.ABERTO)))
				.fetchCount();
	}

	@Override
	public List<ItemPedido> buscarPorItemVendaEPedidoAtivo(ItemVenda itemVenda) {
		return from(itemPedido)
				.where(itemPedido.itemVenda.eq(itemVenda)
						.and(itemPedido.pedido.situacao.eq(SituacaoPedido.ABERTO)))
				.innerJoin(itemPedido.pedido).fetchJoin()
				.innerJoin(itemPedido.itemVenda).fetchJoin()
				.fetch();
	}

}
