package mymeeting.exceptions;

public class C_UnexpectedException extends RuntimeException {

    public C_UnexpectedException() {
        super();
    }

    public C_UnexpectedException(String message) {
        super(message);
    }

    public C_UnexpectedException(Throwable cause) {
        super(cause);
    }

    public C_UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
