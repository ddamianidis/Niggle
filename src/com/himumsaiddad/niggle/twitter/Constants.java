package com.himumsaiddad.niggle.twitter;

public class Constants {

	public static final String CONSUMER_KEY = "rfWntw8sQLLMFbiONpVEw";
	public static final String CONSUMER_SECRET= "BR76HaGxV3AOtRIZUBhMndBlt7EC3a7ZvfH0j9RwQ";
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
	//public static final String	OAUTH_CALLBACK_URL		= "oob";
	//public static final String	OAUTH_CALLBACK_URL		= "http://development.himumsaiddad.com/twitter/success.html";

}

