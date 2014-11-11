package com.himumsaiddad.niggle.myNiggles;

import java.util.ArrayList;

import com.himumsaiddad.niggle.R;
import com.himumsaiddad.niggle.Settings;
import com.himumsaiddad.niggle.myNiggles.myniggles.DeleteClickListener;
import com.himumsaiddad.niggle.myNiggles.myniggles.EditClickListener;
import com.himumsaiddad.niggle.myNiggles.myniggles.ItemClickListener;
import com.himumsaiddad.niggle.myNiggles.myniggles.ItemLongClickListener;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PagingList {
	private ArrayList<NiggleListItem> ListData;
	private NiggleAdapter niggleAdapter;
	final public ListView listview;
	private Cursor mCursor;
	private int start_index = 0;
	private int end_index;
	private int total_items;

	PagingList(Context c, ListView thelistview, Cursor cursor)
	{
		mCursor = cursor;
		listview = thelistview;
		listview.setFocusable(false);
		listview.setFocusableInTouchMode(false);
        listview.setItemsCanFocus(false);
        
        total_items = mCursor.getCount();
        
        ListData = new ArrayList<NiggleListItem>();
    	ArrayList<NiggleListItem> currentListData  = new ArrayList<NiggleListItem>();
        
        niggleAdapter = new NiggleAdapter(c,
        		R.layout.my_niggles_row, currentListData);
                
        end_index = (total_items >= 20)? 19 :total_items - 1;
        
        listview.setAdapter(niggleAdapter);
        
        copyCursorToArray(mCursor, ListData);
        copyDataToAdapter(ListData, start_index, end_index);
        
        
        //  listview.setOnScrollListener(new cOnScrollListener());
              // set this adapter as your ListActivity's adapter
               
          listview.setClickable(true);       

	}
	
	public void updateListView(Typeface face, ItemLongClickListener l1, 
			ItemClickListener l2, DeleteClickListener l3, EditClickListener l4)
	{
		for(int i=0; i<listview.getChildCount(); i++)
        {
        	/*listview.getChildAt(i)
            .findViewById(R.id.delete_s).setVisibility(View.INVISIBLE);
            
        	listview.getChildAt(i)
            .findViewById(R.id.delete_s).getBackground().setAlpha(0);
          */
        	listview.getChildAt(i)
            .findViewById(R.id.right_button).getBackground().setAlpha(0);
                       	
        	listview.getChildAt(i)
            .findViewById(R.id.list_item).setOnLongClickListener(l1);
            
        	/*listview.getChildAt(i)
            .findViewById(R.id.delete_s).setOnClickListener(l3);
*/
        	listview.getChildAt(i)
            .findViewById(R.id.list_item).setOnClickListener(l2);
        	
        	listview.getChildAt(i)
            .findViewById(R.id.right_button).setOnClickListener(l4);
        
        	((TextView)listview.getChildAt(i)		
            .findViewById(R.id.nigglename)).setTypeface(face);
            
            ((TextView)listview.getChildAt(i)
            .findViewById(R.id.niggledate)).setTypeface(face);
        }

	}
	
	private void copyCursorToArray(Cursor c, ArrayList<NiggleListItem> d)
    {
    	    	    	
    	c.moveToFirst();
    	
    	for(int i=0; i < c.getCount(); i++)
    	{
    		d.add(new NiggleListItem());
    		
    		d.get(i).id = c.getString(c.getColumnIndex("_id"));
    		d.get(i).nigglename = c.getString(c.getColumnIndex("ZWHATNIGGLEDME"));
    		d.get(i).niggledate = c.getString(c.getColumnIndex("ZCREATIONDATE"));
    		d.get(i).whatnowknow = c.getString(c.getColumnIndex("ZWHATINOWKNOW"));
    		d.get(i).whatiwilldo = c.getString(c.getColumnIndex("ZWHATIWILLNOWDO"));
    		c.moveToNext();
    	}
    }
	
    private void copyDataToAdapter(ArrayList<NiggleListItem> from, int start, int end)
    {
    	niggleAdapter.clear();    	
    	for(int i=start; i <= end; i++)
    	{
    		niggleAdapter.add(from.get(i));
    	}
    }
    
    public void nextPage(){
    	if(end_index < total_items - 1)
    	{
    		start_index = end_index + 1;
    		
    		if(end_index + 20 < total_items)
        	{
        		end_index += 20;
        	}else
        	{
        		end_index = total_items - 1;
        	}
    		
    		copyDataToAdapter(ListData, start_index, end_index);
        	niggleAdapter.notifyDataSetChanged();
        	
    	}   	
    }
    
    public void previousPage(){
    	
    	if( start_index  >= 20)
    	{
    		end_index =start_index - 1;
    		start_index -= 20;	
    	}
    	else
    	{
    		start_index = 0;
    		end_index = 19;
    	}
    	     	
    	copyDataToAdapter(ListData, start_index, end_index);
    	niggleAdapter.notifyDataSetChanged();
    	
    }
    
    public boolean isLast()
    {
    	return (end_index == total_items-1)? true : false;
    }
    
    public boolean isFirst()
    {
    	return (start_index == 0)? true : false;
    }

    NiggleListItem getItem(int pos)
    {
    	return niggleAdapter.getItem(pos);
    }
    
    public void notifyChanges()
    {
     	niggleAdapter.notifyDataSetChanged();
    }
    
    int getPositionForView(View v)
    {
    	return listview.getPositionForView(v);
    }
    
    void deleteItem(int pos)
    {
    	niggleAdapter.remove(getItem(pos));
    	ListData.remove(start_index+pos);
    	
    	if(end_index == total_items -1)
    		end_index--;
    	
    	total_items--;
    	    	    	
    	copyDataToAdapter(ListData, start_index, end_index);
    	niggleAdapter.notifyDataSetChanged();
    	
    }
    
  }