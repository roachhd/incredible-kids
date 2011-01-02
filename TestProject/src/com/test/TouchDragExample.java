package com.test;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
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

import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

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
	
	private Scene mScene;
	private Sprite [] mBox;
	private Sprite [] mWord;
	private Sprite mCurrentTouchedWord;
	private Sprite mCurrentCollideBox;
	private int mBoxSpriteCount;
	private int mWordSpriteCount;
	private int mCurrentCollideBoxIdx;
	
	private ArrayList <AlphabetItem> arrItem;
	
	
	//match
	private int mCardCount;

	//alphabet
	private Texture [] mAlphabet;
	private TextureRegion [] mAlphabetTexture;

	private static boolean [] m_bCollide = null;

	public final static int CENTER_OFFSET = 10;
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
		arrItem = new ArrayList<AlphabetItem>();
		Log.e("Wooram", "width="+CAMERA_WIDTH + " height="+CAMERA_HEIGHT);
		mCurrentCollideBoxIdx = 0;
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));

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
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		//this.mWordTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mBoxTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		//this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mWordTexture, this, "box1.png", 0, 0);
		
		this.mBoxTextureRegion = TextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box2.png", 0, 0);

		
		this.mEngine.getTextureManager().loadTexture(this.mBoxTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mScene = new Scene(1);
		String [] alphabet = {"d","o","g"};
		setScene(alphabet);
		return mScene;
	}
	

	private void setScene(final String [] alphabet){
		this.mAlphabetTexture = new TextureRegion[alphabet.length];
		this.mAlphabet = new Texture[alphabet.length];
		for(int i=0; i<alphabet.length; i++){
			
			this.mAlphabet[i] = new Texture(64,64,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mEngine.getTextureManager().loadTexture(this.mAlphabet[i]);
			this.mAlphabetTexture[i] = TextureRegionFactory.createFromAsset(this.mAlphabet[i], this, alphabet[i]+".png", 0, 0);
		}

		m_bCollide = new boolean[alphabet.length];
		for (int i=0; i<m_bCollide.length; i++)
			m_bCollide[i] = false;
		mBox = new Sprite[alphabet.length];
		mWord = new Sprite[alphabet.length];
		mScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		for(mBoxSpriteCount=0; mBoxSpriteCount < alphabet.length; mBoxSpriteCount++){
			
			mBox[mBoxSpriteCount] = new Sprite((CAMERA_WIDTH/(alphabet.length+1))*(mBoxSpriteCount+1) 
					- mBoxTexture.getWidth()/2, CAMERA_HEIGHT-mBoxTexture.getWidth()- CAMERA_HEIGHT/10, this.mBoxTextureRegion);
			mScene.getTopLayer().addEntity(mBox[mBoxSpriteCount]);
		}
		
		for(mWordSpriteCount=0; mWordSpriteCount < alphabet.length; mWordSpriteCount++){
			mWord[mWordSpriteCount] = new Sprite((CAMERA_WIDTH/(alphabet.length+1))*(mWordSpriteCount+1) 
					- mBoxTexture.getWidth()/2, 50, this.mAlphabetTexture[mWordSpriteCount]) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP){
						Log.e("WOORAM", "Index="+ mCurrentCollideBoxIdx + " m_bCollide="+m_bCollide);
						if (!m_bCollide[mCurrentCollideBoxIdx])
							return true;		
						
						this.setPosition(mBox[mCurrentCollideBoxIdx].getX(), mBox[mCurrentCollideBoxIdx].getY());
					}else{
						mCurrentTouchedWord = this;
						this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
					}
					return true;
				}			
			};	
			mScene.getTopLayer().addEntity(mWord[mWordSpriteCount]);
			mScene.registerTouchArea(mWord[mWordSpriteCount]);
			mScene.setTouchAreaBindingEnabled(true);
		}
		
		/* The actual collision-checking. */
		mScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {

				for(int i=0; i < alphabet.length; i++ ){
					if (mCurrentTouchedWord == null)
						break;
					if(isCollide(mCurrentTouchedWord,mBox[i])){//mCurrentTouchedWord.collidesWith(mBox[i])) {
						mBox[i].setColor(0, 0, 1);
						//Log.e("WOORAM", "onUpdate Index="+ mCurrentCollideBoxIdx);
						mCurrentCollideBoxIdx = i;
						mCurrentCollideBox = mBox[mCurrentCollideBoxIdx];
						m_bCollide[i] = true;
					} else {
						//Log.e("WOORAM", "onUpdate m_bCollide set falase");
						m_bCollide[i] = false;
						mBox[i].setColor(1, 1, 1);
					}


				}
			}
		});
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
	
	private boolean isMatched(){
		return false;
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
