package com.incrediblekids.activities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.incrediblekids.activities.ResourceClass.Item;
import com.incrediblekids.network.NetworkConnInfo;
import com.incrediblekids.util.Const;
import com.incrediblekids.util.PreviewGallery;

public class PreviewWords extends Activity implements ViewSwitcher.ViewFactory{
	private final String TAG="PreviewWords";
	
	private ImageAdapter m_iaPrevewImgAdapter;
	private ImageView m_ivLeftBtn, m_ivRightBtn;
	private ImageView m_ivWordImg, m_ivQuizImg;
	private ImageView m_ivPicViewBtn;
	private PreviewGallery m_pgPreviewImgGallery;
	private Vector<Item> m_ItemVector;
	private Vector<Bitmap> m_vLeftImg, m_vRightImg;
	private Timer m_tAnimationTimer;
	private WordImgAnimation m_WordImgAnimation;
	
	private int m_iSelectedItem=0;
	private float m_fPosX=0;
	private static int repeat=0;
	
	private static int[] IMAGE_SIZE={198, 169, 128, 96, 64};
	
	public ResourceClass res;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Get Resrouce from ResourceClass */ 
		res = ResourceClass.getInstance();
		m_ItemVector = res.getvItems();
		
		
		/* Create Linear Layout View */
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setBackgroundColor(Color.WHITE);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linear2 = (LinearLayout)inflater.inflate(R.layout.previewwords, null);
		linear.addView(linear2);
		
		
		/* Set Content View */
		setContentView(linear);
		
		
		/* Get Thumbnail Image Resource */
		m_vLeftImg = new Vector<Bitmap>();
		m_vRightImg = new Vector<Bitmap>();
		for(int i=0 ; i<5 ; ++i) {
			Log.d(TAG, "ItemVector = " + i);
			BitmapDrawable bd = (BitmapDrawable)getResources().getDrawable(m_ItemVector.get(i).iItemImgId);
			Bitmap bit = bd.getBitmap();
			m_vLeftImg.add(Bitmap.createBitmap(bit, 0, 0, 380, 256));
			m_vRightImg.add(Bitmap.createBitmap(bit, 380, 0, 380, 256));
		}
		ImageLoadingThread ilThread = new ImageLoadingThread();
		ilThread.start();

		
		/* Assign from Resource */
		m_ivQuizImg = (ImageView) findViewById(R.id.preview_center_image);
		m_ivWordImg = (ImageView) findViewById(R.id.preview_word_image);
		m_ivLeftBtn = (ImageView) findViewById(R.id.preview_leftbtn);
		m_ivRightBtn = (ImageView) findViewById(R.id.preview_rightbtn);
		m_ivPicViewBtn = (ImageView) findViewById(R.id.preview_picviewbtn);
	
		
		m_iaPrevewImgAdapter = new ImageAdapter(this);
		
