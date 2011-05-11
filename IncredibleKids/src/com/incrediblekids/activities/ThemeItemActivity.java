package com.incrediblekids.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.MoveModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ease.EaseElasticOut;
import org.anddev.andengine.entity.shape.modifier.ease.EaseExponentialOut;
import org.anddev.andengine.entity.shape.modifier.ease.EaseLinear;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import com.incrediblekids.util.AlphabetSprite;
import com.incrediblekids.util.Const;
import com.incrediblekids.util.Item;

/**
 * @author Nicolas Gramlich
 */
public class ThemeItemActivity extends BaseGameActivity implements AnimationListener{
	// ===========================================================
	// Constants
	// ===========================================================

	public int CAMERA_WIDTH;
	public int CAMERA_HEIGHT;

	public final static int CENTER_OFFSET = 70;	//OFFSET for collision detect: larger is less sensitive
	public final static int BASE_LAYER = 0;		//Base layer for non-changable sprite
	public final static int ENTITIES_LAYER = 1;	//entiti layer for changable sprite

	public final static int SOUND_ON = 0;
	public final static int SOUND_OFF = 1;

	public final static int ITEM_IMG_FIRST = 0;
	public final static int ITEM_IMG_SECOND = 1;

	public final static int MENU_RESET = 0;
	public final static int MENU_QUIT = MENU_RESET + 1;
	public final static String TAG = "ThemeItemActivity";
	public final static char EMPTY_ALPHABET = '0';

	public final static int ITEM_NUM_PER_STAGE = 5;
	// ===========================================================
	// Fields
	// ===========================================================

	private Camera m_Camera;

	//Background image
	private Texture m_BackgroundTexture;
	private TextureRegion m_BackgroundTextureRegion;
	private Sprite m_BackgroundSprite;

	//Empty Boxes to fill alphabet.
	private Texture  m_BoxTexture;
	private TiledTextureRegion m_BoxTextureRegion;
	private AlphabetSprite [] m_arrBoxSprite;
	private int m_iCurrentCollideBoxIdx;

	//Pass
	private Texture m_PassTexture;	
	private TextureRegion m_PassTextureRegion;
	private Sprite m_PassSprite;

	//Pass
	private Texture m_FailTexture;
	private TextureRegion m_FailTextureRegion;
	private Sprite m_FailSprite;

	//Alphabets
	private String m_strAlphabet;
	private Texture [] m_arrAlphabet;
	private TiledTextureRegion [] m_arrAlphabetTexture;
	private AlphabetSprite [] m_arrAlphabetSprite;
	private AlphabetSprite m_CurrentTouchedAlphabetSprite;

	//Theme item Sprite
	private Texture  m_ItemTexture;
	private AnimatedSprite m_Item;	
	private TiledTextureRegion m_ItemTextureRegion;
	private int m_iCurrentItemNum;

	//Help Button Sprite
	private Sprite m_Help;
	private Texture  m_HelpTexture;
	private TextureRegion m_HelpTextureRegion;

	//Skip Button Sprite
	private Sprite m_SkipSprite;
	private Texture  m_SkipTexture;
	private TextureRegion m_SkipTextureRegion;
	
	//Loading Sprite
	private Sprite m_LoadingSprite;
	private Texture m_LoadingTexture;
	private TextureRegion m_LoadingTextureRegion;

	//Pause
	private Scene m_playScene;

	private Boolean m_bSoundOn;

	//Darken bg
	private Texture m_DarkenTexture;
	private TextureRegion m_DarkenTextureRegion;
	private Sprite m_DarkenSprite;

	//Retry 
	private CameraScene m_RetryScene;
	private Texture m_RetryTexture;
	private TextureRegion m_RetryTextureRegion;
	private Texture m_RetryOkTexture;
	private TextureRegion m_RetryOkTextureRegion;
	private Texture m_RetryCancelTexture;
	private TextureRegion m_RetryCancelTextureRegion;

	//Background Music and sound
	private Music m_Music;
	private Sound m_ItemSound;
	private Sound m_DropToBoxSound;
	private Sound m_HelpSound;
	private Sound m_FailToDropSound;
	private Sound [] m_AlphabetSound;

	private Random randomX;
	private Random randomY;

	private ResourceClass res;
	private Vector<Item> m_ItemVector;
	private ArrayList <Item> m_quizItemList;
	private ArrayList<Point> m_RandomPoint;

	private boolean m_bFirstTouch = true;
	private int m_currentLevel;
	private ResourceClass m_ResourceClass;
	
	private String m_CurTheme;
	
	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {

		Intent intent = getIntent();
		m_currentLevel = intent.getIntExtra(Const.CUR_LEVEL, 0);
		Log.e("WOORAM", "onCreate() m_currentLevel:"+m_currentLevel); 
		super.onCreate(pSavedInstanceState);
	}

