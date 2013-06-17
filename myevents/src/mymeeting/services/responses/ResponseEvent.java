/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import mymeeting.hibernate.pojo.Event;

import com.google.gson.annotations.Expose;

/**
 *
 * @author marber
 */
public class ResponseEvent extends GenericResponse{
  @Expose
  private Event event;
  

  public ResponseEvent(){
    
  }

  /**
   * @return the event
   */
  public Event getEvent() {
    return event;
  }

  /**
   * @param event the event to set
   */
  public void setEvent(Event event) {
    this.event = event;
  }

  
}
