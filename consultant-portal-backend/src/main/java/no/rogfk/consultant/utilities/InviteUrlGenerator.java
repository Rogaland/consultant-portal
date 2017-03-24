package no.rogfk.consultant.utilities;

import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.ConsultantToken;
import no.rogfk.consultant.service.ConfigService;
import no.rogfk.jwt.SpringJwtTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InviteUrlGenerator {

    @Autowired
    private ConfigService configService;

    @Autowired
    private SpringJwtTokenizer springJwtTokenizer;

    public String get(Consultant consultant) {
        ConsultantToken consultantToken = new ConsultantToken();
        consultantToken.setId(consultant.getCn());
        consultantToken.setInviterName(consultantToken.getInviterName());

        return springJwtTokenizer.createWithUrl(configService.getBaseUrl(), "t", consultantToken);
    }

}