/*
 * ScoreActivity.java
 * CS 454
 * Group 2
 * 
 * Purpose
 * 		-
 */

package com.example.wordboggle;

//import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScoreActivity  extends Activity{
	
	/*ListView l;*/
	 int counter=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score);
		
		/*String  arr[]={"word1","word2","word3","word4","word5"};
	    l=(ListView) findViewById(R.id.listView1);
	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arr);
	    l.setAdapter(adapter);*/
		//test
		
		final Button mNewGameButton = (Button) findViewById(R.id.btNewGame);
        
		mNewGameButton.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) { 
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}
			});
		
		 TableLayout tl=(TableLayout)findViewById(R.id.TableLayout01);
	     TableRow tr=new TableRow(this);
	     counter++;
	     TextView tv= new TextView(this);
	     tv.setText("word"+counter);
	     TextView tv2= new TextView(this);
	     tv2.setText("3");
	     
	     tr.addView(tv);
	     tr.addView(tv2);
	       
	     tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}


    
       
   
   
