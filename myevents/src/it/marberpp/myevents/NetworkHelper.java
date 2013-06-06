package it.marberpp.myevents;

import it.marberpp.myevents.services.ServicesUtils;
import it.marberpp.myevents.services.pojo.GenericResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;



public class NetworkHelper {
	private static int timeoutMillis = 15000;


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
	public static GenericResponse doLogin(String username, String password){
		GenericResponse response = null;
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + "/mymeeting/eventservice?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_LOGIN 
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1")
		                 + "&" + ServicesUtils.HTTP_PARAM_PASSWORD + "="
		                 + URLEncoder.encode(password, "ISO-8859-1");
		}catch (Exception e) {
			throw new RuntimeException("Errore nella costruzione della richiesta");
		}
		

		try {
			String jsonResponse = getForecastXML(url);
			
			Type listType = new TypeToken<GenericResponse>(){}.getType();
			  
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			response = gsonConverter.fromJson(jsonResponse, listType);			
			
		} catch (IOException e) {
			response = new GenericResponse();
			response.setErrorCode(ServicesUtils.NET_ERROR_NETWORK_COMUNICATION);
		}
		
		
		return response;
	}
	

	
	//*********************************************
	public static GenericResponse createNewAccount(String username, String password){
		GenericResponse response = null;
		String url;
		try{
			url = "http://" + ServicesUtils.DNS_SERVER + ":" + ServicesUtils.IP_PORT_NUMBER + "/mymeeting/eventservice?" 
						 + ServicesUtils.HTTP_PARAM_OPERATION + "=" + ServicesUtils.HTTP_OP_NEW_ACCOUNT
						 + "&" + ServicesUtils.HTTP_PARAM_USERNAME + "="
		                 + URLEncoder.encode(username, "ISO-8859-1")
		                 + "&" + ServicesUtils.HTTP_PARAM_PASSWORD + "="
		                 + URLEncoder.encode(password, "ISO-8859-1");
		}catch (Exception e) {
			throw new RuntimeException("Errore nella costruzione della richiesta");
		}
		

		try {
			String jsonResponse = getForecastXML(url);
			
			Type listType = new TypeToken<GenericResponse>(){}.getType();
			  
			Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();
			response = gsonConverter.fromJson(jsonResponse, listType);			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return response;
	}
	
	
	
	//*********************************************
	/*
	public static String getEvents() throws IOException {
		String url = "http://10.37.131.4:8080/mymeeting/eventservice?op=getEvents";
		
		return getForecastXML(url);
	}
	*/
	

	
	//*********************************************
	//*********************************************
	//*********************************************
	public static String getForecastXML(String path) throws IOException {
		BufferedReader reader = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(timeoutMillis);
			conn.connect();
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
			if (reader != null) {
				reader.close();
			}

			if (conn != null) {
				conn.disconnect();
			}
			
		}
	}


}//class
