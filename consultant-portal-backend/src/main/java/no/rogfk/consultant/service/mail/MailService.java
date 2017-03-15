package no.rogfk.consultant.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;


    public void prepareAndSend(Map<String, String> templateVaribles, String template, String recipient, String from, String subject) {


        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContentBuilder.build(templateVaribles, template), true);
            messageHelper.addInline("logo", new ClassPathResource("static/logo.png"), "image/jpeg");
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            log.info("Error {}", e.getMessage());
        }
    }

}
