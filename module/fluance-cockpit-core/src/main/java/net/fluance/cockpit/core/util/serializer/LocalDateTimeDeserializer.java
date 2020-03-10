package net.fluance.cockpit.core.util.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
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
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	@Override
	public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		String timeAsString = jsonParser.getText();
		LocalDateTime time = null;
		try {
			time = LocalDateTime.parse(timeAsString, DateUtils.DATE_TIME_FORMATTER);
		} catch(DateTimeParseException dateTimeParseException) {
			throw new JsonParseException(jsonParser, "Invalid time format", dateTimeParseException);
		}
		return time;
	}

}
