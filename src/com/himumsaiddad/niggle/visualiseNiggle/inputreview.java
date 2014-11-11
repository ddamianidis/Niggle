package com.himumsaiddad.niggle.visualiseNiggle;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.himumsaiddad.niggle.BaseActivity;
import com.himumsaiddad.niggle.DataBaseHelper;
import com.himumsaiddad.niggle.EditPopup;
import com.himumsaiddad.niggle.EditPopupWin;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.Utils;
import com.himumsaiddad.niggle.intro.SplashScreen;
import com.himumsaiddad.niggle.myNiggles.myniggles;




public class inputreview extends BaseActivity {
    /** Called when the activity is first created. */
	
	private String niggleTitle = "";
	private Integer niggleId;
	private DataBaseHelper myDbHelper;
	private RelativeLayout m_input_review_layout;
	private EditPopup myedit;
	Cursor mcursor;
	private int edit1;
	private int edit2;
	public int[] editList =  new int[2];
	private Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.input_review);
  
        initBaseActivity();
        hideBack();
        hideMenu();
        
        //myDbHelper = new DataBaseHelper(this);
        //myDbHelper.openDataBase();
        Settings.myDbHelper.myDataBase = Settings.myDbHelper.getReadableDatabase();
        
        m_input_review_layout = (RelativeLayout)findViewById(R.id.inputreview1);
        
       
        edit1 =  R.id.edit_text_1;               
        edit2 =  R.id.edit_text_2;
        
       editList[0] = edit1;
       editList[1] = edit2;
        
           
        myedit = new EditPopup(this, R.layout.edit_popup_review, 
		  		this, m_input_review_layout, 265,
		  		editList) {
        	
        	@Override
        	public void onSave(View v)
        	{
        		OnSaveClick(v);
        	}
        	
        	@Override
        	public void onNoThanks(View v)
        	{
        		OnNoThanksClick(v);
        	}
        	
        	
	    
        };
		  	
		  
	    // WhatIKnow = myedit1.getText();
	     //WhatIDo = myedit2.getText();
	     niggleTitle = Settings.NiggleText;
	    
	    // ((LinearLayout)findViewById(R.id.header_layout)).bringToFront();
	     
	     /*mHandler.postDelayed(new Runnable(){
	    	
	    	 @Override
		       public void run() {
	    		 
	    		 myedit.showAtLocation(myedit.edit_layout, Gravity.CENTER, 0, 0);
	    	 }
	    	 
	     }, 100);*/
	     
	     myedit.show();
    }
    @Override
    public void onStart()
    {
    	super.onStart();
    	
    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    myedit.onConfChange();
	}
    
    @Override
   	protected void onDestroy() {
   		super.onDestroy();
   		
   		if(mcursor != null)
   			mcursor.close();
   		
   		myedit.dismiss();
   		/*if(vPlayer != null)
   		{
   		  vPlayer.releaseMediaPlayer();
   		  vPlayer.doCleanUp();
   		}*/
   		//unregisterReceiver(intentReceiver);
   	}	
       
       @Override
   	protected void onStop() {
   		super.onStop();
   		if(mcursor != null)
   			mcursor.close();
   		
   		myedit.dismiss();
   		
   		/*if(vPlayer != null)
   		{
   		  vPlayer.releaseMediaPlayer();
   		  vPlayer.doCleanUp();
   		}*/
   	}



        
    public void OnSaveClick(View v)
    {	
    	
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String now = sdf.format(cal.getTime());

    	String[] input_niggle = new String[4];
    	input_niggle[0] = niggleTitle;
    	input_niggle[1] = now;
    	input_niggle[2] = myedit.getText(0);
    	input_niggle[3] = myedit.getText(1);
    	
    	Settings.myDbHelper.InsertNiggle(input_niggle);
    	mcursor = Settings.myDbHelper.ReadNiggleLastId();
    	mcursor.moveToFirst();
    	niggleId = mcursor.getInt(0);
    	
    	String[] input_answer = new String[4];
         
        input_answer[0] = niggleId.toString();
        input_answer[2] = now;
    	
    	for(int i=0; i<Settings.niggleAnswers.size(); i++)
    	{
    		Integer an = Settings.niggleAnswers.get(i).mquestionID;
    		input_answer[1] = an.toString();
    		input_answer[3] = Settings.niggleAnswers.get(i).mtext;
    		
    		Settings.myDbHelper.InsertAnswer(input_answer);
    	}
    	Intent intent = new Intent(inputreview.this, wisdom.class);
        startActivity(intent);
      }

    public void OnNoThanksClick(View v)
    {
    	Intent intent = new Intent(inputreview.this, wisdom.class);
        startActivity(intent);
    }
    
    private void  setLayoutParams(View v, boolean hasFocus)
    {
    	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
    											LayoutParams.WRAP_CONTENT);
    	
    	if(hasFocus)
    	{
    		lp.topMargin = v.getTop() - 170;
    		
    	}else
    	{
    		lp.topMargin = v.getTop() + 170;;
    	}
    	lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	v.setLayoutParams(lp);
    }
    
      
    View.OnFocusChangeListener EditChangeFocusListener = new View.OnFocusChangeListener(){
        
    	public void onFocusChange(View v, boolean hasFocus)
    	{
    		Integer b = new Integer(myedit.container.getMeasuredHeight());
    		setLayoutParams (m_input_review_layout, hasFocus);
    		//Utils.showNotification(inputreview.this, b.toString());
    		if(hasFocus == true)
    		{
    	//		m_edit_scroll.scrollTo(0, m_edit_scroll.getBottom());
    		}else
    		{
    			
    		}
    	}
    	
    };

    
}