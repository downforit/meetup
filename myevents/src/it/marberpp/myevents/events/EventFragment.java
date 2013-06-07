package it.marberpp.myevents.events;

import mymeeting.hibernate.pojo.Event;
import it.marberpp.myevents.R;
import it.marberpp.myevents.hibernate.DatabaseHelper;
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


public class EventFragment extends SherlockFragment {
	public static final String PARAM_EVENT_ID = "event_id";

	
	String eventId;
	Event event;

	View vProgressBar = null;
	View pBody = null;
	
	TextView txtName = null;;
	TextView txtDescription = null;;

	
	//***************************************************
	protected static EventFragment newInstance(String eventId) {
		EventFragment f = new EventFragment();
		
		if( eventId != null && eventId.length() > 0 ){
			Bundle args = new Bundle();
			
			args.putString(PARAM_EVENT_ID, eventId);

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

		View result = inflater.inflate(R.layout.event_frament, parent, false);

		this.vProgressBar = result.findViewById(R.id.progressBar);
		this.pBody = result.findViewById(R.id.panelInfos);
		this.txtName = (TextView) result.findViewById(R.id.txtName);
		this.txtDescription = (TextView) result.findViewById(R.id.txtDescription);

		eventId = getArguments().getString(PARAM_EVENT_ID);
	
		
		return result;
	}


	//***************************************************
	@Override
	public void onResume(){
		super.onResume();
		
		if(this.event == null){
			ThreadUtilities.executeAsyncTask(new LoadEventTask(this.eventId), getActivity().getApplicationContext());
			
		} else {
			showEvent(this.event);
		}
	}
	
	
	
	//***************************************************
	private void showEvent(Event event){
		this.vProgressBar.setVisibility(View.GONE);
		this.pBody.setVisibility(View.VISIBLE);
		
		this.txtName.setText(event.getEvnId());
		this.txtDescription.setText(event.getEvnDescription());
	}
	
	
	//***************************************************
	private void setEvent(Event event){
		this.event = event;
		this.showEvent(this.event);
	}

	
	{
		Log.d(getClass().getSimpleName(), "############ event fragment creato");
	}
	
	
	
	
	
	//#####################################################################
	//#####################################################################
	private class LoadEventTask extends AsyncTask<Context, Void, Void> {
		String eventId;
		Event eventTmp;
		
		public LoadEventTask(String eventId){
			this.eventId = eventId;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			
			this.eventTmp = new Event();
			
			this.eventTmp = DatabaseHelper.getInstance(getActivity()).getEvent(eventId);
			
			return null;
		}

		@Override
		public void onPostExecute(Void arg0) {
			EventFragment.this.setEvent(eventTmp);
		}
	}// class PrefsLoadTask
	
	
}
