package com.incrediblekids.activities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.incrediblekids.util.Const;
import com.incrediblekids.util.ImageManager;
import com.incrediblekids.util.Item;
import com.incrediblekids.util.PreviewGallery;

public class PreviewWords extends Activity implements ViewSwitcher.ViewFactory, OnClickListener{
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
	/* Main */
	private FrameLayout m_PreviewFrameLayout;
	private RelativeLayout m_PreviewLayout;
	private ImageView m_LeftBtn, m_RightBtn, m_PhotoViewerBtn;
	private ImageView m_WordImg, m_QuizImg;
	private ImageView m_LoadingImg;
	private WordImgAnimation m_WordImgAnimation;
	private Timer m_QuizImgAnimationTimer;
	private String m_Theme;
	private int m_Height, m_Width;
	
	/* Gallery */
	private PreviewGallery m_PreviewImgGallery;
	private ImageAdapter m_PreviewImgAdapter;
	private int[] IMAGE_SIZE=null; 
	
	/* PhotoViewer */
	private RelativeLayout m_PhotoViewerLayout;
	private ImageView m_PhotoViewer;
	private ProgressBar m_PhotoLoadingProgressBar;
	
	/* Photo Loading and Save */
	private ArrayList <String> m_ImageUrlArr = null;
	private File m_FileDirectory;
	
	/*Image Resource */
	public ResourceClass res;
	private Vector<Item> m_ItemVector;
	private Vector<Bitmap> m_LeftImgVector, m_RightImgVector;
	private Bitmap m_aBitmap[] = null;
	
	/* Thread */
	private ImageLoadingThread m_ImgLoadingThread;
	private PhotoLoadingThread m_PhotoLoadingThread;
	
	/* Handler */
	private Handler m_Handler;
	
	/* Media Resource*/
	private MediaPlayer m_StudyBGM;
	private SoundPool m_SoundEffect = null;
	private int	m_SoundEffectId[] = null;
	private AssetManager m_AssetManager;
	
	/* Variable for current state */
	private int m_iSelectedItem=0, m_iPhoto=0;
	private float m_fPosX=0;
	private static int repeat=0;
	private boolean m_bTouchable = false;

	
	/****************************************************************
	 * onCreate
	 *  - HDPI / MDPI에 따라 Gallery 에 표시되는 이미지 사이즈를 정한다. 
	 *  - 
	 ****************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		if (DEBUG) Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		
		/* Set content view */
		setContentView(R.layout.preview_words);
		m_PreviewFrameLayout = (FrameLayout) findViewById(R.id.preview_frame_layout);
		
		/* Get Image, Sound Resource */
		getResource();
		
		/* Set Background */
		setThemeBackGround();
		
		/* Get Display Size */
		m_Height = getWindowManager().getDefaultDisplay().getHeight();
		m_Width = getWindowManager().getDefaultDisplay().getWidth();
		if (m_Width >= 800) { // HDPI
			IMAGE_SIZE = new int[]{209, 171, 133, 95, 76, 57};
		} else // MDPI
			IMAGE_SIZE = new int[]{136, 102, 85, 68, 51, 34};
		
		/* Set Photo Viewer */
		setPhotoViewerListener();
		
		/* Set Preview Gallery*/
		setPreviewImageGallery();
		
		setQuizImageListener();
		
		setButtonOnTouchListener();
		
		/* Create Handle to receive Image loading complete */
		m_Handler = new Handler() {
			public void handleMessage(Message msg) {
				if (DEBUG) Log.d(TAG, "handleMessage(), msg = " + msg);
				if (msg.what == 0) {
					Toast.makeText(PreviewWords.this, "서버 접속 실패입니다. \n네트웍이 불안정 하거나 Flickr 서버의 일시 장애 입니다. \n나중에 다시 시도 해주세요. ^^", Toast.LENGTH_LONG).show();
					m_PhotoLoadingProgressBar.setVisibility(View.GONE);
					m_PreviewLayout.setVisibility(View.VISIBLE);
					m_PhotoViewerLayout.setVisibility(View.INVISIBLE);
				} else if (msg.what == 1 || msg.what == 2) {
					Toast.makeText(PreviewWords.this, "( " + msg.what + " / 3 ) 다운로드 완료", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 3) {
					m_bTouchable = true;
					m_PhotoLoadingProgressBar.setVisibility(View.GONE);
					m_PhotoViewer.setImageBitmap(m_aBitmap[0]);
				} else if (msg.what == 4) {
					m_LoadingImg.setVisibility(View.GONE);
					m_PreviewLayout.setVisibility(View.VISIBLE);
				}
			}
		};
		
		/* Get Thumbnail Image resource by thread */
		m_ImgLoadingThread = new ImageLoadingThread(m_Handler);
		m_ImgLoadingThread.start();
	}
	
