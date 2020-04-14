package spinnery.util;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonPrimitive;

import java.util.Optional;

public class JanksonUtilities {
	/**
	 * Retrieves a JsonElement as a Number.
	 *
	 * @param element JsonElement to be retrieved.
	 * @return Optional of Number retrieved, containing the Number if the operation was successful.
	 */
	public static Optional<Number> asNumber(JsonElement element) {
		if (!(element instanceof JsonPrimitive)) return Optional.empty();
		if (!(((JsonPrimitive) element).getValue() instanceof Number)) return Optional.empty();
		return Optional.of((Number) ((JsonPrimitive) element).getValue());
	}

	/**
	 * Retrieves a JsonElement as a Boolean.
	 *
	 * @param element JsonElement to be retrieved.
	 * @return Optional of Boolean retrieved, containing the Boolean if the operation was successful.
	 */
	public static Optional<Boolean> asBoolean(JsonElement element) {
		if (!(element instanceof JsonPrimitive)) return Optional.empty();
		if (!(((JsonPrimitive) element).getValue() instanceof Boolean)) return Optional.empty();
		return Optional.of((Boolean) ((JsonPrimitive) element).getValue());
	}

	/**
	 * Retrieves a JsonElement as a String.
	 *
	 * @param element JsonElement to be retrieved.
	 * @return Optional of String retrieved, containing the String if the operation was successful.
	 */
	public static Optional<String> asString(JsonElement element) {
		if (!(element instanceof JsonPrimitive)) return Optional.empty();
		if (!(((JsonPrimitive) element).getValue() instanceof String)) return Optional.empty();
		return Optional.of((String) ((JsonPrimitive) element).getValue());
	}

	/**
	 * Builds JsonArray from the given values.
	 *
	 * @param values Values to be used for the JsonArray.
	 * @return JsonArray of the given values.
	 */
	public static JsonArray arrayOfPrimitives(Object... values) {
		JsonArray elementArray = new JsonArray();
		for (Object value : values) {
			elementArray.add(new JsonPrimitive(value));
		}
		return elementArray;
	}
}
