/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services.responses;

import com.google.gson.annotations.Expose;
import java.util.List;
import mymeeting.hibernate.pojo.Account;

/**
 *
 * @author marber
 */
public class ResponseAccountsList extends GenericResponse{
  @Expose
  private List<Account> accounts;
  

  public ResponseAccountsList(){
    
  }

  /**
   * @return the account
   */
  public List<Account> getAccounts() {
    return accounts;
  }

  /**
   * @param account the account to set
   */
  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }

  
}
