/*
 * TwoPlayerGameActivity.java
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
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


/*
 * The activity that plays both two player game modes
 */
public class TwoPlayerGameActivity extends Activity{
	
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
	boolean isTouchEnabled = false;
	
	//layouts used to manipulate and retrieve information about the xml layout
	private LinearLayout main;
	private SquareTextView editGrid;
	private TextView editText;
	private TextView wordSubmit;
	private TextView textViewShowTime; 
	
	//Boolean to determine if game mode is Basic or cutThroat
    private boolean isCutThroat = false;
    //Boolean to determine if device is Master or Slave
    private boolean isMaster = false;
    //Boolean to determine if the game is running
    private boolean gameRunning = false;
    // Local Bluetooth adapter
    private BluetoothAdapter btAdapter = null;
    // Member object for the BlueToothManager
    private BTManager mChatService = null;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    
	// Intent request codes
    private static final int REQUEST_SECURE_DEVICE = 1;
    private static final int REQUEST_INSECURE_DEVICE = 2;
    private static final int REQUEST_DISCONNECT = 3;
    private static final int REQUEST_ENABLE_BT = 4;    
    
	// Message types sent from the BTManager Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_DEVICE_NAME = 2;
    public static final int MESSAGE_TOAST = 3;
    public static final int MESSAGE_READ = 4;
    
    //Message Codes embedded as the first char is every message
    public static final int GAME_MODE = 0;
    public static final int BOGGLE_BOARD = 1;
    public static final int WORD_LIST = 2;
    public static final int PLAYER_TWO_WORD = 3;
    public static final int START_GAME = 4;
    public static final int NEW_GAME = 5;
    public static final int END_GAME = 6;
    
    // Key names received from the BTManager Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
	
    // Keys for running game
    private GameManager game;
    public static ArrayList<String> searchedWordList;
    private String[] tempArray;
    private ImageView img;
    private String letter;
    private int pts;
    
    //managing words and points from the user
    public static ArrayList<String> player1WordList;
    public static int player1Pts = 0;
    private String player1Word;
    boolean player1Done;
    
    //receiving word and points from other player
    public static ArrayList<String> player2WordList;
    public static int player2Pts;
    private String player2Word;
	boolean player2Ready;
	boolean player2Done;
    
	//stores the Timer for the game countdown
	CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds 
    
	
	private String[] letterstoReset = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p"};
	
	private boolean txtViewRemoved = false;
		
