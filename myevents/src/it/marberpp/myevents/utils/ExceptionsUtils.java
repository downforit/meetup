package it.marberpp.myevents.utils;

import it.marberpp.myevents.R;
import mymeeting.exceptions.C_DatabaseException;
import mymeeting.exceptions.C_NetworkComunicationException;
import mymeeting.exceptions.C_NetworkLoginException;
import mymeeting.exceptions.C_NetworkResponseException;
import mymeeting.exceptions.C_UnexpectedException;
import android.content.Context;
import android.widget.Toast;

public class ExceptionsUtils {

	public static final void standardManagingException(Throwable ex, Context context){
		
		ex.printStackTrace();
			
		if( ex instanceof C_DatabaseException){
			Toast.makeText(context, R.string.databaseErrorMessage, Toast.LENGTH_LONG).show();
		} else if(ex instanceof C_NetworkComunicationException){
			Toast.makeText(context, R.string.networkFailedMessage, Toast.LENGTH_LONG).show();
		} else if(ex instanceof C_NetworkLoginException){
			Toast.makeText(context, R.string.loginFailedMessage, Toast.LENGTH_LONG).show();
		} else if(ex instanceof C_NetworkResponseException){
			Toast.makeText(context, R.string.serverErrorMessage, Toast.LENGTH_LONG).show();
		} else if(ex instanceof C_UnexpectedException){
			Toast.makeText(context, R.string.unexpectedErrorMessage, Toast.LENGTH_LONG).show();
		} else{
			Toast.makeText(context, R.string.unexpectedErrorMessage, Toast.LENGTH_LONG).show();
		}
		
	}
	
}
