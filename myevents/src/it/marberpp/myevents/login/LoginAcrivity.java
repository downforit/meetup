package it.marberpp.myevents.login;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoginAcrivity extends SherlockFragmentActivity implements LoginListener {
	//public static final String USERNAME_ID = "username";
	//public static final String PASSWORD_ID = "password";
	
	public static final int ACTIVITY_ID = 1134;

    //private AccountManager mAccountManager;
	
	String authTokenType;
	String loginType;
	
	boolean newAccount = false;
	
	//***************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		if (getSupportFragmentManager().findFragmentById(android.R.id.content)==null) {
			String username = getIntent().getStringExtra(MainLib.PARAM_USERNAME);
			String password = getIntent().getStringExtra(MainLib.PARAM_PASSWORD);
			loginType = getIntent().getStringExtra(MainLib.PARAM_LOGIN_TYPE);

			authTokenType = getIntent().getStringExtra(MainLib.PARAM_AUTHTOKEN_TYPE);

			if(username == null || username.length() == 0){
				this.newAccount = true;
			}
			
			//if(this.loginType != null){
			//	Log.i(getClass().getSimpleName(), "AccountManager creato");
		    //    mAccountManager = AccountManager.get(this);
			//}
			
			if(this.newAccount){
		        new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(R.string.createAccount)
		        .setMessage(R.string.goToTheProgramForTheLogin)
		        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	LoginAcrivity.this.finish();
		            }
		        })
		        .show();
		        
			}
			
			Log.i(getClass().getSimpleName(), "@#@# loginType = " + loginType + "  authTokenType = " + this.authTokenType + "  VAL_MY_EVENTS_AUTHTOKEN_TYPE " + MainLib.VAL_MY_EVENTS_AUTHTOKEN_TYPE);
	        
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, LoginFragment.newInstance(username, password, authTokenType, loginType)).commit();
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
		if (loginType == null){
			loginCompletedForNormalOperation(username, password);
		} else if(loginType.equals(MainLib.VAL_LOGIN_SYNC) ){
			loginCompletedForSync(username, password);
		} else if(loginType.equals(MainLib.VAL_CONFIRM_CREDENTIAL_SYNC) ){
			loginCompletedForConfirmCredentials(username, password, true);
		}
		
	}	
	//############################# FINE LoginListener methods ###############################

	
	
	
	public void loginCompletedForNormalOperation(String username, String password) {
		Log.d(getClass().getSimpleName(), "loginCompletedForNormalOperation");

		Intent i = getIntent();
		i.putExtra(MainLib.PARAM_USERNAME, username);
		i.putExtra(MainLib.PARAM_PASSWORD, password);

        updateSystemAccounts(username, password, true);
		
		this.setResult(SherlockFragmentActivity.RESULT_OK, i);
		finish();
	}	

	
	
    /**
     * 
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     * 
     * @param the confirmCredentials result.
     */
    protected void loginCompletedForSync(String username, String password) {
        Log.i(getClass().getSimpleName(), "loginCompletedForSync()  newAccount = "+ this.newAccount);

        final Intent intent = new Intent();
        String mAuthtoken = password;
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, MainLib.VAL_ACCOUNT_TYPE);
        if (authTokenType != null && authTokenType.equals(MainLib.VAL_MY_EVENTS_AUTHTOKEN_TYPE)) {
            Log.i(getClass().getSimpleName(), "mAuthtoken = " + mAuthtoken  + "  authTokenType = " + authTokenType);
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
        }

        /*
        final Account account = new Account(username, MainLib.VAL_ACCOUNT_TYPE);
        AccountManager mAccountManager = AccountManager.get(this);
        if(this.newAccount){
            mAccountManager.addAccountExplicitly(account, password, null);
            // Set contacts sync for this account.
            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        } else {
            mAccountManager.setPassword(account, password);
        }
        */
        updateSystemAccounts(username, password, this.newAccount);
        
        //setAccountAuthenticatorResult(intent.getExtras());
        AccountAuthenticatorResponse mAccountAuthenticatorResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null) {
    		Log.d(getClass().getSimpleName(), "mAccountAuthenticatorResponse created correctly");
            mAccountAuthenticatorResponse.onRequestContinued();
            mAccountAuthenticatorResponse.onResult(intent.getExtras());
            mAccountAuthenticatorResponse = null;
        }        
        
        setResult(RESULT_OK, intent);
        finish();
    }
	

    private void updateSystemAccounts(String username, String password, boolean createNewAccount){
        final Account account = new Account(username, MainLib.VAL_ACCOUNT_TYPE);

        AccountManager mAccountManager = AccountManager.get(this);
        
        if(createNewAccount){
            if(mAccountManager.addAccountExplicitly(account, password, null) == true){
	            // Set contacts sync for this account.
	            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
            } else {
                mAccountManager.setPassword(account, password);
            }
        } else {
            mAccountManager.setPassword(account, password);
        }
    	
    }
    
    
    /**
     * Called when response is received from the server for confirm credentials
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller.
     * 
     * @param the confirmCredentials result.
     */
    protected void loginCompletedForConfirmCredentials(String username, String password, boolean result) {
        Log.i(getClass().getSimpleName(), "finishConfirmCredentials()");
        final Account account = new Account(username, MainLib.VAL_ACCOUNT_TYPE);

        AccountManager mAccountManager = AccountManager.get(this);
        mAccountManager.setPassword(account, password);

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);

        //setAccountAuthenticatorResult(intent.getExtras());
        
        AccountAuthenticatorResponse mAccountAuthenticatorResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
            mAccountAuthenticatorResponse.onResult(intent.getExtras());
            mAccountAuthenticatorResponse = null;
        }        

        setResult(RESULT_OK, intent);
        finish();
    }
    
    
	
}
