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
	
	public static int CAMERA_WIDTH 	= 0;
	public static int CAMERA_HEIGHT = 0;
	public static int DENSITY_DPI = 0;
	public static float DENSITY = 0f;
	
	private final float DEFAULT_DP_X = 480f;
	private final float DEFAULT_DP_Y = 320f;
	public static float DP_SCALE_X = 0f;
	public static float DP_SCALE_Y = 0f;

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
		
		m_iMarginHeight = (CAMERA_HEIGHT - (m_iItemHeight + m_iGapHeight) * m_iItemCount - m_iGapHeight)/2;
	}

	private void measureLcdSize() {
		Log.d(TAG, "measureLcdSize()");
		DisplayMetrics displayMetrics = m_Context.getResources().getDisplayMetrics();
		m_iLcdHeight	= displayMetrics.heightPixels;
		m_iLcdWidth 	= displayMetrics.widthPixels;
		
		CAMERA_HEIGHT = m_iLcdHeight;
		CAMERA_WIDTH  = m_iLcdWidth;
		
		DENSITY_DPI = displayMetrics.densityDpi;
		DENSITY 	= displayMetrics.density;
		Log.d(TAG, "densityDpi : " + DENSITY_DPI);
		Log.d(TAG, "density : " + DENSITY);
		Log.d(TAG, "WIDTH : " + CAMERA_WIDTH);
		Log.d(TAG, "HEIGHT : " + CAMERA_HEIGHT);
		
		DP_SCALE_X = ((CAMERA_WIDTH * 160) / DENSITY_DPI) / DEFAULT_DP_X;
		DP_SCALE_Y = ((CAMERA_HEIGHT * 160) / DENSITY_DPI) / DEFAULT_DP_Y;
		Log.d(TAG, "dp_scaleX: " + DP_SCALE_X);
		Log.d(TAG, "dp_scaleY: " + DP_SCALE_Y);
		
	}

	private void measureItemSize() {
		Log.d(TAG, "measureItemSize()");
//		m_iItemHeight = m_iLcdHeight / m_iItemCount;
		m_iItemHeight = CAMERA_HEIGHT / m_iItemCount;
		m_iItemHeight -= 20 * DP_SCALE_Y * DENSITY;
		Log.d(TAG, "m_iITemHeight :" + m_iItemHeight);
		
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
		return m_iLcdHeight;
	}

	public int getLcdWidth() {
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
	
	public static float getDP_X(float dp) {
		return dp * DP_SCALE_X;
	}
	
	public static float getDP_Y(float dp) {
		return dp * DP_SCALE_Y;
	}
}
