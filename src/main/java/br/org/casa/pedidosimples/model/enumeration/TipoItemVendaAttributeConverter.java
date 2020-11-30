/**
 *
 */
package br.org.casa.pedidosimples.model.enumeration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Um {@link AttributeConverter} para persistir campos da classe {@link TipoItemVenda}
 * utilizando o valor correspondente ao elemento.
 *
 * @author jrjosecarlos
 *
 */
@Converter(autoApply = true)
public class TipoItemVendaAttributeConverter implements AttributeConverter<TipoItemVenda, String> {

	@Override
	public String convertToDatabaseColumn(TipoItemVenda tipoItemVenda) {
		return tipoItemVenda == null ? null : tipoItemVenda.getValor();
	}

	@Override
	public TipoItemVenda convertToEntityAttribute(String dbData) {
		return dbData == null ? null : TipoItemVenda.fromValor(dbData);
	}

}
