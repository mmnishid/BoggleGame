package com.example.wordboggle;

import java.util.Random;


public class UserInterface {

	String [] alphabet= {"a","b","c","d","e","f",
						"g","h","i","j","k","l",
						"m","n","o","p","q","r",
						"s","t","u","v","w","x","y","z"};
	String [][] boggleBoard;
	Random randomGenerator;
	
	public UserInterface(){
		boggleBoard = new String[4][4];
		randomGenerator = new Random();
	}
	
	public void setBoard(){
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				boggleBoard[i][j] = randomLetter();
			}
		}
	}
	
	public String getLetter(int x, int y){
		return boggleBoard[x][y];
	}
	
	private String randomLetter(){
		int randomInt = randomGenerator.nextInt(25);
		return alphabet[randomInt];
	}
	
	
}
