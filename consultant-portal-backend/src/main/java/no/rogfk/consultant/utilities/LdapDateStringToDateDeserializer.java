package no.rogfk.consultant.utilities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LdapDateStringToDateDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = p.getText().trim();

            try {
                DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
                ZonedDateTime zdt = ZonedDateTime.parse(str, f);
                str = zdt.toLocalDateTime().toLocalDate().toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            str = str.replace("-", "") + "000000Z";
            return str;
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return p.getText();
        }
        throw ctxt.mappingException("LdapDateStringToDateDeserializer could not map value: " + p.getText());
    }
}
