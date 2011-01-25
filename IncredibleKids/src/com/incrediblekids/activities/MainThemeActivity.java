package com.incrediblekids.activities;

import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.incrediblekids.util.Const;

public class MainThemeActivity extends BaseGameActivity{
	
	private final String TAG = "MainThemeActivity";
	
	private Camera m_Camera;
	private ResourceClass res;
	private Music m_Music;
	
	private Scene m_Scene;
	
	private Texture m_AnimalTexture;
	private TiledTextureRegion m_AnimalTextureRegion;
	private AnimatedSprite m_AnimalAnimSprite;

	private Texture m_ToyTexture;
	private TiledTextureRegion m_ToyTextureRegion;
	private AnimatedSprite m_ToyAnimSprite;
	
	private Texture m_NumberTexture;
	private TiledTextureRegion m_NumberTextureRegion;
	private AnimatedSprite m_NumberAnimSprite;
	
	private Texture m_FruitTexture;
	private TiledTextureRegion m_FruitTextureRegion;
	private AnimatedSprite m_FruitAnimSprite;
	
	private Texture m_BGTexture;
	private TextureRegion m_BGTextureRegion;
	private Sprite m_BGSprite;
	
	//Game mode select popup (study or play game)
	private CameraScene m_GameModeScene;
	private Texture m_GameModeTexture;
	private TextureRegion m_GameModeTextureRegion;
	private Texture m_GameSelTexture;
	private TextureRegion m_GameSelTextureRegion;
	
	private int m_CameraWidth;
	private int m_CameraHeight;


