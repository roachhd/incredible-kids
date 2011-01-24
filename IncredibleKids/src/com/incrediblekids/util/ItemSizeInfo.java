package com.incrediblekids.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class ItemSizeInfo {

	private static final String TAG = "ItemSizeInfo";
	
	private final Context m_Context;
	
	private float m_iItemHeight;
	private float m_iGapHeight;
	private float m_iLcdHeight;
	private float m_iLcdWidth;
	private float m_iMarginHeight;
	
	private int m_iItemCount;
	
	private float m_fItemScale;
	
	public static float CAMERA_WIDTH 	= 0;
	public static float CAMERA_HEIGHT = 0;
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
		
		Log.d(TAG, "getItemHeight: " + m_iItemHeight);
		Log.d(TAG, "getGapHeight: " + m_iGapHeight);
		Log.d(TAG, "CAMERA_HEIGHT: " + CAMERA_HEIGHT);
		Log.d(TAG, "m_iItemCount: " + m_iItemCount);
		m_iMarginHeight = (CAMERA_HEIGHT - (m_iItemHeight + m_iGapHeight) * m_iItemCount - m_iGapHeight)/2;
		Log.d(TAG, "a: " + (CAMERA_HEIGHT - (m_iItemHeight + m_iGapHeight) * m_iItemCount));
		Log.d(TAG, "m_iMarginHeight: " + m_iMarginHeight);
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
		Log.d(TAG, "m_iITemHeight :" + m_iItemHeight);
		
		if(CAMERA_HEIGHT % m_iItemCount == 0) {
			m_iItemHeight = CAMERA_HEIGHT / (m_iItemCount + 1);
		}
		
		m_iItemHeight -= 20 * DP_SCALE_Y * DENSITY;
		
		m_iGapHeight = (CAMERA_HEIGHT - (m_iItemHeight * m_iItemCount)) / (m_iItemCount + 1);
	}

	public float getItemHeight() {
		Log.d(TAG, "getItemHeight: " + m_iItemHeight);
		return m_iItemHeight;
	}
	
	public float getGapHeight() {
		Log.d(TAG, "getGapHeight: " + m_iGapHeight);
		return m_iGapHeight;
	}
	
	public float getMarginHeight() {
		Log.d(TAG, "getMarginHeight: " + m_iMarginHeight);
		return m_iMarginHeight;
	}
	
	public float getLcdHeight() {
		return m_iLcdHeight;
	}

	public float getLcdWidth() {
		return m_iLcdWidth;
	}
	
	public void setRealItemPixel(int realItemPixel) {
		Log.d(TAG, "measureItemScale()");
		Log.d(TAG, "realItemPixel : " + realItemPixel);
		Log.d(TAG, "m_iItemHeight : " + m_iItemHeight);
		try {
			m_fItemScale = ((float)m_iItemHeight / (float)realItemPixel);
			Log.d(TAG, "m_fItemScale : " + m_fItemScale);
			
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
