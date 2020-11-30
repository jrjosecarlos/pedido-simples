/**
 *
 */
package br.org.casa.pedidosimples.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import br.org.casa.pedidosimples.model.ItemVenda;

/**
 * Definição de repositório para {@link ItemVenda}.
 *
 * @author jrjosecarlos
 *
 */
public interface ItemVendaRepository extends JpaRepository<ItemVenda, UUID>, QuerydslPredicateExecutor<ItemVenda> {

}
