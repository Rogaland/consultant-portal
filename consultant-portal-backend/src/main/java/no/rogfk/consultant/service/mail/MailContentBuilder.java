package no.rogfk.consultant.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;


@Service
public class MailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    public String build(Map<String, String> variables, String template) {
        Context context = new Context();

        variables.forEach((name, value) -> {
            context.setVariable(name, value);
        });

        return templateEngine.process(template, context);
    }
 
}