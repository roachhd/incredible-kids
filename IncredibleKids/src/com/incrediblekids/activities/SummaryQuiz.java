package com.incrediblekids.activities;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
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

import com.incrediblekids.util.ItemSizeInfo;

public class SummaryQuiz extends BaseGameActivity {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String TAG = "SummaryQuiz";
	private static final int ITEM_COUNT = 5;
	private final int LEFT_ALIGN 		= 1;
	private final int RIGHT_ALIGN 		= 2;
	
	public static int CAMERA_WIDTH;
	public static int CAMERA_HEIGHT;
	public static int ITEM_HEIGHT;
	public static int ITEM_WIDTH;
	public static int GAP_HEIGHT;
	public static int GAP_WIDTH;
	
	public static float THEME_ITEM_SCALE;
	public static float WORD_ITEM_SCALE;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Camera m_Camera;
	private ItemSizeInfo m_ItemSizeInfo;
	
	//Theme item
	private Sprite[] m_Items;
	private Texture[]  m_ItemTextures;
	private TextureRegion[] m_ItemTextureRegion;
	
	//Word item
	private Sprite[] m_WordItems;
	private Texture[]  m_WordItemTextures;
	private TextureRegion[] m_WordItemTextureRegion;
	
	private Scene m_Scene;
	
	@Override
	public Engine onLoadEngine() {
		Log.d(TAG, "onLoadEngine()");
		
		init();
		m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), 
				m_Camera).setNeedsMusic(true).setNeedsSound(true));
	}

	private void init() {
		Log.d(TAG, "init()");
		m_ItemSizeInfo = new ItemSizeInfo(getApplicationContext());
		CAMERA_WIDTH 	= m_ItemSizeInfo.getLcdWidth();
		CAMERA_HEIGHT 	= m_ItemSizeInfo.getLcdHeight();
		ITEM_HEIGHT		= m_ItemSizeInfo.getItemHeight();
		ITEM_WIDTH		= m_ItemSizeInfo.getItemHeight();
		GAP_HEIGHT		= m_ItemSizeInfo.getGapHeight();
		GAP_WIDTH		= GAP_HEIGHT * 2;
		
		m_Items 			= new Sprite[5];
		m_ItemTextures 		= new Texture[5];
		m_ItemTextureRegion = new TextureRegion[5];
		
		m_WordItems 			= new Sprite[5];
		m_WordItemTextures 		= new Texture[5];
		m_WordItemTextureRegion = new TextureRegion[5];
		
		Log.d(TAG, "CAMERA_WIDTH: " + CAMERA_WIDTH);
		Log.d(TAG, "CAMERA_HEIGHT: " + CAMERA_HEIGHT);
		Log.d(TAG, "ITEM_HEIGHT: " + ITEM_HEIGHT);
		Log.d(TAG, "GAP_HEIGHT: " + GAP_HEIGHT);
	}

	@Override
	public void onLoadResources() {
		Log.d(TAG, "onLoadResources()");
		
		// Load Texture
		TextureRegionFactory.setAssetBasePath("gfx/");
		for(int i = 0; i < ITEM_COUNT; i++) {
			m_ItemTextures[i] = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_ItemTextureRegion[i] = TextureRegionFactory.createFromAsset(this.m_ItemTextures[i], this, "lion.png", 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_ItemTextures[i]);
			
			m_WordItemTextures[i] = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_WordItemTextureRegion[i] = TextureRegionFactory.createFromAsset(this.m_WordItemTextures[i], this, "a.png", 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_WordItemTextures[i]);
		}
		
		// getScale;
		m_ItemSizeInfo.setRealItemPixel(m_ItemTextureRegion[0].getHeight());
		THEME_ITEM_SCALE = m_ItemSizeInfo.getMeasuredItemScale();
		m_ItemSizeInfo.setRealItemPixel(m_WordItemTextureRegion[0].getHeight());
		WORD_ITEM_SCALE = m_ItemSizeInfo.getMeasuredItemScale();
	}

	@Override
	public Scene onLoadScene() {
		Log.d(TAG, "onLoadScene()");
		this.mEngine.registerUpdateHandler(new FPSLogger());

		//Make scene
		m_Scene = new Scene(1);
		m_Scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		//Add all the entities
		updateScene();
		
		return m_Scene;
	}
	
	
	//Update scene with new entities.
	private void updateScene() {
		Log.d(TAG, "updateScene()");
		
		makeItems();
		
		for(int i = 0; i < ITEM_COUNT; i++) {
			m_Items[i].setScale(THEME_ITEM_SCALE);
			m_Scene.getTopLayer().addEntity(m_Items[i]);
			m_Scene.registerTouchArea(m_Items[i]);
			
			m_WordItems[i].setScale(WORD_ITEM_SCALE);
			m_Scene.getTopLayer().addEntity(m_WordItems[i]);
			m_Scene.registerTouchArea(m_WordItems[i]);
		}
		m_Scene.setTouchAreaBindingEnabled(true);
	}

	private void makeItems() {
		Log.d(TAG, "makeItems()");
		
		float position = 0;
		float leftPosition = 0;
		float rightItemPosition = 0;
		float themePosition = 0;
		float wordPosition = 0;
		
		leftPosition 		= ((ITEM_WIDTH) - m_ItemTextureRegion[0].getWidth() + GAP_WIDTH) * THEME_ITEM_SCALE;
		rightItemPosition 	= CAMERA_WIDTH - m_WordItemTextureRegion[0].getWidth()
								- ((ITEM_WIDTH) - m_WordItemTextureRegion[0].getWidth() + GAP_WIDTH) * WORD_ITEM_SCALE;
		themePosition 		= ((ITEM_HEIGHT) - m_ItemTextureRegion[0].getHeight() + GAP_HEIGHT ) * THEME_ITEM_SCALE;
		wordPosition 		= ((ITEM_HEIGHT) - m_WordItemTextureRegion[0].getHeight() + GAP_HEIGHT ) * WORD_ITEM_SCALE;
		
		Log.d(TAG, "leftPosition : " + leftPosition);
		Log.d(TAG, "position: " + position);
		Log.d(TAG, "rightPosition: " + rightItemPosition);
		
		for(int i = 0; i < ITEM_COUNT; i++) {
			m_Items[i] = new Sprite(leftPosition, themePosition, m_ItemTextureRegion[i]) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					return true;
				}
			};
			
			m_WordItems[i] = new Sprite(rightItemPosition, wordPosition, m_WordItemTextureRegion[i]) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					return true;
				}
			};
			
			themePosition = themePosition + ITEM_HEIGHT + GAP_HEIGHT;
			wordPosition = wordPosition + ITEM_HEIGHT + GAP_HEIGHT;
		}
	}
	
	@Override
	public void onLoadComplete() {
		Log.d(TAG, "onLoadComplete()");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		freeMemory();
	}
	
	private void freeMemory() {
		Log.d(TAG, "freeMemory()");
		
	}
}
