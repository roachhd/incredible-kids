package com.incrediblekids.activities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.incrediblekids.network.NetworkConnInfo;
import com.incrediblekids.util.ImageManager;
import com.incrediblekids.util.Item;
import com.incrediblekids.util.PreviewGallery;

public class PreviewWords extends Activity implements ViewSwitcher.ViewFactory{
	/********************************
	 * Constants
	 ********************************/
	private final boolean DEBUG = true;
	private final String TAG="PreviewWords";
	private final String FLICKR_KEY = "edce333ce82dfc77c47fce4bfb7a2803";
	private final String FLICKR_SEC = "e48bbb5063ef5943";
	private final String FLICKR_UID = "24882827@N04";
	
	/********************************
	 * Member Variables
	 ********************************/
	private ImageAdapter m_PreviewImgAdapter;
	private ImageView m_LeftBtn, m_RightBtn;
	private ImageView m_WordImg, m_QuizImg;
	private ImageView m_PictureViewerBtn, m_PictureImg;
	
	private Vector<Item> m_ItemVector;
	private Vector<Bitmap> m_LeftImgVector, m_RightImgVector;
	
	private PreviewGallery m_PreviewImgGallery;
	private WordImgAnimation m_WordImgAnimation;
	private Timer m_QuizImgAnimationTimer;
	
	private LinearLayout m_PictureViewerLayout;
	private View m_PictureViewer;
	private PopupWindow m_PictureViewerPopupWindow;
	
	private ImageLoadingThread m_ImgLoadingThread;
	private PhotoLoadingThread m_PhotoLoadingThread;
	
	private Handler m_Handler;
	private int m_iSelectedItem=0, m_iPicture=0;
	private float m_fPosX=0;
	private static int repeat=0;
	
	private File m_FileDirectory;
	
	private int[] IMAGE_SIZE=null; 
			
	private MediaPlayer m_QuizBGM;
	
	private SoundPool m_SoundEffect = null;
	private int	m_SoundEffectId[] = null;
	
	public ResourceClass res;
	
	/* Test Frame Animation */
	private ImageView m_FrameAnim;
	private AnimationDrawable m_PictureViewerAnim;
	
	/* Test Flickr */
	private Bitmap m_aBitmap[] = null;
	private ArrayList <String> m_ImageUrlArr = null;
	
	private ProgressBar m_PhotoLoadingProgressBar;
	
	/********************************
	 * onCreate
	 ********************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* Get resrouce from ResourceClass */ 
		res = ResourceClass.getInstance();
		m_ItemVector = res.getvItems();
		
		/* Set content view */
		setContentView(R.layout.preview_words);

		/* Get Display Size */
		int mHeight = getWindowManager().getDefaultDisplay().getHeight();
		int mWidth = getWindowManager().getDefaultDisplay().getWidth();
		if (mWidth >= 800) { // HDPI
			IMAGE_SIZE = new int[]{209, 171, 133, 95, 76, 57};
		} else // MDPI
			IMAGE_SIZE = new int[]{136, 102, 85, 68, 51, 34};
		
		
		/* Get Thumbnail Image Resource */
		m_LeftImgVector = new Vector<Bitmap>();
		m_RightImgVector = new Vector<Bitmap>();
		for(int i=0 ; i<5 ; ++i) {
			if (DEBUG) Log.d(TAG, "ItemVector = " + i);
			BitmapDrawable bd = (BitmapDrawable)getResources().getDrawable(m_ItemVector.get(i).iItemImgId);
			Bitmap bit = bd.getBitmap();
			Log.d(TAG, "bit.width = " + bit.getWidth() + " bit.height = " + bit.getHeight());
			m_LeftImgVector.add(Bitmap.createBitmap(bit, 0, 0, bit.getWidth()/2, bit.getHeight()));
			m_RightImgVector.add(Bitmap.createBitmap(bit, bit.getWidth()/2, 0, bit.getWidth()/2, bit.getHeight()));
		}
		
		m_QuizBGM	= MediaPlayer.create(this, R.raw.quizbgm);
		m_QuizBGM.setLooping(true);
		
		m_SoundEffect = new SoundPool(m_ItemVector.size(), AudioManager.STREAM_MUSIC, 0);
		m_SoundEffectId = new int[20];
		
		/* Get Thumbnail Image resource by thread */
		m_ImgLoadingThread = new ImageLoadingThread();
		m_ImgLoadingThread.start();
		