		/* Preview Gallery Setting */
		m_pgPreviewImgGallery = (PreviewGallery) findViewById(R.id.preview_gallery);
		m_pgPreviewImgGallery.setAdapter(m_iaPrevewImgAdapter);
		m_pgPreviewImgGallery.setCallbackDuringFling(true);
		m_pgPreviewImgGallery.setFadingEdgeLength(100);
		m_pgPreviewImgGallery.setAnimationDuration(50);
		m_pgPreviewImgGallery.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
			public void onChildViewRemoved(View parent, View child) {}
			public void onChildViewAdded(View parent, View child) {
				child.invalidate();
			}
		});
		
		
		m_pgPreviewImgGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
				Log.d(TAG, "onItemSelected()");
				m_iSelectedItem = arg2;
				m_ivQuizImg.setImageBitmap(m_vLeftImg.get(m_iSelectedItem));
				m_ivWordImg.setVisibility(View.GONE);
				m_iaPrevewImgAdapter.notifyDataSetChanged();
				if (m_WordImgAnimation != null) {
					m_tAnimationTimer.cancel();
					m_tAnimationTimer = null;
					m_WordImgAnimation = null;
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {Log.d(TAG, "onNothingSelected()");}
		});
		
		
		/* Center(Quiz) Image Setting */
		m_ivQuizImg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_fPosX = event.getX();
					m_ivWordImg.setImageResource(m_ItemVector.get(m_iSelectedItem).iItemWordId);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if ((m_fPosX - event.getX()) < -50) { // Moving left
						if(m_iSelectedItem != 0)
							m_pgPreviewImgGallery.setSelection(m_iSelectedItem-1);
					} else if ((m_fPosX - event.getX()) > 50) { // Moving right
						if(m_iSelectedItem != m_pgPreviewImgGallery.getCount()-1)
							m_pgPreviewImgGallery.setSelection(m_iSelectedItem+1);
					} else { // Click
						if(!m_ivWordImg.isShown()) {
							Log.d(TAG, "Image Click");
							m_ivQuizImg.setImageBitmap(m_vRightImg.get(m_iSelectedItem));
							m_ivWordImg.setAnimation(AnimationUtils.loadAnimation(PreviewWords.this, android.R.anim.fade_in));
							m_ivWordImg.setVisibility(View.VISIBLE);
							
							m_WordImgAnimation = null;
							m_WordImgAnimation = new WordImgAnimation();
							m_tAnimationTimer = new Timer(false);
							m_tAnimationTimer.schedule(m_WordImgAnimation, 500, 500);
						}
					}
				}
				return true;
			}
		});
		
		
		/* Left Button Setting */
		m_ivLeftBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_iSelectedItem != 0)
					m_pgPreviewImgGallery.setSelection(m_iSelectedItem-1);
			}
		});
		
		
		/* Right Button Setting */
		m_ivRightBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_iSelectedItem != m_pgPreviewImgGallery.getCount()-1)
					m_pgPreviewImgGallery.setSelection(m_iSelectedItem+1);
			}
		});
		
		
		/* Picture View Button Setting */
		m_ivPicViewBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!NetworkConnInfo.IsWifiAvailable(PreviewWords.this) && !NetworkConnInfo.Is3GAvailable(PreviewWords.this))
				{
					Toast.makeText(PreviewWords.this, "네크워크에 연결할 수 없습니다.", Toast.LENGTH_LONG).show();
				}
				Intent intent = new Intent(PreviewWords.this, ItemPicActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra(Const.ITEM_NAME, m_ItemVector.get(m_iSelectedItem).strWordCharId);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		m_vLeftImg = null;
		m_vRightImg = null;
		if (m_tAnimationTimer != null)
			m_tAnimationTimer.purge();
		System.gc();
		super.onDestroy();
	}

	public View makeView() {
		ImageView i = new ImageView(this);
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

			if(convertView!=null){
				imageView = (ImageView)convertView;
			}else{
				imageView = new ImageView(m_cContext);
			}

			imageView.setImageBitmap(m_vLeftImg.get(position));			
			imageView.setAdjustViewBounds(false);

			if(position == m_iSelectedItem){
				imageView.setLayoutParams(new Gallery.LayoutParams(IMAGE_SIZE[0], IMAGE_SIZE[0]/2));
			}else if(Math.abs(position-m_iSelectedItem)==1 && m_iSelectedItem!=-1){
				imageView.setLayoutParams(new Gallery.LayoutParams(IMAGE_SIZE[1], IMAGE_SIZE[1]/2));
			}else if(Math.abs(position-m_iSelectedItem)>=2 && m_iSelectedItem!=-1){
				int size = 0;
				if(Math.abs(position-m_iSelectedItem) >= IMAGE_SIZE.length){
					size = IMAGE_SIZE[IMAGE_SIZE.length-1];
				}else{
					size = IMAGE_SIZE[Math.abs(position-m_iSelectedItem)];
				}
				imageView.setLayoutParams(new Gallery.LayoutParams(size, size/2));

			}else{
				imageView.setLayoutParams(new Gallery.LayoutParams(IMAGE_SIZE[0], IMAGE_SIZE[0]/2));
			}
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			return imageView;
		}
	}
	
	class WordImgAnimation extends TimerTask {
		WordImgAnimation () {
			repeat = 0;
		}

		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					if (m_ivWordImg.isShown()) {
						Log.d(TAG, "Running, repeat=" + ++repeat);
						if (repeat == 4) {
							m_ivWordImg.setAnimation(AnimationUtils.loadAnimation(PreviewWords.this, android.R.anim.fade_out));
							m_ivWordImg.setVisibility(View.GONE);
						}
						else if (repeat%2 == 1)
							m_ivQuizImg.setImageBitmap(m_vLeftImg.get(m_iSelectedItem));
						else
							m_ivQuizImg.setImageBitmap(m_vRightImg.get(m_iSelectedItem));
					}
				}
			});
			if (repeat == 4) {		
				this.cancel();
			}
		}
	}
	
	class ImageLoadingThread extends Thread {
		public void run() {
			for(int i=5 ; i<m_ItemVector.size() ; ++i) {
				Log.d(TAG, "ItemVector = " + i);
				BitmapDrawable bd = (BitmapDrawable)getResources().getDrawable(m_ItemVector.get(i).iItemImgId);
				Bitmap bit = bd.getBitmap();
				m_vLeftImg.add(Bitmap.createBitmap(bit, 0, 0, 380, 256));
				m_vRightImg.add(Bitmap.createBitmap(bit, 380, 0, 380, 256));
			}
			this.stop();
		}
	}
}




