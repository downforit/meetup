/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import mymeeting.hibernate.AbstractFacadeHandle;
import mymeeting.hibernate.facade.EventFacade;
import mymeeting.hibernate.pojo.Event;

/**
 *
 * @author marber
 */
@ManagedBean
@SessionScoped
public class EventController {

    private int numPages=0;
    DataModel events=null;
    Event currentEvent = null;
    /**
     * Creates a new instance of EventController
     */
    public EventController() {
    }

    public DataModel getEvents() {
        //if (events == null) {
      EventFacade eventFacade = new EventFacade();
      eventFacade.setHandle(AbstractFacadeHandle.createNewHandle(false));

      events = new ListDataModel(eventFacade.findAll());
        //}
        return events;
    }


   public boolean isHasNextPage() {
//        if (endId + pageSize <= recordCount) {
//            return true;
//        }
        return true;
    }

    public boolean isHasPreviousPage() {
//        if (startId-pageSize > 0) {
//            return true;
//        }
        return true;
    }

    public String next() {
//        startId = endId+1;
//        endId = endId + pageSize;
//        recreateModel();
        return "index";
    }

    public String previous() {
//        startId = startId - pageSize;
//        endId = endId - pageSize;
//        recreateModel();
        return "index";
    }

    public int getPageSize() {
        return this.numPages;
    }
    
    public String prepareEventView(){
        this.currentEvent = (Event) getEvents().getRowData();
        return "show_event_detail";
    }

    public String prepareListEventView(){
        this.currentEvent = (Event) getEvents().getRowData();
        return "index";
    }
    
    public Event getCurrentEvent(){
        return this.currentEvent;
    }
}
