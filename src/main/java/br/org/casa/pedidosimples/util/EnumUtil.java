/**
 *
 */
package br.org.casa.pedidosimples.util;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Implementa facilidades para a conversão de valores normais em elementos de Enums.
 *
 * @author jrjosecarlos
 *
 */
public final class EnumUtil {
	/**
	 * Converte um valor da classe {@code V} para um elemento do Enum {@code E}, utilizando o método
	 * {@code converter} para buscar possíveis valores de {@code V} associados aos elementos de {@code E}.
	 *
	 * Caso o enum E não possua nenhum valor V associado a seus elementos será lançada uma exceção.
	 *
	 * @param <E> Classe do Enum que será retornado
	 * @param <V> Classe de valor associado aos elementos do Enum
	 * @param enumClass referência à classe do Enum que está envolvido na conversão.
	 * @param value o valor a ser buscado nos elementos do Enum.
	 * @param converter a {@link Function} de conversão dos elementos do Enum aos valores associados a eles.
	 * @return o elemento do Enum cujo valor associado é {@code value}.
	 * @throws IllegalArgumentException caso não haja nenhum elemento do Enum com valor associado igual a {@code value}.
	 * @throws NullPointerException se qualquer um dos parâmetros for {@code null}.
	 */
	public static <E extends Enum<E>, V> E enumFromValue(Class<E> enumClass, V value, Function<E, V> converter)
			throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(enumClass, "A referência à classe de enum não pode ser null");
		Objects.requireNonNull(value, "O valor a ser convertido não pode ser null");
		Objects.requireNonNull(converter, "A função de conversão não pode ser null");

		return enumFromValueOptional(enumClass, value, converter)
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("%s não é um valor válido para o Enum %s.", value.toString(), enumClass.getName()) )
				);
	}

	/**
	 * Verifica se um valor da classe {@code V} está associado a um elemento do Enum {@code E}, utilizando o método
	 * {@code converter} para buscar possíveis valores.
	 *
	 * @param <E> Classe do Enum que será retornado
	 * @param <V> Classe de valor associado aos elementos do Enum
	 * @param enumClass referência à classe do Enum que está envolvido na conversão.
	 * @param value o valor a ser buscado nos elementos do Enum.
	 * @param converter a {@link Function} de conversão dos elementos do Enum aos valores associados a eles.
	 * @return {@code true} se {@code value} estiver associado a um elemento do enum, {@code false} caso contrário
	 */
	public static <E extends Enum<E>, V> boolean isEnumFromValue(Class<E> enumClass, V value, Function<E, V> converter) {
		return enumFromValueOptional(enumClass, value, converter).isPresent();
	}

	private static <V, E extends Enum<E>> Optional<E> enumFromValueOptional(Class<E> enumClass, V value,
			Function<E, V> converter) {
		return EnumSet.allOf(enumClass).stream()
				.filter(e -> converter.apply(e).equals(value))
				.findAny();
	}



}
