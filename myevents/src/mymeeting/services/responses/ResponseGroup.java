/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import mymeeting.hibernate.pojo.Group;

import com.google.gson.annotations.Expose;

/**
 *
 * @author marber
 */
public class ResponseGroup extends GenericResponse{
  @Expose
  private Group group;
  

  public ResponseGroup(){
    
  }

  /**
   * @return the group
   */
  public Group getGroup() {
    return group;
  }

  /**
   * @param group the group to set
   */
  public void setGroup(Group group) {
    this.group = group;
  }


  
}
