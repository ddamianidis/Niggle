package com.himumsaiddad.niggle.visualiseNiggle;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.himumsaiddad.niggle.DataBaseHelper;
import com.himumsaiddad.niggle.EditPopup;
import com.himumsaiddad.niggle.EditPopupSingle;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.Utils;
import com.himumsaiddad.niggle.VideoPlayer;
import com.himumsaiddad.niggle.mAnswer;
import com.himumsaiddad.niggle.mQuestion;
import com.himumsaiddad.niggle.BaseActivity;
import com.himumsaiddad.niggle.intro.SplashScreen;


 
public class VisualiseNiggle extends BaseActivity {
   
  final int START_STATE = 1;
  final int ZONEIN_STATE = 2;
  final int QUESTION_STATE = 3;
  
  final int TOTAL_TIMEOUT = 50000; //20 sec's
  final int STEP = 40; //40 millisec's
  final int FIRST_SCREEN_TIMEOUT = 5000; //1 secs
  final int SECOND_SCREEN_TIMEOUT = 6000; //6 secs 
  
  private VideoPlayer vPlayer;
  private boolean videoCompleted = false;
  private LinearLayout list_container;
  private LayoutInflater inflater;
  private boolean isFirstSend = true;
  private int question_number = 0;
  private ScrollView mScrollView;
  public Handler mHandler = new Handler();
  public EditPopupSingle myedit;
  
  float start_point;
  float end_point;
  GestureDetector gestureDetector;

  Object mSuspend  = new Object();
  
  volatile boolean stop = false;
  
  private DataBaseHelper myDbHelper;
    
  private ArrayList<mQuestion> niggleQuestions;
    
  public int waited;
  public int imageRec;
  public int display_state = START_STATE;
  public int qNum = 0;
  
    ArrayList<mQuestion> GetQuestions()
    {
      Random generator = new Random();
      long SelectedItem;
      ArrayList<mQuestion> returnedArray = new ArrayList<mQuestion>();
      long CountOfQuestions = Settings.myDbHelper.CountTableEntries("ZQUESTION");
      Cursor LinkToCursor;
      Cursor QuestionCursor;
      
      if(CountOfQuestions ==0)
        return null;
      
      while(returnedArray.size() <= 11) 
      {
        if(returnedArray.size() != 0)
        {
                // Get previous question ID
        		mQuestion pq = returnedArray.get(returnedArray.size() - 1);
                int previousQuestionID =pq.questionID;
                
             // Check if there are questions linked to the previous question picked
                LinkToCursor =  Settings.myDbHelper.ReadQuestionsLinked(previousQuestionID);              
                if(LinkToCursor.getCount() > 0)
                {
                  LinkToCursor.moveToLast();
                  mQuestion lq = new mQuestion();
                  lq.questionID = LinkToCursor.getInt(0);
                  lq.linkTO = LinkToCursor.getInt(1);
                  lq.text = LinkToCursor.getString(2);
                  returnedArray.add(lq);
                  continue;
                }
        }
      // Get random question ID make sure the same question is not picked twice
        long currentCount = returnedArray.size();
        long r = Math.abs(generator.nextInt());  
        SelectedItem = (r % CountOfQuestions) + 1;
        int i = 0;
        while (i < currentCount)
        {
          mQuestion sq = returnedArray.get(i);
            if (SelectedItem == sq.questionID)
            {
              r = Math.abs(generator.nextInt());
              SelectedItem = (r % CountOfQuestions) + 1;
                i = -1;
            }
            i++;
        }
        while (true) 
        {
            // Get the question object
          QuestionCursor = Settings.myDbHelper.ReadQuestion(SelectedItem);
          mQuestion q; 
             if(QuestionCursor.getCount() > 0)
             {
              QuestionCursor.moveToLast();
                q = new mQuestion();
              q.questionID = QuestionCursor.getInt(0);
              q.linkTO = QuestionCursor.getInt(1);
              q.text = QuestionCursor.getString(2);
              
             }else
               return null;
                             
            if (q.linkTO != -1)
            {
              SelectedItem = q.linkTO;
                continue;
            }
            
            returnedArray.add(q);
            break;
        }
      }
      return returnedArray;
      
    }

    private void FadeIn(View v, int duration)
    {
      AlphaAnimation FadeInAlpha = new AlphaAnimation(0.0F, 1.0F);
      FadeInAlpha.reset();
      FadeInAlpha.setDuration(duration);
      FadeInAlpha.setFillAfter(true);
      v.startAnimation(FadeInAlpha);
    }
    
