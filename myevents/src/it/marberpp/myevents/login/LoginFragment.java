package it.marberpp.myevents.login;

import it.marberpp.myevents.NetworkHelper;
import it.marberpp.myevents.R;
import it.marberpp.myevents.services.ServicesUtils;
import it.marberpp.myevents.services.pojo.GenericResponse;
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
	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_PASSWORD = "password";
	
	private LoginListener listener = null;
	
	private EditText txtUsername = null;
	private EditText txtPassword = null;
	
	private Button btnOk = null;
	private ProgressBar progBarOk = null;
	
	private TextView lblNewUser = null;
	
	//***************************************************
	protected static LoginFragment newInstance(String username, String password) {
		LoginFragment f = new LoginFragment();
		
		if( (username != null && username.length() > 0) || (password != null && password.length() > 0) ){
			Bundle args = new Bundle();
			
			if(username != null && username.length() > 0){
				args.putString(PARAM_USERNAME, username);
			}

			if(password != null && password.length() > 0){
				args.putString(PARAM_PASSWORD, password);
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
			String username = getArguments().getString(PARAM_USERNAME);
			String password = getArguments().getString(PARAM_PASSWORD);
	
			if(username != null){
				this.txtUsername.setText(username);
			}
			
			if(password != null){
				this.txtPassword.setText(password);
			}
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
	public void manageLoginResponse(GenericResponse response){
		if(response != null && response.isServiceSuccessed()){
			Toast.makeText(getActivity(), R.string.loginConfirmedMsg, Toast.LENGTH_LONG).show();
			this.listener.loginCompleted(this.txtUsername.getText().toString(), this.txtPassword.getText().toString());
			
			this.btnOk.setEnabled(false);
			this.lblNewUser.setEnabled(false);
		} else {
			if(response == null){
				Toast.makeText(getActivity(), R.string.unexpectedErrorMessage, Toast.LENGTH_LONG).show();
			} else {
				if(response.getErrorCode() == ServicesUtils.NET_ERROR_NETWORK_COMUNICATION){
					Toast.makeText(getActivity(), R.string.networkFailedMessage, Toast.LENGTH_LONG).show();
				} else if(response.getErrorCode() == ServicesUtils.NET_ERROR_LOGIN_FAILED){
					Toast.makeText(getActivity(), R.string.loginFailedMessage, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity(), R.string.unexpectedErrorMessage, Toast.LENGTH_LONG).show();
				}
			}
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
		GenericResponse response = null;
		
		public LoginTask(String username, String password){
			this.username = username;
			this.password = password;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			try{
				response = NetworkHelper.doLogin(this.username, this.password);
			}catch(Throwable ex){
				response = new GenericResponse();
				response.setServiceSuccessed(false);
				response.setErrorCode(-1);
				response.setDescription(ex.getMessage());
			}
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			LoginFragment.this.manageLoginResponse(response);
		}
	}// class PrefsLoadTask


}//class
