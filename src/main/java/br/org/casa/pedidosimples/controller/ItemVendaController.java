/**
 *
 */
package br.org.casa.pedidosimples.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.repository.ItemVendaRepository;

/**
 *
 * @author jrjosecarlos
 *
 */
@RestController
public class ItemVendaController {

	private final ItemVendaRepository repository;

	ItemVendaController(ItemVendaRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/itens-venda")
	List<ItemVenda> listarTodosItensVenda() {
		return repository.findAll();
	}

	@GetMapping("/item-venda/{uuid}")
	Optional<ItemVenda> buscarItemVendaPorId(@PathVariable("uuid") UUID uuid) {
		return repository.findById(uuid);
	}

	@PostMapping("/item-venda")
	ItemVenda incluirItemVenda(@RequestBody ItemVenda itemVenda) {
		return repository.save(itemVenda);
	}

	@PutMapping("/item-venda")
	ItemVenda atualizarItemVenda(@RequestBody ItemVenda itemVenda) {
		return repository.save(itemVenda);
	}

	@DeleteMapping("/item-venda/{uuid}")
	void excluirItemVenda(@PathVariable("uuid") UUID uuid) {
		repository.deleteById(uuid);
	}
}
