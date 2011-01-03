package com.test;

import java.io.IOException;
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
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 15:13:46 - 15.06.2010
 */
public class TouchDragExample extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	public static int CAMERA_WIDTH;
	public static int CAMERA_HEIGHT;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	//private Texture  mWordTexture;
	private Texture  mBoxTexture;
	//private TextureRegion mFaceTextureRegion;
	private TextureRegion mBoxTextureRegion;
	private TextureRegion mItemTextureRegion;
	private Texture  mItemTexture;
	private Sprite mItem;
	
	private Scene mScene;
	private Sprite [] mBox;
	private AlphabetSprite [] mWord;
	private Sprite mCurrentTouchedWord;
	private int mBoxSpriteCount;
	private int mWordSpriteCount;
	private int mCurrentCollideBoxIdx;

	//alphabet
	private Texture [] mAlphabet;
	private TextureRegion [] mAlphabetTexture;
	
	private Music mMusic;
	private Sound mItemSound;
	
	private int mStageCount;
	
	private String m_strAlphabet;

	private static boolean [] m_bCollide = null;

	public final static int CENTER_OFFSET = 36;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		CAMERA_WIDTH = getLCDWidth();
		CAMERA_HEIGHT = getLCDHeight();
		m_strAlphabet = "tiger";
		Log.e("Wooram", "width="+CAMERA_WIDTH + " height="+CAMERA_HEIGHT);
		mCurrentCollideBoxIdx = 0;
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera).setNeedsMusic(true).setNeedsSound(true));

	}
	int getLCDWidth() {
	    Display display = getWindowManager().getDefaultDisplay();
	    int width = display.getWidth();

	    return width;
	}
	int getLCDHeight() {
	    Display display = getWindowManager().getDefaultDisplay();
	    int height = display.getHeight();

	    return height;
	}

	@Override
	public void onLoadResources() {
		Log.e("WOORAM", "onLoadResources");
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBoxTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mItemTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBoxTextureRegion = TextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box2.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBoxTexture);
		this.mEngine.getTextureManager().loadTexture(this.mItemTexture);
		
		MusicFactory.setAssetBasePath("mfx/");
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "background.wav");
			this.mMusic.setLooping(true);
			this.mMusic.setVolume(1f);
			
			this.mItemSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "explosion.ogg");
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(mMusic != null && !mMusic.isPlaying()) {
			mMusic.stop();
		}
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		mScene = new Scene(2);
		
		setScene(m_strAlphabet);
		if(mMusic != null && !mMusic.isPlaying()) {
			mMusic.play();
		}

		return mScene;
	}
	
	private void resetScreen(){
		Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    /* Now it is save to remove the entity! */
            	ILayer topLayer = mScene.getTopLayer();
            	ILayer bottomLayer = mScene.getTopLayer();
            	for (int i=0; i < topLayer.getEntityCount(); i++)
            		topLayer.removeEntity(i);
            	for (int i=0; i < bottomLayer.getEntityCount(); i++)
            		bottomLayer.removeEntity(i);
            	Log.e("Wooram", "resetScreen end");
            	handler.sendEmptyMessage(0);
    			//mScene.reset();
            }
		});
		thread.run();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Log.e("Wooram", "handleMessage() start");
			mScene.clearUpdateHandlers();
			mScene.clearTouchAreas();	
			mBoxSpriteCount = 0;
			mWordSpriteCount = 0;
			mCurrentCollideBoxIdx = 0;
			mCurrentTouchedWord = null;
			
			onLoadScene();
			
			mEngine.setScene(null);
	        mEngine.setScene(mScene);
		}
	};
	
	private void setScene(final String alphabet){
		Log.e("WOORAM", "setScene");
		
		this.mItemTextureRegion = TextureRegionFactory.createFromAsset(this.mItemTexture, this, alphabet+".png", 0, 0);
		this.mAlphabetTexture = new TextureRegion[alphabet.length()];
		this.mAlphabet = new Texture[alphabet.length()];

		for(int i=0; i<alphabet.length(); i++){
			
			this.mAlphabet[i] = new Texture(64,64,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mEngine.getTextureManager().loadTexture(this.mAlphabet[i]);
			this.mAlphabetTexture[i] = TextureRegionFactory.createFromAsset(this.mAlphabet[i], this, alphabet.charAt(i)+".png", 0, 0);
		}
		
		m_bCollide = new boolean[alphabet.length()];
		for (int i=0; i<m_bCollide.length; i++)
			m_bCollide[i] = false;
		
		mBox = new Sprite[alphabet.length()];
		mWord = new AlphabetSprite[alphabet.length()];
		mScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		for(mBoxSpriteCount=0; mBoxSpriteCount < alphabet.length(); mBoxSpriteCount++){
			
			mBox[mBoxSpriteCount] = new Sprite((CAMERA_WIDTH/(alphabet.length()+1))*(mBoxSpriteCount+1) 
					- mBoxTexture.getWidth()/2, CAMERA_HEIGHT-mBoxTexture.getWidth()- CAMERA_HEIGHT/10, this.mBoxTextureRegion);
			mScene.getTopLayer().addEntity(mBox[mBoxSpriteCount]);
		}
		for(mWordSpriteCount=0; mWordSpriteCount < alphabet.length(); mWordSpriteCount++){
			mWord[mWordSpriteCount] = new AlphabetSprite((CAMERA_WIDTH/(alphabet.length()+1))*(mWordSpriteCount+1) 
					- mBoxTexture.getWidth()/2, 50, this.mAlphabetTexture[mWordSpriteCount], mWordSpriteCount) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP){
						Log.e("WOORAM", "Index="+ mCurrentCollideBoxIdx + " m_bCollide="+m_bCollide);
						this.setScale(1.0f);
						if (!m_bCollide[mCurrentCollideBoxIdx]){
							this.setbFilled(false);							
							return true;		
						}				
						float boxX = mBox[mCurrentCollideBoxIdx].getX();
						float boxY = mBox[mCurrentCollideBoxIdx].getY();
						float boxWidth = mBox[mCurrentCollideBoxIdx].getWidth();
						float boxHeight = mBox[mCurrentCollideBoxIdx].getHeight();
						this.setPosition(boxX + (boxWidth/2 - this.getWidth()/2), boxY + (boxHeight/2 - this.getHeight()/2));
						this.setbFilled(true);
						
						if(this.getSequence() == mCurrentCollideBoxIdx)
							this.setbCorrect(true);
						else
							this.setbCorrect(false);
						if(isStageCleared(mWord)){
							m_strAlphabet = "lion";
							resetScreen();
							Toast.makeText(TouchDragExample.this, "Stage cleared", Toast.LENGTH_SHORT).show();
						}
						mCurrentTouchedWord = null;
					}
					else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
						mCurrentTouchedWord = this;
						this.setScale(1.5f);
					}
					else{				
						Log.e("WOORAM", "alphabet sprite width=" + this.getWidth());
						mCurrentTouchedWord = this;
						this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
					}
					return true;
				}			
			};	
			mScene.registerTouchArea(mWord[mWordSpriteCount]);
			mScene.getTopLayer().addEntity(mWord[mWordSpriteCount]);

		}
		this.mItem = new Sprite(CAMERA_WIDTH/2 - mItemTextureRegion.getWidth()/2
				,CAMERA_HEIGHT/8, this.mItemTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && mItemSound!=null)
					mItemSound.play();
				return true;
			}
		};
		mItem.setScale(1.5f);
		mScene.registerTouchArea(mItem);		
		mScene.getBottomLayer().addEntity(mItem);
		mScene.setTouchAreaBindingEnabled(true);
		/* The actual collision-checking. */
		mScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				for(int i=0; i < alphabet.length(); i++ ){
					
					if (mCurrentTouchedWord == null){
						mBox[i].setColor(1, 1, 1);
						continue;
					}						
					if(isCollide(mCurrentTouchedWord,mBox[i])){
						mBox[i].setColor(0, 0, 1);
						mCurrentCollideBoxIdx = i;
						m_bCollide[i] = true;
					} else {
						m_bCollide[i] = false;
						mBox[i].setColor(1, 1, 1);
					}
				}
			}
		});
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
	
	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
