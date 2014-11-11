package com.himumsaiddad.niggle.intro;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.VideoPlayer;
import com.himumsaiddad.niggle.HowToNiggle.HowToNiggle;
import com.himumsaiddad.niggle.SimpleBroadcastReceiver.SimpleBroadcastReceiver;
import com.himumsaiddad.niggle.myNiggles.myniggles;
import com.himumsaiddad.niggle.share.ShareOnFacebookActivity;
import com.himumsaiddad.niggle.share.ShareScreen;
import com.himumsaiddad.niggle.visualiseNiggle.NailThatNiggle;
 
public class SplashScreen extends Activity {   	
	final int SPLASH_STATE = 1;
	final int ENTER_MENU_STATE = 2;
	final int MENU_STATE = 3;
	private int display_state = SPLASH_STATE;
	
	final int TOTAL_TIMEOUT = 22200; //20 sec's
	final int STEP = 1000; //40 millisec's
	final int FIRST_SCREEN_TIMEOUT = 1000; // 1 sec
	final int SECOND_SCREEN_TIMEOUT = 7000; //7 secs
	final int MENU_SCREEN_TIMEOUT = 18000; //10 secs
	
	private final int DURATION = 6000;
    private final int FRAME_STEP = 40;
    private long mStartTime;
    private VideoPlayer vPlayer;
    
    private SimpleBroadcastReceiver intentReceiver =
			new SimpleBroadcastReceiver();
    
	public int waited;
	public int imageRec;
	public Handler mHandler = new Handler();
	final public LogoScreen mLogoScreen = new LogoScreen();
	final public LoadingAScreen mLoadingAScreen = new LoadingAScreen();
	final public LoadingBScreen mLoadingBScreen = new LoadingBScreen();
	final public LoadingCScreen mLoadingCScreen = new LoadingCScreen();
	final public MenuScreen mMenuScreen = new MenuScreen();
	
	
		
	final public class LogoScreen {
		private ImageView mLogo;
		private RelativeLayout mLogoLayout;

		public void setLogo(ImageView mLogo){
			try{
				this.mLogo = mLogo;
				mLogo.getBackground().setAlpha(0);
			}catch(NullPointerException e)
			{
				Log.e("LOGO SCREEN", "setLogo-null pointer");
			}
		}
		
		public void setLogoLayout(RelativeLayout mLogoLayout){ this.mLogoLayout = mLogoLayout;}
		
		public void  setVisible(int v)
		{
			mLogoLayout.setVisibility(v);
		}
		
		public void FadeIn(int duration)
		{
			AlphaAnimation FadeInAlpha = new AlphaAnimation(0.0F, 1.0F);
			FadeInAlpha.reset();
			FadeInAlpha.setDuration(duration);
			FadeInAlpha.setFillAfter(true);
			mLogoLayout.startAnimation(FadeInAlpha);
		}
		
		public void FadeOut(int duration)
		{
			AlphaAnimation FadeOutAlpha = new AlphaAnimation(1.0F, 0.0F);
			FadeOutAlpha.reset();
			FadeOutAlpha.setDuration(duration);
			FadeOutAlpha.setFillAfter(true);
			mLogoLayout.startAnimation(FadeOutAlpha);
		}

	}
	
	final public class LoadingAScreen {
		private ProgressBarImageView mProgressBar;
		private ImageView mProgressBarBase;
		private RelativeLayout mLoadingALayout;
		private  AlphaAnimation FadeAlpha;
		public void  setVisible(int v)
		{
			mLoadingALayout.setVisibility(v);
		}
	
		public void setOnclickListener(OnClickListener cl)
		{
			mProgressBar.setOnClickListener(cl);
		}
		public boolean isVisible()
		{
			return mLoadingALayout.isShown(); 
		}
		
		public void setLoadingALayout(RelativeLayout mLoadingALayout)
		{
			this.mLoadingALayout = mLoadingALayout;
		}
		
		public void setProgressBar(ProgressBarImageView mProgressBar)
		{ 
			try{
				this.mProgressBar = mProgressBar;
				mProgressBar.getBackground().setAlpha(0);
				mProgressBar.setClickable(false);
			}catch(NullPointerException e)
			{
				Log.e("LOADING A SCREEN", "setProgressBar-null pointer");
			}
		}
		
