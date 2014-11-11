package com.himumsaiddad.niggle.visualiseNiggle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageViewMasked extends ImageView {

    private int mLeft = 0;
    private int mTop = 0;
    private int mBottom = 0;
    private int mRight = 0;
    
    public ImageViewMasked(Context context) {
        super(context);
    }
    
    public ImageViewMasked(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ImageViewMasked(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect(mLeft, mTop, mRight, mBottom);
        canvas.clipRect(rect, Op.REPLACE);
        super.onDraw(canvas);
    }
    
    public void setMargins(int mleft, int mtop,  int mright, int mbottom) {
        mLeft = mleft;
        mTop = mtop;
        mRight = mright;
        mBottom = mbottom;
        invalidate();
    }

}