	public void setThemeBackGround() {
		if (m_Theme.equals(Const.THEME_ANIMAL)) {
			m_PreviewFrameLayout.setBackgroundResource(R.drawable.bg_animal_play);
			//m_PhotoViewerLayout.setBackgroundResource(R.drawable.bg_animal_play);
		} else if (m_Theme.equals(Const.THEME_COLOR)) {
			m_PreviewFrameLayout.setBackgroundResource(R.drawable.bg_color_play);
			//m_PhotoViewerLayout.setBackgroundResource(R.drawable.bg_color_play);
		} else if (m_Theme.equals(Const.THEME_FOOD)) {
			m_PreviewFrameLayout.setBackgroundResource(R.drawable.bg_food_play);
			//m_PhotoViewerLayout.setBackgroundResource(R.drawable.bg_food_play);
		} else if (m_Theme.equals(Const.THEME_NUMBER)) {
			m_PreviewFrameLayout.setBackgroundResource(R.drawable.bg_number_play);
			//m_PhotoViewerLayout.setBackgroundResource(R.drawable.bg_number_play);
		} else if (m_Theme.equals(Const.THEME_TOY)) {
			m_PreviewFrameLayout.setBackgroundResource(R.drawable.bg_toy_play);
			//m_PhotoViewerLayout.setBackgroundResource(R.drawable.bg_toy_play);
		}	
	}
	
	/********************************
	 * onResume
	 *  - BGM를 다시 시작.
	 ********************************/
	protected void onResume() {
		if (DEBUG) Log.d(TAG, "onResume()");
		super.onResume();
		
		if(!m_StudyBGM.isPlaying()) 
			m_StudyBGM.start();
	}

	/********************************
	 * onPause
	 *  - Thread 동작 확인해서 interrupt() 호출.
	 *  - BGM를 정지.
	 ********************************/
	protected void onPause() {
		if (DEBUG) Log.d(TAG, "onPause()");
		super.onPause();
		
		if (m_ImgLoadingThread != null && m_ImgLoadingThread.isAlive()) {
			m_ImgLoadingThread.interrupt();
			if (DEBUG) Log.d(TAG, "m_ImgLoadingThread.interrupted()");
		}
		
		if (m_PhotoLoadingThread != null && m_PhotoLoadingThread.isAlive()) {
			m_PhotoLoadingThread.interrupt();
			if (DEBUG) Log.d(TAG, "m_PhotoLoadingThread.interrupted()");
		}
		if(m_StudyBGM != null) {
			if(m_StudyBGM.isPlaying()) 
				m_StudyBGM.pause();
		}
	}

	/****************************************************************
	 * onDestroy
	 *  - 할당 받은 Image 를 Release.
	 *  - Image Animation Timer 를  Release.
	 *  - 할당 받은 Sound 를 Release.
	 ****************************************************************/
	protected void onDestroy() {
		if (DEBUG) Log.d(TAG, "onDestroy()");
		
		for (int i=0 ; i < m_LeftImgVector.size() ; ++i) {
			m_LeftImgVector.get(i).recycle();
			m_RightImgVector.get(i).recycle();
		}
		m_LeftImgVector.clear();
		m_LeftImgVector = null;
		m_RightImgVector.clear();
		m_RightImgVector = null;

		if (m_QuizImgAnimationTimer != null)
			m_QuizImgAnimationTimer.purge();
		
		m_StudyBGM.release();
		m_SoundEffect.release();
		m_SoundEffectId = null;
		
		SharedPreferences preference = getSharedPreferences(Const.PREFERNCE, 0);
		if(preference == null) {
		    Log.e(TAG, "settings null");
		} else {
			SharedPreferences.Editor editor = preference.edit();
			editor.putBoolean(Const.PHOTO_DOWNLOAD, false);
			editor.commit();
		}
		System.gc();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		if (m_PhotoViewerLayout.isShown()) {
			if (m_PhotoLoadingThread != null && m_PhotoLoadingThread.isAlive()) {
				m_PhotoLoadingThread.interrupt();
				if (DEBUG) Log.d(TAG, "m_PhotoLoadingThread.interrupted()");
			}
			m_PhotoViewer.setImageResource(R.drawable.photo_loading);
			m_PreviewLayout.setVisibility(View.VISIBLE);
			m_PhotoViewerLayout.setVisibility(View.INVISIBLE);
		} else {
			finish();
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			super.onBackPressed();
		}
	}

