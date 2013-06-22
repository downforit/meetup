package mymeeting.hibernate.pojo;
// Generated 7-giu-2013 0.23.19 by Hibernate Tools 3.2.1.GA


import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Group generated by hbm2java
 */
public class Group  implements java.io.Serializable {


     @Expose
     private String grpId;
     @Expose
     private Account account;
     @Expose
     private String grpDescription;
     @Expose
     private String grpImageUrl;
     @Expose
     private Date grpCreationDate;
     @Expose
     private Boolean flgNeedSync;
     @Expose
     private Date lastUpdate;
     @Expose
     private Boolean flgShowed;
     @Expose
     private Boolean flgDeleted;
     private Set events = new HashSet(0);
     private Set RAcnGrps = new HashSet(0);

    public Group() {
    }

	
    public Group(String grpId, Account account, Date grpCreationDate, Date lastUpdate) {
        this.grpId = grpId;
        this.account = account;
        this.grpCreationDate = grpCreationDate;
        this.lastUpdate = lastUpdate;
    }
    public Group(String grpId, Account account, String grpDescription, String grpImageUrl, Date grpCreationDate, Boolean flgNeedSync, Date lastUpdate, Boolean flgShowed, Boolean flgDeleted, Set events, Set RAcnGrps) {
       this.grpId = grpId;
       this.account = account;
       this.grpDescription = grpDescription;
       this.grpImageUrl = grpImageUrl;
       this.grpCreationDate = grpCreationDate;
       this.flgNeedSync = flgNeedSync;
       this.lastUpdate = lastUpdate;
       this.flgShowed = flgShowed;
       this.flgDeleted = flgDeleted;
       this.events = events;
       this.RAcnGrps = RAcnGrps;
    }
   
    public String getGrpId() {
        return this.grpId;
    }
    
    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    public String getGrpDescription() {
        return this.grpDescription;
    }
    
    public void setGrpDescription(String grpDescription) {
        this.grpDescription = grpDescription;
    }
    public String getGrpImageUrl() {
        return this.grpImageUrl;
    }
    
    public void setGrpImageUrl(String grpImageUrl) {
        this.grpImageUrl = grpImageUrl;
    }
    public Date getGrpCreationDate() {
        return this.grpCreationDate;
    }
    
    public void setGrpCreationDate(Date grpCreationDate) {
        this.grpCreationDate = grpCreationDate;
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
    public Set getEvents() {
        return this.events;
    }
    
    public void setEvents(Set events) {
        this.events = events;
    }
    public Set getRAcnGrps() {
        return this.RAcnGrps;
    }
    
    public void setRAcnGrps(Set RAcnGrps) {
        this.RAcnGrps = RAcnGrps;
    }




}

