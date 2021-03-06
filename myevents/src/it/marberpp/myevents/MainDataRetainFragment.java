package it.marberpp.myevents;

import it.marberpp.myevents.events.EventsListFragment;
import it.marberpp.myevents.groups.GroupsListFragment;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;

import com.actionbarsherlock.app.SherlockFragment;


public class MainDataRetainFragment extends SherlockFragment {
	//private static final int DATA_TYPE_PREFS = 0;
	//private static final int DATA_TYPE_FUTURE_EVENTS = 1;
	//private static final int DATA_TYPE_PAST_EVENTS = 2;
	//private static final int DATA_TYPE_OWNED_GROUPS = 3;
	//private static final int DATA_TYPE_OTHER_GROUPS = 4;
	
	
	public static MainDataRetainFragment staticInstance = null;
	
	SparseArray<ListFragment> elfList = new SparseArray<ListFragment>();
	
	private boolean loginVerified = false;
	private String username;
	
	private boolean synchronizerRunning = false;

	
	
	private List<Event> pastEvents = null;
	private LoadEventsTask loadPastEventsTask = null;
	private boolean pastEventsDelivered = false;

	private List<Event> futureEvents = null;
	private LoadEventsTask loadFutureEventsTask = null;
	private boolean futureEventsDelivered = false;

	
	
	private List<Group> ownedGroups = null;
	private LoadEventsTask loadOwnedGroupsTask = null;
	private boolean ownedGroupsDelivered = false;

	private List<Group> otherGroups = null;
	private LoadEventsTask loadOtherGroupsTask = null;
	private boolean otherGroupsDelivered = false;

	
	
	private SharedPreferences prefs = null;
	private LoadPrefsTask loadPrefsTask = null;
	private boolean prefsDelivered = false;
	


