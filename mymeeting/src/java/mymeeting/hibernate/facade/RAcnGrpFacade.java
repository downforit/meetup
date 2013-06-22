/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.hibernate.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mymeeting.hibernate.AbstractFacadeWithHandle;
import mymeeting.hibernate.pojo.Group;
import mymeeting.hibernate.pojo.RAcnGrp;
import mymeeting.hibernate.pojo.RAcnGrpId;
import mymeeting.services.ServicesUtils;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author marber
 */
public class RAcnGrpFacade  extends AbstractFacadeWithHandle<RAcnGrp> {
  public RAcnGrpFacade(){
    super(RAcnGrp.class);
  }


  public RAcnGrp addAccountToGroup(String username, String groupId){
    RAcnGrp rAcnGrp = new RAcnGrp();

    rAcnGrp.setId(new RAcnGrpId(username, groupId));
    rAcnGrp.setAcngrpCreationDate(new Date(System.currentTimeMillis()));
    rAcnGrp.setLastUpdate(rAcnGrp.getAcngrpCreationDate());
    
    this.create(rAcnGrp);
    
    return rAcnGrp;
  }
  
  
  
  public List<RAcnGrp> findForSync(String username, String lastUpdate){
    List<RAcnGrp> result;

    Session session = getSession();

    List<RAcnGrp> rAcnGrps = null;

    Query qr;
    //String baseQuery = "from RAcnGrp as r join fetch r.group join fetch r.group.RAcnGrps  where r.id.acnId = :username";
    String baseQuery = "from RAcnGrp as r  where r.id.acnId = :username";
    if(lastUpdate != null && lastUpdate.length() > 0){
      qr = session.createQuery(baseQuery + " and r.lastUpdate > :lastUp");
      qr.setTimestamp("lastUp", ServicesUtils.parseDate(lastUpdate));
    } else {
      qr = session.createQuery(baseQuery);
    }
    qr.setString("username", username);

    rAcnGrps = (List<RAcnGrp>)qr.list();

    result = new ArrayList<RAcnGrp>();


    for(int i = 0; i < rAcnGrps.size(); i++ ){
      result.addAll(rAcnGrps.get(i).getGroup().getRAcnGrps());
    }

    return result;
  }


}
