package net.sjovatsen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class MailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;
 
    public String build(String owner, String fullname, String username) {
        Context context = new Context();
        context.setVariable("owner", owner);
        context.setVariable("fullname", fullname);
        context.setVariable("username", username);
        return templateEngine.process("mail", context);
    }
 
}