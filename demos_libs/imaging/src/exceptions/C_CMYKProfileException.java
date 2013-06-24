package exceptions;

public class C_CMYKProfileException extends RuntimeException {

//    private static Logger logger = Logger.getRootLogger();

    public C_CMYKProfileException() {
        super();
    }

    public C_CMYKProfileException(String message) {
        super(message);
   //     logger.error(message);
    }

    public C_CMYKProfileException(Throwable cause) {
        super(cause);
    //    logger.error(null,cause);
    }

    public C_CMYKProfileException(String message, Throwable cause) {
        super(message, cause);
     //   logger.error(message,cause);
    }
}
