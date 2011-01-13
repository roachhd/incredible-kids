package com.incrediblekids.util;

import org.anddev.andengine.collision.RectangularShapeCollisionChecker;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.shape.RectangularShape;

import android.os.Handler;
import android.util.Log;

public class QuizLine extends Line {
	private final String TAG = "QuizLine";
	private int m_iCollide;
	private boolean m_bIsCollide;
	private Handler m_handler = new Handler();

	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	
	public QuizLine(float pX1, float pY1, float pX2, float pY2) {
		super(pX1, pY1, pX2, pY2);
		m_iCollide = FAIL;
		m_bIsCollide = false;
	}

	public QuizLine(float pX1, float pY1, float pX2, float pY2, float pLineWidth) {
		super(pX1, pY1, pX2, pY2, pLineWidth);
		m_iCollide = FAIL;
		m_bIsCollide = false;
	}
	
	public boolean collidesWithAnObject(final Object pObject) {
		if(pObject instanceof RectangularShape) {
			
			final RectangularShape pOtherRectangularShape = (RectangularShape) pObject;
			final Rectangle pRectangle = new Rectangle(this.getX2(), this.getY2(), 8, 8);
			
			return RectangularShapeCollisionChecker.checkCollision(pRectangle, pOtherRectangularShape);
		} else {
			return false;
		}
	}
	
	public void drawLine(final float pX1, final float pY1, final float pX2, final float pY2) {
		Log.d(TAG, "drawLine()");
		float baseWidth = (pX2 - pX1);
		float altitude = (pY2 - pY1);
		final float slope = altitude / baseWidth;
		
		// TODO: Thread
		/*
		for(float i = pX1; i < pX2; i = i + 0.01f) {
			setPosition(pX1, pY1, i, pY1 + (i - pX1) * slope);
		}
		*/
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				m_handler.post(new Runnable() {
					@Override
					public void run() {
						for(float i = pX1; i < pX2; i = i + 0.02f) {
							setPosition(pX1, pY1, i, pY1 + (i - pX1) * slope);
						}
					}
				});
			}
		}).start();
	}

	public int getCollideState() {
		return m_iCollide;
	}

	public void setCollideState(int iCollide) {
		this.m_iCollide = iCollide;
	}
	
	public boolean isCollide() {
		return m_bIsCollide;
	}

	public void setCollide(boolean mBIsCollide) {
		m_bIsCollide = mBIsCollide;
	}
}
