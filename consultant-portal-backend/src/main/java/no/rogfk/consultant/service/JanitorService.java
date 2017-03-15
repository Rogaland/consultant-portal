package no.rogfk.consultant.service;

import no.rogfk.consultant.model.Consultant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class JanitorService {

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private EmailNotifyService emailNotifyService;


    @Scheduled(cron = "0 1 * * * ?")
    public void notifyOwnerOfSoonToExpireConsultants() {

        Map<String, List<Consultant>> consultants =  consultantService.getConsultants(null, true);

        consultants.get("CONFIRMED").forEach(consultant -> {
            emailNotifyService.notifyOwnerSoonToExpireConsultant(consultant);
        });

    }
}
