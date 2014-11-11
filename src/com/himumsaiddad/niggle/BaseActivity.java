package com.himumsaiddad.niggle;

import java.util.ArrayList;

import com.himumsaiddad.niggle.intro.SplashScreen;
import com.himumsaiddad.niggle.share.ShareOnFacebookActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class BaseActivity extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
    }
    
    protected void initBaseActivity()
    {
    	((ImageButton)findViewById(R.id.menu))
       	.getBackground().setAlpha(0);
    	
    	
    	((ImageButton)findViewById(R.id.menu))
    	.setOnClickListener(mMenuClickListener);
    	
    	
    	/*((ImageButton)findViewById(R.id.menu))
    	.setOnTouchListener(mMenuTouchListener);
    	*/
        ((ImageButton)findViewById(R.id.back))
       	.getBackground().setAlpha(0);
        
       
    	((ImageButton)findViewById(R.id.back))
    	.setOnClickListener(mBackClickListener);
    	

    }
    protected void hideBack()
    {
    	((ImageButton)findViewById(R.id.back))
        .setVisibility(View.INVISIBLE);
    }
    
    protected void hideMenu()
    {
        ((ImageButton)findViewById(R.id.menu))
        .setVisibility(View.INVISIBLE);
    }

 // Create an anonymous implementation of OnClickListener
	/*	private OnTouchListener mMenuTouchListener = new OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent e) {
		        		    			    	
		    	Intent intent = new Intent(BaseActivity.this, SplashScreen.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	intent.putExtra("frmenu", "1");
		        startActivity(intent);
	            return true;
		    }
		};
		*/
 // Create an anonymous implementation of OnClickListener
  		private OnClickListener mMenuClickListener = new OnClickListener() {
  		    public void onClick(View v) {
  		        		    			    	
  		    	Intent intent = new Intent(BaseActivity.this, SplashScreen.class);
  		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
  		    	intent.putExtra("frmenu", "1");
  		        startActivity(intent);
  	           
  		    }
  		};

  	// Create an anonymous implementation of OnClickListener
  		private OnClickListener mBackClickListener = new OnClickListener() {
  		    public void onClick(View v) {
  		         	
  		    	finish();  	           
  		    }
  		};
	
}
