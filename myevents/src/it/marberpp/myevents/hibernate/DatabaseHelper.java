package it.marberpp.myevents.hibernate;




import it.marberpp.myevents.network.ServicesUtils;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mymeeting.exceptions.C_DatabaseException;
import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "myevents.db";
	private static final int SCHEMA_VERSION = 1;
	private static DatabaseHelper singleton = null;
	//private Context ctxt = null;

	//***************************************************
	public synchronized static DatabaseHelper getInstance(Context ctxt) {
		if (singleton == null) {
			singleton = new DatabaseHelper(ctxt.getApplicationContext());
		}
		return (singleton);
	}

	
	//***************************************************
	private DatabaseHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);
		//this.ctxt = ctxt;
	}

	//***************************************************
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			
			/*
			db.execSQL("CREATE TABLE 'account' ("
					+ "'ACN_ID' varchar(50) NOT NULL,"
					+ "'ACN_PASSWD' varchar(20) NOT NULL,"
					+ "'ACN_IMAGE_URL' varchar(500),"
					+ "'ACN_CREATION_DATE' datetime,"
					+ "PRIMARY KEY ('ACN_ID')"
					+ ");"
					);

			db.execSQL("CREATE TABLE 'group' ("
					+ "'GRP_ID' varchar(50) NOT NULL,"
					+ "'GRP_DESCRIPTION' varchar(500),"
					+ "'GRP_IMAGE_URL' varchar(500),"
					+ "'GRP_CREATION_DATE' datetime NOT NULL,"
					+ "'ACN_ID_OWNER' varchar(50) NOT NULL,"
					+ "PRIMARY KEY ('GRP_ID'),"
					+ "FOREIGN KEY ('ACN_ID_OWNER') REFERENCES 'account' ('ACN_ID')"
					+ ");"
					);
			*/
			
			db.execSQL("CREATE TABLE 'event' ("
					+ "'EVN_ID' varchar(200) NOT NULL,"
					+ "'EVN_DESCRIPTION' varchar(4000),"
					+ "'EVN_IMAGE_URL' varchar(500),"
					+ "'EVN_DATE' datetime,"
					+ "'EVN_CREATION_DATE' datetime,"
					+ "'GRP_ID' varchar(50),"
					+ "'ACN_ID_OWNER' varchar(50),"
					+ "'FLG_NEED_SYNC' tinyint(1),"
					+ "'LAST_UPDATE' datetime,"
					+ "'FLG_SHOWED' tinyint(1),"
					+ "'FLG_DELETED' tinyint(1),"
					+ "PRIMARY KEY ('EVN_ID'),"
					+ "FOREIGN KEY ('GRP_ID') REFERENCES 'group' ('GRP_ID'),"
					+ "FOREIGN KEY ('ACN_ID_OWNER') REFERENCES 'account' ('ACN_ID')  "
					+ ") ;"
					);

			db.execSQL("CREATE TABLE 'groups' ("
					+ "'GRP_ID' varchar(50) NOT NULL,"
					+ "'GRP_DESCRIPTION' varchar(500),"
					+ "'GRP_IMAGE_URL' varchar(500),"
					+ "'GRP_CREATION_DATE' datetime,"
					+ "'ACN_ID_OWNER' varchar(50) NOT NULL,"
					+ "'FLG_NEED_SYNC' tinyint(1),"
					+ "'LAST_UPDATE' datetime,"
					+ "'FLG_SHOWED' tinyint(1),"
					+ "'FLG_DELETED' tinyint(1),"
					+ "PRIMARY KEY ('GRP_ID'),"
					+ "FOREIGN KEY ('ACN_ID_OWNER') REFERENCES 'account' ('ACN_ID')  "
					+ ") ;"
					);

			
			/*

			
			//String[] args = { String.valueOf(position), note };
			db.execSQL("INSERT INTO 'account' ('ACN_ID', 'ACN_PASSWD', 'ACN_IMAGE_URL', 'ACN_CREATION_DATE') VALUES	"
					+ "('marber','marber','img','2013-05-20 21:29:49')	"
					);

			
			db.execSQL("INSERT INTO 'group' ('GRP_ID', 'GRP_DESCRIPTION', 'GRP_IMAGE_URL', 'GRP_CREATION_DATE', 'ACN_ID_OWNER') VALUES "
					+ "('grp_marber','gruppo di prova di mario bernardo','grp_image','2013-05-20 21:30:45','marber')"
					);

			
			
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('event marber','evento creato da mario bernardo','event_image','2013-05-31','2013-05-20 21:33:52','grp_marber','marber')"
					);

			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('secondo evento','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);

			
			
			
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 3','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 4','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 5','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 6','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 7','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 8','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 9','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 10','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 11','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 12','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 13','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 14','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 15','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 16','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 17','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 18','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 19','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 20','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 21','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			db.execSQL("INSERT INTO 'event' ('EVN_ID', 'EVN_DESCRIPTION', 'EVN_IMAGE_URL', 'EVN_DATE', 'EVN_CREATION_DATE', 'GRP_ID', 'ACN_ID_OWNER') VALUES "
					+ "('evento 22','descrizione dettagliata del secondo',NULL,'2013-05-30','2013-05-20 21:33:52','grp_marber','marber')"
					);
			*/
			
			
			db.setTransactionSuccessful();
		} catch (RuntimeException e){
			e.printStackTrace();
			throw e;
		} finally {
			db.endTransaction();
		}
	}

	//***************************************************
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(getClass().getSimpleName(), "CAMBIO versione");
	}

	
	
	
	
	
	// #################################################################################
	// #################################################################################


	//***************************************************
	public void resetDatabase(){
		SQLiteDatabase db = null;

		try{
			db = getReadableDatabase();
			
			db.beginTransaction();
			
			db.execSQL("delete from event");

			db.execSQL("delete from groups");
			
			db.setTransactionSuccessful();

		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(db != null){
				db.endTransaction();
			}
		}
	}
	
	
	//################# EVENTS ################################

	//***************************************************
	public List<Event> getFutureEvents(){
		return getEvents(false);
	}

	//***************************************************
	public List<Event> getPastEvents(){
		return getEvents(true);
	}
	
	//***************************************************
	private List<Event> getEvents(boolean oldEvents){
		List<Event> result = new ArrayList<Event>();
		Cursor c = null;
		
		try{
			String[] args = {ServicesUtils.dateFormatter.format(new Date(System.currentTimeMillis()))};
			String baseQuesry = "SELECT * FROM event";
			
			if(oldEvents){
				baseQuesry += " where EVN_DATE < ? ";
			} else {
				baseQuesry += " where EVN_DATE >= ? ";
			}
			
			baseQuesry += " order by EVN_DATE";
			c = getReadableDatabase().rawQuery(baseQuesry, args);
			c.moveToFirst();
			
			if ( !c.isAfterLast() ) {
				Event currEvent;
				do{
					currEvent = new Event();
	
					initEvent(c, currEvent);
					
					result.add(currEvent);
					
					c.moveToNext();
				}while(!c.isAfterLast());
			}

		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(c != null){
				c.close();
			}
		}
		
		ThreadUtilities.sleep(3000);

		return result;
	}
	
	//***************************************************
	public void updateEvents(List<Event> events){
		SQLiteDatabase db = null;

		try{
			db = getReadableDatabase();
			db.beginTransaction();
			
			Event currentEvent;
			for(int i = 0; i < events.size(); i++){
				currentEvent = events.get(i);
				String[] args = {
						currentEvent.getEvnId(),
						currentEvent.getEvnDescription(),
						currentEvent.getEvnImageUrl(),
						ServicesUtils.dateFormatter.format(currentEvent.getEvnDate()),
						ServicesUtils.dateFormatter.format(currentEvent.getEvnCreationDate()),
						currentEvent.getGroup().getGrpId(),
						currentEvent.getAccount().getAcnId(),
						"false",
						ServicesUtils.dateFormatter.format(currentEvent.getLastUpdate()),
						"false",
						ServicesUtils.extractToStringSave(currentEvent.getFlgDeleted())
						};
				
				db.execSQL("INSERT OR REPLACE INTO 'event' VALUES "
						+ "(?,?,?,?,?,?,?,?,?,?,?)", args
						);
			}//for i
			
			db.setTransactionSuccessful();
		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(db != null){
				db.endTransaction();
			}
		}

	}

	
	//***************************************************
	public String getLastUpdateForEvents(){
		String result = null;

		Cursor c = null;

		try{
			String[] args = {};
			c = getReadableDatabase().rawQuery("select max(LAST_UPDATE) as LAST_UPDATE from event", args);
			c.moveToFirst();
			
			if ( !c.isAfterLast() ) {
				result = c.getString(0);
			}
		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(c != null){
				c.close();
			}
		}
		
		return result;
	}
	
	
	
	//***************************************************
	public Event getEvent(String eventId){
		Event result = null;
		Cursor c = null;
		
		try{
			String[] args = {eventId};
			c = getReadableDatabase().rawQuery("SELECT * FROM event e WHERE evn_id = ?", args);
			c.moveToFirst();
			
			if ( !c.isAfterLast() ) {
				Event currEvent;
				currEvent = new Event();
	
				initEvent(c, currEvent);
				
				result = currEvent;
			}

		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(c != null){
				c.close();
			}
		}

		
		ThreadUtilities.sleep(3000);

		return result;
		
	}


	//***************************************************
	private void initEvent(Cursor c, Event currEvent){
		currEvent.setEvnCreationDate(new Date(c.getLong(c.getColumnIndex("EVN_CREATION_DATE") ) ) );
		currEvent.setEvnDate(new Date(c.getLong(c.getColumnIndex("EVN_DATE") ) ) );
		currEvent.setEvnDescription( c.getString(c.getColumnIndex("EVN_DESCRIPTION") ) );
		currEvent.setEvnId( c.getString(c.getColumnIndex("EVN_ID") ) );
		currEvent.setEvnImageUrl( c.getString(c.getColumnIndex("EVN_IMAGE_URL") ) );
		currEvent.setGroup(new Group());
		currEvent.getGroup().setGrpId(c.getString(c.getColumnIndex("GRP_ID") ) );
	}
		
	//################# END EVENTS ################################


	
	//################# GROUP ################################

	//***************************************************
	public void updateGroups(List<Group> groups){
		SQLiteDatabase db = null;

		try{
			db = getReadableDatabase();
			db.beginTransaction();
			
			Group currentGroup;
			for(int i = 0; i < groups.size(); i++){
				currentGroup = groups.get(i);
				String[] args = {
						currentGroup.getGrpId(),
						currentGroup.getGrpDescription(),
						currentGroup.getGrpImageUrl(),
						ServicesUtils.dateFormatter.format(currentGroup.getGrpCreationDate()),
						currentGroup.getAccount().getAcnId(),
						"false",
						ServicesUtils.dateFormatter.format(currentGroup.getLastUpdate()),
						"false",
						ServicesUtils.extractToStringSave(currentGroup.getFlgDeleted())
						};
				
				db.execSQL("INSERT OR REPLACE INTO 'groups' VALUES "
						+ "(?,?,?,?,?,?,?,?,?)", args
						);
			}//for i
			
			db.setTransactionSuccessful();
		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(db != null){
				db.endTransaction();
			}
		}

	}
	
	
	//***************************************************
	public List<Group> getGroups(String username, boolean owned){
		List<Group> result = new ArrayList<Group>();
		Cursor c = null;
		
		try{
			String[] args = {username};
			String baseQuesry = "SELECT * FROM groups ";
			
			if(owned){
				baseQuesry += " where ACN_ID_OWNER = ? ";
			} else {
				baseQuesry += " where ACN_ID_OWNER != ? ";
			}
			
			baseQuesry += " order by GRP_CREATION_DATE";

			c = getReadableDatabase().rawQuery(baseQuesry, args);
			c.moveToFirst();
			
			if ( !c.isAfterLast() ) {
				Group currGroup;
				do{
					currGroup = new Group();
	
					initGroup(c, currGroup);
					
					result.add(currGroup);
					
					c.moveToNext();
				}while(!c.isAfterLast());
			}

		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(c != null){
				c.close();
			}
		}
		
		ThreadUtilities.sleep(3000);

		return result;
	}
	
	
	//***************************************************
	public Group getGroup(String groupId){
		Group result = null;
		Cursor c = null;
		
		try{
			String[] args = {groupId};
			c = getReadableDatabase().rawQuery("SELECT * FROM groups g WHERE grp_id = ?", args);
			c.moveToFirst();
			
			if ( !c.isAfterLast() ) {
				Group currGroup;
				currGroup = new Group();
	
				initGroup(c, currGroup);
				
				result = currGroup;
			}

		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(c != null){
				c.close();
			}
		}

		
		ThreadUtilities.sleep(3000);

		return result;
		
	}


	//***************************************************
	public String getLastUpdateForGroups(){
		String result = null;

		Cursor c = null;

		try{
			String[] args = {};
			c = getReadableDatabase().rawQuery("select max(LAST_UPDATE) as LAST_UPDATE from groups", args);
			c.moveToFirst();
			
			if ( !c.isAfterLast() ) {
				result = c.getString(0);
			}
		} catch (Throwable ex){
			throw new C_DatabaseException(ex);
		} finally {
			if(c != null){
				c.close();
			}
		}
		
		return result;
	}
	
	
	//***************************************************
	private void initGroup(Cursor c, Group currGroup){
		currGroup.setGrpCreationDate(new Date(c.getLong(c.getColumnIndex("GRP_CREATION_DATE") ) ) );
		currGroup.setGrpDescription( c.getString(c.getColumnIndex("GRP_DESCRIPTION") ) );
		currGroup.setGrpId( c.getString(c.getColumnIndex("GRP_ID") ) );
		currGroup.setGrpImageUrl( c.getString(c.getColumnIndex("GRP_IMAGE_URL") ) );
		currGroup.setAccount(new Account());
		currGroup.getAccount().setAcnId(c.getString(c.getColumnIndex("ACN_ID_OWNER") ) );
	}
	//################# END GROUP ################################
	
	
}//main class
