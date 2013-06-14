package it.marberpp.myevents.network;

import it.marberpp.myevents.hibernate.DatabaseHelper;

import java.util.List;

import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import mymeeting.hibernate.pojo.RAcnGrp;
import android.content.Context;
import android.util.Log;

public class DBSynchronizer {

	//***************************************************
	public static void syncDb(Context ctxt, String username){
		syncEvents(ctxt, username);
		syncGroups(ctxt, username);
		syncRAcnGrp(ctxt, username);
	}
	
	
	//***************************************************
	public static void syncEvents(Context ctxt, String username){
		
		String lastUpdate = DatabaseHelper.getInstance(ctxt).getLastUpdateForEvents();
		
		List<Event> eventsToUpdate = NetworkHelper.getEventsToSync(username, lastUpdate);

		if(eventsToUpdate == null || eventsToUpdate.size() == 0){
			return;
		}
		
		//Log.d(DBSynchronizer.class.getSimpleName(), "LastUpdate del primo evento " + eventsToUpdate.get(0).getLastUpdate().toString());
		
		DatabaseHelper.getInstance(ctxt).updateEvents(eventsToUpdate);
		
		//Log.d(DBSynchronizer.class.getSimpleName(), "Fine sincronizzazione");
	}

	
	//***************************************************
	public static void syncGroups(Context ctxt, String username){
		
		String lastUpdate = DatabaseHelper.getInstance(ctxt).getLastUpdateForGroups();
		
		List<Group> groupsToUpdate = NetworkHelper.getGroupsToSync(username, lastUpdate);

		if(groupsToUpdate == null || groupsToUpdate.size() == 0){
			return;
		}
		
		//Log.d(DBSynchronizer.class.getSimpleName(), "LastUpdate del primo evento " + eventsToUpdate.get(0).getLastUpdate().toString());
		
		DatabaseHelper.getInstance(ctxt).updateGroups(groupsToUpdate);
		
		Log.d(DBSynchronizer.class.getSimpleName(), "Fine sincronizzazione gruppi");
	}
	

	//***************************************************
	public static void syncRAcnGrp(Context ctxt, String username){
		
		String lastUpdate = DatabaseHelper.getInstance(ctxt).getLastUpdateForRAcnGrp();

		List<RAcnGrp> rAcnGrpsToUpdate = NetworkHelper.getRAcnGrpsToSync(username, lastUpdate);

		if(rAcnGrpsToUpdate == null || rAcnGrpsToUpdate.size() == 0){
			return;
		}
		
		//Log.d(DBSynchronizer.class.getSimpleName(), "LastUpdate del primo evento " + eventsToUpdate.get(0).getLastUpdate().toString());
		
		DatabaseHelper.getInstance(ctxt).updateRAcnGrp(rAcnGrpsToUpdate);
		
		Log.d(DBSynchronizer.class.getSimpleName(), "Fine sincronizzazione RAcnGrp");
	}
	
	
}
