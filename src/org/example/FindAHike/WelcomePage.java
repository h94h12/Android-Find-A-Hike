package org.example.FindAHike;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.content.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class WelcomePage extends Activity implements OnClickListener{
    int okButtonRequestCode = 1; 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View startButton = findViewById(R.id.startbutton); 
		startButton.setOnClickListener(this); 
		
		View favButton = findViewById(R.id.favbutton);
		favButton.setOnClickListener(this); 
    }
   
    public void onClick(View v)
    {
    	switch(v.getId())
    	{
    		case R.id.startbutton: 
    			Intent i = new Intent(this, FindHikePage.class);
    			startActivity(i);
    			break; 
    			
    	
    			
    	}
    
    }
    
    
}