package no.rogfk.consultant.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@Slf4j
public class RegisterController {

    @Autowired
    ConsultantService consultantService;

    @GetMapping("/register")
    public String register(@JwtParam(name = "t") ConsultantToken consultantToken, Model model) {
        log.info("ConsultantId: {}", consultantToken.getId());
        Optional<Consultant> consultant = consultantService.getConsultant(consultantToken.getId());
        if (consultant.isPresent()) {
            String message = "";
            if (!consultant.get().getState().equals(ConsultantState.INVITED.name())) {
                message = "Du er allerede registert.";
            } else {
                consultant.get().setLastName(null); // Remove mobile which is used as last name in registration
                // because LDAP requires lastname
            }
            model.addAttribute("message", message);
            model.addAttribute("invitername", consultantToken.getInviterName());
            model.addAttribute("consultant", consultant.get());
        }
        return "register";
    }

    @PostMapping("/register")
    public String updateConsultant(@JwtParam(name = "t") ConsultantToken consultantToken,
                                   @ModelAttribute Consultant consultant,
                                   Model model) {
        log.info("Consultant: {}", consultant);
        String message;
        if (consultantService.stageConsultant(consultant)) {
            message = "Takk for at du registrerte deg.";
        } else {
            message = "Vi klarte ikke registrere deg, vennligst ta kontakt med " + consultantToken.getInviterName();
        }
        model.addAttribute("message", message);
        model.addAttribute("invitername", consultantToken.getInviterName());
        model.addAttribute("consultant", new Consultant());

        return "register";
    }

    @ExceptionHandler(ConsultantAlreadyRegistered.class)
    public ResponseEntity handleAlreadyRegistered(Exception e) {
        return ResponseEntity.status(HttpStatus.GONE).body(new ErrorResponse(e.getMessage()));
    }
}
