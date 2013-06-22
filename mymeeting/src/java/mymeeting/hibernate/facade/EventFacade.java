/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.hibernate.facade;

import java.util.Date;
import java.util.List;
import mymeeting.hibernate.AbstractFacadeWithHandle;
import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import mymeeting.hibernate.pojo.RAcnGrp;
import mymeeting.services.ServicesUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

/**
 *
 * @author marber
 */
public class EventFacade  extends AbstractFacadeWithHandle<Event> {
  public EventFacade(){
    super(Event.class);
  }



  public void create(Event event) {
    //group.setAccount(new Account());
    //group.getAccount().setAcnId(username);

    event.setLastUpdate(new Date(System.currentTimeMillis()));
    event.setEvnCreationDate(event.getLastUpdate());
    
    super.create(event);
  }
  
  
  
  public List<Event> findForSync(String username, String lastUpdate){
    List<Event> result = null;

    Session session = getSession();

    Query qr;
    String baseQuery = "from Event as event join fetch event.account join fetch event.group where event.account.acnId = :username";
    if(lastUpdate != null && lastUpdate.length() > 0){
      qr = session.createQuery(baseQuery + " and event.lastUpdate > :lastUp");
      qr.setTimestamp("lastUp", ServicesUtils.parseDate(lastUpdate));
    } else {
      qr = session.createQuery(baseQuery);
    }
    qr.setString("username", username);

    result = (List<Event>)qr.list();


    DetachedCriteria subCriteria= DetachedCriteria.forClass(RAcnGrp.class);
    subCriteria.add(Property.forName("id.acnId").eq(username));
    subCriteria.setProjection(Projections.property("id.grpId"));

    DetachedCriteria query = DetachedCriteria.forClass(Event.class);
    query.add(Property.forName("group.grpId").in(subCriteria));

    List<Event> eventsTmp = query.getExecutableCriteria(session).list();
    result.addAll(eventsTmp);

    for(int i = 0; i < result.size(); i++){
      makeEventMinimal(result.get(i));
    }


    return result;
  }  


    public static void makeEventMinimal(Event event){
      event.setAccount(new Account(event.getAccount().getAcnId(), null, null));
      event.setGroup(new Group(event.getGroup().getGrpId(), null, null, null));
    }


}
