/*
 * @(#) MatchQuiz.java 1.0, 2011. 3. 7.
 * 
 */
package com.incrediblekids.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.incrediblekids.util.Const;
import com.incrediblekids.util.Item;

public class MatchQuiz extends Activity implements View.OnClickListener {
	
	private final static String TAG = "MatchQuiz";
	private final static int MAX_COUNT 			 = 8;
	private final int CARD_PAIR_COUNT 			 = 4;
	private final int ANI_ROTATION_TIME_DURATION = 180;
	private final int TOGGLE_INTERVAL 			 = 200;
	private final long ANIMATION_TIME_DURATION 	 = 60 * 60 * 10;
	private final long HINT_TIME_DURATION 		 = 60 * 60 * 1;
	
	private long m_TimeInterval;
	private long m_AnimationTimeDuration;
	
	/* Animation status flag */ 
	private boolean m_IsPause;
	
	private ArrayList<Item> m_ItemList;
	
	private ImageView m_Hint;
	private ImageView m_Skip;
	
	private ImageView[] m_ItemImages;
	private ImageView[] m_Questions;
	private ViewGroup[] m_Containers;
	private HashMap<Integer, Integer> m_RandomHashMap;
	
	private ResourceClass m_Res;
	private Vector<Item> m_ItemVector;
	
	private Intent 				m_PopupIntent;
	
	/* FrameImage & Interpolator Animation */
	private ImageView			m_TimeFrameImage;
	private ImageView			m_TimeFrameImageEnd;
	private AnimationDrawable 	m_TimeFrameAnimation;
	private Animation			m_TimeFlowAnimation;
	
	/* timeframe image's left position */
	private int m_LeftPosition;
	
	/* Sound Effect */
	private SoundPool m_SoundEffect;
	private int	m_SoundEffectId;
	
	/* BGM */
	private MediaPlayer m_QuizBGM;
	
	/* Runnable */
	private Runnable m_Runnable;
	
	/* MatchManger */
	private MatchManager m_MatchManager;
	