	/*
	 * Create and start the activity and initialize the activity variables
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
		img = (ImageView) findViewById(R.id.imageView1);
		
		//set all grids to blank initially, populating only when game starts after connection set up
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				editGrid = (SquareTextView) findViewById(viewMatrix[i][j]);
				editGrid.setBackgroundResource(R.drawable.grid);
			}
		} 
		
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
			e.printStackTrace();
		}
		
		// Get local Bluetooth adapter and make sure the adapter isn't null
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        //Set up Two Player Game
        player1WordList = new ArrayList<String>();
		player2WordList = new ArrayList<String>();
		searchedWordList = new ArrayList<String>();
		player1Pts = 0;
		player2Pts = 0;
		player2Ready = false;
		player1Done = false;
		player2Done = false;
        
        //Rename the word lists to Player1 and player2 and submit button to score
		Toast.makeText(this, "Please Connect to play with another player", Toast.LENGTH_SHORT).show();
		editText = (TextView)findViewById(R.id.WordList01);
		editText.setText("Player 1");
		editText = (TextView)findViewById(R.id.WordList02);
		editText.setText("Player 2");
		editText = (TextView)findViewById(R.id.SubmitScoreBtn);
		editText.setText("Score");
		
		//Determine if game mode is basic or cut throat
		isCutThroat = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String value = extras.getString("gameMode");
		    if(value.equals("CutThroat")){
		    	isCutThroat = true;
		    }
		}
	}

	
	/*
	 *
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basic_mode_menu, menu);
		return true;
	}
	
	
	/*
	 *
	 */
	public void onStart() {
		super.onStart();
	
	    // If BT is not on, request that it be enabled.
	    if (!btAdapter.isEnabled()) {
	    	Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	        
	    // Otherwise, setup the BTManager
	    } else {
	    	if (mChatService == null){ 
	    		mChatService = new BTManager(this, mHandler);
	    	}
	    }
	}
	
	
	/*
	 * 
	 */
    public synchronized void onResume() {
        super.onResume();
        
        // check to see if the BT was enabled if not then enable it
        if (mChatService != null) {
        	
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if(mChatService.getState() == BTManager.STATE_NONE){
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
	
    
    /*
     * stop the BluetoothManager when the activity gets destroyed
     */
    public void onDestroy() {
        super.onDestroy();
        
        // Stop the Bluetooth chat services
        if (mChatService != null){ 
        	mChatService.stop();
        }
    }
	
    
    /*
     * Start the Boggle game
     */
    private void startGame(){
    			
		gameRunning = true;
		
		//clear these variables before starting
		player1WordList.clear();
		player2WordList.clear();	
		player1Word = "";
		wordSubmit.setText(player1Word);
		player1Pts = 0;
		player2Pts = 0;
		
		img.setImageResource(android.R.color.transparent);
		
		//Set the board and send Boggle Board message if it is Master
    	if(isMaster){
			Toast.makeText(this,"Is Master", Toast.LENGTH_SHORT).show();
			
			// set its own board first
			searchedWordList.clear();
			do{
				game.startGame();
				searchedWordList = game.getWordList();
			}while(searchedWordList.size() < 3);
			
			//send boggle board to player 2
			String message = "";
			String[] board = game.getBoard();
			for(int i = 0; i < 16; i++){
				message = message + board[i] + " ";
			}
			sendMessage(BOGGLE_BOARD, message);
			
			//send word list to player 2
			message = "";
			for(int i = 0; i < searchedWordList.size(); i++){
				message = message + searchedWordList.get(i) + " ";
			}
			sendMessage(WORD_LIST, message);
		}
    	
    	//resets the submit button
    	editText = (TextView)findViewById(R.id.SubmitScoreBtn);
		editText.setText("Submit");
		player1Done = false;
		player2Done = false;
		
    	//display the boggle board
		resetMatrix();
		//set the grid path to blank
		resetPath();
		//enable the touch event
		isTouchEnabled = true;
		//start timer
		setTimer();
		//keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    
    /*
     * 
     */
    private void endGame(){
		//Set Button text to "Score"
		editText = (TextView)findViewById(R.id.SubmitScoreBtn);
		editText.setText("Score");
	
		// display total score
		addWord("TOTAL SCORE", player1Pts, R.id.TableLayout01,true);
		
		//if not cut throat then add all the words from the player two
		//word list to the player two layout
		if(!isCutThroat){
			player2Pts = 0;
			for(int i = 0; i < player2WordList.size(); i++){
				player2Word = player2WordList.get(i);
				pts = getScore(player2Word);
				addWord(player2Word, pts, R.id.TableLayout02, true);
				player2Pts = player2Pts + pts;
			}
		}
		addWord("TOTAL SCORE", player2Pts, R.id.TableLayout02, true);
		
		//disable onTouchEvent and gameRunning until new game is called
    	isTouchEnabled = false; 
    	gameRunning = false;
    	player2Ready = false;
    	
    	//
    	editText = (TextView)findViewById(R.id.SubmitScoreBtn);
		editText.setText("Score");
    	
    	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
	 * the click listener for the button to start the new game
	 */
	public void onClickNewGame(View view){
		
		if(!gameRunning){
			
			//wait for other player to respond
			if(player2Ready){
				if(isMaster){
					startGame();
				}else{
					//send a message to the master to make them start the game
					sendMessage(START_GAME, " ");
				}
			}else{
				//send a message to player 2 letting them know your ready to play a new game
				sendMessage(NEW_GAME, " ");
				Toast.makeText(this, "Waiting for player 2", Toast.LENGTH_SHORT).show();
			}
			
			// remove previous word list displayed on previous submit
			player1Pts = 0;
			player2Pts = 0;
			txtViewRemoved = true;
			addWord("remove", 0, R.id.TableLayout01, false);
			addWord("remove", 0, R.id.TableLayout02, false);
			addWord("Player 1", 0, R.id.TableLayout01, true);
			addWord("Player 2", 0, R.id.TableLayout02, true);
			txtViewRemoved = false;
		}
	}
	
	
	/*
	 * when a game is running end the game if player 2 is ready to end
	 * if player 2 is still playing send a message to player 2 letting them
	 * know you're ready to quit. If the game is not running go to the results
	 */
	public void onClickSubmitScore(View view){
		if(gameRunning){
			if(player2Done){
				endGame();
			}else{
				player1Done = true;
				Toast.makeText(this, "player2 is still playing", Toast.LENGTH_SHORT).show();
				Toast.makeText(this, "Wait for player2 to finish", Toast.LENGTH_SHORT).show();
			}
			sendMessage(END_GAME, " ");
			
		}else{
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), TwoPlayerResults.class);
			startActivity(intent);
		}
	} 
	
	
	/*
	 * the click listener for the button to go back to the menu page
	 * if the game is not running go back to the playactivity page
	 */
	public void onClickMenu(View view){
		
		mChatService.stop();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(getApplicationContext(), PlayActivity.class);
		startActivity(intent);
	}
    
    
    /*
     * Sends a message as a string of text to send to player 2
     */
    private void sendMessage(int messageCode, String message) {
    	
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BTManager.STATE_CONNECTED) {
            Toast.makeText(this, "not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the message bytes and tell the BluetoothChatService to write
        String fullMessage = messageCode + message;
        //Toast.makeText(this, "Full Message: " + fullMessage, Toast.LENGTH_SHORT).show();
        byte[] send = fullMessage.getBytes();
        mChatService.write(send);
    }

 
    /*
     * The Handler that gets information back from the BluetoothChatService 
     * and performs an action based on that information
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            	//message indicates a connection state change
            	case MESSAGE_STATE_CHANGE:
            	
            		//
            		switch (msg.arg1) {
            			//
            			case BTManager.STATE_CONNECTED:
            				setStatus("Device Connected to " + mConnectedDeviceName);                
            				break;
            				
            			//
            			case BTManager.STATE_CONNECTING:
            				setStatus("connecting");
            				isMaster = true;
            				break;
            			//
            			case BTManager.STATE_LISTEN:
            				
            			//
            			case BTManager.STATE_NONE:
            				setStatus("not connected");
            				break;
            		}
            		break;
                
                //Save the connected device's name
            	case MESSAGE_DEVICE_NAME:
            		mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
            		Toast.makeText(getApplicationContext(), "Connected to "
            				+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
            		break;
                
                //Read message and Toasts the message back to the user
            	case MESSAGE_TOAST:
            		Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
            				Toast.LENGTH_SHORT).show();
            		break;
                
                //Message is related 
            	case MESSAGE_READ:
            		byte[] readBuf = (byte[]) msg.obj;
            		// construct a string from the valid bytes in the buffer
            		String rawMessage = new String(readBuf, 0, msg.arg1);
            		//get the messageCode integer at the beginning of every message
            		String strNum = rawMessage.substring(0, 1);
            		int messageCode = Integer.parseInt(strNum);
            		//get the rest of the message
            		String message = rawMessage.substring(1);
                
            		//test the message code to see what type of message was sent
            		if(messageCode == GAME_MODE){
            			
                	
                	//if the message is a boggle board parse all the letters and add them to your board
            		}else if(messageCode == BOGGLE_BOARD){
            			tempArray = message.split(" ");
            			game.setBoard(tempArray);
                	
            		//if the message is a word list parse all the words and add them to your searched word list
            		}else if(messageCode == WORD_LIST){
            			searchedWordList.clear();
            			tempArray = message.split(" ");
            			for(int i = 0; i < tempArray.length; ++i){
            				searchedWordList.add(tempArray[i]);
            			}
            			startGame();
                
            		//if the massage is a player 2 word add it to your player 2 word list
            		}else if(messageCode == PLAYER_TWO_WORD){
            			player2Word = message;
            			player2WordList.add(player2Word);
            			//if the game is cut add the word to the player 2 scroll list display
            			if(isCutThroat){
            				pts = getScore(player2Word);
            				player2Pts+=pts; 
            				addWord(player2Word, pts, R.id.TableLayout02,true);
            			}
                	
            		//a message sent from the slave to the master to start the game
            		}else if(messageCode == START_GAME){
            			startGame();
                
            		//a message sent from player two to let the user know they are ready for a new game
            		}else if(messageCode == NEW_GAME){
            			player2Ready = true;
            			Toast.makeText(getApplicationContext(),"Player2 Ready",Toast.LENGTH_SHORT).show();
            			Toast.makeText(getApplicationContext(),"Press NewGame to play",Toast.LENGTH_SHORT).show();
                	
            		//a message sent from player two to let the user know they finished playing
            		}else if(messageCode == END_GAME){
            			player2Done = true;
            			Toast.makeText(getApplicationContext(),"Player2 done",Toast.LENGTH_SHORT).show();
            			if(player1Done){
            				endGame();
            			}
            		}
            }
        }
    };
    
    
	
	/*
	 * Listern for a press to the buttons on the menu
	 */
	public boolean onOptionsItemSelected(MenuItem item){
		Intent serverIntent = null;
		switch(item.getItemId()){
			case R.id.secure_connect_scan:
				serverIntent = new Intent(this, DisplayDevices.class);
				startActivityForResult(serverIntent, REQUEST_SECURE_DEVICE);
				return true;
			
			case R.id.insecure_connect_scan:
				serverIntent = new Intent(this, DisplayDevices.class);
				startActivityForResult(serverIntent, REQUEST_INSECURE_DEVICE);
				return true;
				
			case R.id.discoverable:
	            // Ensure this device is discoverable by others
	            ensureDiscoverable();
	            return true;
		}
		
		return false;
	}

	
	/*
	 * 
	 */
	private void ensureDiscoverable() {
        if(btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
	
	
	/*
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode){
			//Request a connection to a secure device
			case REQUEST_SECURE_DEVICE:
				if (resultCode == Activity.RESULT_OK) {
	                connectDevice(data, true);
	            }
				break;
				
			//Request a connection to a insecure device
			case REQUEST_INSECURE_DEVICE:
				if (resultCode == Activity.RESULT_OK) {
	                connectDevice(data, false);
	            }
				break;
				
			// When the request to enable Bluetooth returns
			case REQUEST_ENABLE_BT:
		        if (resultCode == Activity.RESULT_OK) {
		            // Bluetooth is now enabled, so set up a chat session
		        	mChatService = new BTManager(this, mHandler);
		        } else {
		            // User did not enable Bluetooth or an error occurred
		            finish();
		        }
		}
	}
	
	
	/*
	 * connect to another android device running the same app
	 */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(DisplayDevices.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
	
    
    /*
     * Change the activities connecting status
     */
    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
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
                endGame();
            }

        }.start();
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
		for(int k = 0; k < player1Word.length() ; k++){
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
							player1Word = player1Word + letter;
							wordSubmit.setText(player1Word);
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
		
		if(isValid(searchedWordList, wordScored)&& wordScored.length() >= 3){	
			
			if(isExists(wordScored)){
				
				// word already played, do not allow repeated words
				img.setImageResource(R.drawable.played_already);
				
			}else{
					
				//true, so valid, show tick
				img.setImageResource(R.drawable.right);
				
				//send to array of played words and Player Two
				player1WordList.add(wordScored);
				
				//send this valid word to the other player  
				sendMessage(PLAYER_TWO_WORD, wordScored);
				
				//get the word score and fill in words with points in scrollable list	
				pts = getScore(wordScored);
				addWord(wordScored, pts, R.id.TableLayout01,true);
				
				//get the new total points for player 1
				player1Pts+=pts; 
			}
			
		}else{
			// invalid word, show wrong sign
			img.setImageResource(R.drawable.wrong);
		}
		
		// Add words and score to List
		backtounhighlighted(wordScored);
		
		// reset word in textview after submit, so next letter clicked is part of new word
		player1Word = ""; 
		wordSubmit.setText("");
		
		//reset highlighting index after word submission
		resetletter = 0; 
		
		//make all grid squares traversable again once a  
		//candidate word is submitted, to select new word
		resetPath();
	}
	
	
	/*
	 * Resets the touchPath
	 */
	private void resetPath(){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				touchPath[i][j] = 0;
			}
		}
	}
	
	
	/*
	 * checks the word lists to see if a word has already been played before
	 */
	public boolean isExists(String input) {
		if(isCutThroat){
			if(player1WordList.indexOf(input) >= 0 || player2WordList.indexOf(input) >= 0){
				return true;
			}
		}else{
			if(player1WordList.indexOf(input) >= 0){
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * checks the searchedWordLists to see if a word exists in the boggle board
	 */
	public boolean isValid(ArrayList<String> browseList, String input) {
		if(browseList.indexOf(input) >= 0){
			return true;
		}
		return false;
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
	 * add a word and its points to a scroll list or remove all table rows in the scrollview
	 */
	private void addWord(String wordScored, int points, int view, boolean add){
		
		//create a new table row and two textviews for words and points
		final TableLayout tl = (TableLayout)findViewById(view);
		TableRow tr = new TableRow(this);
		TextView tvWord = new TextView(this);
		TextView tvPoints = new TextView(this);
		
		//if the add boolean parameter is true add word to scrollview
		if(add){
		
			//set up the textviews and tablerow
			tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			tvWord.setText(wordScored, TextView.BufferType.EDITABLE);
			tvWord.setGravity(Gravity.CENTER);
			tvWord.setTextAppearance(getApplicationContext(), R.style.scoreText);
			tvPoints.setText(String.valueOf(points), TextView.BufferType.EDITABLE);
			tvPoints.setGravity(Gravity.CENTER);
			tvPoints.setTextAppearance(getApplicationContext(), R.style.scoreText);
			
			//
			if(txtViewRemoved){
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
