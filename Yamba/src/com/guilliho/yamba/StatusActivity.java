package com.guilliho.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher {
    
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        // Inflate java from xml
    	setContentView(R.layout.status);
    	
    	// Find Views
    	editText = (EditText) findViewById(R.id.editText);
    	updateButton = (Button) findViewById(R.id.buttonUpdate);
    	textCount = (TextView) findViewById(R.id.textCount);
    	
    	textCount.setText(Integer.toString(140));
    	textCount.setTextColor(Color.GREEN);
    	
    	textCount.addTextChangedListener(this);
    	updateButton.setOnClickListener(this);
    	
    	twitter = new Twitter("student","password");
    	twitter.setAPIRootUrl("http://yamba.marakana.com/api");
    	
    }
    
    class PostToTwitter extends AsyncTask<String, Integer, String> {

    	// Called to initiate the background activity
    	@Override
    	protected String doInBackground(String... arg0) {
    		
    		try {
    			
    			Twitter.Status status = twitter.updateStatus(arg0[0]);
    			return status.text;
    			
    		} catch (TwitterException e) {
    			
    			Log.e(TAG, e.toString());
    			e.printStackTrace();
    			return "Failed to post";
    			
    		}
    		
    	}
    	
    	// Called when there's a status to be udpated
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		
    		super.onProgressUpdate(values);
    		// Not used in this case
    		
    	}
    	
    	// Called once the background activity has completed
    	@Override
    	protected void onPostExecute(String result) {
    		
    		Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
    		
    	}

    }

	@Override
	public void onClick(View arg0) {

		new PostToTwitter().execute(editText.getText().toString());
		Log.d(TAG, "onClicked");
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		
		int count = 140 - s.length();
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN);
		
		if (count < 10)
			textCount.setTextColor(Color.YELLOW);
		
		if (count < 0)
			textCount.setTextColor(Color.RED);
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
}