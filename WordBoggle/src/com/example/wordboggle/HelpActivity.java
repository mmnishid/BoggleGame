package com.example.wordboggle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HelpActivity extends Activity {
	ImageButton imageButton1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		addListenerOnButton();
		
		}

	private void addListenerOnButton() {
		// TODO Auto-generated method stub
		
		imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
		imageButton1.setOnClickListener(new OnClickListener() {
	
		public void onClick(View arg0) {
		
			Intent intent = new Intent(HelpActivity.this, PlayActivity.class);
		    startActivity(intent);
		}

		
	});

	}
}
