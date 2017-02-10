package no.rogfk.consultant.exception;

public class ConsultantAlreadyRegistered extends RuntimeException {
    public ConsultantAlreadyRegistered(String message) {
        super(message);
    }
}
