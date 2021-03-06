package it.marberpp.myevents.events;

import it.marberpp.myevents.MainDataRetainFragment;
import it.marberpp.myevents.MainLib;
import it.marberpp.myevents.R;
import it.marberpp.myevents.groups.GroupSelectActivity;
import it.marberpp.myevents.hibernate.DatabaseHelper;
import it.marberpp.myevents.network.NetworkHelper;
import it.marberpp.myevents.utils.ExceptionsUtils;
import it.marberpp.myevents.utils.GenericFragmentInterface;
import it.marberpp.myevents.utils.ThreadUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mymeeting.exceptions.C_NetworkKeyDuplicateException;
import mymeeting.hibernate.pojo.Account;
import mymeeting.hibernate.pojo.Event;
import mymeeting.hibernate.pojo.Group;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class EventNewFragment extends SherlockFragment {

	GenericFragmentInterface listener;
	
	String username;
	
	EditText txtEventName;
	EditText txtGroup;
	DatePicker datePicker;
	EditText txtDescription;
	
	
	//***************************************************
	protected static EventNewFragment newInstance(String username) {
		EventNewFragment f = new EventNewFragment();
		
		if( username != null && username.length() > 0 ){
			Bundle args = new Bundle();
			
			args.putString(MainLib.PARAM_USERNAME, username);

			f.setArguments(args);
		}
		return (f);
	}

	
	//***************************************************
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View result = inflater.inflate(R.layout.event_new_fragment, parent, false);

		this.txtEventName = (EditText) result.findViewById(R.id.txtEventName);
		this.txtDescription = (EditText) result.findViewById(R.id.txtDescription);
		
		this.txtGroup = (EditText) result.findViewById(R.id.txtGroup);//attenzione: uso EditText peeche' quando ruoto non perde il testo

		this.txtGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentTmp=new Intent(getActivity(), GroupSelectActivity.class);
				intentTmp.putExtra(MainLib.PARAM_USERNAME, username);
				startActivityForResult(intentTmp, GroupSelectActivity.ACTIVITY_ID);
			}
		});
		
		
		
		this.datePicker = (DatePicker) result.findViewById(R.id.datePicker);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			this.datePicker.setCalendarViewShown(false);
		}

		if(getArguments() != null){
			this.username = getArguments().getString(MainLib.PARAM_USERNAME);
		}
		
		
		return result;
	}

	//***************************************************
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.listener = (GenericFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GenericFragmentInterface");
        }
    }	
	


	//***********************************************
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case GroupSelectActivity.ACTIVITY_ID:
			if(resultCode == Activity.RESULT_CANCELED){
				//do nothing
			} else if(resultCode == Activity.RESULT_OK){
				String groupId = data.getStringExtra(MainLib.PARAM_GROUP_ID);

				this.txtGroup.setText(groupId);
				this.txtGroup.setTag(true);
				
			}
			
			
			break;
		}
	}

	
	//##########################################
	//##########################################

	
	
	//***************************************************
	public void eventCreated(){
		this.listener.onFragmentResult(this.getClass().getSimpleName(), null);
	}
	
	
	//***************************************************
	public void save(){
		Event event = new Event();
		Group group = new Group();
		Account account = new Account();
	

		if(this.txtGroup.getText().length() <= 0 ){
			Toast.makeText(getActivity(), R.string.needSelectAGroup, Toast.LENGTH_LONG).show();
			
			return;
		}
		
		
		event.setEvnId(this.txtEventName.getText().toString());
		event.setEvnDescription(this.txtDescription.getText().toString());
		
		group.setGrpId(this.txtGroup.getText().toString());
		event.setGroup(group);
		
		account.setAcnId(this.username);
		event.setAccount(account);		
		//this.DatePicker.setCalendarViewShown(false);
		
		//event.setEvnDate(new Date(this.datePicker.getCalendarView().getDate()));
		event.setEvnDate( getDateFromDatePicket(this.datePicker) );
			
		ProgressDialog dialog = ProgressDialog.show(getActivity(), "Saving", "Please wait...", true);
		
		NewEventTask newEventTask = new NewEventTask(event, this.username, dialog);
		ThreadUtilities.executeAsyncTask(newEventTask, getActivity().getApplicationContext());
	
		
		
	}

	
	
	
	//#####################################################################
	//#####################################################################
	private class NewEventTask extends AsyncTask<Context, Void, Void> {
		Event event;
		String username;
		ProgressDialog dialog;

		Throwable exception = null;
		
		public NewEventTask(Event event, String username, ProgressDialog dialog){
			this.event = event;
			this.username = username;
			this.dialog = dialog;
		}

		@Override
		protected Void doInBackground(Context... ctxt) {
			try{
				List<Event> eventsTmp = new ArrayList<Event>();
				
				this.event = NetworkHelper.createNewEvent(this.event, this.username);
				eventsTmp.add(this.event);
				//ThreadUtilities.sleep(3000);
				DatabaseHelper.getInstance(getActivity()).updateEvents(eventsTmp);
				
			}catch(Throwable ex){
				this.exception = ex;
			}
			return (null);
		}

		@Override
		public void onPostExecute(Void arg0) {
			this.dialog.dismiss();
			
			if(this.exception != null){
				if(this.exception instanceof C_NetworkKeyDuplicateException){
					Toast.makeText(getActivity(), R.string.eventNameArleadyUsed, Toast.LENGTH_LONG).show();
				} else {
					ExceptionsUtils.standardManagingException(this.exception, getActivity());
				}
			} else {
				MainDataRetainFragment.staticInstance.addEvent(this.event);
				EventNewFragment.this.eventCreated();
			}
		}
	}// class PrefsLoadTask




	public static Date getDateFromDatePicket(DatePicker datePicker){
	    int day = datePicker.getDayOfMonth();
	    int month = datePicker.getMonth();
	    int year =  datePicker.getYear();

	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month, day);

	    return calendar.getTime();
	}	
	
	
}
