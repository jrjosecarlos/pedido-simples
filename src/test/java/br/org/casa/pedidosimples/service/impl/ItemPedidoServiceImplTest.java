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
import java.util.Arrays;
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

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.exception.OperacaoInvalidaException;
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;
import br.org.casa.pedidosimples.repository.ItemPedidoRepository;
import br.org.casa.pedidosimples.service.ItemPedidoService;
import br.org.casa.pedidosimples.service.ItemVendaService;
import br.org.casa.pedidosimples.service.PedidoService;

/**
 *
 * Classe de testes para {@link ItemPedidoServiceImpl}
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
public class ItemPedidoServiceImplTest implements ParameterAsAnswer {

	@Autowired
    private ItemPedidoService service;

	@MockBean
	private ItemPedidoRepository itemPedidoRepository;

	@MockBean
	private PedidoService pedidoService;

	@MockBean
	private ItemVendaService itemVendaService;

    @TestConfiguration
    static class ItemPedidoServiceImplTestContextConfiguration {

        @Bean
        public ItemPedidoService itemPedidoService() {
            return new ItemPedidoServiceImpl();
        }
    }

    @Test
    public void testBuscarTodos() {
    	Page<ItemPedido> page = new PageImpl<ItemPedido>(Collections.emptyList());
    	Pageable pageable = mock(Pageable.class);

    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());

    	when(pedidoService.buscarPorId(any()))
			.thenReturn(Optional.of(pedido));
    	when(itemPedidoRepository.findByPedido(any(), any(), any()))
    		.thenReturn(page);


    	Page<ItemPedido> pageRetornado = service.buscarTodos(pedido.getId(), pageable, Collections.emptyMap());

    	assertThat(pageRetornado)
    		.isEqualTo(page);
    }

    @Test
    public void testBuscarTodosPedidoNaoEncontrado() {
    	Pageable pageable = mock(Pageable.class);

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.empty());

    	assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
    		.isThrownBy(() -> service.buscarTodos(UUID.randomUUID(), pageable, Collections.emptyMap()) );
    }

    @Test
    public void testAtualizarValoresPedidoNaoEncontrado() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.empty());

    	assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
    		.isThrownBy(() -> service.atualizarValores(pedido) );
    }

    @Test
    public void testAtualizarValoresPedidoFechado() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());
    	pedido.setSituacao(SituacaoPedido.FECHADO);

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.of(pedido));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
    		.isThrownBy(() -> service.atualizarValores(pedido) )
    		.withMessageContaining("Não é possível atualizar os valores de um Pedido fechado");
    }

    @Test
    public void testAtualizarValoresPedidoItemVendaInativo() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());
    	pedido.setSituacao(SituacaoPedido.ABERTO);
    	pedido.setFatorDesconto(new BigDecimal("0.50"));

    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setAtivo(false);

    	ItemPedido itemPedido = new ItemPedido();
    	itemPedido.setPedido(pedido);
    	itemPedido.setItemVenda(itemVenda);

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.of(pedido));
    	when(itemPedidoRepository.findByPedido(pedido))
    		.thenReturn(Arrays.asList(itemPedido));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.atualizarValores(pedido) )
			.withMessageContaining("Não é possível atualizar os valores do Pedido")
			.withMessageContaining("inativo");
    }

    @Test
    public void testAtualizarValoresPedidoComSucesso() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());
    	pedido.setSituacao(SituacaoPedido.ABERTO);
    	pedido.setFatorDesconto(new BigDecimal("0.50"));

    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setAtivo(true);
    	itemVenda.setValorBase(new BigDecimal("100.00"));
    	itemVenda.setTipo(TipoItemVenda.PRODUTO);

    	ItemPedido itemPedido = new ItemPedido();
    	itemPedido.setPedido(pedido);
    	itemPedido.setItemVenda(itemVenda);

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.of(pedido));
    	when(itemPedidoRepository.findByPedido(pedido))
    		.thenReturn(Arrays.asList(itemPedido));

    	service.atualizarValores(pedido);

    	assertThat(itemPedido.getValor())
    		.isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    public void testAtualizarValoresItemVendaNaoEncontrado() {
    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());

    	when(itemVendaService.buscarPorId(any()))
    		.thenReturn(Optional.empty());

    	assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
    		.isThrownBy(() -> service.atualizarValores(itemVenda) );
    }

    @Test
    public void testAtualizarValoresItemVendaInativo() {
    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());
    	itemVenda.setAtivo(false);

    	when(itemVendaService.buscarPorId(any()))
    		.thenReturn(Optional.of(itemVenda));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.atualizarValores(itemVenda) )
			.withMessageContaining("Não é possível atualizar os valores")
			.withMessageContaining("Item de Venda")
			.withMessageContaining("inativo");
    }

    @Test
    public void testAtualizarValoresItemVendaComSucesso() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());
    	pedido.setSituacao(SituacaoPedido.ABERTO);
    	pedido.setFatorDesconto(new BigDecimal("0.50"));

    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setAtivo(true);
    	itemVenda.setValorBase(new BigDecimal("100.00"));
    	itemVenda.setTipo(TipoItemVenda.PRODUTO);

    	ItemPedido itemPedido = new ItemPedido();
    	itemPedido.setPedido(pedido);
    	itemPedido.setItemVenda(itemVenda);

    	when(itemVendaService.buscarPorId(any()))
			.thenReturn(Optional.of(itemVenda));
    	when(itemPedidoRepository.buscarPorItemVendaEPedidoAberto(itemVenda))
    		.thenReturn(Arrays.asList(itemPedido));

    	service.atualizarValores(itemVenda);

    	assertThat(itemPedido.getValor())
    		.isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    public void testBuscarPorId() {
    	ItemPedido itemPedido = new ItemPedido();

    	when(itemPedidoRepository.findById(any()))
    		.thenReturn(Optional.of(itemPedido));

    	assertThat(service.buscarPorId(UUID.randomUUID()))
    		.contains(itemPedido);
    }

    @Test
    public void testIncluirPedidoInexistente() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());

    	ItemVenda itemVenda = new ItemVenda();

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.empty());

    	assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
    		.isThrownBy(() -> service.incluir(pedido.getId(), itemVenda) )
    		.withMessageContaining(pedido.getId().toString());
    }

    @Test
    public void testIncluirItemVendaInexistente() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());

    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.of(pedido));
    	when(itemVendaService.buscarPorId(itemVenda.getId()))
    		.thenReturn(Optional.empty());

    	assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
			.isThrownBy(() -> service.incluir(pedido.getId(), itemVenda) )
			.withMessageContaining(itemVenda.getId().toString());
    }

    @Test
    public void testIncluirItemVendaInativo() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());

    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());
    	itemVenda.setAtivo(false);

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.of(pedido));
    	when(itemVendaService.buscarPorId(itemVenda.getId()))
    		.thenReturn(Optional.of(itemVenda));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.incluir(pedido.getId(), itemVenda) )
			.withMessageContaining("Não é possível adicionar um Item de Venda inativo");
    }

    @Test
    public void testIncluirPedidoFechado() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());
    	pedido.setSituacao(SituacaoPedido.FECHADO);

    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());
    	itemVenda.setAtivo(true);

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.of(pedido));
    	when(itemVendaService.buscarPorId(itemVenda.getId()))
    		.thenReturn(Optional.of(itemVenda));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.incluir(pedido.getId(), itemVenda) )
			.withMessageContaining("Não é possível adicionar um Item de Venda a um Pedido fechado");
    }

    @Test
    public void testIncluirComSucesso() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());
    	pedido.setSituacao(SituacaoPedido.ABERTO);
    	pedido.setFatorDesconto(new BigDecimal("0.00"));

    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());
    	itemVenda.setAtivo(true);
    	itemVenda.setTipo(TipoItemVenda.SERVICO);
    	itemVenda.setValorBase(new BigDecimal("250.00"));

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.of(pedido));
    	when(itemVendaService.buscarPorId(itemVenda.getId()))
    		.thenReturn(Optional.of(itemVenda));
    	when(itemPedidoRepository.save(any()))
    		.thenAnswer(this.<ItemPedido>getParameterAsAnswer());

    	ItemPedido incluido = service.incluir(pedido.getId(), itemVenda);

    	assertThat(incluido.getValor())
    		.isEqualTo(new BigDecimal("250.00"));
    }

    @Test
    public void testExcluirItemPedidoNaoEncontrado() {
    	ItemPedido itemPedido = new ItemPedido();
    	itemPedido.setId(UUID.randomUUID());

    	when(pedidoService.buscarPorId(any()))
    		.thenReturn(Optional.empty());

    	assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
			.isThrownBy(() -> service.excluir(itemPedido.getId()) );
    }

    @Test
    public void testExcluirComSucesso() {
    	ItemPedido itemPedido = new ItemPedido();
    	itemPedido.setId(UUID.randomUUID());

    	when(itemPedidoRepository.findById(any()))
    		.thenReturn(Optional.of(itemPedido));
    	doNothing().when(itemPedidoRepository).delete(any());

    	assertThatCode( () -> service.excluir(itemPedido.getId()) )
    		.doesNotThrowAnyException();
    	verify(itemPedidoRepository).delete(itemPedido);
    }

    @Test
    public void testContarPorItemVendaEPedidoAtivo() {
    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());

    	when(itemPedidoRepository.contarPorItemVendaEPedidoAberto(itemVenda))
    		.thenReturn(5L);

    	assertThat(service.contarPorItemVendaEPedidoAtivo(itemVenda))
    		.isEqualTo(5L);
    }

    @Test
    public void testContarPorItemVenda() {
    	ItemVenda itemVenda = new ItemVenda();
    	itemVenda.setId(UUID.randomUUID());

    	when(itemPedidoRepository.countByItemVenda(itemVenda))
    		.thenReturn(10L);

    	assertThat(service.contarPorItemVenda(itemVenda))
    		.isEqualTo(10L);
    }



    @Test
    public void testExcluirPorPedido() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());

    	when(itemPedidoRepository.deleteByPedido(pedido))
    		.thenReturn(15L);

    	assertThat(service.excluirPorPedido(pedido))
    		.isEqualTo(15L);
    }

    @Test
    public void testContarPorPedidoEItemVendaInativo() {
    	Pedido pedido = new Pedido();
    	pedido.setId(UUID.randomUUID());

    	when(itemPedidoRepository.contarPorPedidoEItemVendaInativo(pedido))
    		.thenReturn(20L);

    	assertThat(service.contarPorPedidoEItemVendaInativo(pedido))
    		.isEqualTo(20L);
    }

    @Test
    public void testBuscarPorPedido() {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setId(UUID.randomUUID());

		Pedido pedido = new Pedido();
		pedido.setId(UUID.randomUUID());

		when(itemPedidoRepository.findByPedido(pedido))
			.thenReturn(Arrays.asList(itemPedido));

		assertThat(service.buscarPorPedido(pedido))
			.hasSize(1)
			.containsExactly(itemPedido);
    }

}
