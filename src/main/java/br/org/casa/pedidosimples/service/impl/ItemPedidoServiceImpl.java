/**
 *
 */
package br.org.casa.pedidosimples.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.repository.ItemPedidoPredicateBuilder;
import br.org.casa.pedidosimples.repository.ItemPedidoRepository;
import br.org.casa.pedidosimples.service.ItemPedidoService;
import br.org.casa.pedidosimples.service.PedidoService;

/**
 * Implementação de serviços para {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
@Service
@Transactional(readOnly = true)
public class ItemPedidoServiceImpl implements ItemPedidoService {

	private final ItemPedidoRepository itemPedidoRepository;

	private final PedidoService pedidoService;

	ItemPedidoServiceImpl(ItemPedidoRepository itemPedidoRepository, PedidoService pedidoService) {
		this.itemPedidoRepository = itemPedidoRepository;
		this.pedidoService = pedidoService;
	}

	@Override
	public Page<ItemPedido> buscarTodos(UUID uuidPedido, Pageable pageable, Map<String, String> parametrosBusca) {
		Pedido pedido = pedidoService.buscarPorId(uuidPedido)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuidPedido));

		BooleanExpression predicate = ItemPedidoPredicateBuilder.of(parametrosBusca)
				.build();
		return itemPedidoRepository.findByPedido(pedido, predicate, pageable);
	}

	@Override
	public Optional<ItemPedido> buscarPorId(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ItemPedido incluir(UUID uuidPedido, ItemPedido itemPedido) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void excluir(UUID uuid) {
		// TODO Auto-generated method stub

	}

}
