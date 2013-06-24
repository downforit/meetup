package rfile;

public class RecursiveFileException extends RuntimeException {
//
//    private static Logger logger = Logger.getLogger("ANDREAWEB");

    public RecursiveFileException() {
        super();
    }

    public RecursiveFileException(String message) {
        super(message);
//        logger.error(message);
    }

    public RecursiveFileException(Throwable cause) {
        super(cause);
//        logger.error(null,cause);
    }

    public RecursiveFileException(String message, Throwable cause) {
        super(message, cause);
//        logger.error(message,cause);
    }
}