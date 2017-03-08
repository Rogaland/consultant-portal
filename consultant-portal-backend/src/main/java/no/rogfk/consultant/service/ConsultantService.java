package no.rogfk.consultant.service;

import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.ConsultantState;
import no.rogfk.consultant.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.SearchControls;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultantService {

    @Autowired
    ConsultantObjectService consultantObjectService;

    @Autowired
    ConfigService configService;

    @Autowired
    LdapTemplate ldapTemplate;

    @Autowired
    private SmsNotifySerivce smsNotifySerivce;

    private SearchControls searchControls;

    public ConsultantService() {
        searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    }

    public boolean inviteConsultant(Consultant consultant) {

        consultantObjectService.setupInvitedConsultant(consultant);

        if (!exists(consultant.getDn())) {
            ldapTemplate.create(consultant);
            smsNotifySerivce.sendInvite(consultant);

            return true;
        }

        return false;
    }

    public boolean exists(String dn) {
        try {
            ldapTemplate.lookup(LdapNameBuilder.newInstance(dn).build());
            return true;
        } catch (org.springframework.ldap.NamingException e) {
            return false;
        }
    }

    public boolean updateConsultant(Consultant consultant) {

        if (exists(consultant.getDn())) {
            if (ConsultantState.valueOf(consultant.getState()) == ConsultantState.INVITED) {
                consultantObjectService.setupPendingConsultant(consultant);
                ldapTemplate.update(consultant);
                return true;
            }

            if (ConsultantState.valueOf(consultant.getState()) == ConsultantState.PENDING) {
                consultantObjectService.setupConfirmedConsultant(consultant);
                ldapTemplate.update(consultant);
                smsNotifySerivce.notifyConfirmedConsultant(consultant);
                return true;
            }
            if (ConsultantState.valueOf(consultant.getState()) == ConsultantState.CONFIRMED) {
                ldapTemplate.update(consultant);
                return true;
            }
        }

        return false;
    }

    public Optional<Consultant> deleteConsultant(String cn) {
        Optional<Consultant> consultant = getConsultant(cn);

        if (consultant.isPresent()) {
            ldapTemplate.delete(consultant.get());
            smsNotifySerivce.notifyDeletedConsultant(consultant.get());

            return consultant;

        }
        return Optional.empty();
    }

    public Optional<Consultant> getConsultant(String id) {
        Name dn = LdapNameBuilder.newInstance(configService.getConsultantBaseContainer()).add(LdapConstants.CN, id).build();
        return Optional.ofNullable(ldapTemplate.findByDn(dn, Consultant.class));
    }

    public Map<String, List<Consultant>> getConsultants(String ownerDn, boolean all) {

        List<Consultant> consultants = ldapTemplate.findAll(
                LdapNameBuilder.newInstance(configService.getConsultantBaseContainer()).build(),
                searchControls, Consultant.class
        );

        if (all) {
            return consultants.stream().collect(Collectors.groupingBy(s -> s.getState()));
        } else {
            return consultants.stream()
                    .filter(c -> c.getOwner() != null && (c.getOwner().toLowerCase().equals(ownerDn.toLowerCase())))
                    .collect(Collectors.groupingBy(s -> s.getState()));
        }
    }
}
