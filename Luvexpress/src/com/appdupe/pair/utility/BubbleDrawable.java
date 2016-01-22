package com.appdupe.pair.utility;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class BubbleDrawable extends Drawable {

	// Public Class Constants
	// //////////////////////////////////////////////////////////

	public static final int LEFT = 1;
	public static final int RIGHT = 2;

	public static final int margintop = 30;

	// Private Instance Variables
	// //////////////////////////////////////////////////////////

	private Paint mPaint;
	private int mColor = Color.RED;;

	private RectF mBoxRect;
	private int mBoxWidth;
	private int mBoxHeight;
	private float mCornerRad;
	private Rect mBoxPadding = new Rect();

	private Path mPointer;
	private int mPointerWidth = 30;
	private int mPointerHeight = 30;
	private int mPointerAlignment;

	// Constructors
	// //////////////////////////////////////////////////////////

	public BubbleDrawable(int pointerAlignment) {
		setPointerAlignment(pointerAlignment);
		initBubble();
	}

	// Setters
	// //////////////////////////////////////////////////////////

	public void setPadding(int left, int top, int right, int bottom) {
		mBoxPadding.left = left;
		mBoxPadding.top = top;
		mBoxPadding.right = right;
		mBoxPadding.bottom = bottom;
	}

	public void setBubbleColor(int color) {
		mColor = color;
		mPaint.setColor(mColor);
	}

	public void setCornerRadius(float cornerRad) {
		mCornerRad = cornerRad;
	}

	public void setPointerAlignment(int pointerAlignment) {
		if (pointerAlignment < 0 || pointerAlignment > 3) {
			Log.e("BubbleDrawable", "Invalid pointerAlignment argument");
		} else {
			mPointerAlignment = pointerAlignment;
		}
	}

	public void setPointerWidth(int pointerWidth) {
		mPointerWidth = pointerWidth;
	}

	public void setPointerHeight(int pointerHeight) {
		mPointerHeight = pointerHeight;
	}

	// Private Methods
	// //////////////////////////////////////////////////////////

	private void initBubble() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mColor);
		mCornerRad = 0;
		setPointerWidth(30);
		setPointerHeight(20);
	}

	private void updatePointerPath() {
		mPointer = new Path();
		mPointer.setFillType(Path.FillType.EVEN_ODD);

		if (mPointerAlignment == LEFT) {
			// Set the starting point
			mPointer.moveTo(mPointerWidth, margintop);
			// Define the lines
			mPointer.rLineTo(0, mPointerWidth);
			mPointer.rLineTo(-mPointerHeight, -(mPointerWidth / 2));
			mPointer.rLineTo(mPointerHeight, -(mPointerWidth / 2));
		} else if (mPointerAlignment == RIGHT) {
			// Set the starting point
			mPointer.moveTo(mBoxWidth - mPointerWidth, margintop);

			// Define the lines
			mPointer.rLineTo(0, mPointerWidth);
			mPointer.rLineTo(mPointerHeight, -(mPointerWidth / 2));
			mPointer.rLineTo(-mPointerHeight, -(mPointerWidth / 2));
		}

		mPointer.close();
	}

	// Superclass Override Methods
	// //////////////////////////////////////////////////////////

	@Override
	public void draw(Canvas canvas) {
		if (mPointerAlignment == LEFT) {
			mBoxRect = new RectF(mPointerWidth, 0.0f, mBoxWidth, mBoxHeight);
		} else if (mPointerAlignment == RIGHT) {
			mBoxRect = new RectF(0.0f, 0.0f, mBoxWidth - mPointerWidth,
					mBoxHeight);
		}

		canvas.drawRoundRect(mBoxRect, mCornerRad, mCornerRad, mPaint);
		updatePointerPath();
//		 Paint p=new Paint();
//		 p.setAntiAlias(true);
//		 p.setColor(Color.parseColor("#123456"));
		canvas.drawPath(mPointer, mPaint);
	}

	@Override
	public int getOpacity() {
		return 255;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getPadding(Rect padding) {
		padding.set(mBoxPadding);

      if(mPointerAlignment==LEFT)
      // Adjust the padding to include the height of the pointer
      padding.left += mPointerHeight+10;
      else if(mPointerAlignment==RIGHT)
      	padding.right += mPointerHeight+10;
		return true;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		mBoxWidth = getBounds().width();
		mBoxHeight = bounds.height();
		super.onBoundsChange(bounds);
	}
}
