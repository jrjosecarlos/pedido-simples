/**
 *
 */
package br.org.casa.pedidosimples.service.impl;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.BooleanExpression;

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
public class ItemVendaServiceImpl implements ItemVendaService {

	private final ItemVendaRepository itemVendaRepository;

	ItemVendaServiceImpl(ItemVendaRepository itemVendaRepository) {
		this.itemVendaRepository = itemVendaRepository;
	}

	@Override
	public Page<ItemVenda> buscarTodosItensVenda(Pageable pageable,
			Map<String, String> parametrosBusca) {
		BooleanExpression predicate = ItemVendaPredicateBuilder.of(parametrosBusca)
			.build();

		return itemVendaRepository.findAll(predicate, pageable);
	}

}
