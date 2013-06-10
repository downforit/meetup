package mymeeting.hibernate.pojo;
// Generated 7-giu-2013 0.23.19 by Hibernate Tools 3.2.1.GA


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
     private Boolean flgNeedSync;
     private Date lastUpdate;
     private Boolean flgShowed;
     private Boolean flgDeleted;

    public EventDett() {
    }

	
    public EventDett(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public EventDett(Event event, Account account, String evndDescription, String evndImageUrl, Date evndCreationDate, Boolean flgNeedSync, Date lastUpdate, Boolean flgShowed, Boolean flgDeleted) {
       this.event = event;
       this.account = account;
       this.evndDescription = evndDescription;
       this.evndImageUrl = evndImageUrl;
       this.evndCreationDate = evndCreationDate;
       this.flgNeedSync = flgNeedSync;
       this.lastUpdate = lastUpdate;
       this.flgShowed = flgShowed;
       this.flgDeleted = flgDeleted;
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
    public Boolean getFlgNeedSync() {
        return this.flgNeedSync;
    }
    
    public void setFlgNeedSync(Boolean flgNeedSync) {
        this.flgNeedSync = flgNeedSync;
    }
    public Date getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public Boolean getFlgShowed() {
        return this.flgShowed;
    }
    
    public void setFlgShowed(Boolean flgShowed) {
        this.flgShowed = flgShowed;
    }
    public Boolean getFlgDeleted() {
        return this.flgDeleted;
    }
    
    public void setFlgDeleted(Boolean flgDeleted) {
        this.flgDeleted = flgDeleted;
    }




}

