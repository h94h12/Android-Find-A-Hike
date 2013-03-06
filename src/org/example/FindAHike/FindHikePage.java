package org.example.FindAHike;
//FindHikePage displays all the drop down menus for the user to enter hike criteria. 

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View.OnClickListener;

public class FindHikePage extends Activity implements OnClickListener{
	
	private Spinner[] spinners = new Spinner[5];  
	private ArrayList<ArrayAdapter<CharSequence>> adapters = new ArrayList<ArrayAdapter<CharSequence>>();
	private int[] id = {R.id.distancespinner, R.id.lengthspinner, R.id.diffspinner, R.id.shadespinner, R.id.transportspinner}; 
	private int[] array = {R.array.distance_array, R.array.length_array, R.array.difficulty_array, R.array.shade_array, R.array.transport_array}; 
	
	protected void onCreate(Bundle savedInstanceState)
	{
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findhike);
		
		View goButton = (Button) findViewById(R.id.Ok_Button); 
		goButton.setOnClickListener(this); 
		
		for(int i = 0; i < array.length; i++)
		{
			spinners[i] = (Spinner)findViewById(id[i]);
			adapters.add(ArrayAdapter.createFromResource(this, array[i], android.R.layout.simple_spinner_item)); 
			adapters.get(i).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
			spinners[i].setAdapter(adapters.get(i));  
			
		}
		
	}
	
	public ArrayList<String> getText()
	{
		ArrayList<String> result = new ArrayList<String>();  
		EditText addresstext = (EditText)findViewById(R.id.address); 
		result.add(addresstext.getText().toString()); 
		for(int i = 0; i < spinners.length; i++)
		{
			result.add (String.valueOf(spinners[i].getSelectedItem()));  
			 
		}
		
		return result; 
	}
	
	public void onClick(View v)
    {
    	switch(v.getId())
    	{
    		case R.id.Ok_Button: 
    			Intent i = new Intent(this, Results.class);
    			i.putStringArrayListExtra("criteria", getText()); 
    			startActivity(i);
    			break; 
    			
    	
    			
    	}
    
    }
	
	
	

}
