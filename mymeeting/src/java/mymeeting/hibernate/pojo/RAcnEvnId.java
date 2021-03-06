package mymeeting.hibernate.pojo;
// Generated 7-giu-2013 0.23.19 by Hibernate Tools 3.2.1.GA



/**
 * RAcnEvnId generated by hbm2java
 */
public class RAcnEvnId  implements java.io.Serializable {


     private String acnId;
     private String evnId;

    public RAcnEvnId() {
    }

    public RAcnEvnId(String acnId, String evnId) {
       this.acnId = acnId;
       this.evnId = evnId;
    }
   
    public String getAcnId() {
        return this.acnId;
    }
    
    public void setAcnId(String acnId) {
        this.acnId = acnId;
    }
    public String getEvnId() {
        return this.evnId;
    }
    
    public void setEvnId(String evnId) {
        this.evnId = evnId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof RAcnEvnId) ) return false;
		 RAcnEvnId castOther = ( RAcnEvnId ) other; 
         
		 return ( (this.getAcnId()==castOther.getAcnId()) || ( this.getAcnId()!=null && castOther.getAcnId()!=null && this.getAcnId().equals(castOther.getAcnId()) ) )
 && ( (this.getEvnId()==castOther.getEvnId()) || ( this.getEvnId()!=null && castOther.getEvnId()!=null && this.getEvnId().equals(castOther.getEvnId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getAcnId() == null ? 0 : this.getAcnId().hashCode() );
         result = 37 * result + ( getEvnId() == null ? 0 : this.getEvnId().hashCode() );
         return result;
   }   


}