		/* Create Handle to receive Image loading complete */
		m_Handler = new Handler() {
			public void handleMessage(Message msg) {
				Log.d(TAG, "handlerMeesge = " + msg.what);
				m_PhotoLoadingProgressBar.setVisibility(View.INVISIBLE);
				m_FrameAnim.setVisibility(View.VISIBLE);
				m_PictureImg.setImageBitmap(m_aBitmap[0]);
			}
		};
		
		/* Create Photo Viewer Layout */
		m_PictureViewerLayout = (LinearLayout)findViewById(R.id.linear);
		m_PictureViewer = View.inflate(this, R.layout.picture_viewer, null);
		//m_PictureViewer = new PictureViewer(getApplicationContext());
		m_PictureViewerPopupWindow = new PopupWindow(m_PictureViewer, mWidth/2, mHeight/2, true);
		m_PhotoLoadingProgressBar = (ProgressBar)m_PictureViewer.findViewById(R.id.photo_loading_progress);
		
		/* Assign from Resource */
		m_QuizImg = (ImageView) findViewById(R.id.preview_center_image);
		m_WordImg = (ImageView) findViewById(R.id.preview_word_image);
		m_LeftBtn = (ImageView) findViewById(R.id.preview_leftbtn);
		m_RightBtn = (ImageView) findViewById(R.id.preview_rightbtn);
		m_PictureImg = (ImageView) m_PictureViewer.findViewById(R.id.picture);
		m_PictureViewerBtn = (ImageView) findViewById(R.id.preview_picviewbtn);
		
		
		/* Test Frame Animation */
		m_FrameAnim = (ImageView)m_PictureViewer.findViewById(R.id.picview_rightani);
		m_FrameAnim.setBackgroundResource(R.drawable.arrow_right);
		m_PictureViewerAnim = (AnimationDrawable)m_FrameAnim.getBackground();
		m_FrameAnim.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (m_PictureViewerAnim.isRunning()) {
						Log.d(TAG, "Running ~~~~");
						m_PictureViewerAnim.stop();
					} else {
						Log.d(TAG, "Not Running ~~~~");
						m_PictureViewerAnim.start();
					}
				}
				return true;
			}
		});
		
		
		/* Setting Picture Image */
		m_PictureImg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (DEBUG) Log.d(TAG, "Picture image is touched, event = " + event.getAction());
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_fPosX = event.getX();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if ((m_fPosX - event.getX()) < -50) { // Moving left
						if (m_iPicture == 0)
							m_iPicture = m_aBitmap.length;
						m_PictureImg.setImageBitmap(m_aBitmap[--m_iPicture]);
					} else if ((m_fPosX - event.getX()) > 50) { // Moving right
						if (m_iPicture == m_aBitmap.length-1)
							m_iPicture = -1;
						m_PictureImg.setImageBitmap(m_aBitmap[++m_iPicture]);
					} else { // Click
						m_PictureViewerPopupWindow.dismiss();
					}
				}
				return true;
			}
		});
		
		
		/* Setting Preview Image Gallery */
		m_PreviewImgAdapter = new ImageAdapter(this);
		m_PreviewImgGallery = (PreviewGallery) findViewById(R.id.preview_gallery);
		m_PreviewImgGallery.setAdapter(m_PreviewImgAdapter);
		m_PreviewImgGallery.setCallbackDuringFling(true);
		m_PreviewImgGallery.setFadingEdgeLength(100);
		m_PreviewImgGallery.setAnimationDuration(50);
		m_PreviewImgGallery.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
			public void onChildViewRemoved(View parent, View child) {}
			public void onChildViewAdded(View parent, View child) {
				child.invalidate();
			}
		});
		m_PreviewImgGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
				if (DEBUG) Log.d(TAG, "onItemSelected()");
				m_iSelectedItem = arg2;
				m_QuizImg.setImageBitmap(m_LeftImgVector.get(m_iSelectedItem));
				m_WordImg.setVisibility(View.GONE);
				m_PreviewImgAdapter.notifyDataSetChanged();
				if (m_WordImgAnimation != null) {
					m_QuizImgAnimationTimer.cancel();
					m_QuizImgAnimationTimer = null;
					m_WordImgAnimation = null;
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {if (DEBUG) Log.d(TAG, "onNothingSelected()");}
		});
		
		
		/* Setting Quiz(Center) Image */
		m_QuizImg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_fPosX = event.getX();
					m_WordImg.setImageResource(m_ItemVector.get(m_iSelectedItem).iItemWordId);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if ((m_fPosX - event.getX()) < -50) { // Moving left
						if(m_iSelectedItem != 0)
							m_PreviewImgGallery.setSelection(m_iSelectedItem-1);
					} else if ((m_fPosX - event.getX()) > 50) { // Moving right
						if(m_iSelectedItem != m_PreviewImgGallery.getCount()-1)
							m_PreviewImgGallery.setSelection(m_iSelectedItem+1);
					} else { // Click
						if(!m_WordImg.isShown()) {
							if (DEBUG) Log.d(TAG, "Image Click");
							m_SoundEffect.play(m_SoundEffectId[m_iSelectedItem], 1.0f, 1.0f, 0, 0, 1.0f);
							m_QuizImg.setImageBitmap(m_RightImgVector.get(m_iSelectedItem));
							m_WordImg.setAnimation(AnimationUtils.loadAnimation(PreviewWords.this, android.R.anim.fade_in));
							m_WordImg.setVisibility(View.VISIBLE);
							
							if (m_WordImgAnimation != null) {
								m_QuizImgAnimationTimer.cancel();
								m_QuizImgAnimationTimer = null;
								m_WordImgAnimation = null;
							}
							m_WordImgAnimation = new WordImgAnimation();
							m_QuizImgAnimationTimer = new Timer(false);
							m_QuizImgAnimationTimer.schedule(m_WordImgAnimation, 500, 500);
						}
					}
				}
				return true;
			}
		});
		
		
		/* Setting Left Button */
		m_LeftBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_iSelectedItem != 0)
					m_PreviewImgGallery.setSelection(m_iSelectedItem-1);
			}
		});
		
		
		/* Setting Right Button */
		m_RightBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_iSelectedItem != m_PreviewImgGallery.getCount()-1)
					m_PreviewImgGallery.setSelection(m_iSelectedItem+1);
			}
		});
		
		
		/* Setting Picture Viewer Button */
		m_PictureViewerBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!NetworkConnInfo.IsWifiAvailable(PreviewWords.this) && !NetworkConnInfo.Is3GAvailable(PreviewWords.this))
				{
					Toast.makeText(PreviewWords.this, "네크워크에 연결할 수 없습니다.", Toast.LENGTH_LONG).show();
				} else {
					if (!getFileDirectory())
						Toast.makeText(PreviewWords.this, "SD 카드를 삽입하여 주십시오.", Toast.LENGTH_SHORT).show();
					else {
						File mFile = new File(m_FileDirectory + "/frog_0.png");
						if (mFile.exists()) {
							m_aBitmap = new Bitmap[4];
							for (int i=0 ; mFile.exists() ; ++i) {
								m_aBitmap[i] = BitmapFactory.decodeFile(mFile.getPath().toString());
								mFile = new File(m_FileDirectory + "/frog" + "_" + (i+1) + ".png");
								Log.d(TAG, "mFile = " + mFile.getPath().toString() + " i = " + i);
							}
							m_Handler.sendEmptyMessage(0);
						} else {
							Toast.makeText(PreviewWords.this, "Image loading!", Toast.LENGTH_SHORT).show();
							m_PhotoLoadingThread = new PhotoLoadingThread(m_Handler);
							m_PhotoLoadingThread.start();
							m_PhotoLoadingProgressBar.setVisibility(View.VISIBLE);
						}
						m_PictureViewerPopupWindow.showAtLocation(m_PictureViewerLayout, Gravity.CENTER, 0, 0);
					}
				}
			}
		});
	}
	
	/********************************
	 * onResume
	 ********************************/
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!m_QuizBGM.isPlaying()) 
			m_QuizBGM.start();
	}

	/********************************
	 * onDestroy
	 ********************************/
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(m_QuizBGM != null) {
			if(m_QuizBGM.isPlaying()) 
				m_QuizBGM.pause();
		}
	}

	/********************************
	 * onDestroy
	 ********************************/
	protected void onDestroy() {
		// TODO Auto-generated method stub
		m_LeftImgVector = null;
		m_RightImgVector = null;
		if (m_QuizImgAnimationTimer != null)
			m_QuizImgAnimationTimer.purge();
		System.gc();
		super.onDestroy();
	}

	
	/********************************
	 * Get file directory and Check mount state
	 ********************************/
	public boolean getFileDirectory() {
		Log.d(TAG, "getFileDirectory()");
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			m_FileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HelloWorldEnglish");
			Log.d(TAG, "m_FileDirectory = " + m_FileDirectory);
			if (!m_FileDirectory.exists()) {
				Log.d(TAG, "MakeDirectory");
				m_FileDirectory.mkdir();
			}
			return true;
		} else
			m_FileDirectory = null;
		
		Log.d(TAG, "m_FileDirectory = " + m_FileDirectory);
		return false;
	}
	
	
	/********************************
	 * makeView()
	 * This method is needed to implement ViewSwitcher.ViewFactory
	 ********************************/
	public View makeView() {
		ImageView mPreviewWordsView = new ImageView(this);
		mPreviewWordsView.setBackgroundColor(0x00000000);
		mPreviewWordsView.setScaleType(ImageView.ScaleType.FIT_XY);
		mPreviewWordsView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return mPreviewWordsView;
	}
	
	
	/********************************
	 * ImageAdapter
	 ********************************/
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

			if (DEBUG) Log.d(TAG, "position = " + position);
			imageView.setImageBitmap(m_LeftImgVector.get(position));			
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
	
	
	/********************************
	 * WordImgAnimation
	 ********************************/
	class WordImgAnimation extends TimerTask {
		WordImgAnimation () {
			repeat = 0;
		}

		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					if (m_WordImg.isShown()) {
						if (DEBUG) Log.d(TAG, "Running, repeat=" + ++repeat);
						if (repeat == 5) {
							m_WordImg.setAnimation(AnimationUtils.loadAnimation(PreviewWords.this, android.R.anim.fade_out));
							m_WordImg.setVisibility(View.GONE);
						}
						if (repeat%2 == 1) {
							m_QuizImg.setImageBitmap(m_LeftImgVector.get(m_iSelectedItem));
							if (DEBUG) Log.d(TAG, "Left Image");
						}
						else {
							m_QuizImg.setImageBitmap(m_RightImgVector.get(m_iSelectedItem));
							if (DEBUG) Log.d(TAG, "Right Image");
						}
					}
				}
			});
			if (repeat == 5) {		
				this.cancel();
			}
		}
	}
	
	
	/********************************
	 * Image Loading Thread
	 ********************************/
	class ImageLoadingThread extends Thread {
		AssetManager am = getResources().getAssets();
		public void run() {
			for(int i=5 ; i<m_ItemVector.size() ; ++i) {
				if (DEBUG) Log.d(TAG, "ItemVector = " + i);
				BitmapDrawable bd = (BitmapDrawable)getResources().getDrawable(m_ItemVector.get(i).iItemImgId);
				Bitmap bit = bd.getBitmap();
				m_LeftImgVector.add(Bitmap.createBitmap(bit, 0, 0, bit.getWidth()/2, bit.getHeight()));
				m_RightImgVector.add(Bitmap.createBitmap(bit, bit.getWidth()/2, 0, bit.getWidth()/2, bit.getHeight()));
			}
			
			for(int i=0 ; i<m_ItemVector.size() ; ++i) {		
				try {
					m_SoundEffectId[i] = m_SoundEffect.load(am.openFd("mfx/"+m_ItemVector.get(i).strWordCharId+".mp3"), 1);
				} catch (IOException e) {
					Log.d(TAG, "Sound not found");
					e.printStackTrace();
				}
			}
		}
	}
	
	/********************************
	 * Photo Loading Thread
	 ********************************/
	class PhotoLoadingThread extends Thread {
		private Handler mHandler;
				
		public PhotoLoadingThread(Handler handler) {
			if (DEBUG) Log.d(TAG, "ImageLoadingThread Contructor");	
			mHandler = handler;
		}
		
		public void run() {
			m_ImageUrlArr = getImageURLs("frog", 4, 1); //m_ItemVector.get(m_iSelectedItem).strWordCharId
			m_aBitmap = new Bitmap[m_ImageUrlArr.size()];
			for (int count=0; count < m_ImageUrlArr.size(); count++){
				if (DEBUG) Log.d(TAG, m_ImageUrlArr.get(count));
				m_aBitmap[count] = ImageManager.UrlToBitmap((m_ImageUrlArr.get(count)));
				StoreByteImage(m_ItemVector.get(m_iSelectedItem).strWordCharId, count);
			}
			mHandler.sendEmptyMessage(0);
		}
	}
	
	
	/********************************
	 * Store Photos
	 ********************************/
	public boolean StoreByteImage(String expName, int count) {      
		Log.d(TAG, "StoreByteImage");
	    FileOutputStream fileOutputStream = null;      
	    try {   
	  
	        /*BitmapFactory.Options options=new BitmapFactory.Options();   
	        options.inSampleSize = 5;   */
	          
	           
	        fileOutputStream = new FileOutputStream(m_FileDirectory + "/frog" + "_" + count + ".png");   
	                           
	  
	        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);   
	  
	        m_aBitmap[count].compress(CompressFormat.PNG, 100, bos);   
	  
	        bos.flush();   
	        bos.close();   
	  
	    } catch (FileNotFoundException e) {   
	        // TODO Auto-generated catch block   
	    	Log.e(TAG, "FileNotFoundException");
	        e.printStackTrace();   
	    } catch (IOException e) {   
	        // TODO Auto-generated catch block
	    	Log.e(TAG, "IOException");
	        e.printStackTrace();   
	    }   
	  
	    return true;   
	}  

	/********************************
	 * Picture Viewer
	 ********************************/
	class PictureViewer extends View {
		Bitmap mPicture;
		Context mContext;
		
		public PictureViewer(Context context) {
			super(context);
			if (DEBUG) Log.d(TAG, "PictureViewer Constructor");
			
			mContext = context;
			//mPicture = BitmapFactory.decodeResource(context.getResources(), R.drawable.pic_dog_1);
		}

		
		protected void onDraw(Canvas canvas) {
			if (DEBUG) Log.d(TAG, "PictureViewer OnDraw()");
			Paint mPaint = new Paint();
			mPaint.setAntiAlias(true);
			//canvas.drawColor(Color.WHITE);
			BlurMaskFilter mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
			mPaint.setMaskFilter(mBlur);
			//canvas.drawBitmap(mPicture, null, new Rect(0, 0, 512, 256), mPaint);
			canvas.drawBitmap(m_aBitmap[m_iPicture], null, new Rect(20, 10, 492, 246), mPaint);
			super.onDraw(canvas);
		}


		public boolean onTouchEvent(MotionEvent event) {
			if (DEBUG) Log.d(TAG, "PictureViewer onTouchEvent(), event = " + event.getAction());
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				m_fPosX = event.getX();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if ((m_fPosX - event.getX()) < -50) { // Moving left
					if (m_iPicture == 0)
						m_iPicture = m_aBitmap.length;
					--m_iPicture;
				} else if ((m_fPosX - event.getX()) > 50) { // Moving right
					if (m_iPicture == m_aBitmap.length-1)
						m_iPicture = -1;
					++m_iPicture;
				} else { // Click
					m_PictureViewerPopupWindow.dismiss();
				}
				invalidate(); 
				/*else if (event.getAction() == MotionEvent.ACTION_UP) {
				if ((m_fPosX - event.getX()) < -50) { // Moving left
					if (m_iPicture == 0)
						m_iPicture = m_ItemVector.size()-1;
					mPicture = BitmapFactory.decodeResource(mContext.getResources(), m_ItemVector.get(m_iPicture--).iCardImgId);
				} else if ((m_fPosX - event.getX()) > 50) { // Moving right
					if (m_ItemVector.size()-1 == m_iPicture)
						m_iPicture = 0;
					mPicture = BitmapFactory.decodeResource(mContext.getResources(), m_ItemVector.get(m_iPicture++).iCardImgId);
				} else { // Click
					m_PictureViewerPopupWindow.dismiss();
				}
				invalidate();*/
			}
			return super.onTouchEvent(event);
		}
	}
	
	
	/********************************
	 * Get Image URLs (from Wooram Jung)
	 ********************************/
	public ArrayList <String> getImageURLs(String searchText, int perPage, int pageNum){
		Log.e(TAG, "getImageURLs for term " + searchText);
		ArrayList <String> urlArray = new ArrayList <String>();
		Transport rest;
		try {
			rest = new REST();
			String [] a = new String[1];
			a[0] = new String(searchText);
			Flickr flickr=new Flickr(FLICKR_KEY, FLICKR_SEC, rest);
			SearchParameters searchParams=new SearchParameters();
			searchParams.setTagMode("all");
			searchParams.setUserId(FLICKR_UID);
			searchParams.setTags(a);			

			PhotosInterface photosInterface=flickr.getPhotosInterface();
			PhotoList photoList = photosInterface.search(searchParams,perPage,pageNum);
			
			if(photoList!=null){
				for(int i=0;i<photoList.size();i++){
					Photo photo=(Photo)photoList.get(i);
					urlArray.add(photo.getLargeUrl());
				}
			}
			else{
				Log.e(TAG, "photo list is null");
			}
			Log.e(TAG, "getPhotosInterface() photoList.size = "+photoList.size());
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}  catch (SAXException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return urlArray;
	}
}




