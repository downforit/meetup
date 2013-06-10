package mymeeting.hibernate.pojo;
// Generated 7-giu-2013 0.23.19 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * RAcnEvn generated by hbm2java
 */
public class RAcnEvn  implements java.io.Serializable {


     private RAcnEvnId id;
     private Event event;
     private Account account;
     private Boolean acnevnConfirmed;
     private Date acnevnConfirmationDate;
     private Boolean flgNeedSync;
     private Date lastUpdate;
     private Boolean flgShowed;
     private Boolean flgDeleted;

    public RAcnEvn() {
    }

	
    public RAcnEvn(RAcnEvnId id, Event event, Account account, Date lastUpdate) {
        this.id = id;
        this.event = event;
        this.account = account;
        this.lastUpdate = lastUpdate;
    }
    public RAcnEvn(RAcnEvnId id, Event event, Account account, Boolean acnevnConfirmed, Date acnevnConfirmationDate, Boolean flgNeedSync, Date lastUpdate, Boolean flgShowed, Boolean flgDeleted) {
       this.id = id;
       this.event = event;
       this.account = account;
       this.acnevnConfirmed = acnevnConfirmed;
       this.acnevnConfirmationDate = acnevnConfirmationDate;
       this.flgNeedSync = flgNeedSync;
       this.lastUpdate = lastUpdate;
       this.flgShowed = flgShowed;
       this.flgDeleted = flgDeleted;
    }
   
    public RAcnEvnId getId() {
        return this.id;
    }
    
    public void setId(RAcnEvnId id) {
        this.id = id;
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
    public Boolean getAcnevnConfirmed() {
        return this.acnevnConfirmed;
    }
    
    public void setAcnevnConfirmed(Boolean acnevnConfirmed) {
        this.acnevnConfirmed = acnevnConfirmed;
    }
    public Date getAcnevnConfirmationDate() {
        return this.acnevnConfirmationDate;
    }
    
    public void setAcnevnConfirmationDate(Date acnevnConfirmationDate) {
        this.acnevnConfirmationDate = acnevnConfirmationDate;
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

