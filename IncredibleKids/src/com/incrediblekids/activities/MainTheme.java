package com.incrediblekids.activities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.incrediblekids.util.Const;

public class MainTheme extends Activity implements View.OnClickListener {
	
	private final String TAG = "MainTheme";
	private final long INTRO_TIME 	= 3000;
	private final int MODE_GAME 	= 10;
	private final int MODE_STUDY 	= 11;
	
	private int m_Mode 				= 0;
	private int m_CurrentTheme		= 0;
	private boolean m_IntroShow		= false;
	
	private RelativeLayout m_MainTheme;
	private RelativeLayout m_IntroTheme;
	private RelativeLayout m_rlThemeChar;
	
	private ImageView m_ThemeFood;
	private ImageView m_ThemeColor;
	private ImageView m_ThemeObject;
	private ImageView m_ThemeNumber;
	private ImageView m_ThemeAnimal;
	
	private ImageView m_Character;
	
	private HashMap<Integer, Integer> 	m_ThemeItemChar;
	private HashMap<ImageView, Integer> m_ThemeItemSelBg;
	
	private ImageButton m_ModeStudy;
	private ImageButton m_ModeGame;
	
	private ResourceClass m_Res;
	
//	private SharedPreferences m_ScorePreference;
	
	private Animation m_CharacterOutAni;
	private Animation m_CharacterInAni;
	
	/* BGM */
	private MediaPlayer m_ThemeBGM;
	
	/* Sound Effect */
	private SoundPool m_SoundEffect;
	private int	m_SoundEffectId;
	
	private Runnable m_IntroRunnable;
	
	public static final int HANDLER_MSG_ANIMATION_ENDED = 0;
	
