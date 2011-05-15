package com.incrediblekids.activities;

import java.io.IOException;

import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.incrediblekids.util.Const;

public class GameStatusActivity extends Activity {
	
	private final String TAG = "GameStatusActivity";
	private Intent mIntent = null;
	private ImageView ivLevel_1 = null;
	private ImageView ivLevel_2 = null;
	private ImageView ivLevel_3 = null;
	private ImageView ivLevel_4 = null;
	
	/* BGM */
	private MediaPlayer m_ThemeBGM = null;
	private ResourceClass m_ResourceClass = null;
	
	private int curLevel = 0;
	private String m_CurTheme = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_status);
	
		final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		final AnimationSet animSet = new AnimationSet(true); 
		
		Animation sel_anim = AnimationUtils.loadAnimation(this, R.anim.sel_anim);
		animSet.addAnimation(sel_anim);
		
		m_ResourceClass = ResourceClass.getInstance();
		m_ResourceClass.vItems.size();		
		m_CurTheme = m_ResourceClass.getCurrentTheme();
		
		//BGM Set
		setBGM();
		
		//set BG Image
		setBGImage();
	
		ivLevel_1 = (ImageView)findViewById(R.id.level_1);
		ivLevel_2 = (ImageView)findViewById(R.id.level_2);
		ivLevel_3 = (ImageView)findViewById(R.id.level_3);
		ivLevel_4 = (ImageView)findViewById(R.id.level_4);
		
		setImageResource(m_ResourceClass);

		mIntent = new Intent(GameStatusActivity.this, ThemeItemActivity.class);		
		ivLevel_1.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				
				ivLevel_1.startAnimation(animSet);
				animSet.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationEnd(Animation animation) {
						ivLevel_1.setVisibility(View.INVISIBLE);
						mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_1);
						startActivity(mIntent);						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {						
					}

					@Override
					public void onAnimationStart(Animation animation) {						
					}					
				});				
			}
		});
		ivLevel_2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (curLevel >= 1){
					ivLevel_2.startAnimation(animSet);
					animSet.setAnimationListener(new AnimationListener(){

						@Override
						public void onAnimationEnd(Animation animation) {
							ivLevel_2.setVisibility(View.INVISIBLE);
							mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_2);
							startActivity(mIntent);							
						}

						@Override
						public void onAnimationRepeat(Animation animation) {						
						}

						@Override
						public void onAnimationStart(Animation animation) {						
						}					
					});	
					
				}else{
					ivLevel_2.startAnimation(shake);
				}
			}
			
		});
		ivLevel_3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (curLevel >= 2){
					ivLevel_3.startAnimation(animSet);
					animSet.setAnimationListener(new AnimationListener(){

						@Override
						public void onAnimationEnd(Animation animation) {
							ivLevel_3.setVisibility(View.INVISIBLE);
							mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_3);
							startActivity(mIntent);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {						
						}

						@Override
						public void onAnimationStart(Animation animation) {						
						}					
					});	
					
				}else{
					ivLevel_3.startAnimation(shake);
				}
			}
			
		});
		ivLevel_4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (curLevel >= 3){
					ivLevel_4.startAnimation(animSet);
					animSet.setAnimationListener(new AnimationListener(){

						@Override
						public void onAnimationEnd(Animation animation) {
							ivLevel_4.setVisibility(View.INVISIBLE);
							mIntent.putExtra(Const.CUR_LEVEL, Const.LEVEL_4);
							startActivity(mIntent);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {						
						}

						@Override
						public void onAnimationStart(Animation animation) {						
						}					
					});	
				}else{
					ivLevel_4.startAnimation(shake);
				}
			}
			
		});
	}
	
	private void setBGM(){
		try {
			
			AssetFileDescriptor fd = getAssets().openFd("mfx/theme_animal.mp3");
			m_ThemeBGM = new MediaPlayer();	
			m_ThemeBGM.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
			m_ThemeBGM.setLooping(true);
			m_ThemeBGM.prepare();
			m_ThemeBGM.start();
			fd.close();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	void setBGImage(){

		LinearLayout layout = (LinearLayout)findViewById(R.layout.game_status);
		
		if(m_CurTheme.equals(Const.THEME_ANIMAL)){
			layout.setBackgroundResource(R.drawable.bg_animal_play);
		}
		else if(m_CurTheme.equals(Const.THEME_COLOR)){
			layout.setBackgroundResource(R.drawable.bg_color_play);
		}
		else if(m_CurTheme.equals(Const.THEME_FOOD)){
			layout.setBackgroundResource(R.drawable.bg_food_play);
		}
		else if(m_CurTheme.equals(Const.THEME_NUMBER)){
			layout.setBackgroundResource(R.drawable.bg_number_play);
		}
		else if(m_CurTheme.equals(Const.THEME_TOY)){
			layout.setBackgroundResource(R.drawable.bg_toy_play);
		}
	}
	
	private void setImageResource(ResourceClass m_ResourceClass){
		
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(Const.PREFERNCE, Context.MODE_WORLD_READABLE);
		if(settings == null) {
		    Log.e(TAG, "updateScore() null");
		}
		
		if (m_CurTheme.equals(Const.THEME_ANIMAL)) 
		{			
			curLevel = settings.getInt(Const.THEME_ANIMAL, 0);
			Log.e("WOORAM","curLevel:"+curLevel);
			ivLevel_1.setVisibility(View.VISIBLE);
			ivLevel_2.setVisibility(View.VISIBLE);
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
			
		}else if(m_CurTheme.equals(Const.THEME_TOY)){
			curLevel = settings.getInt(Const.THEME_TOY, 0);
			ivLevel_1.setVisibility(View.VISIBLE);
			ivLevel_2.setVisibility(View.VISIBLE);
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
		}else if(m_CurTheme.equals(Const.THEME_FOOD)){
			curLevel = settings.getInt(Const.THEME_FOOD, 0);
			ivLevel_1.setVisibility(View.VISIBLE);
			ivLevel_2.setVisibility(View.VISIBLE);
			ivLevel_1.setImageResource(R.drawable.level_food_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_big);
			ivLevel_3.setVisibility(View.GONE);
			ivLevel_4.setVisibility(View.GONE);
			if(curLevel == 1 || curLevel == 2)
				ivLevel_2.setImageResource(R.drawable.level_food_2);
			
		}else if(m_CurTheme.equals(Const.THEME_NUMBER)){
			curLevel = settings.getInt(Const.THEME_NUMBER, 0);
			ivLevel_1.setVisibility(View.VISIBLE);
			ivLevel_2.setVisibility(View.VISIBLE);
			ivLevel_1.setImageResource(R.drawable.level_number_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_big);
			ivLevel_3.setVisibility(View.GONE);
			ivLevel_4.setVisibility(View.GONE);
			if(curLevel == 1 || curLevel == 2)
				ivLevel_2.setImageResource(R.drawable.level_number_2);
		}else if(m_CurTheme.equals(Const.THEME_COLOR)){
			curLevel = settings.getInt(Const.THEME_COLOR, 0);
			ivLevel_1.setVisibility(View.VISIBLE);
			ivLevel_2.setVisibility(View.VISIBLE);
			ivLevel_1.setImageResource(R.drawable.level_color_1);
			ivLevel_2.setImageResource(R.drawable.level_locked_big);
			ivLevel_3.setVisibility(View.GONE);
			ivLevel_4.setVisibility(View.GONE);
			if(curLevel == 1 || curLevel == 2)
				ivLevel_2.setImageResource(R.drawable.level_color_2);
		}
		Log.e(TAG, "setImageResource() curLevel:"+curLevel);
	}

	@Override
	protected void onResume() {
		Log.e(TAG, "onResume()");
		ResourceClass m_ResourceClass = ResourceClass.getInstance();
		setImageResource(m_ResourceClass);
		if(m_ThemeBGM != null && !m_ThemeBGM.isPlaying())
			m_ThemeBGM.start();
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
		if(m_ThemeBGM != null && m_ThemeBGM.isPlaying())
			m_ThemeBGM.pause();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		if(m_ThemeBGM != null && m_ThemeBGM.isPlaying()) 
			m_ThemeBGM.release();
		super.onDestroy();
	}
}
