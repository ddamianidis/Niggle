package com.himumsaiddad.niggle;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Application;
import android.database.SQLException;

import com.himumsaiddad.niggle.Settings;


public class NiggleApplication extends Application {
	
		
	public static final String DEBUG = "TRUE";
	
	
	
	private void CreateDB(){
	   // create Niggle DB
	    //DataBaseHelper myDbHelper;
       	Settings.myDbHelper = new DataBaseHelper(this);
       	
	    try {
	    	Settings.myDbHelper.createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
	    
	 	try {
	 		Settings.myDbHelper.openDataBase();
	 	}catch(SQLException sqle){
	 		Settings.myDbHelper.close();
	  		throw sqle;
	 	}
   }


    @Override
    public void onCreate()
    {
        super.onCreate();
        
     // create DB if not exists
        CreateDB();
        
        Settings.initialize(getSharedPreferences("settings", 0));
    }
    
   
    
}
