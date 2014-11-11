package com.himumsaiddad.niggle.visualiseNiggle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class TextBoxView extends View {

    private float[] outerR = new float[] { 12, 12, 12, 12, 0, 0, 0, 0 };
    private int x = 0;
    private int y = 0;
    private int width = 300;
    private int height = 50;
    private int color = 0xa4c55e;
    private int alpha = 85;
    private float radius = 10f;
    private float dx = 1f;
    private float dy = 1f;
    
    public TextBoxView(Context context) {
        super(context);
    }
    
    public TextBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public TextBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setX(int x){
    	this.x = x;
    }
    
    public void setY(int y){
    	this.y = y;
    }

    public void setWidth(int width){
    	this.width = width;
    }
    	
    public void setHeight(int height){
    	this.height = height;
    }

    protected void onDraw(Canvas canvas) {
    	ShapeDrawable sd =new ShapeDrawable(new RoundRectShape(outerR, null,
                null));
        Paint p = sd.getPaint();
        p.setColor(color);
        p.setAlpha(alpha);
        p.setShadowLayer (radius, dx, dy, 0x000000);
        sd.setBounds(x, y, x+width, y+height);
        sd.draw(canvas);
        super.onDraw(canvas);
    }
    
    
}