	@Override
	protected void onResume() {
		Log.e(TAG, "onResume()");
		
		if(m_Scene != null){
			registTouchArea();
		}
		
		if(m_Music != null && !m_Music.isPlaying()) {
			m_Music.seekTo(0);
			m_Music.play();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if(m_Music != null && m_Music.isPlaying()) {
			m_Music.pause();
		}
		super.onPause();
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Engine onLoadEngine() {
		m_CameraWidth = getLCDWidth();
		m_CameraHeight = getLCDHeight();
        res = ResourceClass.getInstance();
        this.m_Camera = new Camera(0, 0, m_CameraWidth, m_CameraHeight);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(m_CameraWidth, m_CameraHeight), this.m_Camera).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {
		Log.e(TAG, "onLoadResources()");
		MusicFactory.setAssetBasePath("mfx/");		
		try {
			this.m_Music = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "theme_bgm.mp3");
			this.m_Music.setLooping(true);
			this.m_Music.setVolume(0.7f);
			this.m_Music.play();
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
		
		//Load Box
		this.m_AnimalTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_AnimalTextureRegion = TextureRegionFactory.createTiledFromResource(this.m_AnimalTexture, this, R.drawable.theme_animal_tile, 0, 0, 2, 1);

		//Load Box
		this.m_ToyTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_ToyTextureRegion = TextureRegionFactory.createTiledFromResource(this.m_ToyTexture, this, R.drawable.box, 0, 0, 1, 1);

		//Load Box
		this.m_NumberTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_NumberTextureRegion = TextureRegionFactory.createTiledFromResource(this.m_NumberTexture, this, R.drawable.box, 0, 0, 1, 1);

		//Load Box
		this.m_FruitTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_FruitTextureRegion = TextureRegionFactory.createTiledFromResource(this.m_FruitTexture, this, R.drawable.box, 0, 0, 1, 1);

		//Load Box
		this.m_BGTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_BGTextureRegion = TextureRegionFactory.createFromResource(this.m_BGTexture, this, R.drawable.theme_bg, 0, 0);
		
		//Game mode sel popup
		this.m_GameModeTexture = new Texture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		this.m_GameModeTextureRegion = TextureRegionFactory.createFromResource(this.m_GameModeTexture, this, R.drawable.type_sel_popup,0,0);
		this.m_GameSelTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_GameSelTextureRegion = TextureRegionFactory.createFromResource(this.m_GameSelTexture, this, R.drawable.type_sel_touch_area,0,0);
		
		this.mEngine.getTextureManager().loadTexture(this.m_AnimalTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_ToyTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_NumberTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_FruitTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_BGTexture);
		
		this.mEngine.getTextureManager().loadTexture(this.m_GameModeTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_GameSelTexture);
	}

	private void composeGameModeScene(){

		final int offset = this.m_GameModeTextureRegion.getWidth()/25;
		
		final int width = this.m_GameModeTextureRegion.getWidth();
		final int height = this.m_GameModeTextureRegion.getHeight();
		
		final int x = m_CameraWidth / 2 - width / 2;
		final int y = m_CameraHeight / 2 - height / 2;
		
		Log.e(TAG, "width="+width+" height="+height);
		
		final Sprite gameModeBGSprite = new Sprite(x , y, this.m_GameModeTextureRegion);
		
		final Sprite modeStudySprite = new Sprite(x + offset, y , this.m_GameSelTextureRegion){
			
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Log.e(TAG, "onAreaTouched");
				Intent intent;
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					m_Scene.clearChildScene();
					intent = new Intent(MainThemeActivity.this, PreviewWords.class);
					startActivity(intent);
				}
				return true;
			}						
		};
		
		final Sprite modeGameSprite = new Sprite(x + offset + m_GameModeTextureRegion.getWidth()/2, y , this.m_GameSelTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Log.e(TAG, "onAreaTouched");
				Intent intent;
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					m_Scene.clearChildScene();
					intent = new Intent(MainThemeActivity.this, ThemeItemActivity.class);
					startActivity(intent);
				}
				return true;
			}	
		};		
		this.m_GameModeScene.getTopLayer().addEntity(gameModeBGSprite);
		this.m_GameModeScene.getTopLayer().addEntity(modeGameSprite);
		this.m_GameModeScene.getTopLayer().addEntity(modeStudySprite);
		
		this.m_GameModeScene.registerTouchArea(modeStudySprite);
		this.m_GameModeScene.registerTouchArea(modeGameSprite);
	}
	
	@Override
	public Scene onLoadScene() {
		Log.e(TAG, "onLoadScene()");
		this.mEngine.registerUpdateHandler(new FPSLogger());

		//Make retry scene
		this.m_GameModeScene = new CameraScene(1, this.m_Camera);
		this.composeGameModeScene();
		this.m_GameModeScene.setBackgroundEnabled(false);
		
		//Make scene
		this.m_Scene = new Scene(1);
		this.m_BGSprite = new Sprite(0,0,this.m_BGTextureRegion);
		this.m_Scene.setBackground(new SpriteBackground(m_BGSprite));//new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		this.m_AnimalAnimSprite = new AnimatedSprite(10, 10, this.m_AnimalTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					res.setTheme(Const.THEME_ANIMAL);
					m_Scene.clearTouchAreas();
					m_Scene.setChildScene(m_GameModeScene, false, true, true);					
				}
				return true;
			}
		};
		this.m_AnimalAnimSprite.animate(800);
		this.m_ToyAnimSprite = new AnimatedSprite(m_CameraWidth - m_ToyTextureRegion.getWidth() - 10, 10, this.m_ToyTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					res.setTheme(Const.THEME_ANIMAL);
					m_Scene.clearTouchAreas();
					m_Scene.setChildScene(m_GameModeScene, false, true, true);	
				}
				return true;
			}
		};
		
		this.m_NumberAnimSprite = new AnimatedSprite(10, m_CameraHeight - m_NumberTextureRegion.getHeight() - 10, this.m_NumberTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					res.setTheme(Const.THEME_ANIMAL);
					m_Scene.clearTouchAreas();
					m_Scene.setChildScene(m_GameModeScene, false, true, true);	
				}
				return true;
			}
		};
		
		this.m_FruitAnimSprite = new AnimatedSprite(m_CameraWidth - m_ToyTextureRegion.getWidth() - 10, m_CameraHeight - m_NumberTextureRegion.getHeight() - 10, this.m_FruitTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					res.setTheme(Const.THEME_ANIMAL);
					m_Scene.clearTouchAreas();
					m_Scene.setChildScene(m_GameModeScene, false, true, true);	
				}
				return true;
			}
		};
		
		m_Scene.getTopLayer().addEntity(m_AnimalAnimSprite);
		m_Scene.getTopLayer().addEntity(m_ToyAnimSprite);
		m_Scene.getTopLayer().addEntity(m_NumberAnimSprite);
		m_Scene.getTopLayer().addEntity(m_FruitAnimSprite);
		
		registTouchArea();
		return m_Scene;
	}
	
	@Override
	public void onBackPressed() {
		if (m_Scene.hasChildScene()){
			m_Scene.clearChildScene();
			registTouchArea();
		}else{
			super.onBackPressed();
		}
	}
	
	private void registTouchArea(){
		m_Scene.registerTouchArea(m_AnimalAnimSprite);
		m_Scene.registerTouchArea(m_ToyAnimSprite);
		m_Scene.registerTouchArea(m_NumberAnimSprite);
		m_Scene.registerTouchArea(m_FruitAnimSprite);
	}
	
	private int getLCDWidth() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		return width;
	}

	private int getLCDHeight() {
		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		return height;
	}
}