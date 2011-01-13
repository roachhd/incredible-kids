package com.incrediblekids.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class ItemSizeInfo {

	private static final String TAG = "ItemSizeInfo";
	
	private final Context m_Context;
	
	private int m_iItemHeight;
	private int m_iGapHeight;
	private int m_iLcdHeight;
	private int m_iLcdWidth;
	private int m_iMarginHeight;
	
	private int m_iItemCount;
	
	private float m_fItemScale;
	
	public static final int CAMERA_WIDTH 	= 800;
	public static final int CAMERA_HEIGHT 	= 480;

	public ItemSizeInfo(Context mContext, int mItemCount) {
		Log.d(TAG, "ItemSizeInfo()");
		m_Context = mContext;
		m_iItemCount = mItemCount;
		
//		measureLcdSize();
		measureItemSize();
		measureMarginHeight();
	}

	private void measureMarginHeight() {
		Log.d(TAG, "measureMarginHeight()");
		
		m_iMarginHeight = (CAMERA_HEIGHT - (m_iItemHeight + m_iGapHeight) * m_iItemCount - m_iGapHeight)/2;
	}

	@Deprecated
	private void measureLcdSize() {
		Log.d(TAG, "measureLcdSize()");
		DisplayMetrics displayMetrics = m_Context.getResources().getDisplayMetrics();
		m_iLcdHeight	= displayMetrics.heightPixels;
		m_iLcdWidth 	= displayMetrics.widthPixels;
	}

	private void measureItemSize() {
		Log.d(TAG, "measureItemSize()");
//		m_iItemHeight = m_iLcdHeight / m_iItemCount;
		m_iItemHeight = CAMERA_HEIGHT / m_iItemCount;
		if(CAMERA_HEIGHT % m_iItemCount == 0) {
			m_iItemHeight = CAMERA_HEIGHT / (m_iItemCount + 1);
		}
		m_iGapHeight = (CAMERA_HEIGHT - (m_iItemHeight * m_iItemCount)) / (m_iItemCount + 1);
	}

	public int getItemHeight() {
		return m_iItemHeight;
	}
	
	public int getGapHeight() {
		return m_iGapHeight;
	}
	
	public int getMarginHeight() {
		return m_iMarginHeight;
	}
	
	public int getLcdHeight() {
		return CAMERA_HEIGHT;
	}

	public int getLcdWidth() {
		return CAMERA_WIDTH;
	}
	
	public void setRealItemPixel(int realItemPixel) {
		Log.d(TAG, "measureItemScale()");
		try {
			m_fItemScale = ((float)m_iItemHeight / (float)realItemPixel);
		}
		catch (Exception e) {
			Log.e(TAG, "measureITemScale, error: " + e.getMessage());
		}
	}
	
	public float getMeasuredItemScale() {
		return m_fItemScale;
	}
}
