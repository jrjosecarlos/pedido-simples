/**
 *
 */
package br.org.casa.pedidosimples.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.Predicate;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.exception.OperacaoInvalidaException;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;
import br.org.casa.pedidosimples.repository.ItemVendaRepository;
import br.org.casa.pedidosimples.service.ItemPedidoService;
import br.org.casa.pedidosimples.service.ItemVendaService;

/**
 * Classe de testes para {@link ItemVendaServiceImpl}.
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
public class ItemVendaServiceImplTest implements ParameterAsAnswer {

	@Autowired
	private ItemVendaService service;

	@MockBean
	private ItemVendaRepository itemVendaRepository;

	@MockBean
	private ItemPedidoService itemPedidoService;

	@TestConfiguration
	static class ItemVendaServiceImplTestContextConfiguration {

		@Bean
		public ItemVendaService itemVendaService() {
			return new ItemVendaServiceImpl();
		}
	}

	@Test
	public void testBuscarTodos() {
		Page<ItemVenda> page = new PageImpl<ItemVenda>(Collections.emptyList());
		Pageable pageable = mock(Pageable.class);

		when(itemVendaRepository.findAll(any(Predicate.class), any(Pageable.class)))
			.thenReturn(page);

		Page<ItemVenda> pageRetornado = service.buscarTodos(pageable, Collections.emptyMap());

		assertThat(pageRetornado)
			.isEqualTo(page);
	}

	@Test
	public void testBuscarPorId() {
		ItemVenda itemVenda = new ItemVenda();

		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.of(itemVenda));

		assertThat(service.buscarPorId(UUID.randomUUID()))
			.contains(itemVenda);
	}

	@Test
	public void testIncluir() {
		ItemVenda itemVendaAIncluir = new ItemVenda();
		ItemVenda itemVendaIncluido = new ItemVenda();
		itemVendaIncluido.setId(UUID.randomUUID());

		when(itemVendaRepository.save(itemVendaAIncluir))
			.thenReturn(itemVendaIncluido);

		assertThat(service.incluir(itemVendaAIncluir))
			.isEqualTo(itemVendaIncluido);
	}

	@Test
	public void testAlterarComEntidadeNaoEncontrada() {
		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.empty());

		assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
			.isThrownBy(() -> service.alterar(UUID.randomUUID(), new ItemVenda()));
	}

	@Test
	public void testAlterarAlterandoTipo() {
		ItemVenda aAlterar = new ItemVenda();
		aAlterar.setId(UUID.randomUUID());
		aAlterar.setTipo(TipoItemVenda.PRODUTO);
		ItemVenda existente = new ItemVenda();
		existente.setTipo(TipoItemVenda.SERVICO);

		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.of(existente));

		assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.alterar(aAlterar.getId(), aAlterar))
			.withMessageContaining("Não é possível alterar o tipo");
	}

	@Test
	public void testAlterarDesativandoComItensPedidoAbertos() {
		ItemVenda aAlterar = new ItemVenda();
		aAlterar.setId(UUID.randomUUID());
		aAlterar.setTipo(TipoItemVenda.PRODUTO);
		aAlterar.setAtivo(false);

		ItemVenda existente = new ItemVenda();
		existente.setTipo(TipoItemVenda.PRODUTO);
		existente.setAtivo(true);

		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.of(existente));
		when(itemPedidoService.contarPorItemVendaEPedidoAtivo(any()))
			.thenReturn(1345L);

		assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.alterar(aAlterar.getId(), aAlterar))
			.withMessageContaining("Não é possível desativar")
			.withMessageContaining("1345");
	}

	@Test
	public void testAlterarMudandoValor() {
		ItemVenda aAlterar = new ItemVenda();
		aAlterar.setId(UUID.randomUUID());
		aAlterar.setTipo(TipoItemVenda.PRODUTO);
		aAlterar.setAtivo(true);
		aAlterar.setValorBase(new BigDecimal("2.00"));

		ItemVenda existente = new ItemVenda();
		existente.setTipo(TipoItemVenda.PRODUTO);
		existente.setAtivo(true);
		existente.setValorBase(new BigDecimal("1.00"));

		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.of(existente));
		when(itemVendaRepository.save(any()))
			.thenAnswer(this.<ItemVenda>getParameterAsAnswer());
		doNothing().when(itemPedidoService).atualizarValores(existente);

		ItemVenda retornado = service.alterar(aAlterar.getId(), aAlterar);

		assertThat(retornado.getValorBase())
			.isEqualTo(aAlterar.getValorBase());
	}

	@Test
	public void testExcluirNaoEncontrado() {
		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.empty());

		assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
			.isThrownBy(() -> service.excluir(UUID.randomUUID()));
	}

	@Test
	public void testExcluirOperacaoInvalida() {
		ItemVenda existente = new ItemVenda();
		existente.setId(UUID.randomUUID());
		existente.setTipo(TipoItemVenda.PRODUTO);
		existente.setAtivo(true);
		existente.setValorBase(new BigDecimal("1.00"));

		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.of(existente));
		when(itemPedidoService.contarPorItemVenda(any()))
			.thenReturn(2L);

		assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.excluir(existente.getId()))
			.withMessageContaining("Total de 2 associações");
	}

	@Test
	public void testExcluirComSucesso() {
		ItemVenda existente = new ItemVenda();
		existente.setId(UUID.randomUUID());
		existente.setTipo(TipoItemVenda.PRODUTO);
		existente.setAtivo(true);
		existente.setValorBase(new BigDecimal("1.00"));

		when(itemVendaRepository.findById(any()))
			.thenReturn(Optional.of(existente));
		when(itemPedidoService.contarPorItemVenda(any()))
			.thenReturn(0L);
		doNothing().when(itemVendaRepository).delete(any());

		assertThatCode( () -> service.excluir(existente.getId()))
			.doesNotThrowAnyException();
		verify(itemVendaRepository).delete(existente);

	}
}
