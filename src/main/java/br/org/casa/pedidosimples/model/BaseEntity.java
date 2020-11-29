/**
 *
 */
package br.org.casa.pedidosimples.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

/**
 * Estrutura base para as entidades.
 *
 * @author jrjosecarlos
 *
 */
@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(updatable = false, nullable = false)
	@NotNull
	private UUID id;

	/**
	 * Retorna o valor atual do campo id.
	 *
	 * @return valor de id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Define um novo valor para o campo id
	 *
	 * @param id o novo valor de id
	 */
	public void setId(UUID id) {
		this.id = id;
	}

}
