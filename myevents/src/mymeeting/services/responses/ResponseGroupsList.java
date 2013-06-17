/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import java.util.List;

import mymeeting.hibernate.pojo.Group;

import com.google.gson.annotations.Expose;

/**
 *
 * @author marber
 */
public class ResponseGroupsList extends GenericResponse{
  @Expose
  private List<Group> groups;
  

  public ResponseGroupsList(){
    
  }

  /**
   * @return the groups
   */
  public List<Group> getGroups() {
    return groups;
  }

  /**
   * @param groups the groups to set
   */
  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  
}
