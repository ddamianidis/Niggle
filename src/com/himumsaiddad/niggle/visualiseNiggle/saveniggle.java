package com.himumsaiddad.niggle.visualiseNiggle;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.himumsaiddad.niggle.DataBaseHelper;
import com.himumsaiddad.niggle.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.himumsaiddad.niggle.R;


public class saveniggle extends Activity {
    /** Called when the activity is first created. */
				
	public DataBaseHelper myDbHelper ;
	
	private EditText mEditText;
	private String niggleTitle = "";
	private String WhatIKnow = "";
	private String WhatIDo = "";
	public String[] str = new String[4];
	String toInsert;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.save_niggle);
        mEditText = (EditText)findViewById(R.id.edit_text);
  		
		//myDbHelper = new DataBaseHelper(this);
	     //myDbHelper.openDataBase();
        Settings.myDbHelper.myDataBase = Settings.myDbHelper.getWritableDatabase();
	    
        Bundle extras = getIntent().getExtras();
  		if (extras == null) {
  			return;
  		}
  		
  		
  		niggleTitle  = extras.getString("title");
  		WhatIKnow  = extras.getString("whatIknow");
  		WhatIDo  = extras.getString("whatIdo");	
						
    }
    
    public void OnBackClick(View v)
    {
    	Intent i = new Intent();
        i.setClassName("com.himumsaiddad.niggle",
                       "com.himumsaiddad.niggle.inputreview");
        i.putExtra("whatIdo", WhatIDo);
    	i.putExtra("title", niggleTitle);
        i.putExtra("whatIknow", WhatIKnow);
        
        startActivity(i);
        super.finish();
    }

    public void OnDoneClick(View v)
    {
    	
    	 toInsert = mEditText.getText().toString();   
    	 
    	//toInsert = "something to edit";
    	Calendar cal = Calendar.getInstance();
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String now = sdf.format(cal.getTime());
        
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, now, duration);
        toast.show();
        
    	str[0] = toInsert;
    	str[1] = now;
    	str[2] = WhatIKnow;
    	str[3] = WhatIDo;
    	
    	
    	Settings.myDbHelper.InsertNiggle(str);
    	
    	//Settings.myDbHelper.close();
   	    
    	Intent i = new Intent();
        i.setClassName("com.himumsaiddad.niggle",
                       "com.himumsaiddad.niggle.inputreview");
        i.putExtra("whatIdo", WhatIDo);
    	i.putExtra("title", toInsert);
        i.putExtra("whatIknow", WhatIKnow);
        i.putExtra("title", toInsert);
    	
        startActivity(i);
        super.finish();
    }

      
    }

