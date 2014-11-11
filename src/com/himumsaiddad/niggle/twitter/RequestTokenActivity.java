package com.himumsaiddad.niggle.twitter;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.TwitterException;

import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Prepares a OAuthConsumer and OAuthProvider 
 * 
 * OAuthConsumer is configured with the consumer key & consumer secret.
 * OAuthProvider is configured with the 3 OAuth endpoints.
 * 
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * 
 * After the request is authorized, a callback is made here.
 * 
 */
public class RequestTokenActivity extends Activity {

	final String TAG = getClass().getName();
	
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    private String sMessage;
	private String logout;
	public String dlg_message;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 Bundle extras = getIntent().getExtras();
			if (extras != null) {
				sMessage = extras.getString("smessage");
				logout = extras.getString("logout");
			}else
			{
				sMessage = this.getString(R.string.twitter_message);
				logout = "no";
			}
						
			if(logout.equals("yes"))
			{
				clearCredentials();
				try {
		    		this.consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		    	    this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,Constants.ACCESS_URL,Constants.AUTHORIZE_URL);
		    	} catch (Exception e) {
		    		Log.e(TAG, "Error creating consumer / provider",e);
				}
				
				Log.i(TAG, "Starting task to retrieve request token.");
				new OAuthRequestTokenTask(this,consumer,provider).execute();
			}else
			{
				Thread t = new Thread() {
	    	        public void run() {
	    	        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RequestTokenActivity.this);
	    	        	
	    	        	try {
	    	        		TwitterUtils.sendTweet(prefs, sMessage);
	    	        		RequestTokenActivity.this.runOnUiThread(new Runnable() {
	    						
	    					    public void run() {
	    					    	
	    					    	showAlert(RequestTokenActivity.this, "Success", "You have successfully shared Niggle on Twitter");
	    					    }
	    					});
	    	        		
	    				} catch (TwitterException ex) {
	    					dlg_message = ex.getMessage();
	    					RequestTokenActivity.this.runOnUiThread(new Runnable() {
	    						
	    					    public void run() {
	    					    	
	    					    	showAlert(RequestTokenActivity.this, "Oops", dlg_message);
	    					    }
	    					});

	    					
	    					ex.printStackTrace();
	    					
	    				}catch (Exception ex)
	    				{
	    					dlg_message = ex.getMessage();
	    					RequestTokenActivity.this.runOnUiThread(new Runnable() {
	    						
	    					    public void run() {
	    					    	
	    					    	showAlert(RequestTokenActivity.this, "Oops", dlg_message);
	    					    }
	    					});
	    					ex.printStackTrace();
	    					
	    				}
	    	        
	    	        }
	    	    };
	    	    t.start();
			}
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the request token).
	 * The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent); 
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i(TAG, "Callback received : " + uri);
			Log.i(TAG, "Retrieving Access Token");
			new RetrieveAccessTokenTask(this,consumer,provider,prefs).execute(uri);
			//finish();	
		}
	}
	
	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private Context	context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;
		
		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs=prefs;
		}


		/**
		 * Retrieve the oauth_verifier, and store the oauth and oauth_token_secret 
		 * for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri...params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				edit.commit();
				
				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
				
				consumer.setTokenWithSecret(token, secret);
				
				try {
					
					TwitterUtils.sendTweet(prefs, sMessage);
					RequestTokenActivity.this.runOnUiThread(new Runnable() {
						
					    public void run() {
					    	
					    	showAlert(RequestTokenActivity.this, "Success", "You have successfully shared Niggle on Twitter");
					    }
					});
				}  catch (TwitterException ex) {
					dlg_message = ex.getMessage();
					RequestTokenActivity.this.runOnUiThread(new Runnable() {
						
					    public void run() {
					    	
					    	showAlert(RequestTokenActivity.this, "Oops", dlg_message);
					    }
					});

					
					ex.printStackTrace();
					
				}catch (Exception ex)
				{
					dlg_message = ex.getMessage();
					RequestTokenActivity.this.runOnUiThread(new Runnable() {
						
					    public void run() {
					    	
					    	showAlert(RequestTokenActivity.this, "Oops", dlg_message);
					    }
					});
					ex.printStackTrace();
					
				}
				
				Log.i(TAG, "OAuth - Access Token Retrieved");
				
			} catch (Exception e) {
				setResult(2);
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
			}

			return null;
		}

	}
	
	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}
	
	 private void showAlert(Context context, String title, String message) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle(title);
	        builder.setMessage(message);
	        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	RequestTokenActivity.this.finish();
	            }
	        });
	        AlertDialog dialog = builder.create();
	        dialog.show();
	    }
	
}
