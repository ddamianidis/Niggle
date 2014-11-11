package com.himumsaiddad.niggle.visualiseNiggle;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.himumsaiddad.niggle.R;
 
public class inputinstructions extends Activity {
   
	final String instructions_text = "Your journey\n"+
								"is almost complete.\n\n"+
								 "Knowing what you now know,\n"+
								 "you can save your"+
								 "thoughts and comments.\n"+
								 "If you don't want to... justskip it.";
	
	
	
		
	private ImageView mBackground;
	private ImageButton mskip;
	private ImageButton mwhat_i_now_know;
	private ImageView minstructions_box;
	private TextView minstructions_text;
		
	
 
	public void OnWhatIKnowClick(View v)
    {
    	Intent k = new Intent();
        k.setClassName("com.himumsaiddad.niggle",
                       "com.himumsaiddad.niggle.inputreview");
        
        k.putExtra("frmenu", "1");
        startActivity(k);
        
        super.finish();
        
    }
	    
	    
    public void OnSkipClick(View v)
    {
    	
    		Intent j = new Intent();
            j.setClassName("com.himumsaiddad.niggle",
                           "com.himumsaiddad.niggle.inputreview");
            
            j.putExtra("frmenu", "1");
            
            startActivity(j);
            
            super.finish();
        
    		
        }
    
   

	
	/*Activity thisAct = this;
	 int duration = Toast.LENGTH_SHORT;
	Toast toast = Toast.makeText(thisAct, "hi", duration);*/
   
		@Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.input_instructions);    
      
        
              
      // get references
      mskip = (ImageButton)findViewById(R.id.skip);
      mskip.getBackground().setAlpha(0);
      
      mwhat_i_now_know = (ImageButton)findViewById(R.id.what_i_now_know);
      mwhat_i_now_know.getBackground().setAlpha(0);
      
      minstructions_box = (ImageView)findViewById(R.id.instructions_box);
      minstructions_text = (TextView)findViewById(R.id.instructions_text);
	  
      minstructions_text.setWidth(400);
      minstructions_text.append(instructions_text);
      minstructions_text.setGravity(Gravity.CENTER);
      minstructions_text.setTextColor(Color.WHITE);       
           
     
   }
}