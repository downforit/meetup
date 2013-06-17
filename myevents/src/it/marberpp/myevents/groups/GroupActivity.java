package it.marberpp.myevents.groups;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.accounts.AccountSelectActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class GroupActivity extends SherlockFragmentActivity {
	
	String groupId;
	GroupFragment groupFragment = null;
	
	//*********************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		this.groupFragment = (GroupFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		if (this.groupFragment==null) {
			this.groupId = getIntent().getStringExtra(MainLib.PARAM_GROUP_ID);
			
			this.groupFragment = GroupFragment.newInstance(this.groupId);
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, this.groupFragment).commit();
	    }
	
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
			Intent intentTmp=new Intent(this, AccountSelectActivity.class);
			//intentTmp.putExtra(MainLib.PARAM_GROUP_ID, this.groupFragment.getGroupId());//prendo l'ID del gruppo dal fragment perche' il fragment ha il retain
			intentTmp.putExtra(MainLib.PARAM_GROUP_ID, this.groupId);//prendo l'ID del gruppo dal fragment perche' il fragment ha il retain
			startActivityForResult(intentTmp, AccountSelectActivity.ACTIVITY_ID);
			
			return true;
		}
		
		return (super.onOptionsItemSelected(item));
	}
	
	//***********************************************
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode){
		case AccountSelectActivity.ACTIVITY_ID:
			if(resultCode == Activity.RESULT_OK){
				this.groupFragment.reloadData();
			}
			break;
		}
	}
	
}
