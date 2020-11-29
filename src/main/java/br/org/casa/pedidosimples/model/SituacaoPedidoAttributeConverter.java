/**
 *
 */
package br.org.casa.pedidosimples.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Um {@link AttributeConverter} para persistir campos da classe {@link SituacaoPedido}
 * utilizando o valor correspondente ao elemento.
 *
 * @author jrjosecarlos
 *
 */
@Converter(autoApply = true)
public class SituacaoPedidoAttributeConverter implements AttributeConverter<SituacaoPedido, String>{

	@Override
	public String convertToDatabaseColumn(SituacaoPedido situacaoPedido) {
		return situacaoPedido == null ? null : situacaoPedido.getValor();
	}

	@Override
	public SituacaoPedido convertToEntityAttribute(String dbData) {
		return dbData == null ? null : SituacaoPedido.fromValor(dbData);
	}

}
