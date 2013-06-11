package mymeeting.exceptions;

public class C_NetworkKeyDuplicateException extends RuntimeException {

    public C_NetworkKeyDuplicateException() {
        super();
    }

    public C_NetworkKeyDuplicateException(String message) {
        super(message);
    }

    public C_NetworkKeyDuplicateException(Throwable cause) {
        super(cause);
    }

    public C_NetworkKeyDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
