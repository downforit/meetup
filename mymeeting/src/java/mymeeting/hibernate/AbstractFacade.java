/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mymeeting.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

/**
 *
 * @author tgiunipero
 */
public abstract class AbstractFacade<T> {
    public static final boolean USE_NEW_SESSION_FOR_EACH_OPERATION = true;
  
    private Class<T> entityClass;
    //Session currentSession = null;
            
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;

        //SessionFactory sf = HibernateUtil.getSessionFactory();
        //this.currentSession = sf.getCurrentSession();
        
    }

    protected static SessionFactory getSessionFactory() {
      return MyHibernateUtil.getSessionFactory();
    }

    protected Session getSession() {
      if(USE_NEW_SESSION_FOR_EACH_OPERATION){
        return AbstractFacade.getSessionFactory().openSession();
      } else {
        return AbstractFacade.getSessionFactory().getCurrentSession();
      }
    }
    
    
    
    protected void closeSession(Transaction transation, Session session){
      if(USE_NEW_SESSION_FOR_EACH_OPERATION){
        if(transation != null){
          transation.commit();
        }
        session.close();
      } else {
        
      }
      
    }
    
    
    //***************************************************
    //***************************************************
    //***************************************************
    
    
    public List<T> findAll() {
        List<T> TList;

        Session currentSession = getSession();
        org.hibernate.Transaction tx = currentSession.beginTransaction();
        Criteria cq = currentSession.createCriteria(entityClass);
        TList = cq.list();
        closeSession(tx, currentSession);
        
        return TList;
    }

    public List<T> findRange(int[] range) {
        List<T> TList;

        Session currentSession = getSession();
        org.hibernate.Transaction tx = currentSession.beginTransaction();
        Criteria cq = currentSession.createCriteria(entityClass);
        cq.setMaxResults(range[1] - range[0]);
        cq.setFirstResult(range[0]);
        TList = cq.list();
        closeSession(tx, currentSession);
        
        return TList;

    }

    
    public T find(Object id) {
        Session currentSession = getSession();
        org.hibernate.Transaction tx = currentSession.beginTransaction();
        T result = (T) currentSession.get(entityClass, (Serializable)id);

        closeSession(tx, currentSession);
        
        return result;
    }    
    
    public void create(T entity) {
        Session currentSession = getSession();
        org.hibernate.Transaction tx = currentSession.beginTransaction();
        currentSession.save(entity);

        closeSession(tx, currentSession);
    }

    public void edit(T entity) {
        Session currentSession = getSession();
        org.hibernate.Transaction tx = currentSession.beginTransaction();
        currentSession.merge(entity);

        closeSession(tx, currentSession);
    }

    
    public void remove(T entity) {
        Session currentSession = getSession();
        org.hibernate.Transaction tx = currentSession.beginTransaction();

        entity = (T) currentSession.merge(entity);
        currentSession.delete(entity);

        closeSession(tx, currentSession);
    }

    //*/
    public int count() {
        Session currentSession = getSession();
        org.hibernate.Transaction tx = currentSession.beginTransaction();
  
        Criteria cq = currentSession.createCriteria(entityClass);
        cq.setProjection(Projections.rowCount());
        Number numRows = (Number)cq.uniqueResult();
        
        closeSession(tx, currentSession);

        return numRows.intValue();
    }
    //*/

}