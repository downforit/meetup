package exceptions;

public class C_RuntimeException extends RuntimeException {

//    private static Logger logger = Logger.getRootLogger();

    public C_RuntimeException() {
        super();
    }

    public C_RuntimeException(String message) {
        super(message);
   //     logger.error(message);
    }

    public C_RuntimeException(Throwable cause) {
        super(cause);
    //    logger.error(null,cause);
    }

    public C_RuntimeException(String message, Throwable cause) {
        super(message, cause);
     //   logger.error(message,cause);
    }
}
