package it.marberpp.myevents.groups;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.utils.GenericFragmentInterface;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class GroupNewActivity extends SherlockFragmentActivity implements GenericFragmentInterface {
	public static final int ACTIVITY_ID = 1136;
	
	GroupNewFragment groupNewFragment = null;
	
	//*********************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		this.groupNewFragment = (GroupNewFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		if (this.groupNewFragment == null) {
			String username = getIntent().getStringExtra(MainLib.PARAM_USERNAME);
			this.groupNewFragment = GroupNewFragment.newInstance(username);
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, this.groupNewFragment).commit();
	    }
	
		getSupportActionBar().setHomeButtonEnabled(true);// serve solo se si imposta un SKD maggiore di 11	

	}
	

	//***********************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.save_cancel, menu);
		return(super.onCreateOptionsMenu(menu));
	}

	
	//*********************************************
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
		case R.id.menuCancel:
			finish();
			
			return true;
		case R.id.menuSave:
			this.groupNewFragment.save();
			return true;
		}
		
		return (super.onOptionsItemSelected(item));
	}


	//*********************************************
	@Override
	public void onFragmentResult(String fragmentName, Object data) {
		Intent i = getIntent();
		this.setResult(SherlockFragmentActivity.RESULT_OK, i);
		finish();

	}
	

}
