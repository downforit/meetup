package mymeeting.exceptions;                                           
                                                                        
public class C_NetworkResponseException extends RuntimeException {      
                                                                        
    public C_NetworkResponseException() {                               
        super();                                                        
    }                                                                   
                                                                        
    public C_NetworkResponseException(String message) {                 
        super(message);                                                 
    }                                                                   
                                                                        
    public C_NetworkResponseException(Throwable cause) {                
        super(cause);                                                   
    }                                                                   
                                                                        
    public C_NetworkResponseException(String message, Throwable cause) {
        super(message, cause);                                          
    }                                                                   
}                                                                       