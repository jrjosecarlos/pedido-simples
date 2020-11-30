/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import br.org.casa.pedidosimples.model.Pedido;

/**
 * Definição de repositório para {@link Pedido}.
 *
 * @author jrjosecarlos
 *
 */
public interface PedidoRepository extends JpaRepository<Pedido, UUID>, QuerydslPredicateExecutor<Pedido> {

}
