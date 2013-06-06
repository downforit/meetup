/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.marberpp.myevents.services;

/**
 *
 * @author marber
 */
public class ServicesUtils {
  public static final String JSON_DATE_FORMAT = "dd/MM/yyyy";
 
  //public static final String DNS_SERVER = "192.168.136.229";
  //public static final String DNS_SERVER = "10.37.130.4";
  //public static final String DNS_SERVER = "192.168.0.14";
  //public static final String DNS_SERVER = "192.168.43.150";
  public static final String DNS_SERVER = "46.105.16.216";
  
  public static final String IP_PORT_NUMBER = "8080";

  public static final String HTTP_PARAM_OPERATION = "op";
  public static final String HTTP_PARAM_USERNAME = "uname";
  public static final String HTTP_PARAM_PASSWORD = "pwd";

  public static final String HTTP_OP_GET_EVENTS = "getEvents";
  public static final String HTTP_OP_LOGIN = "login";
  public static final String HTTP_OP_NEW_ACCOUNT = "newacnt";

  public static final int NET_ERROR_NETWORK_COMUNICATION = -6567;
  public static final int NET_ERROR_LOGIN_FAILED = -1;
  
  public static final int HIB_ERROR_KEY_VIOLATION = -111;

}
