package com.incrediblekids.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class PreviewGallery extends Gallery{
	
	protected boolean checkLayoutParams(LayoutParams p) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "checkLayoutParams");
		return super.checkLayoutParams(p);
	}

	@Override
	protected int computeHorizontalScrollExtent() {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "computeHorizontalScrollExtent");
		return super.computeHorizontalScrollExtent();
	}

	@Override
	protected int computeHorizontalScrollOffset() {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "computeHorizontalScrollOffset");
		return super.computeHorizontalScrollOffset();
	}

	@Override
	protected int computeHorizontalScrollRange() {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "computeHorizontalScrollRange");
		return super.computeHorizontalScrollRange();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "dispatchKeyEvent");
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void dispatchSetPressed(boolean pressed) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "dispatchSetPressed");
		super.dispatchSetPressed(pressed);
	}

	@Override
	public void dispatchSetSelected(boolean selected) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "dispatchSetSelected");
		super.dispatchSetSelected(selected);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "getChildDrawingOrder");
		return super.getChildDrawingOrder(childCount, i);
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "getChildStaticTransformation");
		return super.getChildStaticTransformation(child, t);
	}

	@Override
	protected ContextMenuInfo getContextMenuInfo() {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "getContextMenuInfo");
		return super.getContextMenuInfo();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onDown");
		return super.onDown(e);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onFocusChanged");
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onKeyDown");
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onKeyUp");
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onLayout");
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onLongPress");
		super.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onScroll");
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onShowPress");
		super.onShowPress(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onSingleTapUp");
		return super.onSingleTapUp(e);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("PreviewGallery", "onTouchEvent");
		return super.onTouchEvent(event);
	}
	
	public PreviewGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		velocityX *= 0.5;
		return super.onFling(e1, e2, velocityX, velocityY);
	}
}
