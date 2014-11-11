package com.himumsaiddad.niggle;

import java.util.ArrayList;

import com.sugree.twitter.R.id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class EditPopupSingle extends Dialog {
	
	 private EditText editText;
	 private ImageButton sendButton;
	 private View container; 
	 private int  editResId;
	 private int mLayoutResId;
	 private Activity pActivity;
	 private View pView;
	 private Integer counter = 0;
	 private int top_offset;
	 private View scrollView;
	 private boolean firstTime = true;
	 private int enter_pressed = 0;
	 private int ENTER_LIMIT = 0;
	 private boolean noautoComplete;
	 private Handler mHandler = new Handler();
	 private View activityRootView;
	 private boolean scroll_shift_done = false;
	 private OnGlobalLayoutListener mGlobalLayoutListener;
	public EditPopupSingle(Context context, int layoutResId, 
			Activity pActivity, View pView, int top_offset, int editResId) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mLayoutResId = layoutResId;
		this.editResId = editResId;
		this.pActivity = pActivity;	
		this.pView = pView;
		this.top_offset = top_offset;
	}
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(mLayoutResId);
	        setCancelable(false);
	
	        activityRootView = findViewById(R.id.popup_edit_send);
			//
	       /* sendButton = (ImageButton) findViewById(buttonResId);
			sendButton.setOnClickListener(sendLIstener);
			sendButton.getBackground().setAlpha(0);*/
			editText = (EditText) findViewById(editResId);	
			editText.setOnFocusChangeListener(editFocusChangeListener);
			editText.setOnClickListener(editClickListener);
			editText.setOnKeyListener(editKeyListener);

						
			editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
			
			if((editText.getInputType() &
					InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) == 0)
				ENTER_LIMIT = 0;
			else
				ENTER_LIMIT = 1;
			
			
			//ENTER_LIMIT = 0;
			
			scrollView = pView.findViewById(R.id.visualise_scroll);
			
			LinearLayout my_edit_frame = ((LinearLayout) findViewById(R.id.edit_frame));
			ViewGroup.LayoutParams params =  my_edit_frame.getLayoutParams();
			((RelativeLayout.LayoutParams) params).setMargins(0, top_offset, 0, 0);
			my_edit_frame.setLayoutParams(params);
			
			editText.setImeActionLabel("OK", 777);
			InputMethodManager mgr = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			
			// only will trigger it if no physical keyboard is open	
			mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT/*0*/);
		
			mGlobalLayoutListener = new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
				 	
					 if(EditPopupSingle.this.isShowing())
					 {
					    Rect r = new Rect();
					    //r will be populated with the coordinates of your view that area still visible.
					    activityRootView.getWindowVisibleDisplayFrame(r);
	
					    int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
					    if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
					    	if(Settings.DEBUG_MODE) 
					    		Utils.showNotification(EditPopupSingle.this.getContext(),
									  "SHOWN");
					    	
					    	 if(scrollView.getHeight() >= 400 && scroll_shift_done == false)
					    	 {
					    		 setScrollOffset(-250);
					    		 setScrollShiftDone(true);
					    		 
					    	 }
					    }else
					    {
					    	if(Settings.DEBUG_MODE) 
					    		Utils.showNotification(EditPopupSingle.this.getContext(),
					    				"HIDDEN");
					    	
					    	 if(scrollView.getHeight() < 400 && scroll_shift_done == true)
					    	 {
					    		 setScrollOffset(250);
					    		 setScrollShiftDone(false);
					    	 }
					    }
					 }
				 }
				};
				
			activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener); 
		}
	
	public void setScrollShiftDone(boolean l)
	{
		scroll_shift_done = l;
	}
	
	public void setGlobalLayoutListener()
	{
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(/*!this.firstTime*/ counter == 0)
		{
			//pView.requestFocus();
			pActivity.dispatchTouchEvent(event);	
			scrollView.dispatchTouchEvent(event);
			
			if(Settings.DEBUG_MODE)
				Utils.showNotification(this.getContext(), "touch event:"+counter.toString());
			
			counter++;
		}else
		{
			counter = 0;
		}
		return true;
	}
	
	public String getText()
	{
		String s = editText.getText().toString(); 
		return s;
	}
	
		
	public void setText(String value)
	{
		editText.setText(value);
	}
	
	 public void FadeIn( int duration)
    {
      AlphaAnimation FadeInAlpha = new AlphaAnimation(0.0F, 1.0F);
      FadeInAlpha.reset();
      FadeInAlpha.setDuration(duration);
      FadeInAlpha.setFillAfter(true);
      container.startAnimation(FadeInAlpha);
    }
	    
    public void FadeOut( int duration)
    {
      AlphaAnimation FadeOutAlpha = new AlphaAnimation(1.0F, 0.0F);
      FadeOutAlpha.reset();
      FadeOutAlpha.setDuration(duration);
      FadeOutAlpha.setFillAfter(true);
      container.startAnimation(FadeOutAlpha);
    }
    
    EditText.OnKeyListener editKeyListener  = new EditText.OnKeyListener() {
	    @Override
	    public boolean onKey(View v, int keyCode, KeyEvent event) {
	    	
	    	if(enter_pressed == ENTER_LIMIT )
	    	{
		        if (keyCode == KeyEvent.KEYCODE_ENTER ) { 
		            /* do something */ 
		        	 if(EditPopupSingle.this.isShowing())
		        	 {
						 OnSendClick(getText());
						 setText(""); 
		        	 }
		        	 
		        	 enter_pressed = 0;
		        	 
		        	 return true;
		        	
		        }
	    	}else
	    	{
	    		enter_pressed++;
	    	}
	    	
	    	if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
	            
	        	return true; // Pretend we processed it
	        }
	        
	        return false;
	    }
	};

	
	private View.OnClickListener sendLIstener = new View.OnClickListener(){
		 public void onClick(View v) {
			 if(EditPopupSingle.this.isShowing())
				 OnSendClick(getText());
		 }
	};
	
	View.OnClickListener editClickListener = new View.OnClickListener()
	{
		public void	onClick(View v)
		{
			((EditText)v).setText("");
			enter_pressed = 0;
		}
	};
	
	View.OnFocusChangeListener editFocusChangeListener = new View.OnFocusChangeListener()
	{
		public void	onFocusChange(View v, boolean hasFocus)
		{
				if(hasFocus == true && !firstTime){
					
					((EditText)v).setText("");				
				}else
					firstTime = false;
				
			
		}
	};
			
	public abstract void OnSendClick(String value);
	public abstract void setScrollOffset(int top_offset);
	
}
