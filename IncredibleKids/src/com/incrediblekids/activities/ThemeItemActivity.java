package com.incrediblekids.activities;

import java.io.IOException;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;
import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.ColoredTextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.MoveModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ease.EaseElasticOut;
import org.anddev.andengine.entity.shape.modifier.ease.EaseExponentialOut;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Toast;
import com.incrediblekids.util.AlphabetSprite;

/**
 * @author Nicolas Gramlich
 * @since 15:13:46 - 15.06.2010
 */
public class ThemeItemActivity extends BaseGameActivity implements IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	public final static int CAMERA_WIDTH = 800;
	public final static int CAMERA_HEIGHT = 480;

	public final static int CENTER_OFFSET = 36;		//OFFSET for collision detect: larger is less sensitive
	public final static int BASE_LAYER = 0;		//Base layer for non-changable sprite
	public final static int ENTITIES_LAYER = 1;	//entiti layer for changable sprite
	
	public final static int MENU_RESET = 0;
	public final static int MENU_QUIT = MENU_RESET + 1;
	public final static String TAG = "TouchDragExample";
	public final static char EMPTY_ALPHABET = '0';
	
	public final static String [] ARR_ANIMAL = {"dog", "sheep", "bear", "cat", "monkey", "lion", "mouse", "bird"};

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
	private TextureRegion m_BoxTextureRegion;
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
	private TextureRegion [] m_arrAlphabetTexture;
	private AlphabetSprite [] m_arrAlphabetSprite;
	private AlphabetSprite m_CurrentTouchedAlphabetSprite;

	//Pause button Sprite
	private Texture m_PauseTexture;
	private TextureRegion m_PauseTextureRegion;
	private Sprite m_PauseSprite;

	//Theme item Sprite
	private Sprite m_Item;
	private Texture  m_ItemTexture;
	private TextureRegion m_ItemTextureRegion;
	private int m_iCurrentItemNum;

	//Help Button Sprite
	private Sprite m_Help;
	private Texture  m_HelpTexture;
	private TextureRegion m_HelpTextureRegion;

	//Pause
	private Scene m_Scene;
	private Scene m_MenuScene;

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
	
	//Custom Font
	private Texture m_FontTexture;
	private Font m_Font;

	private Random randomX;
	private Random randomY;

	private int xRange;
	private int yRange;

	@Override
	public Engine onLoadEngine() {

		m_iCurrentItemNum = 0;

		randomX = new Random();
		randomY = new Random();

		//		CAMERA_WIDTH = getLCDWidth();
		//		CAMERA_HEIGHT = getLCDHeight();

		m_strAlphabet = ARR_ANIMAL[m_iCurrentItemNum++];
		m_iCurrentCollideBoxIdx = 0;
		this.m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.m_Camera).setNeedsMusic(true).setNeedsSound(true));
	}

	@Override
	public void onLoadResources() {
		Log.e(TAG, "onLoadResources()");
		SoundFactory.setAssetBasePath("mfx/");
		
		try {
			this.m_DropToBoxSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "drop_to_box.ogg");//m_strAlphabet+".mp3");
			this.m_DropToBoxSound.setVolume(1.0f);
			
			this.m_HelpSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "help_drop.ogg");//m_strAlphabet+".mp3");
			this.m_HelpSound.setVolume(1.0f);
			
			this.m_FailToDropSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "fail_to_drop.ogg");
			this.m_FailToDropSound.setVolume(1.0f);
			
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
		
		MusicFactory.setAssetBasePath("mfx/");		
		try {
			this.m_Music = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "background_animal.ogg");
			this.m_Music.setLooping(true);
			this.m_Music.setVolume(0.2f);
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
		if(m_Music != null && !m_Music.isPlaying()) {
			m_Music.play();
		}

		//Load font
		FontFactory.setAssetBasePath("font/");
		this.m_FontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_Font = FontFactory.createFromAsset(this.m_FontTexture, this, "Plok.ttf", 48, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.m_FontTexture);
		this.mEngine.getFontManager().loadFont(this.m_Font);

		//Load Texture
		TextureRegionFactory.setAssetBasePath("gfx/");

		//Load Background
		this.m_BackgroundTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_BackgroundTextureRegion = TextureRegionFactory.createFromAsset(this.m_BackgroundTexture, this, "background_2.png", 0, 0);
		
		this.m_PauseTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);		
		this.m_BoxTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_ItemTexture = new Texture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_PauseTextureRegion = TextureRegionFactory.createFromAsset(this.m_PauseTexture, this, "pause.png",0,0);
		this.m_BoxTextureRegion = TextureRegionFactory.createFromAsset(this.m_BoxTexture, this, "box.png", 0, 0);
		this.m_HelpTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_HelpTextureRegion = TextureRegionFactory.createFromAsset(this.m_HelpTexture, this, "help.png", 0, 0);

		//Retry popup texture
		m_RetryTexture = new Texture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		m_RetryOkTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		m_RetryCancelTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		
		m_RetryTextureRegion = TextureRegionFactory.createFromAsset(this.m_RetryTexture, this, "retry_popup_bg.png",0,0);
		m_RetryOkTextureRegion = TextureRegionFactory.createFromAsset(this.m_RetryOkTexture, this, "retry_ok_btn.png",0,0);
		m_RetryCancelTextureRegion = TextureRegionFactory.createFromAsset(this.m_RetryCancelTexture, this, "retry_no_btn.png",0,0);
		
		//Load pass texture
		this.m_PassTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_PassTextureRegion = TextureRegionFactory.createFromAsset(this.m_PassTexture, this, "pass_128.png", 0, 0);

		//Load fail texture
		this.m_FailTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_FailTextureRegion = TextureRegionFactory.createFromAsset(this.m_FailTexture, this, "fail_128.png", 0, 0);

		//Range of random x and y axis
		xRange = CAMERA_WIDTH - m_BoxTexture.getWidth() * 2;
		yRange = CAMERA_HEIGHT - CAMERA_HEIGHT/3 - m_BoxTexture.getHeight();
	}

	@Override
	protected void onPause(){
		Log.e(TAG, "onPause()");
		super.onPause();
		if(m_Music != null && !m_Music.isPlaying()) {
			m_Music.stop();
		}
	}

	@Override
	public Scene onLoadScene() {
		Log.e(TAG, "onLoadScene()");

		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		//Make retry scene
		this.m_RetryScene = new CameraScene(1, this.m_Camera);
		this.composeRetryScene();
		this.m_RetryScene.setBackgroundEnabled(false);

		//Make scene
		this.m_Scene = new Scene(2);
		this.m_Scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		this.m_MenuScene = this.createMenuScene();
		this.createBaseSprite();
		//Add all the entities
		//this.createBaseSprite();
		this.updateScene();

		return m_Scene;
	}
	
	private void composeRetryScene(){
		final int OFFSET = 80;	
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
					m_Scene.clearChildScene();
					resetScreen();

				}
				return true;
			}			
		};
		final Sprite retryCancelSprite = new Sprite(x + width - okWidth - OFFSET, y + height - okHeight/2, this.m_RetryCancelTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Log.e(TAG, "onAreaTouched");
				
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					if (m_iCurrentItemNum < ARR_ANIMAL.length)
						m_strAlphabet = ARR_ANIMAL[m_iCurrentItemNum++];
					m_Scene.clearChildScene();
					resetScreen();

				}
				return true;
			}	
		};		
		
		this.m_RetryScene.getTopLayer().addEntity(retryBGSprite);
		this.m_RetryScene.getTopLayer().addEntity(retryOKSprite);
		this.m_RetryScene.getTopLayer().addEntity(retryCancelSprite);
		this.m_RetryScene.registerTouchArea(retryOKSprite);
		this.m_RetryScene.registerTouchArea(retryCancelSprite);
	}

	//Pause Menu
	protected MenuScene createMenuScene() {
		Log.e(TAG, "createMenuScene()");
		final MenuScene menuScene = new MenuScene(this.m_Camera);

		final ColoredTextMenuItem resetMenuItem = new ColoredTextMenuItem(MENU_RESET, this.m_Font, "RESUME", 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(resetMenuItem);

		final ColoredTextMenuItem quitMenuItem = new ColoredTextMenuItem(MENU_QUIT, this.m_Font, "QUIT", 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		menuScene.addMenuItem(quitMenuItem);
		menuScene.buildAnimations();
		menuScene.setBackgroundEnabled(false);
		menuScene.setOnMenuItemClickListener(this);

		return menuScene;
	}

	//Reset Screen - Remove all the entities from scene.
	private void resetScreen(){
		Log.e(TAG, "resetScreen()");
		mEngine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {

				while(m_Scene.getLayer(ENTITIES_LAYER).getEntityCount()>0){
					m_Scene.getLayer(ENTITIES_LAYER).removeEntity(0);
				}
				
				m_Scene.clearUpdateHandlers();
				m_Scene.clearTouchAreas();	
				m_iCurrentCollideBoxIdx = 0;
				m_CurrentTouchedAlphabetSprite = null;
				m_ItemTextureRegion = null;
				m_arrAlphabetTexture = null;
				m_arrAlphabet = null;
				m_Item = null;
				m_arrBoxSprite = null;
				m_arrAlphabetSprite = null;

				ThemeItemActivity.this.updateScene();
			}        	
		});
	}

	private void loadBaseTexture(){
		this.mEngine.getTextureManager().loadTexture(this.m_BackgroundTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_PauseTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_HelpTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_PassTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_FailTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_RetryTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_RetryOkTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_RetryCancelTexture);
	}
	
	private void loadEntityTexture(){
		this.mEngine.getTextureManager().loadTexture(this.m_ItemTexture);	
		this.mEngine.getTextureManager().loadTexture(this.m_BoxTexture);
	}

	//Create base object
	private void createBaseSprite(){
		
		loadBaseTexture();

		this.m_BackgroundSprite = new Sprite(0,0,this.m_BackgroundTextureRegion);
		m_Scene.getLayer(BASE_LAYER).addEntity(m_BackgroundSprite);
		
		this.m_PassSprite = new Sprite(
				CAMERA_WIDTH/2-(this.m_PassTexture.getWidth()/2),
				CAMERA_HEIGHT/2 - (this.m_PassTexture.getHeight()/2),
				this.m_PassTextureRegion);
		
		this.m_FailSprite = new Sprite(
				CAMERA_WIDTH/2-(this.m_FailTexture.getWidth()/2),
				CAMERA_HEIGHT/2 - (this.m_FailTexture.getHeight()/2),
				this.m_FailTextureRegion);
		
		this.m_Help = new Sprite(CAMERA_WIDTH - m_HelpTexture.getWidth() - 10,
				CAMERA_HEIGHT/8 + m_HelpTexture.getHeight() + 10, this.m_HelpTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Log.e(TAG, "onAreaTouched");
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					
					//play sound
					m_HelpSound.play();
					
					for (int i=0; i < m_arrAlphabetSprite.length; i++){
						
						//Wrong alphabet is filled in box
						if(m_arrBoxSprite[i].bFilled && !m_arrBoxSprite[i].bCorrect){

							m_arrAlphabetSprite[m_arrBoxSprite[i].filledAlphabetIndex].addShapeModifier(new MoveModifier(1,
									m_arrBoxSprite[i].getX(), randomX.nextInt(xRange),
									m_arrBoxSprite[i].getY(), randomY.nextInt(yRange),
									EaseExponentialOut.getInstance()));
							m_arrBoxSprite[i].bFilled = false;
							m_arrBoxSprite[i].bCorrect = false;
							m_arrBoxSprite[i].filledAlphabetIndex = -1;
							m_arrBoxSprite[i].alphabetContainer = EMPTY_ALPHABET;
							//return true;
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
							for (int j=0; j < m_arrBoxSprite.length; j++){
								if(m_arrBoxSprite[j].filledAlphabetIndex == i){
									m_arrBoxSprite[j].bFilled = false;
									m_arrBoxSprite[j].bCorrect = false;
									m_arrBoxSprite[j].filledAlphabetIndex = -1;
									m_arrBoxSprite[i].alphabetContainer = EMPTY_ALPHABET;
								}
							}

							m_arrBoxSprite[i].bFilled = true;
							m_arrBoxSprite[i].bCorrect = true;
							m_arrBoxSprite[i].filledAlphabetIndex = i;
							m_arrBoxSprite[i].alphabetContainer = m_arrAlphabetSprite[i].alphabet;
							//reset screen to next item when user clear the stage
							if(isAllBoxesFilled(m_arrBoxSprite)){
								if(isStageCleared(m_arrBoxSprite)){
									if (m_iCurrentItemNum < ARR_ANIMAL.length)
										m_strAlphabet = ARR_ANIMAL[m_iCurrentItemNum++];
									drawResult(m_PassSprite);
									resetAfterDelay(2500);
								}
								else{
									Log.e(TAG, "isStageCleared(m_arrBoxSprite) return false");
									if (m_iCurrentItemNum < ARR_ANIMAL.length)
										m_strAlphabet = ARR_ANIMAL[m_iCurrentItemNum++];
									drawResult(m_FailSprite);
									resetAfterDelay(2500);
								}
							}
							m_CurrentTouchedAlphabetSprite = null;
							break;
						}
					}
				}
				return true;
			}
		};

		m_Scene.getLayer(BASE_LAYER).addEntity(m_Help);
		
		//Add Pause Sprite to Scene
		this.m_PauseSprite = new Sprite(CAMERA_WIDTH - m_PauseTextureRegion.getWidth() - 10
				,CAMERA_HEIGHT/8, this.m_PauseTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
					if(mEngine.isRunning()) {
						if(!m_Scene.hasChildScene()) {
							m_Music.pause();
							m_Scene.setChildScene(m_MenuScene, false, true, true);
						}
					}
				}
				return true;
			}
		}; 
		m_Scene.getLayer(BASE_LAYER).addEntity(m_PauseSprite);
	}

	//Update scene with new entities.
	private void updateScene(){
		
		loadEntityTexture();

		m_FailSprite.setVisible(true);
		
		//re registe touch area for help and pause btn
		m_Scene.registerTouchArea(m_Help);
		m_Scene.registerTouchArea(m_PauseSprite);
		//Load Sound
		try {
			this.m_ItemSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "monkey.mp3");//m_strAlphabet+".mp3");
			this.m_ItemSound.setVolume(1.0f);
		} catch (final IOException e) {
			Debug.e("Error", e);
		}

		Log.e(TAG, "updateScene()");

		//Add ThemeItem Sprite to Scene

		this.m_ItemTextureRegion = TextureRegionFactory.createFromAsset(this.m_ItemTexture, this, m_strAlphabet+".png", 0, 0);

		this.m_Item = new Sprite(CAMERA_WIDTH/2 - m_ItemTextureRegion.getWidth()/2
				,CAMERA_HEIGHT/8, this.m_ItemTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && m_ItemSound!=null)
					m_ItemSound.play();
				return true;
			}
		};

		//m_Item.setScale(1f);
		m_Scene.getLayer(ENTITIES_LAYER).addEntity(m_Item);

		//Load Box Sprite to scene.
		int length = m_strAlphabet.length();
		m_arrBoxSprite = new AlphabetSprite[m_strAlphabet.length()];
		int divWidth = CAMERA_WIDTH/length;
		for(int i=0; i < length; i++){
			m_arrBoxSprite[i] = new AlphabetSprite(divWidth * i + (divWidth-m_BoxTexture.getWidth())/2,
					CAMERA_HEIGHT-m_BoxTexture.getWidth()- CAMERA_HEIGHT/10, this.m_BoxTextureRegion, i, m_strAlphabet.charAt(i));
			m_Scene.getLayer(ENTITIES_LAYER).addEntity(m_arrBoxSprite[i]);
		}

		//Load Alphabet Sprite to scene
		this.m_arrAlphabetTexture = new TextureRegion[m_strAlphabet.length()];
		this.m_arrAlphabet = new Texture[m_strAlphabet.length()];
		m_arrAlphabetSprite = new AlphabetSprite[m_strAlphabet.length()];

		for(int i=0; i<m_strAlphabet.length(); i++){
			this.m_arrAlphabet[i] = new Texture(128,128,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mEngine.getTextureManager().loadTexture(this.m_arrAlphabet[i]);
			this.m_arrAlphabetTexture[i] = TextureRegionFactory.createFromAsset(this.m_arrAlphabet[i], this, m_strAlphabet.charAt(i)+".png", 0, 0);
		}

		//(CAMERA_WIDTH/(m_strAlphabet.length()+1))*(m_iAlphabetSpriteCount+1) - m_BoxTexture.getWidth()/2, 50, 
		for(int j=0; j < m_strAlphabet.length(); j++){
			m_arrAlphabetSprite[j] = new AlphabetSprite(randomX.nextInt(xRange),
					randomY.nextInt(yRange), this.m_arrAlphabetTexture[j], j, m_strAlphabet.charAt(j)) {
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

						if (m_arrBoxSprite[m_iCurrentCollideBoxIdx].bFilled){
							m_FailToDropSound.play();
							
							this.addShapeModifier(new MoveModifier(1,
									this.getX(), randomX.nextInt(xRange),
									this.getY(), randomY.nextInt(yRange),
									EaseExponentialOut.getInstance()));
							m_CurrentTouchedAlphabetSprite = null;
							return true;
						}
						
						//play sound
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
						
						//reset screen to next item when user clear the stage
						if(isAllBoxesFilled(m_arrBoxSprite)){
							if(isStageCleared(m_arrBoxSprite)){
								if (m_iCurrentItemNum < ARR_ANIMAL.length)
									m_strAlphabet = ARR_ANIMAL[m_iCurrentItemNum++];
								drawResult(m_PassSprite);
								resetAfterDelay(2500);
							}
							else{								
								drawResult(m_FailSprite);
								popupAfterDelay(2500);								
							}
						}
						m_CurrentTouchedAlphabetSprite = null;
					}
					//Set item size as 1.5times when user try to drag it.
					else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
						m_CurrentTouchedAlphabetSprite = this;
						this.setScale(1.5f);
					}
					//Dragging
					else{				
						m_CurrentTouchedAlphabetSprite = this;
						this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
					}
					return true;
				}			
			};	
			m_Scene.registerTouchArea(m_arrAlphabetSprite[j]);
			m_Scene.getLayer(ENTITIES_LAYER).addEntity(m_arrAlphabetSprite[j]);
		}

		m_Scene.registerTouchArea(m_Item);	
		m_Scene.setTouchAreaBindingEnabled(true);

		// The actual collision-checking.
		m_Scene.registerUpdateHandler(new IUpdateHandler() {

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
	
	private void drawResult(Sprite sprite){
		
		final SequenceShapeModifier shapeModifier = new SequenceShapeModifier(new AlphaModifier(2, 0, 1));
		sprite.addShapeModifier(shapeModifier);
		sprite.setScale(1.5f);
		this.m_Scene.getLayer(ENTITIES_LAYER).addEntity(sprite);
	}

	private void resetAfterDelay(int delayMS){
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				resetScreen();
			}
		}, delayMS);
	}

	private void popupAfterDelay(int delayMS){
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				m_FailSprite.setVisible(false);
				m_Scene.setChildScene(m_RetryScene, false, true, true);
			}
		}, delayMS);
	}
	
	@Override
	public void onLoadComplete() {

	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		Log.e(TAG, "onMenuItemClicked()");
		switch(pMenuItem.getID()) {
		case MENU_RESET:
			this.m_Music.play();
			this.m_Scene.clearChildScene();
			this.m_MenuScene.reset();
			return true;
		case MENU_QUIT:
			this.finish();
			return true;
		default:
			return false;
		}
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

	private boolean isCollide(Sprite alphabet, Sprite box){
		if ((getCenterX(alphabet) > getCenterX(box) - CENTER_OFFSET) 
				&& (getCenterX(alphabet) < getCenterX(box) + CENTER_OFFSET)
				&& (getCenterY(alphabet) > getCenterY(box) - CENTER_OFFSET) 
				&& (getCenterY(alphabet) < getCenterY(box) + CENTER_OFFSET)){
			return true;
		}
		return false;
	}

	private float getCenterX(Sprite s){
		if (s != null)
			return s.getX() + 2/s.getWidth();
		else
			return 0f;
	}

	private float getCenterY(Sprite s){
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

}
