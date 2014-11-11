package com.himumsaiddad.niggle.share;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;


import com.himumsaiddad.niggle.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.Utils;

public class ShareOnFacebookActivity extends Activity {
    //fb_app_id = 175729095772478
	//fb_app_id = 239718726088483 //doritos
	
    Facebook facebook = new Facebook("175729095772478");
    private Handler mHandler = new Handler();
    private String sMessage = new String();
    private String sLink = new String();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.share_on_facebook);
       
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			sMessage = extras.getString("smessage");
			sLink = extras.getString("slink");
		}else
		{
			sMessage = this.getString(R.string.text_menu);
			sLink = "www.himumsaiddad.com";
		}
        try {
			tryToShare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void onBackPressed()
    {
    	setResult(3);
        finish();
    	
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        facebook.authorizeCallback(requestCode, resultCode, data);
    }       
    
    protected void tryToShare() throws IOException, JSONException {
        
        String access_token = Settings.getFacebookAccessToken();
        long expires = Settings.getFacebookAccessExpires();
        if(access_token.length()>0) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        if (facebook.isSessionValid()) {
        	Utils.share(facebook, sMessage, sLink, shareListener);
            Utils.GetFbId(facebook);
        } else {
            facebook.authorize(this, new String[]{"publish_stream"}, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    Log.d("dbg", "onComplete: " + values.toString());
                    Settings.setFacebookAccessToken(facebook.getAccessToken());
                    Settings.setFacebookAccessExpires(facebook.getAccessExpires());
                    Utils.share(facebook, sMessage, sLink, shareListener);
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
    
                @Override
                public void onFacebookError(FacebookError error) {
                    Log.d("dbg", "onFacebookError: " + error.toString());
                    showShareError();
                }
    
                @Override
                public void onError(DialogError e) {
                    Log.d("dbg", "onError: " + e.toString());
                    showShareError();
                }
    
                @Override
                public void onCancel() {
                    Log.d("dbg", "onCancel:");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {  
                              setResult(3);
                              finish();
                        }
                    });
                }
            });
        }
    }
 
        
    private void showShareError() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {  
                  setResult(2);
                  finish();
            }
        });
    }
    
    private void showShareSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
            	setResult(1);
                finish();
            }
        });
        
      }
    
    RequestListener shareListener = new RequestListener() {

        @Override
        public void onComplete(String response, Object state) {
            Log.d("dbg", "onComplete: "+response);
            showShareSuccess();
        }

        @Override
        public void onIOException(IOException e, Object state) {
            Log.d("dbg", "onIOException: "+e.toString());
            showShareError();
        }

        @Override
        public void onFileNotFoundException(FileNotFoundException e, Object state) {
            Log.d("dbg", "onFileNotFoundException: "+e.toString());
            showShareError();
        }

        @Override
        public void onMalformedURLException(MalformedURLException e, Object state) {
            Log.d("dbg", "onMalformedURLException: "+e.toString());
            showShareError();
        }

        @Override
        public void onFacebookError(FacebookError e, Object state) {
            Log.d("dbg", "onFacebookError: "+e.toString());
            showShareError();
        }
        
    };

}
