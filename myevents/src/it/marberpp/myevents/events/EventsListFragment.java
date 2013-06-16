package it.marberpp.myevents.events;

import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;

import java.util.List;

import mymeeting.hibernate.pojo.Event;
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

	boolean fragmentAttached = false;
	
	List<Event> events;
	int pageId = -1;

	View vProgressBar = null;
	EventRowAdapter eventsAdapter;
	ListView eventsListView = null;

	
	//***************************************************
	public static EventsListFragment newInstance(int pageId) {
		Log.d(EventsListFragment.class.getSimpleName(), "NEW INSTANCE pageId = " + pageId);

		EventsListFragment f = new EventsListFragment();
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

		View result = inflater.inflate(R.layout.events_list_fragment, parent,
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
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}	
	

	
	
	//***************************************************
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Event currentEvent = eventsAdapter.getItem(position);
		
		Intent intentTmp=new Intent(getActivity(), EventActivity.class);
		intentTmp.putExtra(MainLib.PARAM_EVENT_ID, currentEvent.getEvnId());
		startActivityForResult(intentTmp, 0);
		    
	}
	
	//###################################################
	//###################################################

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
			if(eventsAdapter == null){
				eventsAdapter = new EventRowAdapter(events);
			}
			this.vProgressBar.setVisibility(View.GONE);
			this.eventsListView.setVisibility(View.VISIBLE);
			this.eventsListView.setAdapter(eventsAdapter);
		} else {
			this.vProgressBar.setVisibility(View.VISIBLE);
			this.eventsListView.setVisibility(View.GONE);
			this.eventsListView.setAdapter(null);
		}
		
		
	}
	
	
	
	
	//###################################################
	//###################################################
	class EventRowAdapter extends ArrayAdapter<Event> {
		EventWrapper eventWrapper;
		
		EventRowAdapter(List<Event> events) {
			super(getActivity(), R.id.txtEvent, events);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.events_list_row, null);
				eventWrapper = new EventWrapper(convertView);
				convertView.setTag(eventWrapper);
			} else {
				eventWrapper = (EventWrapper) convertView.getTag();
			}
			eventWrapper.populate(getItem(position));
			return convertView;
		}
	}//class
	
	//###################################################
	private static class EventWrapper {
		private TextView txtEvent;
		private TextView txtDescr;
		
		public EventWrapper(View v) {
			txtEvent = (TextView) v.findViewById(R.id.txtEvent);
			txtDescr = (TextView) v.findViewById(R.id.txtEventDescr);
		}
		
		public void populate(Event event) {
			txtEvent.setText(event.getEvnId());
			txtDescr.setText(event.getEvnDescription());
		}
	}



}//class
