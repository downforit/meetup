package it.marberpp.myevents.events;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class EventActivity extends SherlockFragmentActivity {
	
	
	//*********************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		if (getSupportFragmentManager().findFragmentById(android.R.id.content)==null) {
			String eventId = getIntent().getStringExtra(EventFragment.PARAM_EVENT_ID);
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, EventFragment.newInstance(eventId)).commit();
	    }
	
		getSupportActionBar().setHomeButtonEnabled(true);// serve solo se si imposta un SKD maggiore di 11	

	}
	

	
	//*********************************************
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			
			return true;
		}
		
		return (super.onOptionsItemSelected(item));
	}
	
	
}
