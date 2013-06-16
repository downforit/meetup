package it.marberpp.myevents.groups;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.utils.GenericFragmentInterface;

import java.util.List;

import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class GroupsListFragment extends ListFragment {
	public static final int PAGE_ID_SELECTION_MODE = 999;
	
	boolean fragmentAttached = false;
	
	List<Group> groups;
	int pageId = -1;

	View vProgressBar = null;
	GroupRowAdapter GroupsAdapter;
	ListView groupsListView = null;
	
	private GenericFragmentInterface genericListener = null;

	
	//***************************************************
	public static GroupsListFragment newInstance(int pageId) {
		Log.d(GroupsListFragment.class.getSimpleName(), "NEW INSTANCE pageId = " + pageId);

		GroupsListFragment f = new GroupsListFragment();
		Bundle args = new Bundle();
		args.putInt(MainLib.PAGE_ID, pageId);
		f.setArguments(args);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.fragmentAttached = true;
		
		this.pageId = getArguments().getInt(MainLib.PAGE_ID);
		Log.d(getClass().getSimpleName(), "ON CREATE VIEW avviato pageId = " + this.pageId);

		View result = inflater.inflate(R.layout.groups_list_fragment, parent,
				false);


		this.vProgressBar = result.findViewById(R.id.progressBarELF);
		this.groupsListView = (ListView) result.findViewById(android.R.id.list);

		if (this.groups != null) {// questo controllo e' necessario perchè
									// onCreateView viene eseguito anche se si
									// setta in retain a true
			this.showContents();
		}

		return result;
	}
	
	
	//***************************************************
	@Override
	public void onDetach(){
		super.onDetach();
		
		this.fragmentAttached = false;
	}

	
	//***************************************************
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Group currentGroup = this.GroupsAdapter.getItem(position);
		
		if(this.pageId == PAGE_ID_SELECTION_MODE){
			Log.d(getClass().getSimpleName(), " groupId = " + currentGroup.getGrpId());

			if(this.getGenericListener() != null){
				this.getGenericListener().onFragmentResult(GroupsListFragment.class.getSimpleName(), currentGroup );
			}

		} else {
			Intent intentTmp=new Intent(getActivity(), GroupActivity.class);
			intentTmp.putExtra(MainLib.PARAM_GROUP_ID, currentGroup.getGrpId());
			startActivityForResult(intentTmp, 0);
		}
		    
	}
	
	//###################################################
	//###################################################

	//***************************************************
	public void setGroups(List<Group> groups) {
		Log.d(getClass().getSimpleName(), "SET groups avviato pageId = " + this.pageId);
		this.groups = groups;

		this.showContents();
	}


	
	//***************************************************
	public GenericFragmentInterface getGenericListener() {
		return genericListener;
	}

	//***************************************************
	public void setGenericListener(GenericFragmentInterface genericListener) {
		this.genericListener = genericListener;
	}

	
	
	//***************************************************
	private void showContents() {
		if( !this.fragmentAttached ){
			Log.d(getClass().getSimpleName(), "FRAGMENT NOT ATTACHED, can't update UI pageId = " + this.pageId);
			return;
		}

		if(this.groups != null){
			this.GroupsAdapter = new GroupRowAdapter();
			this.vProgressBar.setVisibility(View.GONE);
			this.groupsListView.setVisibility(View.VISIBLE);
			this.groupsListView.setAdapter(this.GroupsAdapter);
		} else {
			this.vProgressBar.setVisibility(View.VISIBLE);
			this.groupsListView.setVisibility(View.GONE);
			this.groupsListView.setAdapter(null);
		}
		
		
	}
	
	
	
	




	//###################################################
	//###################################################
	class GroupRowAdapter extends ArrayAdapter<Group> {
		GroupRowAdapter() {
			super(GroupsListFragment.this.getActivity(), R.id.txtGroup, GroupsListFragment.this.groups);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GroupRowWrapper wrapper;
			
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.groups_list_row, null);
				wrapper = new GroupRowWrapper(convertView);
				convertView.setTag(wrapper);
			} else {
				wrapper = (GroupRowWrapper)convertView.getTag();
			}

			wrapper.populate(getItem(position));
						
			return convertView;
		}
	}//class
	

	//###################################################
	private static class GroupRowWrapper {
		private TextView txtGroup;
		private TextView txtDescr;
		
		public GroupRowWrapper(View v) {
			txtGroup = (TextView) v.findViewById(R.id.txtGroup);
			txtDescr = (TextView) v.findViewById(R.id.txtGroupDescr);
		}
		
		public void populate(Group group) {
			txtGroup.setText(group.getGrpId());
			txtDescr.setText(group.getGrpDescription());
		}
	}
	

}//class
