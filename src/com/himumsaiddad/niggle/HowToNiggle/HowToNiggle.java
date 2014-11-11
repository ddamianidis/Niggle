package com.himumsaiddad.niggle.HowToNiggle;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.himumsaiddad.niggle.BaseActivity;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.visualiseNiggle.NailThatNiggle;

public class HowToNiggle extends BaseActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                       
        // Set The View Of Main Screen
        setContentView(R.layout.how_to_niggle);
        
       ((ImageButton)findViewById(R.id.nail_that_niggle))
       	.getBackground().setAlpha(0);
       
   /*    ((TextView)findViewById(R.id.text_about))
      	.setMovementMethod(new ScrollingMovementMethod());*/
       
       // params: menu, back, bg, logo, header, video, loop_video, rc_video
       initBaseActivity();
      
       hideBack();
              
       Typeface face=Typeface.createFromAsset(getAssets(), Settings.FONT_ITC);
       
       ((TextView)findViewById(R.id.text_about)).setTypeface(face);
       ((TextView)findViewById(R.id.text_about)).setTextSize(Settings.ABOUT_SIZE);

        
    }
    
    public void OnNailThatNiggleClick(View view)
    {
    	Intent intent = new Intent(HowToNiggle.this, NailThatNiggle.class);
        startActivity(intent);
    }
}