	@Override
	public Engine onLoadEngine() {	
		
		m_ResourceClass = ResourceClass.getInstance();	
		m_CurTheme = m_ResourceClass.getCurrentTheme();		
		
		m_quizItemList = new ArrayList<Item>();

		this.m_iCurrentItemNum = (m_currentLevel) * 5;
		this.m_bSoundOn = true;

		this.randomX = new Random();
		this.randomY = new Random();

		DisplayMetrics dm = new DisplayMetrics();
		Display display = getWindowManager().getDefaultDisplay();
		display.getMetrics(dm);

		Log.e(TAG, "getLCDWidth():"+getLCDWidth());
		Log.e(TAG, "getLCDHeight():"+getLCDHeight());
		Log.e(TAG, "densityDpi:"+dm.densityDpi);

		if (dm.densityDpi == DisplayMetrics.DENSITY_HIGH){
			this.CAMERA_WIDTH = 800;
			this.CAMERA_HEIGHT = 480;
		}else if (dm.densityDpi == DisplayMetrics.DENSITY_MEDIUM){
			this.CAMERA_WIDTH = 480;
			this.CAMERA_HEIGHT = 320;
		}

		this.res = ResourceClass.getInstance();
		this.m_ItemVector = res.getvItems();
		Log.e("WOORAM", "m_iCurrentItemNum:" + m_iCurrentItemNum);
		this.m_strAlphabet = this.m_ItemVector.get(m_iCurrentItemNum).strWordCharId;

		this.m_iCurrentCollideBoxIdx = 0;
		this.m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new FillResolutionPolicy(), this.m_Camera).setNeedsMusic(true).setNeedsSound(true));
	}
	//FillResolutionPolicy
	@Override
	public void onLoadResources() {
		Log.e(TAG, "onLoadResources()");

		//Load Background
		this.m_BackgroundTexture = new Texture(2048, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		m_LoadingTexture = new Texture(1024,1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		m_LoadingTextureRegion = TextureRegionFactory.createFromResource(this.m_LoadingTexture, this, R.drawable.loading_sprite, 0, 0);

		MusicFactory.setAssetBasePath("mfx/");		
		try {
			this.m_Music = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "theme_animal.mp3");
			this.m_Music.setLooping(true);
			this.m_Music.setVolume(0.4f);
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
		
		if(m_CurTheme.equals(Const.THEME_ANIMAL)){
			this.m_BackgroundTextureRegion = TextureRegionFactory.createFromResource(this.m_BackgroundTexture, this, R.drawable.bg_animal_play, 0, 0);
		}
		else if(m_CurTheme.equals(Const.THEME_COLOR)){
			this.m_BackgroundTextureRegion = TextureRegionFactory.createFromResource(this.m_BackgroundTexture, this, R.drawable.bg_color_play, 0, 0);
		}
		else if(m_CurTheme.equals(Const.THEME_FOOD)){
			this.m_BackgroundTextureRegion = TextureRegionFactory.createFromResource(this.m_BackgroundTexture, this, R.drawable.bg_food_play, 0, 0);
		}
		else if(m_CurTheme.equals(Const.THEME_NUMBER)){
			this.m_BackgroundTextureRegion = TextureRegionFactory.createFromResource(this.m_BackgroundTexture, this, R.drawable.bg_number_play, 0, 0);
		}
		else if(m_CurTheme.equals(Const.THEME_TOY)){
			this.m_BackgroundTextureRegion = TextureRegionFactory.createFromResource(this.m_BackgroundTexture, this, R.drawable.bg_toy_play, 0, 0);
		}
		this.m_BackgroundSprite = new Sprite(0,0,this.m_BackgroundTextureRegion);
		this.mEngine.getTextureManager().loadTexture(this.m_BackgroundTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_LoadingTexture);
	}
	
	

	public void myLoadResources(){
		Log.e(TAG, "myLoadResources()");
		
		m_playScene.setBackground(new SpriteBackground(m_BackgroundSprite));
		this.m_HelpTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_HelpTextureRegion = TextureRegionFactory.createFromResource(this.m_HelpTexture, this, R.drawable.btn_hint , 0, 0);
		this.m_Help = new Sprite(m_HelpTextureRegion.getWidth()/4, m_HelpTextureRegion.getHeight()/4, this.m_HelpTextureRegion){

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Log.e(TAG, "onAreaTouched");
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){

					if (m_bFirstTouch == true)
						return true;
 
					//play sound
					if (m_bSoundOn == true)
						m_HelpSound.play();

					for (int i=0; i < m_arrAlphabetSprite.length; i++){

						//Wrong alphabet is filled in box
						if(m_arrBoxSprite[i].bFilled && !m_arrBoxSprite[i].bCorrect){

							m_arrAlphabetSprite[m_arrBoxSprite[i].filledAlphabetIndex].addShapeModifier(new MoveModifier(1,
									m_arrBoxSprite[i].getX(), m_RandomPoint.get(m_arrBoxSprite[i].filledAlphabetIndex).x,
									m_arrBoxSprite[i].getY(), m_RandomPoint.get(m_arrBoxSprite[i].filledAlphabetIndex).y,
									EaseExponentialOut.getInstance()));
							m_arrAlphabetSprite[m_arrBoxSprite[i].filledAlphabetIndex].setCurrentTileIndex(0);
							m_arrBoxSprite[i].bFilled = false;
							m_arrBoxSprite[i].bCorrect = false;
							m_arrBoxSprite[i].filledAlphabetIndex = -1;
							m_arrBoxSprite[i].alphabetContainer = EMPTY_ALPHABET;
						}

						if(!m_arrBoxSprite[i].bFilled){
							Log.e(TAG, "help work!!");
							float boxX = m_arrBoxSprite[i].getX();
							float boxY = m_arrBoxSprite[i].getY();
							float boxWidth = m_arrBoxSprite[i].getWidth();
							float boxHeight = m_arrBoxSprite[i].getHeight();
							m_arrAlphabetSprite[i].addShapeModifier(new MoveModifier(2,
									m_arrAlphabetSprite[i].getX(), boxX + (boxWidth/2 - m_arrAlphabetSprite[i].getWidth()/2),
									m_arrAlphabetSprite[i].getY(), boxY + (boxHeight/2 - m_arrAlphabetSprite[i].getHeight()/2),
									EaseElasticOut.getInstance()));
							m_arrAlphabetSprite[i].setCurrentTileIndex(1);
							for (int j=0; j < m_arrBoxSprite.length; j++){
								if(m_arrBoxSprite[j].filledAlphabetIndex == i){
									m_arrBoxSprite[j].bFilled = false;
									m_arrBoxSprite[j].bCorrect = false;
									m_arrBoxSprite[j].filledAlphabetIndex = -1;
									m_arrBoxSprite[i].alphabetContainer = EMPTY_ALPHABET;
								}
							}
							
							//m_arrAlphabetSprite[i].
							m_arrBoxSprite[i].bFilled = true;
							m_arrBoxSprite[i].bCorrect = true;
							m_arrBoxSprite[i].filledAlphabetIndex = i;
							m_arrBoxSprite[i].alphabetContainer = m_arrAlphabetSprite[i].alphabet;

							//reset screen to next item when user clear the stage
							if(isAllBoxesFilled(m_arrBoxSprite)){
								if(isStageCleared(m_arrBoxSprite)){
									if (m_iCurrentItemNum < m_ItemVector.size()-1){
										m_iCurrentItemNum++;
										m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;
									}else{
										m_iCurrentItemNum = 0;
										m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;
									}
									m_playScene.clearTouchAreas();
									shakeAndResetSprite(m_Item);
								}
							}
							m_CurrentTouchedAlphabetSprite = null;
							break;
						}
					}
					return true;
				}
				return false;
			}
		};
		m_playScene.getLayer(BASE_LAYER).addEntity(m_Help);

		//Skip 		
		this.m_SkipTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_SkipTextureRegion = TextureRegionFactory.createFromResource(m_SkipTexture, this, R.drawable.btn_skip, 0, 0);

		this.m_SkipSprite = new Sprite(CAMERA_WIDTH - m_SkipTextureRegion.getWidth() - m_SkipTextureRegion.getWidth()/4,
				m_SkipTextureRegion.getHeight()/4, this.m_SkipTextureRegion){
			@Override 
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){

					if (m_iCurrentItemNum < m_ItemVector.size()-1){
						m_iCurrentItemNum++;
						m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;
					}else{
						m_iCurrentItemNum++;
					}
					m_playScene.clearTouchAreas();			 
					resetScreen();
					return true;
				}
				return false;
			}
		};
		m_playScene.getLayer(BASE_LAYER).addEntity(m_SkipSprite);	

		this.mEngine.getTextureManager().loadTexture(this.m_HelpTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_SkipTexture);

		SoundFactory.setAssetBasePath("mfx/");

		try {
			this.m_DropToBoxSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "drop_to_box.ogg");//m_strAlphabet+".mp3");
			this.m_DropToBoxSound.setVolume(0.1f);

			this.m_HelpSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "help_drop.ogg");//m_strAlphabet+".mp3");
			this.m_HelpSound.setVolume(0.4f);

			this.m_FailToDropSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "fail_to_drop.ogg");
			this.m_FailToDropSound.setVolume(1.0f);

		} catch (final IOException e) {
			Debug.e("Error", e);
		}

