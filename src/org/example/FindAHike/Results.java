package org.example.FindAHike;


import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class Results extends Activity implements OnClickListener {

	private ArrayList<String> criteria = new ArrayList<String>(); 
	private ArrayList<String> responsebody = new ArrayList<String>(); 
	
	final String SH_URL = "http://strollerhikes.com/helen_test/WebApp.php?";  //the base url, criteria will be concatenated to this 
	final String[] distance_display = {"up to 5 miles", "up to 10 miles", "up to 20 miles", "any"}; 
	final String[] distance_url = {"short", "med", "long", "any"}; 
	final String[] length_display = {"short (0-2 miles)", "medium (2-5 miles)", "long (5+ miles)", "any"}; 
	final String[] length_url = {"short", "med", "long", "any"}; 
	final String[] diff_display = {"easy", "medium", "difficult", "any"};
	final String[] diff_url = {"easy", "med", "diff", "any"};
	final String[] shade_display = {"sunny(less than 50% shade)", "shady(more than 50% shade) ", "any"};
	final String[] shade_url = {"not+shady", "shady","any"};
	final String[] trans_display = {"jogger", "small-wheeled stroller", "carrier", "any"};
	final String[] trans_url = {"jogger", "stroller", "carrier", "any"};
	private TextView txtResult;
	private TableLayout resultTable; 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        
        responsebody.clear(); 
        criteria.clear(); 
        
        this.resultTable = (TableLayout)findViewById(R.id.resultTable); 
        
        criteria = savedInstanceState != null ? savedInstanceState.getStringArrayList("criteria"):null;
        
        //check to see if a Bundle is .
        if(criteria == null){
            //get the Bundle out of the Intent...
            Bundle extras = getIntent().getExtras();
            //check to see if "myKey" is in the bundle, if so then assign it's value
            // to mIntentString  if not, assign "nothing passed in" to mIntentString...
            criteria = (ArrayList<String>) (extras != null ? extras.getStringArrayList("criteria") : "nothing passed in");
        }
    
        new GetHikes().execute(); 
      // String temp = listToString(responsebody); 
		//Toast.makeText(this,temp,Toast.LENGTH_LONG).show();      moved to postExecute()
        
        //add stuff to display resulting list of hikes
        //output = JSON 
        //use AJAX to call strollerhikes
        //JSON to decode, see php json library
        //then get array of hikes
        //(webservice request or REST)
        //after findhike, construct URL from form input, pass to web api 
        //webserver calls encode from JSON lib
        //android app calls decode 
        
        View backButton = (Button) findViewById(R.id.Back_Button); 
		backButton.setOnClickListener(this);    
    }
    
    public String getURL() //construct the strollerhikes url from ArrayList of criteria
    {
    	String result = SH_URL; 
    	ArrayList<String> crit = getURLCriteria(); 
    	result += "address=+++"+crit.get(0)+"&distance="+crit.get(1)+"&length="+crit.get(2)+"&terrain="+crit.get(3)+"&shade="+crit.get(4)+"&transport="+crit.get(5)+"&output=json&submit=Find+my+hike+%26%238594%3B";
    	
    	return result; 
    }
    
    public ArrayList<String> getURLCriteria() //may contain logic flaws. returns ArrayList of what user selects from drop down 
    {
    	ArrayList<String> newcrit = new ArrayList<String>(6); 
    	newcrit.add(URLEncoder.encode(criteria.get(0))); 
    	for(int i = 0; i < 5; i++)
    	{
    		newcrit.add("any"); 
    	}
    	for(int i = 0; i < distance_display.length; i++)
    	{
    		if (criteria.get(1).toLowerCase().equals(distance_display[i]))
    			newcrit.set(1, distance_url[i]); 
    	}
    	for(int i = 0; i < distance_display.length; i++)
    	{
    		if (criteria.get(2).toLowerCase().equals(length_display[i]))
    			newcrit.set(2,length_url[i]);
    	}
    	for(int i = 0; i < distance_display.length; i++)
    	{
    		if (criteria.get(3).toLowerCase().equals(diff_display[i]))
    			newcrit.set(3, diff_url[i]); 
    	}
    	for(int i = 0; i < shade_display.length; i++)
    	{
    		if (criteria.get(4).toLowerCase().equals(shade_display[i]))
    			newcrit.set(4, shade_url[i]); 
    	}
    	for(int i = 0; i < distance_display.length; i++)
    	{
    		if (criteria.get(5).toLowerCase().equals(trans_display[i]))
    			newcrit.set(5, trans_url[i]); 
    	}
    	
    	return newcrit; 
    	
    }
    
    private class GetHikes extends AsyncTask<String, String, Void> { //connect to web api and show the result list of hikes 
    	 private ProgressDialog dialog;
    	 protected Context applicationContext;

    	 protected void onPreExecute() {}
    	  @Override
        protected Void doInBackground(String... params) {
          String fromURL = readFeed(Results.this, getURL());
         
            try {
                
            	JSONObject jsonObjecttemp = new JSONObject(fromURL); //convert string to JSON object
            	JSONArray jsonArray = new JSONArray(jsonObjecttemp.getString("results"));
               
            	if(responsebody.isEmpty())
                for (int i = 0; i < jsonArray.length(); i++) {
                   JSONObject jsonObject = jsonArray.getJSONObject(i);
                   responsebody.add(jsonObject.getString("loc").toString());   //namevalues include loc, trail, summary, and dist
                   responsebody.add(jsonObject.getString("dist").toString()); 
                }
                
            } catch (Exception e) {
                Log.i("Catch the exception", e + "");
            }
            return null;
        }

        protected void onPostExecute(Void v) { //add rows to the table to display list of hikes 
        
        	
        	
        	
        	
        	for(int i = 0; i < responsebody.size()-1; i+=2 ) //for some reason the rows are not showing up...
        	{
        	
        		TableRow tr = new TableRow(Results.this);  //first create row 
        		//tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        		
        			TextView col1 = new TextView(Results.this); 
        			col1.setText(responsebody.get(i)); 
        			col1.setGravity(Gravity.LEFT); 
        		    tr.addView(col1); 
        		    
        		    TextView col2 = new TextView(Results.this);
        		    col2.setText(responsebody.get(i+1));
        		    col1.setGravity(Gravity.RIGHT); 
        		    tr.addView(col2); 

        			
        		
        		resultTable.addView(tr); //finally add row to the table 
        	
        	}
        	
        }
    }
    
    public static String readFeed(Context context, String str) { //get a String version of the json output 
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(str);

        try {
            HttpResponse response = client.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content, "ISO-8859-1"));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    
    public String listToString(ArrayList<String> list)
    {
    	String result = ""; 
    	for(int i = 0; i < list.size(); i++)
    	{
    		
    		result += list.get(i); 
    	}
    	return result; 
    	
    }
    
    
    
    public void onClick(View v)
    {
    	switch(v.getId())
    	{
    		case R.id.Back_Button: 
    			Intent i = new Intent(this, FindHikePage.class);
    			startActivity(i);
    			break; 
    			
    	}
    
    }
    
    
}
