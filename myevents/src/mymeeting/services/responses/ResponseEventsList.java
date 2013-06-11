/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import com.google.gson.annotations.Expose;
import java.util.List;
import mymeeting.hibernate.pojo.Event;

/**
 *
 * @author marber
 */
public class ResponseEventsList extends GenericResponse{
  @Expose
  private List<Event> events;
  

  public ResponseEventsList(){
    
  }

  /**
   * @return the events
   */
  public List<Event> getEvents() {
    return events;
  }

  /**
   * @param events the events to set
   */
  public void setEvents(List<Event> events) {
    this.events = events;
  }
  
}
