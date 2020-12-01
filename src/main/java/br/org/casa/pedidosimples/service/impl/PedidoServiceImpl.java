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
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.repository.PedidoPredicateBuilder;
import br.org.casa.pedidosimples.repository.PedidoRepository;
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

	private final PedidoRepository pedidoRepository;

	PedidoServiceImpl(PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
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
		return pedidoRepository.findById(uuid)
				.map(p -> {
					p.setCodigo(pedido.getCodigo());
					return pedidoRepository.save(p);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuid));
	}

	@Override
	@Transactional
	public void excluir(UUID uuid) {
		pedidoRepository.delete(pedidoRepository.findById(uuid)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuid)));
	}

}
