/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import com.google.gson.annotations.Expose;
import java.util.List;
import mymeeting.hibernate.pojo.RAcnGrp;

/**
 *
 * @author marber
 */
public class ResponseRAcnGrpsList extends GenericResponse{
  @Expose
  private List<RAcnGrp> rAcnGrps;
  

  public ResponseRAcnGrpsList(){
    
  }

  /**
   * @return the rAcnGrp
   */
  public List<RAcnGrp> getrAcnGrps() {
    return rAcnGrps;
  }

  /**
   * @param rAcnGrp the rAcnGrp to set
   */
  public void setrAcnGrps(List<RAcnGrp> rAcnGrps) {
    this.rAcnGrps = rAcnGrps;
  }
  
}
