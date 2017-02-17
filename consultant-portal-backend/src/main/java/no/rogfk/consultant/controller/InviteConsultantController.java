package no.rogfk.consultant.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.rogfk.consultant.exception.ConsultantAlreadyRegistered;
import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.ConsultantState;
import no.rogfk.consultant.model.ConsultantToken;
import no.rogfk.consultant.model.ErrorResponse;
import no.rogfk.consultant.service.ConsultantService;
import no.rogfk.jwt.annotations.JwtParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@Api(tags = "Invite Consultants")
@CrossOrigin()
@RequestMapping(value = "/api/invited/consultants")
public class InviteConsultantController {

    @Autowired
    ConsultantService consultantService;

    @ApiOperation("Invite consultant")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity inviteConsultant(@RequestBody Consultant consultant, @RequestHeader("x-owner-dn") String ownerDn) {
        log.info("Consultant: {}", consultant);
        log.info("OwnerDn: {}", ownerDn);

        consultant.setOwner(ownerDn);
        if (consultantService.inviteConsultant(consultant)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(consultant);
        }

        return ResponseEntity.badRequest().build();
    }
}
