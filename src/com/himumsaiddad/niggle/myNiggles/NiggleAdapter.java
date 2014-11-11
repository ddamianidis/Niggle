package com.himumsaiddad.niggle.myNiggles;

import java.util.ArrayList;

import com.himumsaiddad.niggle.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NiggleAdapter extends ArrayAdapter<NiggleListItem>{

    Context context;
    int layoutResourceId;   
    ArrayList<NiggleListItem> data = null;
   
    public NiggleAdapter(Context context, int layoutResourceId, ArrayList<NiggleListItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NiggleHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new NiggleHolder();
            holder.nigglename = (TextView)row.findViewById(R.id.nigglename);
            holder.niggledate = (TextView)row.findViewById(R.id.niggledate);
           
            row.setTag(holder);
        }
        else
        {
            holder = (NiggleHolder)row.getTag();
        }
       
        if(position < data.size() )
        {
        	NiggleListItem niggle = data.get(position);
        	holder.nigglename.setText(niggle.nigglename);
        	holder.niggledate.setText(niggle.niggledate);
        	
        }else
        {
        	holder.nigglename.setText("");
        	holder.niggledate.setText("");
        }
        	
       
        return row;
        
    }
    
    public void setData(ArrayList<NiggleListItem> data)
    {
    	this.data = data;
    }
   
    static class NiggleHolder
    {
    	TextView nigglename;
        TextView niggledate;
    }
}