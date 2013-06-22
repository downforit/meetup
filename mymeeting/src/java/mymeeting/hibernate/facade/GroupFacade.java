/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.hibernate.facade;

import java.util.Date;
import java.util.List;
import mymeeting.hibernate.AbstractFacadeWithHandle;
import mymeeting.hibernate.exceptions.C_HibernateConstraintException;
import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Group;
import mymeeting.hibernate.pojo.RAcnGrp;
import mymeeting.services.ServicesUtils;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author marber
 */
public class GroupFacade  extends AbstractFacadeWithHandle<Group> {
  public GroupFacade(){
    super(Group.class);
  }



  public void create(Group group) {
    //group.setAccount(new Account());
    //group.getAccount().setAcnId(username);

    group.setLastUpdate(new Date(System.currentTimeMillis()));
    group.setGrpCreationDate(group.getLastUpdate());
    
    super.create(group);
  }
  
  
  
  public List<Group> findForSync(String username, String lastUpdate){
    List<Group> result;

    Session session = getSession();

    Query qr;
    String baseQuery = "from Group as g join fetch g.account where g.account.acnId = :username";
    if(lastUpdate != null && lastUpdate.length() > 0){
      qr = session.createQuery(baseQuery + " and g.lastUpdate > :lastUp");
      qr.setTimestamp("lastUp", ServicesUtils.parseDate(lastUpdate));
    } else {
      qr = session.createQuery(baseQuery);
    }
    qr.setString("username", username);

    result = (List<Group>)qr.list();

            
    baseQuery = "from RAcnGrp as r join fetch r.group join fetch r.group.account where r.id.acnId = :username";
    if(lastUpdate != null && lastUpdate.length() > 0){
      qr = session.createQuery(baseQuery + " and r.lastUpdate > :lastUp");
      qr.setTimestamp("lastUp", ServicesUtils.parseDate(lastUpdate));
    } else {
      qr = session.createQuery(baseQuery);
    }
    qr.setString("username", username);

    List<RAcnGrp> rAcnGrps = (List<RAcnGrp>)qr.list();
    for(int i = 0; i < rAcnGrps.size(); i++){
      //rAcnGrps.get(i).getGroup().getAccount().getAcnId();//per costringere hibernate a caricare i dati
      result.add(rAcnGrps.get(i).getGroup());
    }

    return result;
  }  




}
