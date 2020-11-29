/**
 *
 */
package br.org.casa.pedidosimples.controller;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.repository.ItemVendaRepository;

/**
 * Controller para exposição dos serviços REST relacionados a {@link ItemVenda}.
 *
 * @author jrjosecarlos
 *
 */
@RestController
public class ItemVendaController {

	/**
	 * Nome da entidade principal associada a este Controller. Usado no retorno de mensagens de erro
	 */
	private static final String NOME_EXIBICAO_ENTIDADE = "Item de Venda";

	private final ItemVendaRepository repository;

	ItemVendaController(ItemVendaRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/itens-venda")
	ResponseEntity<List<ItemVenda>> listarTodosItensVenda() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/item-venda/{uuid}")
	ResponseEntity<ItemVenda> buscarItemVendaPorId(@PathVariable("uuid") UUID uuid) {
		return ResponseEntity.ok(repository.findById(uuid)
					.orElseThrow(gerarEntidadeNaoEncontradaException(uuid))
				);
	}

	@PostMapping("/item-venda")
	ResponseEntity<ItemVenda> incluirItemVenda(@RequestBody ItemVenda itemVenda) {
		ItemVenda criado = repository.save(itemVenda);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{uuid}")
					.buildAndExpand(criado.getId())
					.toUri()
				)
				.body(criado);
	}

	@PutMapping("/item-venda/{uuid}")
	ResponseEntity<ItemVenda> atualizarItemVenda(@RequestBody ItemVenda itemVenda, @PathVariable("uuid") UUID uuid) {
		ItemVenda atualizado = repository.findById(uuid)
				.map(iv -> {
					itemVenda.setId(uuid);
					return repository.save(itemVenda);
				}).orElseThrow(gerarEntidadeNaoEncontradaException(uuid));

		return ResponseEntity.ok(atualizado);
	}

	@DeleteMapping("/item-venda/{uuid}")
	ResponseEntity<?> excluirItemVenda(@PathVariable("uuid") UUID uuid) {
		ItemVenda existente = repository.findById(uuid)
			.orElseThrow(gerarEntidadeNaoEncontradaException(uuid));

		repository.delete(existente);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Método de facilidade para criar {@link EntidadeNaoEncontradaException} relacionados à {@link ItemVenda};
	 *
	 * @param uuid uuid a ser exibido na exceção
	 * @return um {@link Supplier} que cria exceções com o nome da entidade fixo e o uuid informado.
	 * @see EntidadeNaoEncontradaException#EntidadeNaoEncontradaException(String, UUID)
	 */
	private Supplier<EntidadeNaoEncontradaException> gerarEntidadeNaoEncontradaException(UUID uuid) {
		return () -> new EntidadeNaoEncontradaException(NOME_EXIBICAO_ENTIDADE, uuid);
	}
}