		public void setProgressBarBase(ImageView mProgressBarBase)
		{
			try{
				this.mProgressBarBase = mProgressBarBase;
				mProgressBarBase.getBackground().setAlpha(0);
			}catch(NullPointerException e)
			{
				Log.e("LOADING A SCREEN", "setProgressBarBase-null pointer");
			}
		}	
		
		public void FadeIn(int duration)
		{
			FadeAlpha = new AlphaAnimation(0.0F, 1.0F);
			FadeAlpha.reset();
			FadeAlpha.setDuration(duration);
			FadeAlpha.setFillAfter(true);
			mLoadingALayout.startAnimation(FadeAlpha);
			
		}
		
		public void FadeOut(int duration)
		{
			FadeAlpha = new AlphaAnimation(1.0F, 0.1F);
			FadeAlpha.reset();
			FadeAlpha.setDuration(duration);
			FadeAlpha.setFillAfter(true);
			mLoadingALayout.startAnimation(FadeAlpha);
		}
		
		public void FadeCancel()
		{
			if(FadeAlpha != null)
			{
				FadeAlpha.cancel();
			}
		}
		 
		public ProgressBarImageView getProgressBar(){return mProgressBar;}
		
	};

	final  public class LoadingBScreen {
		private ImageView mTextBox;
		private TextView mText;
		private RelativeLayout mLoadingBLayout;
		
		public void  setVisible(int v)
		{
			mLoadingBLayout.setVisibility(v);
		}
		
		public void setLoadingBLayout(RelativeLayout mLoadingBLayout)
		{
			this.mLoadingBLayout = mLoadingBLayout;
		}

		public void setTextBox(ImageView mTextBox){ this.mTextBox = mTextBox;}
		public void setTextView(TextView mText){ 		
			try{	
			  this.mText = mText;
			  Typeface face=Typeface.createFromAsset(getAssets(),
			    		  Settings.FONT_ITC);
		      mText.setTypeface(face); 
		      mText.setTextSize(Settings.SPLASH_SIZE);
		      mText.setWidth(400);
		      mText.setGravity(Gravity.CENTER);
		      mText.setTextColor(Color.WHITE);
			}catch(NullPointerException e)
			{
				Log.e("LOADING B SCREEN", "setTextView-null pointer");
			}
		}
		
		public void FadeIn(int duration)
		{
			AlphaAnimation FadeInAlpha = new AlphaAnimation(0.0F, 1.0F);
			FadeInAlpha.reset();
			FadeInAlpha.setDuration(duration);
			FadeInAlpha.setFillAfter(true);
			mLoadingBLayout.startAnimation(FadeInAlpha);
		}
		
		public void FadeOut(int duration)
		{
			AlphaAnimation FadeOutAlpha = new AlphaAnimation(1.0F, 0.0F);
			FadeOutAlpha.reset();
			FadeOutAlpha.setDuration(duration);
			FadeOutAlpha.setFillAfter(true);
			mLoadingBLayout.startAnimation(FadeOutAlpha);
		}

	}
	
	final  class LoadingCScreen {
		private ImageView mBackground;
		private RelativeLayout mLoadingCLayout;
		
		public void  setVisible(int v)
		{
			mLoadingCLayout.setVisibility(v);
		}

		public void setLoadingCLayout(RelativeLayout mLoadingCLayout)
		{
			this.mLoadingCLayout = mLoadingCLayout;
		}

		public void setBackground(ImageView mBackground){
			try{	
				this.mBackground = mBackground;
			}catch(NullPointerException e)
			{
				Log.e("LOADING C SCREEN", "setBackground-null pointer");
			}
		}
		
		public void setBackgroundListener(OnClickListener mOnClickListener){ 
			try{
				mBackground.setOnClickListener(mOnClickListener);
			}catch(NullPointerException e)
			{
				Log.e("LOADING C SCREEN", "setBackgroundListener-null pointer");
			}
		}
			
		
		public void FadeIn(int duration)
		{
			AlphaAnimation FadeInAlpha = new AlphaAnimation(0.0F, 1.0F);
			FadeInAlpha.reset();
			FadeInAlpha.setDuration(duration);
			FadeInAlpha.setFillAfter(true);
			mLoadingCLayout.startAnimation(FadeInAlpha);
		}
		
