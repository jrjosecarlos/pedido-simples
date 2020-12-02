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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.casa.pedidosimples.exception.EntidadeNaoEncontradaException;
import br.org.casa.pedidosimples.model.ItemVenda;
import br.org.casa.pedidosimples.model.Pedido;
import br.org.casa.pedidosimples.service.PedidoService;

/**
 * Controller para exposição dos serviços REST relacionados a {@link ItemVenda}.
 *
 * @author jrjosecarlos
 *
 */
@RestController
public class PedidoController {

	private final PedidoService service;

	PedidoController(PedidoService service) {
		this.service = service;
	}

	@GetMapping("/pedidos")
	ResponseEntity<Page<Pedido>> listarTodosPedidos(Pageable pageable, @RequestParam Map<String, String> params) {
		return ResponseEntity.ok(service.buscarTodos(pageable, params));
	}

	@GetMapping("/pedido/{uuid}")
	ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable("uuid") UUID uuid) {
		return ResponseEntity.ok(service.buscarPorId(uuid)
					.orElseThrow(() -> new EntidadeNaoEncontradaException(Pedido.NOME_EXIBICAO_ENTIDADE, uuid))
				);
	}

	@PostMapping("/pedido")
	ResponseEntity<Pedido> incluirPedido(@RequestBody @Valid Pedido pedido) {
		Pedido criado = service.incluir(pedido);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{uuid}")
				.buildAndExpand(criado.getId())
				.toUri()
			)
			.body(criado);
	}

	@PutMapping("/pedido/{uuid}")
	ResponseEntity<Pedido> alterarPedido(@RequestBody @Valid Pedido pedido, @PathVariable("uuid") UUID uuid) {
		return ResponseEntity.ok(service.alterar(uuid, pedido));
	}

	@PutMapping("/pedido/{uuid}/aplicar-desconto")
	ResponseEntity<Pedido> aplicarDesconto(@RequestBody @Valid FatorDescontoDTO fatorDesconto, @PathVariable("uuid") UUID uuid) {
		return ResponseEntity.ok(service.aplicarDesconto(uuid, fatorDesconto.getValor()));
	}

	@PostMapping("/pedido/{uuid}/fechar")
	ResponseEntity<Pedido> fecharPedido(@PathVariable("uuid") UUID uuid) {
		return ResponseEntity.ok(service.fechar(uuid));
	}

	@DeleteMapping("/pedido/{uuid}")
	ResponseEntity<?> excluirPedido(@PathVariable("uuid") UUID uuid) {
		service.excluir(uuid);
		return ResponseEntity.noContent().build();
	}
}
