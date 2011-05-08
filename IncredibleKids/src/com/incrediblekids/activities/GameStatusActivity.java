package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.incrediblekids.util.Const;

public class GameStatusActivity extends Activity {
	
	private final String TAG = "GameStatusActivity";
	private Intent mIntent = null;
	private ImageView ivLevel_1 = null;
	private ImageView ivLevel_2 = null;
	private ImageView ivLevel_3 = null;
	private ImageView ivLevel_4 = null;
	
	private int curLevel = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game_status);
		
		ResourceClass m_ResourceClass = ResourceClass.getInstance();
		m_ResourceClass.vItems.size();		
	
		ivLevel_1 = (ImageView)findViewById(R.id.level_1);
		ivLevel_2 = (ImageView)findViewById(R.id.level_2);
		ivLevel_3 = (ImageView)findViewById(R.id.level_3);
		ivLevel_4 = (ImageView)findViewById(R.id.level_4);
		
		setImageResource(m_ResourceClass);

		mIntent = new Intent(GameStatusActivity.this, ThemeItemActivity.class);		
		ivLevel_1.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_1);
				startActivity(mIntent);
			}
			
		});
		ivLevel_2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_2);
				startActivity(mIntent);
			}
			
		});
		ivLevel_3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_3);
				startActivity(mIntent);
			}
			
		});
		ivLevel_4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_4);
				startActivity(mIntent);
			}
			
		});
	}
	
	private void setImageResource(ResourceClass m_ResourceClass){
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(Const.PREFERNCE, Context.MODE_WORLD_READABLE);
		if(settings == null) {
		    Log.e(TAG, "updateScore() null");
		}
		
		if (m_ResourceClass.getCurrentTheme().equals(Const.THEME_ANIMAL)) 
		{			
			curLevel = settings.getInt(Const.THEME_ANIMAL, 0);
			Log.e("WOORAM","curLevel:"+curLevel);
			ivLevel_3.setVisibility(View.VISIBLE);
			ivLevel_4.setVisibility(View.VISIBLE);
			ivLevel_1.setImageResource(R.drawable.level_animal_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_small);
			ivLevel_3.setImageResource(R.drawable.level_locked_small);
			ivLevel_4.setImageResource(R.drawable.level_locked_small);
			
			if(curLevel == 1)
				ivLevel_2.setImageResource(R.drawable.level_animal_2);
			if(curLevel == 2){
				ivLevel_2.setImageResource(R.drawable.level_animal_2);
				ivLevel_3.setImageResource(R.drawable.level_animal_3);
			}
			if(curLevel == 3 || curLevel == 4){
				ivLevel_2.setImageResource(R.drawable.level_animal_2);
				ivLevel_3.setImageResource(R.drawable.level_animal_3);
				ivLevel_4.setImageResource(R.drawable.level_animal_4);
			}
			
		}else if(m_ResourceClass.getCurrentTheme().equals(Const.THEME_TOY)){
			curLevel = settings.getInt(Const.THEME_TOY, 0);
			ivLevel_3.setVisibility(View.VISIBLE);
			ivLevel_4.setVisibility(View.VISIBLE);
			ivLevel_1.setImageResource(R.drawable.level_toy_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_small);
			ivLevel_3.setImageResource(R.drawable.level_locked_small);
			ivLevel_4.setImageResource(R.drawable.level_locked_small);
			
			if(curLevel == 1)
				ivLevel_2.setImageResource(R.drawable.level_toy_2);
			if(curLevel == 2){
				ivLevel_2.setImageResource(R.drawable.level_toy_2);
				ivLevel_3.setImageResource(R.drawable.level_toy_3);
			}
			if(curLevel == 3 || curLevel == 4){
				ivLevel_2.setImageResource(R.drawable.level_toy_2);
				ivLevel_3.setImageResource(R.drawable.level_toy_3);
				ivLevel_4.setImageResource(R.drawable.level_toy_4);
			}
		}else if(m_ResourceClass.getCurrentTheme().equals(Const.THEME_FOOD)){
			curLevel = settings.getInt(Const.THEME_FOOD, 0);
			ivLevel_1.setImageResource(R.drawable.level_food_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_big);
			ivLevel_3.setVisibility(View.GONE);
			ivLevel_4.setVisibility(View.GONE);
			if(curLevel == 1 || curLevel == 2)
				ivLevel_2.setImageResource(R.drawable.level_food_2);
			
		}else if(m_ResourceClass.getCurrentTheme().equals(Const.THEME_NUMBER)){
			curLevel = settings.getInt(Const.THEME_NUMBER, 0);
			ivLevel_1.setImageResource(R.drawable.level_number_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_big);
			ivLevel_3.setVisibility(View.GONE);
			ivLevel_4.setVisibility(View.GONE);
			if(curLevel == 1 || curLevel == 2)
				ivLevel_2.setImageResource(R.drawable.level_number_2);
		}else if(m_ResourceClass.getCurrentTheme().equals(Const.THEME_COLOR)){
			curLevel = settings.getInt(Const.THEME_COLOR, 0);
			ivLevel_1.setImageResource(R.drawable.level_color_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_big);
			ivLevel_3.setVisibility(View.GONE);
			ivLevel_4.setVisibility(View.GONE);
			if(curLevel == 1 || curLevel == 2)
				ivLevel_2.setImageResource(R.drawable.level_color_2);
		}
	}

	@Override
	protected void onResume() {
		ResourceClass m_ResourceClass = ResourceClass.getInstance();
		setImageResource(m_ResourceClass);
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
/*		ResourceClass m_ResourceClass = ResourceClass.getInstance();
		setImageResource(m_ResourceClass);*/
		super.onNewIntent(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