    private void FadeOut(View v, int duration)
    {
      AlphaAnimation FadeOutAlpha = new AlphaAnimation(1.0F, 0.0F);
      FadeOutAlpha.reset();
      FadeOutAlpha.setDuration(duration);
      FadeOutAlpha.setFillAfter(true);
      v.startAnimation(FadeOutAlpha);
    }

    private void init_interface()
    {
    	FadeOut(((RelativeLayout)findViewById(R.id.visualise_base_activity)), 0);
    	
    	FadeOut(((ImageButton)findViewById(R.id.nailed_it_small)), 0);
    	
    	FadeOut(((LinearLayout)findViewById(R.id.qa_list)), 0);
    	
    	myedit.dismiss();
    	
    	FadeOut(((LinearLayout)findViewById(R.id.flow_buttons)), 0);
    	
    	FadeOut(((RelativeLayout)findViewById(R.id.zone_in_box)), 8000);
    }
    
    private Runnable SetSequenceInterface = new Runnable() {
       
      @Override
       public void run() {
       
    	  
    	  
    	  // or fade in
    	  FadeIn(((RelativeLayout)findViewById(R.id.visualise_base_activity)),
    			  	2000);
          
    	  FadeIn(((ImageButton)findViewById(R.id.nailed_it_small)),
  			  	2000);

    	  FadeIn(((LinearLayout)findViewById(R.id.qa_list)), 2000);
    	
    	  myedit.show();
    
    	  ((ImageButton)findViewById(R.id.nail_it)).setOnClickListener(nailitClickListener);
          ((ImageButton)findViewById(R.id.keep_going)).setOnClickListener(keepgoingClickListener); 
          ((ImageButton)findViewById(R.id.nailed_it_small)).setOnClickListener(nailitClickListener);

    }
  };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
     /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, 
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
     */
      setContentView(R.layout.visualise_niggle);
      
      // params: menu, back, bg, logo, header, video, loop_video, rc_video
      initBaseActivity();
      hideBack();  
      
      ((ImageButton)findViewById(R.id.menu))
  		.setOnClickListener(mMenuClickListener);
      
      SurfaceView sv = (SurfaceView) findViewById(R.id.surface_view);
      
      // in case video sre disabled
      mHandler.postDelayed(SetSequenceInterface, 8000);
  	 
      /*vPlayer = new VideoPlayer(this, false, sv, R.raw.vortex){
      	  
        	public void OnComplete()
        	{
        		mHandler.postDelayed(SetSequenceInterface, 100);
        		videoCompleted = true;
        	}
        	  
      };*/
      
      videoCompleted = false;
            
      myedit = new EditPopupSingle(this, R.layout.edit_popup_with_send, 
		  		this, ((RelativeLayout)findViewById(R.id.visualise_niggle)), 700,
		  		R.id.edit_text) {
	   
	   public void OnSendClick(String value) {
		   submitText(value);
	   }
	   	   
	   	   
	   public void setScrollOffset(int top_offset)
	   {
		   setVisualizeScrollOffset(top_offset);
	   }
	    
	  };
   	 // myedit.setOwnerActivity(this);
   	
   	
      ((ImageButton)findViewById(R.id.nail_it)).getBackground().setAlpha(0);
      ((ImageButton)findViewById(R.id.keep_going)).getBackground().setAlpha(0); 
      ((ImageButton)findViewById(R.id.nailed_it_small)).getBackground().setAlpha(0);

    
      //myDbHelper = DataBaseHelper.instance(this);
      //myDbHelper = new DataBaseHelper(this);
      //myDbHelper.openDataBase();
      Settings.myDbHelper.myDataBase = Settings.myDbHelper.getReadableDatabase();
            
      init_interface();
   
      //mHandler.postDelayed(SetSequenceInterface, 5000);
            
      // release previous data
      if(niggleQuestions != null)
    	  niggleQuestions =null;
      
      niggleQuestions = GetQuestions();  
      
      Settings.niggleAnswers.clear();
      
      list_container = (LinearLayout)findViewById(R.id.qa_list);
      inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      
      ((RelativeLayout)findViewById(R.id.visualise_niggle)).bringToFront();
            
