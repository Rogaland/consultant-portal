package no.rogfk.consultant.service;

import lombok.extern.slf4j.Slf4j;
import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.ConsultantState;
import no.rogfk.consultant.model.HostEmployee;
import no.rogfk.consultant.utilities.InviteUrlGenerator;
import no.rogfk.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info(url);
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

    public boolean notifyNewConsultantPassword(Consultant consultant) {
        String notifyConsultantResponse = smsService.sendSms(consultant.getPassword(), consultant.getMobile());

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
