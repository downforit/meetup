package it.marberpp.myevents;

//import android.R;
import it.marberpp.myevents.R;
import it.marberpp.myevents.events.EventsListFragment;
import it.marberpp.myevents.groups.GroupsListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ListsVPAdapter extends FragmentStatePagerAdapter {
	public static final int NUM_PAGES = 4;

	public static final int ID_EVENTS_LIST_FUTURES = 0;
	public static final int ID_EVENTS_LIST_PAST = 1;
	public static final int ID_GROUPS_LIST_OWNED = 2;
	public static final int ID_GROUPS_LIST_OTHER = 3;
	
	
	SherlockFragmentActivity ctxt;

	MainDataRetainFragment dataRetain;

	//***************************************************
	public ListsVPAdapter(SherlockFragmentActivity ctxt, MainDataRetainFragment dataRetain) {
		super(ctxt.getSupportFragmentManager());
		
		this.ctxt = ctxt;
		
		this.dataRetain = dataRetain;
	}

	//***************************************************
	@Override
	public Fragment getItem(int position) {
		ListFragment result;
		if(position == ID_EVENTS_LIST_FUTURES || position == ID_EVENTS_LIST_PAST){
			result = EventsListFragment.newInstance(position);
			this.dataRetain.addListFragment(result, position);
		} else {
			result = GroupsListFragment.newInstance(position);
			this.dataRetain.addListFragment(result, position);
		}
		
		return result;
	}


	//***************************************************
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		this.dataRetain.removeListFragment(null, position);
	}

	//***************************************************
	@Override
	public int getCount() {
		return NUM_PAGES;
	}
	
	
	//***************************************************
	@Override
	public String getPageTitle(int position) {
		switch(position){
		case ID_EVENTS_LIST_FUTURES:
			return this.ctxt.getString(R.string.mainVPTabFutureEvents);
		case ID_EVENTS_LIST_PAST:
			return this.ctxt.getString(R.string.mainVPTabPastEvents);
		case ID_GROUPS_LIST_OWNED:
			return this.ctxt.getString(R.string.mainVPTabOwnedGroups);
		case ID_GROUPS_LIST_OTHER:
			return this.ctxt.getString(R.string.mainVPTabOtherGroups);
		}
		
		return "";
	}

}
