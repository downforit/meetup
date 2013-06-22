/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mymeeting.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 *
 * @author tgiunipero
 */
public abstract class AbstractFacadeWithHandle<T> {
  
    private Class<T> entityClass;
    AbstractFacadeHandle handle;
    //Session currentSession = null;
            
    public AbstractFacadeWithHandle(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setHandle(AbstractFacadeHandle handle){
        this.handle = handle;
    }
    
    protected Session getSession() {
      if(this.handle == null){
        this.handle = AbstractFacadeHandle.createNewHandle(false);
      }
      return this.handle.getSession();
    }
    
    
    
    protected void closeSessionAndCommit(){
      this.handle.closeAndCommit();
    }
    
    protected void closeSessionAndRollback(){
      this.handle.closeAndRollback();
    }
    
    
    //***************************************************
    //***************************************************
    //***************************************************
    
    
    public List<T> findAll() {
        List<T> TList;

        Session currentSession = getSession();

        Criteria cq = currentSession.createCriteria(entityClass);
        TList = cq.list();
        closeSessionAndCommit();
        
        return TList;
    }

    public List<T> findRange(int[] range) {
        List<T> TList;

        Session currentSession = getSession();

        Criteria cq = currentSession.createCriteria(entityClass);
        cq.setMaxResults(range[1] - range[0]);
        cq.setFirstResult(range[0]);
        TList = cq.list();

        closeSessionAndCommit();
        
        return TList;

    }

    
    public T find(Object id) {
        Session currentSession = getSession();

        T result = (T) currentSession.get(entityClass, (Serializable)id);

        closeSessionAndCommit();
        
        return result;
    }    
    
    public void create(T entity) {
        Session currentSession = getSession();

        currentSession.save(entity);

        closeSessionAndCommit();
    }

    public void edit(T entity) {
        Session currentSession = getSession();

        currentSession.merge(entity);

        closeSessionAndCommit();
    }

    
    public void remove(T entity) {
        Session currentSession = getSession();

        entity = (T) currentSession.merge(entity);
        currentSession.delete(entity);

        closeSessionAndCommit();
    }

    //*/
    public int count() {
        Session currentSession = getSession();
  
        Criteria cq = currentSession.createCriteria(entityClass);
        cq.setProjection(Projections.rowCount());
        Number numRows = (Number)cq.uniqueResult();
        
        closeSessionAndCommit();

        return numRows.intValue();
    }
    //*/

}