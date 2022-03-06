package eskang.popshoplive.service;

public class PhotoFileNotFoundException extends RuntimeException {
    public PhotoFileNotFoundException(String message) {
        super(message);
    }

    public PhotoFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
