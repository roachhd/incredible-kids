package com.incrediblekids.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class ItemSizeInfo {

	private static final String TAG = "ItemSizeInfo";
	
	private final Context m_Context;
	
	private static int m_iItemHeight;
	private static int m_iGapHeight;
	private static int m_iLcdHeight;
	private static int m_iLcdWidth;
	private static int m_iMarginHeight;
	
	private int m_iItemCount;
	
	private float m_fItemScale;

	public ItemSizeInfo(Context mContext, int mItemCount) {
		Log.d(TAG, "ItemSizeInfo()");
		m_Context = mContext;
		m_iItemCount = mItemCount;
		
		measureLcdSize();
		measureItemSize();
		measureMarginHeight();
	}

	private void measureMarginHeight() {
		Log.d(TAG, "measureMarginHeight()");
		
		m_iMarginHeight = (m_iLcdHeight - (m_iItemHeight + m_iGapHeight) * m_iItemCount - m_iGapHeight)/2;
	}

	private void measureLcdSize() {
		Log.d(TAG, "measureLcdSize()");
		DisplayMetrics displayMetrics = m_Context.getResources().getDisplayMetrics();
		m_iLcdHeight	= displayMetrics.heightPixels;
		m_iLcdWidth 	= displayMetrics.widthPixels;
	}

	private void measureItemSize() {
		Log.d(TAG, "measureItemSize()");
		m_iItemHeight = m_iLcdHeight / m_iItemCount;
		if(m_iLcdHeight % m_iItemCount == 0) {
			m_iItemHeight = m_iLcdHeight / (m_iItemCount + 1);
		}
		m_iGapHeight = (m_iLcdHeight - (m_iItemHeight * m_iItemCount)) / (m_iItemCount + 1);
	}

	public static int getItemHeight() {
		return m_iItemHeight;
	}
	
	public static int getGapHeight() {
		return m_iGapHeight;
	}
	
	public static int getMarginHeight() {
		return m_iMarginHeight;
	}
	
	public static int getLcdHeight() {
		return m_iLcdHeight;
	}

	public static int getLcdWidth() {
		return m_iLcdWidth;
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
