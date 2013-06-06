package it.marberpp.myevents.hibernate.pojo;
// Generated 19-mag-2013 15.00.25 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * EventDett generated by hbm2java
 */
public class EventDett  implements java.io.Serializable {


     private Integer evndId;
     private Event event;
     private Account account;
     private String evndDescription;
     private String evndImageUrl;
     private Date evndCreationDate;

    public EventDett() {
    }

    public EventDett(Event event, Account account, String evndDescription, String evndImageUrl, Date evndCreationDate) {
       this.event = event;
       this.account = account;
       this.evndDescription = evndDescription;
       this.evndImageUrl = evndImageUrl;
       this.evndCreationDate = evndCreationDate;
    }
   
    public Integer getEvndId() {
        return this.evndId;
    }
    
    public void setEvndId(Integer evndId) {
        this.evndId = evndId;
    }
    public Event getEvent() {
        return this.event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    public String getEvndDescription() {
        return this.evndDescription;
    }
    
    public void setEvndDescription(String evndDescription) {
        this.evndDescription = evndDescription;
    }
    public String getEvndImageUrl() {
        return this.evndImageUrl;
    }
    
    public void setEvndImageUrl(String evndImageUrl) {
        this.evndImageUrl = evndImageUrl;
    }
    public Date getEvndCreationDate() {
        return this.evndCreationDate;
    }
    
    public void setEvndCreationDate(Date evndCreationDate) {
        this.evndCreationDate = evndCreationDate;
    }




}

