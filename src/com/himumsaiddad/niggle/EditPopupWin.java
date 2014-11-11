package com.himumsaiddad.niggle;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public abstract class EditPopupWin extends PopupWindow {
	
	 private ArrayList<EditText> editTextList = new ArrayList<EditText>();
	 private int[] editList;
	 private View container; 
	 private int mLayoutResId;
	 private Activity pActivity;
	 private View pView;
	 private Integer counter = 0;
	 private int top_offset;
	 private Handler mHandler = new Handler(); 
	 private Configuration config;
	 private Context mContext;
	 public View edit_layout;
	 InputMethodManager mgr;

	
	public EditPopupWin(Context context, int layoutResId, 
			Activity pActivity, View pView, int top_offset, int[] editList) {
		super(context);
		mLayoutResId = layoutResId;
		this.editList = editList;
		this.pActivity = pActivity;	
		this.pView = pView;
		this.top_offset = top_offset;
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 

		
		edit_layout = inflater.inflate(mLayoutResId, null);

		//pw.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);

		
		this.setContentView(edit_layout);
		
		init();
	}
	
	
	
	
		void init() {
	
	        
	        //editLayout theeditLayout = new editLayout(this.getContext(), null);
	        
	       /*((ImageView)findViewById(R.id.background_review)).setAlpha(0);
	        setCancelable(false);
	 	*/
	        
	        
	        for(int i=0; i<editList.length; i++)
			{
	        	editTextList.add(i, ((EditText)edit_layout.findViewById(editList[i])));
			}
	        
			/*LinearLayout my_edit_frame = ((LinearLayout) findViewById(R.id.edit_frame_1));
			ViewGroup.LayoutParams params =  my_edit_frame.getLayoutParams();
			((RelativeLayout.LayoutParams) params).setMargins(0, top_offset, 0, 0);
			my_edit_frame.setLayoutParams(params);
			*/
			mgr = (InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			//mgr.showSoftInput(editTextList.get(1), InputMethod.SHOW_EXPLICIT, onSoftKeyboardShown);
			mgr.restartInput(editTextList.get(1));
			config = mContext.getResources().getConfiguration();

			
			for(int i=0; i<editTextList.size(); i++)
			{
				//editTextList.get(i).setImeActionLabel("OK", 777);
				
//				editTextList.get(i).setInputType(InputType.TYPE_NULL);
				// only will trigger it if no physical keyboard is open
				mgr.showSoftInput(editTextList.get(i), InputMethodManager.SHOW_IMPLICIT);
		//		mgr.showSoftInput(editTextList.get(i), InputMethodManager.RESULT_HIDDEN, onSoftKeyboardHidden);
				//mgr.showSoftInput(editTextList.get(i), InputMethodManager.RESULT_SHOWN, onSoftKeyboardShown);
				
				//editTextList.get(i).setOnFocusChangeListener(editFocusChangeListener);
				//editTextList.get(i).setOnClickListener(editClickListener);
				//editTextList.get(i).setOnKeyListener(editKeyListener);
			}
			
			
			
			
		    ImageButton saveButton = (ImageButton)edit_layout.findViewById(R.id.save);
	        saveButton.getBackground().setAlpha(0);
	        saveButton.setOnClickListener(saveOnClickListener);
	        ImageButton noThanksButton = (ImageButton)edit_layout.findViewById(R.id.no_thanks);
	        noThanksButton.getBackground().setAlpha(0);	
	        noThanksButton.setOnClickListener(noThanksOnClickListener);

	        Typeface face=Typeface.createFromAsset(pActivity.getAssets(),
		    		  Settings.FONT_HELV);
	        ((TextView)edit_layout.findViewById(R.id.title_text_1)).setTypeface(face); 
	        ((TextView)edit_layout.findViewById(R.id.title_text_2)).setTypeface(face);
	        ((TextView)edit_layout.findViewById(R.id.what_i_now_know_text)).setTypeface(face);
	        ((TextView)edit_layout.findViewById(R.id.what_i_ll_now_do_text)).setTypeface(face);
	        
			/*WindowManager.LayoutParams WMLP = this.getWindow().getAttributes();


			 WMLP.x = 10;   //x position
			 WMLP.y = top_offset;   //y position

			 this.getWindow().setAttributes(WMLP);
*/
	      // ((RelativeLayout) findViewById(R.id.input_review_popup)).setMargins(0, 750, 0, 0);
	        
	        editTextList.get(1).setOnEditorActionListener(new TextView.OnEditorActionListener() {
	            @Override
	            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	                if (actionId == EditorInfo.IME_ACTION_DONE) {

	                	Utils.showNotification(EditPopupWin.this.mContext, "hidden");

	                    return true;
	                }
	                return false;
	            }
	        });

	        checkKeyboard.start();
	        
	        this.setTouchInterceptor(onTouchListener);
	        
	        //showAtLocation(edit_layout, Gravity.CENTER, 0, 0);
		}
	
	View.OnTouchListener onTouchListener = new View.OnTouchListener()
	{
		public boolean onTouch(View v, MotionEvent event)
		{
			pView.requestFocus();
			pActivity.dispatchTouchEvent(event);	
			//onConfChange();
			//Utils.showNotification(this.getContext(), "touch event:"+counter.toString());
			counter++;
			return false;
		}

	};
	
	ResultReceiver onSoftKeyboardHidden = new ResultReceiver(mHandler)
	{
		@Override
		public void onReceiveResult(int resultCode, Bundle resultData)
		{
			Utils.showNotification(EditPopupWin.this.mContext, "hidden");
		}
	};
	
	View.OnClickListener editClickListener = new View.OnClickListener()
	{
		public void	onClick(View v)
		{
			
			if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES)				
			{
			
			   //Force virtual keyboard to appear,
			
			   //assuming it is now closed
			
				mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				
				EditPopupWin.this.edit_layout.
				findViewById(R.id.text_title_layout).setVisibility(View.INVISIBLE);
			
			}


		}
	};
	
	View.OnFocusChangeListener editFocusChangeListener = new View.OnFocusChangeListener()
	{
		public void	onFocusChange(View v, boolean hasFocus)
		{
			if(mgr.isActive())
			{
				if(hasFocus == false &&
						config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES)
				{
					
					EditPopupWin.this.checkKeyboard.stop();
					EditPopupWin.this.edit_layout.findViewById(R.id.text_title_layout).setVisibility(View.VISIBLE);
				}
				
			}
		}
	};
	
	View.OnClickListener saveOnClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			onSave(v);
			EditPopupWin.this.checkKeyboard.stop();
		}
		
	};
	
	View.OnClickListener noThanksOnClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			onNoThanks(v);
			EditPopupWin.this.checkKeyboard.stop();
		}
		
	};
	
	EditText.OnKeyListener editKeyListener  = new EditText.OnKeyListener() {
	    @Override
	    public boolean onKey(View v, int keyCode, KeyEvent event) {
	    	
	    	Integer kc = new Integer(keyCode);
	    
	    	Utils.showNotification(EditPopupWin.this.mContext, kc.toString());
	        if (keyCode == KeyEvent.KEYCODE_1) { 
	            /* do something */ 
	        	EditPopupWin.this.edit_layout.
				findViewById(R.id.text_title_layout).setVisibility(View.VISIBLE);
	        }
	        return false;
	    }
	};

	
	
	public boolean onTouchEvent(View v, MotionEvent event)
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
			
			EditPopupWin.this.edit_layout.
			findViewById(R.id.text_title_layout).setVisibility(View.VISIBLE);
		
		}
    }
    
    
    public Thread checkKeyboard  = new Thread()
    {
    	public void run()
    	{
    		while(true)
    		{
    			
    			mHandler.post(new Runnable()
    			{
    				
    				  @Override
    			       public void run() {
    					  
				    		if (EditPopupWin.this.mgr.isActive())
				    		{
				    			EditPopupWin.this.edit_layout.
				    			findViewById(R.id.text_title_layout).setVisibility(View.INVISIBLE);
				
				    		}else
				    		{
				    			EditPopupWin.this.edit_layout.
				    			findViewById(R.id.text_title_layout).setVisibility(View.VISIBLE);
				
				    		}
    				  }
    			});
    			
	    		try
	    		{
	    			sleep(100);
	    		}catch(Exception e)
	    		{
	    			
	    		}
    		}
    	}
    };
    
    abstract public  void onSave(View v);
    
    abstract public void onNoThanks(View v);
    
   
}
