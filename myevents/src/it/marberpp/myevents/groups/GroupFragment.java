package it.marberpp.myevents.groups;

import mymeeting.hibernate.pojo.Group;
import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.ThreadUtilities;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;


public class GroupFragment extends SherlockFragment {

	
	String groupId;
	Group group;

	View vProgressBar = null;
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
			ThreadUtilities.executeAsyncTask(new LoadGroupTask(this.groupId), getActivity().getApplicationContext());
			
		} else {
			showGroup(this.group);
		}
	}
	
	
	
	//***************************************************
	private void showGroup(Group group){
		this.vProgressBar.setVisibility(View.GONE);
		this.pBody.setVisibility(View.VISIBLE);
		
		this.txtName.setText(group.getGrpId());
		this.txtDescription.setText(group.getGrpDescription());
	}
	
	
	//***************************************************
	private void setGroup(Group group){
		this.group = group;
		this.showGroup(this.group);
	}

	
	{
		Log.d(getClass().getSimpleName(), "############ group fragment creato");
	}
	
	
	
	
	
	//#####################################################################
	//#####################################################################
	private class LoadGroupTask extends AsyncTask<Context, Void, Void> {
		String groupId;
		Group groupTmp;
		
		Throwable exception = null;

		public LoadGroupTask(String groupId){
			this.groupId = groupId;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			
			this.groupTmp = new Group();
			
			try{
				this.groupTmp = DatabaseHelper.getInstance(getActivity()).getGroup(this.groupId);
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

			GroupFragment.this.setGroup(groupTmp);
		}
	}// class PrefsLoadTask
	
	
}
