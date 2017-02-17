package no.rogfk.consultant.controller;

import lombok.extern.slf4j.Slf4j;
import no.rogfk.consultant.model.ConsultantToken;
import no.rogfk.jwt.SpringJwtTokenizer;
import no.rogfk.sms.strategy.ManualQueueStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(value = "/admin/sms", produces = MediaType.APPLICATION_JSON_VALUE)
public class SmsController {

    @Autowired(required = false)
    private ManualQueueStrategy manualQueueStrategy;

    @Autowired
    private SpringJwtTokenizer springJwtTokenizer;

    @PostConstruct
    public void init() {
        if (manualQueueStrategy == null) {
            log.info("Manual queue strategy not loaded, SmsController is disabled");
        } else {
            log.info("Manual queue strategy found, SmsController is active");
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll(@RequestParam(required = false) String decode) {
        if (manualQueueStrategy == null) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            if (decode == null) {
                return ResponseEntity.ok(manualQueueStrategy.getFormattedValues());
            } else {
                List<String> queue = manualQueueStrategy.getQueue();
                List<String> tokens = queue.stream().map(value -> springJwtTokenizer.parseWithUrl(value, "t", ConsultantToken.class).toString()).collect(Collectors.toList());
                return ResponseEntity.ok(tokens);
            }
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void removeAll() {
        if (manualQueueStrategy != null) {
            manualQueueStrategy.emptyQueue();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public void send() {
        if (manualQueueStrategy != null) {
            manualQueueStrategy.sendQueue();
        }
    }
}