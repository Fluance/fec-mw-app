package net.fluance.cockpit.core.util.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import net.fluance.cockpit.core.util.DateUtils;

/**
 * This class deserializes the String content to {@link LocalDateTime} to String, because JacksonParser v2.8.8 works with JodaTime
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

	@Override
	public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		String timeAsString = jsonParser.getText();
		LocalTime time = null;
		try {
			time = LocalTime.parse(timeAsString, DateUtils.TIME_FORMATTER);
		} catch(DateTimeParseException dateTimeParseException) {
			throw new JsonParseException(jsonParser, "Invalid time format", dateTimeParseException);
		}
		return time;
	}

}
