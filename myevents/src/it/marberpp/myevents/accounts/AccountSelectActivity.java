package it.marberpp.myevents.accounts;

import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import it.marberpp.myevents.MainDataRetainFragment;
import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.events.EventsListFragment;
import it.marberpp.myevents.utils.GenericFragmentInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class AccountSelectActivity extends SherlockFragmentActivity implements GenericFragmentInterface {
	public static final int ACTIVITY_ID = 1240;
	
	
	String groupId;
	
	//*********************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AccountSelectFragment accountSelectFragment =(AccountSelectFragment) getSupportFragmentManager().findFragmentById(android.R.id.content); 
		if (accountSelectFragment==null) {
			this.groupId = getIntent().getStringExtra(MainLib.PARAM_GROUP_ID);
			accountSelectFragment = AccountSelectFragment.newInstance(this.groupId);
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, accountSelectFragment).commit();
	    }
	
		accountSelectFragment.setGenericListener(this);
		//this.groupsListFragment.setGroups(MainDataRetainFragment.staticInstance.getAllGroups());
		
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

	
	//***************************************************
	@Override
	public void onFragmentResult(String fragmentName, Object data) {
		if(AccountSelectFragment.class.getSimpleName().equals(fragmentName)){
			

			/*
			Intent i = getIntent();
			i.putExtra(MainLib.PARAM_GROUP_ID, selectedAccount.getAcnId());
			this.setResult(SherlockFragmentActivity.RESULT_OK, i);
			finish();
			*/
			
			this.setResult(SherlockFragmentActivity.RESULT_OK);
			finish();
		}
	}

	
	
	
	
	
}
