package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
	
	public ResourceClass res;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		res = ResourceClass.getInstance();
		
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
				m_ivQuizImg.setImageResource(res.vItems.get(arg2).iItemImgId);
				m_iSelectedItem = arg2;
				m_ivWordImg.setVisibility(View.GONE);
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		m_ivQuizImg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_fPosX = event.getX();
					m_ivWordImg.setImageResource(res.vItems.get(m_iSelectedItem).iWordImgId);
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
	private final String TAG="PreviewWords";
	
	private Context m_cContext;
		
	public ResourceClass res;

	public ImageAdapter(Context _context) {
		m_cContext = _context;
		res = ResourceClass.getInstance();
	}
	
	public int getCount() {
		return res.vItems.size();
	}

	public Object getItem(int position) {
		return res.vItems.get(position).iItemImgId;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView m_ivGallery;
		Log.d(TAG, "width = " + parent.getWidth());
		if (convertView == null) {
			m_ivGallery = new ImageView(m_cContext);
		} else {
			m_ivGallery = (ImageView)convertView;
		}
		
		m_ivGallery.setImageResource(res.vItems.get(position).iItemImgId);
		m_ivGallery.setScaleType(ImageView.ScaleType.FIT_XY);
		m_ivGallery.setLayoutParams(new Gallery.LayoutParams(196, 98));
		return m_ivGallery;
	}
}