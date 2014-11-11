package com.himumsaiddad.niggle;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.himumsaiddad.niggle.visualiseNiggle.inputreview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Region.Op;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public abstract class EditPopup extends Dialog {
	
	 private ArrayList<EditText> editTextList = new ArrayList<EditText>();
	 private int[] editList;
	 public View container; 
	 private int mLayoutResId;
	 private Activity pActivity;
	 private View pView;
	 private Integer counter = 0;
	 private int top_offset;
	 private Handler mHandler = new Handler(); 
	 private Configuration config;
	 InputMethodManager mgr;
	 private boolean firstTime = true;
	 private View activityRootView;
	
	public EditPopup(Context context, int layoutResId, 
			Activity pActivity, View pView, int top_offset, int[] editList) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mLayoutResId = layoutResId;
		this.editList = editList;
		this.pActivity = pActivity;	
		this.pView = pView;
		this.top_offset = top_offset;
	}
	
	
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //editLayout theeditLayout = new editLayout(this.getContext(), null);
	        setContentView(mLayoutResId);
	       /*((ImageView)findViewById(R.id.background_review)).setAlpha(0);
	     
	 	*/
	        setCancelable(false);
	        
	        activityRootView = findViewById(R.id.edit_popup_review);
	        
	        container = findViewById(R.id.edit_popup_review);
	        for(int i=0; i<editList.length; i++)
			{
	        	editTextList.add(i, ((EditText)findViewById(editList[i])));
			}
	        
			/*LinearLayout my_edit_frame = ((LinearLayout) findViewById(R.id.edit_frame_1));
			ViewGroup.LayoutParams params =  my_edit_frame.getLayoutParams();
			((RelativeLayout.LayoutParams) params).setMargins(0, top_offset, 0, 0);
			my_edit_frame.setLayoutParams(params);
			*/
			mgr = (InputMethodManager) this.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			
			mgr.restartInput(editTextList.get(1));
			config = this.getContext().getResources().getConfiguration();

			
			for(int i=0; i<editTextList.size(); i++)
			{
				editTextList.get(i).setImeActionLabel("OK", 777);
				
//				editTextList.get(i).setInputType(InputType.TYPE_NULL);
				editTextList.get(i).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
				
				// only will trigger it if no physical keyboard is open
				mgr.showSoftInput(editTextList.get(i), InputMethodManager.SHOW_IMPLICIT);
				editTextList.get(i).setOnFocusChangeListener(editFocusChangeListener);
				editTextList.get(i).setOnClickListener(editClickListener);
				editTextList.get(i).setOnKeyListener(editKeyListener);
				
			}
				
		    ImageButton saveButton = (ImageButton)findViewById(R.id.save);
	        saveButton.getBackground().setAlpha(0);
	        saveButton.setOnClickListener(saveOnClickListener);
	        ImageButton noThanksButton = (ImageButton)findViewById(R.id.no_thanks);
	        noThanksButton.getBackground().setAlpha(0);	
	        noThanksButton.setOnClickListener(noThanksOnClickListener);

	        Typeface face=Typeface.createFromAsset(pActivity.getAssets(),
		    		  Settings.FONT_HELV);
	        ((TextView)findViewById(R.id.title_text_1)).setTypeface(face); 
	        ((TextView)findViewById(R.id.title_text_2)).setTypeface(face);
	        ((TextView)findViewById(R.id.what_i_now_know_text)).setTypeface(face);
	        ((TextView)findViewById(R.id.what_i_ll_now_do_text)).setTypeface(face);
	                
	        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
				    Rect r = new Rect();
				    //r will be populated with the coordinates of your view that area still visible.
				    activityRootView.getWindowVisibleDisplayFrame(r);

				    int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
				    if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
				    	if(Settings.DEBUG_MODE) 
				    		Utils.showNotification(EditPopup.this.getContext(),
				    				"SHOWN");
				    	
				    	 if(editTextList.get(1).hasFocus())
				    	 {	 
				    		 EditPopup.this.
							findViewById(R.id.text_title_layout).setVisibility(View.INVISIBLE);
				    	 }

				    }else
				    {
				    	if(Settings.DEBUG_MODE) 
				    		Utils.showNotification(EditPopup.this.getContext(),
				    				"HIDDEN");
				    	
				    	 EditPopup.this.
							findViewById(R.id.text_title_layout).setVisibility(View.VISIBLE);
				    }
				 }
				}); 

		}
	
	@Override 
	public void onStart()
	{
		super.onStart();
	}
	
	
	
	ResultReceiver onSoftKeyboardHidden = new ResultReceiver(mHandler)
	{
		@Override
		public void onReceiveResult(int resultCode, Bundle resultData)
		{
			if(Settings.DEBUG_MODE)
				Utils.showNotification(EditPopup.this.getContext(), "hidden");
		}
	};
	
	View.OnClickListener editClickListener = new View.OnClickListener()
	{
		public void	onClick(View v)
		{
			((EditText)v).setText("");
			
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
	
	View.OnClickListener saveOnClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			onSave(v);
		}
		
	};
	
	View.OnClickListener noThanksOnClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			onNoThanks(v);
		}
		
	};
	
	EditText.OnKeyListener editKeyListener  = new EditText.OnKeyListener() {
	    @Override
	    public boolean onKey(View v, int keyCode, KeyEvent event) {
	    	
	    		    
	        if (keyCode == KeyEvent.KEYCODE_ENTER) { 
	            /* do something */ 
	        	//onSave(v);
	        	//return true;
	        }else if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
	            
	        	return true; // Pretend we processed it
	        }

	        return false;
	    }
	};

	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		pView.requestFocus();
		pActivity.dispatchTouchEvent(event);	
		//onConfChange();
		//Utils.showNotification(this.getContext(), "touch event:"+counter.toString());
		counter++;
		return false;
	}
	
	
	public String getText(int pos)
	{
		String s = editTextList.get(pos).getText().toString(); 
		return s;
	}
	
	public void setText(String value, int pos)
	{
		editTextList.get(pos).setText(value);
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
    
    public void onConfChange()
    {
    	if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES)				
		{
		
		   //Force virtual keyboard to appear,
		
		   //assuming it is now closed
		
			mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			
			EditPopup.this.
			findViewById(R.id.text_title_layout).setVisibility(View.VISIBLE);
		
		}
    }
    
    abstract public  void onSave(View v);
    
    abstract public void onNoThanks(View v);
    
   
}
