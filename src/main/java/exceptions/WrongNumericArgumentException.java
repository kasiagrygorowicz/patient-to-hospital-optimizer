package exceptions;

public class WrongNumericArgumentException extends RuntimeException {
    public WrongNumericArgumentException(String message) {
        super(message);
    }
}
