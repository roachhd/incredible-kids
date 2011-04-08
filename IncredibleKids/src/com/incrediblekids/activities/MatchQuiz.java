/*
 * @(#) MatchQuiz.java 1.0, 2011. 3. 7.
 * 
 */
package com.incrediblekids.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.incrediblekids.activities.ResourceClass.Item;

public class MatchQuiz extends Activity implements View.OnClickListener {
	
	private final String TAG = "MatchQuiz";
	private final int MAX_COUNT = 8;
	
	private ResourceClass m_Res;
	private Vector<Item> m_ItemVector;
	
	private ImageView m_Hint;
	
	private ImageView[] m_ItemImages;
	private ImageView[] m_Questions;
	private ViewGroup[] m_Containers;
	private HashMap<Integer, Integer> m_RandomHashMap;
	
	/* FrameImage & Interpolator Animation */
	private ImageView			m_TimeFrameImage;
	private ImageView			m_TimeFrameImageEnd;
	private AnimationDrawable 	m_TimeFrameAnimation;
	private Animation			m_TimeFlowAnimation;
	
	/* MatchManger */
	private MatchManager m_MatchManager;
	
	
	/**
	 * using for animation end callback.
	 */
	private Handler m_Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what) {
			case ANIMATION_ENDED:
				clickEnable(true);
				analysisMatchResult();
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
		init();
		toggleImages();
		setItems();
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
					//						Toast.makeText(this, "Matched", Toast.LENGTH_SHORT).show();
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
			// TODO: make Popup
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		setTimeAnimation();
	}

	private void init() {
		Log.d(TAG, "init()");
		
		m_ItemImages 		= new ImageView[MAX_COUNT];
		m_Questions			= new ImageView[MAX_COUNT];
		m_Containers 		= new ViewGroup[MAX_COUNT];
		m_MatchManager		= new MatchManager();
		
		m_Res 				= ResourceClass.getInstance();
		m_ItemVector 		= m_Res.getvItems();
		
		int firstItemValue	= R.id.ivItems1;
		int questionValue	= R.id.ivQuestion1;
		int viewGroupValue	= R.id.flContainer1;
		
		for(int i = 0; i < MAX_COUNT; i++) {
			m_ItemImages[i]	= (ImageView)findViewById(firstItemValue); // �녍뚌��-_-;
			m_ItemImages[i].setOnClickListener(this);
			m_ItemImages[i].setTag(ITEM_DEFAULT);
			
			m_Questions[i]	= (ImageView)findViewById(questionValue);
			m_Questions[i].setOnClickListener(this);
			
			m_Containers[i]	= (ViewGroup)findViewById(viewGroupValue); // �녍뚌��-_-;
			m_Containers[i].setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
			m_Containers[i].setTag(new ViewHolder(m_ItemImages[i], m_Questions[i]));
			
			firstItemValue  = firstItemValue + 3;
			questionValue   = questionValue + 3;
			viewGroupValue  = viewGroupValue + 3;
		}
		
		m_Hint				= (ImageView)findViewById(R.id.ivHint);
		
		m_TimeFrameImage 	= (ImageView)findViewById(R.id.ivTimeFrame);
		m_TimeFrameImage.setBackgroundResource(R.drawable.frame_transition);
		
		m_TimeFrameImageEnd	= (ImageView)findViewById(R.id.ivTimeFrameEnd);
		
		m_Hint.setOnClickListener(this);
		
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
		
		makeRandomHashMap();
		clickEnable(false);
	}
	
	/**
	 * N �슿�쨩� �륅？쨉횁 쩔횄�횄징��뤒�Question 쩔횄�횄징��뫥��타�ㅲ궗짜타.
	 */
	private void toggleImages() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

			public void run() {
				for(int i = 0; i < MAX_COUNT; i++) {
					m_ItemImages[i].setVisibility(View.GONE);
					m_Questions[i].setVisibility(View.VISIBLE);
					
				}
				//Test
				m_TimeFrameImage.startAnimation(m_TimeFlowAnimation);
				clickEnable(true);
			}
        }, 1000);
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
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = m_RandomHashMap.get(key);
			
			if((flag % 2) == 0) {
				m_ItemImages[value].setImageResource(m_ItemVector.get(vectorNum).iCardImgId);
//				m_ItemImages[value].setImageResource(R.drawable.card_front);
				m_MatchManager.addItem(getParentId(m_ItemImages[value].getId()), m_ItemVector.get(vectorNum).strWordCharId);
				Log.d(TAG, "setItems() key: " +  getParentId(m_ItemImages[value].getId()));
				Log.d(TAG, "setItems() String: " +  m_ItemVector.get(vectorNum).strWordCharId);
			}
			else {
				m_ItemImages[value].setImageResource(m_ItemVector.get(vectorNum).iCardWordId);
//				m_ItemImages[value].setImageResource(R.drawable.card_word);
				m_MatchManager.addItem(getParentId(m_ItemImages[value].getId()), m_ItemVector.get(vectorNum).strWordCharId);
				Log.d(TAG, "setItems() key: " +  getParentId(m_ItemImages[value].getId()));
				Log.d(TAG, "setItems() String: " +  m_ItemVector.get(vectorNum).strWordCharId);
				vectorNum++;
			}
			flag++;
		}
	}
	
	/* Set TimeAnimation */
	private void setTimeAnimation() {
		View targetParent;
		float fromXDelta;
		float toXDelta;
		
		targetParent = (View) m_TimeFrameImage.getParent();
		fromXDelta 	 = m_TimeFrameImage.getLeft();
		toXDelta 	 = targetParent.getWidth() - m_TimeFrameImage.getWidth();
		
		m_TimeFrameAnimation = (AnimationDrawable)m_TimeFrameImage.getBackground();
		m_TimeFlowAnimation  = new TranslateAnimation(fromXDelta, toXDelta, 0.0f, 0.0f);
		
		m_TimeFlowAnimation.setDuration(50000);
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
				m_TimeFrameAnimation.stop();
				m_TimeFrameImage.setVisibility(View.INVISIBLE);
				m_TimeFrameImageEnd.setVisibility(View.VISIBLE);
			}
		});
	}
	
	/**
	 * Random HashMap 짧�쨘��<br />
	 * Item쩔짧 �횏�째짬���궱�짧횁첩횓쨉쨩짜타.
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
    	clickEnable(false);
    	
//    	m_TimeFlowAnimation.reset();
			
    	for(int i = 0; i < MAX_COUNT; i++) {
    		m_ItemImages[i].setVisibility(View.VISIBLE);
    		m_Questions[i].setVisibility(View.GONE);
    	}
    	
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
        	HashMap<Integer, String> rawItems 		= m_MatchManager.getItems();
        	HashMap<Integer, String> matchedItems 	= m_MatchManager.getMatchedItems();
        	Iterator<Integer> ri = rawItems.keySet().iterator();
        	Iterator<Integer> mi = matchedItems.keySet().iterator();
        	Integer parentViewId;
        	ViewGroup parentView;

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
			}
        }, 2000);
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
    private void toggleClickedItems(int preClickedItemId, int targetId) {
    	Log.d(TAG, "toggleClickedItems()");
    	ViewGroup parent1 = (ViewGroup) findViewById(preClickedItemId);
    	ViewGroup parent2 = (ViewGroup) findViewById(targetId);
    	
    	doRotation(parent1, true);
    	doRotation(parent2, true);
    	// TODO:
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
        rotation.setDuration(200);
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

            rotation.setDuration(200);
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
				}
			});

            mParentView.startAnimation(rotation);
        }
    }
    
    /**
     * @author TickerBomb
     * This class is responsible for matching results.
     */
    private class MatchManager {
    	private final int MAX = 8;
    	private boolean isSolo;
    	private boolean isMatched;
    	private boolean isAllMatched;
    	private boolean isTouchedSameView;
    	private int m_PreClickedItemId;	// parentViewId
    	private int m_CurClickedItemId;	// parentViewId
    	
    	private HashMap<Integer, String> m_Items;	// key: parentViewId, value: child String Value
    	private HashMap<Integer, String> m_MatchedItems;
    	private HashMap<Integer, String> m_PreClickedItem;
    	
    	private MatchManager() {
    		m_Items 			= new HashMap<Integer, String>(MAX_COUNT);
    		m_MatchedItems 		= new HashMap<Integer, String>(MAX_COUNT);
    		m_PreClickedItem 	= new HashMap<Integer, String>();
    		isSolo				= true;
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
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
