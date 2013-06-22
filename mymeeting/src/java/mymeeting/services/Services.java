/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mymeeting.hibernate.MyHibernateUtil;
import mymeeting.hibernate.AbstractFacadeHandle;
import mymeeting.hibernate.exceptions.C_HibernateConstraintException;
import mymeeting.hibernate.facade.AccountFacade;
import mymeeting.hibernate.facade.EventFacade;
import mymeeting.hibernate.facade.GroupFacade;
import mymeeting.hibernate.facade.RAcnGrpFacade;
import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import mymeeting.hibernate.pojo.RAcnGrp;
import mymeeting.services.responses.GenericResponse;
import mymeeting.services.responses.ResponseAccountsList;
import mymeeting.services.responses.ResponseEvent;
import mymeeting.services.responses.ResponseEventsList;
import mymeeting.services.responses.ResponseGroup;
import mymeeting.services.responses.ResponseGroupsList;
import mymeeting.services.responses.ResponseRAcnGrp;
import mymeeting.services.responses.ResponseRAcnGrpsList;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author marber
 */
public class Services extends HttpServlet {
  
  /**
   * Processes requests for both HTTP
   * <code>GET</code> and
   * <code>POST</code> methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/txt");
    PrintWriter out = response.getWriter();

    AbstractFacadeHandle dbHandle = null;
    Gson gsonConverter = null;

    
    try {
      String operation = request.getParameter(ServicesUtils.HTTP_PARAM_OPERATION);
      
      if(operation == null){
        throw new RuntimeException("paramentro OPERATION mancante nella richiesta REST");
      }
      
      //I use AbstractFacadeHandle becouse for operations that change data in the Database I need to update the "last update time" for
      //the Account (to perform a better synchronization). With AbstractFacadeHandle I cat do all operation in a single transaction with the
      //possibility to rollback all updates.
      dbHandle = AbstractFacadeHandle.createNewHandle(true);

      GenericResponse gresponse = null;
      gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();

      boolean updateLastModificationForAccount = false;
      
      if( operation.equals(ServicesUtils.HTTP_OP_LOGIN) ){
        gresponse = serviceLogin(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_NEW_ACCOUNT) ){
        updateLastModificationForAccount = true;
        gresponse = serviceNewAccount(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_SYNC_EVENTS) ){
        gresponse = serviceSyncEvents(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_NEW_EVENT) ){
        updateLastModificationForAccount = true;
        gresponse = serviceNewEvent(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_SYNC_GROUPS) ){
        gresponse = serviceSyncGroups(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_NEW_GROUP) ){
        updateLastModificationForAccount = true;
        gresponse = serviceNewGroup(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_SEARCH_ACCOUNT) ){
        gresponse = serviceSearchAccount(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_ADD_ACCOUNT_TO_GROUP) ){
        updateLastModificationForAccount = true;
        gresponse = serviceAddAccountToGroup(request, out, dbHandle);
      } else if( operation.equals(ServicesUtils.HTTP_OP_SYNC_R_ACN_GRP) ){
        gresponse = serviceSyncRAcnGrps(request, out, dbHandle);
      }

      
      if(updateLastModificationForAccount){
        //update last update time for the current accoutn. It is usefull for inprove the synching operations. I will improved it in a short time 
      }
      
      
      String jsonOutput = gsonConverter.toJson(gresponse);
      out.print(jsonOutput);      
      
      dbHandle.setAvoidCommit(false);
      dbHandle.closeAndCommit();
        
    }catch(ConstraintViolationException ex){
      if(gsonConverter != null){
        GenericResponse gresponse = new GenericResponse();

        gresponse.setServiceSuccessed(false);
        gresponse.setErrorCode(ServicesUtils.DB_ERROR_KEY_VIOLATION);
        gresponse.setDescription(ex.getMessage());

        String jsonOutput = gsonConverter.toJson(gresponse);
        out.print(jsonOutput);
      }
    } catch(Throwable ex){

      MyHibernateUtil.resetSessionFactory();
      throw new RuntimeException(ex);
    } finally { 
      if(dbHandle != null){
        dbHandle.setAvoidCommit(false);
        dbHandle.closeAndRollback();
      }

      out.close();
    }
  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP
   * <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP
   * <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>

  private GenericResponse serviceLogin(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
      String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);
      String password = request.getParameter(ServicesUtils.HTTP_PARAM_PASSWORD);
      

      AccountFacade accountFacade = new AccountFacade();
      accountFacade.setHandle(handle);
      Account account = accountFacade.find(username, password);
      

      GenericResponse gresponse = new GenericResponse();
      
      if(account == null){
        gresponse.setServiceSuccessed(false);
        gresponse.setErrorCode(-1);
      } else {
        gresponse.setServiceSuccessed(true);
      }
      
      return gresponse;
  }


  private GenericResponse serviceNewAccount(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
      String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);
      String password = request.getParameter(ServicesUtils.HTTP_PARAM_PASSWORD);
      
      AccountFacade accountFacade = new AccountFacade();
      accountFacade.setHandle(handle);
      Account account = accountFacade.createNew(username, password);

      GenericResponse gresponse = new GenericResponse();
      gresponse.setServiceSuccessed(true);

      if(account == null){
        gresponse.setServiceSuccessed(false);
        gresponse.setErrorCode(-1);
      }  
      
      
      return gresponse;
  }
  

  private GenericResponse serviceSyncEvents(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
    String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);
    String lastUpdate = request.getParameter(ServicesUtils.HTTP_PARAM_LAST_UPDATE);

    ResponseEventsList responseEvents = new ResponseEventsList();
    
    EventFacade eventFacade = new EventFacade();
    eventFacade.setHandle(handle);
    
    List<Event> events = eventFacade.findForSync(username, lastUpdate);    
    
    responseEvents.setEvents(events);
    
    return responseEvents;
    
  }



  
  private GenericResponse serviceNewEvent(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
    String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);

    Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();

    ResponseEvent responseEvent = new ResponseEvent();
    
    StringBuilder bodyRequest = new StringBuilder();
    try {
      BufferedReader in = request.getReader();
      
      String line;
      while ((line = in.readLine()) != null) {
          bodyRequest.append(line).append("\n");
      }
    } catch(IOException ex){
      throw new RuntimeException(ex);
    }
    
    Type eventType = new TypeToken<Event>(){}.getType();
    Event event = gsonConverter.fromJson(bodyRequest.toString(), eventType);
      

    EventFacade eventFacade = new EventFacade();
    eventFacade.setHandle(handle);
    
    eventFacade.create(event);
    
    responseEvent.setEvent(event);
      
    return responseEvent;
    
  }
  
  
  private GenericResponse serviceSyncGroups(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
    String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);
    String lastUpdate = request.getParameter(ServicesUtils.HTTP_PARAM_LAST_UPDATE);

    ResponseGroupsList responseGroups = new ResponseGroupsList();
    
    GroupFacade groupFacade = new GroupFacade();
    groupFacade.setHandle(handle);
    
    List<Group> groups = groupFacade.findForSync(username, lastUpdate);
    
    responseGroups.setGroups(groups);
    
    return responseGroups;
  }
 

 
  private GenericResponse serviceSyncRAcnGrps(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
    String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);
    String lastUpdate = request.getParameter(ServicesUtils.HTTP_PARAM_LAST_UPDATE);

    ResponseRAcnGrpsList responseRAcnGrps = new ResponseRAcnGrpsList();

    RAcnGrpFacade rAcnGrpFacade = new RAcnGrpFacade();
    rAcnGrpFacade.setHandle(handle);

    List<RAcnGrp> rAcnGrps = rAcnGrpFacade.findForSync(username, lastUpdate);
      
    responseRAcnGrps.setrAcnGrps(rAcnGrps);
      

    return responseRAcnGrps;
  }
  
  
  
  private GenericResponse serviceNewGroup(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
    String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);

    Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();

    ResponseGroup responseGroup = new ResponseGroup();
    
    StringBuilder bodyRequest = new StringBuilder();
    try{
      BufferedReader in = request.getReader();
      
      String line;
      while ((line = in.readLine()) != null) {
          bodyRequest.append(line).append("\n");
      }
    } catch(IOException ex){
      throw new RuntimeException(ex);
    }

    
    Type groupType = new TypeToken<Group>(){}.getType();
    Group group = gsonConverter.fromJson(bodyRequest.toString(), groupType);

    
    GroupFacade groupFacade = new GroupFacade();
    groupFacade.setHandle(handle);
    
    groupFacade.create(group);

    responseGroup.setGroup(group);
      
    return responseGroup;
    
  }
  
  
  
  private GenericResponse serviceSearchAccount(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
    String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);

    ResponseAccountsList response = new ResponseAccountsList();
    
    AccountFacade accountFacade = new AccountFacade();
    accountFacade.setHandle(handle);
    
    List<Account> accounts = accountFacade.findBySubstring(username);

    if(accounts == null){
      response.setServiceSuccessed(false);
      response.setErrorCode(-1);
    } else {
      response.setServiceSuccessed(true);
      response.setAccounts(accounts);
    }

    return response;
    
  }
  

  
  private GenericResponse serviceAddAccountToGroup(HttpServletRequest request, PrintWriter out, AbstractFacadeHandle handle){
    String username = request.getParameter(ServicesUtils.HTTP_PARAM_USERNAME);
    String group = request.getParameter(ServicesUtils.HTTP_PARAM_GROUP_ID);

      
    RAcnGrpFacade rAcnGrpFacade = new RAcnGrpFacade();
    rAcnGrpFacade.setHandle(handle);
    RAcnGrp rAcnGrp = rAcnGrpFacade.addAccountToGroup(username, group);


    ResponseRAcnGrp response = new ResponseRAcnGrp();

    if(rAcnGrp == null){
      response.setServiceSuccessed(false);
      response.setErrorCode(-1);
    } else {
      response.setServiceSuccessed(true);
      response.setrAcnGrp(rAcnGrp);
    }


    return response;
    
  }
  

}
