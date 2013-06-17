package it.marberpp.myevents.groups;

import it.marberpp.myevents.MainDataRetainFragment;
import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.network.NetworkHelper;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.GenericFragmentInterface;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.ArrayList;
import java.util.List;

import mymeeting.exceptions.C_NetworkKeyDuplicateException;
import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Group;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class GroupNewFragment extends SherlockFragment {

	GenericFragmentInterface listener;
	
	String username;
	
	EditText txtGroupName;
	EditText txtDescription;
	
	
	//***************************************************
	protected static GroupNewFragment newInstance(String username) {
		GroupNewFragment f = new GroupNewFragment();
		
		if( username != null && username.length() > 0 ){
			Bundle args = new Bundle();
			
			args.putString(MainLib.PARAM_USERNAME, username);

			f.setArguments(args);
		}
		return (f);
	}

	
	//***************************************************
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View result = inflater.inflate(R.layout.group_new_fragment, parent, false);

		this.txtGroupName = (EditText) result.findViewById(R.id.txtGroupName);
		this.txtDescription = (EditText) result.findViewById(R.id.txtDescription);

		if(getArguments() != null){
			this.username = getArguments().getString(MainLib.PARAM_USERNAME);
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
            this.listener = (GenericFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GenericFragmentInterface");
        }
    }	
	
	
	//***************************************************
	public void groupCreated(){
		this.listener.onFragmentResult(this.getClass().getSimpleName(), null);
	}
	
	
	//***************************************************
	public void save(){
		Group group = new Group();
		Account account = new Account();
		
		group.setGrpId(this.txtGroupName.getText().toString());
		group.setGrpDescription(this.txtDescription.getText().toString());
		
		account.setAcnId(this.username);
		group.setAccount(account);
		
		ProgressDialog dialog = ProgressDialog.show(getActivity(), "Saving", "Please wait...", true);
		
		NewGroupTask newEventTask = new NewGroupTask(group, this.username, dialog);
		ThreadUtilities.executeAsyncTask(newEventTask, getActivity().getApplicationContext());
	
		
		
	}

	
	
	
	//#####################################################################
	//#####################################################################
	private class NewGroupTask extends AsyncTask<Context, Void, Void> {
		Group group;
		String username;
		ProgressDialog dialog;

		Throwable exception = null;
		
		public NewGroupTask(Group group, String username, ProgressDialog dialog){
			this.group = group;
			this.username = username;
			this.dialog = dialog;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			try{
				List<Group> groupsTmp = new ArrayList<Group>();
				
				this.group = NetworkHelper.createNewGroup(this.group, this.username);
				groupsTmp.add(this.group);
				//ThreadUtilities.sleep(3000);
				DatabaseHelper.getInstance(getActivity()).updateGroups(groupsTmp);
				
			}catch(Throwable ex){
				this.exception = ex;
			}
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			this.dialog.dismiss();
			
			if(this.exception != null){
				if(this.exception instanceof C_NetworkKeyDuplicateException){
					Toast.makeText(getActivity(), R.string.groupNameArleadyUsed, Toast.LENGTH_LONG).show();
				} else {
					ExceptionsUtils.standardManagingException(this.exception, getActivity());
				}
			} else {
				MainDataRetainFragment.staticInstance.addOwnedGroup(this.group);
				//MainDataRetainFragment.staticInstance.ownedGroups.add(this.group);
				GroupNewFragment.this.groupCreated();
			}
		}
	}// class PrefsLoadTask
	
}
