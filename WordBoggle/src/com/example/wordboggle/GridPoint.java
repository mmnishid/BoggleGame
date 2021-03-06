/*
 * GridPoint.java
 * CS 454
 * Group 2
 * 
 * Purpose
 * 		-stores a location on a grid
 */

package com.example.wordboggle;

public class GridPoint {
	
	//the X and Y positions
	public int x;
	public int y;
	
	
	/*
	 * the default constructor
	 */
	GridPoint(){
		x = -1;
		y = -1;
	}
	
	
	/*
	 * the constructor which passes the X and Y positions
	 */
	GridPoint(int i, int j){
		x = i;
		y = j;
	}
}
