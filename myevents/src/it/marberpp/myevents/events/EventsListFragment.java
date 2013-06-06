package it.marberpp.myevents.events;

import it.marberpp.myevents.R;
import it.marberpp.myevents.hibernate.pojo.Event;
import it.marberpp.myevents.login.LoginAcrivity;
import it.marberpp.myevents.login.LoginFragment;

import java.util.List;


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


public class EventsListFragment extends ListFragment {
	private static final String PAGE_ID = "pageId";

	boolean fragmentAttached = false;
	
	List<Event> events;
	int pageId = -1;

	View vProgressBar = null;
	ListView eventsListView = null;

	
	//***************************************************
	protected static EventsListFragment newInstance(int pageId) {
		Log.d(EventsListFragment.class.getSimpleName(), "NEW INSTANCE pageId = " + pageId);

		EventsListFragment f = new EventsListFragment();
		Bundle args = new Bundle();
		args.putInt(PAGE_ID, pageId);
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
		
		this.pageId = getArguments().getInt(PAGE_ID);
		Log.d(getClass().getSimpleName(), "ON CREATE VIEW avviato pageId = " + this.pageId);

		View result = inflater.inflate(R.layout.eventslist_fragment, parent,
				false);


		this.vProgressBar = result.findViewById(R.id.progressBarELF);
		this.eventsListView = (ListView) result.findViewById(android.R.id.list);

		if (this.events != null) {// questo controllo e' necessario perchè
									// onCreateView viene eseguito anche se si
									// setta in retain a true
			this.showContents();
		}

		//Log.d("EventsListFragment", "ON CREATE VIEW completato");
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
		Event currentEvent = this.events.get(position);
		
		Intent intentTmp=new Intent(getActivity(), EventActivity.class);
		intentTmp.putExtra(EventFragment.PARAM_EVENT_ID, currentEvent.getEvnId());
		startActivityForResult(intentTmp, 0);
		    
	}
	

	//***************************************************
	public void setEvents(List<Event> events) {
		Log.d(getClass().getSimpleName(), "SET EVENT avviato pageId = " + this.pageId);
		this.events = events;

		this.showContents();
	}

	//***************************************************
	private void showContents() {
		if( !this.fragmentAttached ){
			Log.d(getClass().getSimpleName(), "FRAGMENT NOT ATTACHED, can't update UI pageId = " + this.pageId);
			return;
		}

		if(this.events != null){
			this.vProgressBar.setVisibility(View.GONE);
			this.eventsListView.setVisibility(View.VISIBLE);
			this.eventsListView.setAdapter(new EventRowAdapter());
		} else {
			this.vProgressBar.setVisibility(View.VISIBLE);
			this.eventsListView.setVisibility(View.GONE);
			this.eventsListView.setAdapter(null);
		}
		
		
	}
	
	
	
	
	//###################################################
	//###################################################
	class EventRowAdapter extends ArrayAdapter<Event> {
		EventRowAdapter() {
			super(EventsListFragment.this.getActivity(), R.layout.events_list_row,R.id.txtEvent, EventsListFragment.this.events);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);

			TextView txtEvent = (TextView) row.findViewById(R.id.txtEvent);
			txtEvent.setText(events.get(position).getEvnId());

			TextView txtDescr = (TextView) row.findViewById(R.id.txtEventDescr);
			txtDescr.setText(events.get(position).getEvnDescription());
			
			return (row);
		}
	}//class
	
	

}//class