      mScrollView =  ((ScrollView)findViewById(R.id.visualise_scroll));
      gestureDetector = new GestureDetector(new MyGestureDetector());
      mScrollView.setOnTouchListener(scrollTouchListener);
      
  }
    
    
  public void onNailItClick()
  {
	  if(Settings.NiggleText == null || question_number < 1)
	  {
		  Utils.showAlert(this, getString(R.string.invalid_input_message_title),
				  getString(R.string.invalid_input_message_text));
		  return;
	  }
	  
	  Intent intent = new Intent(VisualiseNiggle.this, inputreview.class);
      startActivity(intent);
  }
  
  
  public void onKeepGoingClick()
  {
	  //list_container.removeAllViews();

	  // release previous data
      if(niggleQuestions != null)
    	  niggleQuestions = null;

	  niggleQuestions = GetQuestions();
	  question_number = 0;
	  
	  ListItem l1 = new ListItem(niggleQuestions.get(0).text, -1, 
			  Settings.FONT_ITC, true);
	  l1.buildViewItem();

	  //FadeIn(((LinearLayout)(myedit.d.findViewById(R.id.edit_frame))), 0);
	  myedit.setScrollShiftDone(false);
	  myedit.setGlobalLayoutListener();
	  myedit.show();
	  
	  FadeOut(((LinearLayout)findViewById(R.id.flow_buttons)), 0);
	  ((ImageButton)findViewById(R.id.nail_it)).setClickable(false);
	  ((ImageButton)findViewById(R.id.keep_going)).setClickable(false);
	  
  }

  public void setVisualizeScrollOffset(int top_offset)
  {
		
		ViewGroup.LayoutParams params =  mScrollView.getLayoutParams();
		params.height += top_offset;
		//((RelativeLayout.LayoutParams) params).setMargins(0, top_offset, 0, 0);
		mScrollView.setLayoutParams(params);
		mScrollView.post(new Runnable() {
			
	        @Override
	        public void run() {
	        	mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
	        }
		});
  }
  
  public void submitText(String value)
  {
	  if(question_number < 10)
	  {
		 if(value.equals("") == false)
		 {
		  if(isFirstSend)
		  {
			  sendFirst(value);
			  isFirstSend = false;
		  }
		  else
		  {
			  sendRegular(false, value);
		  	  
		  }
		 }else
		 {
			 String msg = new String();
			 String title = new String();	
			 if(isFirstSend)
			 {
				 msg = getString(R.string.invalid_niggle_message_text);;
				 title = getString(R.string.invalid_niggle_message_title);
			 }
			 else
			 {
				 msg = getString(R.string.invalid_answer_message_text);
				 title = getString(R.string.invalid_answer_message_title);
			 }
			 
			 //Utils.showAlert(this, title, msg);
			  return;
		 }
	  } else if(question_number == 10)
	 {
		
		 if(value.equals("") == false)
		 {
			  sendRegular(true, value);
			  //FadeOut(((LinearLayout)(myedit.d.findViewById(R.id.edit_frame))), 0);
			  myedit.dismiss();
			  if(mScrollView.getHeight() < 400)
				  setVisualizeScrollOffset(250);
			  
			  FadeIn(((LinearLayout)findViewById(R.id.flow_buttons)), 0);
			  ((ImageButton)findViewById(R.id.nail_it)).setClickable(true);
			  ((ImageButton)findViewById(R.id.keep_going)).setClickable(true);

		 }else
		 {
			 Utils.showAlert(this, getString(R.string.invalid_answer_message_title),
					 getString(R.string.invalid_answer_message_text));
			  return;
		 }

	 }
	 
     mScrollView.post(new Runnable() {
	
	        @Override
	        public void run() {
	        	mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
	        }
	});

  }
  
  void sendFirst(String value)
  {
	  Resources res = getResources();
	  
	  Settings.NiggleText = value;
	  ListItem l1 = new ListItem(niggleQuestions.get(0).text, -1, 
			  Settings.FONT_ITC, true);
	  l1.buildViewItem();
	  
	  myedit.setText(res.getString(R.string.text_input_2));
  }
    
  void sendRegular(boolean isLast, String value)
  {
	  Settings.niggleAnswers.add(new mAnswer(
			  niggleQuestions.get(question_number).questionID, 
			 value));
	  
	  int last_index = Settings.niggleAnswers.size() - 1;
	  ListItem a = new ListItem(Settings.niggleAnswers.get(last_index).mtext, -1, 
			  Settings.FONT_ITC, false);
		a.buildViewItem();
	
	if(isLast == false)
	{
		// go to next question	
		ListItem q = new ListItem(niggleQuestions.get(++question_number).text, -1, 
				Settings.FONT_ITC, true);
		q.buildViewItem();
	}
	 
  }
  
  @Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	   
	}

  
  private class ListItem {
		View mitem;
		String mbody;
		int mtColor;
		String mfont;
		boolean misQuestion;
		
		ListItem(String body, int tColor, String font, boolean isQuestion)
		{
			int item_res_id = isQuestion==true?R.layout.visualise_niggle_question:
											R.layout.visualise_niggle_answer;
			mitem = inflater.inflate(item_res_id, null);
			mbody = body;
			mtColor = tColor;
			mfont = font;
			misQuestion = isQuestion;
		}
		
		    	    	
		void setItemText()
	    {
	    	Typeface face=Typeface.createFromAsset(getAssets(), mfont);
	
	    	if(mfont != null)
	    	{
	    		((TextView)mitem.findViewById(R.id.visualised_text)).setTypeface(face);
	    		((TextView)mitem.findViewById(R.id.visualised_text)).setTextSize(Settings.Q_A_1_SIZE);
	    		
	    	}
	    	
	    	if(mtColor != -1)
	    	{
	    		((TextView)mitem.findViewById(R.id.visualised_text)).setTextColor(mtColor);
	    	}
	    	
			((TextView)mitem.findViewById(R.id.visualised_text))
			.setText(mbody);
			
	    }
	
		void buildViewItem()
	    {	
	        //mitem.setPadding(0, 20, 0, 0);
			list_container.addView(mitem);
	        setItemText();
	    }		
	} 
  
  public boolean checkClick(View v, int x, int y)
  {
	  Rect r = new Rect();
	  //v.getDrawingRect(r);
	  v.getGlobalVisibleRect(r);
	  
	  if(r.contains(x, y))
	  {
		  return true;
	  }else
		  return false;
  }
  
  @Override
	public boolean onTouchEvent(MotionEvent event)
	{
	  
	  ImageButton n = ((ImageButton)findViewById(R.id.nailed_it_small));
	  ImageButton m = ((ImageButton)findViewById(R.id.menu));
	  ImageButton n_l = ((ImageButton)findViewById(R.id.nail_it));
	  ImageButton k_g = ((ImageButton)findViewById(R.id.keep_going));

	  if(checkClick(n, (int)event.getRawX(),(int)event.getRawY()) )
	  {
		  onNailItClick();
	  }else if (checkClick(m, (int)event.getRawX(), (int)event.getRawY()))
	  {
		  onMenuClick();
	  }else if (checkClick(n_l, (int)event.getRawX(), (int)event.getRawY()))
	  {
		  onNailItClick();
	  }else if (checkClick(k_g, (int)event.getRawX(), (int)event.getRawY()))
	  {
		 // onKeepGoingClick();
	  }
	 
	  return true;
	}
  
   
  private void onMenuClick()
  {
	  	/*if(vPlayer != null)
		{
		  vPlayer.releaseMediaPlayer();
		  vPlayer.doCleanUp();
		}*/
	  
    	Intent intent = new Intent(VisualiseNiggle.this, SplashScreen.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	intent.putExtra("frmenu", "1");
        startActivity(intent);
           
  }
  
  	private View.OnClickListener nailitClickListener = new View.OnClickListener(){
		 public void onClick(View v) {
			 onNailItClick();
		 }
	};
	
	private View.OnClickListener keepgoingClickListener = new View.OnClickListener()
	{
		public void	onClick(View v)
		{
			onKeepGoingClick();
		}
	};
	
	 class MyGestureDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	        {
	        	 mScrollView.scrollTo(0, (int)(mScrollView.getScrollY()+ distanceY));  
	        	return true;
	        }	 
	 }
  
	View.OnTouchListener scrollTouchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
		
			return gestureDetector.onTouchEvent(event);

		}
	};
	
	// Create an anonymous implementation of OnClickListener
		private OnClickListener mMenuClickListener = new OnClickListener() {
		    public void onClick(View v) {
		        
		    	/*if(vPlayer != null)
				{
				  vPlayer.releaseMediaPlayer();
				  vPlayer.doCleanUp();
				}*/
		    	
		    	Intent intent = new Intent(VisualiseNiggle.this, SplashScreen.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	intent.putExtra("frmenu", "1");
		        startActivity(intent);
	           
		    }
		};
	
	@Override
	protected void onResume() {
		super.onResume();
		/*if(vPlayer != null && vPlayer.surfaceIsInitiated() == true)
		{
			if(vPlayer.isPlaying() == false && videoCompleted == false)
				vPlayer.startVideoPlayback();
		}*/
	}
	
	/*protected void onRestart() {
		super.onRestart();
		playVideo();
	}*/
	
	@Override
	protected void onStop() {
		super.onStop();
		
		myedit.dismiss();
		/*if(vPlayer != null)
		{
		  vPlayer.releaseMediaPlayer();
		  vPlayer.doCleanUp();
		}*/
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		/*if(vPlayer != null && vPlayer.surfaceIsInitiated() == true)
		{
			if(vPlayer.isPlaying() == true)
				 vPlayer.pauseVideoPlayback();
		}*/
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		myedit.dismiss();
		/*if(vPlayer != null)
		{
		  vPlayer.releaseMediaPlayer();
		  vPlayer.doCleanUp();
		}*/
	}	
}