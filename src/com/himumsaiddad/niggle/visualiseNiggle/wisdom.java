package com.himumsaiddad.niggle.visualiseNiggle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import org.json.JSONException;

import com.facebook.android.Facebook;
import com.himumsaiddad.niggle.BaseActivity;

import com.himumsaiddad.niggle.DataBaseHelper;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.Utils;
import com.himumsaiddad.niggle.VideoPlayer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.intro.SplashScreen;
import com.himumsaiddad.niggle.myNiggles.detailedniggle;
import com.himumsaiddad.niggle.share.ShareOnFacebookActivity;
import com.himumsaiddad.niggle.share.ShareScreen;

public class wisdom extends Activity {
    /** Called when the activity is first created. */
	
	Facebook facebook = new Facebook("175729095772478");
	private int requestCode;
	private DataBaseHelper myDbHelper;
	private TextView mQouteText;
	private VideoPlayer vPlayer;
	private boolean videoCompleted = false;
	private ImageButton mmenu;
	private ImageButton mfacebook;
	private ImageButton mtwitter;
	private RelativeLayout textLayout;
	private String ShareString;
	public Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wisdom); 
        
        SurfaceView sv = (SurfaceView) findViewById(R.id.surface_view);
        
       /* vPlayer = new VideoPlayer(this, false, sv, R.raw.heart){
      	  
      	public void OnComplete()
      	{
      		videoCompleted = true;
      	}
      	  
        };*/
        
        videoCompleted = false;
        
        mmenu = (ImageButton)findViewById(R.id.menu);
        mmenu.getBackground().setAlpha(0);
        mmenu.setOnClickListener(mMenuClickListener);
        
        mfacebook = (ImageButton)findViewById(R.id.facebook);
        mfacebook.getBackground().setAlpha(0);
        mfacebook.setOnClickListener(mFacebookClickListener);
        
        mtwitter = (ImageButton)findViewById(R.id.twitter);
        mtwitter.getBackground().setAlpha(0);
        mtwitter.setOnClickListener(mTwitterClickListener);
        
        textLayout = (RelativeLayout)findViewById(R.id.text_layout);

        mQouteText = (TextView)findViewById(R.id.quote_text);

        Typeface face=Typeface.createFromAsset(getAssets(),
	    		  Settings.FONT_ITC);
        mQouteText.setTypeface(face); 
        mQouteText.setTextSize(Settings.WISDOM_SIZE);
		//myDbHelper = new DataBaseHelper(this);
	     //myDbHelper.openDataBase();
	    Settings.myDbHelper.myDataBase = Settings.myDbHelper.getWritableDatabase();
	    
	    long CountOfQuotes = Settings.myDbHelper.CountTableEntries("ZQUOTE");  
	    Random generator = new Random();
	    long r = Math.abs(generator.nextInt());
	    long SelectedItem = (r % CountOfQuotes) + 1;
	    Cursor QuoteCursor = Settings.myDbHelper.ReadQuote(SelectedItem);
	    
	    if(QuoteCursor.getCount() > 0)
        {
	    	QuoteCursor.moveToLast();
	    	mQouteText.setText(QuoteCursor.getString(0));
	    	ShareString = QuoteCursor.getString(0);
        }
	    
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  		
	    hideViews();
	    
	    //in case of video disabled
	    mHandler.postDelayed(ShowViewsInterface, 100);
	    //mHandler.postDelayed(ShowViewsInterface, 5000);
    }
    
    private void hideViews()
    {
    	FadeOut(textLayout, 0);
    	FadeOut(mmenu, 0);
    	FadeOut(mfacebook, 0);
    	FadeOut(mtwitter, 0);
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

    

    private Runnable ShowViewsInterface = new Runnable() {
       
	      @Override
	       public void run() {
	    	  showViews();
	      }
      };
       
    
    private void showViews()
    {
       	FadeIn(textLayout, 2000);
    	FadeIn(mmenu, 2000);
    	FadeIn(mfacebook, 2000);
    	FadeIn(mtwitter, 2000);
 
    }
    
    private Uri URI(String string) {
		// TODO Auto-generated method stub
		return null;
	}
    
 // Create an anonymous implementation of OnClickListener
		private OnClickListener mMenuClickListener = new OnClickListener() {
		    public void onClick(View v) {
		        	
		    	/*if(vPlayer != null)
				{
				  vPlayer.releaseMediaPlayer();
				  vPlayer.doCleanUp();
				}*/
		    	
		    	Intent intent = new Intent(wisdom.this, SplashScreen.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	intent.putExtra("frmenu", "1");
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
	    	
			if(isAlreadyLoggedIn())
	        {
	     	   // ask
	    		String title = "Facebook";
	    		String  message = "You are already logged in as  " + Settings.FACEBOOK_USERNAME +
	    				". Do you want to share using this account?";
	    		Utils.ShowYesNoDialog(wisdom.this, title, message,
	    				yesListener, noListener);
	    		
	        }else
	        {
		    	Intent intent = new Intent(wisdom.this, ShareOnFacebookActivity.class);
		    	intent.putExtra("smessage", ShareString);
		        intent.putExtra("slink", "www.himumsaiddad.com");
		    	startActivityForResult(intent, requestCode);
	        }        
			
	        
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
	    	
	    	Intent intent = new Intent(wisdom.this, ShareScreen.class);
	    	intent.putExtra("type", "twitter");
	    	intent.putExtra("wisdom", ShareString);
	        startActivity(intent);
           
	    }
	};	
	
	@Override
	protected void onResume() {
		super.onResume();		
		getFacebookId();
		
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
		/*if(vPlayer != null)
		{
		  vPlayer.releaseMediaPlayer();
		  vPlayer.doCleanUp();
		}*/
	}	
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     //   Log.d("CheckStartActivity","onActivityResult and resultCode = "+resultCode);
	        // TODO Auto-generated method stub
	        super.onActivityResult(requestCode, resultCode, data);
	        if(resultCode == 1){
	        	Utils.showAlert(this, "Success", "You have successfully shared Niggle on Facebook");
	            
	        }
	        else if(resultCode == 2){
	        	Utils.showAlert(this, "Oops", "There has been a problem sharing on Facebook. Please try again later.");
	        }
	    }
	    
	    private void getFacebookId()
	    {
	    	String access_token = Settings.getFacebookAccessToken();
	        long expires = Settings.getFacebookAccessExpires();
	        if(access_token.length()>0) {
	            facebook.setAccessToken(access_token);
	        }
	        if(expires != 0) {
	            facebook.setAccessExpires(expires);
	        }
	        
	        if (facebook.isSessionValid())
	        {
	 			try {
	 				Utils.GetFbId(facebook);
	 			} catch (IOException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (JSONException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	        }

	    }
	    
	   private  boolean isAlreadyLoggedIn(){
		   
		   return Settings.FACEBOOK_ID != "" ? true : false;
	   }
	    
	    DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	           	
	           	Intent intent = new Intent(wisdom.this, ShareOnFacebookActivity.class);
		    	intent.putExtra("smessage", ShareString);
		        intent.putExtra("slink", "www.himumsaiddad.com");
		    	startActivityForResult(intent, requestCode);
		    	
		    	dialog.cancel();
	        }

	        
	    };
	    
	    
	    DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	        		try {
	    				facebook.logout(wisdom.this);
	    				Settings.FACEBOOK_ID = "";
	    				Settings.FACEBOOK_USERNAME = "";
	    				Settings.setFacebookAccessToken(null);
	    				Settings.setFacebookAccessExpires(0 );
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			    	Intent intent = new Intent(wisdom.this, ShareOnFacebookActivity.class);
			    	intent.putExtra("smessage", ShareString);
			        intent.putExtra("slink", "www.himumsaiddad.com");
			    	startActivityForResult(intent, requestCode);

	        		dialog.cancel();
	        }
	    };

     
}
