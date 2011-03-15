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
	
	/* 개별 이미지 */
	private ViewGroup	m_ClickedViewGroup;
	private ImageView	m_ClickedQuestion;
	private ImageView	m_ClickedItemImage;
	private ImageView	m_SoundBtnImage;
	
	

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
		
		m_Res 				= ResourceClass.getInstance();
		m_ItemVector 		= m_Res.getvItems();
		
		int firstItemValue	= R.id.ivItems1;
		int questionValue	= R.id.ivQuestion1;
		int viewGroupValue	= R.id.flContainer1;
		
		for(int i = 0; i < MAX_COUNT; i++) {
			m_ItemImages[i]	= (ImageView)findViewById(firstItemValue); // 편법 -_-;
			m_ItemImages[i].setOnClickListener(this);
			
			m_Questions[i]	= (ImageView)findViewById(questionValue);
			m_Questions[i].setOnClickListener(this);
			
			m_Containers[i]	= (ViewGroup)findViewById(viewGroupValue); // 편법 -_-;
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
	 * N 초 후 모든 이미지를 Question 이미지로 바꾼다.
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
	 * ItemImage와 WordImage를 섞는다.
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
			}
			else {
//				m_ItemImages[value].setImageResource(m_ItemVector.get(vectorNum).iWordImgId);
				m_ItemImages[value].setImageResource(R.drawable.card_word);
				vectorNum++;
			}
			flag++;
		}
		Log.d(TAG, "flag: " + flag);
		Log.d(TAG, "vectorNum: " + vectorNum);
	}
	
	/**
	 * Random HashMap 생성 <br />
	 * Item을 배치할 때 사용된다.
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
     * 참고 : View에 아이디 부여 가능하다.
     */
    private class MatchManager {
    	private boolean isClickable;
    	private int clickedImagesCnt;
    	
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
