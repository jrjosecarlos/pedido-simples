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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;
import br.org.casa.pedidosimples.repository.PedidoRepository;
import br.org.casa.pedidosimples.service.ItemPedidoService;
import br.org.casa.pedidosimples.service.PedidoService;

/**
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
public class PedidoServiceImplTest {
	@Autowired
    private PedidoService service;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private ItemPedidoService itemPedidoService;

    private static Answer<Pedido> answerDevolveParametro = new Answer<Pedido>() {
		public Pedido answer(InvocationOnMock invocation) throws Throwable {
			return (Pedido) invocation.getArguments()[0];
		}
	};

    @TestConfiguration
    static class PedidoServiceImplTestContextConfiguration {

        @Bean
        public PedidoService pedidoService() {
            return new PedidoServiceImpl();
        }
    }

    @Test
    public void testBuscarTodos() {
    	Page<Pedido> page = new PageImpl<Pedido>(Collections.emptyList());
    	Pageable pageable = mock(Pageable.class);

    	when(pedidoRepository.findAll(any(Predicate.class), any(Pageable.class)))
    		.thenReturn(page);

    	Page<Pedido> pageRetornado = service.buscarTodos(pageable, Collections.emptyMap());

    	assertThat(pageRetornado)
    		.isEqualTo(page);
    }

    @Test
    public void testBuscarPorId() {
    	Pedido pedido = new Pedido();

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(pedido));

    	assertThat(service.buscarPorId(UUID.randomUUID()))
    		.contains(pedido);
    }

    @Test
    public void testIncluir() {
    	Pedido pedidoAIncluir = new Pedido();
    	Pedido pedidoIncluido = new Pedido();
    	pedidoIncluido.setId(UUID.randomUUID());

    	when(pedidoRepository.save(pedidoAIncluir))
    		.thenReturn(pedidoIncluido);

    	assertThat(service.incluir(pedidoAIncluir))
    		.isEqualTo(pedidoIncluido);
    }

    @Test
    public void testAlterarComEntidadeNaoEncontrada() {
    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.empty());

    	assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
    		.isThrownBy(() -> service.alterar(UUID.randomUUID(), new Pedido()));
    }

    @Test
    public void testAlterarAlterandoFatorDesconto() {
    	Pedido aAlterar = new Pedido();
    	aAlterar.setFatorDesconto(new BigDecimal("0.00"));

    	Pedido existente = new Pedido();
    	existente.setFatorDesconto(new BigDecimal("1.00"));

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.alterar(aAlterar.getId(), aAlterar))
			.withMessageContaining("Não é possível alterar diretamente o fator de desconto");
    }

    @Test
    public void testAlterarAlterandoSituacao() {
    	Pedido aAlterar = new Pedido();
    	aAlterar.setFatorDesconto(new BigDecimal("1.00"));
    	aAlterar.setSituacao(SituacaoPedido.FECHADO);

    	Pedido existente = new Pedido();
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.ABERTO);

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
			.isThrownBy(() -> service.alterar(aAlterar.getId(), aAlterar))
			.withMessageContaining("Não é possível alterar diretamente a situação");
    }

    @Test
    public void testAlterarComSucesso() {
    	Pedido aAlterar = new Pedido();
    	aAlterar.setFatorDesconto(new BigDecimal("1.00"));
    	aAlterar.setSituacao(SituacaoPedido.ABERTO);
    	aAlterar.setCodigo("00110022");

    	Pedido existente = new Pedido();
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.ABERTO);
    	existente.setCodigo("00110011");

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));
    	when(pedidoRepository.save(any()))
	    	.thenAnswer(answerDevolveParametro);

    	Pedido retornado = service.alterar(aAlterar.getId(), aAlterar);

    	assertThat(retornado.getCodigo())
    		.isEqualTo(aAlterar.getCodigo());
    }

    @Test
    public void testExcluirNaoEncontrado() {
    	when(pedidoRepository.findById(any()))
			.thenReturn(Optional.empty());

		assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
			.isThrownBy(() -> service.excluir(UUID.randomUUID()));
    }


    @Test
    public void testExcluirComSucesso() {
    	Pedido existente = new Pedido();
    	existente.setId(UUID.randomUUID());
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.ABERTO);
    	existente.setCodigo("00110011");

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));
    	when(itemPedidoService.excluirPorPedido(existente))
    		.thenReturn(2L);
    	doNothing().when(pedidoRepository).delete(any());

    	assertThatCode( () -> service.excluir(existente.getId()))
    		.doesNotThrowAnyException();
    	verify(pedidoRepository).delete(existente);
    }

    @Test
    public void testAplicarDescontoNaoEncontrado() {
    	when(pedidoRepository.findById(any()))
			.thenReturn(Optional.empty());

		assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
			.isThrownBy(() -> service.aplicarDesconto(UUID.randomUUID(), BigDecimal.ONE));
    }

    @Test
    public void testAplicarDescontoOperacaoInvalida() {
    	Pedido existente = new Pedido();
    	existente.setId(UUID.randomUUID());
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.FECHADO);
    	existente.setCodigo("00110011");

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
    		.isThrownBy(() -> service.aplicarDesconto(existente.getId(), BigDecimal.ZERO))
    		.withMessageContaining("Não é possível alterar o fator de desconto");
    }

    @Test
    public void testAplicarDescontoComSucesso() {
    	Pedido existente = new Pedido();
    	existente.setId(UUID.randomUUID());
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.ABERTO);
    	existente.setCodigo("00110011");

    	BigDecimal novoFatorDesconto = new BigDecimal("0.50");

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));

    	when(pedidoRepository.save(any())).thenAnswer(answerDevolveParametro);
    	doNothing().when(itemPedidoService).atualizarValores(any(Pedido.class));

    	assertThatCode( () -> service.aplicarDesconto(existente.getId(), novoFatorDesconto))
    		.doesNotThrowAnyException();
    	assertThat(existente.getFatorDesconto())
    		.isEqualTo(novoFatorDesconto);
    }

    @Test
    public void testFecharNaoEncontrado() {
    	when(pedidoRepository.findById(any()))
			.thenReturn(Optional.empty());

		assertThatExceptionOfType(EntidadeNaoEncontradaException.class)
			.isThrownBy(() -> service.fechar(UUID.randomUUID()));
    }

    @Test
    public void testFecharOperacaoInvalidaJaFechado() {
    	Pedido existente = new Pedido();
    	existente.setId(UUID.randomUUID());
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.FECHADO);
    	existente.setCodigo("00110011");

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
    		.isThrownBy(() -> service.fechar(existente.getId()))
    		.withMessageContaining("Não é possível fechar um Pedido já fechado");
    }

    @Test
    public void testFecharOperacaoInvalidaItensInativos() {
    	Pedido existente = new Pedido();
    	existente.setId(UUID.randomUUID());
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.ABERTO);
    	existente.setCodigo("00110011");

    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));
    	when(itemPedidoService.contarPorPedidoEItemVendaInativo(any()))
    		.thenReturn(3L);

    	assertThatExceptionOfType(OperacaoInvalidaException.class)
    		.isThrownBy(() -> service.fechar(existente.getId()))
    		.withMessageContaining("Não é possível fechar este Pedido");
    }

    @Test
    public void testFecharComSucesso() {
    	Pedido existente = new Pedido();
    	existente.setId(UUID.randomUUID());
    	existente.setFatorDesconto(new BigDecimal("1.00"));
    	existente.setSituacao(SituacaoPedido.ABERTO);
    	existente.setCodigo("00110011");


    	when(pedidoRepository.findById(any()))
    		.thenReturn(Optional.of(existente));
    	when(itemPedidoService.contarPorPedidoEItemVendaInativo(any()))
    		.thenReturn(0L);
    	doNothing().when(itemPedidoService).atualizarValores(any(Pedido.class));
    	when(pedidoRepository.save(any())).thenAnswer(answerDevolveParametro);


    	assertThatCode( () -> service.fechar(existente.getId()))
    		.doesNotThrowAnyException();
    	assertThat(existente.getSituacao())
    		.isEqualTo(SituacaoPedido.FECHADO);
    }

}
