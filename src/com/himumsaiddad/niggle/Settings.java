package com.himumsaiddad.niggle;

import java.util.ArrayList;

import com.himumsaiddad.niggle.mAnswer;

import android.content.SharedPreferences;

public class Settings {

    private static SharedPreferences mPreferences = null;
    private static final String TOKEN = "TOKEN";
    private static final String SESSION_TOKEN = "SESSION_TOKEN";
    private static final String EMAIL = "EMAIL";
    private static final String FACEBOOK_ACCESS_TOKEN = "FACEBOOK_ACCESS_TOKEN";
    private static final String FACEBOOK_ACCESS_EXPIRES = "FACEBOOK_ACCESS_EXPIRES";

    public static final String FONT_ITC = "fonts/ITC Avant Garde CE Gothic Demi.ttf";
    public static  final String FONT_HELV = "fonts/HelveticaNeueLTStd-HvCnO.otf";
    public static  final float Q_A_1_SIZE = 14f;
    public static  final float Q_A_2_SIZE = 17f;
    public static  final float SPLASH_SIZE = 16f;
    public static  final float FB_STRING_SIZE = 16f;
    public static  final float TW_STRING_SIZE = 16f;
    public static  final float ABOUT_SIZE = 17f;
    public static  final float MYNIGGLES_1_SIZE = 16f;
    public static  final float MYNIGGLES_2_SIZE = 12f;
    public static  final float WISDOM_SIZE = 17f;
    public static  String FACEBOOK_ID = "";
    public static  String FACEBOOK_USERNAME = "";
    public static boolean DEBUG_MODE = false;
    
    public static ArrayList<mAnswer> niggleAnswers;
    
    public static String NiggleText;
    
    public static DataBaseHelper myDbHelper;
    
    public static void initialize(SharedPreferences sharedPreferences) {
        mPreferences = sharedPreferences;
        Settings.niggleAnswers  = new ArrayList<mAnswer>();
    }
    
    public static void setToken(String value) {
        setString(TOKEN, value);
    }
    
    public static String getToken() {
        return mPreferences.getString(TOKEN, "");
    }
    
    public static void setSessionToken(String value) {
        setString(SESSION_TOKEN, value);
    }
    
    public static String getSessionToken() {
        return mPreferences.getString(SESSION_TOKEN, "");
    }
    
    public static void setEmail(String value) {
        setString(EMAIL, value);
    }
    
    public static String getEmail() {
        return mPreferences.getString(EMAIL, "");
    }
      
    public static void setFacebookAccessToken(String value) {
        setString(FACEBOOK_ACCESS_TOKEN, value);
    }
    
    public static String getFacebookAccessToken() {
        return mPreferences.getString(FACEBOOK_ACCESS_TOKEN, "");
    }
    
    public static void setFacebookAccessExpires(long value) {
        setLong(FACEBOOK_ACCESS_EXPIRES, value);
    }
    
    public static long getFacebookAccessExpires() {
        return mPreferences.getLong(FACEBOOK_ACCESS_EXPIRES, 0);
    }
    
    private static void setString(String key, String value) {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putString(key,value);
        edit.commit();
    }
    
    private static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }
    
    private static void setInt(String key, int value) {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    
    private static void setLong(String key, long value) {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putLong(key, value);
        edit.commit();
    }
}
