package com.himumsaiddad.niggle.myNiggles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.himumsaiddad.niggle.BaseActivity;
import com.himumsaiddad.niggle.DataBaseHelper;
import com.himumsaiddad.niggle.NiggleApplication;
import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;


public class myniggles extends BaseActivity {
	
	public View ClickedView;
	public boolean isLongClick = false;
	public boolean isScroll = false;
    /** Called when the activity is first created. */
	    
    String DEBUG = NiggleApplication.DEBUG;
    
    private DataBaseHelper myDbHelper ;
	private PagingList paging_listview;
	private View pSelected = null;
	private AlertDialog alert;
	public ImageButton delete_button;
	Typeface face;
	Cursor mCursor;
	public Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myDbHelper = new DataBaseHelper(this);
        
        Settings.myDbHelper.myDataBase = Settings.myDbHelper.getReadableDatabase();
        mCursor = Settings.myDbHelper.ReadNiggles();
                
        setContentView(R.layout.my_niggles);
        
        initBaseActivity();
        
        // hide back button which is not included in How to Niggle Activity
        hideMenu();
        
        delete_button = (ImageButton)findViewById(R.id.delete_s); 
        
        delete_button.setVisibility(View.INVISIBLE);
        
        delete_button.getBackground().setAlpha(0);
    	
        delete_button.setOnClickListener(new DeleteClickListener());
        
        // create Alert Dialog
        CreateAlterDialog();
                
        ListView listview = (ListView)findViewById(R.id.list);
              
        paging_listview = new PagingList(this, listview, mCursor);
        
        
        ((ImageButton)findViewById(R.id.next_button))
       	.getBackground().setAlpha(0);
        
        if(paging_listview.isFirst() && paging_listview.isLast())
        {
        	((ImageButton)findViewById(R.id.next_button))
            .setAlpha(0);
        }
        
        ((ImageButton)findViewById(R.id.previous_button))
        .getBackground().setAlpha(0);
        
        ((ImageButton)findViewById(R.id.previous_button))
        .setAlpha(0);
                
        face=Typeface.createFromAsset(getAssets(),
	    		  Settings.FONT_ITC);
        ((TextView)findViewById(R.id.my_niggles)).setTypeface(face); 
        
        listview.setOnScrollListener(new cOnScrollListener()); 
        
        mHandler.postDelayed(new Runnable() {
            public void run() {
            	 paging_listview.updateListView(face, new ItemLongClickListener(),
            			 new ItemClickListener(), new DeleteClickListener(), new EditClickListener());
                            
            }                            
         }, 100);        
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
	
    	 
	 public void resetViewParams(View v, int top_offset)
	 {
		 ViewGroup.LayoutParams params =  v.getLayoutParams();
			((RelativeLayout.LayoutParams) params).setMargins(320, top_offset, 0, 0);
		 v.setLayoutParams(params);
		 v.getBackground().setAlpha(0);
		 v.setVisibility(View.VISIBLE);
	 }
        
    class cOnScrollListener implements AbsListView.OnScrollListener{
	
    	@Override
    	public void  onScrollStateChanged(AbsListView v, int s)
    	{
    		//Utils.showNotification(myniggles.this, "scroll state changed event");
    	}
    	
    	
    	
    	@Override
    	public void onScroll(AbsListView view, int firstVisibleItem,
    						int visibleItemCount, int totalItemCount){
    		
    		if(!isLongClick)
    		  delete_button.setVisibility(View.INVISIBLE);
    		
    		for(int i=0; i<paging_listview.listview.getChildCount(); i++)
            {
    			
    			((TextView)paging_listview.listview.getChildAt(i).
   	            	 findViewById(R.id.nigglename)).setTypeface(face);
    			
    			paging_listview.listview.getChildAt(i)
                .findViewById(R.id.right_button).getBackground().setAlpha(0);
    			   			
            }
    		isLongClick = false;
    	}    	
	
    }
    
    class ItemLongClickListener implements View.OnLongClickListener{
    	
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
		
			isLongClick = true;
			pSelected = v;
		
			isScroll = false;
			final int top_offset = v.getTop();
			mHandler.postDelayed(new Runnable() {
	            public void run() {
	            	resetViewParams(delete_button,  top_offset + 175);
	            }
	    	}, 50);
			
