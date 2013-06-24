package rfile;

public class AbortedByUserException extends RuntimeException{

  public AbortedByUserException (String s) {
    super(s);
  }

  public AbortedByUserException (String s, Exception e) {
    super(s,e);
  }   
}