	/****************************************************************
	 * Main Activity onClick Method
	 *  - 사진 다운로드시 3G 팝업 Dialog 에 대한 Positive Click 처리
	 ****************************************************************/
	public void onClick(DialogInterface dialog, int which) {
		if (DEBUG) Log.d(TAG, "onClick(), which = " + which);
		SharedPreferences preference = getSharedPreferences(Const.PREFERNCE, 0);
		SharedPreferences.Editor editor = preference.edit();
		editor.putBoolean(Const.PHOTO_DOWNLOAD, true);
		editor.commit();
		onPhotoViewerClick(m_ItemVector.get(m_iSelectedItem).strWordCharId);
	}
	

	/****************************************************************
	 * Set Left / Right Button Listener
	 *  - 메인 Activity 의 좌우 버튼에 대한 Listener 등록
	 ****************************************************************/
	public void setButtonOnTouchListener() {
		m_LeftBtn.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					m_LeftBtn.setImageResource(R.drawable.btn_left_sel);
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					if(m_iSelectedItem != 0)
						m_PreviewImgGallery.setSelection(m_iSelectedItem-1);
					m_LeftBtn.setImageResource(R.drawable.btn_left);
				}
				return true;
			}
		});
		m_RightBtn.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					m_RightBtn.setImageResource(R.drawable.btn_right_sel);
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					if(m_iSelectedItem != m_PreviewImgGallery.getCount()-1)
						m_PreviewImgGallery.setSelection(m_iSelectedItem+1);
					m_RightBtn.setImageResource(R.drawable.btn_right);
				}
				return true;
			}
		});
	}
	
	
	/****************************************************************
	 * Get Sound / Image Resource
	 *  - ResourceClass로부터 테마에 해당되는 Vector 객체를 가져옴.
	 *  - Main Activity 에 있는 View Resource 를 가져옴.
	 *  - Image 를 반으로 잘라서 저장해야 함. 
	 *	   for()를 이용하여 5개 먼저 Load, 나머지는 Thread 를 호출하여  Load.
	 *  - Sound 는 PreviewWord BGM을 위해서 MediaPlayer를 사용하고,
	 *    단어 발음을 위해서 SoundPool에 Load.
	 ****************************************************************/
	public void getResource() {
		/* Get resrouce from ResourceClass */ 
		res = ResourceClass.getInstance();
		m_ItemVector = res.getvItems();
		m_Theme = res.getCurrentTheme();
		
		/* Assign from Resource */
		m_LoadingImg = (ImageView) findViewById(R.id.preview_loading);
		m_PreviewLayout = (RelativeLayout) findViewById(R.id.preview_relative_layout);
		m_QuizImg = (ImageView) findViewById(R.id.preview_center_image);
		m_WordImg = (ImageView) findViewById(R.id.preview_word_image);
		m_LeftBtn = (ImageView) findViewById(R.id.preview_leftbtn);
		m_RightBtn = (ImageView) findViewById(R.id.preview_rightbtn);
		
		m_PhotoViewerLayout = (RelativeLayout) findViewById(R.id.photo_viewer_layout);
		m_PhotoViewer = (ImageView) findViewById(R.id.preview_photo_viewer);
		m_PhotoLoadingProgressBar = (ProgressBar) findViewById(R.id.photo_loading_progress);
		m_PhotoViewerBtn = (ImageView) findViewById(R.id.preview_picviewbtn);
		if (res.getCurrentTheme() == Const.THEME_COLOR || res.getCurrentTheme() == Const.THEME_NUMBER)
			m_PhotoViewerBtn.setVisibility(View.GONE);
		
		/* Get Sound Resource */
		m_AssetManager = getResources().getAssets();
		m_StudyBGM	= MediaPlayer.create(this, R.raw.studybgm);
		m_StudyBGM.setVolume(0.5f, 0.5f);
		m_StudyBGM.setLooping(true);
		m_SoundEffect = new SoundPool(m_ItemVector.size(), AudioManager.STREAM_MUSIC, 0);
		m_SoundEffectId = new int[20];
		try {
			m_SoundEffectId[0] = m_SoundEffect.load(m_AssetManager.openFd("mfx/"+m_ItemVector.get(0).strWordCharId+".mp3"), 1);
		} catch (IOException e) {
			Log.e(TAG, "Sound not found");
			e.printStackTrace();
		}
		
		/* Get Image Resource */
		m_LeftImgVector = new Vector<Bitmap>();
		m_RightImgVector = new Vector<Bitmap>();
		for(int i=0 ; i<5 ; ++i) {
			if (DEBUG) Log.d(TAG, "ItemVector = " + i);
			BitmapDrawable bd = (BitmapDrawable)getResources().getDrawable(m_ItemVector.get(i).iItemImgId);
			Bitmap bit = bd.getBitmap();
			m_LeftImgVector.add(Bitmap.createBitmap(bit, 0, 0, bit.getWidth()/2, bit.getHeight()));
			m_RightImgVector.add(Bitmap.createBitmap(bit, bit.getWidth()/2, 0, bit.getWidth()/2, bit.getHeight()));
		}
	}
	
	
	/****************************************************************
	 * Set Photo Viewer Listener
	 *  - Photo Viewer Button 을 눌렀을 경우에, 
	 *    Preference 를 참조하여 Image 다운로드 여부를 물음.
	 ****************************************************************/
	public void setPhotoViewerListener() {
		/* Setting Photo Viewer Button */
		m_PhotoViewerBtn.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					m_PhotoViewerBtn.setBackgroundResource(R.drawable.btn_showpic_sel);
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					m_PhotoViewerBtn.setBackgroundResource(R.drawable.btn_showpic_nor);
					SharedPreferences preference = getSharedPreferences(Const.PREFERNCE, 0);
					if(preference == null) {
					    Log.e(TAG, "settings null");
					}
					if (preference.getBoolean(Const.PHOTO_DOWNLOAD, false)) {
						onPhotoViewerClick(m_ItemVector.get(m_iSelectedItem).strWordCharId);
					} else {
						new AlertDialog.Builder(PreviewWords.this) 
						.setTitle("알림")
						.setMessage("사진을 다운로드 합니다.\n3G인 경우 통신요금이 부과될 수 있습니다.\n접속 하시겠습니까?\n(다운 받은 사진은 재 다운로드 하지 않습니다.)")
						.setIcon(R.drawable.icon)
						.setPositiveButton("연결", PreviewWords.this)
						.setNegativeButton("닫기", null)
						.create()
						.show();
					}
					
				}
				return true;
			}
		});
		
		/* Setting Photo Image */
		/*m_PhotoViewer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (m_PhotoLoadingThread != null && m_PhotoLoadingThread.isAlive()) {
					m_PhotoLoadingThread.interrupt();
					if (DEBUG) Log.d(TAG, "m_PhotoLoadingThread.interrupted()");
				}
					
				m_PhotoImg.setImageResource(R.drawable.photo_loading);
				m_PhotoLoadingProgressBar.setVisibility(View.GONE);
				if (m_PhotoViewerPopupWindow != null) {
					m_PhotoViewerPopupWindow.dismiss();
					m_PhotoViewerPopupWindow = null;
				}
		});*/
		
		/* Setting Photo Image */
		m_PhotoViewer.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (!m_bTouchable) 
					return true;
				if (DEBUG) Log.d(TAG, "Photo image is touched, event = " + event.getAction());
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_fPosX = event.getX();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if ((m_fPosX - event.getX()) < -50) { // Moving left
						if (m_iPhoto == 0)
							m_iPhoto = m_aBitmap.length;
						m_PhotoViewer.setImageBitmap(m_aBitmap[--m_iPhoto]);
					} else { // if ((m_fPosX - event.getX()) > 50) { // Moving right, click
						if (m_iPhoto == m_aBitmap.length-1)
							m_iPhoto = -1;
						m_PhotoViewer.setImageBitmap(m_aBitmap[++m_iPhoto]);
					} 
				}
				return true;
			}
		});
	}
	
	
	/****************************************************************  
	 * On Photo Viewer Click Method
	 *  - setPhotoViewerListener()에서 다운로드를 선택하면 네트웍(3G, Wifi)와 SD 카드 유무를 검사.
	 *  - 기존의 다운받은 사진이 있는지 확인.
	 *  - 사진이 없을 경우, Thread 이용해서 사진 다운로드.
	 ****************************************************************/
	public void onPhotoViewerClick(String expName) {
		if (!NetworkConnInfo.IsWifiAvailable(PreviewWords.this) && !NetworkConnInfo.Is3GAvailable(PreviewWords.this)) {
			Toast.makeText(PreviewWords.this, "네트워크에 연결할 수 없습니다.", Toast.LENGTH_LONG).show();
		} else {
			if (!getFileDirectory())
				Toast.makeText(PreviewWords.this, "사진을 저장할 SD 카드가 없습니다.", Toast.LENGTH_SHORT).show();
			else {
				m_bTouchable = false;
				int i=0;
				File mFile = new File(m_FileDirectory + "/" + expName + "_" + i + ".png");
				while (mFile.exists()) {
					if (mFile.length() == 0)
						break;
					++i;
					mFile = new File(m_FileDirectory + "/" + expName + "_" + i + ".png");
				}
				
				if (i == 3) { // 이미지 3장 모두 저장확인. 바로 보여주기
					m_aBitmap = new Bitmap[3];
					for (i=0 ; i<3 ; ++i) {
						mFile = new File(m_FileDirectory + "/" + expName + "_" + i + ".png");
						m_aBitmap[i] = BitmapFactory.decodeFile(mFile.getPath().toString());
						if (DEBUG) Log.d(TAG, "mFile = " + mFile.getPath().toString() + " i = " + i);
					}
					m_Handler.sendEmptyMessage(3);
				} else {
					Toast.makeText(PreviewWords.this, "사진을 다운 받고 있습니다. \n sdcard/.helloWorldEnglish에 저장됩니다.", Toast.LENGTH_SHORT).show();
					m_PhotoLoadingThread = new PhotoLoadingThread(m_Handler, expName);
					m_PhotoLoadingThread.start();
					m_PhotoLoadingProgressBar.setVisibility(View.VISIBLE);
				}
				m_PhotoViewerLayout.setVisibility(View.VISIBLE);
				m_PreviewLayout.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	
	/****************************************************************
	 * Set Preview Image Gallery
	 *  - 전체 이미지를 훑어 볼 수 있는 Gallery 추가 
	 *    onItemSelectedListener를 이용해서 기존의 Animation Timer 를 초기화
	 ****************************************************************/
	public void setPreviewImageGallery() {
		/* Setting Preview Image Gallery */
		m_PreviewImgAdapter = new ImageAdapter(this);
		m_PreviewImgGallery = (PreviewGallery) findViewById(R.id.preview_gallery);
		//m_PreviewImgGallery.setBackgroundColor(color.white);
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
	}
	
	
	/****************************************************************
	 * Set Quiz(Center) Image Listener
	 *  - Main Activity 의 중앙 Image 를 선택하면 Animation 을 보여주고, 해당 발음을 들려준다.
	 ****************************************************************/
	public void setQuizImageListener() {
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
	}
	
	
	/********************************
	 * Get file directory and Check mount state
	 ********************************/
	public boolean getFileDirectory() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			m_FileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.helloWorldEnglish");
			if (!m_FileDirectory.exists()) {
				m_FileDirectory.mkdir();
			}
			return true;
		} else
			m_FileDirectory = null;
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
		private Context m_cContext;

		public ImageAdapter(Context _context) {
			m_cContext = _context;
			//res = ResourceClass.getInstance();
			//m_ItemVector = res.getvItems();
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
		private Handler mHandler;
		
		public ImageLoadingThread(Handler _mHandler) {
			if (DEBUG) Log.d(TAG, "ImageLoadingThread Contructor");	
			mHandler = _mHandler;
		}
		
		public void run() {
			for(int i=5 ; i<m_ItemVector.size() ; ++i) {
				if (DEBUG) Log.d(TAG, "ItemVector = " + i);
				if(!Thread.currentThread().isInterrupted()) {
					BitmapDrawable bd = (BitmapDrawable)getResources().getDrawable(m_ItemVector.get(i).iItemImgId);
					Bitmap bit = bd.getBitmap();
					m_LeftImgVector.add(Bitmap.createBitmap(bit, 0, 0, bit.getWidth()/2, bit.getHeight()));
					m_RightImgVector.add(Bitmap.createBitmap(bit, bit.getWidth()/2, 0, bit.getWidth()/2, bit.getHeight()));
				}				
			}
			
			for(int i=1 ; i<m_ItemVector.size() ; ++i) {		
				if(!Thread.currentThread().isInterrupted()) {
					try {
						m_SoundEffectId[i] = m_SoundEffect.load(m_AssetManager.openFd("mfx/"+m_ItemVector.get(i).strWordCharId+".mp3"), 1);
					} catch (IOException e) {
						Log.e(TAG, "Sound not found");
						e.printStackTrace();
					}
				}
			}
			mHandler.sendEmptyMessage(4);
		}
	}

	/********************************
	 * Photo Loading Thread
	 ********************************/
	class PhotoLoadingThread extends Thread {
		private Handler mHandler;
		private String expName;
		private int count;
		
		public PhotoLoadingThread(Handler _mHandler, String _expName) {
			if (DEBUG) Log.d(TAG, "PhotoLoadingThread Contructor");	
			mHandler = _mHandler;
			expName = _expName;
		}
		
		public void run() {
			m_ImageUrlArr = getImageURLs(expName, 3, 1); //m_ItemVector.get(m_iSelectedItem).strWordCharId
			m_aBitmap = new Bitmap[m_ImageUrlArr.size()];

			if (m_ImageUrlArr.size() != 0) { 
				for (count=0; count < m_ImageUrlArr.size(); count++){
					if(!Thread.currentThread().isInterrupted()) {
						if (DEBUG) Log.d(TAG, m_ImageUrlArr.get(count));
						m_aBitmap[count] = Bitmap.createScaledBitmap(ImageManager.UrlToBitmap((m_ImageUrlArr.get(count))), 430, 400, false);
						if (m_aBitmap[count] != null) {
							StoreByteImage(expName, count);
							mHandler.sendEmptyMessage(count+1);
						} else {
							
						}
					}
				}
			} else {
				mHandler.sendEmptyMessage(0);
				Log.e(TAG, "Image Url Arr is 0");
			}
		}
	}
	
	
	/********************************
	 * Store Photos
	 ********************************/
	public boolean StoreByteImage(String expName, int count) {      
	    FileOutputStream fileOutputStream = null;
	    File mFile = new File(m_FileDirectory + "/" + expName + "_" + count + ".png");
	    if (mFile.exists())
	    	return true;
	    
	    try {            
	        fileOutputStream = new FileOutputStream(m_FileDirectory + "/" + expName + "_" + count + ".png");   
  
	        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
	  
	        m_aBitmap[count].compress(CompressFormat.PNG, 100, bos);   
	  
	        bos.flush();   
	        bos.close();   
	  
	    } catch (FileNotFoundException e) {
	    	Log.e(TAG, "FileNotFoundException");
	        e.printStackTrace();   
	    } catch (IOException e) {
	    	Log.e(TAG, "IOException");
	        e.printStackTrace();   
	    }   
	  
	    return true;   
	}  

	
	/********************************
	 * Get Image URLs (from Wooram Jung)
	 ********************************/
	public ArrayList <String> getImageURLs(String searchText, int perPage, int pageNum){
		if (DEBUG) Log.d(TAG, "getImageURLs for term " + searchText);
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
			if (DEBUG) Log.d(TAG, "getPhotosInterface() photoList.size = "+photoList.size());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
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




