package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Intent;
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
	
	private int MODE 				= 0;
	
	private RelativeLayout m_MainTheme;
	private RelativeLayout m_IntroTheme;
	
	private ImageView m_FoodTheme;
	private ImageView m_ColorTheme;
	private ImageView m_ThingTheme;
	private ImageView m_NumberTheme;
	private ImageView m_AnimalTheme;
	
	private ImageView m_StudyMode;
	private ImageView m_GameMode;
	
	private ResourceClass m_Res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		
		m_GameMode		= (ImageView)findViewById(R.id.ivGame);
		m_StudyMode		= (ImageView)findViewById(R.id.ivStudy);
		
		m_AnimalTheme.setOnClickListener(this);
		m_NumberTheme.setOnClickListener(this);
		m_ThingTheme.setOnClickListener(this);
		m_ColorTheme.setOnClickListener(this);
		m_FoodTheme.setOnClickListener(this);
		m_GameMode.setOnClickListener(this);
		m_StudyMode.setOnClickListener(this);
		
		
		showIntro();
		
		m_Res = ResourceClass.getInstance();
		
		MODE = MODE_STUDY;	// default Mode
	}

	private void showIntro() {
		Handler handler = new Handler();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				m_IntroTheme.setVisibility(View.GONE);
				m_MainTheme.setVisibility(View.VISIBLE);
			}
		},  INTRO_TIME);
		
	}
	
	@Override
	public void onClick(View v) {
//		Intent intent = new Intent(MainTheme.this, PopupActivity.class);
		boolean isValid = true;
		
		//TODO: resource load
		switch(v.getId()) {
		case R.id.ivAnimal:
//			intent.putExtra(Const.ITEM_NAME, Const.THEME_ANIMAL);
			m_Res.setTheme(Const.THEME_ANIMAL);
			break;
			
		case R.id.ivNumber:
//			intent.putExtra(Const.ITEM_NAME, Const.THEME_NUMBER);
			m_Res.setTheme(Const.THEME_NUMBER);
			break;
			
		case R.id.ivThing:
//			intent.putExtra(Const.ITEM_NAME, Const.THEME_TOY);
			m_Res.setTheme(Const.THEME_TOY);
			break;
			
		case R.id.ivColor:
//			intent.putExtra(Const.ITEM_NAME, Const.THEME_COLOR);
			m_Res.setTheme(Const.THEME_COLOR);
			break;
			
		case R.id.ivFood:
//			intent.putExtra(Const.ITEM_NAME, Const.THEME_FOOD);
			m_Res.setTheme(Const.THEME_FOOD);
			break;
			
		case R.id.ivGame:
			MODE = MODE_GAME;
			isValid = false;
			break;
			
		case R.id.ivStudy:
			MODE = MODE_STUDY;
			isValid = false;
			break;
			
		default:
			Log.e(TAG," onClick()");
//			isValid = false;
		}
		
		if(isValid)
			launchActivity();
		
		
//		if(isValid) {
//			startActivityForResult(intent, Const.MODE_DIALOG_RESULT);
//		}
	}

	private void launchActivity() {
		Intent intent;
		
		if(MODE == MODE_STUDY) {
			intent = new Intent(MainTheme.this, PreviewWords.class);
		}
		else {
			intent = new Intent(MainTheme.this, GameStatusActivity.class);
		}
		
		if(intent != null)
			startActivity(intent);
	}

/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String theme = "";
		Intent intent = null;
		
		if(requestCode == Const.MODE_DIALOG_RESULT) {
			theme = data.getStringExtra(Const.ITEM_NAME);
			Log.d(TAG, "theme: " + theme);
			
			if(resultCode == RESULT_OK) { 				// Game 
				intent = new Intent(MainTheme.this, ThemeItemActivity.class);
				startActivity(intent);
			} 
			else if(resultCode == RESULT_CANCELED) {	// Study
				intent = new Intent(MainTheme.this, PreviewWords.class);
				startActivity(intent);
			}
		}
	}
*/

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	

}
