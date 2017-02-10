package no.rogfk.consultant.exception;

public class MissingMandatoryAttribute extends RuntimeException {
    public MissingMandatoryAttribute(String message) {
        super(message);
    }
}