			return true;	
		}
	}
    
    class ItemClickListener implements View.OnClickListener{
    	
  		@Override
  		public void onClick(View v) {
  			// TODO Auto-generated method stub
  			ClickedView = v;
  			OnEditClick(v);

	  	  	if(v != null)
	    	{
	    		showEditButton(v);
	      	}
  		}
  	}

    class EditClickListener implements View.OnClickListener{
    	
  		@Override
  		public void onClick(View v) {
  			// TODO Auto-generated method stub
  			ClickedView = v;
  			OnEditClick(v);
  	    	  			
  		}
  	}

    class DeleteClickListener implements View.OnClickListener{
    	
  		@Override
  		public void onClick(View v) {
  			// TODO Auto-generated method stub
  			
  			alert.show();
  			
  		}
  	}

    private void CreateAlterDialog(){
    	
   	  //create an alterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete the selected item?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   try{
	        		   OnDeleteClick(myniggles.this.pSelected);
	        	   }
	        	   catch(NullPointerException e)
	        	   {
	        		   e.printStackTrace();
	        		   Log.d("My Niggles", "Null pointer for the selected view", e);
	        	   }
	        	   
	        	   dialog.cancel();
	           }
	       		})
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
         }
      });
        
       alert = builder.create();

    }
    
    public void textLayoutFront(){
    
    	ListView lvItems = paging_listview.listview;
    	try{
    	for(int i=0; i<lvItems.getCount(); i++)
        {
            lvItems.getChildAt(i)
            .findViewById(R.id.right_button).getBackground().setAlpha(0);
            lvItems.getChildAt(i)
            .findViewById(R.id.text_layout).bringToFront();
            lvItems.getChildAt(i)
            .findViewById(R.id.text_layout).setEnabled(true);
            lvItems.getChildAt(i)
            .findViewById(R.id.text_layout).setClickable(true);

        }
    	}catch(NullPointerException e)
    	{
    		e.printStackTrace();
    	}

	}
    
    public void showEditButton(View v)
    {	
    	// set previous selected item
    	v.findViewById(R.id.right_button)
			.setVisibility(View.VISIBLE);
	 	
    	v.setBackgroundColor(Color.TRANSPARENT);
    	v.invalidate();
    }
    
    public void showEditButtons()
    {
    	ListView lvItems = paging_listview.listview;
    	try{
	    	for(int i=0; i<lvItems.getCount(); i++)
	        {   
	            lvItems.getChildAt(i)
	            .findViewById(R.id.right_button).setVisibility(View.VISIBLE);  
	        }
    	}catch(NullPointerException e)
    	{
    		e.printStackTrace();
    	}
    }
       
    public void OnDeleteClick(View v)
    {
    	Integer pos =paging_listview.getPositionForView(v);
    	
    	try{
    	String nId = paging_listview.getItem(pos).id;
    	
    	Settings.myDbHelper.DeleteNiggles(nId);
    	
    	paging_listview.deleteItem(pos);
    	
    	paging_listview.notifyChanges();
    	    	
    	showEditButton(v);
    	 
    	if(paging_listview.isFirst() && paging_listview.isLast())
         {
         	((ImageButton)findViewById(R.id.next_button))
             .setAlpha(0);
         }
    	 
    	if (DEBUG == "TRUE")
    	{
    		Toast.makeText(getApplicationContext(), nId,
    	          Toast.LENGTH_SHORT).show();
    	}
    	
    	}catch(Exception e)
    	{
    		
    	}
    }
    
    public void OnPreviousClick(View v)
    {
    	if(!paging_listview.isFirst())
    	{
    		paging_listview.previousPage();
    		
    		if(paging_listview.isFirst())
    			  ((ImageButton)findViewById(R.id.previous_button))
    		        .setAlpha(0);
    		
    		((ImageButton)findViewById(R.id.next_button))
		        .setAlpha(255);	
    	}
    }

    public void OnNextClick(View v)
    {
    	if(!paging_listview.isLast())
    	{
    		paging_listview.nextPage();
    		
    		if(paging_listview.isLast())
  			  ((ImageButton)findViewById(R.id.next_button))
  		        .setAlpha(0);
    		
    		((ImageButton)findViewById(R.id.previous_button))
	        .setAlpha(255);
    	}
    }

    
    public void OnEditClick(View v)
    {
    	Integer pos =paging_listview.getPositionForView(v);
    	 	
    	String wniggleid = paging_listview.getItem(pos).id; 
    	    	
    	String wniggleme =paging_listview.getItem(pos).nigglename;
    	
    	String niggledate = paging_listview.getItem(pos).niggledate;
    	
    	String wknownow = paging_listview.getItem(pos).whatnowknow;
    	
    	String wwilldo = paging_listview.getItem(pos).whatiwilldo;
    	
    	Intent i = new Intent(myniggles.this, detailedniggle.class);
        
    	i.putExtra("wniggleid", wniggleid);
        i.putExtra("wniggleme", wniggleme);
        i.putExtra("niggledate", niggledate);
        i.putExtra("wknownow", wknownow);
        i.putExtra("wwilldo", wwilldo);
        startActivity(i);
    	
    }
    
   
  
}