		public void FadeOut(int duration)
		{
			AlphaAnimation FadeOutAlpha = new AlphaAnimation(1.0F, 0.0F);
			FadeOutAlpha.reset();
			FadeOutAlpha.setDuration(duration);
			FadeOutAlpha.setFillAfter(true);
			mLoadingCLayout.startAnimation(FadeOutAlpha);
		}

	}
	
	final  class MenuScreen {		
		private ImageButton mfacebook;
		private ImageButton mtwitter;
		private ImageButton mHowToNiggle;
		private ImageButton mNailThatNiggle;
		private ImageButton mNailedNiggles;
		private ImageView mTextBox1;
		private TextView mText1;
		private RelativeLayout mMenuLayout;

		public void  setVisible(int v)
		{
			mMenuLayout.setVisibility(v);
		}

		public void setMenuLayout(RelativeLayout mMenuLayout)
		{
			this.mMenuLayout = mMenuLayout;
		}

		public void setFacebookButton(ImageButton mfacebook){
			try{
				this.mfacebook = mfacebook;
				mfacebook.getBackground().setAlpha(0);
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "SetFacebookButton-null pointer");
			}
		}
		
		public void setTwitterButton(ImageButton mtwitter){
			try{
				this.mtwitter = mtwitter;
				mtwitter.getBackground().setAlpha(0);
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "SetTwitterButton-null pointer");
			}
		}

		public void setHowToNiggleButton(ImageButton mHowToNiggle){ 
			try{
				this.mHowToNiggle = mHowToNiggle;
				mHowToNiggle.getBackground().setAlpha(0);
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "SetHowToNiggleButton-null pointer");
			}
		}
		
		public void setNailThatNiggleButton(ImageButton mNailThatNiggle){
			try{
				this.mNailThatNiggle = mNailThatNiggle;
				mNailThatNiggle.getBackground().setAlpha(0);
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "SetNailThatNiggleButton-null pointer");
			}		
		}
		
		public void setNailedNigglesButton(ImageButton mNailedNiggles){ 
			try{
				this.mNailedNiggles = mNailedNiggles;
				mNailedNiggles.getBackground().setAlpha(0);
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "SetNailedNigglesButton-null pointer");
			}
		}
			
		public void setButtonListener(ImageButton button, OnClickListener mOnClickListener){ 
			try{
				if(button.equals(mfacebook))
					mfacebook.setOnClickListener(mOnClickListener);
				else if(button.equals(mtwitter))
					mtwitter.setOnClickListener(mOnClickListener);
				else if(button.equals(mHowToNiggle))
					mHowToNiggle.setOnClickListener(mOnClickListener);
				else if(button.equals(mNailThatNiggle))
					mNailThatNiggle.setOnClickListener(mOnClickListener);
				else if(button.equals(mNailedNiggles))
					mNailedNiggles.setOnClickListener(mOnClickListener);
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "SetButtonListener-null pointer");
			}
		}
		
		public void setTextBox1(ImageView mTextBox1){
			try{	
				this.mTextBox1 = mTextBox1;
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "SetButtonListener-null pointer");
			}
		}
		
		public void setTextView1(TextView mText1){
			try{
			  this.mText1 = mText1;
			  Typeface face1=Typeface.createFromAsset(getAssets(),
			    		  Settings.FONT_ITC); 
		      mText1.setTypeface(face1); 
		      mText1.setTextSize(Settings.SPLASH_SIZE);
		      mText1.setWidth(400);
		      mText1.setGravity(Gravity.CENTER);
		      mText1.setTextColor(Color.WHITE);
		     // mText1.setVisibility(View.INVISIBLE);
			}catch(NullPointerException e)
			{
				Log.e("MENU SCREEN", "setTextView1-null pointer");
			}
		}
		
		public void FadeIn(int duration)
		{
			AlphaAnimation FadeInAlpha = new AlphaAnimation(0.0F, 1.0F);
			FadeInAlpha.reset();
			FadeInAlpha.setDuration(duration);
			FadeInAlpha.setFillAfter(true);
			mMenuLayout.startAnimation(FadeInAlpha);
		}
		
		public void FadeOut(int duration)
		{
			AlphaAnimation FadeOutAlpha = new AlphaAnimation(0.1F, 0.0F);
			FadeOutAlpha.reset();
			FadeOutAlpha.setDuration(duration);
			FadeOutAlpha.setFillAfter(true);
			mMenuLayout.startAnimation(FadeOutAlpha);
		}
	}
	
	private void setDisplayState(int state)
	{		
		switch(state)
		{
		case SPLASH_STATE:
			/*mLoadingAScreen.FadeIn(0);
			mLoadingBScreen.FadeIn(0);
			mLoadingCScreen.FadeIn(0);*/
			mMenuScreen.FadeOut(0);
			break;
		case ENTER_MENU_STATE:
			mLoadingAScreen.setVisible(View.VISIBLE);
			mLoadingBScreen.setVisible(View.VISIBLE);
			mLoadingCScreen.setVisible(View.VISIBLE);
			mMenuScreen.setVisible(View.INVISIBLE);
			break;
		case MENU_STATE:
			//mLoadingAScreen.FadeOut(0);
			mLoadingAScreen.FadeCancel();
  	    	mLoadingAScreen.setVisible(View.INVISIBLE);
			/*mLoadingAScreen.FadeCancel();
			mLoadingAScreen.setVisible(View.INVISIBLE);*/
			mLoadingBScreen.FadeOut(0);
			mLoadingCScreen.FadeIn(0);
			mMenuScreen.FadeIn(0);
			break;
		default:
			mLoadingAScreen.setVisible(View.INVISIBLE);
			mLoadingBScreen.setVisible(View.INVISIBLE);
			mLoadingCScreen.setVisible(View.INVISIBLE);
			mMenuScreen.setVisible(View.VISIBLE);
			break;
		}
	}
		
	public void SetSplashScreenListeners()
	{
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.how_to_niggle), 
				mSplashClickListener);
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.nailed_niggles), 
				mSplashClickListener);
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.nail_that_niggle), 
				mSplashClickListener);
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.facebook), 
				mSplashClickListener);
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.twitter), 
				mSplashClickListener);
		mLoadingCScreen.setBackgroundListener(mSplashClickListener);
	}
	
	public void SetMenuScreenListeners()
	{
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.how_to_niggle), 
				mHowToNiggleClickListener);
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.nailed_niggles), 
				mSavedNigglesClickListener);
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.nail_that_niggle), 
		mVisualiseNigglesClickListener);
    	mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.facebook), 
    			mFacebookClickListener);
		mMenuScreen.setButtonListener((ImageButton)findViewById(R.id.twitter), 
				mTwitterClickListener);
		mLoadingCScreen.setBackgroundListener(null);
	}

	
	private Runnable AnimationFrame = new Runnable() {
       
   	@Override
       public void run() {
           int deltaTime = (int)(System.currentTimeMillis() - mStartTime);
           if (deltaTime <= DURATION) {
        	   mLoadingAScreen.getProgressBar().setProgress((float)deltaTime/DURATION);
               mHandler.postDelayed(AnimationFrame, FRAME_STEP);
           } else {
        	   mLoadingAScreen.getProgressBar().setProgress((float)1.0);
        	   mLoadingAScreen.getProgressBar().invalidate();
           }
       }
   };

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.splash_screen);
      
      SurfaceView sv = (SurfaceView) findViewById(R.id.surface_view);
      
      /*vPlayer = new VideoPlayer(this, true, sv, R.raw.background_android){
    	  
    	public void OnComplete()
    	{
    		
    	}
    	  
      };*/
      
      // init receiver
     /* IntentFilter intentFilter =
    		  new IntentFilter(Intent.ACTION_SCREEN_OFF);
    		  intentFilter.addAction(Intent.ACTION_SCREEN_ON);
    		  registerReceiver(intentReceiver, intentFilter);
      */
      //Initialize LogoScreen
      mLogoScreen.setLogo((ImageView)findViewById(R.id.logo));
      mLogoScreen.setLogoLayout((RelativeLayout)findViewById(R.id.LogoScreen));
      mLogoScreen.setVisible(View.VISIBLE);
      
       //Initialize LoadingAScreen
     mLoadingAScreen.setProgressBar(
    		  (ProgressBarImageView) findViewById(R.id.prog_bar));          
      mLoadingAScreen.setProgressBarBase(
    		  (ImageView)findViewById(R.id.prog_bar_top));
      mLoadingAScreen.setLoadingALayout((RelativeLayout)findViewById(R.id.LoadingAScreen));
      mLoadingAScreen.setOnclickListener(mProgBarClickListener);
            
      //Initialize LoadingBScreen
     // mLoadingBScreen.setTextBox((ImageView)findViewById(R.id.text_box));
      mLoadingBScreen.setTextView((TextView)findViewById(R.id.text_loading));
      mLoadingBScreen.setLoadingBLayout((RelativeLayout)findViewById(R.id.LoadingBScreen));

      //Initialize LoadingCScreen
      mLoadingCScreen.setBackground(
    		  (ImageView)findViewById(R.id.intro_background_static));
      mLoadingCScreen.setLoadingCLayout((RelativeLayout)findViewById(R.id.LoadingCScreen));
    
      //Initialize MenuScreen
      mMenuScreen.setFacebookButton((ImageButton)findViewById(R.id.facebook));
      mMenuScreen.setTwitterButton((ImageButton)findViewById(R.id.twitter));
      mMenuScreen.setHowToNiggleButton((ImageButton)findViewById(R.id.how_to_niggle));
      mMenuScreen.setNailThatNiggleButton((ImageButton)findViewById(R.id.nail_that_niggle));
      mMenuScreen.setNailedNigglesButton((ImageButton)findViewById(R.id.nailed_niggles));
     // mMenuScreen.setTextBox1((ImageView)findViewById(R.id.text_box_1));
      mMenuScreen.setTextView1((TextView)findViewById(R.id.text_menu));     
      mMenuScreen.setMenuLayout((RelativeLayout)findViewById(R.id.MenuScreen));
      
      
      waited = STEP;
      Bundle extras = getIntent().getExtras();
		
      if (extras != null) {
    	setDisplayState(MENU_STATE);
    	SetMenuScreenListeners();		
		return;
	  }
      SetSplashScreenListeners();
      setDisplayState(SPLASH_STATE);
      mLogoScreen.FadeIn(1000);
      mLoadingAScreen.FadeIn(1000);	 
      //mLoadingAScreen.setVisible(View.VISIBLE);
      mLoadingBScreen.FadeIn(1000);
      mStartTime = System.currentTimeMillis();
      mHandler.postDelayed(AnimationFrame, FRAME_STEP);	
      mHandler.postDelayed(new Runnable()
      {  
    	   public void run() {
    		  mLoadingCScreen.FadeIn(2000);
    		  display_state = ENTER_MENU_STATE;
   	  }
      }, 5000);
      mHandler.postDelayed(fadeInMenu, MENU_SCREEN_TIMEOUT);
      mHandler.postDelayed(setButtonListeners, TOTAL_TIMEOUT);      
      
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
   }

   private Runnable setButtonListeners = new Runnable()
   {  
   	   public void run() {
   		if(display_state != MENU_STATE)
   		{
   			display_state = MENU_STATE;
   			SetMenuScreenListeners();
   			
   		}
      }
   };
   
   private OnClickListener mProgBarClickListener = new OnClickListener() {
	    public void onClick(View v) {
	      // do something when the image is clicked
	    	if(display_state == MENU_STATE && mLoadingAScreen.isVisible())
	    	{
	    		mLoadingAScreen.FadeCancel();
	  	    	mLoadingAScreen.setVisible(View.INVISIBLE);
	    	}
	    }
	};
	
    private Runnable fadeInMenu = new Runnable()
   {  
  	    public void run() {
  	    	mLoadingAScreen.FadeCancel();
  	    	mLoadingAScreen.setVisible(View.INVISIBLE);
  	      //mLoadingAScreen.FadeOut(8000);	 
	   	  mLoadingBScreen.FadeOut(8000);
	   	  mMenuScreen.FadeIn(8000);
	   }
   };

	// Create an anonymous implementation of OnClickListener
	private OnClickListener mSplashClickListener = new OnClickListener() {
	    public void onClick(View v) {
	      // do something when the image is clicked
	    	if(display_state == ENTER_MENU_STATE)
	    	{
	    		display_state = MENU_STATE;
	    		mLoadingAScreen.FadeCancel();
	  	    	mLoadingAScreen.setVisible(View.INVISIBLE);
	    	   	mLoadingBScreen.FadeOut(0);
	    	   	mMenuScreen.FadeIn(0);
	    		SetMenuScreenListeners();
	    		mHandler.removeCallbacks(fadeInMenu);
	    		mHandler.removeCallbacks(setButtonListeners);
	    	}
	    }
	};
		
	// Create an anonymous implementation of OnClickListener
		private OnClickListener mHowToNiggleClickListener = new OnClickListener() {
		    public void onClick(View v) {
		    	
		    			    	
		      // do something when the image is clicked
		    	Intent intent = new Intent(SplashScreen.this, HowToNiggle.class);
		        startActivity(intent);
		    	
		        /*if(vPlayer != null)
				{
				  vPlayer.releaseMediaPlayer();
				  vPlayer.doCleanUp();
				}*/

		    }
		};

	// Create an anonymous implementation of OnClickListener
	private OnClickListener mSavedNigglesClickListener = new OnClickListener() {
	    public void onClick(View v) {
	      // do something when the image is clicked
	    	/*if(vPlayer != null)
			{
			  vPlayer.releaseMediaPlayer();
			  vPlayer.doCleanUp();
			}*/
	    	
	    	Intent intent = new Intent(SplashScreen.this, myniggles.class);
	        startActivity(intent);
	    	
	    }
	};
	
	// Create an anonymous implementation of OnClickListener
	private OnClickListener mVisualiseNigglesClickListener = new OnClickListener() {
	    public void onClick(View v) {
	      // do something when the image is clicked
	    		
	    	/*if(vPlayer != null)
			{
			  vPlayer.releaseMediaPlayer();
			  vPlayer.doCleanUp();
			}*/
	    	
	    	Intent intent = new Intent(SplashScreen.this, NailThatNiggle.class);
	        startActivity(intent);
         
	    }
	};
 
	// Create an anonymous implementation of OnClickListener
		private OnClickListener mFacebookClickListener = new OnClickListener() {
		    public void onClick(View v) {
		      // do something when the image is clicked
		    	/*if(vPlayer != null)
				{
				  vPlayer.releaseMediaPlayer();
				  vPlayer.doCleanUp();
				}*/
		    	
		    	Intent intent = new Intent(SplashScreen.this, ShareScreen.class);
		    	intent.putExtra("type", "facebook");
		        startActivity(intent);
	           
		    }
		};
	 
		// Create an anonymous implementation of OnClickListener
		private OnClickListener mTwitterClickListener = new OnClickListener() {
		    public void onClick(View v) {
		      // do something when the image is clicked
		    	/*if(vPlayer != null)
				{
				  vPlayer.releaseMediaPlayer();
				  vPlayer.doCleanUp();
				}*/
		    	
		    	Intent intent = new Intent(SplashScreen.this, ShareScreen.class);
		    	intent.putExtra("type", "twitter");
		        startActivity(intent);
	           
		    }
		};
		
		@Override
		protected void onResume() {
			super.onResume();
			
			/*if(vPlayer != null && vPlayer.surfaceIsInitiated() == true)
			{
				if(vPlayer.isPlaying() == false)
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
			
			Settings.myDbHelper.close();
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
			
			Settings.myDbHelper.close();
			/*if(vPlayer != null)
			{
			  vPlayer.releaseMediaPlayer();
			  vPlayer.doCleanUp();
			}*/
			//unregisterReceiver(intentReceiver);
		}	
}