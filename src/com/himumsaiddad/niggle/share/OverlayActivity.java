package com.himumsaiddad.niggle.share;


import com.himumsaiddad.niggle.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;



public class OverlayActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlay_activity);
        
        findViewById(R.id.button_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
      /*  ((ImageView)findViewById(R.id.title)).setImageResource(getTitleResourceId());
        
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getContentResourceId(), (ViewGroup)findViewById(R.id.content));
    */
    }
    
    /*protected abstract int getTitleResourceId();
    protected abstract int getContentResourceId();*/
}
