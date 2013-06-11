/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import com.google.gson.annotations.Expose;

/**
 *
 * @author marber
 */
public class GenericResponse {
  @Expose
  private boolean serviceSuccessed = true;
  @Expose
  private int errorCode = 0;
  @Expose
  private String description;

  public GenericResponse(){
    
  }

  /**
   * @return the serviceSuccessed
   */
  public boolean isServiceSuccessed() {
    return serviceSuccessed;
  }

  /**
   * @param serviceSuccessed the serviceSuccessed to set
   */
  public void setServiceSuccessed(boolean serviceSuccessed) {
    this.serviceSuccessed = serviceSuccessed;
  }

  /**
   * @return the errorCode
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * @param errorCode the errorCode to set
   */
  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  
  
  
}
