package no.rogfk.consultant.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.rogfk.consultant.model.Consultant;
import no.rogfk.consultant.model.ConsultantState;
import no.rogfk.consultant.model.ErrorResponse;
import no.rogfk.consultant.model.SuccessResponse;
import no.rogfk.consultant.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@Api(tags = "Consultants")
@CrossOrigin()
@RequestMapping(value = "/api/consultants")
public class ConsultantController {

    @Autowired
    ConsultantService consultantService;

    @ApiOperation("Update consultant")
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity updateConsultant(@RequestBody Consultant consultant) {
        log.info("Consultant: {}", consultant);

        if (consultantService.updateConsultant(consultant)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(consultant);
        }
        return ResponseEntity.badRequest().build();
    }

    @ApiOperation("Update consultant state")
    @RequestMapping(
            value = "progressstate",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity updateConsultantState(@RequestBody Consultant consultant) {
        log.info("Consultant: {}", consultant);

        if (consultantService.stageConsultant(consultant)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(consultant);
        }
        return ResponseEntity.badRequest().build();
    }

    @ApiOperation("Delete consultant")
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity deleteConsultant(@PathVariable final String id) {
        log.info("Consultant: {}", id);
        Optional<Consultant> consultant = consultantService.deleteConsultant(id);
        String successResponeMessage = "Invitasjonen ble slettet.";
        if (consultant.isPresent()) {
            if (!consultant.get().getState().equals(ConsultantState.INVITED.name())) {
                successResponeMessage = String.format("%s %s ble slettet.",
                        consultant.get().getFirstName(), consultant.get().getLastName()
                );
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    new SuccessResponse(successResponeMessage));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse("Vi kunne ikke finne konsulenten du forsøker å slette.")
        );
    }

    @ApiOperation("Get consultant")
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET)
    public ResponseEntity getConsultant(@PathVariable String id) {
        log.info("Id: {}", id);

        Optional<Consultant> consultant = consultantService.getConsultant(id);
        if (consultant.isPresent()) {
            return ResponseEntity.ok(consultant.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Get consultants")
    @RequestMapping(
            method = RequestMethod.GET)
    public ResponseEntity getConsultants(@RequestHeader("x-owner-dn") String ownerDn, @RequestParam(defaultValue = "false") boolean all) {
        log.info("OwnerDn: {}", ownerDn);

        return ResponseEntity.ok(consultantService.getConsultants(ownerDn, all));
    }
}
