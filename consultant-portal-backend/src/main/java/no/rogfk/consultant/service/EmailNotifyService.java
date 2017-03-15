package no.rogfk.consultant.service;

import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.HostEmployee;
import no.rogfk.consultant.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailNotifyService {

    private static final String NOREPLY = "no-reply@rogfk.no";


    @Autowired
    private MailService mailService;

    @Autowired
    private HostEmployeeService hostEmployeeService;

    public void notifyOwnerSoonToExpireConsultant(Consultant consultant) {

        Map variables = new HashMap();
        HostEmployee hostEmployee = hostEmployeeService.getHostEmployee(consultant.getOwner());
        String consultantFullname = String.format("%s %s", consultant.getFirstName(), consultant.getLastName());

        variables.put("consultantFullname", consultantFullname);
        variables.put("ownerFullname", hostEmployee.getFullname());

        mailService.prepareAndSend(variables,
                "consultantexpires",
                hostEmployee.getMail(), NOREPLY,
                String.format("Konsulentbrukeren til %s uløper snart!", consultantFullname));

    }

    public void notifyNewConsultant(Consultant consultant) {
        Map variables = new HashMap();
        HostEmployee hostEmployee = hostEmployeeService.getHostEmployee(consultant.getOwner());

        variables.put("consultantFullname",
                String.format("%s %s", consultant.getFirstName(), consultant.getLastName())
        );
        variables.put("consultantUsername", consultant.getCn());
        variables.put("ownerFullname", hostEmployee.getFullname());
        variables.put("ownerEmail", hostEmployee.getMail());
        variables.put("ownerMobile", hostEmployee.getMobile());

        mailService.prepareAndSend(variables, "newconsultant",
                consultant.getMail(), hostEmployee.getMail(),
                "Velkommen / Welcome");

    }
}
