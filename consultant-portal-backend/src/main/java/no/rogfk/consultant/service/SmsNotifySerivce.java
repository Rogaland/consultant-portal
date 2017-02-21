package no.rogfk.consultant.service;

import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.ConsultantState;
import no.rogfk.consultant.model.HostEmployee;
import no.rogfk.consultant.utilities.InviteUrlGenerator;
import no.rogfk.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsNotifySerivce {

    @Autowired
    InviteUrlGenerator inviteUrlGenerator;

    @Autowired
    ConfigService configService;

    @Autowired
    HostEmployeeService hostEmployeeService;

    @Autowired
    SmsService smsService;

    public boolean sendInvite(Consultant consultant) {
        String url = inviteUrlGenerator.get(consultant);
        HostEmployee hostEmployee = hostEmployeeService.getHostEmployee(consultant.getOwner());
        String notifyConsultantResponse = smsService.sendSms(
                String.format(configService.getConsultantInviteMessage(),
                        hostEmployee.getFullname(),
                        configService.getJwtMaxAgeMinutes(),
                        url),
                consultant.getMobile()
        );

        if (notifyConsultantResponse.contains(">true<")) {
            return true;
        }
        return false;
    }

    public boolean notifyConfirmedConsultant(Consultant consultant) {
        String notifyConsultantResponse = smsService.sendSms(
                String.format(configService.getConsultantConfirmMessage(),
                        String.format("%s %s", consultant.getFirstName(), consultant.getLastName()),
                        consultant.getCn(),
                        consultant.getPassword()),
                consultant.getMobile()
        );

        if (notifyConsultantResponse.contains(">true<")) {
            return true;
        }
        return false;
    }

    public boolean notifyDeletedConsultant(Consultant consultant) {
        if (!consultant.getState().equals(ConsultantState.INVITED.name())) {
            String notifyConsultantResponse = smsService.sendSms(
                    String.format(configService.getConsultantDeleteMessage(),
                            String.format("%s %s", consultant.getFirstName(), consultant.getLastName())
                    ),
                    consultant.getMobile()
            );

            if (notifyConsultantResponse.contains(">true<")) {
                return true;
            }
        }
        return false;
    }
}
