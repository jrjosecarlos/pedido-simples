/**
 *
 */
package br.org.casa.pedidosimples.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.exception.OperacaoInvalidaException;
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;
import br.org.casa.pedidosimples.repository.ItemPedidoPredicateBuilder;
import br.org.casa.pedidosimples.repository.ItemPedidoRepository;
import br.org.casa.pedidosimples.service.ItemPedidoService;
import br.org.casa.pedidosimples.service.ItemVendaService;
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

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private ItemVendaService itemVendaService;

	ItemPedidoServiceImpl(ItemPedidoRepository itemPedidoRepository) {
		this.itemPedidoRepository = itemPedidoRepository;
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
	@Transactional
	public void atualizarValores(Pedido pedido) {
		Pedido pedidoExistente = pedidoService.buscarPorId(pedido.getId())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, pedido.getId()));

		if (SituacaoPedido.FECHADO.equals(pedidoExistente.getSituacao())) {
			throw new OperacaoInvalidaException("Não é possível atualizar os valores de um Pedido fechado");
		}

		itemPedidoRepository.findByPedido(pedidoExistente)
			.stream()
			.forEach(ItemPedido::calcularValor);
	}

	@Override
	@Transactional
	public void atualizarValores(ItemVenda itemVenda) {
		ItemVenda itemVendaExistente = itemVendaService.buscarPorId(itemVenda.getId())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(ItemVenda.NOME_EXIBICAO_ENTIDADE, itemVenda.getId()));

		itemPedidoRepository.buscarPorItemVendaEPedidoAberto(itemVendaExistente)
			.stream()
			.forEach(ItemPedido::calcularValor);
	}

	@Override
	public Optional<ItemPedido> buscarPorId(UUID uuid) {
		return itemPedidoRepository.findById(uuid);
	}

	@Override
	@Transactional
	public ItemPedido incluir(UUID uuidPedido, ItemVenda itemVenda) {
		Pedido pedido = pedidoService.buscarPorId(uuidPedido)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuidPedido));
		ItemVenda itemVendaExistente = itemVendaService.buscarPorId(itemVenda.getId())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(ItemVenda.NOME_EXIBICAO_ENTIDADE, itemVenda.getId()));

		if (!itemVenda.isAtivo()) {
			throw new OperacaoInvalidaException(String.format("Não é possível adicionar um %s inativo ao %s.",
					ItemVenda.NOME_EXIBICAO_ENTIDADE,
					Pedido.NOME_EXIBICAO_ENTIDADE
					));
		}

		if (SituacaoPedido.FECHADO.equals(pedido.getSituacao())) {
			throw new OperacaoInvalidaException(String.format("Não é possível adicionar um %s a um %s fechado.",
					ItemVenda.NOME_EXIBICAO_ENTIDADE,
					Pedido.NOME_EXIBICAO_ENTIDADE
					));
		}

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setPedido(pedido);
		itemPedido.setItemVenda(itemVendaExistente);
		itemPedido.calcularValor();

		return itemPedidoRepository.save(itemPedido);
	}

	@Override
	@Transactional
	public void excluir(UUID uuid) {
		itemPedidoRepository.delete(itemPedidoRepository.findById(uuid)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(ItemPedido.NOME_EXIBICAO_ENTIDADE, uuid)));
	}

	@Override
	public long contarPorItemVendaEPedidoAtivo(ItemVenda itemVenda) {
		return itemPedidoRepository.contarPorItemVendaEPedidoAberto(itemVenda);
	}

	@Override
	public long contarPorItemVenda(ItemVenda itemVenda) {
		return itemPedidoRepository.countByItemVenda(itemVenda);
	}

	@Override
	public long excluirPorPedido(Pedido pedido) {
		return itemPedidoRepository.deleteByPedido(pedido);
	}

	@Override
	public long contarPorPedidoEItemVendaInativo(Pedido pedido) {
		return itemPedidoRepository.contarPorPedidoEItemVendaInativo(pedido);
	}

}