	//*********************************************
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		
		loadAndDeliveryData();
	}
	
	
	//*********************************************
	synchronized private void loadAndDeliveryData(){
		
		if(this.synchronizerRunning){
			return;
		}
		
		if(prefs != null){
			if( !this.prefsDelivered ){
				this.prefsDelivered = true;//deve essere settato prima per evitare che la schedulazione del secondo thread faccia effettuare il delivery due volte
				((MainActivity) getActivity()).setPreferences(this.prefs);
			}
		} else {
			if(prefs == null && this.loadPrefsTask == null){
				this.prefsDelivered = false;

				this.loadPrefsTask = new LoadPrefsTask();
				ThreadUtilities.executeAsyncTask(this.loadPrefsTask, getActivity().getApplicationContext());
				
				return;
			}
			
		}	
		
		if( this.loginVerified ){
			if(futureEvents != null){
				if ( !this.futureEventsDelivered ){
					this.futureEventsDelivered = true;
					((MainActivity) getActivity()).setFutureEvents(futureEvents);
				}
			} else {
				if(futureEvents == null && this.loadFutureEventsTask == null){
					this.futureEventsDelivered = false;
					
					this.loadFutureEventsTask = new LoadEventsTask(ListsVPAdapter.ID_EVENTS_LIST_FUTURES, this.username);
					ThreadUtilities.executeAsyncTask(this.loadFutureEventsTask, getActivity().getApplicationContext());
					return;
				}
			}

			if(pastEvents != null){
				if ( !this.pastEventsDelivered ){
					this.pastEventsDelivered = true;
					((MainActivity) getActivity()).setPastEvents(pastEvents);
				}
			} else {
				if(pastEvents == null && this.loadPastEventsTask == null){
					this.pastEventsDelivered = false;
					
					this.loadPastEventsTask = new LoadEventsTask(ListsVPAdapter.ID_EVENTS_LIST_PAST, this.username);
					ThreadUtilities.executeAsyncTask(this.loadPastEventsTask, getActivity().getApplicationContext());
					return;
				}
			}

			
			if(this.username != null){
				if(ownedGroups != null){
					if ( !this.ownedGroupsDelivered ){
						this.ownedGroupsDelivered = true;
						((MainActivity) getActivity()).setOwnedGroups(ownedGroups);
					}
				} else {
					if(ownedGroups == null && this.loadOwnedGroupsTask == null){
						this.ownedGroupsDelivered = false;
						
						this.loadOwnedGroupsTask = new LoadEventsTask(ListsVPAdapter.ID_GROUPS_LIST_OWNED, this.username);
						ThreadUtilities.executeAsyncTask(this.loadOwnedGroupsTask, getActivity().getApplicationContext());
						return;
					}
				}
	
				
				if(otherGroups != null){
					if ( !this.otherGroupsDelivered ){
						this.otherGroupsDelivered = true;
						((MainActivity) getActivity()).setOtherGroups(otherGroups);
					}
				} else {
					if(otherGroups == null && this.loadOtherGroupsTask == null){
						this.otherGroupsDelivered = false;
						
						this.loadOtherGroupsTask = new LoadEventsTask(ListsVPAdapter.ID_GROUPS_LIST_OTHER, this.username);
						ThreadUtilities.executeAsyncTask(this.loadOtherGroupsTask, getActivity().getApplicationContext());
						return;
					}
				}
			}//if username
			
			
		}//if userLoggedIn
	}
	

	//*********************************************
	public void setUsername(String username){
		this.username = username;
		this.loadAndDeliveryData();
	}
	
	
	//*********************************************
	public void refreshLists(){
		this.futureEventsDelivered = false;
		this.pastEventsDelivered = false;
		this.ownedGroupsDelivered = false;
		this.otherGroupsDelivered = false;
		this.loadAndDeliveryData();
	}
	
	//*********************************************
	public void invalidateData(){
		this.futureEvents = null;
		this.futureEventsDelivered = false;
	
		this.pastEvents = null;
		this.pastEventsDelivered = false;

		this.ownedGroups = null;
		this.ownedGroupsDelivered = false;

		this.otherGroups = null;
		this.otherGroupsDelivered = false;
	}
	
	
	//*********************************************
	public void setLoginVerified(boolean loginVerified) {
		
		if(this.loginVerified != loginVerified){
			if(this.loginVerified == false){
				invalidateData();
			}
			
			this.loginVerified = loginVerified;
			this.loadAndDeliveryData();
		}
	}


	//*********************************************
	public synchronized void reloadLists(){
		databaseSynchronizationStarted();
		databaseSynchronizationCompleted();
	}
	
	
	//*********************************************
	public synchronized void databaseSynchronizationStarted(){
		
		this.synchronizerRunning = true;
		for(int i = 0; i < elfList.size(); i++){
			if(this.elfList.get(i) != null){
				if(i == ListsVPAdapter.ID_EVENTS_LIST_FUTURES || i == ListsVPAdapter.ID_EVENTS_LIST_PAST){
					((EventsListFragment)this.elfList.get(i)).setEvents(null);
				} else {
					((GroupsListFragment)this.elfList.get(i)).setGroups(null);
				}
			}
		}//for i
		
	}

	//*********************************************
	public synchronized void databaseSynchronizationCompleted(){
		this.invalidateData();
		
		this.synchronizerRunning = false;
		this.loadAndDeliveryData();
	}
	
	
	//*********************************************
	public synchronized void prefsNeedToBeDelivered(){
		this.prefsDelivered = false;
	}
	
	

	//*********************************************
	public synchronized void addListFragment(ListFragment fragment, int position){
		this.elfList.put(position, fragment);
		
		switch(position){
		case ListsVPAdapter.ID_EVENTS_LIST_FUTURES:
			if(futureEvents != null){
				((EventsListFragment)fragment).setEvents(futureEvents);
			}
			break;
		case ListsVPAdapter.ID_EVENTS_LIST_PAST:
			if(pastEvents != null){
				((EventsListFragment)fragment).setEvents(pastEvents);
			}
			break;		
		case ListsVPAdapter.ID_GROUPS_LIST_OWNED:
			if(ownedGroups != null){
				((GroupsListFragment)fragment).setGroups(ownedGroups);
			}
			break;		
		case ListsVPAdapter.ID_GROUPS_LIST_OTHER:
			if(otherGroups != null){
				((GroupsListFragment)fragment).setGroups(otherGroups);
			}
			break;		
		}//switch
	}
	
		
	//*********************************************
	public synchronized ListFragment getListFragment(int position){
		return this.elfList.get(position);
	}
	
	//*********************************************
	public synchronized void removeListFragment(ListFragment fragment, int position){
		this.elfList.remove(position);
	}

	
	//*********************************************
	public List<Group> getAllGroups(){
		List<Group> result = new ArrayList<Group>();
		
		result.addAll(this.ownedGroups);
		result.addAll(this.otherGroups);
		
		return result;
	}
	
	
	
	//*********************************************
	private void eventsLoaded(List<Event> futureEvents, List<Event> pastEvents){
		if(futureEvents != null){
			this.futureEvents = futureEvents;
			this.loadFutureEventsTask = null;
		}
		
		if(pastEvents != null){
			this.pastEvents = pastEvents;
			this.loadPastEventsTask = null;
		}

		this.loadAndDeliveryData();
	}
	

	//*********************************************
	private void groupsLoaded(List<Group> ownedGroups, List<Group> otherGroups){
		if(ownedGroups != null){
			this.ownedGroups = ownedGroups;
			this.loadOwnedGroupsTask = null;
		}
		
		if(otherGroups != null){
			this.otherGroups = otherGroups;
			this.loadOtherGroupsTask = null;
		}

		this.loadAndDeliveryData();
	}

	
	//*********************************************
	public void addEvent(Event event){
		Date now = new Date(System.currentTimeMillis());
		if(event.getEvnDate().before(now)){
			this.pastEvents.add(event);
			((EventsListFragment)this.elfList.get(ListsVPAdapter.ID_EVENTS_LIST_PAST)).addEvent(event);
		} else {
			this.futureEvents.add(event);
			((EventsListFragment)this.elfList.get(ListsVPAdapter.ID_EVENTS_LIST_FUTURES)).addEvent(event);
		}
	}
	
	
	//*********************************************
	public void addOwnedGroup(Group group){
		this.ownedGroups.add(group);
		GroupsListFragment listFragment = (GroupsListFragment)this.elfList.get(ListsVPAdapter.ID_GROUPS_LIST_OWNED);
	}
	
	
	//*********************************************
	public void reloadOwnedGroupsSynchronous(){
		this.ownedGroups = DatabaseHelper.getInstance(getActivity()).getGroups(this.username, true);
		this.ownedGroupsDelivered = false;
		this.loadAndDeliveryData();
	}
	

	//#####################################################################
	//#####################################################################
	private class LoadEventsTask extends AsyncTask<Context, Void, Void> {
		private List<Event> eventsTmp = null;
		private List<Group> groupTmp = null;
		

		private Exception exception = null;

		private int dataType;
		private String username;
		
		public LoadEventsTask(int dataType, String username){
			this.dataType = dataType;
			this.username = username;
			//Log.e(getClass().getSimpleName(), ">>>>>>>>> thread type = " + this.dataType);
		}
		
		@Override
		protected Void doInBackground(Context... ctxt) {
			try {
				switch(dataType){
				case ListsVPAdapter.ID_EVENTS_LIST_FUTURES:
					this.eventsTmp = DatabaseHelper.getInstance(getActivity()).getFutureEvents();
					break;
				case ListsVPAdapter.ID_EVENTS_LIST_PAST:
					this.eventsTmp = DatabaseHelper.getInstance(getActivity()).getPastEvents();
					break;
				case ListsVPAdapter.ID_GROUPS_LIST_OWNED:
					this.groupTmp = DatabaseHelper.getInstance(getActivity()).getGroups(this.username, true);
					break;
				case ListsVPAdapter.ID_GROUPS_LIST_OTHER:
					this.groupTmp = DatabaseHelper.getInstance(getActivity()).getGroups(this.username, false);
					break;
				}//switch
				
				
			} catch (Exception e) {
				this.exception = e;
			}
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			if (exception == null) {
				if(MainDataRetainFragment.this == null){
					//succede quando l'applicazione viene chiusa prima che il task sia completato
					return;
				}
				
				//Log.e(getClass().getSimpleName(), "<<<<<<< thread type = " + this.dataType);

				switch(dataType){
				case ListsVPAdapter.ID_EVENTS_LIST_FUTURES:
					MainDataRetainFragment.this.eventsLoaded(this.eventsTmp, null);
					break;
				case ListsVPAdapter.ID_EVENTS_LIST_PAST:
					MainDataRetainFragment.this.eventsLoaded(null, this.eventsTmp);
					break;
				case ListsVPAdapter.ID_GROUPS_LIST_OWNED:
					MainDataRetainFragment.this.groupsLoaded(this.groupTmp, null);
					break;
				case ListsVPAdapter.ID_GROUPS_LIST_OTHER:
					MainDataRetainFragment.this.groupsLoaded(null, this.groupTmp);
					break;
				}//switch
				
			} else {
				ExceptionsUtils.standardManagingException(this.exception, getActivity());
			}
		}
	}// class
	

	//#####################################################################
	private class LoadPrefsTask extends AsyncTask<Context, Void, Void> {
		SharedPreferences prefsTmp = null;

		@Override
		protected Void doInBackground(Context... ctxt) {
			
			prefsTmp = PreferenceManager.getDefaultSharedPreferences(ctxt[0]);
			prefsTmp.getAll();
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			MainDataRetainFragment.this.prefs = prefsTmp;
			MainDataRetainFragment.this.loadPrefsTask = null;

			MainDataRetainFragment.this.loadAndDeliveryData();
		}
	}// class PrefsLoadTask




}//class
