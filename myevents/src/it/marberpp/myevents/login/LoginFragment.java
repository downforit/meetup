package it.marberpp.myevents.login;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.network.NetworkHelper;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.ThreadUtilities;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class LoginFragment extends SherlockFragment implements View.OnClickListener {
	
	private String authTokenType = null;
	private String loginType = null;
	
	private LoginListener listener = null;
	
	private EditText txtUsername = null;
	private EditText txtPassword = null;
	
	private Button btnOk = null;
	private ProgressBar progBarOk = null;
	
	private TextView lblNewUser = null;
	
	//***************************************************
	protected static LoginFragment newInstance(String username, String password, String authTokenType, String loginType) {
		LoginFragment f = new LoginFragment();
		
		if( (username != null && username.length() > 0)
		     || (password != null && password.length() > 0)
		     || (authTokenType != null && authTokenType.length() > 0)
		     || (loginType != null && loginType.length() > 0) ){
			Bundle args = new Bundle();
			
			if(username != null && username.length() > 0){
				args.putString(MainLib.PARAM_USERNAME, username);
			}

			if(password != null && password.length() > 0){
				args.putString(MainLib.PARAM_PASSWORD, password);
			}
			
			if(authTokenType != null && authTokenType.length() > 0){
				args.putString(MainLib.PARAM_AUTHTOKEN_TYPE, authTokenType);
			}

			if(loginType != null && loginType.length() > 0){
				args.putString(MainLib.PARAM_LOGIN_TYPE, loginType);
			}
			
			f.setArguments(args);
		}
		return (f);
	}
	
	
	//***************************************************
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View result = inflater.inflate(R.layout.login_fragment, parent, false);

		this.txtUsername = (EditText) result.findViewById(R.id.loginTxtUsername);
		this.txtPassword = (EditText) result.findViewById(R.id.loginTxtPassword);
		
		this.btnOk=(Button)result.findViewById(R.id.loginBtnOk);
		this.btnOk.setOnClickListener(this);

		this.progBarOk = (ProgressBar) result.findViewById(R.id.loginProgBarOk);

		this.lblNewUser = (TextView) result.findViewById(R.id.loginLblNewUser);
		this.lblNewUser.setOnClickListener(this);
		
		if(getArguments() != null){
			String username = getArguments().getString(MainLib.PARAM_USERNAME);
			String password = getArguments().getString(MainLib.PARAM_PASSWORD);
			
			this.authTokenType = getArguments().getString(MainLib.PARAM_AUTHTOKEN_TYPE);
			this.loginType = getArguments().getString(MainLib.PARAM_LOGIN_TYPE);
	
			if(username != null){
				this.txtUsername.setText(username);
			}
			
			if(password != null){
				this.txtPassword.setText(password);
			}
		}
		

		Log.i(getClass().getSimpleName(), "loginType = " + this.loginType + "  authTokenType = " + this.authTokenType + "  VAL_MY_EVENTS_AUTHTOKEN_TYPE " + MainLib.VAL_MY_EVENTS_AUTHTOKEN_TYPE);

		if (loginType != null) {
        	this.lblNewUser.setVisibility(View.GONE);
        }
		
		
		return result;
	}

	
	//***************************************************
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.listener = (LoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement LoginListener");
        }
    }	
	
	

    
    //***************************************************
	@Override
	public void onClick(View item) {
		if(item.getId() == R.id.loginBtnOk){
			Log.d(getClass().getSimpleName(), "####### btnOk_click");

			this.btnOk.setVisibility(View.GONE);
			this.lblNewUser.setVisibility(View.GONE);
			this.progBarOk.setVisibility(View.VISIBLE);
			
			LoginTask loginTask = new LoginTask(this.txtUsername.getText().toString(), this.txtPassword.getText().toString());
			ThreadUtilities.executeAsyncTask(loginTask, getActivity().getApplicationContext());
			
			
		}else if(item.getId() == R.id.loginLblNewUser){
			this.listener.newUserRequired();
		}
		
	} //onClick
	

    //***************************************************
	public void loginTerminated(boolean loginCompleted){
		if(loginCompleted){
			Toast.makeText(getActivity(), R.string.loginConfirmedMsg, Toast.LENGTH_LONG).show();
			this.listener.loginCompleted(this.txtUsername.getText().toString(), this.txtPassword.getText().toString());
			
			this.btnOk.setEnabled(false);
			this.lblNewUser.setEnabled(false);
//		} else {
//			if(response == null){
//				Toast.makeText(getActivity(), R.string.unexpectedErrorMessage, Toast.LENGTH_LONG).show();
//			} else {
//				if(response.getErrorCode() == ServicesUtils.NET_ERROR_NETWORK_COMUNICATION){
//					Toast.makeText(getActivity(), R.string.networkFailedMessage, Toast.LENGTH_LONG).show();
//				} else if(response.getErrorCode() == ServicesUtils.NET_ERROR_LOGIN_FAILED){
//					Toast.makeText(getActivity(), R.string.loginFailedMessage, Toast.LENGTH_LONG).show();
//				} else {
//					Toast.makeText(getActivity(), R.string.unexpectedErrorMessage, Toast.LENGTH_LONG).show();
//				}
//			}
		}

		this.btnOk.setVisibility(View.VISIBLE);
		this.lblNewUser.setVisibility(View.VISIBLE);
		this.progBarOk.setVisibility(View.GONE);

	}
	


	//#####################################################################
	//#####################################################################
	private class LoginTask extends AsyncTask<Context, Void, Void> {
		String username;
		String password;
		//GenericResponse response = null;

		Throwable exception = null;
		
		public LoginTask(String username, String password){
			this.username = username;
			this.password = password;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			try{
				NetworkHelper.doLogin(this.username, this.password);
			}catch(Throwable ex){
				/*
				response = new GenericResponse();
				response.setServiceSuccessed(false);
				response.setErrorCode(-1);
				response.setDescription(ex.getMessage());
				*/
				this.exception = ex;
			}
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			if(this.exception != null){
				ExceptionsUtils.standardManagingException(this.exception, getActivity());
			}
			
			LoginFragment.this.loginTerminated(this.exception == null);
		}
	}// class PrefsLoadTask


}//class
