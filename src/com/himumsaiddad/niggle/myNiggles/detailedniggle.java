package com.himumsaiddad.niggle.myNiggles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.himumsaiddad.niggle.BaseActivity;
import com.himumsaiddad.niggle.DataBaseHelper;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.Utils;
import com.himumsaiddad.niggle.share.ShareOnFacebookActivity;
import com.himumsaiddad.niggle.share.ShareScreen;


public class detailedniggle extends BaseActivity {
    /** Called when the activity is first created. */
	Facebook facebook = new Facebook("175729095772478");
	private int requestCode;
	private Handler mHandler = new Handler();
	private DataBaseHelper myDbHelper ;
	private Cursor mCursor;
	private LinearLayout list_container;
	private LayoutInflater inflater;
	private ListItem  l1;
	private ListItem  l2;
	private ListItem  l3;
	private ListItem  l4;
	private ArrayList<ListItem> l_qa = new ArrayList<ListItem>();
	String ShareString = new String();
	
	String[] columns = new String[] { "QUESTION", "ANSWER" };
	int[] to = new int[] { R.id.detailed_text1, R.id.detailed_text2 };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getFacebookId();
        
        //myDbHelper = new DataBaseHelper(this);
        //myDbHelper.openDataBase();
        Settings.myDbHelper.myDataBase = Settings.myDbHelper.getReadableDatabase();
                
        setContentView(R.layout.detailed_niggle);
        
        initBaseActivity();
        
        // hide back button which is not included in How to Niggle Activity
        hideMenu();
                
        Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		
		String wniggleid = extras.getString("wniggleid");
		String wniggleme = extras.getString("wniggleme");
		String wknownow = extras.getString("wknownow");
		String wwilldo = extras.getString("wwilldo");
		
		mCursor = Settings.myDbHelper.ReadAnswersByNiggle(wniggleid);
						
		list_container = (LinearLayout)findViewById(R.id.list_layout);
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		l1 = new ListItem("What niggled me:", wniggleme, 0xFFFF0000, 
				"fonts/ITC Avant Garde CE Gothic Book.ttf");
		l1.buildViewItem();
		
		mCursor.moveToFirst();
		
		for(int i=0; i < mCursor.getCount(); i++)
		{
			String q = "Q: "+ mCursor
			.getString(mCursor.getColumnIndex("QUESTION"));
			String a = "A: "+ mCursor
					.getString(mCursor.getColumnIndex("ANSWER"));
			l_qa.add( new ListItem(q, a, -1, 
					"fonts/ITC Avant Garde CE Gothic Book.ttf"));
			l_qa.get(i).buildViewItem();
	
			if(!mCursor.isLast())
				mCursor.moveToNext();
		}
		
		l2 = new ListItem("What I'II now know:", wknownow, 0xFFFF0000, 
				"fonts/ITC Avant Garde CE Gothic Book.ttf");
		l2.buildViewItem();

		l3 = new ListItem("What I'II now do:", wwilldo, 0xFFFF0000, 
				"fonts/ITC Avant Garde CE Gothic Book.ttf");
		l3.buildViewItem();
		
		long CountOfQuotes = Settings.myDbHelper.CountTableEntries("ZQUOTE");  
	    Random generator = new Random();
	    long r = Math.abs(generator.nextInt());
	    long SelectedItem = (r % CountOfQuotes) + 1;
	    Cursor QuoteCursor = Settings.myDbHelper.ReadQuote(SelectedItem);
	    String mQoute =  "";
	    if(QuoteCursor.getCount() > 0)
        {
	    	QuoteCursor.moveToLast();
	    	mQoute = QuoteCursor.getString(0);
        }
		
		l4 = new ListItem("Niggle Quote:", mQoute, 0xFFFF0000, 
				"fonts/ITC Avant Garde CE Gothic Book.ttf");
		l4.buildViewItem();
		
		((ImageButton)findViewById(R.id.share_button_big))
		.getBackground().setAlpha(0);
        
        ((ImageButton)findViewById(R.id.share_button_big))
        .setOnClickListener( new View.OnClickListener(){
    		
	    		public void onClick(View v)
	    		{
	    			buildShareString();
	    			if(checkShareString())
	    			{
	    				if(isAlreadyLoggedIn())
	    		        {
	    		     	   // ask
	    		    		String title = "Facebook";
	    		    		String  message = "You are already logged in as  " + Settings.FACEBOOK_USERNAME +
	    		    				". Do you want to share using this account?";
	    		    		Utils.ShowYesNoDialog(detailedniggle.this, title, message,
	    		    				yesListener, noListener);
	    		    		
	    		        }else
	    		        {
	    			    	Intent intent = new Intent(detailedniggle.this, ShareOnFacebookActivity.class);
	    			    	intent.putExtra("smessage", ShareString);
		    		        intent.putExtra("slink", "www.himumsaiddad.com");
	    			    	startActivityForResult(intent, requestCode);
	    		        }        
	    				
	    		        
	    			}else
	    			{
	    				Utils.showAlert(detailedniggle.this, "Share Message ", "Your massage is greater than " +
	    								"1000 characters and can't be shared");
	    			}
	    		}
        	}
        );
        
