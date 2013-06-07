package it.marberpp.myevents;

import it.marberpp.myevents.events.EventsListFragment;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.List;

import mymeeting.hibernate.pojo.Event;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;

import com.actionbarsherlock.app.SherlockFragment;


public class MainDataRetainFragment extends SherlockFragment {
	
	SparseArray<EventsListFragment> elfList = new SparseArray<EventsListFragment>();
	
	private boolean loginVerified = false;
	
	private List<Event> events = null;
	private LoadEventsTask loadEventsTask = null;
	private boolean eventsDelivered = false;
	
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
	public void setLoginVerified(boolean loginVerified) {
		
		if(this.loginVerified != loginVerified){
			if(this.loginVerified == false){
				this.events = null;
				this.eventsDelivered = false;
			}
			
			this.loginVerified = loginVerified;
			this.loadAndDeliveryData();
		}
	}

	
	//*********************************************
	synchronized private void loadAndDeliveryData(){
		if(prefs != null){
			if( !this.prefsDelivered ){
				((MainActivity) getActivity()).setPreferences(this.prefs);
				this.prefsDelivered = true;
			}
		} else {
			if(prefs == null && this.loadPrefsTask == null){
				this.loadPrefsTask = new LoadPrefsTask();
				ThreadUtilities.executeAsyncTask(this.loadPrefsTask, getActivity().getApplicationContext());
				return;
			}
			
		}	
		
		if( this.loginVerified ){
			if(events != null){
				if ( !this.eventsDelivered ){
					((MainActivity) getActivity()).setEvents(this.events);
					this.eventsDelivered = true;
				}
			} else {
				if(events == null && this.loadEventsTask == null){
					this.eventsDelivered = false;
					
					this.loadEventsTask = new LoadEventsTask();
					ThreadUtilities.executeAsyncTask(this.loadEventsTask, getActivity().getApplicationContext());
					return;
				}
			}
		}//if userLoggedIn
	}
	

	//*********************************************
	public synchronized void addEventsListFragment(EventsListFragment fragment, int position){
		this.elfList.put(position, fragment);
		
	}

	//*********************************************
	public synchronized EventsListFragment getEventsListFragment(int position){
		return this.elfList.get(position);
	}
	
	
	//#####################################################################
	//#####################################################################
	private class LoadEventsTask extends AsyncTask<Context, Void, Void> {
		private List<Event> eventsTmp = null;
		private Exception e = null;

		@Override
		protected Void doInBackground(Context... ctxt) {
			try {
				this.eventsTmp = DatabaseHelper.getInstance(getActivity()).getEvents();
			} catch (Exception e) {
				this.e = e;
			}
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			if (e == null) {
				if(MainDataRetainFragment.this == null){
					//succede quando l'applicazione viene chiusa prima che il task sia completato
					return;
				}
				MainDataRetainFragment.this.events = this.eventsTmp;
				MainDataRetainFragment.this.loadEventsTask = null;

				MainDataRetainFragment.this.loadAndDeliveryData();
			} else {
				Log.e(getClass().getSimpleName(), "Exception loading contents", e);
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
