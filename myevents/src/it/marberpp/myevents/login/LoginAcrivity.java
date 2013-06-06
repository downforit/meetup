package it.marberpp.myevents.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoginAcrivity extends SherlockFragmentActivity implements LoginListener {
	//public static final String USERNAME_ID = "username";
	//public static final String PASSWORD_ID = "password";
	
	public static final int ACTIVITY_ID = 1134;

	//***************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		if (getSupportFragmentManager().findFragmentById(android.R.id.content)==null) {
			String username = getIntent().getStringExtra(LoginFragment.PARAM_USERNAME);
			String password = getIntent().getStringExtra(LoginFragment.PARAM_PASSWORD);
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, LoginFragment.newInstance(username, password)).commit();
	    }
	
		getSupportActionBar().setHomeButtonEnabled(true);// serve solo se si imposta un SKD maggiore di 11	

	}

	
	//***************************************************
	@Override
	public void onBackPressed() {
		this.setResult(SherlockFragmentActivity.RESULT_CANCELED);
		finish();
	}


	
	
	//############################# LoginListener methods ###############################
	public void newUserRequired() {
		Log.d(getClass().getSimpleName(), "####### newUserRequired pressed");
		
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, LoginNewFragment.newInstance(null, null)).commit();
		
	}


	@Override
	public void newUserCreated(String username, String password) {
		Log.d(getClass().getSimpleName(), "####### New Account Created correclty");
		this.loginCompleted(username, password);
	}


	@Override
	public void loginCompleted(String username, String password) {
		Intent i = getIntent();
		i.putExtra(LoginFragment.PARAM_USERNAME, username);
		i.putExtra(LoginFragment.PARAM_PASSWORD, password);
		this.setResult(SherlockFragmentActivity.RESULT_OK, i);
		finish();
	}	
	//############################# FINE LoginListener methods ###############################

}
