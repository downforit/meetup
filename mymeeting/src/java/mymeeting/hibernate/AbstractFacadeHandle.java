/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author marber
 */
public class AbstractFacadeHandle {
  private Session session;
  private Transaction transaction;
  private boolean avoidCommit;
  
  public static AbstractFacadeHandle createNewHandle(boolean avoidCommit){
    AbstractFacadeHandle result = new AbstractFacadeHandle();

    result.setAvoidCommit(avoidCommit);
    
    if(AbstractFacade.USE_NEW_SESSION_FOR_EACH_OPERATION){
      result.session = MyHibernateUtil.getSessionFactory().openSession();
    } else {
      result.session = MyHibernateUtil.getSessionFactory().getCurrentSession();
    }
    
    result.transaction = result.getSession().beginTransaction();
    
    return result;
  }

  /**
   * @return the session
   */
  public Session getSession() {
    return session;
  }
  
  
    
  public void closeAndCommit(){
    
    if(this.isAvoidCommit()){
      this.session.flush();
      return;
    }
    
    if(transaction != null){
      transaction.commit();
      transaction = null;
    }

    if(AbstractFacade.USE_NEW_SESSION_FOR_EACH_OPERATION){
      if(session != null){
        session.close();
        session = null;
      }
    } else {
      //do nothing
    }
  }

  public void closeAndRollback(){
    if(this.isAvoidCommit()){
      return;
    }

    if(transaction != null){
      transaction.rollback();
      transaction = null;
    }

    if(AbstractFacade.USE_NEW_SESSION_FOR_EACH_OPERATION){
      if(session != null){
        session.close();
        session = null;
      }
    } else {
      //do nothing
    }
  }

  /**
   * @return the avoidCommit
   */
  public boolean isAvoidCommit() {
    return avoidCommit;
  }

  /**
   * @param avoidCommit the avoidCommit to set
   */
  public void setAvoidCommit(boolean avoidCommit) {
    this.avoidCommit = avoidCommit;
  }
  
}
