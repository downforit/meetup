/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package it.marberpp.myevents.interfaces;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.login.LoginAcrivity;
import it.marberpp.myevents.network.NetworkHelper;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * This class is an implementation of AbstractAccountAuthenticator for
 * authenticating accounts in the com.example.android.samplesync domain.
 */
class Authenticator extends AbstractAccountAuthenticator {
	private static final Class<?> loginActivity = LoginAcrivity.class;
	
    private static final String TAG = "@@@@ Authenticator";
    // Authentication Service context
    private final Context mContext;

    public Authenticator(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
        String accountType, String authTokenType, String[] requiredFeatures,
        Bundle options) {

    	Log.i(TAG, "addAccount START");
   
    	
    	Log.i(TAG, "PARAM_AUTHTOKEN_TYPE =" + authTokenType);
        
        final Intent intent = new Intent(mContext, loginActivity);
        intent.putExtra(MainLib.PARAM_LOGIN_TYPE, MainLib.VAL_LOGIN_SYNC);
        intent.putExtra(MainLib.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        Log.i(TAG, "addAccount END");

        return bundle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response,
        Account account, Bundle options) {
    	
    	Log.i(TAG, "confirmCredentials START");

    	if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
            final String password = options.getString(AccountManager.KEY_PASSWORD);
            final boolean verified = onlineConfirmPassword(account.name, password);
            final Bundle result = new Bundle();
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, verified);

            Log.i(TAG, "confirmCredentials STOP INTERMEDIO");
            
            return result;
        }
        // Launch AuthenticatorActivity to confirm credentials
        final Intent intent = new Intent(mContext, loginActivity);
        intent.putExtra(MainLib.PARAM_LOGIN_TYPE, MainLib.VAL_LOGIN_SYNC);
        intent.putExtra(MainLib.PARAM_USERNAME, account.name);
        //intent.putExtra(MainLib.PARAM_CONFIRMCREDENTIALS, true);
        intent.putExtra(MainLib.PARAM_LOGIN_TYPE, MainLib.VAL_CONFIRM_CREDENTIAL_SYNC);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        Log.i(TAG, "confirmCredentials STOP FINAL");

        return bundle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response,
        String accountType) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response,
        Account account, String authTokenType, Bundle loginOptions) {

    	Log.i(TAG, "getAuthToken START");
    	
    	if (!authTokenType.equals(MainLib.VAL_MY_EVENTS_AUTHTOKEN_TYPE)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE,
                "invalid authTokenType");

            Log.i(TAG, "getAuthToken STOP 1");
            return result;
        }
        final AccountManager am = AccountManager.get(mContext);
        final String password = am.getPassword(account);
        if (password != null) {
            final boolean verified =
                onlineConfirmPassword(account.name, password);
            if (verified) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, MainLib.VAL_ACCOUNT_TYPE);
                result.putString(AccountManager.KEY_AUTHTOKEN, password); //??? don't know why

                Log.i(TAG, "getAuthToken STOP 2");
                
                return result;
            }
        }
        // the password was missing or incorrect, return an Intent to an
        // Activity that will prompt the user for the password.
        final Intent intent = new Intent(mContext, loginActivity);
        intent.putExtra(MainLib.PARAM_LOGIN_TYPE, MainLib.VAL_LOGIN_SYNC);
        intent.putExtra(MainLib.PARAM_USERNAME, account.name);
        intent.putExtra(MainLib.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        Log.i(TAG, "getAuthToken STOP FINALE");
        
        return bundle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthTokenLabel(String authTokenType) {
        Log.i(TAG, "getAuthTokenLabel SART");

        if (authTokenType.equals(MainLib.VAL_MY_EVENTS_AUTHTOKEN_TYPE)) {
            String result = mContext.getString(R.string.syncLabel);

            Log.i(TAG, "getAuthTokenLabel STOP 1 [" + result + "]");

            return result;
        }
        

        Log.i(TAG, "getAuthTokenLabel STOP FINALE");
        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response,
        Account account, String[] features) {
        
    	Log.i(TAG, "hasFeatures SART");

        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);

        Log.i(TAG, "hasFeatures STOP finale [" + result + "]");
        
        return result;
    }

    /**
     * Validates user's password on the server
     */
    private boolean onlineConfirmPassword(String username, String password) {
    	
    	Log.i(TAG, "onlineConfirmPassword SART");

    	boolean result = false;
    	
    	//boolean result = NetworkUtilities.authenticate(username, password, null/* Handler */, null/* Context */);
    	try{
	    	NetworkHelper.doLogin(username, password);
	    	result = true;
    	} catch (Throwable ex){
    		Log.e(getClass().getSimpleName(), ex.getMessage());
    	}
    
        Log.i(TAG, "onlineConfirmPassword STOP finale [" + result + "]");
    
    	return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response,
        Account account, String authTokenType, Bundle loginOptions) {

    	Log.i(TAG, "updateCredentials SART");
        
    	final Intent intent = new Intent(mContext, loginActivity);
        intent.putExtra(MainLib.PARAM_LOGIN_TYPE, MainLib.VAL_LOGIN_SYNC);
        intent.putExtra(MainLib.PARAM_USERNAME, account.name);
        intent.putExtra(MainLib.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(MainLib.PARAM_CONFIRMCREDENTIALS, false);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        Log.i(TAG, "updateCredentials STOP");
        
        return bundle;
    }

}
