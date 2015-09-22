/*
 * BoggleBoard.java
 * CS 454
 * Group 2
 */

package com.example.wordboggle;

import java.util.Random;


/*
 * stores and randomly generates a boggle board
 */
public class BoggleBoard {

	//an array containing the entire alphabet
	String [] alphabet= {"a","b","c","d","e","f",
						"g","h","i","j","k","l",
						"m","n","o","p","q","r",
						"s","t","u","v","w","x","y","z"};
	//a double array that represents the Boggle board
	String [][] boggleBoard;
	//the rondom object to generate random letters
	Random randomGenerator;
	
	
	/*
	 * default constructor
	 */
	public BoggleBoard(){
		boggleBoard = new String[4][4];
		randomGenerator = new Random();
	}
	
	
	/*
	 * set the boggle board by filling it up with random letters
	 */
	public void setBoard(){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				boggleBoard[i][j] = randomLetter();
			}
		}
	}
	
	
	/*
	 * set the boggle board using the variables passed in from a string array
	 */
	public void setBoard(String[] board){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				boggleBoard[i][j] = board[i*4 + j];
			}
		}
	}
	
	
	/*
	 * get the letter from the location on the boggle board
	 * specified by the inputs
	 */
	public String getLetter(int x, int y){
		return boggleBoard[y][x];
	}
	
	
	/*
	 * generate a random letter
	 */
	private String randomLetter(){
		int randomInt = randomGenerator.nextInt(26);
		return alphabet[randomInt];
	}
}