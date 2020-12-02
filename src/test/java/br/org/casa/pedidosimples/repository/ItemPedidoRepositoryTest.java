/**
 *
 */
package br.org.casa.pedidosimples.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.dsl.Expressions;

import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.model.enumeration.SituacaoPedido;
import br.org.casa.pedidosimples.model.enumeration.TipoItemVenda;

/**
 * Classe de testes para {@link ItemPedidoRepository}.
 *
 * @author jrjosecarlos
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ItemPedidoRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	private ItemVenda itemVenda;

	private Pedido pedido;

	private ItemPedido itemPedido;

	@Before
	public void setup() {
		itemVenda = new ItemVenda();
		itemVenda.setNome("Item-venda-1");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("1.23"));
		itemVenda.setAtivo(true);

		itemVenda = entityManager.persist(itemVenda);

		pedido = new Pedido();
		pedido.setCodigo("11234556");
		pedido.setFatorDesconto(new BigDecimal(0.00));
		pedido.setSituacao(SituacaoPedido.ABERTO);

		pedido = entityManager.persist(pedido);

		itemPedido = new ItemPedido();
		itemPedido.setItemVenda(itemVenda);
		itemPedido.setPedido(pedido);
		itemPedido.calcularValor();

		itemPedido = entityManager.persist(itemPedido);
	}

	@Test
	public void testCountByItemVenda() {
		assertThat(itemPedidoRepository.countByItemVenda(itemVenda))
			.isEqualTo(1L);

	}

	@Test
	public void testFindByPedidoPage() {
		Page<ItemPedido> page = itemPedidoRepository.findByPedido(pedido,
				Expressions.asBoolean(true).isTrue(),
				Pageable.unpaged());

		assertThat(page.getNumberOfElements())
			.isEqualTo(1);
		assertThat(page.getContent())
			.hasSize(1)
			.allSatisfy(ip -> {
				assertThat(ip)
					.isEqualTo(itemPedido);
			});
	}

	@Test
	public void testFindByPedidoList() {
		List<ItemPedido> list = itemPedidoRepository.findByPedido(pedido);

		assertThat(list)
			.hasSize(1)
			.allSatisfy(ip -> {
				assertThat(ip)
					.isEqualTo(itemPedido);
			});
	}

	@Test
	public void testFindByIdExistente() {
		Optional<ItemPedido> opItemPedido = itemPedidoRepository.findById(itemPedido.getId());

		assertThat(opItemPedido)
			.contains(itemPedido);
	}

	@Test
	public void testFindByIdInexistente() {
		Optional<ItemPedido> opItemPedido = itemPedidoRepository.findById(UUID.randomUUID());

		assertThat(opItemPedido)
			.isEmpty();
	}

	@Test
	public void testDeleteByPedido() {
		UUID id = itemPedido.getId();
		assertThat(itemPedidoRepository.findById(id))
			.isNotEmpty();

		long count = itemPedidoRepository.deleteByPedido(pedido);

		assertThat(count)
			.isEqualTo(1L);
		assertThat(itemPedidoRepository.findById(id))
			.isEmpty();
	}

	@Test
	public void testContarPorItemVendaEPedidoAberto() {
		assertThat(itemPedidoRepository.contarPorItemVendaEPedidoAberto(itemVenda))
			.isEqualTo(1L);
	}

	@Test
	public void testBuscarPorItemVendaEPedidoAberto() {
		assertThat(itemPedidoRepository.buscarPorItemVendaEPedidoAberto(itemVenda))
			.hasSize(1)
			.allSatisfy(ip -> {
				assertThat(ip)
					.isEqualTo(itemPedido);
			});
	}

	@Test
	public void testContarPorPedidoEItemVendaInativo() {
		assertThat(itemPedidoRepository.contarPorPedidoEItemVendaInativo(pedido))
			.isEqualTo(0L);
	}
}
