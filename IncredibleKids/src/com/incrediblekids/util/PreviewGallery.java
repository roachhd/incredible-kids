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
	public PreviewGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		velocityX *= 0.3;
		return super.onFling(e1, e2, velocityX, velocityY);
	}
}
