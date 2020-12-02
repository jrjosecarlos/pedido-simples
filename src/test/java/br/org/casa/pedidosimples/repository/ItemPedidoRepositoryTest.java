/**
 *
 */
package br.org.casa.pedidosimples.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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

	@Test
	public void testCountByItemVenda() {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setNome("Item-venda-1");
		itemVenda.setTipo(TipoItemVenda.PRODUTO);
		itemVenda.setValorBase(new BigDecimal("1.23"));
		itemVenda.setAtivo(true);

		itemVenda = entityManager.persist(itemVenda);

		Pedido pedido = new Pedido();
		pedido.setCodigo("11234556");
		pedido.setFatorDesconto(new BigDecimal(0.00));
		pedido.setSituacao(SituacaoPedido.ABERTO);

		pedido = entityManager.persist(pedido);

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setItemVenda(itemVenda);
		itemPedido.setPedido(pedido);
		itemPedido.calcularValor();

		itemPedido = entityManager.persist(itemPedido);


		assertThat(itemPedidoRepository.countByItemVenda(itemVenda))
			.isEqualTo(1L);

	}
}