/*		if(m_Music != null && !m_Music.isPlaying()) {
			Log.e(TAG, "Music start play()");
			m_Music.play();
		}*/

		m_DarkenTextureRegion = TextureRegionFactory.createFromResource(this.m_BackgroundTexture, this, R.drawable.darken_bg, 800, 0);

		//Load Box
		this.m_BoxTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_BoxTextureRegion = TextureRegionFactory.createTiledFromResource(this.m_BoxTexture, this, R.drawable.box, 0, 0, 1, 1);

		this.m_ItemTexture = new Texture(1024, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		//Retry popup texture
		this.m_RetryTexture = new Texture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		this.m_RetryOkTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		this.m_RetryCancelTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	

		this.m_RetryTextureRegion = TextureRegionFactory.createFromResource(this.m_RetryTexture, this, R.drawable.retry_popup_bg,0,0);
		this.m_RetryOkTextureRegion = TextureRegionFactory.createFromResource(this.m_RetryOkTexture, this, R.drawable.retry_ok_btn,0,0);
		this.m_RetryCancelTextureRegion = TextureRegionFactory.createFromResource(this.m_RetryCancelTexture, this, R.drawable.retry_no_btn,0,0);


		//Load pass texture
		this.m_PassTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_PassTextureRegion = TextureRegionFactory.createFromResource(this.m_PassTexture, this, R.drawable.pass_128, 0, 0);

		//Load fail texture
		this.m_FailTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_FailTextureRegion = TextureRegionFactory.createFromResource(this.m_FailTexture, this, R.drawable.fail_128, 0, 0);
	}

	private ArrayList <Point> getAreaArray(){
		ArrayList <Point> area = new ArrayList<Point>();
		int areaWidth = (CAMERA_WIDTH- 2*m_BoxTextureRegion.getWidth())/3;
		int areaHeight = (CAMERA_HEIGHT * 2 / 3 - m_BoxTextureRegion.getHeight())/2;

		int offsetX = m_BoxTextureRegion.getWidth()/4;
		int offsetY = m_BoxTextureRegion.getHeight()/4;
		area.add(new Point(m_BoxTextureRegion.getWidth() + randomX.nextInt(areaWidth- offsetX), randomY.nextInt(areaHeight- offsetY)));
		area.add(new Point(m_BoxTextureRegion.getWidth() + areaWidth + randomX.nextInt(areaWidth - offsetX), randomY.nextInt(areaHeight- offsetY)));
		area.add(new Point(m_BoxTextureRegion.getWidth() + 2 * areaWidth + randomX.nextInt(areaWidth - offsetX), randomY.nextInt(areaHeight- offsetY)));
		area.add(new Point(m_BoxTextureRegion.getWidth() + randomX.nextInt(areaWidth- offsetX), randomY.nextInt(areaHeight- offsetY) + areaHeight));
		area.add(new Point(m_BoxTextureRegion.getWidth() + areaWidth + randomX.nextInt(areaWidth- offsetX), randomY.nextInt(areaHeight- offsetY) + areaHeight));
		area.add(new Point(m_BoxTextureRegion.getWidth() + 2 * areaWidth + randomX.nextInt(areaWidth- offsetX), randomY.nextInt(areaHeight- offsetY) + areaHeight));

		Collections.shuffle(area);
		return area;
	}

	@Override
	protected void onPause(){
		Log.e(TAG, "onPause()");
		super.onPause();
		if(m_Music != null && m_Music.isPlaying()) {
			m_Music.pause();
		}
	}
	
	@Override
	protected void onResume(){
		
		super.onResume();
		if (m_Music != null && !m_Music.isPlaying()){
			Log.e(TAG, "onResume() Music start play()");
			m_Music.resume();
		}
	}

	@Override
	public Scene onLoadScene() {
		Log.e(TAG, "onLoadScene()");
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene loadingScene = new Scene(1);

		loadingScene.setBackground(new SpriteBackground(m_BackgroundSprite));
		m_LoadingSprite = new Sprite(0 ,0,this.m_LoadingTextureRegion);
		//loadingScene.registerTouchArea(m_LoadingSprite);
		loadingScene.getTopLayer().addEntity(m_LoadingSprite);
		loadingScene.registerUpdateHandler(new TimerHandler(1.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				m_playScene = new Scene(2);
				myLoadResources();
				//Make retry scene
				m_RetryScene = new CameraScene(1, m_Camera); 	
				m_RetryScene.setBackgroundEnabled(false);
				composeRetryScene();
				createBaseSprite();
				updateScene();
				mEngine.setScene(m_playScene);
				Log.e(TAG, "registerUpdateHandler onTimePassed");
				m_playScene.setTouchAreaBindingEnabled(true);
				
				//play sound
				if(m_Music != null && !m_Music.isPlaying()) {
					Log.e(TAG, "Music start play()");
					m_Music.play();
				}
			}
		}));
		return loadingScene;
	}

	private void composeRetryScene(){
		final int OFFSET = m_RetryOkTextureRegion.getWidth()/2;	
		final int width = this.m_RetryTextureRegion.getWidth();
		final int height = this.m_RetryTextureRegion.getHeight();
		final int okHeight = this.m_RetryOkTextureRegion.getHeight();
		final int okWidth = this.m_RetryOkTextureRegion.getWidth();

		final int x = CAMERA_WIDTH / 2 - width / 2;
		final int y = CAMERA_HEIGHT / 2 - height / 2;

		Log.e(TAG, "width="+width+" height="+height);

		final Sprite retryBGSprite = new Sprite(x , y, this.m_RetryTextureRegion);

		final Sprite retryOKSprite = new Sprite(x + OFFSET, y + height - okHeight/2, this.m_RetryOkTextureRegion){

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Log.e(TAG, "onAreaTouched");

				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					removeDarkenBG();
					m_playScene.clearChildScene();					
					resetScreen();
					return true;
				}
				return false;				
			}			

		};

		final Sprite retryCancelSprite = new Sprite(x + width - okWidth - OFFSET, y + height - okHeight/2, this.m_RetryCancelTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Log.e(TAG, "onAreaTouched");

				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					if (m_iCurrentItemNum < m_ItemVector.size()-1){
						m_iCurrentItemNum++;
						m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;//ARR_ANIMAL[m_iCurrentItemNum++];
					}else{
						m_iCurrentItemNum++;
					}
					removeDarkenBG();
					m_playScene.clearChildScene();
					resetScreen();
					return true;
				}
				return false;
			}	
		};		

		this.m_RetryScene.getTopLayer().addEntity(retryBGSprite);
		this.m_RetryScene.getTopLayer().addEntity(retryOKSprite);
		this.m_RetryScene.getTopLayer().addEntity(retryCancelSprite);
		this.m_RetryScene.registerTouchArea(retryOKSprite);
		this.m_RetryScene.registerTouchArea(retryCancelSprite);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == Const.MATCH_QUIZ_RESULT){
			if (resultCode == RESULT_OK){
				mEngine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {

						while(m_playScene.getLayer(ENTITIES_LAYER).getEntityCount()>0){
							m_playScene.getLayer(ENTITIES_LAYER).removeEntity(0);
						}				
						m_playScene.clearUpdateHandlers();
						m_iCurrentCollideBoxIdx = 0;
						m_CurrentTouchedAlphabetSprite = null;
						m_ItemTextureRegion = null;
						m_arrAlphabetTexture = null;
						m_arrAlphabet = null;
						m_Item = null;
						m_arrBoxSprite = null;
						m_arrAlphabetSprite = null;
						m_bFirstTouch = true;

						ThemeItemActivity.this.updateScene();
					}        	
				});
			}
		}
		if(requestCode == Const.RETRY_DIALOG_RESULT) {
			if(resultCode == RESULT_OK) {
				Log.d(TAG, "resultCode:" + "RESULT_OK");
				//m_playScene.clearChildScene();
				resetScreen();
			}
			else if(resultCode == RESULT_CANCELED) {
				if (m_iCurrentItemNum < m_ItemVector.size()-1){
					m_iCurrentItemNum++;
					m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;//ARR_ANIMAL[m_iCurrentItemNum++];
				}else{
					m_iCurrentItemNum++;
				}
				//m_playScene.clearChildScene();
				resetScreen();
			}
		}
	}

	//Reset Screen - Remove all the entities from scene.
	private void resetScreen(){
		Log.e(TAG, "resetScreen:m_iCurrentItemNum"+m_iCurrentItemNum);
		
		if (m_iCurrentItemNum != 0 && this.m_iCurrentItemNum % ITEM_NUM_PER_STAGE == 0){
			ArrayList<Item> m_quizItemList = new ArrayList<Item>();
			m_quizItemList.addAll(this.m_ItemVector.subList(m_iCurrentItemNum - ITEM_NUM_PER_STAGE, m_iCurrentItemNum));
			Log.e(TAG, "m_ItemVector.subList m_iCurrentItemNum:" + m_iCurrentItemNum);
			Log.e(TAG, "m_quizItemList:"+ m_quizItemList.get(0).strWordCharId);
			Intent intent = new Intent(this, MatchQuiz.class);
			intent.putExtra(Const.CUR_LEVEL, m_currentLevel);
			intent.putParcelableArrayListExtra(Const.MATCH_QUIZ, m_quizItemList);
			startActivityForResult(intent, Const.MATCH_QUIZ_RESULT);
			finish();

		}else{ 
			Log.e(TAG, "resetScreen()");
			mEngine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {

					while(m_playScene.getLayer(ENTITIES_LAYER).getEntityCount()>0){
						m_playScene.getLayer(ENTITIES_LAYER).removeEntity(0);
					}				
					m_playScene.clearUpdateHandlers();
					m_iCurrentCollideBoxIdx = 0;
					m_CurrentTouchedAlphabetSprite = null;
					m_ItemTextureRegion = null;
					m_arrAlphabetTexture = null;
					m_arrAlphabet = null;
					m_Item = null;
					m_arrBoxSprite = null;
					m_arrAlphabetSprite = null;
					m_bFirstTouch = true;

					ThemeItemActivity.this.updateScene();
				}        	
			});
		}
	}

	//Reset Screen - Remove all the m_DarkenSprite from scene.
	private void removeDarkenBG(){
		Log.e(TAG, "removeDarkenBG()");
		mEngine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				m_playScene.getLayer(ENTITIES_LAYER).removeEntity(m_DarkenSprite);		
			}        	
		});
	}
	//Load fixed texture
	private void loadBaseTexture(){

		this.mEngine.getTextureManager().loadTexture(this.m_PassTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_FailTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_RetryTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_RetryOkTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_RetryCancelTexture);
		//this.mEngine.getTextureManager().loadTexture(this.m_DarkenTexture);

	}

	//Load changeable texture
	private void loadEntityTexture(){
		this.mEngine.getTextureManager().loadTexture(this.m_ItemTexture);	
		this.mEngine.getTextureManager().loadTexture(this.m_BoxTexture);
	}
	
	protected void onDestroy() {
		//Background Music and sound
/*		if (m_Music != null){
			if (m_Music.isPlaying()){
				m_Music.release();
			}			
		}*/
		if (m_ItemSound != null)
			m_ItemSound.release();
		if (m_DropToBoxSound != null)
			m_DropToBoxSound.release();
		if (m_HelpSound != null)
			m_HelpSound.release();
		if (m_FailToDropSound != null)
			m_FailToDropSound.release();
		if (m_AlphabetSound != null && m_AlphabetSound.length > 0){
			for(int i = 0; i < m_AlphabetSound.length; i++){
				if (m_AlphabetSound[i] != null)
					m_AlphabetSound[i].release();
			}
		}
		super.onDestroy();
	}

	//Create base object
	private void createBaseSprite(){

		loadBaseTexture();

		this.m_DarkenSprite = new Sprite(0,0,this.m_DarkenTextureRegion);

		this.m_SkipSprite = new Sprite(CAMERA_WIDTH - m_SkipTextureRegion.getWidth() - m_SkipTextureRegion.getWidth()/4,
				m_SkipTextureRegion.getHeight()/4, this.m_SkipTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){

					if (m_iCurrentItemNum < m_ItemVector.size()-1){
						m_iCurrentItemNum++;
						m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;
					}else{
						m_iCurrentItemNum++;
					}
					m_playScene.clearTouchAreas();			
					resetScreen();
					return true;
				}
				return false;

			}
		};
		m_playScene.getLayer(BASE_LAYER).addEntity(m_SkipSprite);

		this.m_PassSprite = new Sprite(
				CAMERA_WIDTH/2-(this.m_PassTextureRegion.getWidth()/2),
				CAMERA_HEIGHT/2 - (this.m_PassTextureRegion.getHeight()/2),
				this.m_PassTextureRegion);

		this.m_FailSprite = new Sprite(
				CAMERA_WIDTH/2-(this.m_FailTextureRegion.getWidth()/2),
				CAMERA_HEIGHT/2 - (this.m_FailTextureRegion.getHeight()/2),
				this.m_FailTextureRegion);

		m_playScene.getLayer(BASE_LAYER).addEntity(m_Help);
	}

	//Update scene with new entities.
	private void updateScene(){
		
		m_RandomPoint = this.getAreaArray();

		loadEntityTexture();

		m_FailSprite.setVisible(true);

		//re regist touch area for help and pause btn
		m_playScene.registerTouchArea(m_Help);
		m_playScene.registerTouchArea(m_SkipSprite);
		//Load Sound
		try {
			this.m_ItemSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, m_strAlphabet+".mp3");
			this.m_ItemSound.setVolume(1.0f);
		} catch (final IOException e) {
			Debug.e("Error", e);
		}

		Log.e(TAG, "updateScene()");

		//Add ThemeItem Sprite to Scene
		this.m_ItemTextureRegion = TextureRegionFactory.createTiledFromResource(this.m_ItemTexture, this, m_ItemVector.get(m_iCurrentItemNum).iItemImgId, 0, 0, 2, 1);
		this.m_ItemTextureRegion.setCurrentTileIndex(ITEM_IMG_FIRST);
		Log.e("WOORAM", "m_ItemTextureRegion.getWidth():"+m_ItemTextureRegion.getWidth());
		this.m_Item = new AnimatedSprite((CAMERA_WIDTH - m_ItemTextureRegion.getWidth()/2)/2
				,CAMERA_HEIGHT/8, this.m_ItemTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && m_ItemSound!=null && m_bSoundOn == true && m_bFirstTouch == false){
					m_ItemSound.play();
					return true;
				}					

				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && m_bFirstTouch == true){
					drawAlphabet(pSceneTouchEvent);
					m_bFirstTouch = false;
				}
				return false;
			}
			
		};

		m_Item.setScale(1.3f);

		m_playScene.getLayer(ENTITIES_LAYER).addEntity(m_Item);

		//Load Box Sprite to scene.
		int length = m_strAlphabet.length();
		m_arrBoxSprite = new AlphabetSprite[m_strAlphabet.length()];
		//int divWidth = CAMERA_WIDTH/length;

		int space = m_BoxTextureRegion.getWidth()/(length * length);
		int boxWidth = m_BoxTextureRegion.getWidth();
		int w = boxWidth * length + space*(length-1);
		int x = (CAMERA_WIDTH - w)/2;

		for(int i=0; i < length; i++){			
			m_arrBoxSprite[i] = new AlphabetSprite(x,
					CAMERA_HEIGHT-m_BoxTextureRegion.getHeight(), this.m_BoxTextureRegion, i, m_strAlphabet.charAt(i));
			Log.e(TAG, "CAMERA_HEIGHT:"+CAMERA_HEIGHT+" m_BoxTexture.getWidth()"+m_BoxTexture.getWidth()+" CAMERA_HEIGHT/10:"+CAMERA_HEIGHT/10);
			m_playScene.getLayer(ENTITIES_LAYER).addEntity(m_arrBoxSprite[i]);
			x = x + space + boxWidth;
		}

		//Load Alphabet Sprite to scene
		this.m_arrAlphabetTexture = new TiledTextureRegion[m_strAlphabet.length()];
		this.m_arrAlphabet = new Texture[m_strAlphabet.length()];
		m_arrAlphabetSprite = new AlphabetSprite[m_strAlphabet.length()];

		for(int i=0; i<m_strAlphabet.length(); i++){
			this.m_arrAlphabet[i] = new Texture(256,128,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mEngine.getTextureManager().loadTexture(this.m_arrAlphabet[i]);
			this.m_arrAlphabetTexture[i] = TextureRegionFactory.createTiledFromResource(this.m_arrAlphabet[i], this, res.getAlphabetResourceId(m_strAlphabet.charAt(i)), 0, 0, 2, 1 );//m_strAlphabet.charAt(i)+
			this.m_arrAlphabetTexture[i].setCurrentTileIndex(0);
		}

		m_AlphabetSound = new Sound[m_strAlphabet.length()];

		for(int m_Idx=0; m_Idx < m_strAlphabet.length(); m_Idx++){
			try {
				m_AlphabetSound[m_Idx] = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, this.m_strAlphabet.charAt(m_Idx)+".mp3");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			m_arrAlphabetSprite[m_Idx] = new AlphabetSprite(0,0,
					this.m_arrAlphabetTexture[m_Idx], m_Idx, m_strAlphabet.charAt(m_Idx)) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP){

						//Change to original Size
						this.setScale(1.0f);

						//Collision Check
						if (!this.bCollied){
							Log.e(TAG, "sprite is now Collide when Action up");
							for (int i = 0; i < m_arrBoxSprite.length; i++){
								if(m_arrBoxSprite[i].filledAlphabetIndex == this.sequence){
									m_arrBoxSprite[i].bFilled = false;
									m_arrBoxSprite[i].bCorrect = false;
									m_arrBoxSprite[i].alphabetContainer = EMPTY_ALPHABET;
									break;
								}
							}
							m_CurrentTouchedAlphabetSprite = null;
							this.setCurrentTileIndex(0);
							return true;		
						}	

						//When drop the alphabet to Box						

						for (int j=0; j < m_arrBoxSprite.length; j++){
							if(m_arrBoxSprite[j].filledAlphabetIndex == this.sequence){
								m_arrBoxSprite[j].bFilled = false;
								m_arrBoxSprite[j].bCorrect = false;
								m_arrBoxSprite[j].filledAlphabetIndex = -1;
							}
						}

						if (m_arrBoxSprite[m_iCurrentCollideBoxIdx].bFilled && m_bSoundOn == true){
							m_FailToDropSound.play();

							this.addShapeModifier(new MoveModifier(1,
									this.getX(), m_RandomPoint.get(this.sequence).x,
									this.getY(), m_RandomPoint.get(this.sequence).y,
									EaseExponentialOut.getInstance()));
							m_CurrentTouchedAlphabetSprite = null;
							return true;
						}

						//play sound
						if (m_bSoundOn == true)
							m_DropToBoxSound.play();

						float boxX = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getX();
						float boxY = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getY();
						float boxWidth = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getWidth();
						float boxHeight = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getHeight();
						this.setPosition(boxX + (boxWidth/2 - this.getWidth()/2), boxY + (boxHeight/2 - this.getHeight()/2));

						m_arrBoxSprite[m_iCurrentCollideBoxIdx].bFilled = true;
						m_arrBoxSprite[m_iCurrentCollideBoxIdx].filledAlphabetIndex = this.sequence;

						//set bCorret to true if user position the alphabet to right box.
						if(this.sequence == m_iCurrentCollideBoxIdx)
							m_arrBoxSprite[m_iCurrentCollideBoxIdx].bCorrect = true;	
						else
							m_arrBoxSprite[m_iCurrentCollideBoxIdx].bCorrect = false;

						m_arrBoxSprite[m_iCurrentCollideBoxIdx].alphabetContainer = this.alphabet;
						this.setCurrentTileIndex(1);

						//reset screen to next item when user clear the stage
						if(isAllBoxesFilled(m_arrBoxSprite)){
							if(isStageCleared(m_arrBoxSprite)){
								if (m_iCurrentItemNum < m_ItemVector.size()-1){
									m_iCurrentItemNum++;
									m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;//ARR_ANIMAL[m_iCurrentItemNum++];
								}else{
									m_iCurrentItemNum = 0;
									m_strAlphabet = m_ItemVector.get(m_iCurrentItemNum).strWordCharId;
								}
								//drawResult(m_PassSprite);
								shakeAndResetSprite(m_Item);							
								m_playScene.clearTouchAreas();
								//resetAfterDelay(2500);
							}
							else{								
								drawResult(m_FailSprite);
								m_playScene.clearTouchAreas();
								popupAfterDelay(2500);								
							}
						}
						m_CurrentTouchedAlphabetSprite = null;
					}
					//Set item size as 1.5times when user try to drag it.
					else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
						m_CurrentTouchedAlphabetSprite = this;
						Log.e(TAG, "alphabet:"+this.alphabet+".mp3");

						this.clearShapeModifiers();
						m_AlphabetSound[this.sequence].setVolume(1.0f);
						m_AlphabetSound[this.sequence].play();
						this.setScale(1.5f);
						return true;
					}
					//Dragging
					else{				
						m_CurrentTouchedAlphabetSprite = this;
						this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
						return true;
					}
					return false;
				}			
			};	

		}

		for (int k = m_strAlphabet.length(); k > 0; k--){
			m_playScene.registerTouchArea(m_arrAlphabetSprite[k-1]);
		}

		m_playScene.registerTouchArea(m_Item);	
		//m_playScene.setTouchAreaBindingEnabled(true);

		// The actual collision-checking.
		m_playScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				for(int i=0; i < m_arrBoxSprite.length; i++ ){
					if (m_CurrentTouchedAlphabetSprite == null){
						m_arrBoxSprite[i].setColor(1, 1, 1);
						continue;
					}						
					if(isCollide(m_CurrentTouchedAlphabetSprite,m_arrBoxSprite[i])){
						m_arrBoxSprite[i].setColor(0, 0, 1);
						m_iCurrentCollideBoxIdx = i;
						if (m_CurrentTouchedAlphabetSprite != null)
							m_CurrentTouchedAlphabetSprite.bCollied = true;					
					} else if(m_iCurrentCollideBoxIdx == i && !isCollide(m_CurrentTouchedAlphabetSprite,m_arrBoxSprite[i])) {
						m_arrBoxSprite[i].setColor(1, 1, 1);		
						if (m_CurrentTouchedAlphabetSprite != null)
							m_CurrentTouchedAlphabetSprite.bCollied = false;
					}
				}
			}
		});
	}

	private void drawAlphabet(final TouchEvent touchEvent){
		for(int j=0; j < m_strAlphabet.length(); j++){
			m_arrAlphabetSprite[j].setPosition(touchEvent.getX(), touchEvent.getY());
			m_arrAlphabetSprite[j].addShapeModifier(new ParallelShapeModifier(
					new MoveModifier(1,touchEvent.getX(), m_RandomPoint.get(j).x, touchEvent.getY(), m_RandomPoint.get(j).y,EaseLinear.getInstance()),
					new RotationModifier(1, 0, 360)));
		}
		for(int l=0; l < m_strAlphabet.length(); l++){
			m_playScene.getLayer(ENTITIES_LAYER).addEntity(m_arrAlphabetSprite[l]);
		}
	}

	private void drawResult(Sprite sprite){

		final SequenceShapeModifier shapeModifier = new SequenceShapeModifier(new AlphaModifier(2, 0, 1));
		sprite.addShapeModifier(shapeModifier);
		sprite.setScale(1.5f);
		this.m_playScene.getLayer(ENTITIES_LAYER).addEntity(sprite);
	}

	private void popupAfterDelay(int delayMS){
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				m_FailSprite.setVisible(false);		
				/*darkenBG();
				m_playScene.setChildScene(m_RetryScene, false, true, true);*/
				Intent popupIntent = new Intent(ThemeItemActivity.this, PopupActivity.class);
				startActivityForResult(popupIntent, Const.RETRY_DIALOG_RESULT);
			}
		}, delayMS);
	}

	private void darkenBG(){
		m_playScene.getLayer(ENTITIES_LAYER).addEntity(m_DarkenSprite);	
	}

	private boolean isAllBoxesFilled(AlphabetSprite [] sprites){
		boolean result = true;
		for(int i=0; i < sprites.length; i++){
			if (!sprites[i].bFilled)
				result = false;
		}
		return result;
	}

	private boolean isStageCleared(AlphabetSprite [] sprites){
		boolean result = true;
		for(int i=0; i < sprites.length; i++){
			if (sprites[i].alphabet != sprites[i].alphabetContainer)
				result = false;
		}
		return result;
	}

	private boolean isCollide(AnimatedSprite alphabet, AnimatedSprite box){
		if ((getCenterX(alphabet) > getCenterX(box) - CENTER_OFFSET) 
				&& (getCenterX(alphabet) < getCenterX(box) + CENTER_OFFSET)
				&& (getCenterY(alphabet) > getCenterY(box) - CENTER_OFFSET) 
				&& (getCenterY(alphabet) < getCenterY(box) + CENTER_OFFSET)){
			return true;
		}
		return false;
	}

	private float getCenterX(AnimatedSprite s){
		if (s != null)
			return s.getX() + 2/s.getWidth();
		else
			return 0f;
	}

	private float getCenterY(AnimatedSprite s){
		if (s != null)
			return s.getY() + 2/s.getHeight();
		else
			return 0f;
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

	//Shake sprite and reset to next stage
	private void shakeAndResetSprite(AnimatedSprite sprite){
		sprite.animate(new long[] { 400,400 }, 3, new IAnimationListener(){

			@Override
			public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
				// TODO Auto-generated method stub
				m_ItemSound.play();	
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resetScreen();
			}			
		});
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}
}
