package com.himumsaiddad.niggle;

import java.util.ArrayList;

import com.himumsaiddad.niggle.intro.SplashScreen;
import com.himumsaiddad.niggle.share.ShareOnFacebookActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class editLayout extends RelativeLayout {
	private int largestHeight;

    public editLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.edit_popup_review, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // Log.d("Search Layout", "Handling Keyboard Window shown");

        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();
        int b = ((LinearLayout)findViewById(R.id.edit_frame_1)).getBottom();
        largestHeight = Math.max(largestHeight, getHeight());

        if (actualHeight > proposedheight){
            // Keyboard is shown
        	Utils.showNotification(this.getContext(), "keboard shown");

        } else {
            // Keyboard is hidden
        	Utils.showNotification(this.getContext(), "keboard hidden");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