        list_container.invalidate();
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		
		mCursor.close();
		/*if(vPlayer != null)
		{
		  vPlayer.releaseMediaPlayer();
		  vPlayer.doCleanUp();
		}*/
		//unregisterReceiver(intentReceiver);
	}	
    
    @Override
	protected void onStop() {
		super.onStop();
		mCursor.close();
		
		/*if(vPlayer != null)
		{
		  vPlayer.releaseMediaPlayer();
		  vPlayer.doCleanUp();
		}*/
	}

    
    private void buildShareString()
    {
    	if(l1.isSelected())
    		ShareString = l1.mtitle + "\n" + l1.mbody + "\n";
    	
    	for(int i=0; i < l_qa.size(); i++)
    	{
    		if(l_qa.get(i).isSelected())
    			ShareString += l_qa.get(i).mtitle + "\n" + l_qa.get(i).mbody + "\n";  
    	}

    	if(l2.isSelected())
			ShareString += l2.mtitle + "\n" + l2.mbody + "\n";
		
		if(l3.isSelected())
			ShareString += l3.mtitle +  "\n" + l3.mbody + "\n";
		
		if(l4.isSelected())
			ShareString += l4.mtitle + "\n" + l4.mbody;
    }
    
    private boolean checkShareString()
    {
    	return (ShareString.length() <= 1000 ? true : false); 
    }
    
    private class ListItem {
    	View mitem;
    	ImageView button1;
    	ImageView button2;
    	String mtitle;
    	String mbody;
    	int mtColor;
    	String mfont;
    	boolean selected;
    	
    	ListItem(String title, String body, int tColor, String font)
    	{
    		mitem = inflater.inflate(R.layout.detailed_niggle_row, null);;
    		button1 = (ImageView)mitem.findViewById(R.id.round_button);
    		button2 = (ImageView)mitem.findViewById(R.id.round_button_2);
    		button1.setOnClickListener(mOnClickListener1);
    		button2.setOnClickListener(mOnClickListener2);
    		mtitle = title;
    		mbody = body;
    		mtColor = tColor;
    		mfont = font;
    		selected = true;
    	}
    	
    	boolean isSelected(){
    	 return selected;
    	}
    	
    	void setSelected(boolean b){
    		selected = b;
    		if(b)
    		{
    			button1.setVisibility(View.VISIBLE);
    			button2.setVisibility(View.INVISIBLE);
				
    		}else
    		{
    			button1.setVisibility(View.INVISIBLE);
    			button2.setVisibility(View.VISIBLE);
    		}
    	}
    	
    	void setItemText()
        {
        	Typeface face=Typeface.createFromAsset(getAssets(), mfont);

        	((TextView)mitem.findViewById(R.id.detailed_text1))
    			.setText(mtitle);
        	if(mfont != null)
        	{
        		((TextView)mitem.findViewById(R.id.detailed_text1)).setTypeface(face);
        		((TextView)mitem.findViewById(R.id.detailed_text2)).setTypeface(face);
        	}
        	
        	if(mtColor != -1)
        	{
        		((TextView)mitem.findViewById(R.id.detailed_text1)).setTextColor(mtColor);
        	}
        	
    		((TextView)mitem.findViewById(R.id.detailed_text2))
    		.setText(mbody);
    		
    		int h = ((TextView)mitem.findViewById(R.id.detailed_text2)).getHeight(); 
    		
    		
    		((RelativeLayout)mitem.findViewById(R.id.text_bg)).setBackgroundDrawable(getTextBackground(300, h));		
        }
    	
    	ShapeDrawable getTextBackground(int width, int height)
    	{
    		float[] outerR = new float[] { 16, 16, 16, 16, 16, 16, 16, 16};
    		ShapeDrawable sd =new ShapeDrawable(new RoundRectShape(outerR, null,
                    null));
            Paint p = sd.getPaint();
            p.setShadowLayer(5f, 1f, 1f, 0x48a4c55e);
            p.setColor(0xa4c55e);
            p.setAlpha(85);
            sd.setPadding(15, 15, 15, 15);
            sd.setBounds(0, 0, width, height);
            
            return sd;
    	}

    	void buildViewItem()
        {	
            mitem.setPadding(0, 10, 0, 10);	
            setItemText();
            RelativeLayout.LayoutParams layoutParams1 = 
            	    (RelativeLayout.LayoutParams)button1.getLayoutParams();
            layoutParams1.setMargins(410, 0, 0, 0);
            layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL);
            //layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL);
            button1.setLayoutParams(layoutParams1);
            RelativeLayout.LayoutParams layoutParams2 = 
            	    (RelativeLayout.LayoutParams)button2.getLayoutParams();
            layoutParams2.setMargins(410, 0, 0, 0);
            layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
            button2.setLayoutParams(layoutParams2);

            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.INVISIBLE);
            list_container.addView(mitem);
                     }
    	
    	private View.OnClickListener mOnClickListener1 = new View.OnClickListener(){
    		
    		public void onClick(View v)
    		{
    			if(isSelected())
    			{
    				setSelected(false);
    			}
    		}
    	};

    	private View.OnClickListener mOnClickListener2 = new View.OnClickListener(){
    		
    		public void onClick(View v)
    		{
    			if(!isSelected())
    			{
    				setSelected(true);
    			}

    		}
    	};
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
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	getFacebookId();
    	
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
           	
           	Intent intent = new Intent(detailedniggle.this, ShareOnFacebookActivity.class);
	    	intent.putExtra("smessage", ShareString);
	        intent.putExtra("slink", "www.himumsaiddad.com");
	    	startActivityForResult(intent, requestCode);
	    	
	    	dialog.cancel();
        }

        
    };
    
    
    DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
        		try {
    				facebook.logout(detailedniggle.this);
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
				
		    	Intent intent = new Intent(detailedniggle.this, ShareOnFacebookActivity.class);
		    	intent.putExtra("smessage", ShareString);
		        intent.putExtra("slink", "www.himumsaiddad.com");
		    	startActivityForResult(intent, requestCode);

        		dialog.cancel();
        }
    };

}
