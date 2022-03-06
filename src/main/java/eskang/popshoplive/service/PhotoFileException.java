package eskang.popshoplive.service;

public class PhotoFileException extends RuntimeException {
    public PhotoFileException(String message) {
        super(message);
    }

    public PhotoFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
