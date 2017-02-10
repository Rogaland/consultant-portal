package no.rogfk.consultant.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import no.rogfk.jwt.claims.DefaultClaim;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConsultantToken extends DefaultClaim {
    private String id;
    private String inviterName;
}
