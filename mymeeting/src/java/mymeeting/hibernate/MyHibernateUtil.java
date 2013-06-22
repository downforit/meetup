/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.hibernate;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author marber
 */
public class MyHibernateUtil {

    private static SessionFactory sessionFactory;
        
    private static void initSessionFactory(){
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    
    public static void resetSessionFactory(){
      sessionFactory.close();
      sessionFactory = null;
      System.out.println("#################################");
      System.out.println("####### RESET HIBERNATE #########");
      System.out.println("#################################");
      initSessionFactory();
    }
    
    public static SessionFactory getSessionFactory() {
      if(sessionFactory == null){
        initSessionFactory();
      }
        return sessionFactory;
    }
}
