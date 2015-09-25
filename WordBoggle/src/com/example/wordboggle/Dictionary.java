/*
 * Dictionary.java
 * CS 454
 * Group 2
 * 
 * Purpose
 * 		-Reads in all the words listed in the dictionary.txt file
 * 		-Stores all the words in a HashMap of ArrayLists which organizes the word alphabetically
 * 		-Searches the dictionary data structure to test if a string input is a real word
 * 		-Searches the dictionary data structure to test if a string input is part of a word
 */

package com.example.wordboggle;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

	/*
	 * This class will maintain all of the valid wordList able to be used in the game.
	 */
	public class Dictionary {
		
		//HashMap that stores the dictionary
		private HashMap<String, ArrayList<String>> dictionary;
		private ArrayList<String> hashInput;
		
		
		/*
		 * initialize the dictionary variable and parse the dictionary
		 * file to add all words in the dictionary file to the 
		 * dictionary HashMap
		 */
		public Dictionary(InputStream dictFile) throws Exception {
			dictionary = new HashMap<String,ArrayList<String>>();
			parseFile(dictFile);
		}

		
		/*
		 * This will check the entire list of wordList to see if the given word is valid 
		 */
		public boolean isReal(String input){
			
			if(input.length() < 3){
				return false;
			}
			
			String sub = input.substring(0, 2);
			if(dictionary.containsKey(sub)){
				ArrayList<String> wordLists = dictionary.get(sub);
				if(wordLists.contains(input)){
					return true;
				}
			}
			
			return false;
		}
		
		
		/*
		 * This will check the entire list of wordList to see if the given string 
		 * is part of a word
		 */
		public boolean isPartOfAWord(String input){
			
			if(input.length() == 1){
				return true;
			}
			
			String sub = input.substring(0, 2);
			if(dictionary.containsKey(sub)){
				ArrayList<String> wordLists = dictionary.get(sub);
				for(int i = 0; i < wordLists.size(); ++i){
					int subSize = input.length();
					int wordSize = wordLists.get(i).length();
					if(subSize <= wordSize){
						String compare = wordLists.get(i).substring(0, subSize);
						if(input.equals(compare)){
							return true;
						}
					}
				}
			}
			
			return false;
		}
		

		/* 
		 * This will parse the dictionary text file and store all of the wordList 
		 * from the word file that are three letters long or greater into a list.
		 */
		public void parseFile(InputStream is) throws Exception {
			String temp;
			String key = "";
			String nextKey;
			DataInputStream input = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			while ((temp = br.readLine()) != null){
				if(temp.length() >= 3){
					
					nextKey = temp.substring(0, 2);
					if(nextKey.equals(key)){
						hashInput.add(temp);
					}else{
						dictionary.put(key, hashInput);
						key = nextKey;
						hashInput = new ArrayList<String>();
						hashInput.add(temp);
					}
				}
			}
			dictionary.put(key, hashInput);
			input.close();
		}
	}

