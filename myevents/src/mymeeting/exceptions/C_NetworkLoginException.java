package mymeeting.exceptions;

public class C_NetworkLoginException extends RuntimeException {

    public C_NetworkLoginException() {
        super();
    }

    public C_NetworkLoginException(String message) {
        super(message);
    }

    public C_NetworkLoginException(Throwable cause) {
        super(cause);
    }

    public C_NetworkLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
