package net.fluance.cockpit.core.util.serializer;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import net.fluance.cockpit.core.util.DateUtils;

/**
 * This class serializes a {@link LocalDateTime} to String, because JacksonParser v2.8.8 works with JodaTime
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
	
    @Override
    public void serialize(LocalDateTime time, JsonGenerator jsonGenerator, SerializerProvider serialiserProvider) throws IOException {
    	if(time != null) {
    		jsonGenerator.writeString(time.format(DateUtils.DATE_TIME_FORMATTER));
    	}
    }
}
