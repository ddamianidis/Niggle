package com.himumsaiddad.niggle.visualiseNiggle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.himumsaiddad.niggle.R;



public class inputniggle extends Activity {
    /** Called when the activity is first created. */
				
	private String ScreenState = "KNOW";
	private EditText mEditText;
	private String niggleTitle = "";
	private String WhatIKnow = "";
	private String WhatIDo = "";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.input_niggle);
        
        ImageButton backButton = (ImageButton)findViewById(R.id.back);
        backButton.getBackground().setAlpha(0);
        Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
				
		int wEdit = extras.getInt("whatEdit");
		niggleTitle  = extras.getString("title");
		WhatIKnow  = extras.getString("whatIknow");
		WhatIDo  = extras.getString("whatIdo");
				
		ImageView mknownow = (ImageView)findViewById(R.id.what_i_now_know);
		ImageView mwwilldo = (ImageView)findViewById(R.id.what_ill_now_do);
		mEditText = (EditText)findViewById(R.id.edit_text);
		
		if(wEdit == 1)
		{
			mknownow.setVisibility(View.VISIBLE);
			mwwilldo.setVisibility(View.INVISIBLE);
			ScreenState = "KNOW";
		}else
		{
			mknownow.setVisibility(View.INVISIBLE);
			mwwilldo.setVisibility(View.VISIBLE);
			ScreenState = "DO";
		}
				
    }
    
   
    public void OnBackClick(View v)
    {
    	
    	String toInsert = mEditText.getText().toString();   
    	
    	    	    	    	    	
    	Intent i = new Intent();
        i.setClassName("com.himumsaiddad.niggle",
                       "com.himumsaiddad.niggle.inputreview");
        
        if(ScreenState == "KNOW")
    	{
            i.putExtra("whatIknow", toInsert);
            i.putExtra("title", niggleTitle);
            i.putExtra("whatIdo", WhatIDo);
    	}else
    	{
    		i.putExtra("whatIdo", toInsert);
    		i.putExtra("title", niggleTitle);
            i.putExtra("whatIknow", WhatIKnow);
    	}

        startActivity(i);
        super.finish();
    }

    public void OnSaveClick(View v)
    {
    	Intent i = new Intent();
        i.setClassName("com.himumsaiddad.niggle",
                       "com.himumsaiddad.niggle.myniggles");
        
        i.putExtra("whatIknow", WhatIKnow);
        i.putExtra("title", niggleTitle);
        i.putExtra("whatIdo", WhatIDo);
        startActivity(i);
        super.finish();
    }

    public void OnNoThanksClick(View v)
    {
    	Intent i = new Intent();
        i.setClassName("com.himumsaiddad.niggle",
                       "com.himumsaiddad.niggle.SplashScreen");
        
        i.putExtra("toMenu", "1");
        startActivity(i);
        super.finish();
    }

    
    }

