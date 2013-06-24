package images.text;

//import org.apache.log4j.Logger;

public class C_FontNotPresentException extends RuntimeException {

    //private static Logger logger = Logger.getRootLogger();

    public C_FontNotPresentException() {
        super();
    }

    public C_FontNotPresentException(String message) {
        super(message);
        //logger.error(message);
    }

    public C_FontNotPresentException(Throwable cause) {
        super(cause);
        //logger.error(null,cause);
    }

    public C_FontNotPresentException(String message, Throwable cause) {
        super(message, cause);
        //logger.error(message,cause);
    }
}
