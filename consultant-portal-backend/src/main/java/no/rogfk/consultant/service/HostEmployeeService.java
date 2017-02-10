package no.rogfk.consultant.service;

import no.rogfk.consultant.model.HostEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class HostEmployeeService {

    @Autowired
    LdapTemplate ldapTemplate;

    public HostEmployee getHostEmployee(String dn) {
        return ldapTemplate.findByDn(LdapNameBuilder.newInstance(dn).build(), HostEmployee.class);
    }
}
