package com.himumsaiddad.niggle.intro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ProgressBarImageView extends ImageView {

    private float mProgress = 0.0f;
    
    public ProgressBarImageView(Context context) {
        super(context);
    }
    
    public ProgressBarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ProgressBarImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect(0,0,(int)(mProgress*canvas.getWidth()),canvas.getHeight());
        canvas.clipRect(rect, Op.REPLACE);
        super.onDraw(canvas);
    }
    
    public void setProgress(float progress) {
        mProgress = progress;
        invalidate();
    }

}
