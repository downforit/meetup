package it.marberpp.myevents.events;

import it.marberpp.myevents.MainDataRetainFragment;
//import android.R;
import it.marberpp.myevents.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class EventsVPAdapter extends FragmentStatePagerAdapter {
	public static final int NUM_PAGES = 2;

	public static final int ID_EVENTS_LIST_FUTURES = 0;
	public static final int ID_EVENTS_LIST_PAST = 1;
	
	
	SherlockFragmentActivity ctxt;

	MainDataRetainFragment dataRetain;

	//***************************************************
	public EventsVPAdapter(SherlockFragmentActivity ctxt, MainDataRetainFragment dataRetain) {
		super(ctxt.getSupportFragmentManager());
		
		this.ctxt = ctxt;
		
		this.dataRetain = dataRetain;
	}

	//***************************************************
	@Override
	public Fragment getItem(int position) {
		EventsListFragment result = EventsListFragment.newInstance(position);
		
		this.dataRetain.addEventsListFragment(result, position);
		
		return result;
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
		default:
			return this.ctxt.getString(R.string.mainVPTabPastEvents);
		}
	}
}
