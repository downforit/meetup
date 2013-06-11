/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.marberpp.myevents.network;

import java.text.SimpleDateFormat;

/**
 *
 * @author marber
 */
public class ServicesUtils {
  public static final String JSON_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
  public static final SimpleDateFormat dateFormatter = new SimpleDateFormat(JSON_DATE_FORMAT);
 
  //public static final String DNS_SERVER = "192.168.1.2";
  //public static final String DNS_SERVER = "192.168.136.229";
  //public static final String DNS_SERVER = "10.37.130.4";
  //public static final String DNS_SERVER = "192.168.0.14";
  //public static final String DNS_SERVER = "192.168.43.HTTP_OP_NEW_EVENT150";
  public static final String DNS_SERVER = "46.105.16.216";
  
  public static final String HTTP_SERVICE_URI = "/mymeeting/eventservice";
  
  public static final String IP_PORT_NUMBER = "8080";

  public static final String HTTP_PARAM_OPERATION = "op";
  public static final String HTTP_PARAM_USERNAME = "uname";
  public static final String HTTP_PARAM_PASSWORD = "pwd";
  public static final String HTTP_PARAM_LAST_UPDATE = "lastUp";

  public static final String HTTP_OP_GET_EVENTS = "getEvents";
  public static final String HTTP_OP_LOGIN = "login";
  public static final String HTTP_OP_NEW_ACCOUNT = "newacnt";
  public static final String HTTP_OP_SYNC_EVENTS = "syncevents";
  public static final String HTTP_OP_NEW_EVENT = "newevent";
  public static final String HTTP_OP_SYNC_GROUPS = "syncgroups";
  public static final String HTTP_OP_NEW_GROUP = "newgroup";
  
  
  public static final int NET_ERROR_NETWORK_COMUNICATION = -6567;
  public static final int NET_ERROR_LOGIN_FAILED = -1;
  public static final int NET_ERROR_UNEXPECTED = -9999;
  
  public static final int DB_ERROR_KEY_VIOLATION = -111;

  
  
  
	public static String extractToStringSave(Object obj){
		if(obj == null){
			return null;
		} else {
			return obj.toString();
		}
	}
	
  
}
