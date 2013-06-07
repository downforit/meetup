package it.marberpp.myevents;

/*
 * 1) come fare ad ottenere i Fragment da un ViewPager
 * 
 * 
 * 
 */






import it.marberpp.myevents.events.EventsVPAdapter;
import it.marberpp.myevents.login.LoginAcrivity;
import it.marberpp.myevents.login.LoginFragment;
//import it.marberpp.myevents.R;

import java.util.List;

import mymeeting.hibernate.pojo.Event;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class MainActivity extends SherlockFragmentActivity {
	private static final String DATA_RETAIN_FRAGMENT_ID = "datafrag";

	public static final String PREFS_USERNAME = "username";
	public static final String PREFS_PASSWORD = "password";
	public static final String PREFS_LOGIN_VERIFIED = "log_ver";
	
	private SharedPreferences prefs = null;

	private ViewPager eventsVP = null;
	private EventsVPAdapter eventsVPAdapter = null;

	private MainDataRetainFragment dataRetain = null;
	
	
	private String username = null;
	private String password = null;
	private boolean loginVerified = false;
	private boolean logged = false;
	
	
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
		
		this.eventsVP = (ViewPager) this.findViewById(R.id.evnlViewPager);
		this.eventsVPAdapter = new EventsVPAdapter(this, dataRetain);

		this.eventsVP.setAdapter(this.eventsVPAdapter);		
		
		this.eventsVP.setCurrentItem(EventsVPAdapter.ID_EVENTS_LIST_FUTURES);
		
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
		new MenuInflater(this).inflate(R.menu.main_action_menu, menu);
		return(super.onCreateOptionsMenu(menu));
	}

	//***********************************************
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menuLogOut:
			this.logged = false;
			this.setLoginVerified(false);
			this.savePreferences();
			this.setEvents(null);
			this.verifyActivityStatus();

			return true;
		case android.R.id.home:
			Log.d(getClass().getSimpleName(), "#### tasto HOME premuto");
			
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
				this.username = data.getStringExtra(LoginFragment.PARAM_USERNAME);
				this.password = data.getStringExtra(LoginFragment.PARAM_PASSWORD);
				this.logged = true;
				this.setLoginVerified(true);
				this.setEvents(null);
				this.savePreferences();
			}
			break;
		}
	}
	
	
	//***********************************************
	//***********************************************
	//***********************************************

	
	
	//***********************************************
	public void setPreferences(SharedPreferences prefs){
		//this.eventsVPAdapter.setEvents(events);
		if(prefs != null){
			this.prefs = prefs;
			
			this.loadPreferences();
		}
		
		
	}

	
	//***********************************************
	private void loadPreferences(){
		if (prefs != null) {
			this.setLoginVerified(prefs.getBoolean(PREFS_LOGIN_VERIFIED,false));
			this.username = prefs.getString(PREFS_USERNAME, null);
			this.password = prefs.getString(PREFS_PASSWORD, null);

			this.verifyActivityStatus();
		}
		
	}
	
	//***********************************************
	private void savePreferences() {
		Editor edit = prefs.edit();
		edit.putBoolean(PREFS_LOGIN_VERIFIED, this.loginVerified);
		edit.putString(PREFS_USERNAME, this.username);
		edit.putString(PREFS_PASSWORD, this.password);
		edit.apply();
	}

	//***********************************************
	private void verifyActivityStatus(){
		if(!this.loginVerified){
			Intent intentTmp=new Intent(this, LoginAcrivity.class);
			intentTmp.putExtra(LoginFragment.PARAM_USERNAME, this.username);
			intentTmp.putExtra(LoginFragment.PARAM_PASSWORD, this.password);
			startActivityForResult(intentTmp, LoginAcrivity.ACTIVITY_ID);
		}		
	}
	
	//***********************************************
	public void setEvents(List<Event> events){
		//this.eventsVPAdapter.setEvents(events);
		
		Log.d(getClass().getSimpleName(), "##### START setEvents");
		if(this.dataRetain != null){
			if(this.dataRetain.getEventsListFragment(EventsVPAdapter.ID_EVENTS_LIST_FUTURES) == null){
				Log.d(getClass().getSimpleName(), "****************** attenzione: il fragment non è ancora pronto al completamento del dataretain");
			} else {
				this.dataRetain.getEventsListFragment(EventsVPAdapter.ID_EVENTS_LIST_FUTURES).setEvents(events);
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
	

}//class MainActivity