	private Handler m_Handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what) {
			case HANDLER_MSG_ANIMATION_ENDED:
				showCharacterLayout();
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		
		setContentView(R.layout.main_theme);
		
		m_MainTheme 	= (RelativeLayout)findViewById(R.id.main_theme);
		m_IntroTheme 	= (RelativeLayout)findViewById(R.id.intro_theme);
		m_rlThemeChar	= (RelativeLayout)findViewById(R.id.themeCharacterLayout);
		
		m_ThemeAnimal 	= (ImageView)findViewById(R.id.ivThemeAnimal);
		m_ThemeNumber 	= (ImageView)findViewById(R.id.ivThemeNumber);
		m_ThemeObject 	= (ImageView)findViewById(R.id.ivThemeObject);
		m_ThemeColor 	= (ImageView)findViewById(R.id.ivThemeColor);
		m_ThemeFood 	= (ImageView)findViewById(R.id.ivThemeFood);
		
		m_Character		= (ImageView)findViewById(R.id.ivCharacter);
		
		m_ModeGame		= (ImageButton)findViewById(R.id.ivModeGame);
		m_ModeStudy		= (ImageButton)findViewById(R.id.ivModeStudy);
		
		m_ThemeBGM		= MediaPlayer.create(this, R.raw.theme_bgm); 
		m_SoundEffect	= new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		m_SoundEffectId	= m_SoundEffect.load(this, R.raw.mode_effect, 1);
		
		
		m_ThemeItemChar		= new HashMap<Integer, Integer>();
		m_ThemeItemSelBg	= new HashMap<ImageView, Integer>();
		
//		m_ScorePreference 	= getSharedPreferences(Const.PREFERNCE, 0);
		
		m_ThemeAnimal.setOnClickListener(this);
		m_ThemeNumber.setOnClickListener(this);
		m_ThemeObject.setOnClickListener(this);
		m_ThemeColor.setOnClickListener(this);
		m_ThemeFood.setOnClickListener(this);
		
		m_ModeGame.setOnClickListener(this);
		m_ModeStudy.setOnClickListener(this);
		
		
		m_ThemeBGM.setLooping(true);
		
		m_Res 	= ResourceClass.getInstance();
		
		showIntro();
		initThemeHashMap();
		setAnimation();
		
		setInitLayout();
	}
	


	/**
	 * set Init Theme and Menu
	 */
	private void setInitLayout() {
		m_Res.setTheme(Const.THEME_ANIMAL);
		updateThemeMenuView(m_CurrentTheme, R.id.ivThemeAnimal);
		m_CurrentTheme = R.id.ivThemeAnimal;
	}

	/**
	 * make ThemeItem Hashmap via View's ID and character's drawable resources
	 * make ThemeItemSelBg Hashmap via View and selected drawable images
	 */
	private void initThemeHashMap() {
		m_ThemeItemChar.put(R.id.ivThemeAnimal, R.drawable.theme_character_animal);
		m_ThemeItemChar.put(R.id.ivThemeNumber, R.drawable.theme_character_number);
		m_ThemeItemChar.put(R.id.ivThemeObject, R.drawable.theme_character_object);
		m_ThemeItemChar.put(R.id.ivThemeColor, R.drawable.theme_character_color);
		m_ThemeItemChar.put(R.id.ivThemeFood, R.drawable.theme_character_food);
		
		m_ThemeItemSelBg.put(m_ThemeAnimal, R.drawable.theme_item_animal_s);
		m_ThemeItemSelBg.put(m_ThemeNumber, R.drawable.theme_item_number_s);
		m_ThemeItemSelBg.put(m_ThemeObject, R.drawable.theme_item_object_s);
		m_ThemeItemSelBg.put(m_ThemeColor, R.drawable.theme_item_color_s);
		m_ThemeItemSelBg.put(m_ThemeFood, R.drawable.theme_item_food_s);
	}

    private void showIntro() {
		
		m_IntroRunnable = new Runnable() {
			
			@Override
			public void run() {
				m_IntroTheme.setVisibility(View.GONE);
				m_MainTheme.setVisibility(View.VISIBLE);
				m_ThemeBGM.start();
				m_IntroShow = true;
			}
		};
		
		m_Handler.postDelayed(m_IntroRunnable, INTRO_TIME);
	}
	
	@Override
	public void onClick(View v) {
		boolean isValid = false;
		int clickedTheme = -1;
		
		switch(v.getId()) {
		case R.id.ivThemeAnimal:
			m_Res.setTheme(Const.THEME_ANIMAL);
			break;
			
		case R.id.ivThemeNumber:
			m_Res.setTheme(Const.THEME_NUMBER);
			break;
			
		case R.id.ivThemeObject:
			m_Res.setTheme(Const.THEME_TOY);
			break;
			
		case R.id.ivThemeColor:
			m_Res.setTheme(Const.THEME_COLOR);
			break;
			
		case R.id.ivThemeFood:
			m_Res.setTheme(Const.THEME_FOOD);
			break;
			
		case R.id.ivModeGame:
			/**
		    if(m_Mode != MODE_GAME) {
		        m_Mode = MODE_GAME;
		        updatePlayMode(m_Mode, true);
		    }
		    **/
		    
		    updatePlayMode(m_Mode, true);
			isValid = true;
			break;
			
		case R.id.ivModeStudy:
			/**
		    if(m_Mode != MODE_STUDY) {
		        m_Mode = MODE_STUDY;
		        updatePlayMode(m_Mode, true);
		    }
		    **/
		    updatePlayMode(m_Mode, true);
			isValid = true;
			break;
			
		default:
			Log.e(TAG," onClick()");
		}
		
		if(v.getId() != R.id.ivModeGame && v.getId() != R.id.ivModeStudy) synchronized(this) {
			clickedTheme = v.getId();
			if(m_CurrentTheme != clickedTheme) {
				updateThemeMenuView(m_CurrentTheme, clickedTheme);
				
				m_CurrentTheme = clickedTheme;
				changeCharacterLayout(m_CurrentTheme);
			}
		}
		
		if(isValid)
			launchActivity();
	}

	/**
	 * @param currTheme	selected View's ID
	 */
	private void changeCharacterLayout(int currTheme) {
		
		if(m_rlThemeChar.getVisibility() == View.VISIBLE) {
			hideCharacterLayout();
			//showCharacterLayout via Handler
		}
		else {
			showCharacterLayout();
		}
	}


	private void showCharacterLayout() {
		int bgResource = 0;
		
		bgResource = m_ThemeItemChar.get(m_CurrentTheme);
		
		m_Character.setBackgroundResource(bgResource);
		m_rlThemeChar.startAnimation(m_CharacterInAni);
	}


	private void hideCharacterLayout() {
		m_rlThemeChar.startAnimation(m_CharacterOutAni);
	}
	
	private void setPlayModeEnable(boolean isEnable) {
		m_ModeGame.setEnabled(isEnable);
		m_ModeStudy.setEnabled(isEnable);
	}
	
	private void setAnimation() {
		m_CharacterOutAni 	= AnimationUtils.loadAnimation(MainTheme.this, R.anim.char_right_out);
		m_CharacterInAni 	= AnimationUtils.loadAnimation(MainTheme.this, R.anim.char_right_in);
		
		m_CharacterOutAni.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO:Disable TouchButton and frameAnimation
				setPlayModeEnable(false);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				m_rlThemeChar.setVisibility(View.INVISIBLE);
				m_Handler.sendEmptyMessage(HANDLER_MSG_ANIMATION_ENDED);
			}
		});
		
		m_CharacterInAni.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				m_rlThemeChar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO:enable TouchButton and frameAnimation
				setPlayModeEnable(true);
			}
		});
	}



	/**
	 * 이전 테마 이미지를 원래대로 바꾸고 선택된 테마를 선택된 이미지로 변경한다.
	 * @param prevTheme	이전 테마
	 * @param currTheme 현재 클릭된 테마
	 */
	private void updateThemeMenuView(int prevTheme, int currTheme) {
		Log.d(TAG, "updateThemeMenuView()");
		Log.d(TAG, "updateThemeMenuView(), prevTheme: " + prevTheme);
		Log.d(TAG, "updateThemeMenuView(), currTheme: " + currTheme);
		
		Drawable drawable;
		ImageView previousView;
		ImageView selectedView;
		
		previousView = (ImageView)findViewById(prevTheme);
		selectedView = (ImageView)findViewById(currTheme);
		
		if(previousView != null) {
			// restore normal button
			
			if (previousView.getTag() instanceof Drawable) {
				drawable = (Drawable) previousView.getTag();
				previousView.setBackgroundDrawable(drawable);
			}
			else {
				Log.e(TAG, "ooooops!!!!");
				return;
			}
		} else {	// init
			Log.e(TAG, "previousView : null");
		}
		
		if(selectedView != null) {
			if(selectedView.getTag() == null) {
				drawable = selectedView.getBackground();
				selectedView.setTag(drawable);
			}

			// change to selected button view
			selectedView.setBackgroundResource(m_ThemeItemSelBg.get(selectedView));
		}
	}

	/**
	 * change play mode image
	 * @param mMode
	 * @param bSound	true : play sound false : don't play sound
	 */
	private void updatePlayMode(int mMode, boolean bSound) {
		
		/**
	    if(mMode == MODE_STUDY) {
	        m_ModeStudy.setBackgroundResource(R.drawable.btn_study_n);
	        m_ModeGame.setBackgroundResource(R.drawable.btn_play_n);
	    }
	    else {
	        m_ModeGame.setBackgroundResource(R.drawable.btn_play_n);
	        m_ModeStudy.setBackgroundResource(R.drawable.btn_study_n);
	    }
	    **/
	    
	    if(bSound)
	    	Log.d(TAG, "Sound: " + m_SoundEffect.play(m_SoundEffectId, 1.0f, 1.0f, 0, 0, 1.0f));
    }

    private void launchActivity() {
		Intent intent;
		
		if(m_Mode == MODE_STUDY) {
			intent = new Intent(MainTheme.this, PreviewWords.class);
		}
		else {
			intent = new Intent(MainTheme.this, GameStatusActivity.class);
		}
		
		if(intent != null) {
			startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
	}
    
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()");
		if(m_ThemeBGM != null) {
			if(m_ThemeBGM.isPlaying()) 
				m_ThemeBGM.pause();
		}
		m_Handler.removeCallbacks(m_IntroRunnable);
		m_Handler.removeMessages(HANDLER_MSG_ANIMATION_ENDED);
		m_IntroRunnable = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
		
		updatePlayMode(m_Mode, false);
		
		if(m_IntroShow == true && !m_ThemeBGM.isPlaying()) 
			m_ThemeBGM.start();
		
		if(!m_IntroShow && m_IntroRunnable == null) {
			if(m_IntroTheme.getVisibility() == View.VISIBLE) {
				m_IntroTheme.setVisibility(View.GONE);
				m_MainTheme.setVisibility(View.VISIBLE);
				if(!m_ThemeBGM.isPlaying())
					m_ThemeBGM.start();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
		
		if(m_ThemeBGM.isPlaying()) 
			m_ThemeBGM.release();
		
		m_ThemeItemChar.clear();
		m_ThemeItemSelBg.clear();
		m_SoundEffect.release();
		m_SoundEffect 	= null;
		
		m_MainTheme 	= null;
		m_IntroTheme 	= null;
		
		m_ThemeAnimal 	= null;
		m_ThemeNumber 	= null;
		m_ThemeObject 	= null;
		m_ThemeColor 	= null;
		m_ThemeFood 	= null;
		
		m_ModeGame		= null;
		m_ModeStudy		= null;
		
		m_ThemeBGM		= null; 
		m_IntroRunnable	= null;
		
		m_ThemeItemChar		= null;
		m_ThemeItemSelBg	= null;
//		m_ScorePreference 	= null;
		
		System.gc();
	}
}
