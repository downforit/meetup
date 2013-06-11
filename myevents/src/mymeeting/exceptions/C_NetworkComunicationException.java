package mymeeting.exceptions;

public class C_NetworkComunicationException extends RuntimeException {

    public C_NetworkComunicationException() {
        super();
    }

    public C_NetworkComunicationException(String message) {
        super(message);
    }

    public C_NetworkComunicationException(Throwable cause) {
        super(cause);
    }

    public C_NetworkComunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
