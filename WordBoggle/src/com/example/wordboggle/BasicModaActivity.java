package com.example.wordboggle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class BasicModaActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basicmode);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basic_mode_menu, menu);
		return true;
	}

}
