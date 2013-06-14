package it.marberpp.myevents.network;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import mymeeting.exceptions.C_NetworkComunicationException;
import mymeeting.exceptions.C_NetworkKeyDuplicateException;
import mymeeting.exceptions.C_NetworkLoginException;
import mymeeting.exceptions.C_NetworkResponseException;
import mymeeting.exceptions.C_UnexpectedException;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;



public class NetworkHelper {
	private static int timeoutMillis = 60000;


	//*********************************************
	public static int getTimeoutMillis() {
		return timeoutMillis;
	}


	//*********************************************
	public static void setTimeoutMillis(int timeoutMillis) {
		NetworkHelper.timeoutMillis = timeoutMillis;
	}


	//http://10.37.131.4:8080/mymeeting/eventservice?op=login&uname=marber&pwd=marberdd
	//*********************************************
	public static void doLogin(String username, String password){
		GenericResponse response = null;
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_LOGIN 
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1")
		                 + "&" + ServicesUtils.HTTP_PARAM_PASSWORD + "="
		                 + URLEncoder.encode(password, "ISO-8859-1");
		}catch (Exception e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}
		

		try {
			String jsonResponse = callServer(url);
			
			Type listType = new TypeToken<GenericResponse>(){}.getType();
			  
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			response = gsonConverter.fromJson(jsonResponse, listType);
			
			if( !response.isServiceSuccessed() ){
				if(response.getErrorCode() == ServicesUtils.NET_ERROR_LOGIN_FAILED){
					throw new C_NetworkLoginException();
				} else {
					throw new C_NetworkResponseException(response.getDescription());
				}
			}
			
		} catch (IOException e) {
			throw new C_NetworkComunicationException(e);
		}
		
		
		return;
	}
	

	
	//*********************************************
	public static void createNewAccount(String username, String password){
		GenericResponse response = null;
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_NEW_ACCOUNT
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1")
		                 + "&" + ServicesUtils.HTTP_PARAM_PASSWORD + "="
		                 + URLEncoder.encode(password, "ISO-8859-1");
		}catch (Exception e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}
		

		try {
			String jsonResponse = callServer(url);
			
			Type listType = new TypeToken<GenericResponse>(){}.getType();
			  
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			response = gsonConverter.fromJson(jsonResponse, listType);			
			
		} catch (IOException e) {
			throw new C_NetworkComunicationException(e);

		}
		
		if( !response.isServiceSuccessed() ){
			if(response.getErrorCode() == ServicesUtils.NET_ERROR_LOGIN_FAILED){
				throw new C_NetworkLoginException();
			} else if(response.getErrorCode() == ServicesUtils.DB_ERROR_KEY_VIOLATION) {
				throw new C_NetworkKeyDuplicateException(response.getDescription());
			} else {
				throw new C_NetworkResponseException(response.getDescription());
			}
		}
		
		return;
	}
	
	
	//*********************************************
	public static List<Account> serachAccounts(String username){
		ResponseAccountsList response = null;
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_SEARCH_ACCOUNT
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1");
		}catch (Exception e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}
		

		try {
			String jsonResponse = callServer(url);
			
			Type listType = new TypeToken<ResponseAccountsList>(){}.getType();
			  
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			response = gsonConverter.fromJson(jsonResponse, listType);
			
			if( !response.isServiceSuccessed() ){
				throw new C_NetworkResponseException(response.getDescription());
			}
			
		} catch (IOException e) {
			throw new C_NetworkComunicationException(e);
		}
		
		
		return response.getAccounts();
	}

	
		
	//*********************************************
	public static RAcnGrp addAccountToGroup(String username, String groupId){
		ResponseRAcnGrp response = null;
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_ADD_ACCOUNT_TO_GROUP
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1")
		                 + "&" + ServicesUtils.HTTP_PARAM_GROUP_ID + "="
		                 + URLEncoder.encode(groupId, "ISO-8859-1");
		}catch (Exception e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}
		

		try {
			String jsonResponse = callServer(url);
			
			Type listType = new TypeToken<ResponseRAcnGrp>(){}.getType();
			  
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			response = gsonConverter.fromJson(jsonResponse, listType);
			
			if( !response.isServiceSuccessed() ){
				if(response.getErrorCode() == ServicesUtils.DB_ERROR_KEY_VIOLATION) {
					throw new C_NetworkKeyDuplicateException(response.getDescription());
				} else {
					throw new C_NetworkResponseException(response.getDescription());
				}
			}
			
		} catch (IOException e) {
			throw new C_NetworkComunicationException(e);
		}
		
		
		return response.getrAcnGrp();
	}
	
	
	
	//*********************************************
	public static Event createNewEvent(Event event, String username){
		Event result = null;
		
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_NEW_EVENT
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1");
		}catch (Exception e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}

		
		ResponseEvent response;
		
		try{
			Type listType = new TypeToken<Event>(){}.getType();
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			String jsonEvent = gsonConverter.toJson(event, listType);			

			String jsonResponse = NetworkHelper.callServer(url, jsonEvent);
			
			Type eventType = new TypeToken<ResponseEvent>(){}.getType();
			response = gsonConverter.fromJson(jsonResponse, eventType);

		} catch (Throwable e) {
			throw new C_NetworkComunicationException(e);
		}

		if( !response.isServiceSuccessed() ){
			if(response.getErrorCode() == ServicesUtils.NET_ERROR_LOGIN_FAILED){
				throw new C_NetworkLoginException();
			} else if(response.getErrorCode() == ServicesUtils.DB_ERROR_KEY_VIOLATION) {
				throw new C_NetworkKeyDuplicateException(response.getDescription());
			} else {
				throw new C_NetworkResponseException(response.getDescription());
			}
		}
		
		result = response.getEvent();
		
		return result;
	}
	
	//http://192.168.1.3:8080/mymeeting/eventservice?op=syncevents&uname=marber&lastUp=2013%2F06%2F08+15%3A47%3A30
	//*********************************************
	public static List<Event> getEventsToSync(String username, String lastUpdate){
		String url;
		ResponseEventsList responseEvents = null;
		
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_SYNC_EVENTS
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1");
			if(lastUpdate != null && lastUpdate.length() > 0){
						 url += "&" + ServicesUtils.HTTP_PARAM_LAST_UPDATE + "="
		                 + URLEncoder.encode(lastUpdate, "ISO-8859-1") ;
			} else {
						 url += "&" + ServicesUtils.HTTP_PARAM_LAST_UPDATE + "=";
			}
		                 
		}catch (Throwable e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}
		

		try {
			String jsonResponse = NetworkHelper.callServer(url);

			
			Type listType = new TypeToken<ResponseEventsList>(){}.getType();
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			responseEvents = gsonConverter.fromJson(jsonResponse, listType);			
			
			if(responseEvents.getErrorCode() < 0){
				throw new C_NetworkResponseException(responseEvents.getErrorCode() + ": " + responseEvents.getDescription());
			}
			
		} catch (Throwable e) {
			throw new C_NetworkComunicationException(e);
		}
		
		return responseEvents.getEvents();
	}
	
	
	



	//*********************************************
	public static Group createNewGroup(Group group, String username){
		Group result = null;
		
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_NEW_GROUP
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1");
		}catch (Exception e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}

		
		
		ResponseGroup response;
		
		try{
			Type listType = new TypeToken<Group>(){}.getType();
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			String jsonGroup = gsonConverter.toJson(group, listType);			

			String jsonResponse = NetworkHelper.callServer(url, jsonGroup);
			
			Type groupType = new TypeToken<ResponseGroup>(){}.getType();
			response = gsonConverter.fromJson(jsonResponse, groupType);

		} catch (Throwable e) {
			throw new C_NetworkComunicationException(e);
		}

		if( !response.isServiceSuccessed() ){
			if(response.getErrorCode() == ServicesUtils.NET_ERROR_LOGIN_FAILED){
				throw new C_NetworkLoginException();
			} else if(response.getErrorCode() == ServicesUtils.DB_ERROR_KEY_VIOLATION) {
				throw new C_NetworkKeyDuplicateException(response.getDescription());
			} else {
				throw new C_NetworkResponseException(response.getDescription());
			}
		}
		
		result = response.getGroup();
		
		
		
		return result;
	}
	
	
	
	//*********************************************
	public static List<Group> getGroupsToSync(String username, String lastUpdate){
		String url;
		ResponseGroupsList responseGroups = null;
		
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_SYNC_GROUPS
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1");
			if(lastUpdate != null && lastUpdate.length() > 0){
						 url += "&" + ServicesUtils.HTTP_PARAM_LAST_UPDATE + "="
		                 + URLEncoder.encode(lastUpdate, "ISO-8859-1") ;
			} else {
						 url += "&" + ServicesUtils.HTTP_PARAM_LAST_UPDATE + "=";
			}
		                 
		}catch (Throwable e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}
		

		try {
			String jsonResponse = NetworkHelper.callServer(url);

			
			Type listType = new TypeToken<ResponseGroupsList>(){}.getType();
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			responseGroups = gsonConverter.fromJson(jsonResponse, listType);			
			
			if(responseGroups.getErrorCode() < 0){
				throw new C_NetworkResponseException(responseGroups.getErrorCode() + ": " + responseGroups.getDescription());
			}
			
		} catch (Throwable e) {
			throw new C_NetworkComunicationException(e);
		}
		
		return responseGroups.getGroups();
	}
	
	

	
	//*********************************************
	public static List<RAcnGrp> getRAcnGrpsToSync(String username, String lastUpdate){
		String url;
		ResponseRAcnGrpsList responseRAcnGrps = null;
		
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + ServicesUtils.HTTP_SERVICE_URI + "?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_SYNC_R_ACN_GRP
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1");
			if(lastUpdate != null && lastUpdate.length() > 0){
						 url += "&" + ServicesUtils.HTTP_PARAM_LAST_UPDATE + "="
		                 + URLEncoder.encode(lastUpdate, "ISO-8859-1") ;
			} else {
						 url += "&" + ServicesUtils.HTTP_PARAM_LAST_UPDATE + "=";
			}
		                 
		}catch (Throwable e) {
			throw new C_UnexpectedException("error during construction of url", e);
		}
		

		try {
			String jsonResponse = NetworkHelper.callServer(url);

			
			Type listType = new TypeToken<ResponseRAcnGrpsList>(){}.getType();
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			responseRAcnGrps = gsonConverter.fromJson(jsonResponse, listType);			
			
			if(responseRAcnGrps.getErrorCode() < 0){
				throw new C_NetworkResponseException(responseRAcnGrps.getErrorCode() + ": " + responseRAcnGrps.getDescription());
			}
			
		} catch (Throwable e) {
			throw new C_NetworkComunicationException(e);
		}
		
		return responseRAcnGrps.getrAcnGrp();
	}
	
	
	//*********************************************
	//*********************************************
	//*********************************************
	public static String callServer(String path) throws IOException {
		return callServer(path, null);
	}
	//*********************************************
	public static String callServer(String path, String bodyToSend) throws IOException {
		
		BufferedWriter writer = null;
		BufferedReader reader = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			
			if(bodyToSend == null){
				conn.setRequestMethod("GET");
			} else {
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "text/plain"); 
				//conn.setRequestProperty("Content-Length", String.valueOf(bodyToSend.getBytes().length) );
			}
			
			conn.setReadTimeout(timeoutMillis);
			conn.connect();
			
			if(bodyToSend != null){

				writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				writer.write(bodyToSend);
				writer.close();
				writer = null;
			}
			
			
			
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder buf = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buf.append(line + "\n");
			}
			return (buf.toString());
		} catch(RuntimeException ex){
			ex.printStackTrace();
			throw ex;
		} finally {
			if(writer != null){
				writer.close();
			}
			
			if (reader != null) {
				reader.close();
			}

			if (conn != null) {
				conn.disconnect();
			}
			
		}
	}


	
	
	
	
}//class
