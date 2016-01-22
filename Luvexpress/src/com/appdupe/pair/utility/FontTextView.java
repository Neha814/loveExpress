package com.appdupe.pair.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView {

	public FontTextView(Context context) {
		super(context);
		if(!isInEditMode())
		font(context);
	}

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode())
		font(context);
	}

	public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(!isInEditMode())
		font(context);
	}

	private void font(Context context) {
		Typeface tf=Typeface.createFromAsset(context.getAssets(),
				"fonts/Montserrat-Regular.ttf");
		setTypeface(tf);
	}
}