/*
 * NewGameActivity.java
 * CS 454
 * Group 2
 * 
 * Purpose
 * 		-plays the single player game utilizing the GameManager class
 * 		-Initializes the Dynamic gameboard screen and keeps track of its dimensions
 * 		-Finds and highlights words via touch screen input and the gameboard dimensions
 * 		-Displays all words found on the Boggle board and the points each is worth in a ScrollView
 * 		-Keeps track of how much time has passed sense the game has passed and stops game after 3 minutes
 * 		-Ends a started game when the submit button is pressed
 * 		-Locks the gameboard so no more words can be inputted when game has ended
 * 		-Displays all words found by the GameManager on the Boggle board and the points each is worth in a ScrollView
 * 		-Start a new game and reset the gameboard and timer when new game button is pressed
 * 		-Return to PlayActivity when menu button is pressed
 */

package com.example.wordboggle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class NewGameActivity extends Activity {
    
	//this double array holds the id of all grid squares
	int[][] viewMatrix = {{R.id.Point00, R.id.Point01, R.id.Point02, R.id.Point03}, 
			  			  {R.id.Point10, R.id.Point11, R.id.Point12, R.id.Point13}, 
			  			  {R.id.Point20, R.id.Point21, R.id.Point22, R.id.Point23}, 
			  			  {R.id.Point30, R.id.Point31, R.id.Point32, R.id.Point33}};
	
	//these values hold the location in pixels of the grid's table layout
	int gridLocationX;
	int gridLocationY;
	
	//these values hold the locations in pixels of all the grid squares in regards to the
	//table layout and the height and width of all the grid squares
	GridPoint[][] locationMatrix;
	int viewHeight;
	int viewWidth;
	int offset;
	int screenSize;
	int normalSize;
	int scoreSize;
	int resetletter = 0;
	
	//to hold grid ids for resetting highlighted grid
	int[] gridIds = new int[16]; 
	//keeps track of the grid square the user slides over during a touch event
	int[][] touchPath;
	
	//bool value to determine whether to enable or disable the touch event
	boolean isTouchEnabled;
	
	//layouts used to manipulate and retrieve information about the xml layout
	private LinearLayout main;
	private SquareTextView editGrid;
	private TextView editText;
	private TextView wordSubmit;
	private TextView textViewShowTime; 
	private ImageView img;
	
	//the word lists that hold the words the user has found and 
	//all the words in the boggle board
	private ArrayList<String> playedWordList;
	private ArrayList<String> searchedWordList;
	
	//the object that managers the boggle board, the dictionary and the search algorithm
	private GameManager game;

	//
	int count;
	boolean slide = true;
	private int TotalPoints;
	private String letter, word;
	private String wordtoAdd;
	int pts;	
	
	// built in android class CountDownTimer
	CountDownTimer countDownTimer;   
	// total count down time in milliseconds
    long totalTimeCountInMilliseconds;       
    
	//
	private boolean txtViewRemoved = false;
	private String[] letterstoReset = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p"};
	
	
	/*
	 * Create and start the activity and initialize the activity variables and start a new boggle game
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_layout);
		
		//initialize variables
		viewHeight = 0;
		viewWidth = 0;
		locationMatrix = new GridPoint[4][4];
		touchPath = new int[4][4];
		wordSubmit = (TextView)findViewById(R.id.WordView);
		textViewShowTime = (TextView)findViewById(R.id.TimeCount);
		img= (ImageView) findViewById(R.id.imageView1);
				
		//find the X position of the left most of the grid container
		main = (LinearLayout) findViewById(R.id.MainLayout);
		main.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					public void onGlobalLayout() {
			            // measure your views here
						SquareLayout grid = (SquareLayout) findViewById(R.id.SquareLayout);
						gridLocationX = (int)grid.getX();
						setTouchLayout();
					}
				});

		//find the Y position of the top of the grid container
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
			gridLocationY = 
				TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
		}
		gridLocationY += getStatusBarHeight();
		
		//read the dictionary text file and passes it to the game manager
		try {
			InputStream fStream = getResources().openRawResource(R.raw.dictionary);
			game = new GameManager(fStream);
			fStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//start the game
		startSingleGame();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	/*
	 * Start the Boggle game
	 */
	private void startSingleGame(){
		
		//clear these variables before starting
		word = "";
		playedWordList = new ArrayList<String>();
		wordSubmit.setText(word);
		img.setImageResource(android.R.color.transparent);
		
		//Set the board
		do {
			game.startGame();
			searchedWordList = game.getWordList();
		}
		while(searchedWordList.size() < 3);
				
		//display the boggle board
		resetMatrix();
		//set the grid path to blank
		resetPath();
		//enable the touch event
		isTouchEnabled = true;
		//start timer
		setTimer();
		count = 0;
	}
	
	
	/*
	 * Sets up the boggle board matrix to display the boggleBoard matrix
	 * in the GameManager
	 */
	private void resetMatrix(){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				editGrid = (SquareTextView) findViewById(viewMatrix[i][j]);
				String[] board = game.getBoard();
				
				setLetterImageonBoard(board[i*4 + j], editGrid);
				editGrid.setText(board[i*4 + j], TextView.BufferType.EDITABLE);
				editGrid.setTextAppearance(getApplicationContext(), R.style.letterText);
			}
		}
	}
	
	
	/*
	 * Resets the touchPath to
	 * in the GameManager
	 */
	private void resetPath(){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				touchPath[i][j] = 0;
			}
		}
	}
	
	
	/*
	 * Alphabet Constants
	 */
	public enum Alphabet {
	    a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z;
	}
	
	
	/*
	 * set board with grid alphabet images
	 */
	public void setLetterImageonBoard(String input, SquareTextView editGrid ) {
		
		Alphabet alphabet = Alphabet.valueOf(input);
				
		switch(alphabet){
		case a :
			editGrid.setBackgroundResource(R.drawable.grida);
			break;
		case b:
			editGrid.setBackgroundResource(R.drawable.gridb);
			break;
		case c :
			editGrid.setBackgroundResource(R.drawable.gridc);
			break;
		case d:
			editGrid.setBackgroundResource(R.drawable.gridd);
			break;
		case e :
			editGrid.setBackgroundResource(R.drawable.gride);
			break;
		case f:
			editGrid.setBackgroundResource(R.drawable.gridf);
			break;
		case g :
			editGrid.setBackgroundResource(R.drawable.gridg);
			break;
		case h:
			editGrid.setBackgroundResource(R.drawable.gridh);
			break;
		case i :
			editGrid.setBackgroundResource(R.drawable.gridi);
			break;
		case j:
			editGrid.setBackgroundResource(R.drawable.gridj);
			break;
		case k :
			editGrid.setBackgroundResource(R.drawable.gridk);
			break;
		case l:
			editGrid.setBackgroundResource(R.drawable.gridl);
			break;	
		case m :
			editGrid.setBackgroundResource(R.drawable.gridm);
			break;
		case n:
			editGrid.setBackgroundResource(R.drawable.gridn);
			break;
		case o :
			editGrid.setBackgroundResource(R.drawable.grido);
			break;
		case p:
			editGrid.setBackgroundResource(R.drawable.gridp);
			break;
		case q :
			editGrid.setBackgroundResource(R.drawable.gridq);
			break;
		case r:
			editGrid.setBackgroundResource(R.drawable.gridr);
			break;
		case s :
			editGrid.setBackgroundResource(R.drawable.grids);
			break;
		case t:
			editGrid.setBackgroundResource(R.drawable.gridt);
			break;
		case u :
			editGrid.setBackgroundResource(R.drawable.gridu);
			break;
		case v:
			editGrid.setBackgroundResource(R.drawable.gridv);
			break;
		case w :
			editGrid.setBackgroundResource(R.drawable.gridw);
			break;
		case x:
			editGrid.setBackgroundResource(R.drawable.gridx);
			break;
		case y :
			editGrid.setBackgroundResource(R.drawable.gridy);
			break;
		case z:
			editGrid.setBackgroundResource(R.drawable.gridz);
			break;
		default:
			break;
		}
	}
	
	
	/*
	 * checks the word lists to see if a word has already been played before
	 */
	public boolean isExists(ArrayList<String> browseList, String input) {
		if (browseList.indexOf(input) >= 0) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * reset the timer to count down for 3 minutes before ending the game
	 */
	private void setTimer(){
		
		totalTimeCountInMilliseconds = 180 * 1000;
		textViewShowTime.setTextAppearance(getApplicationContext(), R.style.normalText);
		
		if(countDownTimer != null){
			countDownTimer.cancel();
		}
		
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
                
                // display total score
        		addWord("TOTAL SCORE", TotalPoints, R.id.TableLayout01, true);
                
                //disable onTouchEvent until new game is called
                isTouchEnabled = false;
                
                // show list of all words on board
                for(int i = 0; i < searchedWordList.size(); i++){
        			wordtoAdd = searchedWordList.get(i);
        			pts = getScore(wordtoAdd);
        			addWord(wordtoAdd, pts, R.id.TableLayout02, true);
        		}
            }
        }.start();
	}
	
	
	/*
	 * the click listener for the button to start the new game
	 */
	public void onClickNewGame(View view){
		
		// Do something in response to button
		Toast.makeText(this,"Play New Game", Toast.LENGTH_SHORT).show();
		
		// remove previous word list displayed on previous submit		
		TotalPoints = 0;
		txtViewRemoved = true;
		addWord("remove", 0, R.id.TableLayout01, false);
		addWord("remove", 0, R.id.TableLayout02, false);
		addWord("User List", TotalPoints, R.id.TableLayout01, true);
		addWord("Word List", TotalPoints, R.id.TableLayout02, true);
		txtViewRemoved = false;
		startSingleGame();
	}

	
	/*
	 * when a game is running end the game
	 */
	public void onClickSubmitScore(View view){
		
		//Toast.makeText(this,"Test Submit", Toast.LENGTH_SHORT).show();
		for(int i = 0; i < searchedWordList.size(); i++){
			wordtoAdd = searchedWordList.get(i);
			pts = getScore(wordtoAdd);
			addWord(wordtoAdd, pts, R.id.TableLayout02, true);
		}
		
		// display total score
		addWord("TOTAL SCORE", TotalPoints, R.id.TableLayout01,true);
		
		//disable onTouchEvent until new game is called
        isTouchEnabled = false;
	}
	
	
	/*
	 * the click listener for the button to go back to the menu page
	 */
	public void onClickMenu(View view){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(getApplicationContext(), PlayActivity.class);
		startActivity(intent);
	}
	
	
	/*
	 * Change the image of a grid image to reflect it being highlighted
	 */
	private void setHighlighting(String input) {
		Alphabet alphabet = Alphabet.valueOf(input);
		switch(alphabet){
		case a :
			editGrid.setBackgroundResource(R.drawable.higha);
			break;
		case b:
			editGrid.setBackgroundResource(R.drawable.highb);
			break;
		case c :
			editGrid.setBackgroundResource(R.drawable.highc);
			break;
		case d:
			editGrid.setBackgroundResource(R.drawable.highd);
			break;
		case e :
			editGrid.setBackgroundResource(R.drawable.highe);
			break;
		case f:
			editGrid.setBackgroundResource(R.drawable.highf);
			break;
		case g :
			editGrid.setBackgroundResource(R.drawable.highg);
			break;
		case h:
			editGrid.setBackgroundResource(R.drawable.highh);
			break;
		case i :
			editGrid.setBackgroundResource(R.drawable.highi);
			break;
		case j:
			editGrid.setBackgroundResource(R.drawable.highj);
			break;
		case k :
			editGrid.setBackgroundResource(R.drawable.highk);
			break;
		case l:
			editGrid.setBackgroundResource(R.drawable.highl);
			break;	
		case m :
			editGrid.setBackgroundResource(R.drawable.highm);
			break;
		case n:
			editGrid.setBackgroundResource(R.drawable.highn);
			break;
		case o :
			editGrid.setBackgroundResource(R.drawable.higho);
			break;
		case p:
			editGrid.setBackgroundResource(R.drawable.highp);
			break;
		case q :
			editGrid.setBackgroundResource(R.drawable.highq);
			break;
		case r:
			editGrid.setBackgroundResource(R.drawable.highr);
			break;
		case s :
			editGrid.setBackgroundResource(R.drawable.highs);
			break;
		case t:
			editGrid.setBackgroundResource(R.drawable.hight);
			break;
		case u :
			editGrid.setBackgroundResource(R.drawable.highu);
			break;
		case v:
			editGrid.setBackgroundResource(R.drawable.highv);
			break;
		case w :
			editGrid.setBackgroundResource(R.drawable.highw);
			break;
		case x:
			editGrid.setBackgroundResource(R.drawable.highx);
			break;
		case y :
			editGrid.setBackgroundResource(R.drawable.highy);
			break;
		case z:
			editGrid.setBackgroundResource(R.drawable.highz);
			break;
		default:
			break;
				
		}
	}
	
	
	/*
	 * make an array of letters and also array of gridIds of grids having these letters
	 * to prepare to remove highlighting on these grids.
	 */
	private void resetHighlight(String input, int viewMatrix){
		letterstoReset[resetletter] = input;
		gridIds [resetletter] = viewMatrix;
		++resetletter;
	}
	
	
	/*
	 * get the grid to reset back to non-highlighted form and remove highlighting.
	 */
	private void backtounhighlighted(String wordScored){
		for(int k = 0; k < word.length() ; k++){
			editGrid = (SquareTextView) findViewById(gridIds[k]);
			setLetterImageonBoard(letterstoReset[k], editGrid);
		}
	}
	
	
	/*
	 * take the x and y positions give by the touch event listern and
	 * checks to see if the location is inside one of the boggle board 
	 * grids and adds the letter to the submit word
	 */
	private void trackLocation(int x, int y){
		
		//
		img.setImageResource(android.R.color.transparent);
		int pointX;
		int pointY;
		
		//iterate through the grid and get each grid square's top left corner X and Y positions
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				pointX = locationMatrix[i][j].x + gridLocationX;
				pointY = locationMatrix[i][j].y + gridLocationY;
				
				//Compare the location of the finger touching the screen with the designated 
				//boundaries of each grid and whether or not the grid has already been selected
				if(x > pointX + offset && x < pointX + viewWidth - offset){
					if(y > pointY + offset && y < pointY + viewHeight -offset){
						if(touchPath[i][j] == 0){
							
							//Get the letter of the selected grid
							editGrid = (SquareTextView) findViewById(viewMatrix[i][j]);
							letter = editGrid.getText().toString();
							
							//highlight the selected grids on touch input
							setHighlighting(letter); 
							
							//add the letter to the end of the word and reset the wordSubmit textview
							word = word + letter;
							wordSubmit.setText(word);
							wordSubmit.setGravity(Gravity.LEFT);
							
							//prepare to reset highlighting after submitting for word validation and scoring
						    resetHighlight(letter,viewMatrix[i][j] ); 
						    touchPath[i][j] = 1;
						}
					}
				}
			}
		}
	}
	
	
	/*
	 * take the string highlighted by the user on the touch screen and
	 * then check if the string is a word on the boggle board that hasn't
	 * been played before submitting it to the player 1 word list
	 */
	public void submit(){
		/*
		pass the word for validation with dictionary, if it is valid, call ScoreKeeping() 
		for now on clicking 'Submit' button after selecting word, it is sent to score board */
		
		
		// gets the word from textview,if valid/non-valid for now
		String wordScored = wordSubmit.getText().toString(); 
		
		if(isExists(searchedWordList, wordScored)){
				
			if(isExists(playedWordList, wordScored)){
				// word already played, do not allow repeated words
				img.setImageResource(R.drawable.played_already);
				
			}else{
				
				//true, so valid, show tick
				img.setImageResource(R.drawable.right);
					
				// send to array of played words
				playedWordList.add(wordScored);  
				
				//get the word score and fill in words with points in scrollable list	
				int points = getScore(wordScored);
				addWord(wordScored, points, R.id.TableLayout01,true);
				
				// get the new total points
				TotalPoints+=points; 
			}   
			
		}else{
			// invalid word, show wrong sign
			img.setImageResource(R.drawable.wrong);
		}
		
		// Add words and score to List		
		backtounhighlighted(wordScored);
		
		// reset word in textview after submit, so next letter clicked is part of new word
		word = ""; 
		wordSubmit.setText("");
		
		//reset highlighting index after word submission
		resetletter = 0; 
		
		// make all grid squares traversable again once a candidate word is submitted, 
		//to select new word
		resetPath();
	}
	
	
	/*
	 * calculate the number of points a word is worth
	 */
	private int getScore(String wordScored){ 
		int points = 0;
		int len = wordScored.length(); 
		if((len==3) || (len == 4) ){
			points = 1;
		}else if(len == 5){
			points = 2;
		}else if(len == 6){
			points = 3;
		}else if(len == 7){
			points = 5;
		}else if(len >= 8){
			points = 11;
		}
		return points;
	}
	
	
	/*
	 * add a word and its points into a scroll list or remove all tablerows in the scrollview 
	 */
	private void addWord(String wordScored, int points, int view, boolean add){
		
		//create a new tablerow and two textviews for words and points
		final TableLayout tl = (TableLayout)findViewById(view);
		TableRow tr = new TableRow(this);
		TextView tvWord = new TextView(this);
		TextView tvPoints = new TextView(this);
		
		//if the add boolean parameter is true add word to scrollview
		if (add){
		
			//set up the textviews and tablerow
			tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			tvWord.setText(wordScored, TextView.BufferType.EDITABLE);
			tvWord.setGravity(Gravity.CENTER);
			tvWord.setTextAppearance(getApplicationContext(), R.style.scoreText);
			tvPoints.setText(String.valueOf(points), TextView.BufferType.EDITABLE); 
			tvPoints.setGravity(Gravity.CENTER);
			tvPoints.setTextAppearance(getApplicationContext(), R.style.scoreText);
			
			//
			if (txtViewRemoved){
				tvPoints.setText("Pts", TextView.BufferType.EDITABLE);
			}
		
			//add textviews to the tablerow then add the tablerow to the scrollview
			tr.addView(tvWord);
			tr.addView(tvPoints);
			tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)); 
		
		//if the add boolean parameter is false remove all words from the scrollview
		}else {
			tl.removeAllViewsInLayout();
	    }
	}
	
	
	/*
	 * listens for a finger touch on the screen and get the location 
	 * of where the screen is being touched and pass that location
	 * to trackLocation function, when the finger is removed from the
	 * screen call the submit function
	 */
	public boolean onTouchEvent(MotionEvent event) {

		//if false disable touch event
		if(!isTouchEnabled){
			return false;
		}
				
		//get the x and y positions of the touch event
	    int X = (int) event.getX();
	    int Y = (int) event.getY();

	    //determine the event action and preform that action
	    int eventaction = event.getAction();
	    switch (eventaction) {

	    	case MotionEvent.ACTION_DOWN:
	    		trackLocation(X, Y);
	    		//Toast.makeText(this, "ACTION_DOWN "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
	    		break;

	    	case MotionEvent.ACTION_MOVE:
	    		trackLocation(X, Y);
	    		//Toast.makeText(this, "ACTION_MOVE "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
	    		break;

	    	case MotionEvent.ACTION_UP:
	    		submit();	
	    		break;
	    }

	    return true;
	}
	
	
	/*
	 * Find the exact position, height and width of every grid
	 * in the boggle board
	 */
	private void setTouchLayout(){
		
		//
		editGrid = (SquareTextView) findViewById(viewMatrix[0][0]);
		viewWidth = editGrid.getWidth();
		viewHeight = editGrid.getHeight();
		offset = (viewWidth * 2) / 6;
		//setSettingSize();
		
		//
		int x;
		int y = 0;
		for(int i = 0; i < 4; ++i){
			x = 0;
			for(int j = 0; j < 4; ++j){
				locationMatrix[i][j] = new GridPoint(x, y);
				x = x + viewWidth;
			}
			y = y + viewHeight;
		}
	}
	
	
	/*
	 * Find the height of the status bar
	 */
	private int getStatusBarHeight() {
	      int height = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          height = getResources().getDimensionPixelSize(resourceId);
	      }
	      return height;
	}
}