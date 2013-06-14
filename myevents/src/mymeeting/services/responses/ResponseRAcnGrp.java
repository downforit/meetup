/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import com.google.gson.annotations.Expose;
import mymeeting.hibernate.pojo.RAcnGrp;

/**
 *
 * @author marber
 */
public class ResponseRAcnGrp extends GenericResponse{
  @Expose
  private RAcnGrp rAcnGrp;
  

  public ResponseRAcnGrp(){
    
  }

  /**
   * @return the rAcnGrp
   */
  public RAcnGrp getrAcnGrp() {
    return rAcnGrp;
  }

  /**
   * @param rAcnGrp the rAcnGrp to set
   */
  public void setrAcnGrp(RAcnGrp rAcnGrp) {
    this.rAcnGrp = rAcnGrp;
  }

  
}
