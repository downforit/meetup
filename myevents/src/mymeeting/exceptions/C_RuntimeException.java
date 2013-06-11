package mymeeting.exceptions;                                   
                                                                
public class C_RuntimeException extends RuntimeException {      
                                                                
    public C_RuntimeException() {                               
        super();                                                
    }                                                           
                                                                
    public C_RuntimeException(String message) {                 
        super(message);                                         
    }                                                           
                                                                
    public C_RuntimeException(Throwable cause) {                
        super(cause);                                           
    }                                                           
                                                                
    public C_RuntimeException(String message, Throwable cause) {
        super(message, cause);                                  
    }                                                           
}                                                               