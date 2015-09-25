/*
 * SquareLayout.java
 * CS 454
 * Group 2
 * 
 * Purpose
 * 		-Create a custom LinearLayout that always remains square based on the devices width
 */

package com.example.wordboggle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/*
 * 
 */
public class SquareLayout extends LinearLayout{

	public SquareLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SquareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	      //--- Additional custom code --
	}

	public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	      //--- Additional custom code --
	}
	
	/*
	 * set the height of the layout equal to the layout's width
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
