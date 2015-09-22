/*
 * SquareTextView.java
 * CS 454
 * Group 2
 */

package com.example.wordboggle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


/*
 * 
 */
public class SquareTextView extends TextView {

	public SquareTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SquareTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	      //--- Additional custom code --
	}

	public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	      //--- Additional custom code --
	}
	
	/*
	 * set the height of the text view equal to the text view's width
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}