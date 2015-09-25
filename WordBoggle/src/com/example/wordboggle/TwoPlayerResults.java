/*
 * TwoPlayerResults.java
 * CS 454
 * Group 2
 */

package com.example.wordboggle;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


/*
 * display the results of a two player game
 */
public class TwoPlayerResults extends Activity {
	
	
	private TwoPlayerGameActivity twoPlayerBasic;
	//textviews for player1 and player2 scores
	private TextView txtPlayer1Score;
	private TextView txtPlayer2Score;
	private TextView txtPlayer1ScoreBelow;
	private TextView txtPlayer2ScoreBelow;
	 
	//word in wordlist
	private String wordtoAdd;
	
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score2);
		
		txtPlayer1Score = (TextView)findViewById(R.id.textView7);
		txtPlayer2Score = (TextView)findViewById(R.id.textView11);
		txtPlayer1ScoreBelow = (TextView)findViewById(R.id.textView8);
		txtPlayer2ScoreBelow = (TextView)findViewById(R.id.textView9);
		
		fillBasicModeResults();
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basic_mode_menu, menu);
		return true;
	}

	
	/*
	 * 
	 */
	public void onClickNewGame(View view){
		// Do something in response to button
		Toast.makeText(this,"Play New Game", Toast.LENGTH_SHORT).show();
		
		// remove previous word list displayed on previous submit
		//for(int i = 0; i < searchedWordList.size(); i++){
			//addWord("word list", 0, R.id.TableLayout02, false);  //remove added views
		
		// remove previous user list displayed from previous game
			//addWord("TOTAL SCORE", TotalPoints, R.id.TableLayout01, false);
		
		//startSingleGame();
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), TwoPlayerGameActivity.class);
		startActivity(intent);
	}
	
	
	/*
	 * 
	 */
	public void onClickMenu(View view){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(getApplicationContext(), PlayActivity.class);
		startActivity(intent);
	}
	
	
	/*
	 * 
	 */
	private void fillBasicModeResults() {
		
		// set the scores of both player
		txtPlayer1Score.setText("Player1 Score = " + String.valueOf(twoPlayerBasic.player1Pts), TextView.BufferType.EDITABLE);
		txtPlayer2Score.setText("Player2 Score = " + String.valueOf(twoPlayerBasic.player2Pts), TextView.BufferType.EDITABLE);
		txtPlayer1ScoreBelow.setText(String.valueOf(twoPlayerBasic.player1Pts), TextView.BufferType.EDITABLE);
		txtPlayer2ScoreBelow.setText(String.valueOf(twoPlayerBasic.player2Pts), TextView.BufferType.EDITABLE);
		
		// show list of all words on board
        for(int i = 0; i < twoPlayerBasic.searchedWordList.size(); i++){
			wordtoAdd = twoPlayerBasic.searchedWordList.get(i);
			addWord(wordtoAdd, 0, R.id.TableLayout01);
		}
        
        //display player1 user list
        for(int i = 0; i < twoPlayerBasic.player1WordList.size(); i++){
			wordtoAdd = twoPlayerBasic.player1WordList.get(i);
			addWord(wordtoAdd, 0, R.id.TableLayout02);
		} 
        
      	//display player2 user list 
        for(int i = 0; i < twoPlayerBasic.player2WordList.size(); i++){
			wordtoAdd = twoPlayerBasic.player2WordList.get(i);
			addWord(wordtoAdd, 0, R.id.TableLayout03);
		}
		
	}
	
	/*
	 * 
	 */
	private void addWord(String wordScored, int points, int view){
		
		final TableLayout tl = (TableLayout)findViewById(view);
		TableRow tr = new TableRow(this);
		TextView tvWord = new TextView(this);
				
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		//TextView tvWord = new TextView(this);
		tvWord.setText(wordScored, TextView.BufferType.EDITABLE);
		tvWord.setGravity(Gravity.CENTER);
		tvWord.setTextAppearance(getApplicationContext(), R.style.scoreText);
		
				
		tr.addView(tvWord);
		tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)); 
						
	}
	
}

