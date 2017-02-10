package no.rogfk.consultant.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PasswordService {

    String generatePassword() {
        String alpha = RandomStringUtils.randomAlphabetic(4);
        String alphaLowerCase = RandomStringUtils.randomAlphabetic(1).toLowerCase();
        String alphaUpperCase = RandomStringUtils.randomAlphabetic(1).toUpperCase();
        String numeric = RandomStringUtils.random(2, false, true);
        String password = alphaLowerCase + alpha + alphaUpperCase + numeric;
        log.info(password);

        return password;
    }

}