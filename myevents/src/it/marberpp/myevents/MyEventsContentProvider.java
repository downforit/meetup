package it.marberpp.myevents;

import java.lang.reflect.Type;

import mymeeting.services.responses.ResponseEventsList;
import mymeeting.services.responses.ResponseGroupsList;
import mymeeting.services.responses.ResponseRAcnGrpsList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.network.ServicesUtils;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class MyEventsContentProvider extends ContentProvider {
	public static final int URI_TBL_LAST_UPDATE = 0;
	public static final int URI_TBL_EVENT = 1;
	public static final int URI_TBL_GROUP = 2;
	public static final int URI_TBL_RACNGRP = 3;
	

	private static final UriMatcher uriMatcher;
	 static {
	  uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  uriMatcher.addURI(MyEventsContract.AUTHORITY, MyEventsContract.TBL_LAST_UPDATE + "/*", URI_TBL_LAST_UPDATE);
	  uriMatcher.addURI(MyEventsContract.AUTHORITY, MyEventsContract.TBL_EVENT,       URI_TBL_EVENT);
	  uriMatcher.addURI(MyEventsContract.AUTHORITY, MyEventsContract.TBL_GROUP,       URI_TBL_GROUP);
	  uriMatcher.addURI(MyEventsContract.AUTHORITY, MyEventsContract.TBL_RACNGRP,       URI_TBL_RACNGRP);
	  //uriMatcher.addURI(MyEventrsContract.AUTHORITY, "countries/#", SINGLE_COUNTRY);
	 }

	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	
	//**********************************************
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		MatrixCursor cursor = null;
		
		switch (uriMatcher.match(uri)) {
		case URI_TBL_LAST_UPDATE:
			String lastUpdate = null;
			
			String id = uri.getPathSegments().get(1);
			if(id.equalsIgnoreCase(MyEventsContract.PARAM_LAST_UPDATE_FOR_EVENT)){
				lastUpdate = DatabaseHelper.getInstance(getContext()).getLastUpdateForEvents();
			} else if(id.equalsIgnoreCase(MyEventsContract.PARAM_LAST_UPDATE_FOR_GROUP)){
				lastUpdate = DatabaseHelper.getInstance(getContext()).getLastUpdateForGroups();
			} else if(id.equalsIgnoreCase(MyEventsContract.PARAM_LAST_UPDATE_FOR_RACNGRP)){
				lastUpdate = DatabaseHelper.getInstance(getContext()).getLastUpdateForRAcnGrp();
			} else {
				throw new IllegalArgumentException("Unsupported URI: " + uri);
			}
			
			cursor = new MatrixCursor(new String[]{"LAST_UPDATE"});
			if(lastUpdate != null){
				cursor.addRow(new Object[]{lastUpdate});
			}
			break;
		}
		
		return cursor;
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	
	//**********************************************
	@Override
	public Uri insert(Uri uri, ContentValues values) {

		Uri uriResult = null;
		String jsonResponse = values.getAsString(MyEventsContract.KEY_JSON_RESPONSE);
		Gson gsonConverter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(ServicesUtils.JSON_DATE_FORMAT).create();

		
		//SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case URI_TBL_EVENT:
		{
			Type listType = new TypeToken<ResponseEventsList>(){}.getType();
			ResponseEventsList response = gsonConverter.fromJson(jsonResponse, listType);			
			
			DatabaseHelper.getInstance(getContext()).updateEvents(response.getEvents());
			
			uriResult = Uri.parse(MyEventsContract.URI_DB_EVENT + "/0");
			
			break;
		}
		case URI_TBL_GROUP:
		{
			Type listType = new TypeToken<ResponseGroupsList>(){}.getType();
			ResponseGroupsList response = gsonConverter.fromJson(jsonResponse, listType);			
			
			DatabaseHelper.getInstance(getContext()).updateGroups(response.getGroups());

			uriResult = Uri.parse(MyEventsContract.URI_DB_GROUP + "/0");
			
			break;
		}
		case URI_TBL_RACNGRP:
		{
			Type listType = new TypeToken<ResponseRAcnGrpsList>(){}.getType();
			ResponseRAcnGrpsList response = gsonConverter.fromJson(jsonResponse, listType);			
			
			DatabaseHelper.getInstance(getContext()).updateRAcnGrp(response.getrAcnGrps());

			uriResult = Uri.parse(MyEventsContract.URI_DB_GROUP + "/0");
			
			break;
		}
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		
		return uriResult;
	}



	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
