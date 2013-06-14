package it.marberpp.myevents.accounts;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.events.EventActivity;
import it.marberpp.myevents.events.EventsListFragment;
import it.marberpp.myevents.groups.GroupsListFragment;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.network.NetworkHelper;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.GenericFragmentInterface;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.ArrayList;
import java.util.List;

import mymeeting.exceptions.C_NetworkKeyDuplicateException;
import mymeeting.exceptions.C_RuntimeException;
import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import mymeeting.hibernate.pojo.RAcnGrp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class AccountSelectFragment extends SherlockListFragment {

	private GenericFragmentInterface genericListener;

	String groupId;

	List<Account> accounts;
	Account currentAccount;
	//String username;
	
	EditText txtSearch;
	Button btnSearch;
	ListView listAccounts;
	View vProgressBar;
	
	
	
	//***************************************************
	protected static AccountSelectFragment newInstance(String groupId) {
		AccountSelectFragment f = new AccountSelectFragment();

		if( groupId != null && groupId.length() > 0 ){
			Bundle args = new Bundle();
			
			args.putString(MainLib.PARAM_GROUP_ID, groupId);

			f.setArguments(args);
		}
		return (f);
	}

	
	//***************************************************
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View result = inflater.inflate(R.layout.account_select_fragment, parent, false);

		groupId = getArguments().getString(MainLib.PARAM_GROUP_ID);
		
		this.txtSearch = (EditText) result.findViewById(R.id.txtSearch);
		this.listAccounts = (ListView) result.findViewById(android.R.id.list);
		this.vProgressBar = result.findViewById(R.id.progressBar);

		this.btnSearch = (Button) result.findViewById(R.id.btnSearch);

		this.btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listAccounts.setVisibility(View.GONE);
				vProgressBar.setVisibility(View.VISIBLE);
				
				ManageAccountsTask searchTask = new ManageAccountsTask(ManageAccountsTask.OP_SEARCH_ACCOUNTS
						, AccountSelectFragment.this.txtSearch.getText().toString()
						, AccountSelectFragment.this.groupId
						,null);
				ThreadUtilities.executeAsyncTask(searchTask, getActivity().getApplicationContext());
			}
		});
		/*
		if(getArguments() != null){
			this.username = getArguments().getString(MainLib.PARAM_USERNAME);
		}
		*/
		
		return result;
	}

	//***************************************************
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.genericListener = (GenericFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GenericFragmentInterface");
        }
    }
	
	//***************************************************
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		currentAccount = this.accounts.get(position);

        new AlertDialog.Builder(getActivity())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.addUser)
        .setMessage(R.string.reallyAddUser)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	confirmCurrentAccount();
            }

        })
        .setNegativeButton(R.string.cancel, null)
        .show();		
		
	}
	

	//***************************************************
	public void accountsFounded(List<Account> accounts){
		vProgressBar.setVisibility(View.GONE);
		listAccounts.setVisibility(View.VISIBLE);
		
		this.accounts = accounts;
		
		this.listAccounts.setAdapter(new AccountRowAdapter());
	}
	
	
	//***************************************************
	public void confirmCurrentAccount(){
		ProgressDialog dialog = ProgressDialog.show(getActivity(), "Saving", "Please wait...", true);
		
		ManageAccountsTask searchTask = new ManageAccountsTask(ManageAccountsTask.OP_SAVE_ACCOUNT_IN_GROUP
															, this.currentAccount.getAcnId()
															, this.groupId
															, dialog);
		ThreadUtilities.executeAsyncTask(searchTask, getActivity().getApplicationContext());
	}
	
	
	//***************************************************
	public void accountAddedToGroup(){
		if(this.genericListener != null){
			this.genericListener.onFragmentResult(this.getClass().getSimpleName(), null);
		}
	}
	
	
	//***************************************************
	public GenericFragmentInterface getGenericListener() {
		return genericListener;
	}


	//***************************************************
	public void setGenericListener(GenericFragmentInterface genericListener) {
		this.genericListener = genericListener;
	}



	//#####################################################################
	//#####################################################################
	private class ManageAccountsTask extends AsyncTask<Context, Void, Void> {
		public static final int OP_SEARCH_ACCOUNTS = 0;
		public static final int OP_SAVE_ACCOUNT_IN_GROUP = 1;
		
		
		Throwable exception = null;
		
		String username;
		String groupId;
		int operation;
		ProgressDialog dialog;
		
		List<Account> accountsTmp;
		
		
		public ManageAccountsTask(int operation, String username, String groupId, ProgressDialog dialog){
			this.operation = operation;
			this.username = username;
			this.groupId = groupId;
			this.dialog = dialog;
			
			if(this.operation == OP_SAVE_ACCOUNT_IN_GROUP){
				if(this.username == null || this.username.length() == 0){
					throw new C_RuntimeException("username required for OP_SAVE_ACCOUNT_IN_GROUP");
				}
				if(this.groupId == null || this.groupId.length() == 0){
					throw new C_RuntimeException("group required for OP_SAVE_ACCOUNT_IN_GROUP");
				}
			}
			
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			try{
				//ThreadUtilities.sleep(4000);
				switch(this.operation){
				case OP_SEARCH_ACCOUNTS:
					accountsTmp = NetworkHelper.serachAccounts(this.username);
					break;
				case OP_SAVE_ACCOUNT_IN_GROUP:
					List<RAcnGrp> rAcnGrpsTmp = new ArrayList<RAcnGrp>();

					RAcnGrp rAcnGrp = NetworkHelper.addAccountToGroup(this.username, this.groupId);
					rAcnGrpsTmp.add(rAcnGrp);
					DatabaseHelper.getInstance(getActivity()).updateRAcnGrp(rAcnGrpsTmp);
					
					break;
				}
				
			}catch(Throwable ex){
				this.exception = ex;
			}
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			
			if(this.exception != null){
				if(this.exception instanceof C_NetworkKeyDuplicateException){
					Toast.makeText(getActivity(), R.string.accountArleadyAdded, Toast.LENGTH_LONG).show();
				} else {
					ExceptionsUtils.standardManagingException(this.exception, getActivity());
				}
			}

			if(this.dialog != null){
				this.dialog.dismiss();
			}
			
			switch(this.operation){
			case OP_SEARCH_ACCOUNTS:
				AccountSelectFragment.this.accountsFounded(this.accountsTmp);
				break;
			case OP_SAVE_ACCOUNT_IN_GROUP:
				if(this.exception == null){
					AccountSelectFragment.this.accountAddedToGroup();
				}
				break;
			}

		}
	}// class PrefsLoadTask
	


	//###################################################
	//###################################################
	class AccountRowAdapter extends ArrayAdapter<Account> {
		AccountRowAdapter() {
			super(AccountSelectFragment.this.getActivity(), R.layout.accounts_list_row,R.id.txtAccount, AccountSelectFragment.this.accounts);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);

			Account currentAccount = accounts.get(position);
			
			TextView txtAccount = (TextView) row.findViewById(R.id.txtAccount);
			txtAccount.setText(currentAccount.getAcnId());

			//TextView txtDescr = (TextView) row.findViewById(R.id.txtEventDescr);
			//txtDescr.setText(currentEvent.getEvnDescription());
			
			return (row);
		}
	}//class


}
