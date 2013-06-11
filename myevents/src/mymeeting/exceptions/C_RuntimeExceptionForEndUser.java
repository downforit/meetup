package mymeeting.exceptions;

public class C_RuntimeExceptionForEndUser extends RuntimeException {

    public C_RuntimeExceptionForEndUser() {
        super();
    }

    public C_RuntimeExceptionForEndUser(String message) {
        super(message);
    }

    public C_RuntimeExceptionForEndUser(Throwable cause) {
        super(cause);
    }

    public C_RuntimeExceptionForEndUser(String message, Throwable cause) {
        super(message, cause);
    }
}
