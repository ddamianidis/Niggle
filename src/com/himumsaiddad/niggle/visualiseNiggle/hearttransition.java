package com.himumsaiddad.niggle.visualiseNiggle;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.himumsaiddad.niggle.R;

public class hearttransition extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set The View Of Main Screen
        setContentView(R.layout.heart_transition);
        ImageButton meureka = (ImageButton)findViewById(R.id.eureka);
        meureka.getBackground().setAlpha(0);
    }
    
    public void OnEurekaClick(View v)
    {
    	Intent i = new Intent();
        i.setClassName("com.himumsaiddad.niggle",
                       "com.himumsaiddad.niggle.wisdom");
        startActivity(i);
        super.finish();
    }
}
