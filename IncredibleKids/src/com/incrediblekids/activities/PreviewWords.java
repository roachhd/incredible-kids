package com.incrediblekids.activities;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.incrediblekids.activities.ResourceClass.Item;
import com.incrediblekids.util.PreviewGallery;

public class PreviewWords extends Activity implements ViewSwitcher.ViewFactory{
	private final String TAG="PreviewWords";
	
	private ImageAdapter m_iaPrevewImgAdapter;
	private ImageView m_ibLeftBtn, m_ibRightBtn;
	private ImageSwitcher m_ivQuizImg;
	private ImageView m_ivWordImg;
	private PreviewGallery m_pgPreviewImgGallery;
	private Vector<Item> m_ItemVector;
	private int m_iSelectedItem=0;
	private float m_fPosX=0;
	
	private static int[] IMAGE_SIZE={198, 169, 128, 96, 64};
	//int mSelectedPosition = -1;
	
	public ResourceClass res;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		res = ResourceClass.getInstance();
		m_ItemVector = res.getvItems();
		
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
		m_ivQuizImg = (ImageSwitcher) findViewById(R.id.preview_center_image);
		m_ivWordImg = (ImageView) findViewById(R.id.preview_word_image);
		m_ibLeftBtn = (ImageView) findViewById(R.id.preview_leftbtn);
		m_ibRightBtn = (ImageView) findViewById(R.id.preview_rightbtn);
		
		m_iaPrevewImgAdapter = new ImageAdapter(this);
		
		m_pgPreviewImgGallery = (PreviewGallery) findViewById(R.id.preview_gallery);
		m_pgPreviewImgGallery.setAdapter(m_iaPrevewImgAdapter);
		m_pgPreviewImgGallery.setCallbackDuringFling(true);
		m_pgPreviewImgGallery.setFadingEdgeLength(100);
		m_pgPreviewImgGallery.setAnimationDuration(100);
		m_pgPreviewImgGallery.setSpacing(10);
		m_pgPreviewImgGallery.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
			public void onChildViewRemoved(View parent, View child) {}
			public void onChildViewAdded(View parent, View child) {
				child.invalidate();
			}
		});
		/* Register Listener */
		m_pgPreviewImgGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
				m_ivQuizImg.setImageResource(m_ItemVector.get(arg2).iItemImgId);
				m_iSelectedItem = arg2;
				m_ivWordImg.setVisibility(View.GONE);
				//mSelectedPosition = arg2;
				m_iaPrevewImgAdapter.notifyDataSetChanged();
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		m_ivQuizImg.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		//m_ivQuizImg.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		m_ivQuizImg.setFactory(this);
		m_ivQuizImg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_fPosX = event.getX();
					m_ivWordImg.setImageResource(m_ItemVector.get(m_iSelectedItem).iWordImgId);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if ((m_fPosX - event.getX()) < -50) { // Moving left
						if(m_iSelectedItem != 0)
							m_pgPreviewImgGallery.setSelection(m_iSelectedItem-1);
					} else if ((m_fPosX - event.getX()) > 50) { // Moving right
						if(m_iSelectedItem != m_pgPreviewImgGallery.getCount()-1)
							m_pgPreviewImgGallery.setSelection(m_iSelectedItem+1);
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
					m_pgPreviewImgGallery.setSelection(m_iSelectedItem-1);
			}
		});
		
		m_ibRightBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_iSelectedItem != m_pgPreviewImgGallery.getCount()-1)
					m_pgPreviewImgGallery.setSelection(m_iSelectedItem+1);
			}
		});	
	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
//      i.setBackgroundColor(0xFF000000);
		i.setBackgroundColor(0x00000000);
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return i;
	}
	
	class ImageAdapter extends BaseAdapter {
		public ResourceClass res;

		private final String TAG="PreviewWords";
		private Vector<Item> m_ItemVector;
		private Context m_cContext;

		public ImageAdapter(Context _context) {
			m_cContext = _context;
			res = ResourceClass.getInstance();
			m_ItemVector = res.getvItems();
		}

		public int getCount() {
			return m_ItemVector.size();
		}

		public Object getItem(int position) {
			return m_ItemVector.get(position).iItemImgId;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;

			Log.d("PreviewWords", "position = " + position);
			if(convertView!=null){
				imageView = (ImageView)convertView;
			}else{
				imageView = new ImageView(m_cContext);
			}

			imageView.setImageResource(m_ItemVector.get(position).iItemImgId);
			imageView.setAdjustViewBounds(false);

			if(position == m_iSelectedItem){
				Log.d(TAG, "getView 196");
				imageView.setLayoutParams(new Gallery.LayoutParams(IMAGE_SIZE[0], IMAGE_SIZE[0]/2));
			}else if(Math.abs(position-m_iSelectedItem)==1 && m_iSelectedItem!=-1){
				Log.d(TAG, "getView 128");
				imageView.setLayoutParams(new Gallery.LayoutParams(IMAGE_SIZE[1], IMAGE_SIZE[1]/2));
			}else if(Math.abs(position-m_iSelectedItem)>=2 && m_iSelectedItem!=-1){
				Log.d(TAG, "getView from IMAGE_SIZE");
				int size = 0;
				if(Math.abs(position-m_iSelectedItem) >= IMAGE_SIZE.length){
					size = IMAGE_SIZE[IMAGE_SIZE.length-1];
				}else{
					size = IMAGE_SIZE[Math.abs(position-m_iSelectedItem)];
				}
				imageView.setLayoutParams(new Gallery.LayoutParams(size, size/2));

			}else{
				Log.d(TAG, "getView else");
				imageView.setLayoutParams(new Gallery.LayoutParams(IMAGE_SIZE[0], IMAGE_SIZE[0]/2));

			}

			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


			return imageView;
		}
	}
}




