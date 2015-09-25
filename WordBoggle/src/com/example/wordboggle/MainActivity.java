/*
 * Activity.java
 * CS 454
 * Group 2
 * 
 * Purpose
 * 		-App title screen
 * 		-Start up the android activity
 */

package com.example.wordboggle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	
	ImageButton imageButton1;


	/*
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();
	}

	
	/*
	 * setup a listener for the image button then
	 * listen for the start button to be pressed
	 */
	private void addListenerOnButton() {
		// TODO Auto-generated method stub
		imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
		imageButton1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//Intent intent = new Intent(MainActivity.this, PlayActivity.class);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), PlayActivity.class);
			    startActivity(intent);
			}
		});
	}
	
	
	/*
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
