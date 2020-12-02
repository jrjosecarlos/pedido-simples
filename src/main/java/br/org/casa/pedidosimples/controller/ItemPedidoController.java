/**
 *
 */
package br.org.casa.pedidosimples.controller;

import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.model.ItemPedido;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.service.ItemPedidoService;

/**
 * Controller para exposição dos serviços REST relacionados a {@link ItemPedido}.
 *
 * @author jrjosecarlos
 *
 */
@RestController
public class ItemPedidoController {

	private final ItemPedidoService service;

	ItemPedidoController(ItemPedidoService service) {
		this.service = service;
	}

	@GetMapping("/pedido/{uuidPedido}/itens-pedido")
	ResponseEntity<Page<ItemPedido>> listarTodosItensVenda(@PathVariable("uuidPedido") UUID uuidPedido, Pageable pageable, @RequestParam Map<String, String> params) {
		return ResponseEntity.ok(service.buscarTodos(uuidPedido, pageable, params));
	}

	@GetMapping("/item-pedido/{uuid}")
	ResponseEntity<ItemPedido> buscarItemPedidoPorId(@PathVariable("uuid") UUID uuid) {
		return ResponseEntity.ok(service.buscarPorId(uuid)
					.orElseThrow(() -> new EntidadeNaoEncontradaException(ItemPedido.NOME_EXIBICAO_ENTITDADE, uuid))
				);
	}

	@PostMapping("/pedido/{uuidPedido}/item-pedido")
	ResponseEntity<ItemPedido> incluirItemPedido(@PathVariable("uuidPedido") UUID uuidPedido,
			@RequestBody @Valid ItemVenda itemVenda) {
		ItemPedido criado = service.incluir(uuidPedido, itemVenda);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/item-pedido/{uuid}")
					.buildAndExpand(criado.getId())
					.toUri()
				)
				.body(criado);
	}

	@DeleteMapping("/item-pedido/{uuid}")
	ResponseEntity<?> excluirItemPedido(@PathVariable("uuid") UUID uuid) {
		service.excluir(uuid);
		return ResponseEntity.noContent().build();
	}
}
