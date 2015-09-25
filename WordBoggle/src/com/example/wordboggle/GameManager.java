/*
 * GameManager.java
 * CS 454
 * Group 2
 * 
 * Purpose
 * 		-Manages the data and behavior of a Boggle game
 * 		-Resets the gameboard every time the player starts a new game
 * 		-returns a string array representing the gameboard
 * 		-Returns a letter from the gameboard based on the x and y coordinates of the gameboard
 * 		-Finds all possible words a user can find on the gameboard using a recursive search function
 */

package com.example.wordboggle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import android.app.Application;
import android.content.res.Configuration;


/*
 * Game Manager is a class that is created on start up and manages all the data
 * for a game and shares that data between the three activities
 */
public class GameManager {
	
	//
	public ArrayList<String> boggleWordList;
	public BoggleBoard boggleBoard;
	public Dictionary dictionary;
	
	
	/*
	 * default constructor
	 */
	public GameManager() {
		boggleBoard = new BoggleBoard();
		boggleWordList = new ArrayList<String>();
	}
		
		
	/*
	 * constructor that initializes the dictionary
	 */
	public GameManager(InputStream dictFile) {

		boggleBoard = new BoggleBoard();
		boggleWordList = new ArrayList<String>();
		
		try {
			dictionary = new Dictionary(dictFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
	 * sets up a new board and finds all the words on the board
	 */
	public void startGame(){
		boggleBoard.setBoard();
		boggleWordList.clear();
		boggleWordListSearch();
	}
	
	
	/*
	 * get the letter at the x and y positions on the board
	 */
	public String getLetter(int x, int y){
		return boggleBoard.getLetter(y, x);
	}
	
	
	/*
	 * get the full boggle board as a string array 
	 */
	public String[] getBoard(){
		String[] board = new String[16];
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				board[i*4 + j] = boggleBoard.getLetter(j, i);
			}
		}
		return board;
	}
	
	
	/*
	 * sets up the board using the given string array
	 */
	public void setBoard(String[] board){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				boggleBoard.setBoard(board);
			}
		}
	}
	
	
	/*
	 * return the word list of all words the word search has found 
	 * in the current boggleboard
	 */
	public ArrayList<String> getWordList(){
		return boggleWordList;
	}
	
	
	/*
	 * search for all the words in the entire boggle board
	 */
	private void boggleWordListSearch(){
		
		//initialize the board search path
		int[][] searchBoard = {{0, 0, 0, 0}, 
							   {0, 0, 0, 0}, 
							   {0, 0, 0, 0}, 
							   {0, 0, 0, 0}};
		
		//call the boggleWordSearch function for every position on the board
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				boggleWordSearch("", j, i, searchBoard);
			}
		}
	}
	
	
	/*
	 * Recursive function that starts at one point in the grid and finds all
	 * words that can be highlighted starting at that point
	 */
	private void boggleWordSearch(String word, int x, int y, int[][] path){
		
		//
		int[][] newPath = new int[4][4];
		copyPath(path, newPath);
		newPath[y][x] = 1;
		String newWord = word + boggleBoard.getLetter(x, y);
		
		//
		if(dictionary.isReal(newWord)){
			if(!boggleWordList.contains(newWord)){
				boggleWordList.add(newWord);
			}
			
			ArrayList<GridPoint> movesList = nextWord(x, y, newPath);
			if(!(movesList.isEmpty())){
				for(int i = 0; i < movesList.size(); ++i){
					GridPoint next = movesList.get(i);
					boggleWordSearch(newWord, next.x, next.y, newPath);
				}
			}
			
		//
		}else if(dictionary.isPartOfAWord(newWord)){
			
			ArrayList<GridPoint> movesList = nextWord(x, y, newPath);
			if(!(movesList.isEmpty())){
				for(int i = 0; i < movesList.size(); ++i){
					GridPoint next = movesList.get(i);
					boggleWordSearch(newWord, next.x, next.y, newPath);
				}
			}
		}
	}
	
	
	/*
	 * Uses the grid position and a 4X4 int array representing the path
	 * already taken to find all possible grid positions the search
	 * algorithm can take from the given position
	 */
	private ArrayList<GridPoint> nextWord(int x, int y, int[][] path){
		
		ArrayList<GridPoint> moves = new ArrayList<GridPoint>();
		for(int i = 0; i < 3; ++i){
			for(int j = 0; j < 3; ++j){
				int mx = x + j - 1;
				int my = y + i - 1;
				if((mx >= 0 && mx < 4) && (my >= 0 && my < 4)){
					if(path[my][mx] == 0){
						GridPoint newMove = new GridPoint(mx, my);
						moves.add(newMove);
					}
				}
			}
		}
		return moves;
	}
	
	
	/*
	 * preforms a deep copy on a 4X4 int array representing the path
	 * the search algorithm takes through the boggle board
	 */
	private void copyPath(int[][] path, int[][] newPath){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				newPath[i][j] = path[i][j];
			}
		}
	}
}
