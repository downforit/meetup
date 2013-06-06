package it.marberpp.myevents.hibernate;



import it.marberpp.myevents.hibernate.pojo.Event;
import it.marberpp.myevents.hibernate.pojo.Group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
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
			
			db.execSQL("CREATE TABLE 'event' ("
					+ "'EVN_ID' varchar(200) NOT NULL,"
					+ "'EVN_DESCRIPTION' varchar(4000),"
					+ "'EVN_IMAGE_URL' varchar(500),"
					+ "'EVN_DATE' datetime,"
					+ "'EVN_CREATION_DATE' datetime,"
					+ "'GRP_ID' varchar(50),"
					+ "'ACN_ID_OWNER' varchar(50),"
					+ "PRIMARY KEY ('EVN_ID'),"
					+ "FOREIGN KEY ('ACN_ID_OWNER') REFERENCES 'account' ('ACN_ID'),"
					+ "FOREIGN KEY ('GRP_ID') REFERENCES 'group' ('GRP_ID')  "
					+ ") ;"
					);

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
	public List<Event> getEvents(){
		List<Event> result = new ArrayList<Event>();
		
		//*/
		String[] args = {};
		Cursor c = getReadableDatabase().rawQuery("SELECT * FROM event", args);
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
		//c.getString(0)
		
		//TODO: verificare se occorre invocare il metodo close in caso di eccezione
		c.close();
		SystemClock.sleep(3000);
		//*/

		
		return result;
	}
	

	
	
	//***************************************************
	public Event getEvent(String eventId){
		Event result = null;
		
		//*/
		String[] args = {eventId};
		Cursor c = getReadableDatabase().rawQuery("SELECT * FROM event e WHERE evn_id = ?", args);
		c.moveToFirst();
		
		if ( !c.isAfterLast() ) {
			Event currEvent;
			currEvent = new Event();

			initEvent(c, currEvent);
			
			result = currEvent;

		}
		//c.getString(0)
		
		//TODO: verificare se occorre invocare il metodo close in caso di eccezione
		c.close();
		SystemClock.sleep(3000);
		//*/

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
		
	
	
}//main class
