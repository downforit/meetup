package it.marberpp.myevents.login;

import mymeeting.exceptions.C_NetworkKeyDuplicateException;
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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class LoginNewFragment extends SherlockFragment implements View.OnClickListener {
	private LoginListener listener = null;
	
	private EditText txtUsername = null;
	private EditText txtPassword1 = null;
	private EditText txtPassword2 = null;
	
	private Button btnOk = null;
	private ProgressBar progBarOk = null;
	
	
	//***************************************************
	protected static LoginNewFragment newInstance(String username, String password) {
		LoginNewFragment f = new LoginNewFragment();
		
		if( (username != null && username.length() > 0) || (password != null && password.length() > 0) ){
			Bundle args = new Bundle();
			
			if(username != null && username.length() > 0){
				args.putString(MainLib.PARAM_USERNAME, username);
			}

			if(password != null && password.length() > 0){
				args.putString(MainLib.PARAM_PASSWORD, password);
			}
			
			f.setArguments(args);
		}
		return (f);
	}
	
	
	//***************************************************
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View result = inflater.inflate(R.layout.login_new_fragment, parent, false);

		this.txtUsername = (EditText) result.findViewById(R.id.lgnNewTxtUsername);
		this.txtPassword1 = (EditText) result.findViewById(R.id.lgnNewTxtPassword1);
		this.txtPassword2 = (EditText) result.findViewById(R.id.lgnNewTxtPassword2);
		
		this.btnOk=(Button)result.findViewById(R.id.lgnNewBtnOk);
		this.btnOk.setOnClickListener(this);

		this.progBarOk = (ProgressBar) result.findViewById(R.id.lgnNewProgBarOk);
		
		if(getArguments() != null){
			String username = getArguments().getString(MainLib.PARAM_USERNAME);
			String password = getArguments().getString(MainLib.PARAM_PASSWORD);
	
			if(username != null){
				this.txtUsername.setText(username);
			}
			
			if(password != null){
				this.txtPassword1.setText(password);
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
		if(item.getId() == R.id.lgnNewBtnOk){
			
			if( !this.txtPassword1.getText().toString().equals(this.txtPassword2.getText().toString()) ){
				Toast.makeText(getActivity(), R.string.lgnNewPasswordsDiffers, Toast.LENGTH_LONG).show();
				return;
			}
			
			Log.d(getClass().getSimpleName(), "####### btnOk_click");
			
			this.btnOk.setVisibility(View.GONE);
			this.progBarOk.setVisibility(View.VISIBLE);
			
			NewAccountTask loginTask = new NewAccountTask(this.txtUsername.getText().toString(), this.txtPassword1.getText().toString());
			ThreadUtilities.executeAsyncTask(loginTask, getActivity().getApplicationContext());
			
		}
		
	} //onClick


	//***************************************************
    //***************************************************
	public void createUSerTerminated(boolean loginCompleted){

		if(loginCompleted){
			Toast.makeText(getActivity(), R.string.lgnNewConfirmedMsg, Toast.LENGTH_LONG).show();
			this.listener.newUserCreated(this.txtUsername.getText().toString(), this.txtPassword1.getText().toString());
			
			this.btnOk.setEnabled(false);
//		} else {
//			if(response.getErrorCode() == ServicesUtils.HIB_ERROR_KEY_VIOLATION){
//				Toast.makeText(getActivity(), R.string.lgnNewUsernameExistMsg, Toast.LENGTH_LONG).show();
//			} else{
//				Toast.makeText(getActivity(), R.string.lgnNewFailedMessage, Toast.LENGTH_LONG).show();
//			}
		}
		

		this.btnOk.setVisibility(View.VISIBLE);
		this.progBarOk.setVisibility(View.GONE);

	}
	
	
	
	//#####################################################################
	//#####################################################################
	private class NewAccountTask extends AsyncTask<Context, Void, Void> {
		String username;
		String password;

		Throwable exception = null;
		
		public NewAccountTask(String username, String password){
			this.username = username;
			this.password = password;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			try{
				NetworkHelper.createNewAccount(this.username, this.password);
			}catch(Throwable ex){
				this.exception = ex;
			}
			return null;
		}

		@Override
		public void onPostExecute(Void arg0) {
			if(this.exception != null){
				if(this.exception instanceof C_NetworkKeyDuplicateException){
					Toast.makeText(getActivity(), R.string.usernameAlreadyUsed, Toast.LENGTH_LONG).show();
				} else {
					ExceptionsUtils.standardManagingException(this.exception, getActivity());
				}
			}

			LoginNewFragment.this.createUSerTerminated(this.exception == null);
		}
	}// class
	
}//class
