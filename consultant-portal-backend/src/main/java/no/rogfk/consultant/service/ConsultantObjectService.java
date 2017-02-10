package no.rogfk.consultant.service;

import no.rogfk.consultant.exception.MissingMandatoryAttribute;
import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.ConsultantState;
import no.rogfk.consultant.utilities.LdapConstants;
import no.rogfk.ldap.utilities.LdapTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;

@Service
public class ConsultantObjectService {

    @Autowired
    ConfigService configService;

    @Autowired
    PasswordService passwordService;

    public void setupInvitedConsultant(Consultant consultant) {

        if (consultant.getMobile() == null || consultant.getMobile().isEmpty()) {
            throw new MissingMandatoryAttribute("Missing mobile phone.");
        }

        String cn = String.format(configService.getUsernamePrefix(), consultant.getMobile());
        Name dn = LdapNameBuilder.newInstance(
                configService.getConsultantBaseContainer()).add(LdapConstants.CN, cn).build();
        consultant.setCn(cn);
        consultant.setDn(dn);
        consultant.setLastName(consultant.getMobile());
        consultant.setState(ConsultantState.INVITED.name());
        consultant.setInviteTimeStamp(LdapTimestamp.getTimestampString());
        consultant.setLoginDisabled(true);
    }

    public void setupPendingConsultant(Consultant consultant) {

        if (consultant.getFirstName() == null || consultant.getFirstName().isEmpty()) {
            throw new MissingMandatoryAttribute("Missing firstname.");
        }

        if (consultant.getLastName() == null || consultant.getLastName().isEmpty()) {
            throw new MissingMandatoryAttribute("Missing lastname.");
        }

        if (consultant.getOrganization() == null || consultant.getOrganization().isEmpty()) {
            throw new MissingMandatoryAttribute("Missing organisation.");
        }

        consultant.setState(ConsultantState.PENDING.name());
    }

    public void setupConfirmedConsultant(Consultant consultant) {
        consultant.setState(ConsultantState.CONFIRMED.name());
        consultant.setPassword(passwordService.generatePassword());
    }
}
