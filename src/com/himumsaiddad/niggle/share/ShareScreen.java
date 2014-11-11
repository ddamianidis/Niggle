package com.himumsaiddad.niggle.share;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.android.Facebook;
import com.himumsaiddad.niggle.BaseActivity;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.Utils;
import com.himumsaiddad.niggle.twitter.RequestTokenActivity;
import com.himumsaiddad.niggle.twitter.TwitterUtils;

public class ShareScreen extends BaseActivity {
	
	private SharedPreferences prefs;
	private Handler mHandler = new Handler();
	private String mType;
	private String mWisdomText;
	private int requestCode;
	private ImageView img; 
	int rsid;
	Resources res;
	Facebook facebook = new Facebook("175729095772478");

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                              
        Bundle extras = getIntent().getExtras();
        
        if(extras != null)
        {
        	mType = extras.getString("type");
        	mWisdomText = extras.getString("wisdom");
        }

        // Set The View Of Main Screen
        setContentView(R.layout.share_screen);
        
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
       ((ImageButton)findViewById(R.id.share_button))
       	.getBackground().setAlpha(0);
       
       getFacebookId();
              
       img = (ImageView) findViewById(R.id.profile_image);
       
       
       Thread t = new Thread() {
	        public void run() {
	        	
	        	try {
	        		setProfileImage();
	        		//mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
	        }

	    };
	    t.start();
                    
       initBaseActivity();
       
       // hide back button which is not included in How to Niggle Activity
       hideMenu();
       
       res = getResources();
       
       rsid = mType.equals("twitter") ?R.string.twitter_message 
    		   							: R.string.facebook_message;
       
       TextView mText = (TextView)findViewById(R.id.share_text);
       
       if(mWisdomText != null && mType.equals("twitter"))
       {
    	   mText.setText(mWisdomText);
       }else
       {
    	   mText.setText(res.getString(rsid));
       }
       Typeface face1=Typeface.createFromAsset(getAssets(),
	    		  Settings.FONT_ITC); 
       mText.setTypeface(face1); 
       mText.setTextSize(Settings.FB_STRING_SIZE);
       
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	getFacebookId();
    	
    	setProfileImage();
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
    
    private void setProfileImage()
    {
    	if(mType.equals("twitter"))
    	{
    		if(TwitterUtils.
    				isAuthenticated(prefs))
			{
    			URL url;
				try {
					url = new URL(TwitterUtils.getImageUrl(prefs));
					img.setImageBitmap(getBitmapFromURL("", true, url));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
			}
    	}else
    	{
	    	if(Settings.FACEBOOK_ID != "")
	        {
	     	   img.setImageBitmap(getBitmapFromURL(Settings.FACEBOOK_ID, false, null));
	     	   	 			
	        }
    	}
		ViewGroup.LayoutParams params =  img.getLayoutParams();
		params.width = 144;
		params.height = 146;	
		img.setLayoutParams(params);
    }
    
    public void OnShareClick(View view)
    {
    	Intent intent;
    	
    	if(mType.equals("twitter"))
    	{
    		if(TwitterUtils.
    				isAuthenticated(prefs))
			{
    			String title = "Twitter";
    			String name = TwitterUtils.getUser(PreferenceManager.getDefaultSharedPreferences(this));
	    		String  message = "You are already logged in as  " + name +
	    				". Do you want to share using this account?";
	    		Utils.ShowYesNoDialog(ShareScreen.this, title, message,
	    				yesTwListener, noTwListener);

			}else
			{
				
	        	intent = new Intent(ShareScreen.this, RequestTokenActivity.class);
	        	String msg = mWisdomText == null ? res.getString(rsid):mWisdomText;
				intent.putExtra("smessage", msg);
				intent.putExtra("logout", "yes");
				//startActivity(intent);
				startActivityForResult(intent, requestCode);
			}
    	}else
    	{
	    	if(isAlreadyLoggedIn())
	        {
	     	   // ask
	    		String title = "Facebook";
	    		String  message = "You are already logged in as  " + Settings.FACEBOOK_USERNAME +
	    				". Do you want to share using this account?";
	    		Utils.ShowYesNoDialog(ShareScreen.this, title, message,
	    				yesFbListener, noFbListener);
	    		
	        }else
	        {		    		
		    	intent = new Intent(ShareScreen.this, ShareOnFacebookActivity.class);	
		    	startActivityForResult(intent, requestCode);
	        }
    	}
	
    	
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
        	Utils.showAlert(this, "Oops", "There has been a sharing problem on Facebook. Please try again later.");
        }
    }
    
    private static String getUrlFacebookUserAvatar(String name_or_idUser) throws IOException
    {
    	String address = "http://graph.facebook.com/"+name_or_idUser+"/picture";	
    	URL url = new URL(address);
    	HttpURLConnection.setFollowRedirects(false); //Do _not_ follow redirects!
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	
    	return connection.getHeaderField("Location"); 	
    	
    }
    
    public static Bitmap getBitmapFromURL(String name_or_idUser, boolean tw, URL tw_url) {
        try {
        	URL url;
        	if(tw == true){
        		url = tw_url; 
        	}else
        	{
        		String url_str = getUrlFacebookUserAvatar(name_or_idUser);
        		url = new URL(url_str);
        	}
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {   
            e.printStackTrace();
            return null;
        }
    }
            
    DialogInterface.OnClickListener yesFbListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
        	
        
        	Intent intent;
	    	intent = new Intent(ShareScreen.this, ShareOnFacebookActivity.class);
	    	startActivityForResult(intent, requestCode);
	    	
	    	dialog.cancel();
        }
    };
    
    
    DialogInterface.OnClickListener noFbListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
        	
