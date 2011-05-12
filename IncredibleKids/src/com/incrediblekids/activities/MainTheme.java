package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.incrediblekids.util.Const;

public class MainTheme extends Activity implements View.OnClickListener {
	
	private final String TAG = "MainTheme";
	private final long INTRO_TIME 	= 3000;
	private final int MODE_GAME 	= 10;
	private final int MODE_STUDY 	= 11;
	
	private final int STAR_FOUR[]   = {R.drawable.star_4_0, R.drawable.star_4_1, R.drawable.star_4_2, R.drawable.star_4_3, R.drawable.star_4_4};
	private final int STAR_TWO[]    = {R.drawable.star_2_0, R.drawable.star_2_1, R.drawable.star_2_2};
	
	private int m_Mode 				= 0;
	private boolean m_IntroShow		= false;
	
	private RelativeLayout m_MainTheme;
	private RelativeLayout m_IntroTheme;
	
	private ImageView m_FoodTheme;
	private ImageView m_ColorTheme;
	private ImageView m_ThingTheme;
	private ImageView m_NumberTheme;
	private ImageView m_AnimalTheme;
	
	private ImageView m_FoodScore;
	private ImageView m_ColorScore;
	private ImageView m_ThingScore;
	private ImageView m_NumberScore;
	private ImageView m_AnimalScore;
	
	private ImageView m_StudyMode;
	private ImageView m_GameMode;
	
	private ResourceClass m_Res;
	
	private SharedPreferences m_ScorePreference;
	private SharedPreferences m_ModePreference;
	
	/* BGM */
	private MediaPlayer m_ThemeBGM;
	
	/* Sound Effect */
	private SoundPool m_SoundEffect;
	private int	m_SoundEffectId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		
		setContentView(R.layout.main_theme);
		
		m_MainTheme 	= (RelativeLayout)findViewById(R.id.main_theme);
		m_IntroTheme 	= (RelativeLayout)findViewById(R.id.intro_theme);
		
		m_AnimalTheme 	= (ImageView)findViewById(R.id.ivAnimal);
		m_NumberTheme 	= (ImageView)findViewById(R.id.ivNumber);
		m_ThingTheme 	= (ImageView)findViewById(R.id.ivThing);
		m_ColorTheme 	= (ImageView)findViewById(R.id.ivColor);
		m_FoodTheme 	= (ImageView)findViewById(R.id.ivFood);
		
		m_AnimalScore   = (ImageView)findViewById(R.id.ivAnimalScore);
		m_NumberScore   = (ImageView)findViewById(R.id.ivNumberScore);
		m_ThingScore    = (ImageView)findViewById(R.id.ivThingScore);
		m_ColorScore    = (ImageView)findViewById(R.id.ivColorScore);
		m_FoodScore     = (ImageView)findViewById(R.id.ivFoodScore);
		
		m_GameMode		= (ImageView)findViewById(R.id.ivGame);
		m_StudyMode		= (ImageView)findViewById(R.id.ivStudy);
		
		m_ThemeBGM		= MediaPlayer.create(this, R.raw.theme_bgm); 
		m_SoundEffect	= new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		m_SoundEffectId	= m_SoundEffect.load(this, R.raw.mode_effect, 1);
		
		m_ScorePreference 	= getSharedPreferences(Const.PREFERNCE, 0);
		m_ModePreference 	= getSharedPreferences(Const.PREFERNCE_MODE, 0);
		
		m_AnimalTheme.setOnClickListener(this);
		m_NumberTheme.setOnClickListener(this);
		m_ThingTheme.setOnClickListener(this);
		m_ColorTheme.setOnClickListener(this);
		m_FoodTheme.setOnClickListener(this);
		m_GameMode.setOnClickListener(this);
		m_StudyMode.setOnClickListener(this);
		
		m_ThemeBGM.setLooping(true);
		
		m_Res 	= ResourceClass.getInstance();
		m_Mode 	= getPlayMode();	// default Mode
		
