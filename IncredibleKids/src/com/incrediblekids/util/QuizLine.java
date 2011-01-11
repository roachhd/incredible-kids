package com.incrediblekids.util;

import org.anddev.andengine.collision.RectangularShapeCollisionChecker;
import org.anddev.andengine.entity.primitive.BaseRectangle;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.shape.RectangularShape;

import android.util.Log;

public class QuizLine extends Line {
	
	private final String TAG = "QuizLine";

	public QuizLine(float pX1, float pY1, float pX2, float pY2) {
		super(pX1, pY1, pX2, pY2);
	}

	public QuizLine(float pX1, float pY1, float pX2, float pY2,
			float pLineWidth) {
		super(pX1, pY1, pX2, pY2, pLineWidth);
	}
	
	public boolean collidesWithAnObject(final BaseRectangle pObject) {
		if(pObject instanceof RectangularShape) {
			
			final RectangularShape pOtherRectangularShape = (RectangularShape) pObject;
			final Rectangle pRectangle = new Rectangle(this.getX2(), this.getY2(), 2, 2);
					
			Log.d(TAG, "pRectangle x: " + pRectangle.getX());
			Log.d(TAG, "pRectangle y: " + pRectangle.getY());
			Log.d(TAG, "pRectangle width: " + pRectangle.getWidth());
			Log.d(TAG, "pRectangle height: " + pRectangle.getHeight());
			Log.d(TAG, "pRectangle scaled: " + pRectangle.isScaled());
			
			Log.d(TAG, "pOtherRectangularShape x: " + pOtherRectangularShape.getX());
			Log.d(TAG, "pOtherRectangularShape y: " + pOtherRectangularShape.getY());
			Log.d(TAG, "pOtherRectangularShape width: " + pOtherRectangularShape.getWidth());
			Log.d(TAG, "pOtherRectangularShape height: " + pOtherRectangularShape.getHeight());
			Log.d(TAG, "pOtherRectangularShape scaled: " + pOtherRectangularShape.isScaled());

			return RectangularShapeCollisionChecker.checkCollision(pRectangle, pOtherRectangularShape);
		} else {
			return false;
		}
	}
}
