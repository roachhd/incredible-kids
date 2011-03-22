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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.incrediblekids.activities.ResourceClass.Item;

public class MatchQuiz extends Activity implements View.OnClickListener {
	
	private final String TAG = "MatchQuiz";
	
	private final int MAX_COUNT = 8;
	
	private ResourceClass m_Res;
	private Vector<Item> m_ItemVector;
	
	private ImageView[] m_ItemImages;
	private ImageView[] m_Questions;
	private ViewGroup[] m_Containers;
	private HashMap<Integer, Integer> m_RandomHashMap;
	
	private Bitmap 		m_bitSoundBtnLeft, m_bitSoundBtnRight;
	
	/* ∞≥∫∞ ¿ÃπÃ¡ˆ */
	private ViewGroup	m_ClickedViewGroup;
	private ImageView	m_ClickedQuestion;
	private ImageView	m_ClickedItemImage;
	private ImageView	m_SoundBtnImage;
	
	private MatchManager m_MatchManager;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.match_quiz);
		init();
		toggleImages();
		setItems();
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
			m_ItemImages[i]	= (ImageView)findViewById(firstItemValue); // ∆Ìπ˝ -_-;
			m_ItemImages[i].setOnClickListener(this);
			
			m_Questions[i]	= (ImageView)findViewById(questionValue);
			m_Questions[i].setOnClickListener(this);
			
			m_Containers[i]	= (ViewGroup)findViewById(viewGroupValue); // ∆Ìπ˝ -_-;
			m_Containers[i].setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
			
			firstItemValue  = firstItemValue + 3;
			questionValue   = questionValue + 3;
			viewGroupValue  = viewGroupValue + 3;
		}
		
		/* Get Sound Button Image Resource */
		BitmapDrawable bd 	= (BitmapDrawable)getResources().getDrawable(R.drawable.btn_sound);
		Bitmap bit 			= bd.getBitmap();
		Log.d(TAG, "bit.getWidtd(): " + bit.getWidth());
		Log.d(TAG, "bit.getHeight(): " + bit.getHeight());
		m_bitSoundBtnLeft 	= Bitmap.createBitmap(bit, 0, 0, bit.getWidth()/2, bit.getHeight());
		m_bitSoundBtnRight 	= Bitmap.createBitmap(bit, bit.getWidth()/2, 0, bit.getWidth()/2, bit.getHeight());
		
		m_SoundBtnImage 	= (ImageView)findViewById(R.id.ivSound);
		m_SoundBtnImage.setImageBitmap(m_bitSoundBtnLeft);
		
		makeRandomHashMap();
	}
	
	/**
	 * N √  »ƒ ∏µÁ ¿ÃπÃ¡ˆ∏¶ Question ¿ÃπÃ¡ˆ∑Œ πŸ≤€¥Ÿ.
	 */
	private void toggleImages() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

			public void run() {
				for(int i = 0; i < MAX_COUNT; i++) {
					m_ItemImages[i].setVisibility(View.GONE);
					m_Questions[i].setVisibility(View.VISIBLE);
				}
			}
        }, 1000);
	}
	
	/**
	 * ItemImageøÕ WordImage∏¶ ºØ¥¬¥Ÿ.
	 */
	private void setItems() {
		Log.d(TAG, "setItems()");
		Iterator<Integer> ii = m_RandomHashMap.keySet().iterator();
		int vectorNum = 0;
		int flag = 0;
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = m_RandomHashMap.get(key);
			Log.d(TAG, "value : " + value);
			
			if((flag % 2) == 0) {
//				m_ItemImages[value].setImageResource(m_ItemVector.get(vectorNum).iItemImgId);
				m_ItemImages[value].setImageResource(R.drawable.card_front);
				m_MatchManager.addItem(m_ItemVector.get(vectorNum).iItemImgId, m_ItemVector.get(vectorNum).strWordCharId);
			}
			else {
//				m_ItemImages[value].setImageResource(m_ItemVector.get(vectorNum).iWordImgId);
				m_ItemImages[value].setImageResource(R.drawable.card_word);
				m_MatchManager.addItem(m_ItemVector.get(vectorNum).iWordImgId, m_ItemVector.get(vectorNum).strWordCharId);
				vectorNum++;
			}
			flag++;
		}
		Log.d(TAG, "flag: " + flag);
		Log.d(TAG, "vectorNum: " + vectorNum);
	}
	
	/**
	 * Random HashMap ª˝º∫ <br />
	 * Item¿ª πËƒ°«“ ∂ß ªÁøÎµ»¥Ÿ.
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
		m_ClickedViewGroup = (ViewGroup) v.getParent();
		int cnt = m_ClickedViewGroup.getChildCount();
		int flag = 0;
		for(int i = 0; i < cnt; i++) {
			if(m_ClickedViewGroup.getChildAt(i).getVisibility() == View.VISIBLE) {
//				m_ClickedViewGroup.getChildAt(i).setVisibility(View.GONE);
				flag = 1;
			}
			else {
//				m_ClickedViewGroup.getChildAt(i).setVisibility(View.VISIBLE);
				flag = -1;
			}
		}
		
		m_ClickedItemImage 	= (ImageView) m_ClickedViewGroup.getChildAt(0);
		m_ClickedQuestion 	= (ImageView) m_ClickedViewGroup.getChildAt(1);
		
        applyRotation(flag, 0, 90);
	}
	
    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(int position, float start, float end) {
        // Find the center of the container
        final float centerX = m_ClickedViewGroup.getWidth() / 2.0f;
        final float centerY = m_ClickedViewGroup.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        m_ClickedViewGroup.startAnimation(rotation);
    }	
    
    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
        	Log.d(TAG, "onAnimationEnd()");
        	m_ClickedViewGroup.post(new SwapViews(mPosition));
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

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = m_ClickedViewGroup.getWidth() / 2.0f;
            final float centerY = m_ClickedViewGroup.getHeight() / 2.0f;
            Rotate3dAnimation rotation;
            Log.d(TAG, "mPosition : " + mPosition);
            
            if (mPosition > -1) {
                m_ClickedQuestion.setVisibility(View.GONE);
                m_ClickedItemImage.setVisibility(View.VISIBLE);
                m_ClickedItemImage.requestFocus();

                rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false);
            } else {
            	m_ClickedItemImage.setVisibility(View.GONE);
            	m_ClickedQuestion.setVisibility(View.VISIBLE);
            	m_ClickedQuestion.requestFocus();

                rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false);
            }

            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            m_ClickedViewGroup.startAnimation(rotation);
        }
    }
    
    /**
     * @author TickerBomb
     * This class is responsible for managing clickable images and Matching result
     * ¬¸∞Ì : Viewø° æ∆¿Ãµ ∫Œø© ∞°¥…«œ¥Ÿ.
     */
    private class MatchManager {
    	private boolean isClickable;
    	private boolean isSolo;
    	private int m_PreClickedItemId;
    	private HashMap<Integer, String> m_PreClickedItem;
    	private HashMap<Integer, String> m_Items;
    	private HashMap<Integer, String> m_MatchedItems;
    	
    	public MatchManager() {
    		m_Items 			= new HashMap<Integer, String>(MAX_COUNT);
    		m_MatchedItems 		= new HashMap<Integer, String>(MAX_COUNT);
    		m_PreClickedItem 	= new HashMap<Integer, String>();
    		isClickable			= false;
    		isSolo				= true;
    		
    		/*
    		for(int i = 0; i < items.length; i++) {
    			m_Items.add(items[i]) ;
    		}
    		
    		for(int i = 0; i < question.length; i++) {
    			m_Items.add(question[i]) ;
    		}
    		*/
    	}
    	
    	public void clickedItem(int targetId) {
//    		setEnableClickItems(true);
    		if(isSolo) {
    			m_PreClickedItem.put(targetId, m_Items.get(targetId));
    			m_PreClickedItemId = targetId;
    			isSolo = false;
    		}
    		else {
    			if(isMatched(targetId)) {
    				//TODO :setting Clickable
    				moveItemsToMatched(targetId);
    				playSound(true);
    			}
    			else {
    				//TODO :setting Clickable
    				togglePreviousItem();
    				playSound(false);
    			}
    			m_PreClickedItem.clear();
    			isSolo = true;
    		}
    	}

		private void togglePreviousItem() {
			// TODO Auto-generated method stub
			
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

		private boolean isMatched(int targetId) {
			Log.d(TAG, "isMatched()");
			if(m_PreClickedItem.equals(m_Items.get(targetId))) {
				return true;
			}
			else {
				return false;
			}
    	}
    	
    	private void playSound(boolean b) {
			// TODO Auto-generated method stub
		}
    	
    	public void addItem(int key, String value) {
    		m_Items.put(key, value);
    	}

		/**
		 * @param enable true: user can click items, false: can't click items
		 */
    	public void setEnableClickItems(boolean enable) {
    		Log.d(TAG, "setEnableClickItems()");
    		Iterator<Integer> itr = m_Items.keySet().iterator();
    		ImageView iv;
    		int imageViewId;
    		while(itr.hasNext()) {
    			imageViewId = (Integer)itr.next();
    			iv = (ImageView)findViewById(imageViewId);
    			iv.setClickable(enable);
    		}
    	}
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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
