package it.marberpp.myevents.groups;

import mymeeting.hibernate.pojo.Group;
import it.marberpp.myevents.MainDataRetainFragment;
import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.utils.GenericFragmentInterface;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class GroupSelectActivity extends SherlockFragmentActivity implements GenericFragmentInterface {
	public static final int ACTIVITY_ID = 1140;
	
	GroupsListFragment groupsListFragment = null;
	
	//*********************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.groupsListFragment = (GroupsListFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		
		if (this.groupsListFragment==null) {
			this.groupsListFragment = GroupsListFragment.newInstance(GroupsListFragment.PAGE_ID_SELECTION_MODE);
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, this.groupsListFragment).commit();
	    }
	
		this.groupsListFragment.setGenericListener(this);
		this.groupsListFragment.setGroups(MainDataRetainFragment.staticInstance.getAllGroups());
		
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
		if(GroupsListFragment.class.getSimpleName().equals(fragmentName)){
			Group selectedGroup = (Group) data;
			
			Intent i = getIntent();
			i.putExtra(MainLib.PARAM_GROUP_ID, selectedGroup.getGrpId());
			this.setResult(SherlockFragmentActivity.RESULT_OK, i);
			finish();
		}
	}

	
	
}
