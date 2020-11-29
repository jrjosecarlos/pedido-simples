/**
 *
 */
package br.org.casa.pedidosimples.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Um {@link AttributeConverter} para persistir campos da classe {@link SituacaoPedido}
 * utilizando o valor da sigla correspondente.
 *
 * @author jrjosecarlos
 *
 */
@Converter(autoApply = true)
public class SituacaoPedidoConverter implements AttributeConverter<SituacaoPedido, String>{

	@Override
	public String convertToDatabaseColumn(SituacaoPedido situacaoPedido) {
		return situacaoPedido == null ? null : situacaoPedido.getSigla();
	}

	@Override
	public SituacaoPedido convertToEntityAttribute(String dbData) {
		return dbData == null ? null : SituacaoPedido.fromSigla(dbData);
	}

}
