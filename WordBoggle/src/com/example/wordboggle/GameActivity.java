package com.example.wordboggle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
    
	int [][] buttonMatrix= {{R.id.btn00, R.id.btn01, R.id.btn02, R.id.btn03}, 
							{R.id.btn10, R.id.btn11, R.id.btn12, R.id.btn13}, 
							{R.id.btn20, R.id.btn21, R.id.btn22, R.id.btn23}, 
							{R.id.btn30, R.id.btn31, R.id.btn32, R.id.btn33}};
	
	GameManager game;
	
	int count;
	boolean slide = true;
	private int TotalPoints;
	private TextView editText;
	private TextView wordSubmit;
	private String letter, word;
	private ArrayList<String> playedWordList;
	private ImageView img;
	TextView textViewShowTime; 
	CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds 
    
   // ArrayList<String> listItems=new ArrayList<String>();
	//ArrayAdapter<String> adapter;
	//ListView listView1;
	
	Button one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		
		
		
		try {
			InputStream fStream = getResources().openRawResource(R.raw.dictionary);
			game = new GameManager(fStream);
			fStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		game.startGame();
		
		setMatrix();
		
		
		textViewShowTime = (TextView) findViewById(R.id.tvTimeCount);
		setTimer();
		
		
		wordSubmit = (TextView)findViewById(R.id.wordSubmit);
		
		//test to see if the dictionary read in the text file
		//wordSubmit.setText(game.dictionary.getWord(0));

		count = 0;
		
		word = "";
		
		playedWordList = new ArrayList<String>();
		img= (ImageView) findViewById(R.id.imageView1);
		
		final Button mScoreButton = (Button) findViewById(R.id.NewGameButton);							
		mScoreButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				img.setImageResource(android.R.color.transparent);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		}); 
		
		
		
	  /*  listView1 = (ListView)findViewById(R.id.listView1);
		listItems = new ArrayList<String>();
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		listView1.setAdapter(adapter);*/
			
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//public void onClick00(View view){
	    // Do something in response to button
		
		
	//}
	
	public void findButtonsView(){
		 one =(Button)findViewById(R.id.btn00);
	    
	     two=(Button)findViewById(R.id.btn01);
	   
	     three=(Button)findViewById(R.id.btn02);
	    
	     four=(Button)findViewById(R.id.btn03);
	   
	     five=(Button)findViewById(R.id.btn10);
	  
	     six=(Button)findViewById(R.id.btn11);
	  
	     seven=(Button)findViewById(R.id.btn12);
	    
	     eight=(Button)findViewById(R.id.btn13);
	   
	     nine=(Button)findViewById(R.id.btn20);
	   
	     ten=(Button)findViewById(R.id.btn21);
	    
	     eleven=(Button)findViewById(R.id.btn22);
	    
	     twelve=(Button)findViewById(R.id.btn23);
	    
	     thirteen=(Button)findViewById(R.id.btn30);
	    
	     fourteen=(Button)findViewById(R.id.btn31);
	   
	     fifteen=(Button)findViewById(R.id.btn32);
	    
	     sixteen=(Button)findViewById(R.id.btn33);
		
	}
	
    
	
	public void onClick00(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(0,0);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick01(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(0,1);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick02(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(0,2);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick03(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(0,3);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick10(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(1,0);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick11(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(1,1);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick12(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(1,2);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick13(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(1,3);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	
	public void onClick20(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(2,0);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick21(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(2,1);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick22(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(2,2);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick23(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(2,3);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	
	public void onClick30(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(3,0);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick31(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(3,1);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick32(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(3,2);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	public void onClick33(View view){
	    // Do something in response to button
		//if (slide){
		img.setImageResource(android.R.color.transparent);
		letter = game.getLetter(3,3);
		word = word + letter;
		view.setClickable(false);
		view.setEnabled(false);
		wordSubmit.setText(word, TextView.BufferType.EDITABLE);
		//}
	}
	
	
	public void onClickReset(View view){
		if (slide){
		img.setImageResource(android.R.color.transparent);
		wordSubmit.setText("");
		game.startGame();
		setMatrix();
		enableMatrix();
		word = "";
		}
	}
	
	
	/*
	 * Sets up the boggle board matrix to display the boggleBoard matrix
	 * in the GameManager
	 */
	private void setMatrix(){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				editText = (TextView) findViewById(buttonMatrix[i][j]);
				String[] board = game.getBoard();
				editText.setText(board[i*4 + j], TextView.BufferType.EDITABLE);
			}
		}
	}
	
	
	/*
	 * 
	 */
	private void enableMatrix(){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				editText = (TextView) findViewById(buttonMatrix[i][j]);
				editText.setClickable(true);
				editText.setEnabled(true);
			}
		}
	}	
	
	public boolean isExists(String input) {
		 if (playedWordList.indexOf(input) >= 0) {
		 return true;
		}
		return false;
	}
	public void onClickSubmit(View view){
		/*For now calling
		scorekeeping function from here. Move this call to proper place ie first select by sliding, then
		pass that word for validation with dictionary, if it is valid, call ScoreKeeping() 
		for now on clicking 'Submit' button after selecting word, it is sent to score board */
		
		if (slide){
		
			String wordScored = wordSubmit.getText().toString(); // gets the word from textview,if valid/non-valid for now
			if(game.dictionary.isReal(wordScored)){
				
				if(isExists(wordScored)){
					// word already played, do not allow repeated words
					img.setImageResource(R.drawable.played_already);
				}
				else {
			
					//true, so valid, show tick
					img.setImageResource(R.drawable.right);
		    
					// send to array of played words
					playedWordList.add(wordScored);
					// ScoreKeeping() function implemented here 
			
					int points = 0;
					int len = wordScored.length(); 
					if((len==3) || (len == 4) ){
						points = 1;
					}
					if(len==5){
						points = 2;
					}
					if(len==6){
						points = 3;
					}
					if(len == 7){
						points = 5;
					}
					if(len>=8){
						points = 11;
					}
					
					TotalPoints+=points; // send total points to GameResults screen.
					
					// remove this later
					final TextView tvTotal;
					tvTotal = (TextView)findViewById(R.id.total);
					tvTotal.setText("        " + String.valueOf(TotalPoints), TextView.BufferType.EDITABLE);
					
					// fill in words with points in scrollable list
					
					if (len >0) {
					final TableLayout tl  =(TableLayout)findViewById(R.id.TableLayout01);
					TableRow tr = new TableRow(this);
				    tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					
				    TextView tvWord = new TextView(this);
					tvWord.setText(wordScored, TextView.BufferType.EDITABLE);
					tvWord.setGravity(Gravity.CENTER);
					tvWord.setTextAppearance(getApplicationContext(), R.style.scoreText);
							
					TextView tvPoints = new TextView(this);
					tvPoints.setText(String.valueOf(points), TextView.BufferType.EDITABLE); 
					tvPoints.setGravity(Gravity.CENTER);
					tvPoints.setTextAppearance(getApplicationContext(), R.style.scoreText);
							
					tr.addView(tvWord);
			        tr.addView(tvPoints);
			        tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)); 
			     
					}
				}
			}
			else {
			// invalid word, show wrong sign
				img.setImageResource(R.drawable.wrong);
			}
		
	       
		word = ""; // reset word in textview after submit, so next letter clicked is part of new word
		//wordSubmit.setText("");
		// make all buttons clickable again once a candidate word is submitted, to select new word
		
		findButtonsView(); 
		one.setEnabled(true);
		one.setClickable(true);
        two.setEnabled(true);
        two.setClickable(true);
        three.setEnabled(true);
        three.setClickable(true);
        four.setEnabled(true);
        four.setClickable(true);
        five.setEnabled(true);
        five.setClickable(true);
        six.setEnabled(true);
        six.setClickable(true);
        seven.setEnabled(true);
        seven.setClickable(true);
        eight.setEnabled(true);
        eight.setClickable(true);
        nine.setEnabled(true);
        nine.setClickable(true);
        ten.setEnabled(true);
        ten.setClickable(true);
        eleven.setEnabled(true);
        eleven.setClickable(true);
        twelve.setEnabled(true);
        twelve.setClickable(true);
        thirteen.setEnabled(true);
        thirteen.setClickable(true);
        fourteen.setEnabled(true);
        fourteen.setClickable(true);
        fifteen.setEnabled(true);
        fifteen.setClickable(true);
        sixteen.setEnabled(true); 
        sixteen.setClickable(true);

		}
		
	}
	
	public void generatewordList() {
		//all generate-able words and score to 
		final TableLayout tl2  =(TableLayout)findViewById(R.id.TableLayout02);
		TableRow tr2 = new TableRow(this);
	    tr2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
	    TextView tvWord2 = new TextView(this);
	    TextView tvPoints2 = new TextView(this);
		
        for(int i = 0; i < game.boggleWordList.size(); ++i){
        	tvWord2.setText("1", TextView.BufferType.EDITABLE);
			tvWord2.setGravity(Gravity.CENTER);
			tvWord2.setTextAppearance(getApplicationContext(), R.style.scoreText);
					
			
			tvPoints2.setText("", TextView.BufferType.EDITABLE); 
			tvPoints2.setGravity(Gravity.CENTER);
			tvPoints2.setTextAppearance(getApplicationContext(), R.style.scoreText);
					
			tr2.addView(tvWord2);
	        tr2.addView(tvPoints2);
	        tl2.addView(tr2,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)); 
		
        }
	}
	private void setTimer(){
		totalTimeCountInMilliseconds = 180 * 1000;
		textViewShowTime.setTextAppearance(getApplicationContext(), R.style.normalText);
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
          
            public void onTick(long leftTimeInMilliseconds) {
            	long seconds = leftTimeInMilliseconds / 1000;
            	textViewShowTime.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
          	           	
                        	
            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                textViewShowTime.setText("Time up!");
                slide = false ;
                
                findButtonsView();
                
                one.setEnabled(false);
                two.setEnabled(false);
                three.setEnabled(false);
                four.setEnabled(false);
                five.setEnabled(false);
                six.setEnabled(false);
                seven.setEnabled(false);
                eight.setEnabled(false);
                nine.setEnabled(false);
                ten.setEnabled(false);
                eleven.setEnabled(false);
                twelve.setEnabled(false);
                thirteen.setEnabled(false);
                fourteen.setEnabled(false);
                fifteen.setEnabled(false);
                sixteen.setEnabled(false);
                
                one.setClickable(false);
                two.setClickable(false);
                three.setClickable(false);
                four.setClickable(false);
                five.setClickable(false);
                six.setClickable(false);
                seven.setClickable(false);
                eight.setClickable(false);
                nine.setClickable(false);
                ten.setClickable(false);
                eleven.setClickable(false);
                twelve.setClickable(false);
                thirteen.setClickable(false);
                fourteen.setClickable(false);
                fifteen.setClickable(false);
                sixteen.setClickable(false);
                
                // show list of all words on board
             // Add all generate-able words and score to List
                generatewordList();
                
            }

        }.start();
	}
	
	
}
