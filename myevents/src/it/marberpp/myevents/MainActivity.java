package it.marberpp.myevents;

/*
 * 1) come fare ad ottenere i Fragment da un ViewPager
 * 
 * 
 * 
 */






import it.marberpp.myevents.events.EventNewActivity;
import it.marberpp.myevents.events.EventsListFragment;
import it.marberpp.myevents.groups.GroupNewActivity;
import it.marberpp.myevents.groups.GroupsListFragment;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.login.LoginAcrivity;
import it.marberpp.myevents.network.DBSynchronizer;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.List;

import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity extends SherlockFragmentActivity {
	private static final String DATA_RETAIN_FRAGMENT_ID = "datafrag";

	
	private SharedPreferences prefs = null;

	private ViewPager listsVP = null;
	private ListsVPAdapter listsVPAdapter = null;

	private MainDataRetainFragment dataRetain = null;
	
	
	private String username = null;
	private String password = null;
	private boolean loginVerified = false;
	public boolean logged = false;
	
	
	//***********************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.main_activity);

		//to give privileges to all member of the app
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		}		
		
		getSupportActionBar().setHomeButtonEnabled(true);// serve solo se si imposta un SKD maggiore di 11	
		
		
		dataRetain = (MainDataRetainFragment) getSupportFragmentManager().findFragmentByTag(DATA_RETAIN_FRAGMENT_ID);
		if (dataRetain == null) {
			dataRetain = new MainDataRetainFragment();
			getSupportFragmentManager().beginTransaction().add(dataRetain, DATA_RETAIN_FRAGMENT_ID).commit();
		}
		
		this.dataRetain.prefsNeedToBeDelivered();
		
		
		this.listsVP = (ViewPager) this.findViewById(R.id.evnlViewPager);
		this.listsVPAdapter = new ListsVPAdapter(this, dataRetain);
		this.listsVP.setAdapter(this.listsVPAdapter);		
		this.listsVP.setCurrentItem(ListsVPAdapter.ID_EVENTS_LIST_FUTURES);
		
	}

	
	
	//***********************************************
	@Override
	public void onResume() {
		super.onResume();

		this.loadPreferences();
	}	
	
	//***********************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.main_activity, menu);
		return(super.onCreateOptionsMenu(menu));
	}

	//***********************************************
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			Log.d(getClass().getSimpleName(), "#### tasto HOME premuto");
			
			return true;
		case R.id.menuLogOut:
			this.logged = false;
			this.setLoginVerified(false);
			this.savePreferences();
			this.emptyLists();
			this.verifyActivityStatus();

			return true;

		case R.id.menuSync:
			synchronizeDatabase();
			return true;

		case R.id.resetDb:
			resetDatabase();
			return true;
		case R.id.menuAdd:
			int currentPageId = this.listsVP.getCurrentItem();
			
			if(currentPageId == ListsVPAdapter.ID_EVENTS_LIST_FUTURES || currentPageId == ListsVPAdapter.ID_EVENTS_LIST_PAST){
				Intent intentTmp=new Intent(this, EventNewActivity.class);
				intentTmp.putExtra(MainLib.PARAM_USERNAME, this.username);
				startActivityForResult(intentTmp, EventNewActivity.ACTIVITY_ID);
			} else {
				Intent intentTmp=new Intent(this, GroupNewActivity.class);
				intentTmp.putExtra(MainLib.PARAM_USERNAME, this.username);
				startActivityForResult(intentTmp, GroupNewActivity.ACTIVITY_ID);
			}
			
			return true;
		}
		
		return (super.onOptionsItemSelected(item));
	}

	
	//***********************************************
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode){
		case LoginAcrivity.ACTIVITY_ID:
			if(resultCode == Activity.RESULT_CANCELED){
				finish();
			} else if(resultCode == Activity.RESULT_OK){
				String oldUsername = this.password;
				this.username = data.getStringExtra(MainLib.PARAM_USERNAME);
				this.password = data.getStringExtra(MainLib.PARAM_PASSWORD);
				
				if( !oldUsername.equals(this.username) ){
					Log.d(getClass().getSimpleName(), "##### RESET DATABASE");
					DatabaseHelper.getInstance(MainActivity.this).resetDatabase();
					//this.synchronizeDatabase();
				}
				
				this.dataRetain.setUsername(this.username);
				this.logged = true;
				this.setLoginVerified(true);
				this.emptyLists();
				this.savePreferences();
			}
			break;
		case EventNewActivity.ACTIVITY_ID:
			if(resultCode == Activity.RESULT_OK){
				this.dataRetain.reloadEvents();
			}
			
			break;
		case GroupNewActivity.ACTIVITY_ID:
			if(resultCode == Activity.RESULT_OK){
				this.dataRetain.reloadEvents();
			}
			
			break;
		}
	}
	
	
	//***********************************************
	//***********************************************
	//***********************************************

	
	public void synchronizeDatabase(){
		this.dataRetain.databaseSynchronizationStarted();
		
		ThreadUtilities.executeAsyncTask(new synkDbTask(this.username, synkDbTask.OP_SYNC), this);
	}

	//***********************************************
	public void resetDatabase(){
		

		this.dataRetain.databaseSynchronizationStarted();
		
		ThreadUtilities.executeAsyncTask(new synkDbTask(this.username, synkDbTask.OP_RESET), this);
	}
	
	
	//***********************************************
	public void setPreferences(SharedPreferences prefs){
		Log.d(getClass().getSimpleName(), "##### SET preferences");

		if(prefs != null){
			this.prefs = prefs;
			
			this.loadPreferences();
		}
		
		
	}

	
	//***********************************************
	private void loadPreferences(){
		Log.d(getClass().getSimpleName(), "##### load preferences");
		if (prefs != null) {
			this.setLoginVerified(prefs.getBoolean(MainLib.PREFS_LOGIN_VERIFIED,false));
			this.username = prefs.getString(MainLib.PREFS_USERNAME, null);
			this.password = prefs.getString(MainLib.PREFS_PASSWORD, null);

			this.dataRetain.setUsername(this.username);

			this.verifyActivityStatus();
		}
		
	}
	
	//***********************************************
	private void savePreferences() {
		if(prefs == null){
			Log.d(getClass().getSimpleName(), "##### PREFS = NULL");
			
			return;
		}
		Editor edit = prefs.edit();
		edit.putBoolean(MainLib.PREFS_LOGIN_VERIFIED, this.loginVerified);
		edit.putString(MainLib.PREFS_USERNAME, this.username);
		edit.putString(MainLib.PREFS_PASSWORD, this.password);
		edit.apply();
	}

	//***********************************************
	private void verifyActivityStatus(){
		if(!this.loginVerified){
			Intent intentTmp=new Intent(this, LoginAcrivity.class);
			intentTmp.putExtra(MainLib.PARAM_USERNAME, this.username);
			intentTmp.putExtra(MainLib.PARAM_PASSWORD, this.password);
			startActivityForResult(intentTmp, LoginAcrivity.ACTIVITY_ID);
		}		
	}


	//***********************************************
	public void emptyLists(){
		this.setEvents(null, ListsVPAdapter.ID_EVENTS_LIST_FUTURES);
		this.setEvents(null, ListsVPAdapter.ID_EVENTS_LIST_PAST);
	}

	
	
	
	
	//***********************************************
	public void setFutureEvents(List<Event> events){
		this.setEvents(events, ListsVPAdapter.ID_EVENTS_LIST_FUTURES);
	}
	
	//***********************************************
	public void setPastEvents(List<Event> events){
		this.setEvents(events, ListsVPAdapter.ID_EVENTS_LIST_PAST);
	}

	//***********************************************
	public void setEvents(List<Event> events, int FragmentID){
		//this.eventsVPAdapter.setEvents(events);
		
		Log.d(getClass().getSimpleName(), "##### START setEvents");
		if(this.dataRetain != null){
			if(this.dataRetain.getListFragment(FragmentID) == null){
				Log.d(getClass().getSimpleName(), "****************** attenzione: il fragment non è ancora pronto al completamento del dataretain");
			} else {
				((EventsListFragment)this.dataRetain.getListFragment(FragmentID)).setEvents(events);
			}
		} else {
			Log.d(getClass().getSimpleName(), "DATARETAIN uguale a null");
		}
	}
	

	
	
	//***********************************************
	public void setOwnedGroups(List<Group> ownedGroups){
		this.setGroups(ownedGroups, ListsVPAdapter.ID_GROUPS_LIST_OWNED);
	}
	
	//***********************************************
	public void setOtherGroups(List<Group> otherGroups){
		this.setGroups(otherGroups, ListsVPAdapter.ID_GROUPS_LIST_OTHER);
	}

	//***********************************************
	public void setGroups(List<Group> groups, int FragmentID){
		//this.eventsVPAdapter.setEvents(events);
		
		Log.d(getClass().getSimpleName(), "##### START setEvents");
		if(this.dataRetain != null){
			if(this.dataRetain.getListFragment(FragmentID) == null){
				Log.d(getClass().getSimpleName(), "****************** attenzione: il fragment non è ancora pronto al completamento del dataretain");
			} else {
				((GroupsListFragment)this.dataRetain.getListFragment(FragmentID)).setGroups(groups);
			}
		} else {
			Log.d(getClass().getSimpleName(), "DATARETAIN uguale a null");
		}
	}
	
	
	
	
	//***********************************************
	public void setLoginVerified(boolean loginVerified){
		this.loginVerified = loginVerified;
		this.dataRetain.setLoginVerified(loginVerified);
	}

	
	

	//***********************************************
	//***********************************************
	//***********************************************

	//#####################################################################
	private class synkDbTask extends AsyncTask<Context, Void, Void> {
		public static final int OP_SYNC = 0;
		public static final int OP_RESET = 1;
		
		String username;
		int operation;
		
		Throwable exception = null;
		
		
		public synkDbTask(String username, int operation){
			this.username = username;
			this.operation = operation;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			try{
				switch (this.operation){
				case OP_SYNC:
					DBSynchronizer.syncDb(MainActivity.this, this.username);
					break;
				case OP_RESET:
					DatabaseHelper.getInstance(MainActivity.this).resetDatabase();
					break;
				}
			} catch(Throwable ex){
				this.exception = ex;
			}
			
			return null;
		}

		@Override
		public void onPostExecute(Void arg0) {
			if(this.exception != null){
				ExceptionsUtils.standardManagingException(this.exception, MainActivity.this);
			}
			
			MainActivity.this.dataRetain.databaseSynchronizationCompleted();
		}
	}// class PrefsLoadTask
	
	
	
}//class MainActivity
