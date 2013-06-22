package it.marberpp.myevents.network;

import it.marberpp.myevents.interfaces.MyEventsContract;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DBSynchronizer {

	//***************************************************
	public static void syncDb(Context ctxt, String username, ContentResolver contentResolver){
		syncEvents(ctxt, username, contentResolver);
		syncGroups(ctxt, username, contentResolver);
		syncRAcnGrp(ctxt, username, contentResolver);
	}
	
	
	//***************************************************
	public static void syncEvents(Context ctxt, String username, ContentResolver contentResolver){
		

		/*
		String lastUpdate = DatabaseHelper.getInstance(ctxt).getLastUpdateForEvents();		 *
		 
		List<Event> eventsToUpdate = NetworkHelper.getEventsToSync(username, lastUpdate);
		if(eventsToUpdate == null || eventsToUpdate.size() == 0){
			return;
		}
		
		DatabaseHelper.getInstance(ctxt).updateEvents(eventsToUpdate);
		*/

		String lastUpdate = retriveLastUpdate(MyEventsContract.PARAM_LAST_UPDATE_FOR_EVENT, contentResolver);

		String jsonResponse = NetworkHelper.getEventsToSyncJson(username, lastUpdate);
		ContentValues contentValues = new ContentValues();
		contentValues.put(MyEventsContract.KEY_JSON_RESPONSE, jsonResponse);

		contentResolver.insert(MyEventsContract.URI_DB_EVENT, contentValues);

	}

	
	//***************************************************
	public static void syncGroups(Context ctxt, String username, ContentResolver contentResolver){
		/*
		String lastUpdate = DatabaseHelper.getInstance(ctxt).getLastUpdateForGroups();
		
		List<Group> groupsToUpdate = NetworkHelper.getGroupsToSync(username, lastUpdate);
		if(groupsToUpdate == null || groupsToUpdate.size() == 0){
			return;
		}
		
		DatabaseHelper.getInstance(ctxt).updateGroups(groupsToUpdate);
		*/


		String lastUpdate = retriveLastUpdate(MyEventsContract.PARAM_LAST_UPDATE_FOR_GROUP, contentResolver);

		String jsonResponse = NetworkHelper.getGroupsToSyncJson(username, lastUpdate);
		ContentValues contentValues = new ContentValues();
		contentValues.put(MyEventsContract.KEY_JSON_RESPONSE, jsonResponse);

		contentResolver.insert(MyEventsContract.URI_DB_GROUP, contentValues);
	
	}
	

	//***************************************************
	public static void syncRAcnGrp(Context ctxt, String username, ContentResolver contentResolver){

		/*
		String lastUpdate = DatabaseHelper.getInstance(ctxt).getLastUpdateForRAcnGrp();

		List<RAcnGrp> rAcnGrpsToUpdate = NetworkHelper.getRAcnGrpsToSync(username, lastUpdate);
		if(rAcnGrpsToUpdate == null || rAcnGrpsToUpdate.size() == 0){
			return;
		}
		
		DatabaseHelper.getInstance(ctxt).updateRAcnGrp(rAcnGrpsToUpdate);
		*/

		String lastUpdate = retriveLastUpdate(MyEventsContract.PARAM_LAST_UPDATE_FOR_RACNGRP, contentResolver);

		String jsonResponse = NetworkHelper.getRAcnGrpsToSyncJson(username, lastUpdate);

		ContentValues contentValues = new ContentValues();
		contentValues.put(MyEventsContract.KEY_JSON_RESPONSE, jsonResponse);
		contentResolver.insert(MyEventsContract.URI_DB_RACNGRP, contentValues);
	
	}

	
	
	
	
	//***************************************************
	private static String retriveLastUpdate(String paramLastUpdate, ContentResolver contentResolver){
		String lastUpdate = null;
		Uri uri = Uri.parse(MyEventsContract.URI_DB_LAST_UPDATE.toString() + "/" + paramLastUpdate);
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		cursor.moveToFirst();
		if ( !cursor.isAfterLast() ) {
			lastUpdate = cursor.getString(0);
		}
		
		return lastUpdate;
	}
	
	
	
	
}
