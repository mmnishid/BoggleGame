package com.example.wordboggle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;


public class PlayActivity extends Activity implements OnClickListener {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		
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

	
	/*
	 * Button Listener Template
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
	/*
	 * start the single player mode
	 */
	public void onClickSingle(View view){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), NewGameActivity.class);
		startActivity(intent);
	}
	
	
	/*
	 * start the two player basic throat mode
	 */
	public void onClickBasic(View view){
		Intent intent = new Intent();
		intent.putExtra("gameMode", "Basic");
		intent.setClass(getApplicationContext(), TwoPlayerGameActivity.class);
		startActivity(intent);
	}
	
	
	/*
	 * start the two player cut throat mode
	 */
	public void onClickCutthroat(View view){
		Intent intent = new Intent();
		intent.putExtra("gameMode", "CutThroat");
		intent.setClass(getApplicationContext(), TwoPlayerGameActivity.class);
		startActivity(intent);
	}
	
	
	/*
	 * display the boggle instructions when pressed
	 */
	public void onClickHelp(View view){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), HelpActivity.class);
		startActivity(intent); 
	}
}