        		Thread t = new Thread(new Runnable(){
        			
        			public void run()
        			{
        				try {
	        				facebook.logout(ShareScreen.this);
	        				Settings.FACEBOOK_ID = "";
	        				Settings.FACEBOOK_USERNAME = "";
        				} catch (MalformedURLException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				} catch (IOException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        				
        				Intent intent;
        		    	
        		   		intent = new Intent(ShareScreen.this, ShareOnFacebookActivity.class);
        		    	
        		    	startActivityForResult(intent, requestCode);
                	
        			}
        		});
        		
        		try {
    				facebook.logout(ShareScreen.this);
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
				
				Intent intent;
		    	
		    	intent = new Intent(ShareScreen.this, ShareOnFacebookActivity.class);
		    	
		    	startActivityForResult(intent, requestCode);
        		//t.start();
				
        		dialog.cancel();
        }
    };

    DialogInterface.OnClickListener yesTwListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
        	
        /*	Thread t = new Thread() {
    	        public void run() {
    	        	
    	        	try {
    	        		TwitterUtils.sendTweet(prefs,res.getString(rsid));
    	        	
    				} catch (Exception ex) {
    					ex.printStackTrace();
    				}
    	        }

    	    };
    	    t.start();
*/
       	
        	Intent intent;
        	intent = new Intent(ShareScreen.this, RequestTokenActivity.class);
        	String msg = mWisdomText == null ? res.getString(rsid):mWisdomText;
			intent.putExtra("smessage", msg);
			intent.putExtra("logout", "no");
			//startActivity(intent);
			startActivityForResult(intent, requestCode);
	    	
	    	dialog.cancel();
        }
    };
    
    
    DialogInterface.OnClickListener noTwListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
        	
        	Intent intent;
        	intent = new Intent(ShareScreen.this, RequestTokenActivity.class);
        	String msg = mWisdomText == null ? res.getString(rsid):mWisdomText;
			intent.putExtra("smessage", msg);
			intent.putExtra("logout", "yes");
			//startActivity(intent);
			startActivityForResult(intent, requestCode);
				
        		dialog.cancel();
        }
    };

}