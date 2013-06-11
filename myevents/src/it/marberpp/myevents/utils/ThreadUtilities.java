package it.marberpp.myevents.utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

public class ThreadUtilities {

	//***************************************************
	@TargetApi(11)
	static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}
	
	
	//************************************************
	static public void sleep(int millis){
		try{
			 //SystemClock.sleep(millis);
		}catch (Exception e) {
			Log.d(ThreadUtilities.class.getSimpleName(), "eccezione sulla sleep " + millis);
		}
	}
	
}//class
