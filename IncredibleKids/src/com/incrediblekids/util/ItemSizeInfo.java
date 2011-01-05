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
	
	private final int ITEM_COUNT = 5;
	
	private float m_fItemScale;

	public ItemSizeInfo(Context mContext) {
		Log.d(TAG, "ItemSizeInfo()");
		m_Context = mContext;
		
		measureLcdSize();
		measureItemSize();
	}

	private void measureLcdSize() {
		Log.d(TAG, "measureLcdSize()");
		DisplayMetrics displayMetrics = m_Context.getResources().getDisplayMetrics();
		m_iLcdHeight	= displayMetrics.heightPixels;
		m_iLcdWidth 	= displayMetrics.widthPixels;
	}

	private void measureItemSize() {
		Log.d(TAG, "measureItemSize()");
		m_iItemHeight = m_iLcdHeight / ITEM_COUNT;
		if(m_iLcdHeight % ITEM_COUNT == 0) {
			m_iItemHeight = m_iLcdHeight / (ITEM_COUNT + 1);
		}
		m_iGapHeight = (m_iLcdHeight - (m_iItemHeight * ITEM_COUNT)) / (ITEM_COUNT + 1);
	}

	public static int getItemHeight() {
		return m_iItemHeight;
	}

	public static int getGapHeight() {
		return m_iGapHeight;
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
