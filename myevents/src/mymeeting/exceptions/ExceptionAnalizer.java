/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mymeeting.exceptions;

import java.io.EOFException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.SocketException;

/**
 *
 * @author root
 */
public class ExceptionAnalizer {


  //***********************************************************
  public static boolean isEOFException(Throwable ex){
    Throwable t1 = ex,t2 = ex;
    do{
        t1 = t2;
        if( t1 instanceof EOFException ){
          return true;
        }
        t2 = t1.getCause();
    }while(t1!=null && t2!=null && t1 != t2);
    return false;
  }

  //***********************************************************
  public static boolean isC_RuntimeException(Throwable ex){
    Throwable t1 = ex,t2 = ex;
    do{
        t1 = t2;
        if( t1 instanceof C_RuntimeException ){
          return true;
        }
        t2 = t1.getCause();
    }while(t1!=null && t2!=null && t1 != t2);
    return false;
  }


  //***********************************************************
  public static boolean isInsufficientDiskSpace(Throwable ex){
    Throwable t1 = ex,t2 = ex;
    String errMess;
    String itMsg = "spazio su disco insufficiente";
    String enMsg= "not enough space";
    do{
        t1 = t2;

        if( t1 instanceof IOException) {
            errMess = t1.getMessage();
            if (errMess != null) {
                errMess = errMess.toLowerCase();
                if (errMess.indexOf(itMsg) != -1 || errMess.indexOf(enMsg) != -1) {
                    return true;
                }
            }
        }
        t2 = t1.getCause();
    }while(t1!=null && t2!=null && t1 != t2);
    return false;
  }

  //***********************************************************
  public static boolean isTemporaryException(Throwable ex){
    boolean result = false;

    if(isNfsStaleException(ex) || isRetryTransactionException(ex)){
      result = true;
    }

    return result;
  }


  //***********************************************************
  public static boolean isNfsStaleException(Throwable ex){
    Throwable t1 = ex,t2 = ex;
    String errMess;
    String word1 = "nfs";
    String word2= "stale";
    do{
        t1 = t2;

        errMess = t1.getMessage();
        if (errMess != null) {
            errMess = errMess.toLowerCase();
            if (errMess.indexOf(word1) >= 0 && errMess.indexOf(word2) >= 0) {
                return true;
            }
        }

        t2 = t1.getCause();
    }while(t1!=null && t2!=null && t1 != t2);
    return false;
  }

  //***********************************************************
  public static boolean isRetryTransactionException(Throwable ex){
    Throwable t1 = ex,t2 = ex;
    String errMess;
    String word1 = "try";
    String word2= "restarting";
    String word3= "transaction";
    do{
        t1 = t2;

        errMess = t1.getMessage();
        if (errMess != null) {
            errMess = errMess.toLowerCase();
            if (errMess.indexOf(word1) >= 0 && errMess.indexOf(word2) >= 0 && errMess.indexOf(word3) >= 0) {
                return true;
            }
        }

        t2 = t1.getCause();
    }while(t1!=null && t2!=null && t1 != t2);
    return false;
  }

 //***********************************************************
 public static boolean isFileNotFoundException(Throwable ex){
   Throwable t1 = ex,t2 = ex;
   do{
       t1 = t2;
       if( t1 instanceof FileNotFoundException ){
         return true;
       }
       t2 = t1.getCause();
   }while(t1!=null && t2!=null && t1 != t2);
   return false;
 }

 //***********************************************************
 public static boolean isOutOfMemoryError(Throwable ex){
   Throwable t1 = ex,t2 = ex;
   do{
       t1 = t2;
       if( t1 instanceof OutOfMemoryError || (t1.getMessage() != null && t1.getMessage().toLowerCase().indexOf("insufficient memory") >= 0  ) ){
         return true;
       }
       t2 = t1.getCause();
   }while(t1!=null && t2!=null && t1 != t2);
   return false;
 }


 //***********************************************************
 public static boolean isFileImageCorruptionException(Throwable ex){
   Throwable t1 = ex,t2 = ex;
   do{
       t1 = t2;
       if( (t1.getMessage() != null && t1.getMessage().toLowerCase().indexOf("incorrect format") >= 0  ) ){
         return true;
       }
       t2 = t1.getCause();
   }while(t1!=null && t2!=null && t1 != t2);
   return false;
 }

  //***********************************************************************/
  public static boolean isNetworkException(Throwable ex){
      Throwable t1 = ex,t2 = ex;
      do{
          t1 = t2;
          if( t1 instanceof SocketException){
            return true;
          }
          t2 = t1.getCause();
      }while(t1!=null && t2!=null && t1 != t2);
      return false;
  }

 //***********************************************************
 public static boolean isC_RuntimeExceptionForEndUser(Throwable ex){
   Throwable t1 = ex,t2 = ex;
   do{
       t1 = t2;
       if( t1 instanceof C_RuntimeExceptionForEndUser ){
         return true;
       }
       t2 = t1.getCause();
   }while(t1!=null && t2!=null && t1 != t2);
   return false;
 }




}//FINE CLASSE
