package it.marberpp.myevents.hibernate.pojo;
// Generated 19-mag-2013 15.00.25 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Account generated by hbm2java
 */
public class Account  implements java.io.Serializable {
     private String acnId;
     private String acnPasswd;
     private String acnImageUrl;
     private Date acnCreationDate;
     private Set<RAcnEvn> RAcnEvns = new HashSet<RAcnEvn>(0);
     private Set<Group> groups = new HashSet<Group>(0);
     private Set<RAcnGrp> RAcnGrps = new HashSet<RAcnGrp>(0);
     private Set<EventDett> eventDetts = new HashSet<EventDett>(0);

    public Account() {
    }

	
    public Account(String acnId, String acnPasswd) {
        this.acnId = acnId;
        this.acnPasswd = acnPasswd;
    }
    public Account(String acnId, String acnPasswd, String acnImageUrl, Date acnCreationDate, Set<RAcnEvn> RAcnEvns, Set<Group> groups, Set<RAcnGrp> RAcnGrps, Set<EventDett> eventDetts) {
       this.acnId = acnId;
       this.acnPasswd = acnPasswd;
       this.acnImageUrl = acnImageUrl;
       this.acnCreationDate = acnCreationDate;
       this.RAcnEvns = RAcnEvns;
       this.groups = groups;
       this.RAcnGrps = RAcnGrps;
       this.eventDetts = eventDetts;
    }
   
    public String getAcnId() {
        return this.acnId;
    }
    
    public void setAcnId(String acnId) {
        this.acnId = acnId;
    }
    public String getAcnPasswd() {
        return this.acnPasswd;
    }
    
    public void setAcnPasswd(String acnPasswd) {
        this.acnPasswd = acnPasswd;
    }
    public String getAcnImageUrl() {
        return this.acnImageUrl;
    }
    
    public void setAcnImageUrl(String acnImageUrl) {
        this.acnImageUrl = acnImageUrl;
    }
    public Date getAcnCreationDate() {
        return this.acnCreationDate;
    }
    
    public void setAcnCreationDate(Date acnCreationDate) {
        this.acnCreationDate = acnCreationDate;
    }
    public Set<RAcnEvn> getRAcnEvns() {
        return this.RAcnEvns;
    }
    
    public void setRAcnEvns(Set<RAcnEvn> RAcnEvns) {
        this.RAcnEvns = RAcnEvns;
    }
    public Set<Group> getGroups() {
        return this.groups;
    }
    
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    public Set<RAcnGrp> getRAcnGrps() {
        return this.RAcnGrps;
    }
    
    public void setRAcnGrps(Set<RAcnGrp> RAcnGrps) {
        this.RAcnGrps = RAcnGrps;
    }
    public Set<EventDett> getEventDetts() {
        return this.eventDetts;
    }
    
    public void setEventDetts(Set<EventDett> eventDetts) {
        this.eventDetts = eventDetts;
    }




}


