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
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.ColoredTextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
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

import com.incrediblekids.util.AlphabetSprite;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 15:13:46 - 15.06.2010
 */
public class ThemeItemActivity extends BaseGameActivity implements IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	public static int CAMERA_WIDTH;
	public static int CAMERA_HEIGHT;

	public final static int CENTER_OFFSET = 36;
	public final static int BOTTOM_LAYER = 0;
	public final static int TOP_LAYER = 1;
	public final static int MENU_RESET = 0;
	public final static int MENU_QUIT = MENU_RESET + 1;
	public final static String TAG = "TouchDragExample";
	
	public final static String [] ARR_ANIMAL = {"monkey", "lion", "tiger"};

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera m_Camera;
	
	//Empty Boxes to fill alphabet.
	private Texture  m_BoxTexture;
	private TextureRegion m_BoxTextureRegion;
	private Sprite [] m_arrBoxSprite;
	private int m_iBoxSpriteCount;
	private int m_iCurrentCollideBoxIdx;
	
	//Alphabets
	private String m_strAlphabet;
	private Texture [] m_arrAlphabet;
	private TextureRegion [] m_arrAlphabetTexture;
	private AlphabetSprite [] m_arrAlphabetSprite;
	private int m_iAlphabetSpriteCount;
	private Sprite m_CurrentTouchedAlphabetSprite;
	
	//Pause button
	private Texture m_PauseTexture;
	private TextureRegion m_PauseTextureRegion;
	private Sprite m_PauseSprite;
	
	//Theme item
	private Sprite m_Item;
	private Texture  m_ItemTexture;
	private TextureRegion m_ItemTextureRegion;
	private int m_iCurrentItemNum;

	private Scene m_Scene;
	private Scene m_MenuScene;

	//Background Music and sound
	private Music m_Music;
	private Sound m_ItemSound;

	//Custom Font
	private Texture m_FontTexture;
	private Font m_Font;

	private static boolean [] m_bCollide = null;

	
	@Override
	public Engine onLoadEngine() {
		m_iCurrentItemNum = 0;
		CAMERA_WIDTH = getLCDWidth();
		CAMERA_HEIGHT = getLCDHeight();
		m_strAlphabet = ARR_ANIMAL[m_iCurrentItemNum++];
		m_iCurrentCollideBoxIdx = 0;
		this.m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.m_Camera).setNeedsMusic(true).setNeedsSound(true));

	}

	@Override
	public void onLoadResources() {
		Log.e(TAG, "onLoadResources()");
		
		MusicFactory.setAssetBasePath("mfx/");		
		try {
			this.m_Music = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "background.wav");
			this.m_Music.setLooping(true);
			this.m_Music.setVolume(0.5f);
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
		this.m_PauseTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);		
		this.m_BoxTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_ItemTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_PauseTextureRegion = TextureRegionFactory.createFromAsset(this.m_PauseTexture, this, "box.png",0,0);
		this.m_BoxTextureRegion = TextureRegionFactory.createFromAsset(this.m_BoxTexture, this, "box2.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTexture(this.m_BoxTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_ItemTexture);
		this.mEngine.getTextureManager().loadTexture(this.m_PauseTexture);

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

		//Make scene
		m_Scene = new Scene(1);
		m_Scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		m_MenuScene = this.createMenuScene();
		
		//Add all the entities
		this.updateScene();
		
		return m_Scene;
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
				for(int i = 0; i < m_Scene.getLayerCount(); ++i){
					while(m_Scene.getTopLayer().getEntityCount()>0){
						m_Scene.getTopLayer().removeEntity(0);
					}
				}
				mHandler.sendEmptyMessage(0);
			}        	
		});
	}
	
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
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
	};
	
	//Update scene with new entities.
	private void updateScene(){
		
		//Load Sound
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.m_ItemSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, m_strAlphabet+".mp3");
			this.m_ItemSound.setVolume(1.0f);
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
		
		Log.e(TAG, "updateScene()");
		//Check the collision
		m_bCollide = new boolean[m_strAlphabet.length()];
		for (int i=0; i<m_bCollide.length; i++)
			m_bCollide[i] = false;
		
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
		m_Scene.getTopLayer().addEntity(m_PauseSprite);
		m_Scene.registerTouchArea(m_PauseSprite);
		
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
		
		m_Item.setScale(1.5f);
		m_Scene.getTopLayer().addEntity(m_Item);

		//Load Box Sprite to scene.
		m_arrBoxSprite = new Sprite[m_strAlphabet.length()];
		for(m_iBoxSpriteCount=0; m_iBoxSpriteCount < m_strAlphabet.length(); m_iBoxSpriteCount++){
			m_arrBoxSprite[m_iBoxSpriteCount] = new Sprite((CAMERA_WIDTH/(m_strAlphabet.length()+1))*(m_iBoxSpriteCount+1) 
					- m_BoxTexture.getWidth()/2, CAMERA_HEIGHT-m_BoxTexture.getWidth()- CAMERA_HEIGHT/10, this.m_BoxTextureRegion);
			m_Scene.getTopLayer().addEntity(m_arrBoxSprite[m_iBoxSpriteCount]);
		}
		
		//Load Alphabet Sprite to scene
		this.m_arrAlphabetTexture = new TextureRegion[m_strAlphabet.length()];
		this.m_arrAlphabet = new Texture[m_strAlphabet.length()];
		m_arrAlphabetSprite = new AlphabetSprite[m_strAlphabet.length()];
		
		for(int i=0; i<m_strAlphabet.length(); i++){
			this.m_arrAlphabet[i] = new Texture(64,64,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mEngine.getTextureManager().loadTexture(this.m_arrAlphabet[i]);
			this.m_arrAlphabetTexture[i] = TextureRegionFactory.createFromAsset(this.m_arrAlphabet[i], this, m_strAlphabet.charAt(i)+".png", 0, 0);
		}
		final Random randomX = new Random();
		final Random randomY = new Random();
		
		int xRange = CAMERA_WIDTH - m_BoxTexture.getWidth() * 2;
		int yRange = CAMERA_HEIGHT - CAMERA_HEIGHT/3 - m_BoxTexture.getHeight();
		//(CAMERA_WIDTH/(m_strAlphabet.length()+1))*(m_iAlphabetSpriteCount+1) - m_BoxTexture.getWidth()/2, 50, 
		for(m_iAlphabetSpriteCount=0; m_iAlphabetSpriteCount < m_strAlphabet.length(); m_iAlphabetSpriteCount++){
			m_arrAlphabetSprite[m_iAlphabetSpriteCount] = new AlphabetSprite(randomX.nextInt(xRange),
					randomY.nextInt(yRange), this.m_arrAlphabetTexture[m_iAlphabetSpriteCount], m_iAlphabetSpriteCount) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP){
						
						//Change to original Size
						this.setScale(1.0f);
						
						//Collision Check
						if (!m_bCollide[m_iCurrentCollideBoxIdx]){
							this.setbFilled(false);							
							return true;		
						}	
						
						float boxX = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getX();
						float boxY = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getY();
						float boxWidth = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getWidth();
						float boxHeight = m_arrBoxSprite[m_iCurrentCollideBoxIdx].getHeight();
						this.setPosition(boxX + (boxWidth/2 - this.getWidth()/2), boxY + (boxHeight/2 - this.getHeight()/2));
						this.setbFilled(true);

						//set bCorret to true if user position the alphabet to right box.
						if(this.getSequence() == m_iCurrentCollideBoxIdx)
							this.setbCorrect(true);
						else
							this.setbCorrect(false);
						
						//reset screen to next item when user clear the stage
						if(isStageCleared(m_arrAlphabetSprite)){
							if (m_iCurrentItemNum < ARR_ANIMAL.length)
								m_strAlphabet = ARR_ANIMAL[m_iCurrentItemNum++];
							resetScreen();
							Toast.makeText(ThemeItemActivity.this, "Stage cleared", Toast.LENGTH_SHORT).show();
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
			m_Scene.registerTouchArea(m_arrAlphabetSprite[m_iAlphabetSpriteCount]);
			m_Scene.getTopLayer().addEntity(m_arrAlphabetSprite[m_iAlphabetSpriteCount]);

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
						m_bCollide[i] = true;
					} else {
						m_arrBoxSprite[i].setColor(1, 1, 1);
						m_bCollide[i] = false;						
					}
				}
			}
		});
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
	
	private boolean isStageCleared(AlphabetSprite [] sprites){
		boolean result = true;
		for(int i=0; i < sprites.length; i++){
			if (!sprites[i].isFilled() || !sprites[i].isCorrect())
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
		return s.getX() + 2/s.getWidth();
	}

	private float getCenterY(Sprite s){
		return s.getY() + 2/s.getHeight();
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
