package it.marberpp.myevents.groups;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.List;

import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Group;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;


public class GroupFragment extends ListFragment {

	
	String groupId;
	Group group;
	List<Account> accounts;

	View vProgressBar = null;
	ListView accountsListView = null;
	View pBody = null;
	
	TextView txtName = null;;
	TextView txtDescription = null;;

	
	//***************************************************
	protected static GroupFragment newInstance(String groupId) {
		GroupFragment f = new GroupFragment();
		
		if( groupId != null && groupId.length() > 0 ){
			Bundle args = new Bundle();
			
			args.putString(MainLib.PARAM_GROUP_ID, groupId);

			f.setArguments(args);
		}
		return (f);
	}

	
	//***************************************************
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(getClass().getSimpleName(), "ON CREATE avviato"); 
		this.setRetainInstance(true);
	}	
	
	
	
	//***************************************************
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View result = inflater.inflate(R.layout.group_fragment, parent, false);

		this.vProgressBar = result.findViewById(R.id.progressBar);
		this.accountsListView = (ListView) result.findViewById(android.R.id.list);

		this.pBody = result.findViewById(R.id.panelInfos);
		this.txtName = (TextView) result.findViewById(R.id.txtName);
		this.txtDescription = (TextView) result.findViewById(R.id.txtDescription);

		groupId = getArguments().getString(MainLib.PARAM_GROUP_ID);
	
		
		return result;
	}


	//***************************************************
	@Override
	public void onResume(){
		super.onResume();
		
		if(this.group == null){
			reloadData();
			
		} else {
			showGroup(this.group);
		}
	}
	
	
	//***************************************************
	public void reloadData(){
		ThreadUtilities.executeAsyncTask(new LoadGroupTask(this.groupId), getActivity().getApplicationContext());
	}
	
	//***************************************************
	public String getGroupId(){
		return this.groupId;
	}
	
	
	//***************************************************
	private void showGroup(Group group){
		this.vProgressBar.setVisibility(View.GONE);
		this.accountsListView.setVisibility(View.VISIBLE);
		this.pBody.setVisibility(View.VISIBLE);
		
		this.txtName.setText(group.getGrpId());
		this.txtDescription.setText(group.getGrpDescription());
		this.accountsListView.setAdapter(new AccountRowAdapter());

	}
	
	
	//***************************************************
	private void setGroupInfos(Group group, List<Account> accounts){
		this.group = group;
		this.accounts = accounts;
		this.showGroup(this.group);
	}

	
	
	//#####################################################################
	//#####################################################################
	private class LoadGroupTask extends AsyncTask<Context, Void, Void> {
		String groupId;
		Group groupTmp;
		List<Account> accountsTmp;
		
		Throwable exception = null;

		public LoadGroupTask(String groupId){
			this.groupId = groupId;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			
			this.groupTmp = new Group();
			
			try{
				this.groupTmp = DatabaseHelper.getInstance(getActivity()).getGroup(this.groupId);
				
				this.accountsTmp = DatabaseHelper.getInstance(getActivity()).getAccountsByGroupId(this.groupId);
				
				
			} catch(Throwable ex){
				this.exception = ex;
			}
			
			return null;
		}

		@Override
		public void onPostExecute(Void arg0) {
			if(this.exception != null){
				ExceptionsUtils.standardManagingException(this.exception, getActivity());
			}

			GroupFragment.this.setGroupInfos(this.groupTmp, this.accountsTmp);
		}
	}// class PrefsLoadTask
	

	//###################################################
	//###################################################
	class AccountRowAdapter extends ArrayAdapter<Account> {
		AccountRowAdapter() {
			super(GroupFragment.this.getActivity(), R.layout.accounts_list_row,R.id.txtAccount, GroupFragment.this.accounts);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);

			Account currentAccount = GroupFragment.this.accounts.get(position);
			
			TextView txtAccount = (TextView) row.findViewById(R.id.txtAccount);
			txtAccount.setText(currentAccount.getAcnId());
			
			return (row);
		}
	}//class
	
	
}
