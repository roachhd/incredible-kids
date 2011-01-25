package com.incrediblekids.util;

import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

import com.incrediblekids.activities.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PointSprite extends Sprite {
	
	private ITouchArea m_Listener;
	private final String TAG = "PointSprite";
	private ImageLineView m_ImageLine;
	private int m_iCollide;
	private boolean m_bIsCollide;
	
	public PointSprite(float pX, float pY, float pWidth, float pHeight, TextureRegion pTextureRegion, RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pRectangleVertexBuffer);
	}

	public PointSprite(float pX, float pY, TextureRegion pTextureRegion, RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pTextureRegion, pRectangleVertexBuffer);
	}

	public PointSprite(float pX, float pY, TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}
	
	public PointSprite(float pX, float pY, TextureRegion pTextureRegion, Context pContext) {
		super(pX, pY, pTextureRegion);
		initView(pContext);
	}

	public PointSprite(float pX, float pY, float pWidth, float pHeight, TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
	}
	
	public void setOnAreaTouchListener(ITouchArea l) {
		m_Listener = l;
	}
	
	public void initView(Context _context) {
		Log.d(TAG, "initView()");
		m_ImageLine = new ImageLineView(_context);
	}
	
	public ImageLineView getImageLine() {
		return m_ImageLine;
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
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		Log.d(TAG, "onAreaTouched()");
		return m_Listener.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
	
	public class ImageLineView extends View {

		private float m_fLineStartX;
		private float m_fLineStartY;
		private float m_fLineLastX;
		private float m_fLineLastY;
		private boolean m_bFlag = false;
		
		public ImageLineView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			Log.d(TAG, "onDraw()");
			Paint Pnt = new Paint();
			Pnt.setAntiAlias(true);
			
			Bitmap point = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.red_dot);
			
			Pnt.setShader(new BitmapShader(point, TileMode.MIRROR, TileMode.REPEAT));
			Pnt.setStrokeWidth(5);
			
			canvas.drawLine(m_fLineStartX, m_fLineStartY, m_fLineLastX, m_fLineLastY, Pnt);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			Log.d(TAG, "onTouchEvent()");
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				m_fLineLastX = event.getX();
				m_fLineLastY = event.getY();
				invalidate();
				
				if(!m_bFlag) {
					m_fLineStartX = PointSprite.this.getX() + PointSprite.this.getWidth()/2;
					m_fLineStartY = PointSprite.this.getY() + PointSprite.this.getHeight()/2;
					m_bFlag = true;
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP) {
				Log.d(TAG, "ACTION_UP");
				if(isCollide()) {
					m_fLineLastX = event.getX();
					m_fLineLastY = event.getY();
				}
				else {
					m_fLineLastX = m_fLineStartX;
					m_fLineLastY = m_fLineStartY;
				}
				Log.d(TAG, "m_fLineLastX: " + m_fLineLastX);
				Log.d(TAG, "m_fLineLastY: " + m_fLineLastY);
				invalidate();
			}
			return false;
		}
	}
	
	
}
