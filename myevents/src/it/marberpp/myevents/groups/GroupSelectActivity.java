package it.marberpp.myevents.groups;

import it.marberpp.myevents.MainDataRetainFragment;
import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.utils.GenericFragmentInterface;
import mymeeting.hibernate.pojo.Group;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class GroupSelectActivity extends SherlockFragmentActivity implements GenericFragmentInterface {
	public static final int ACTIVITY_ID = 1140;
	
	GroupsListFragment groupsListFragment = null;
	String username;
	
	//*********************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.groupsListFragment = (GroupsListFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		
		this.username = getIntent().getStringExtra(MainLib.PARAM_USERNAME);
		
		if (this.groupsListFragment==null) {
			this.groupsListFragment = GroupsListFragment.newInstance(GroupsListFragment.PAGE_ID_SELECTION_MODE);
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, this.groupsListFragment).commit();
	    }
	
		this.groupsListFragment.setGenericListener(this);
		this.groupsListFragment.setGroups(MainDataRetainFragment.staticInstance.getAllGroups());
		
		getSupportActionBar().setHomeButtonEnabled(true);// serve solo se si imposta un SKD maggiore di 11	

	}
	
	
	
	//***********************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.add, menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	
	
	//*********************************************
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			
			return true;
		case R.id.menuAdd:
			Intent intentTmp=new Intent(this, GroupNewActivity.class);
			intentTmp.putExtra(MainLib.PARAM_USERNAME, this.username);
			startActivityForResult(intentTmp, GroupNewActivity.ACTIVITY_ID);

			return true;
		}
		
		return (super.onOptionsItemSelected(item));
	}


	
	//***********************************************
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case GroupNewActivity.ACTIVITY_ID:
			if(resultCode == Activity.RESULT_CANCELED){
				//non faccio niente
			} else if(resultCode == Activity.RESULT_OK){
				this.groupsListFragment.setGroups(MainDataRetainFragment.staticInstance.getAllGroups());

			}
			
			
			break;
		}
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
