package no.rogfk.consultant.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;

@Data
@Service
public class ConfigService {

    @Value("${rfk.ldap.url}")
    private String ldapHostUrl;

    @Value("${rfk.ldap.user}")
    private String ldapUser;

    @Value("${rfk.ldap.password}")
    private String ldapPassword;

    @Value("${rfk.consultant.base-container}")
    private String consultantBaseContainer;

    @Value("${rfk.consultant.username-prefix}")
    private String usernamePrefix;

    @Value("${rfk.message.sms.invite-consultant}")
    private String consultantInviteMessage;

    @Value("${rfk.message.sms.confirm-consultant}")
    private String consultantConfirmMessage;

    @Value("${rfk.message.sms.delete-consultant}")
    private String consultantDeleteMessage;

    @Value("${rfk.consultant.base-url}")
    private String baseUrl;

    @Value("${jwt.max-age-minutes}")
    private long jwtMaxAgeMinutes;

    public Name getConsultantBaseContainer() {
        return LdapNameBuilder.newInstance(consultantBaseContainer).build();
    }

}
