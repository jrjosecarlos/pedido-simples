/**
 *
 */
package br.org.casa.pedidosimples.service.impl;

import java.math.BigDecimal;
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
import br.org.casa.pedidosimples.repository.PedidoPredicateBuilder;
import br.org.casa.pedidosimples.repository.PedidoRepository;
import br.org.casa.pedidosimples.service.ItemPedidoService;
import br.org.casa.pedidosimples.service.PedidoService;

/**
 * Implementação de serviços para {@link Pedido}.
 *
 * @author jrjosecarlos
 *
 */
@Service
@Transactional(readOnly = true)
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ItemPedidoService itemPedidoService;

	PedidoServiceImpl() {

	}

	@Override
	public Page<Pedido> buscarTodos(Pageable pageable, Map<String, String> parametrosBusca) {
		BooleanExpression predicate = PedidoPredicateBuilder.of(parametrosBusca)
			.build();

		return pedidoRepository.findAll(predicate, pageable);
	}

	@Override
	public Optional<Pedido> buscarPorId(UUID uuid) {
		return pedidoRepository.findById(uuid);
	}

	@Override
	@Transactional
	public Pedido incluir(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	@Override
	@Transactional
	public Pedido alterar(UUID uuid, Pedido pedido) {
		Pedido existente = pedidoRepository.findById(uuid)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuid));

		if (!pedido.getFatorDesconto().equals(existente.getFatorDesconto())) {
			throw new OperacaoInvalidaException(String.format("Não é possível alterar diretamente o fator de desconto do %s. "
					+ "Utilize o método /aplicar-desconto.", Pedido.NOME_EXIBICAO_ENTIDADE));
		}

		if (!pedido.getSituacao().equals(existente.getSituacao())) {
			throw new OperacaoInvalidaException(String.format("Não é possível alterar diretamente a situação do %s. "
					+ "Utilize o método /fechar.", Pedido.NOME_EXIBICAO_ENTIDADE));
		}

		existente.setCodigo(pedido.getCodigo());

		return pedidoRepository.save(existente);
	}

	@Override
	@Transactional
	public void excluir(UUID uuid) {
		Pedido pedido = pedidoRepository.findById(uuid)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuid));

		itemPedidoService.excluirPorPedido(pedido);
		pedidoRepository.delete(pedido);
	}

	@Override
	@Transactional
	public Pedido aplicarDesconto(UUID uuid, BigDecimal fatorDesconto) {
		Pedido pedido = pedidoRepository.findById(uuid)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuid));

		if (SituacaoPedido.FECHADO.equals(pedido.getSituacao())) {
			throw new OperacaoInvalidaException(String.format("Não é possível alterar o fator de desconto de um %s fechado",
					Pedido.NOME_EXIBICAO_ENTIDADE));
		}

		pedido.setFatorDesconto(fatorDesconto);
		itemPedidoService.atualizarValores(pedidoRepository.save(pedido));

		return pedido;
	}

	@Override
	@Transactional
	public Pedido fechar(UUID uuid) {
		Pedido pedido = pedidoRepository.findById(uuid)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuid));

		if (SituacaoPedido.FECHADO.equals(pedido.getSituacao())) {
			throw new OperacaoInvalidaException(String.format("Não é possível fechar um %s já fechado", Pedido.NOME_EXIBICAO_ENTIDADE));
		}

		// Já existe um bloqueio de alteração de status de ItemVenda quando estão associados a ItemVenda de Pedidos abertos.
		// Essa verificação é uma redundância para garantir que não fechamento com ItemVenda inativo.
		if (itemPedidoService.contarPorPedidoEItemVendaInativo(pedido) > 0L) {
			throw new OperacaoInvalidaException(String.format("Não é possível fechar este %s pois possui %s associados a %s inativos",
					Pedido.NOME_EXIBICAO_ENTIDADE,
					ItemPedido.NOME_EXIBICAO_ENTIDADE,
					ItemVenda.NOME_EXIBICAO_ENTIDADE));
		}

		// O recálculo também é uma redundância por garantia, dado que os valores são recalculados a cada alteração (valor do ItemVenda
		// ou fator de desconto do Pedido).
		itemPedidoService.atualizarValores(pedido);

		pedido.setSituacao(SituacaoPedido.FECHADO);
		return pedidoRepository.save(pedido);
	}
}
