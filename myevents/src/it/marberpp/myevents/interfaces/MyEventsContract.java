package it.marberpp.myevents.interfaces;

import mymeeting.hibernate.pojo.Event;
import android.content.ContentValues;
import android.net.Uri;

public class MyEventsContract {
	public static final String AUTHORITY = "it.marber.myevents.provider";
	

	public static final String TBL_LAST_UPDATE = "lastupdate";
	public static final String TBL_EVENT = "event";
	public static final String TBL_GROUP = "groups";
	public static final String TBL_RACNGRP = "r_acn_grp";

	public static final String PARAM_LAST_UPDATE_FOR_EVENT = "evn";
	public static final String PARAM_LAST_UPDATE_FOR_GROUP = "grp";
	public static final String PARAM_LAST_UPDATE_FOR_RACNGRP = "racngrp";
	
	
	public static final Uri URI_DB_LAST_UPDATE = Uri.parse("content://" + AUTHORITY + "/" + TBL_LAST_UPDATE);
	public static final Uri URI_DB_EVENT = Uri.parse("content://" + AUTHORITY + "/" + TBL_EVENT);
	public static final Uri URI_DB_GROUP = Uri.parse("content://" + AUTHORITY + "/" + TBL_GROUP);
	public static final Uri URI_DB_RACNGRP = Uri.parse("content://" + AUTHORITY + "/" + TBL_RACNGRP);
	
	
	
	public static final String KEY_JSON_RESPONSE = "JSON_RESP";
}
