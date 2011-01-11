package com.incrediblekids.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

import com.incrediblekids.util.ItemSizeInfo;
import com.incrediblekids.util.QuizLine;
import com.incrediblekids.util.ThemeSprite;
import com.incrediblekids.util.WordSprite;

public class SummaryQuiz extends BaseGameActivity {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String TAG = "SummaryQuiz";
	private static final int THEME_ITEM_COUNT = 3;
	private static final int WORD_ITEM_COUNT = 3;
	
	public static int CAMERA_WIDTH;
	public static int CAMERA_HEIGHT;
	
	public static int THEME_ITEM_HEIGHT;
	public static int THEME_ITEM_WIDTH;
	public static int THEME_GAP_HEIGHT;
	public static int THEME_GAP_WIDTH;
	public static int THEME_MARGIN_HEIGHT;
	
	public static int WORD_ITEM_HEIGHT;
	public static int WORD_ITEM_WIDTH;
	public static int WORD_GAP_HEIGHT;
	public static int WORD_GAP_WIDTH;
	public static int WORD_MARGIN_HEIGHT;
	
	public static float THEME_ITEM_SCALE;
	public static float WORD_ITEM_SCALE;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Camera m_Camera;
	private ItemSizeInfo m_ThemeItemSizeInfo;
	private ItemSizeInfo m_WordItemSizeInfo;
	
	//Theme item
	private ThemeSprite[] m_Items;
	private Texture[]  m_ItemTextures;
	private TextureRegion[] m_ItemTextureRegion;
	
	//Word item
	private WordSprite[] m_WordItems;
	private Texture[]  m_WordItemTextures;
	private TextureRegion[] m_WordItemTextureRegion;
	
	private Scene m_Scene;
	
	//Line
	private QuizLine[] m_QuizLine;
	
	//Temp
	private ArrayList<String> m_tempString = new ArrayList<String>(5);
	
	
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
		
		m_ThemeItemSizeInfo = new ItemSizeInfo(getApplicationContext(), THEME_ITEM_COUNT);
		CAMERA_WIDTH 		= m_ThemeItemSizeInfo.getLcdWidth();
		CAMERA_HEIGHT 		= m_ThemeItemSizeInfo.getLcdHeight();
		
		THEME_ITEM_HEIGHT	= m_ThemeItemSizeInfo.getItemHeight();
		THEME_ITEM_WIDTH	= m_ThemeItemSizeInfo.getItemHeight();
		THEME_GAP_HEIGHT	= m_ThemeItemSizeInfo.getGapHeight();
		THEME_GAP_WIDTH		= THEME_GAP_HEIGHT * 2;
		THEME_MARGIN_HEIGHT	= m_ThemeItemSizeInfo.getMarginHeight();
		
		m_WordItemSizeInfo 	= new ItemSizeInfo(getApplicationContext(), WORD_ITEM_COUNT);
		WORD_ITEM_HEIGHT	= m_WordItemSizeInfo.getItemHeight();
		WORD_ITEM_WIDTH		= m_WordItemSizeInfo.getItemHeight();
		WORD_GAP_HEIGHT		= m_WordItemSizeInfo.getGapHeight();
		WORD_GAP_WIDTH		= WORD_GAP_HEIGHT * 2;
		WORD_MARGIN_HEIGHT	= m_WordItemSizeInfo.getMarginHeight();
		
		m_Items 			= new ThemeSprite[THEME_ITEM_COUNT];
		m_ItemTextures 		= new Texture[THEME_ITEM_COUNT];
		m_ItemTextureRegion = new TextureRegion[THEME_ITEM_COUNT];
		
		m_WordItems 			= new WordSprite[WORD_ITEM_COUNT];
		m_WordItemTextures 		= new Texture[WORD_ITEM_COUNT];
		m_WordItemTextureRegion = new TextureRegion[WORD_ITEM_COUNT];
		
		m_QuizLine	= new QuizLine[THEME_ITEM_COUNT];
		
		m_tempString.add("Tiger");
		m_tempString.add("Lion");
		m_tempString.add("Cat");
		m_tempString.add("Dog");
		m_tempString.add("Monkey");
		
