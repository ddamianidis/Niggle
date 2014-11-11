package com.himumsaiddad.niggle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.himumsaiddad.niggle.myNiggles.myniggles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	
    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    public static void ShowYesNoDialog(Context context, String title, String message,
    		DialogInterface.OnClickListener yesListener, 
    		DialogInterface.OnClickListener noListener){
    	
     	  //create an alterDialog
          AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setTitle(title)
          .setMessage(message)
  	      .setCancelable(false)
  	      .setPositiveButton("Yes", yesListener)
          .setNegativeButton("No", noListener);
          
         builder.create().show();
      }
    
    public static String escapequotes(String s)
    {
    	return DatabaseUtils.sqlEscapeString(s);
    	
    }
    
    public static String[]  escapequotes(String[] s)
    {
    	for(int i=0; i<s.length; i++)
    	{
    		s[i] = DatabaseUtils.sqlEscapeString(s[i]);
       	}
    	return s;
    }

    public static void showNotification(Context context, String text)
    {
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, 3000/*duration*/);
    	toast.show();
    }
    
    public  static void GetFbId(Facebook facebook) throws IOException, JSONException, IOException {
     	 JSONObject me = new JSONObject(facebook.request("me"));
     	 Settings.FACEBOOK_ID = me.getString("id");  	 
     	 Settings.FACEBOOK_USERNAME = me.getString("name");
      }
    
    public static  void share(Facebook facebook, String sMessage, 
    			String sLink, RequestListener r) {
        AsyncFacebookRunner runner = new AsyncFacebookRunner(facebook);
        Bundle params = new Bundle();
        params.putString("message", sMessage);
        params.putString("link", sLink);
       
        runner.request("me/feed", params, "POST", r, null);
        
        
    }
    
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());

      }

}
