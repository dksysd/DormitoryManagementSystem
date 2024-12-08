package server.exception;

public class IllegalHeaderElementException extends RuntimeException {
    public IllegalHeaderElementException(String message) {
        super(message);
    }
}
