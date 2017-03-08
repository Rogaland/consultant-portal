package no.rogfk.consultant.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LdapDateStringToDateSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String isoDateString = null;
        Date theDate = null;
        try {
            theDate = sdf.parse(value);
            isoDateString = theDate.toInstant().toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        gen.writeString(isoDateString);
    }
}
