/**
 *
 */
package br.org.casa.pedidosimples.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.repository.ItemVendaPredicateBuilder;
import br.org.casa.pedidosimples.repository.ItemVendaRepository;
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
		return itemVendaRepository.findById(uuid)
				.map(iv -> {
					itemVenda.setId(uuid);
					return itemVendaRepository.save(itemVenda);
				}).orElseThrow(gerarEntidadeNaoEncontradaException(uuid));
	}

	@Override
	@Transactional
	public void excluir(UUID uuid) {
		itemVendaRepository.delete(itemVendaRepository.findById(uuid)
			.orElseThrow(gerarEntidadeNaoEncontradaException(uuid)));
	}

	/**
	 * Método de facilidade para criar {@link EntidadeNaoEncontradaException} relacionados à {@link ItemVenda};
	 *
	 * @param uuid uuid a ser exibido na exceção
	 * @return um {@link Supplier} que cria exceções com o nome da entidade fixo e o uuid informado.
	 * @see EntidadeNaoEncontradaException#EntidadeNaoEncontradaException(String, UUID)
	 */
	private Supplier<EntidadeNaoEncontradaException> gerarEntidadeNaoEncontradaException(UUID uuid) {
		return () -> new EntidadeNaoEncontradaException(ItemVenda.NOME_EXIBICAO_ENTIDADE, uuid);
	}

}
