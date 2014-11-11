package com.himumsaiddad.niggle.twitter;

import java.net.URL;

import oauth.signpost.OAuth;
import twitter4j.ProfileImage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class TwitterUtils {

	public static boolean isAuthenticated(SharedPreferences prefs) {

		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		
		try {
			twitter.getAccountSettings();
			return true;
		} catch (TwitterException e) {
			return false;
		}catch (NullPointerException e)
		{
			return false;
		}
	}
	
	public static void sendTweet(SharedPreferences prefs,String msg) throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
        twitter.updateStatus(msg);
        
	}	
	
	public static String getUser(SharedPreferences prefs)
	{
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		try {
			
			return twitter.getScreenName();
		
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}

	}
	
	public static String getImageUrl(SharedPreferences prefs)
	{
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		
		try {
					
//			user = twitter.showUser(twitter.verifyCredentials().getId());
			return twitter.getProfileImage
					(twitter.getScreenName(), ProfileImage.NORMAL).getURL();
		
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}

	}

}
