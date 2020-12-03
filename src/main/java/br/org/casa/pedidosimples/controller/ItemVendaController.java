/**
 *
 */
package br.org.casa.pedidosimples.controller;

import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.exception.RequisicaoInvalidaException;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.service.ItemVendaService;

/**
 * Controller para exposição dos serviços REST relacionados a {@link ItemVenda}.
 *
 * @author jrjosecarlos
 *
 */
@RestController
public class ItemVendaController {

	@Autowired
	private ItemVendaService service;

	ItemVendaController() {

	}

	@GetMapping("/itens-venda")
	ResponseEntity<Page<ItemVenda>> listarTodosItensVenda(Pageable pageable, @RequestParam Map<String, String> params) {
		return ResponseEntity.ok(service.buscarTodos(pageable, params));
	}

	@GetMapping("/item-venda/{uuid}")
	ResponseEntity<ItemVenda> buscarItemVendaPorId(@PathVariable("uuid") UUID uuid) {
		return ResponseEntity.ok(service.buscarPorId(uuid)
					.orElseThrow(() -> new EntidadeNaoEncontradaException(ItemVenda.NOME_EXIBICAO_ENTIDADE, uuid))
				);
	}

	@PostMapping("/item-venda")
	ResponseEntity<ItemVenda> incluirItemVenda(@RequestBody @Valid ItemVenda itemVenda) {
		ItemVenda criado = service.incluir(itemVenda);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{uuid}")
					.buildAndExpand(criado.getId())
					.toUri()
				)
				.body(criado);
	}

	@PutMapping("/item-venda/{uuid}")
	ResponseEntity<ItemVenda> atualizarItemVenda(@RequestBody @Valid ItemVenda itemVenda, @PathVariable("uuid") UUID uuid) {
		if (!uuid.equals(itemVenda.getId())) {
			throw new RequisicaoInvalidaException("O uuid do %s na url é diferente do no payload", ItemVenda.NOME_EXIBICAO_ENTIDADE);
		}

		return ResponseEntity.ok(service.alterar(uuid, itemVenda));
	}

	@DeleteMapping("/item-venda/{uuid}")
	ResponseEntity<?> excluirItemVenda(@PathVariable("uuid") UUID uuid) {
		service.excluir(uuid);
		return ResponseEntity.noContent().build();
	}

}
