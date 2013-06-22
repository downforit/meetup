package mymeeting.hibernate.exceptions;


public class C_HibernateConstraintException extends RuntimeException {
//    private static Logger logger = Logger.getRootLogger();

    public C_HibernateConstraintException(String message, Throwable cause) {
        super(message, cause);
//        logger.error(message,cause);
    }

    public C_HibernateConstraintException(String message) {
        super(message);
//        logger.error(message);
    }

    public C_HibernateConstraintException(Throwable ex) {
        super(ex);
    }

}
