package com.himumsaiddad.niggle.visualiseNiggle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.Util;
import com.himumsaiddad.niggle.BaseActivity;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.Utils;
import com.himumsaiddad.niggle.visualiseNiggle.VisualiseNiggle;

public class NailThatNiggle extends BaseActivity {
	
	private static final int SHAKE_THRESHOLD = 2;

	private SensorManager mSensorManager;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity
	  
	  private boolean isShaking = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                       
        // Set The View Of Main Screen
        setContentView(R.layout.nail_that_niggle);
        
       ((ImageButton)findViewById(R.id.wiggle_phone_image))
       	.getBackground().setAlpha(0);
              
       initBaseActivity();
       
       // hide back button which is not included in How to Niggle Activity
       hideBack();
       
       mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
       mSensorManager.registerListener(pSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
       mAccel = 0.00f;
       mAccelCurrent = SensorManager.GRAVITY_EARTH;
       mAccelLast = SensorManager.GRAVITY_EARTH;
       
       Typeface face=Typeface.createFromAsset(getAssets(), Settings.FONT_ITC);
      
       ((TextView)findViewById(R.id.text_wiggle)).setTypeface(face);
       ((TextView)findViewById(R.id.text_wiggle)).setTextSize(Settings.Q_A_2_SIZE);
    }
    
    @Override
    protected void onResume() {
      super.onResume();
      mSensorManager.registerListener(pSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
      isShaking = false;
    }

    @Override
    protected void onStop() {
      mSensorManager.unregisterListener(pSensorListener);
      super.onStop();
    }

    SensorEventListener pSensorListener = new SensorEventListener()
   {
    	public void onSensorChanged(SensorEvent se) {
    	      float x = se.values[0];
    	      float y = se.values[1];
    	      float z = se.values[2];
    	      mAccelLast = mAccelCurrent;
    	      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
    	      float delta = mAccelCurrent - mAccelLast;
    	      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
    	      
    	      if(mAccel > SHAKE_THRESHOLD && isShaking == false)
    	      {
    	    	 // Utils.showNotification(NailThatNiggle.this, "Shake!!!");
    	    	  Intent intent = new Intent(NailThatNiggle.this, VisualiseNiggle.class);
    	          startActivity(intent);
    	          isShaking = true;
    	      }
    	    }

	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }


	   };
	   
    public void OnWiggleClick(View view)
    {
    	Intent intent = new Intent(NailThatNiggle.this, VisualiseNiggle.class);
        startActivity(intent);
    }
}