package no.rogfk.consultant;


import no.rogfk.consultant.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfiguration {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private ConfigService configService;

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(configService.getLdapHostUrl());
        contextSource.setUserDn(configService.getLdapUser());
        contextSource.setPassword(configService.getLdapPassword());
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

}