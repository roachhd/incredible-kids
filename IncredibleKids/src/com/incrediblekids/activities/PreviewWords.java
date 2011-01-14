package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PreviewWords extends Activity {
	private final String TAG="PreviewWords";
	private ImageButton m_ibLeftBtn, m_ibRightBtn;
	private ImageView m_ivQuizImg;
	private ImageView m_ivWordImg;
	private Gallery m_gPreviewImgGallery;
	private int m_iSelectedItem=0;
	private float m_fPosX=0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* Create Linear Layout View */
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setBackgroundColor(Color.WHITE);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear2 = (LinearLayout)inflater.inflate(R.layout.previewwords, null);
		linear.addView(linear2);
		
		/* Set View */
		setContentView(linear);
		
		/* Assign from Resource */
		m_ivQuizImg = (ImageView) findViewById(R.id.preview_center_image);
		m_ivWordImg = (ImageView) findViewById(R.id.preview_word_image);
		m_ibLeftBtn = (ImageButton) findViewById(R.id.preview_leftbtn);
		m_ibRightBtn = (ImageButton) findViewById(R.id.preview_rightbtn);
		
		m_gPreviewImgGallery = (Gallery) findViewById(R.id.preview_gallery);
		m_gPreviewImgGallery.setAdapter(new ImageAdapter(this));
		//m_gPreviewImgGallery.setCallbackDuringFling(false);
		m_gPreviewImgGallery.setFadingEdgeLength(100);
		
		/* Register Listener */
		m_gPreviewImgGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
				m_ivQuizImg.setImageResource(ImageAdapter.m_aImageIds[arg2]);
				m_iSelectedItem = arg2;
				m_ivWordImg.setVisibility(View.GONE);
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		m_ivQuizImg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_fPosX = event.getX();
					m_ivWordImg.setImageResource(R.drawable.cat_word);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if ((m_fPosX - event.getX()) < -50) { // Moving left
						if(m_iSelectedItem != 0)
							m_gPreviewImgGallery.setSelection(m_iSelectedItem-1);
					} else if ((m_fPosX - event.getX()) > 50) { // Moving right
						if(m_iSelectedItem != m_gPreviewImgGallery.getCount()-1)
							m_gPreviewImgGallery.setSelection(m_iSelectedItem+1);
					} else { // Click
						m_ivWordImg.setVisibility(View.VISIBLE);
					}
				}
				return true;
			}
		});
		
		m_ibLeftBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_iSelectedItem != 0)
					m_gPreviewImgGallery.setSelection(m_iSelectedItem-1);
			}
		});
		
		m_ibRightBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_iSelectedItem != m_gPreviewImgGallery.getCount()-1)
					m_gPreviewImgGallery.setSelection(m_iSelectedItem+1);
			}
		});	
	}
}

class ImageAdapter extends BaseAdapter {
	private Context m_cContext;
	private final String TAG="PreviewWords";
	public static int[] m_aImageIds = {
			R.drawable.e,
			R.drawable.f,
			R.drawable.g,
			R.drawable.a,
			R.drawable.b,
			R.drawable.c,
			R.drawable.d,
			R.drawable.e,
			R.drawable.f,
			R.drawable.g,
			R.drawable.a,
			R.drawable.b,
			R.drawable.c
	};

	public ImageAdapter(Context _context) {
		m_cContext = _context;
	}

	public int getCount() {
		Log.d(TAG, "getCount()");
		return m_aImageIds.length;
	}

	public Object getItem(int position) {
		Log.d(TAG, "getItem()");
		return m_aImageIds[position];
	}

	public long getItemId(int position) {
		Log.d(TAG, "getItemId()");
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView m_ivGallery;
		
		if (convertView == null) {
			m_ivGallery = new ImageView(m_cContext);
		} else {
			m_ivGallery = (ImageView)convertView;
		}
		
		m_ivGallery.setImageResource(m_aImageIds[position]);
		m_ivGallery.setScaleType(ImageView.ScaleType.FIT_XY);
		m_ivGallery.setLayoutParams(new Gallery.LayoutParams((int)parent.getWidth()/5, (int)parent.getWidth()/9));
		return m_ivGallery;
	}
}