		Log.d(TAG, "CAMERA_WIDTH: " + CAMERA_WIDTH);
		Log.d(TAG, "CAMERA_HEIGHT: " + CAMERA_HEIGHT);
	}

	@Override
	public void onLoadResources() {
		Log.d(TAG, "onLoadResources()");
		
		// Load Texture
		TextureRegionFactory.setAssetBasePath("gfx/");
		for(int i = 0; i < THEME_ITEM_COUNT; i++) {
			m_ItemTextures[i] = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_ItemTextureRegion[i] = TextureRegionFactory.createFromAsset(this.m_ItemTextures[i], this, "lion.png", 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_ItemTextures[i]);
		}
			
		for(int i = 0; i < WORD_ITEM_COUNT; i++) {
			m_WordItemTextures[i] = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_WordItemTextureRegion[i] = TextureRegionFactory.createFromAsset(this.m_WordItemTextures[i], this, "tiger.png", 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_WordItemTextures[i]);
		}
		
		// getScale;
		m_ThemeItemSizeInfo.setRealItemPixel(m_ItemTextureRegion[0].getHeight());
		THEME_ITEM_SCALE = m_ThemeItemSizeInfo.getMeasuredItemScale();
		
		m_WordItemSizeInfo.setRealItemPixel(m_WordItemTextureRegion[0].getHeight());
		WORD_ITEM_SCALE = m_WordItemSizeInfo.getMeasuredItemScale();
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
			
		for(int i = 0; i < THEME_ITEM_COUNT; i++) {
			m_Scene.getTopLayer().addEntity(m_QuizLine[i]);
			
			m_Items[i].setScale(THEME_ITEM_SCALE);
			m_Scene.getTopLayer().addEntity(m_Items[i]);
			m_Scene.registerTouchArea(m_Items[i]);
		}
			
		for(int i = 0; i < WORD_ITEM_COUNT; i++) {
			m_WordItems[i].setScale(WORD_ITEM_SCALE);
			m_Scene.getTopLayer().addEntity(m_WordItems[i]);
			m_Scene.registerTouchArea(m_WordItems[i]);
		}
		m_Scene.setTouchAreaBindingEnabled(true);
		Log.d(TAG, "scale before X:" + m_Items[0].getX());
		Log.d(TAG, "scale After X:" + m_Items[0].getWidth() + m_Items[0].getWidth() * WORD_ITEM_SCALE);
//		m_Items[0].setPosition(0 - m_Items[0].getWidth() + m_Items[0].getWidth() * THEME_ITEM_SCALE,0);
		Log.d(TAG, "x base: " + m_Items[0].getBaseX());
		Log.d(TAG, "x base: " + m_Items[0].getX());
		Log.d(TAG, "final width: " + m_Items[0].getWidthScaled());
		Log.d(TAG, "final height : " + m_Items[0].getHeightScaled());
		Log.d(TAG, "scale final X:" + m_Items[0].getX());
	}

	private void makeItems() {
		Log.d(TAG, "makeItems()");
		
		Random rnd;
		int tempNum = 0;
		int count = 0;
		float position = 0;
		float leftPosition = 0;
		float rightItemPosition = 0;
		float themePosition = 0;
		float wordPosition = 0;
		
		HashMap<Integer, Integer> randNumHashMap = new HashMap<Integer, Integer>();
		rnd = new Random(System.currentTimeMillis());
		
		while(true) {
			tempNum = Math.abs(rnd.nextInt(4) + 1);
			if(!randNumHashMap.containsValue(tempNum)) {
				randNumHashMap.put(count, tempNum);
				count++;
			}
			if(randNumHashMap.size() == 3)
				break;
		}
		
		
		//TODO: Scale 값에 따라 구분해야됨.
//		leftPosition 		= ((ITEM_WIDTH) - m_ItemTextureRegion[0].getWidth() + GAP_WIDTH) * THEME_ITEM_SCALE;
//		leftPosition 		= THEME_GAP_WIDTH * THEME_ITEM_SCALE - THEME_GAP_WIDTH;
		leftPosition 		= THEME_GAP_WIDTH + m_ItemTextureRegion[0].getHeight() * (THEME_ITEM_SCALE - 1)/2;
		rightItemPosition 	= CAMERA_WIDTH - m_WordItemTextureRegion[0].getWidth()
								- ((WORD_ITEM_WIDTH) - m_WordItemTextureRegion[0].getWidth() + WORD_GAP_WIDTH) * WORD_ITEM_SCALE;
		themePosition 		= ((THEME_ITEM_HEIGHT) - m_ItemTextureRegion[0].getHeight() + THEME_GAP_HEIGHT) * THEME_ITEM_SCALE;
		wordPosition 		= ((WORD_ITEM_HEIGHT) - m_WordItemTextureRegion[0].getHeight() + WORD_GAP_HEIGHT) * WORD_ITEM_SCALE;
		
		rightItemPosition	= CAMERA_WIDTH - m_WordItemTextureRegion[0].getWidth();
		leftPosition 		= 0;
		themePosition = 0;
		wordPosition = 0;
		
		Log.d(TAG, "m_WordItem.Height :" + m_WordItemTextureRegion[0].getHeight());
		Log.d(TAG, "m_WordItem.SCALE :" + WORD_ITEM_SCALE);
		
		Log.d(TAG, "leftPosition : " + leftPosition);
		Log.d(TAG, "position: " + position);
		Log.d(TAG, "rightPosition: " + rightItemPosition);
		Log.d(TAG, "themePosition: "+ themePosition);
		Log.d(TAG, "wordPosition: "+ wordPosition);
		
		for(int i = 0; i < THEME_ITEM_COUNT; i++) {
			m_QuizLine[i] = new QuizLine(0,0,0,0,5f);
			m_QuizLine[i].setColor(0.0f,0.0f,0.0f);
		}
		
		Iterator<Integer> ii = randNumHashMap.keySet().iterator();
		count = 0;
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = randNumHashMap.get(key);
			m_Items[count] = new ThemeSprite(leftPosition, themePosition, m_ItemTextureRegion[count], m_tempString.get(value), m_QuizLine[count]) {
				
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					boolean flag = false;
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						Log.d(TAG, "ACTION_UP");
						Log.d(TAG, "getString: " + this.get_Id());
						for(int i = 0; i < WORD_ITEM_COUNT; i++) {
							Log.d(TAG, "i : " + i);
							if(getLine().collidesWithAnObject(m_WordItems[i])) {
								Log.d(TAG, "i = " + i + ", break");
								getLine().setPosition(getX() + getWidth()/2, getY() + getHeight()/2, 
										m_WordItems[i].getX() + m_WordItems[i].getWidth()/2, m_WordItems[i].getY() + m_WordItems[i].getHeight()/2);
								flag = true;
								break;
							}
							else {
//								getLine().setPosition(0, 0, 0, 0);
							}
						}
						if(!flag) {
							getLine().setPosition(0, 0, 0, 0);
						}
							
					}
					else {	// Drawing Line
						getLine().setPosition(getX() + getWidth()/2, getY() + getHeight()/2, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					}
					return true;
				}
				
			};
			
			count++;
			themePosition = themePosition + THEME_ITEM_HEIGHT + THEME_GAP_HEIGHT;
		}
		
		
		for(int i = 0; i < WORD_ITEM_COUNT; i++) {
			
			/*
			m_Items[i] = new ThemeSprite(leftPosition, themePosition, m_ItemTextureRegion[i], m_tempString.get(i), m_QuizLine[i]) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						Log.d(TAG, "ACTION_UP");
						
						for(int i = 0; i < m_WordItems.length; i++) {
							if(getLine().collidesWithAnObject(m_WordItems[i])) {
								Log.d(TAG, "collides");
								getLine().setPosition(getX() + getWidth()/2, getY() + getHeight()/2, 
										m_WordItems[i].getX() + m_WordItems[i].getWidth()/2, m_WordItems[i].getY() + m_WordItems[i].getHeight()/2);
							}
							else {
								Log.d(TAG, "clear");
								getLine().setPosition(0, 0, 0, 0);

							}
						}
					}
					else {
						getLine().setPosition(getX() + getWidth()/2, getY() + getHeight()/2, 
								pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					}
					return true;
				}
			};
			
			*/
			
			m_WordItems[i] = new WordSprite(rightItemPosition, wordPosition, m_WordItemTextureRegion[i], m_tempString.get(i)) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					return true;
				}
			};
			
			wordPosition = wordPosition + WORD_ITEM_HEIGHT + WORD_GAP_HEIGHT;
			Log.d(TAG, "themePositionAfter: "+ themePosition);
			Log.d(TAG, "wordPositionAfter: "+ wordPosition);
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
