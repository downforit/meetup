package mymeeting.exceptions;

//import org.apache.log4j.Logger;


public class C_DatabaseException extends C_RuntimeException {
//    private static Logger logger = Logger.getRootLogger();

    public C_DatabaseException(String message, Throwable cause) {
        super(message, cause);
//        logger.error(message,cause);
    }

    public C_DatabaseException(String message) {
        super(message);
//        logger.error(message);
    }

    public C_DatabaseException(Throwable ex) {
        super(ex);
    }

}