		showIntro();
	}
	
	/**
	 * @return PLAY MODE from preference
	 */
	private int getPlayMode() {
		int mode = m_ModePreference.getInt(Const.PLAY_MODE, MODE_STUDY);
		
		return mode;
	}

	/**
	 * update Theme's score
	 */
	private void updateScore() {
		Log.d(TAG, "updateScore()");
	    
		// Restore preferences
		if(m_ScorePreference == null) {
		    Log.e(TAG, "updateScore() null");
		}
		
		int animal_val    = m_ScorePreference.getInt(Const.THEME_ANIMAL, 0);
		int toy_val       = m_ScorePreference.getInt(Const.THEME_TOY, 0);
		int food_val      = m_ScorePreference.getInt(Const.THEME_FOOD, 0);
		int number_val    = m_ScorePreference.getInt(Const.THEME_NUMBER, 0);
		int color_val     = m_ScorePreference.getInt(Const.THEME_COLOR, 0);
		
		m_AnimalScore.setBackgroundResource(STAR_FOUR[animal_val]);
		m_ThingScore.setBackgroundResource(STAR_FOUR[toy_val]);
		
		m_FoodScore.setBackgroundResource(STAR_TWO[food_val]);
		m_NumberScore.setBackgroundResource(STAR_TWO[number_val]);
		m_ColorScore.setBackgroundResource(STAR_TWO[color_val]);
    }

    private void showIntro() {
		Handler handler = new Handler();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				m_IntroTheme.setVisibility(View.GONE);
				m_MainTheme.setVisibility(View.VISIBLE);
				m_ThemeBGM.start();
				m_IntroShow = true;
			}
		},  INTRO_TIME);
	}
	
	@Override
	public void onClick(View v) {
//		Intent intent = new Intent(MainTheme.this, PopupActivity.class);
		boolean isValid = true;
		
		switch(v.getId()) {
		case R.id.ivAnimal:
			m_Res.setTheme(Const.THEME_ANIMAL);
			break;
			
		case R.id.ivNumber:
			m_Res.setTheme(Const.THEME_NUMBER);
			break;
			
		case R.id.ivThing:
			m_Res.setTheme(Const.THEME_TOY);
			break;
			
		case R.id.ivColor:
			m_Res.setTheme(Const.THEME_COLOR);
			break;
			
		case R.id.ivFood:
			m_Res.setTheme(Const.THEME_FOOD);
			break;
			
		case R.id.ivGame:
		    if(m_Mode != MODE_GAME) {
		        m_Mode = MODE_GAME;
		        updatePlayMode(m_Mode, true);
		    }
			isValid = false;
			break;
			
		case R.id.ivStudy:
		    if(m_Mode != MODE_STUDY) {
		        m_Mode = MODE_STUDY;
		        updatePlayMode(m_Mode, true);
		    }
			isValid = false;
			break;
			
		default:
			Log.e(TAG," onClick()");
		}
		
		if(isValid)
			launchActivity();
	}

	/**
	 * change play mode image
	 * @param mMode
	 * @param bSound	true : play sound false : don't play sound
	 */
	private void updatePlayMode(int mMode, boolean bSound) {
	    if(mMode == MODE_STUDY) {
	        m_StudyMode.setBackgroundResource(R.drawable.btn_study);
	        m_GameMode.setBackgroundResource(R.drawable.btn_play_d);
	    }
	    else {
	        m_GameMode.setBackgroundResource(R.drawable.btn_play);
	        m_StudyMode.setBackgroundResource(R.drawable.btn_study_d);
	    }
	    
	    SharedPreferences.Editor editor = m_ModePreference.edit();
	    editor.putInt(Const.PLAY_MODE, mMode);
	    // Commit the edits!
	    editor.commit();
	    
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
		
		if(intent != null)
			startActivity(intent);
	}
    
	@Override
	protected void onPause() {
		super.onPause();
		if(m_ThemeBGM != null) {
			if(m_ThemeBGM.isPlaying()) 
				m_ThemeBGM.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
		
		updateScore();
		updatePlayMode(m_Mode, false);
		
		if(m_IntroShow == true && !m_ThemeBGM.isPlaying()) 
			m_ThemeBGM.start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(m_ThemeBGM.isPlaying()) 
			m_ThemeBGM.release();
		
		m_SoundEffect.release();
		m_SoundEffect 	= null;
		
		m_MainTheme 	= null;
		m_IntroTheme 	= null;
		
		m_AnimalTheme 	= null;
		m_NumberTheme 	= null;
		m_ThingTheme 	= null;
		m_ColorTheme 	= null;
		m_FoodTheme 	= null;
		
		m_AnimalScore   = null;
		m_NumberScore   = null;
		m_ThingScore    = null;
		m_ColorScore    = null;
		m_FoodScore     = null;
		
		m_GameMode		= null;
		m_StudyMode		= null;
		
		m_ThemeBGM		= null; 
		
		m_ScorePreference 	= null;
		m_ModePreference 	= null;
		
		System.gc();
	}
}
