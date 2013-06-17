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


public class GroupFragment extends ListFragment {

	
	String groupId;
	Group group;
	List<Account> accounts;
	AccountRowAdapter accountsAdapter;

	View vProgressBar = null;
	ListView accountsListView = null;
	//View pBody = null;
	
	//TextView txtName = null;
	//TextView txtDescription = null;

	
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

		this.accountsListView.setClickable(false);
		//this.pBody = result.findViewById(R.id.panelInfos);
		//this.txtName = (TextView) result.findViewById(R.id.txtName);
		//this.txtDescription = (TextView) result.findViewById(R.id.txtDescription);

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
		//this.pBody.setVisibility(View.VISIBLE);
		
		//this.txtName.setText(group.getGrpId());
		//this.txtDescription.setText(group.getGrpDescription());

		this.accountsAdapter = new AccountRowAdapter(this.accounts);
		this.accountsListView.setAdapter(this.accountsAdapter);

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
        private static final int TYPE_GROUP = 0;
        private static final int TYPE_ACCOUNT = 1;

		AccountRowAdapter(List<Account> accountsTmp) {
			super(GroupFragment.this.getActivity(),R.id.txt_id, accountsTmp);
			
		}

		
		
		@Override
		public int getViewTypeCount(){
			return 2;
		}

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? TYPE_GROUP : TYPE_ACCOUNT;
        }		
		
		@Override
		public int getCount() {
			return super.getCount() + 1;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(this.getItemViewType(position) == TYPE_GROUP){
				GroupDetailRowWrapper wrapper;
				
				if(convertView == null){
					convertView = getActivity().getLayoutInflater().inflate(R.layout.group_detail_row, null);
					wrapper = new GroupDetailRowWrapper(convertView);
					convertView.setTag(wrapper);
				} else {
					wrapper = (GroupDetailRowWrapper)convertView.getTag();
				}
				wrapper.populate(GroupFragment.this.group);
				
			} else {
				AccountRowWrapper wrapper;

				if(convertView == null){
					convertView = getActivity().getLayoutInflater().inflate(R.layout.accounts_list_row, null);
					wrapper = new AccountRowWrapper(convertView);
					convertView.setTag(wrapper);
				} else {
					wrapper = (AccountRowWrapper)convertView.getTag();
				}
				wrapper.populate(getItem(position -1));
			}
			
			return convertView;
		}


	}//class


	
	//###################################################
	private static class RowWrapper {
		TextView txt_id = null;
		
		public String getRowId(){
			return this.txt_id.getText().toString();
		}
	}
	
	
	//###################################################
	private static class AccountRowWrapper extends RowWrapper {
		//TextView txt_id = null;
		private TextView txtAccount;
		
		public AccountRowWrapper(View v) {
			txt_id = (TextView) v.findViewById(R.id.txt_id);
			txtAccount = (TextView) v.findViewById(R.id.txtAccount);
		}
		
		public void populate(Account account) {
			txt_id.setText("acn_" + account.getAcnId());
			txtAccount.setText(account.getAcnId());
		}
	}
		

	//###################################################
	private static class GroupDetailRowWrapper extends RowWrapper {
		//TextView txt_id = null;
		TextView txtName = null;
		TextView txtDescription = null;
		
		public GroupDetailRowWrapper(View v) {
			txt_id = (TextView) v.findViewById(R.id.txt_id);
			this.txtName = (TextView) v.findViewById(R.id.txtName);
			this.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
		}
		
		public void populate(Group group) {
			txt_id.setText("grp_" + group.getGrpId());
			this.txtName.setText(group.getGrpId());
			this.txtDescription.setText(group.getGrpDescription());
		}
	}
				
}
