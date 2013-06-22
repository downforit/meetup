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
import org.hibernate.Query;

/**
 *
 * @author marber
 */
public class AccountFacade  extends AbstractFacadeWithHandle<Account> {
  public AccountFacade(){
    super(Account.class);
  }


  public Account find(String username, String password){
    Account result;

    Query qr = getSession().createQuery("from Account as account Where account.acnId = :username and account.acnPasswd = :password");
    qr.setString("username", username);
    qr.setString("password", password);
    result = (Account) qr.uniqueResult();

    return result;
  }  

  
  public List<Account> findBySubstring(String substring){
    List<Account> result;

    Query qr = getSession().createQuery("from Account as account where account.acnId LIKE :usernamelike");
    qr.setString("usernamelike", "%" + substring + "%");
    result = (List<Account>)qr.list();

    return result;
  }  

  
  public Account createNew(String username, String password){
    Account accountNew = null;

    accountNew = new Account();

    accountNew.setAcnId(username);
    accountNew.setAcnPasswd(password);
    accountNew.setLastUpdate(new Date(System.currentTimeMillis()));
    accountNew.setAcnCreationDate(accountNew.getLastUpdate());

    this.create(accountNew);


    return accountNew;
  }  



}
