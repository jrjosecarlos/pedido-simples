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
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.repository.ItemVendaPredicateBuilder;
import br.org.casa.pedidosimples.repository.ItemVendaRepository;
import br.org.casa.pedidosimples.service.ItemPedidoService;
import br.org.casa.pedidosimples.service.ItemVendaService;

/**
 * Implementação de serviços para {@link ItemVenda}.
 *
 * @author jrjosecarlos
 *
 */
@Service
@Transactional(readOnly = true)
public class ItemVendaServiceImpl implements ItemVendaService {

	private final ItemVendaRepository itemVendaRepository;

	@Autowired
	private ItemPedidoService itemPedidoService;

	ItemVendaServiceImpl(ItemVendaRepository itemVendaRepository) {
		this.itemVendaRepository = itemVendaRepository;
	}

	@Override
	public Page<ItemVenda> buscarTodos(Pageable pageable,
			Map<String, String> parametrosBusca) {
		BooleanExpression predicate = ItemVendaPredicateBuilder.of(parametrosBusca)
			.build();

		return itemVendaRepository.findAll(predicate, pageable);
	}

	@Override
	public Optional<ItemVenda> buscarPorId(UUID uuid) {
		return itemVendaRepository.findById(uuid);
	}

	@Override
	@Transactional
	public ItemVenda incluir(ItemVenda itemVenda) {
		return itemVendaRepository.save(itemVenda);
	}

	@Override
	@Transactional
	public ItemVenda alterar(UUID uuid, ItemVenda itemVenda) {
		ItemVenda existente = itemVendaRepository.findById(uuid)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(ItemVenda.NOME_EXIBICAO_ENTIDADE, uuid));

		if (!itemVenda.getTipo().equals(existente.getTipo())) {
			throw new OperacaoInvalidaException(String.format("Não é possível alterar o tipo de um %s", ItemVenda.NOME_EXIBICAO_ENTIDADE));
		}

		if ((itemVenda.isAtivo() == false) && (itemVenda.isAtivo() != existente.isAtivo())) {
			long itensPedidoAbertos = itemPedidoService.contarPorItemVendaEPedidoAtivo(itemVenda);
			if (itensPedidoAbertos > 0L) {
				throw new OperacaoInvalidaException(String.format("Não é possível desativar o %s, pois está associado "
						+ "a algum %s em aberto. Total de itens em aberto: %d",
						ItemVenda.NOME_EXIBICAO_ENTIDADE,
						Pedido.NOME_EXIBICAO_ENTIDADE,
						itensPedidoAbertos));
			}
		}

		// Necessário armazenar a necessidade ou não de atualizar os valores de itens de Pedido antes de aplicar os
		// novos valores, dado que a atualização em si depende de a entidade já ter sido atualizada no banco
		boolean doAtualizarValores = !itemVenda.getValorBase().equals(existente.getValorBase());

		existente.setNome(itemVenda.getNome());
		existente.setValorBase(itemVenda.getValorBase());
		existente.setAtivo(itemVenda.isAtivo());

		existente = itemVendaRepository.save(existente);

		if (doAtualizarValores) {
			itemPedidoService.atualizarValores(existente);
		}
		return existente;
	}

	@Override
	@Transactional
	public void excluir(UUID uuid) {
		itemVendaRepository.delete(itemVendaRepository.findById(uuid)
			.orElseThrow(() -> new EntidadeNaoEncontradaException(ItemVenda.NOME_EXIBICAO_ENTIDADE, uuid)));
	}

}