	/* Retry Game variable */
	private boolean m_IsInit = false;
	private int m_StartPosition;
	private int m_EndPosition;
	
	
	/**
	 * using for animation end callback.
	 */
	private Handler m_Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what) {
			case ANIMATION_ENDED:
				analysisMatchResult();
				clickEnable(true);
				break;
				
			case TOGGLE_ENDED:
				Log.d(TAG,"TOGGLE_ENDED");
				clickEnable(true);
				break;
			}
		}
	};

	public static final int ITEM_DEFAULT 	= 0;
	public static final int ITEM_MATCHED 	= 1;
	public static final int ANIMATION_ENDED = 10;
	public static final int TOGGLE_ENDED 	= 11;
	public static final int RETRY_DIALOG 	= 20;
	
	public class ViewHolder {
		
		View itemView;
		View questionView;
		
		public ViewHolder(ImageView itemView, ImageView questionView) {
			this.itemView = itemView;
			this.questionView = questionView;
		}

		public View getItemView() {
			return itemView;
		}

		public View getQuestionView() {
			return questionView;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		
		setContentView(R.layout.match_quiz);
		
		m_IsInit = true;
		
		init();
		toggleImages();
		setItems();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult()");
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Const.RETRY_DIALOG_RESULT) {
			if(resultCode == RESULT_OK) {
				Log.d(TAG, "resultCode:" + "RESULT_OK");
				Intent intent = new Intent(MatchQuiz.this, MatchQuiz.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
			else if(resultCode == RESULT_CANCELED) {
				Log.d(TAG, "resultCode:" + "RESULT_CANCELED");
				setResult(RESULT_OK);
				finish();
				Intent intent = new Intent(MatchQuiz.this, GameStatusActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}
	}

	private void analysisMatchResult() {
		Log.d(TAG, "analysisMatchResult()");
		if(m_MatchManager.isTouchedSameView()) {
			m_MatchManager.setSolo(true);
			m_MatchManager.setTouchedSameView(false);
			return;
		}
		else { //Touched SameView
			
			if(!m_MatchManager.isSolo()) {
				if(m_MatchManager.isMatched()) {
					Log.d(TAG, "Matched!!");
					clickDisable(m_MatchManager.getPreClickedId(), m_MatchManager.getCurClickedId());
				}
				else {
					toggleClickedItems(m_MatchManager.getPreClickedId(), m_MatchManager.getCurClickedId());
				}
				m_MatchManager.setSolo(true);
				m_MatchManager.clearPreClikedItemInfo();
			}
			else {
				m_MatchManager.setSolo(false);
			}
		}

		if(m_MatchManager.isAllMatched()) {
			Log.d(TAG, "Game End");
			pauseAnimation();
			m_TimeFrameAnimation.stop();
			
			updatePreference();
			
			finish();
			Intent intent = new Intent(MatchQuiz.this, GameStatusActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			//TODO: goto Main
//			startActivityForResult(m_PopupIntent, Const.RETRY_DIALOG_RESULT);
		}
	}

	/**
	 * update Current theme's score
	 */
	private void updatePreference() {
	    String currentTheme = m_Res.getCurrentTheme();
	    int currentVal = 0;
	    int previousScore = 0;
	    int maxValue = 0;
	    
	    // We need an Editor object to make preference changes.
	    // All objects are from android.context.Context
	    SharedPreferences settings = getSharedPreferences(Const.PREFERNCE, 0);
	    
	    currentVal = settings.getInt(currentTheme, 0);
	    previousScore = getIntent().getIntExtra(Const.CUR_LEVEL, 0);
	    
	    Log.d(TAG, "currentTheme: " + currentTheme);
	    Log.d(TAG, "currentVal: " + currentVal);
	    Log.d(TAG, "previousScore: " + previousScore);
	    
	    if(previousScore > currentVal) {
	        // do nothing;
	    }
	    else {
	        if(currentTheme == Const.THEME_ANIMAL || currentTheme == Const.THEME_TOY) {
	            maxValue = 4;
	        }
	        else {
	            maxValue = 2;
	        }
	        
	        if(currentVal < maxValue) {
	            currentVal++;
	            SharedPreferences.Editor editor = settings.edit();
	            editor.putInt(currentTheme, currentVal);

	            // Commit the edits!
	            editor.commit();
	        }
	        
	    }
    }

    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Log.d(TAG, "onWindowFocusChanged()");
		setTimeAnimation(false);
	}

	private void init() {
		Log.d(TAG, "init()");
		
		m_PopupIntent 		= new Intent(MatchQuiz.this, PopupActivity.class);
		m_ItemList			= new ArrayList<Item>(CARD_PAIR_COUNT);
		
		m_ItemImages 		= new ImageView[MAX_COUNT];
		m_Questions			= new ImageView[MAX_COUNT];
		m_Containers 		= new ViewGroup[MAX_COUNT];
		
		m_QuizBGM			= MediaPlayer.create(this, R.raw.quizbgm); 
		m_SoundEffect		= new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		m_MatchManager		= MatchManager.getInstance();
		m_Res 				= ResourceClass.getInstance();
		
		m_ItemVector 		= m_Res.getvItems();
		m_IsPause			= false;
		m_LeftPosition		= 0;
		m_TimeInterval		= 0;
		m_AnimationTimeDuration	= ANIMATION_TIME_DURATION;
		
		m_PopupIntent.setAction(Const.MATCH_QUIZ);
		
		int firstItemValue	= R.id.ivItems1;
		int questionValue	= R.id.ivQuestion1;
		int viewGroupValue	= R.id.flContainer1;
		
		for(int i = 0; i < MAX_COUNT; i++) {
			m_ItemImages[i]	= (ImageView)findViewById(firstItemValue); // 占쎌쥙�ο옙蹂⑹삕占쎈끃�뺧옙醫묒삕_-;
			m_ItemImages[i].setOnClickListener(this);
			m_ItemImages[i].setTag(ITEM_DEFAULT);
			m_ItemImages[i].setClickable(true);
			
			m_Questions[i]	= (ImageView)findViewById(questionValue);
			m_Questions[i].setOnClickListener(this);
			m_Questions[i].setClickable(true);
			
			m_Containers[i]	= (ViewGroup)findViewById(viewGroupValue); // 占쎌쥙�ο옙蹂⑹삕占쎈끃�뺧옙醫묒삕_-;
			m_Containers[i].setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
			m_Containers[i].setTag(new ViewHolder(m_ItemImages[i], m_Questions[i]));
			
			firstItemValue  = firstItemValue + 3;
			questionValue   = questionValue + 3;
			viewGroupValue  = viewGroupValue + 3;
		}
		
		m_Hint				= (ImageView)findViewById(R.id.ivHint);
		m_Skip				= (ImageView)findViewById(R.id.ivSkip);
		
		m_TimeFrameImage 	= (ImageView)findViewById(R.id.ivTimeFrame);
		m_TimeFrameImage.setBackgroundResource(R.drawable.frame_transition);
		
		m_TimeFrameImageEnd	= (ImageView)findViewById(R.id.ivTimeFrameEnd);
		
		m_SoundEffectId		= m_SoundEffect.load(this, R.raw.flipflop, 1);
		m_Hint.setOnClickListener(this);
		m_Hint.setClickable(false);
		
		m_Skip.setOnClickListener(this);
		
		m_QuizBGM.setLooping(true);
		
		/* Get Sound Button Image Resource */
		/** deleted
		BitmapDrawable bd 	= (BitmapDrawable)getResources().getDrawable(R.drawable.btn_sound);
		Bitmap bit 			= bd.getBitmap();
		Log.d(TAG, "bit.getWidtd(): " + bit.getWidth());
		Log.d(TAG, "bit.getHeight(): " + bit.getHeight());
		
		m_bitSoundBtnLeft 	= Bitmap.createBitmap(bit, 0, 0, bit.getWidth()/2, bit.getHeight());
		m_bitSoundBtnRight 	= Bitmap.createBitmap(bit, bit.getWidth()/2, 0, bit.getWidth()/2, bit.getHeight());
		
		m_SoundBtnImage 	= (ImageView)findViewById(R.id.ivSound);
		m_SoundBtnImage.setImageBitmap(m_bitSoundBtnLeft);
		*/
		
		clickEnable(false);
		makeRandomHashMap();
	}
	
	/**
	 * N 占쎌쥙�⑼옙��쐻占쎈쉨怨⑸쐻�좎뜴�앾옙�됱굶櫻뗫봿�뺞���쇿뜝�꿔렎�됵옙��옙醫롳옙占썬굩彛わ쭚�껋굲占쎌쥙�εㅇ硫⑤쐻占쎌괵estion �뜯뫀占쏙옙�낅쐻占쏙옙��춯�뚮꺼占쎌눨�앾옙��텖占쎌쥙猷욑옙�뗭삕�좎럩�뺝뜝�꾩삀占쏙옙彛わ옙��삕.
	 */
	private void toggleImages() {
		
		m_Runnable = new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0; i < MAX_COUNT; i++) {
					m_ItemImages[i].setVisibility(View.GONE);
					m_Questions[i].setVisibility(View.VISIBLE);
				}
				//Test
				if(m_TimeFrameImage == null)
					Log.d(TAG, "m_TimeFrameImage");
				
				if(m_TimeFlowAnimation == null)
					Log.d(TAG, "m_TimeFlowAnimation");
				m_TimeFrameImage.startAnimation(m_TimeFlowAnimation);
				clickEnable(true);
				m_Hint.setClickable(true);
			}
		};
		
		m_Handler.postDelayed(m_Runnable, HINT_TIME_DURATION);
	}
	
	/**
	 * Set ItemImages & WordImages randomly.
	 * add to MatchManager's items
	 */
	private void setItems() {
		Log.d(TAG, "setItems()");
		Iterator<Integer> ii = m_RandomHashMap.keySet().iterator();
		int vectorNum = 0;
		int flag = 0;
		
		setItemList();
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = m_RandomHashMap.get(key);
			Log.d(TAG, "value:" + value);
			
			if((flag % 2) == 0) {
				
				m_ItemImages[value].setImageResource(m_ItemVector.get(vectorNum).iCardImgId);
				m_MatchManager.addItem(getParentId(m_ItemImages[value].getId()), m_ItemVector.get(vectorNum).strWordCharId);
//				m_ItemImages[value].setImageResource(m_ItemList.get(vectorNum).iCardImgId);
//				m_MatchManager.addItem(getParentId(m_ItemImages[value].getId()), m_ItemList.get(vectorNum).strWordCharId);
//				Log.d(TAG, "setItems() key: " +  getParentId(m_ItemImages[value].getId()));
//				Log.d(TAG, "setItems() String: " +  m_ItemVector.get(vectorNum).strWordCharId);
			}
			else {
				m_ItemImages[value].setImageResource(m_ItemVector.get(vectorNum).iCardWordId);
				m_MatchManager.addItem(getParentId(m_ItemImages[value].getId()), m_ItemVector.get(vectorNum).strWordCharId);
//				m_ItemImages[value].setImageResource(m_ItemList.get(vectorNum).iCardWordId);
//				m_MatchManager.addItem(getParentId(m_ItemImages[value].getId()), m_ItemList.get(vectorNum).strWordCharId);
//				Log.d(TAG, "setItems() key: " +  getParentId(m_ItemImages[value].getId()));
//				Log.d(TAG, "setItems() String: " +  m_ItemVector.get(vectorNum).strWordCharId);
				vectorNum++;
			}
			flag++;
		}
	}

	private void setItemList() {
		ArrayList<Item> oriItemList;
		Random rnd;
		Bundle bundle;
		
		int tempNum;
		int loopCount = 0;
		
		bundle 		= getIntent().getExtras();
		oriItemList = bundle.getParcelableArrayList(Const.MATCH_QUIZ);
		
		if(oriItemList == null) {
			Log.e(TAG, "setItemList(), oriItemList is null");
			return;
		}
		
		rnd = new Random(System.currentTimeMillis());
		
		
		while(true) {
			tempNum = Math.abs(rnd.nextInt(oriItemList.size()));
			Log.d(TAG, "tempNum: " + tempNum);
			if(!m_ItemList.contains(oriItemList.get(tempNum))) {
				m_ItemList.add(oriItemList.get(tempNum));
				oriItemList.remove(tempNum);
				loopCount++;
			}
			if(loopCount > 3)
				break;
		}
		
	}

	/* Set TimeAnimation */
	/**
	 * @param isRetry 
	 */
	private void setTimeAnimation(boolean isRetry) {
		Log.d(TAG, "setTimeAnimation()");
		View targetParent;
		float fromXDelta;
		float toXDelta;
		
		targetParent = (View) m_TimeFrameImage.getParent();
		fromXDelta 	 = m_TimeFrameImage.getLeft() - m_LeftPosition;
		toXDelta 	 = targetParent.getWidth() - m_TimeFrameImage.getWidth() - m_LeftPosition;
		
		// temp T_T
		if(m_IsInit) {
			m_StartPosition = (int) fromXDelta;
			m_EndPosition = (int) toXDelta;
			m_IsInit = false;
		}
		
		m_TimeFrameAnimation 	= (AnimationDrawable)m_TimeFrameImage.getBackground();
		
		if(isRetry) {
			m_TimeFlowAnimation	= new TranslateAnimation(m_StartPosition, m_EndPosition, 0.0f, 0.0f);
		}
		else {
			m_TimeFlowAnimation	= new TranslateAnimation(fromXDelta, toXDelta, 0.0f, 0.0f);
		}
		
		m_AnimationTimeDuration = m_AnimationTimeDuration - m_TimeInterval;
		if(m_AnimationTimeDuration < 0) 
			m_AnimationTimeDuration = 0;
		
		m_TimeFlowAnimation.setDuration(m_AnimationTimeDuration);
		Log.d(TAG, "m_AnimationTimeDuration: " + m_AnimationTimeDuration);
		Log.d(TAG, "fromXDelta: " + fromXDelta);
		Log.d(TAG, "toXDelta: " + toXDelta);
		m_TimeFlowAnimation.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.accelerate_decelerate_interpolator));
		m_TimeFlowAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				m_TimeFrameAnimation.start();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				Log.d(TAG, "onEnd()");
				if(m_IsPause) {
					// Do nothing : )
				}
				else {	// Animation end. Time out.
					m_TimeFrameAnimation.stop();
					m_TimeFrameImage.setVisibility(View.INVISIBLE);
					m_TimeFrameImageEnd.setVisibility(View.VISIBLE);
					m_Hint.setClickable(false);
					
					startActivityForResult(m_PopupIntent, Const.RETRY_DIALOG_RESULT);
				}
			}
		});
	}
	
	/**
	 * Random HashMap 嶺뚯쉧猷뉛옙�욍뀋占쎈낌�뺧옙醫묒삕br />
	 * Item�뜯뫀�꾬옙占쏙옙醫롳옙占쎈갊�앾옙�ｋ윯嶺뚯솳�우굲占쎌쥙猷욑옙�곸쒜繹먮씮�뺟춯�곷즸占썬룇�쀯옙釉먲옙勇싲８留⑺씇嶺뚯쉸裕됧뜝占�
	 */
	private void makeRandomHashMap() {
		Log.d(TAG, "makeRandomHashMap()");
		Random rnd;
		int tempNum = 0;
		int key = 0;
		
		m_RandomHashMap = new HashMap<Integer, Integer>();
		rnd = new Random(System.currentTimeMillis());
		
		while(true) {
			tempNum = Math.abs(rnd.nextInt(MAX_COUNT));
			if(!m_RandomHashMap.containsValue(tempNum)) {
				m_RandomHashMap.put(key, tempNum);
				key++;
			}
			if(m_RandomHashMap.size() == MAX_COUNT)
				break;
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ivHint) {
			Log.d(TAG, "Hint!");
			showHint();
		}
		else if(v.getId() == R.id.ivSkip) {
			m_Handler.removeCallbacks(m_Runnable);
			setResult(RESULT_OK);
			finish();
		}
		else {
			ViewGroup parentView = (ViewGroup) v.getParent();

			doRotation(parentView, false);

			m_MatchManager.clickedItem(getParentId(v.getId()));
			if(!m_MatchManager.isSolo()) {
				m_MatchManager.setCurrentPviewId(parentView.getId());
			}
		}
	}
	
    private void showHint() {
    	Log.d(TAG, "showHint()");
    	m_Hint.setClickable(false);
    	m_MatchManager.setSolo(true);
    	m_MatchManager.clearPreClikedItemInfo();
    	clickEnable(false);
    	
    	for(int i = 0; i < MAX_COUNT; i++) {
    		m_ItemImages[i].setVisibility(View.VISIBLE);
    		m_Questions[i].setVisibility(View.GONE);
    	}
    	
    	pauseAnimation();
    	
    	m_Runnable = new Runnable() {
        	HashMap<Integer, String> rawItems 		= m_MatchManager.getItems();
        	HashMap<Integer, String> matchedItems 	= m_MatchManager.getMatchedItems();
        	Iterator<Integer> ri = rawItems.keySet().iterator();
        	Iterator<Integer> mi = matchedItems.keySet().iterator();
        	Integer parentViewId;
        	ViewGroup parentView;
			
			@Override
			public void run() {
				while(ri.hasNext()) {	// hide
					parentViewId = ri.next();
					parentView = (ViewGroup) findViewById(parentViewId);
					parentView.getChildAt(0).setVisibility(View.INVISIBLE);
					parentView.getChildAt(1).setVisibility(View.VISIBLE);
				}
				while(mi.hasNext()) {	// show
					parentViewId = mi.next();
					parentView = (ViewGroup) findViewById(parentViewId);
					parentView.getChildAt(0).setVisibility(View.VISIBLE);
					parentView.getChildAt(1).setVisibility(View.INVISIBLE);
				}
				clickEnable(true);
				resumeAnimation();
				m_Hint.setClickable(true);
				
			}
		};
		
		m_Handler.postDelayed(m_Runnable, HINT_TIME_DURATION);
	}


	/**
	 * pause timeflowAnimation
	 */
	private void pauseAnimation() {
		Log.d(TAG, "pauseAnimation()");
		
		Matrix transformationMatrix;
		Transformation outTransformation = new Transformation();
		float[] matrixValues = new float[9];
		long currentTime = AnimationUtils.currentAnimationTimeMillis();
		float transX;
		
		m_IsPause = true;
		
		m_TimeFlowAnimation.getTransformation(currentTime, outTransformation);
		transformationMatrix = outTransformation.getMatrix();
		transformationMatrix.getValues(matrixValues);
		
		transX = matrixValues[Matrix.MTRANS_X];
		
		m_TimeInterval = currentTime - m_TimeFlowAnimation.getStartTime();
		if(m_TimeInterval < 0)
			m_TimeInterval = 0;
		Log.d(TAG, "m_TimeInterval: " + m_TimeInterval);
		
		savePreViewLeftPosition((int) transX);
		m_TimeFrameImage.layout(m_LeftPosition,
								m_TimeFrameImage.getTop(), 
								m_LeftPosition + m_TimeFrameImage.getWidth(),
								m_TimeFrameImage.getBottom());
		m_TimeFrameImage.clearAnimation();
		m_TimeFlowAnimation = null;
	}
	
	private void savePreViewLeftPosition(int x) {
		m_LeftPosition = m_TimeFrameImage.getLeft() + x;
	}

	/**
	 * resume timeflowAnimation
	 */
	private void resumeAnimation() {
		Log.d(TAG, "resumeAnimation()");
		
		m_IsPause = false;
		
		setTimeAnimation(false);
		
		m_TimeFrameImage.startAnimation(m_TimeFlowAnimation);
	}

	/**
     * @param parentView
     * @param isToggle 
     */
    private synchronized void doRotation(ViewGroup parentView, boolean isToggle) {
    	Log.d(TAG, "doRatation()");
		int flag = 0;
		
		for(int i = 0; i < parentView.getChildCount(); i++) {
			if(parentView.getChildAt(i).getVisibility() == View.VISIBLE) {
//				parentView.getChildAt(i).setVisibility(View.GONE);
				flag = 1;
			}
			else {
//				parentView.getChildAt(i).setVisibility(View.VISIBLE);
				flag = -1;
			}
		}
		
		/**
		m_ClickedItemImage 	= (ImageView) parentView.getChildAt(0);
		m_ClickedQuestion 	= (ImageView) parentView.getChildAt(1);
		**/
		
		clickEnable(false);
        applyRotation(parentView, flag, 0, 90, isToggle);
	}

	/**
     * @param isEnable
     * true : click enable 
     * false: click disable
     */
    public void clickEnable(boolean isEnable) {
    	Log.d(TAG, "clickeEnable:" + isEnable);
		
    	if(isEnable) {	// change to clickable
    		for(int i = 0; i < MAX_COUNT; i++) {
    			m_Questions[i].setClickable(isEnable);
    			if(m_ItemImages[i].getTag() == (Integer)ITEM_DEFAULT)
    				m_ItemImages[i].setClickable(isEnable);
    		}
    	}
    	else {			// change to clickdisable
    		for(int i = 0; i < MAX_COUNT; i++) {
    			m_Questions[i].setClickable(isEnable);
    			m_ItemImages[i].setClickable(isEnable);
    		}
    	}
	}
    
    /**
     * @param preClickedItemId parentViewId
     * @param targetId parentViewId
     * set view's tag to ITEM_MATCHED.
     * if tag's value is ITEM_MATCHED which can't get click event.
     */
    private void clickDisable(int preClickedItemId, int targetId) {
    	Log.d(TAG, "clickDisable()");
    	View item1;
    	View item2;
    	
    	item1 = ((ViewGroup) findViewById(preClickedItemId)).getChildAt(0);
    	item2 = ((ViewGroup) findViewById(targetId)).getChildAt(0);
    	
    	item1.setTag(ITEM_MATCHED);
    	item2.setTag(ITEM_MATCHED);
    }
    
    /**
     * @param preClickedItemId parentViewId
     * @param targetId parentViewId
     * It toggles clicked items only if it dosen't match.
     */
    private void toggleClickedItems(final int preClickedItemId, final int targetId) {
    	Log.d(TAG, "toggleClickedItems()");
    	
        m_Handler.postDelayed(new Runnable() {
        	ViewGroup parent1 = (ViewGroup) findViewById(preClickedItemId);
        	ViewGroup parent2 = (ViewGroup) findViewById(targetId);

			public void run() {
				doRotation(parent1, true);
				doRotation(parent2, true);
			}
        }, TOGGLE_INTERVAL);
    }
    
    private int getParentId(int targetId) {
    	return ((View) findViewById(targetId).getParent()).getId();
    }


	/**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(ViewGroup parentView, int position, float start, float end, boolean isToggle) {
        // Find the center of the container
        final float centerX = parentView.getWidth() / 2.0f;
        final float centerY = parentView.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(ANI_ROTATION_TIME_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(parentView, position, isToggle));

        parentView.startAnimation(rotation);
    }	
    
    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;
        private ViewGroup mParentView;
        private boolean mIsToggle;

        private DisplayNextView(ViewGroup parentView, int position, boolean isToggle) {
            mPosition = position;
            mParentView = parentView;
            mIsToggle = isToggle;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
        	mParentView.post(new SwapViews(mParentView, mPosition, mIsToggle));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    } 
    
    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;
        private ViewGroup mParentView;
        private ViewHolder mViewHolder;
        private boolean mIsToggle;

        public SwapViews(ViewGroup parentView, int position, boolean isToggle) {
            mPosition = position;
            mParentView = parentView;
            mViewHolder = (ViewHolder)mParentView.getTag();
            mIsToggle = isToggle;
        }

        public void run() {
            final float centerX = mParentView.getWidth() / 2.0f;
            final float centerY = mParentView.getHeight() / 2.0f;
            Rotate3dAnimation rotation;
//            Log.d(TAG, "mPosition : " + mPosition);
            
            if (mPosition > -1) {
            	mViewHolder.getQuestionView().setVisibility(View.GONE);
            	mViewHolder.getItemView().setVisibility(View.VISIBLE);
            	mViewHolder.getItemView().requestFocus();

                rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false);
            } else {
            	mViewHolder.getItemView().setVisibility(View.GONE);
            	mViewHolder.getQuestionView().setVisibility(View.VISIBLE);
            	mViewHolder.getQuestionView().requestFocus();

                rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false);
            }

            rotation.setDuration(ANI_ROTATION_TIME_DURATION);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            
            rotation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation paramAnimation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation paramAnimation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation paramAnimation) {
					if(!mIsToggle)
						m_Handler.sendEmptyMessage(ANIMATION_ENDED);
					else
						m_Handler.sendEmptyMessage(TOGGLE_ENDED);
					
					Log.d(TAG, "Sound: " + m_SoundEffect.play(m_SoundEffectId, 1.0f, 1.0f, 0, 0, 1.0f));
				}
			});

            mParentView.startAnimation(rotation);
        }
    }
    
    /**
     * @author TickerBomb
     * This class is responsible for matching results.
     */
    public static class MatchManager {
    	private final int MAX = 8;
    	private boolean isSolo;
    	private boolean isMatched;
    	private boolean isAllMatched;
    	private boolean isTouchedSameView;
    	private int m_PreClickedItemId;	// parentViewId
    	private int m_CurClickedItemId;	// parentViewId
    	private static MatchManager mMatchManager;
    	
    	private HashMap<Integer, String> m_Items;	// key: parentViewId, value: child String Value
    	private HashMap<Integer, String> m_MatchedItems;
    	private HashMap<Integer, String> m_PreClickedItem;
    	
    	private MatchManager() {
    		Log.d(TAG, "MatchManager()");
    		m_Items 			= new HashMap<Integer, String>(MAX_COUNT);
    		m_MatchedItems 		= new HashMap<Integer, String>(MAX_COUNT);
    		m_PreClickedItem 	= new HashMap<Integer, String>();
    		isSolo				= true;
    		m_PreClickedItemId	= -1;
    		m_CurClickedItemId	= 0;
    	}
    	
    	public static MatchManager getInstance() {
    		if(mMatchManager == null) {
    			mMatchManager = new MatchManager();
    		}
    		return mMatchManager;
    	}
    	
    	public void release() {
    		m_Items.clear();
    		m_MatchedItems.clear();
    		m_PreClickedItem.clear();
    		isSolo				= true;
    		isAllMatched        = false;
    		m_PreClickedItemId	= -1;
    		m_CurClickedItemId	= 0;
    	}
    	
    	/**
    	 * @param targetId
    	 * if there isn't clicked item then set isSolo or !isSolo
    	 * and do check whether clicked items is matched or mismatched.
    	 */
    	public void clickedItem(int targetId) {
    		Log.d(TAG, "clickedItem() isSolo : " + isSolo);
    		Log.d(TAG, "clickedItem() key: " + targetId);
    		Log.d(TAG, "clickedItem() value: " + m_Items.get(targetId));
    		
    		if(m_PreClickedItemId == targetId) {
    			Log.e(TAG, "SAME");
    			setTouchedSameView(true);
    			//doNothing();
    		}
    		else {
    			if(isSolo) {
    				m_PreClickedItem.put(targetId, m_Items.get(targetId));
    				m_PreClickedItemId = targetId;
    			}
    			else {
    				if(isMatched(targetId)) {
    					moveItemsToMatched(targetId);
    					playSound(true);
    					checkAllMatched();
    				}
    				else {
    					playSound(false);
    				}
    			}
    		}
    	}

		public void setTouchedSameView(boolean isTouchedSameView) {
			Log.d(TAG, "touchedSameView()");
			this.isTouchedSameView = isTouchedSameView;
			clearPreClikedItemInfo();
		}
		
		public boolean isTouchedSameView() {
			return isTouchedSameView;
		}

		private void checkAllMatched() {
			if(m_MatchedItems.size() == MAX)
				isAllMatched = true;
			else
				isAllMatched = false;
		}
		
		private void moveItemsToMatched(int targetId) {
			Log.d(TAG, "moveItemsToMatched()");
			// Add MatchedItem
			m_MatchedItems.put(targetId, m_Items.get(targetId));
			m_MatchedItems.put(m_PreClickedItemId, m_Items.get(m_PreClickedItemId));
			// Remove MatcheItem
			m_Items.remove(targetId);
			m_Items.remove(m_PreClickedItemId);
		}

		/**
		 * @param targetId	target view's parent view id.
		 * @return true  : matched
		 * 		   false : dismatched
		 */
		private boolean isMatched(int targetId) {
			
			if(m_PreClickedItem.get(m_PreClickedItemId).equals(m_Items.get(targetId))) {
				isMatched = true;
			}
			else {
				isMatched = false;
			}
			
			return isMatched;
    	}
		
		public boolean isMatched() {
			return isMatched;
		}
		
		public int getPreClickedId() {
			if(m_PreClickedItemId == 0) {
				Log.e(TAG, "getPreClickedId: " + m_PreClickedItemId);
			}
			return m_PreClickedItemId;
		}
		
		public void setCurrentPviewId(int id) {
			m_CurClickedItemId = id;
		}
		
		public int getCurClickedId() {
			if(m_CurClickedItemId == 0) {
				Log.e(TAG, "m_CurClickedItemId: " + m_CurClickedItemId);
			}
			return m_CurClickedItemId;
		}
		
		public void clearPreClikedItemInfo() {
			Log.d(TAG, "clearPreClickedItemInfo()");
			m_PreClickedItem.clear();
			m_PreClickedItemId = -1;
		}
		
		public boolean isAllMatched() {
			return isAllMatched;
		}
		
		/**
		 * @return true: only one item was clicked.
		 * 		  false: two items were clicked.
		 */
		public boolean isSolo() {
			return isSolo;
		}
		
		public void setSolo(boolean isSolo) {
			this.isSolo = isSolo;
		}
		
		public HashMap<Integer, String> getItems() {
			return m_Items;
		}
		
		public HashMap<Integer, String> getMatchedItems() {
			return m_MatchedItems;
		}
    	
    	private void playSound(boolean b) {
    		Log.d(TAG, "playSound()");
			// TODO Auto-generated method stub
		}
    	
    	/**
    	 * @param key: target parent view's id
    	 * @param value: item's string value
    	 */
    	public void addItem(int key, String value) {
    		m_Items.put(key, value);
    	}
    }
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id) {
		case RETRY_DIALOG:
			dialog = setRetryDialog();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	private Dialog setRetryDialog() {
		Dialog dialog = new Dialog(MatchQuiz.this);

		dialog.setContentView(R.layout.retry_dialog);
		
		return dialog;
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume()");
		super.onResume();
		
		if(!m_QuizBGM.isPlaying()) 
			m_QuizBGM.start();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "onNewIntent()");
		
		releaseMemory();
		
		setContentView(R.layout.match_quiz);
		
		init();
		toggleImages();
		setItems();
		setTimeAnimation(true);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()");
		if(m_QuizBGM != null) {
			if(m_QuizBGM.isPlaying()) 
				m_QuizBGM.pause();
		}
	}
	
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG, "onBackPressed()");
		finish();
		Intent intent = new Intent(MatchQuiz.this, GameStatusActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
		releaseMemory();
		
		m_Handler 				= null;
		m_Runnable				= null;
		
		m_TimeFrameImage 		= null;
		m_TimeFrameImageEnd 	= null;
		m_TimeFrameAnimation 	= null;
		m_TimeFlowAnimation 	= null;
		
		System.gc();
	}
	
	private void releaseMemory() {
		Log.d(TAG, "releaseMemory()");
		
		if(m_QuizBGM.isPlaying()) 
			m_QuizBGM.release();
		
		m_SoundEffect.release();
		m_MatchManager.release();
		
		m_Handler.removeCallbacks(m_Runnable);
		
		m_ItemList				= null;
		
		m_ItemImages 			= null;	
		m_Questions				= null;
		m_Containers 			= null;	
		
		m_QuizBGM				= null; 
		m_SoundEffect			= null;
		m_MatchManager			= null;
		m_Res 					= null;
		
		m_ItemVector 			= null;
		
		m_PopupIntent 			= null;
		
	}
}
