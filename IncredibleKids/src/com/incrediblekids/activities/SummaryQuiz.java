package com.incrediblekids.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.incrediblekids.activities.ResourceClass.Item;
import com.incrediblekids.util.ItemSizeInfo;
import com.incrediblekids.util.QuizLine;
import com.incrediblekids.util.ThemeSprite;
import com.incrediblekids.util.WordSprite;

public class SummaryQuiz extends BaseGameActivity {
	//	TODO: O,X 타이밍
	//	TODO: Hint, Sound on
	//	TODO: 선 그리기
	
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String TAG = "SummaryQuiz";
	private static final int THEME_ITEM_COUNT = 3;
	private static final int WORD_ITEM_COUNT = 3;
	
	public static float CAMERA_WIDTH;
	public static float CAMERA_HEIGHT;
	
	public static float THEME_ITEM_HEIGHT;
	public static float THEME_GAP_HEIGHT;
	public static float THEME_GAP_WIDTH;
	public static float THEME_MARGIN_HEIGHT;
	public static float THEME_POINT_GAP;
	
	public static float WORD_ITEM_HEIGHT;
	public static float WORD_GAP_HEIGHT;
	public static float WORD_GAP_WIDTH;
	public static float WORD_MARGIN_HEIGHT;
	public static float WORD_POINT_GAP;
	
	public static float THEME_ITEM_SCALE;
	public static float THEME_ITEM_SCALE_X;
	public static float WORD_ITEM_SCALE;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Camera m_Camera;
	private ItemSizeInfo m_ThemeItemSizeInfo;
	private ItemSizeInfo m_WordItemSizeInfo;
	
	//Background image
	private Texture m_BackgroundTexture;
	private TextureRegion m_BackgroundTextureRegion;
	private Sprite m_BackgroundSprite;
	
	//Theme item
	private ThemeSprite[] m_Items;
	private Texture[]  m_ItemTextures;
	private TextureRegion[] m_ItemTextureRegion;
	
	//Word item
	private WordSprite[] m_WordItems;
	private Texture[]  m_WordItemTextures;
	private TextureRegion[] m_WordItemTextureRegion;
	
	//Pass
	private Texture m_PassTexture;
	private TextureRegion m_PassTextureRegion;
	private Sprite m_PassSprite;
	
	//Fail
	private Texture m_FailTexture;
	private TextureRegion m_FailTextureRegion;
	private Sprite m_FailSprite;
	
	//Current result Sprite
	private Sprite m_CurrentResult;
	
	private Scene m_Scene;
	
	//Line
//	private QuizLine[] m_QuizLine;
	
	private HashMap<Integer, Integer> m_RandomHashMap;
	
	private ResourceClass m_Res;
	private Vector<Item> m_ItemVector;
	
	private ImageLineView mView;
	
	private float m_fLineStartX;
	private float m_fLineStartY;
	private float m_fLineLastX;
	private float m_fLineLastY;
	
	private class ImageLineView extends View {

		public ImageLineView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			Paint Pnt = new Paint();
			Pnt.setAntiAlias(true);
			
			Bitmap point = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.red_dot);
			
			Pnt.setShader(new BitmapShader(point, TileMode.MIRROR, TileMode.REPEAT));
			Pnt.setStrokeWidth(5);
			canvas.drawLine(m_fLineStartX, m_fLineStartY, m_fLineLastX, m_fLineLastY, Pnt);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			Log.d(TAG, "onTouchEvent()");
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				Log.d(TAG, "ACTION_UP");
				m_fLineLastX = event.getX();
				m_fLineLastY = event.getY();
				invalidate();
			}
			else if(event.getAction() == MotionEvent.ACTION_UP) {
				
			}
			return false;
		}
		
	}
	
	@Override
	public Engine onLoadEngine() {
		Log.d(TAG, "onLoadEngine()");
		
		init();
		m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), 
				m_Camera).setNeedsMusic(true).setNeedsSound(true));
	}
	
	

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		mView = new ImageLineView(this);
		super.onCreate(pSavedInstanceState);
		addContentView(mView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}



	private void init() {
		Log.d(TAG, "init()");
		
		m_ThemeItemSizeInfo = new ItemSizeInfo(getApplicationContext(), THEME_ITEM_COUNT);
		
		CAMERA_WIDTH 		= m_ThemeItemSizeInfo.getLcdWidth();
		CAMERA_HEIGHT 		= m_ThemeItemSizeInfo.getLcdHeight();
		
		THEME_ITEM_HEIGHT	= m_ThemeItemSizeInfo.getItemHeight();
		THEME_GAP_HEIGHT	= m_ThemeItemSizeInfo.getGapHeight();
		THEME_GAP_WIDTH		= THEME_GAP_HEIGHT;
		THEME_MARGIN_HEIGHT	= m_ThemeItemSizeInfo.getMarginHeight();
//		THEME_POINT_GAP     = (ItemSizeInfo.getDP_X(10)) * ItemSizeInfo.DENSITY;
		THEME_POINT_GAP     = (ItemSizeInfo.getDP_X(25)) / ItemSizeInfo.DENSITY;
		
		Log.d(TAG, "THEME_ITEM_HEIGHT:" + THEME_ITEM_HEIGHT);
		Log.d(TAG, "THEME_GAP_HEIGHT:" + THEME_GAP_HEIGHT);
		Log.d(TAG, "THEME_GAP_WIDTH:" + THEME_GAP_WIDTH);
		Log.d(TAG, "THEME_MARGIN_HEIGHT:" + THEME_MARGIN_HEIGHT);
		
		m_WordItemSizeInfo 	= new ItemSizeInfo(getApplicationContext(), WORD_ITEM_COUNT);
		WORD_ITEM_HEIGHT	= m_WordItemSizeInfo.getItemHeight();
		WORD_GAP_HEIGHT		= m_WordItemSizeInfo.getGapHeight();
		WORD_GAP_WIDTH		= WORD_GAP_HEIGHT;
		WORD_MARGIN_HEIGHT	= m_WordItemSizeInfo.getMarginHeight();
		WORD_POINT_GAP     	= (ItemSizeInfo.getDP_X(25)) / ItemSizeInfo.DENSITY;
		
		m_Items 			= new ThemeSprite[THEME_ITEM_COUNT];
		m_ItemTextures 		= new Texture[THEME_ITEM_COUNT];
		m_ItemTextureRegion = new TextureRegion[THEME_ITEM_COUNT];
		
		m_WordItems 			= new WordSprite[WORD_ITEM_COUNT];
		m_WordItemTextures 		= new Texture[WORD_ITEM_COUNT];
		m_WordItemTextureRegion = new TextureRegion[WORD_ITEM_COUNT];
		
		m_Res = ResourceClass.getInstance();
		m_ItemVector = m_Res.getvItems();
	}

	@Override
	public void onLoadResources() {
		Log.d(TAG, "onLoadResources()");
		
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		//Background image
		m_BackgroundTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		m_BackgroundTextureRegion = TextureRegionFactory.createFromResource(m_BackgroundTexture, this, R.drawable.background_1, 0, 0);
		this.mEngine.getTextureManager().loadTexture(m_BackgroundTexture);
	
		makeRandomHashMap();
		
		//Load Theme & Word Texture
		Iterator<Integer> ii = m_RandomHashMap.keySet().iterator();
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = m_RandomHashMap.get(key);
			
			m_ItemTextures[key] = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_ItemTextureRegion[key] = TextureRegionFactory.createFromResource(m_ItemTextures[key], this, m_ItemVector.get(value).iItemImgId, 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_ItemTextures[key]);
			
			m_WordItemTextures[key] = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_WordItemTextureRegion[key] = TextureRegionFactory.createFromResource(m_WordItemTextures[key], this, m_ItemVector.get(value).iWordImgId, 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_WordItemTextures[key]);
		}
		
		/**
		Iterator<Integer> ii = m_RandomHashMap.keySet().iterator();
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = m_RandomHashMap.get(key);
			m_ItemTextures[key] = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_ItemTextureRegion[key] = TextureRegionFactory.createFromAsset(this.m_ItemTextures[key], this, m_tempString.get(value) + ".png", 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_ItemTextures[key]);
			
			m_PointTextures[key] = new Texture(16, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			m_PointTextureRegion[key] = TextureRegionFactory.createFromResource(this.m_PointTextures[key], this, R.drawable.dot_16, 0, 0);
			this.mEngine.getTextureManager().loadTexture(m_PointTextures[key]);
		}
		**/
		
		//Load pass texture
		m_PassTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		m_PassTextureRegion = TextureRegionFactory.createFromResource(m_PassTexture, this, R.drawable.pass_128, 0, 0);
		this.mEngine.getTextureManager().loadTexture(m_PassTexture);
		
		//Load fail texture
		m_FailTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		m_FailTextureRegion = TextureRegionFactory.createFromResource(m_FailTexture, this, R.drawable.fail_128, 0, 0);
		this.mEngine.getTextureManager().loadTexture(m_FailTexture);
		
		// getScale;
		m_ThemeItemSizeInfo.setRealItemPixel(m_ItemTextureRegion[0].getHeight());
		THEME_ITEM_SCALE = m_ThemeItemSizeInfo.getMeasuredItemScale();
		Log.d(TAG, "THEME_ITEM_SCALE: " + THEME_ITEM_SCALE);
		
		m_WordItemSizeInfo.setRealItemPixel(m_WordItemTextureRegion[0].getHeight());
		WORD_ITEM_SCALE = m_WordItemSizeInfo.getMeasuredItemScale();
		Log.d(TAG, "WORD_ITEM_SCALE: " + THEME_ITEM_SCALE);
		
		makeItems();
	}

	private void makeRandomHashMap() {
		Log.d(TAG, "makeRandomHashMap()");
		Random rnd;
		int tempNum = 0;
		int count = 0;
		
		m_RandomHashMap = new HashMap<Integer, Integer>();
		rnd = new Random(System.currentTimeMillis());
		
		while(true) {
			tempNum = Math.abs(rnd.nextInt(4) + 1);
			if(!m_RandomHashMap.containsValue(tempNum)) {
				m_RandomHashMap.put(count, tempNum);
				count++;
			}
			if(m_RandomHashMap.size() == 3)
				break;
		}
	}

	@Override
	public Scene onLoadScene() {
		Log.d(TAG, "onLoadScene()");
		this.mEngine.registerUpdateHandler(new FPSLogger());

		//Make scene
		m_Scene = new Scene(1);
		
		//Add all the entities
		updateScene();
		
		layoutItemPosition();
		
		return m_Scene;
	}
	
	private void layoutItemPosition() {
		Log.d(TAG, "layoutItemPosition()");
		
		float themePosition = THEME_GAP_HEIGHT;
		float wordPosition	= WORD_GAP_HEIGHT + WORD_MARGIN_HEIGHT;
		float maxWidth = 0;
		int index;
		int count = 0;
		
		ArrayList<Integer> tempList = new ArrayList<Integer>(3);
		Random rnd;
		
		for(int i = 0; i < THEME_ITEM_COUNT; i++) {
			
			Log.d(TAG, "before x : " + m_Items[i].getX());
			m_Items[i].setPosition(ItemSizeInfo.getDP_X(THEME_GAP_WIDTH) * ItemSizeInfo.DENSITY, themePosition);
			m_Items[i].getPoint().setPosition(m_Items[i].getX() + m_Items[i].getWidthScaled() - THEME_POINT_GAP / ItemSizeInfo.DENSITY,
					m_Items[i].getY() + m_Items[i].getHeightScaled()/2);
			
			themePosition = themePosition + THEME_ITEM_HEIGHT + THEME_GAP_HEIGHT;
		}
		
		// calculator maxWidth
		for(int i = 0; i < WORD_ITEM_COUNT; i++) {
			float width = m_WordItems[i].getWidthScaled();
			if(maxWidth < width)
				maxWidth = width;
		}
		Log.d(TAG, "maxWidth: " + maxWidth);
		
		rnd = new Random(System.currentTimeMillis());
		
		while(true) {
			index = Math.abs(rnd.nextInt(3));
			Log.d(TAG, "tempNum: " + index);
			if(!tempList.contains(index)) {
				tempList.add(index);
				
				m_WordItems[index].setPosition(CAMERA_WIDTH - m_WordItems[index].getWidthScaled(), wordPosition);
				Log.d(TAG, "widthScale : " + m_WordItems[index].getWidthScaled());

				wordPosition = wordPosition + WORD_ITEM_HEIGHT + WORD_GAP_HEIGHT;
				count++;
			}
			if(count == WORD_ITEM_COUNT)
				break;
		}
	}

	//Update scene with new entities.
	private void updateScene() {
		Log.d(TAG, "updateScene()");
		
		m_Scene.getTopLayer().addEntity(m_BackgroundSprite);
		for(int i = 0; i < THEME_ITEM_COUNT; i++) {
			m_Items[i].setScaleCenter(0, 0);
			m_Items[i].setScale(THEME_ITEM_SCALE * ItemSizeInfo.DP_SCALE_X, THEME_ITEM_SCALE);
			
			m_Scene.getTopLayer().addEntity(m_Items[i].getLine());
			m_Scene.getTopLayer().addEntity(m_Items[i]);
			m_Scene.getTopLayer().addEntity(m_Items[i].getPoint());
			m_Scene.registerTouchArea(m_Items[i].getPoint());
		}
		
		for(int i = 0; i < WORD_ITEM_COUNT; i++) {
			m_WordItems[i].setScaleCenter(0, 0);
			m_WordItems[i].setScale(WORD_ITEM_SCALE * ItemSizeInfo.DP_SCALE_X, WORD_ITEM_SCALE);
			
			m_Scene.getTopLayer().addEntity(m_WordItems[i]);
			m_Scene.getTopLayer().addEntity(m_WordItems[i].getPoint());
			m_Scene.registerTouchArea(m_WordItems[i].getPoint());
		}
		
		m_Scene.setTouchAreaBindingEnabled(true);
	}

	private void makeItems() {
		Log.d(TAG, "makeItems()");
		
		Iterator<Integer> ii = m_RandomHashMap.keySet().iterator();
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = m_RandomHashMap.get(key);
			
			m_Items[key] 		= new ThemeSprite(0, 0, m_ItemTextureRegion[key], m_ItemVector.get(value).strWordCharId, getApplicationContext());
			m_WordItems[key] 	= new WordSprite(0, 0, m_WordItemTextureRegion[key], m_ItemVector.get(value).strWordCharId, getApplicationContext());
			
			final ThemeSprite item = m_Items[key];
			
			m_Items[key].getPoint().setOnAreaTouchListener(new ITouchArea() {
				
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						Log.d(TAG, "ACTION_UP");
						// Test
						item.getLine().setVisible(false);
					}
					else {
						m_fLineStartX = item.getPoint().getX() + item.getPoint().getWidth()/2;
						m_fLineStartY = item.getPoint().getY() + item.getPoint().getHeight()/2;
						/*
						item.getLine().setPosition(item.getPoint().getX() + item.getPoint().getWidth()/2, item.getPoint().getY() + item.getPoint().getHeight()/2,
								pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
								*/
						
					}
					mView.onTouchEvent(pSceneTouchEvent.getMotionEvent());
					return true;
				}
				
				@Override
				public float[] convertSceneToLocalCoordinates(float pX, float pY) {
					return null;
				}
				@Override
				public float[] convertLocalToSceneCoordinates(float pX, float pY) {
					return null;
				}
				@Override
				public boolean contains(float pX, float pY) {
					return false;
				}
			});
			
			this.mEngine.getTextureManager().loadTexture(m_Items[key].getPointTexture());
			this.mEngine.getTextureManager().loadTexture(m_WordItems[key].getPointTexture());
		}
		
		m_PassSprite = new Sprite(CAMERA_WIDTH / 2- (m_PassTexture.getWidth() / 2),
				CAMERA_HEIGHT / 2 - (m_PassTexture.getHeight()/2), m_PassTextureRegion);		
		
		m_FailSprite = new Sprite(CAMERA_WIDTH / 2- (m_PassTexture.getWidth() / 2),
				CAMERA_HEIGHT / 2 - (m_FailTexture.getHeight()/2), m_FailTextureRegion);		
		
		m_BackgroundSprite = new Sprite(0, 0, m_BackgroundTextureRegion); 
	}
	
	@Deprecated
	private void makeItemsTemp() {
		Log.d(TAG, "makeItems()");
		
		int count = 0;
		float position = 0;
		float leftPosition = 0;
		float rightItemPosition = 0;
		float themePosition = 0;
		float wordPosition = 0;
		
		//TODO: Scale 값에 따라 구분해야됨.
//		leftPosition 		= ((ITEM_WIDTH) - m_ItemTextureRegion[0].getWidth() + GAP_WIDTH) * THEME_ITEM_SCALE;
//		leftPosition 		= THEME_GAP_WIDTH * THEME_ITEM_SCALE - THEME_GAP_WIDTH;
		leftPosition 		= THEME_GAP_WIDTH + m_ItemTextureRegion[0].getHeight() * (THEME_ITEM_SCALE - 1)/2;
//		rightItemPosition 	= CAMERA_WIDTH - m_WordItemTextureRegion[0].getWidth()
//								- ((WORD_ITEM_WIDTH) - m_WordItemTextureRegion[0].getWidth() + WORD_GAP_WIDTH) * WORD_ITEM_SCALE;
		themePosition 		= ((THEME_ITEM_HEIGHT) - m_ItemTextureRegion[0].getHeight() + THEME_GAP_HEIGHT) * THEME_ITEM_SCALE;
//		wordPosition 		= ((WORD_ITEM_HEIGHT) - m_WordItemTextureRegion[0].getHeight() + WORD_GAP_HEIGHT) * WORD_ITEM_SCALE;
		
//		rightItemPosition	= CAMERA_WIDTH - m_WordItemTextureRegion[0].getWidth();
		leftPosition 		= -100;
//		themePosition = 0;
		wordPosition = 0;
		
//		Log.d(TAG, "m_WordItem.SCALE :" + WORD_ITEM_SCALE);
		
//		Log.d(TAG, "leftPosition : " + leftPosition);
//		Log.d(TAG, "position: " + position);
//		Log.d(TAG, "rightPosition: " + rightItemPosition);
//		Log.d(TAG, "themePosition: "+ themePosition);
//		Log.d(TAG, "wordPosition: "+ wordPosition);
		
		/**
		for(int i = 0; i < THEME_ITEM_COUNT; i++) {
			m_QuizLine[i] = new QuizLine(0,0,0,0,5f);
			m_QuizLine[i].setColor(0.0f,0.0f,0.0f);
		}
		**/
		
		Iterator<Integer> ii = m_RandomHashMap.keySet().iterator();
		while(ii.hasNext()) {
			Integer key = ii.next();
			int value = m_RandomHashMap.get(key);
			m_Items[count] = new ThemeSprite(leftPosition, themePosition, m_ItemTextureRegion[count], "", getApplicationContext()) {
				
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					boolean flag = false;
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						Log.d(TAG, "ACTION_UP");
						Log.d(TAG, "getString: " + this.get_Id());
						for(int i = 0; i < WORD_ITEM_COUNT; i++) {
							// collide with Text
							/** temp
							if(getLine().collidesWithAnObject(m_WordText[i])) {
								getLine().drawLine(getX() + getWidth()/2, getY() + getHeight()/2, m_WordText[i].getX(), m_WordText[i].getY() + m_WordText[i].getHeight()/2);
								m_WordText[i].getScaleY();
								getLine().setCollide(true);
								flag = true;
								// Hide text and Show TickerText
								if(get_Id().equals(m_WordText[i].getText())) {
									Toast.makeText(SummaryQuiz.this, "Right", Toast.LENGTH_SHORT).show();
									getLine().setCollideState(QuizLine.SUCCESS);
								}
								else {
									getLine().setCollideState(QuizLine.FAIL);
								}
								break;
							}
							**/
						}
						if(!flag) {
							getLine().setPosition(0, 0, 0, 0);
							getLine().setCollideState(QuizLine.FAIL);
							getLine().setCollide(false);
						}
						// TODO: 
						/** temp
						updateResultScreen();
						**/
						/*
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
							
					*/
					}
					else {	// Drawing Line
//						getLine().setPosition(getX() + getWidth()/2, getY() + getHeight()/2, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						getLine().setPosition(getWidthScaled(), getHeightScaled()/2, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						getLine().setCollideState(QuizLine.FAIL);
						getLine().setCollide(false);
						updateResultScreen();
					}
					return true;
				}
			};
			
//			setPointLayout(m_Items[count], count);
			
			/** temp
			m_WordItems[count] = new WordSprite(rightItemPosition, wordPosition, m_WordItemTextureRegion[count], m_tempString.get(count)) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					return true;
				}
			};
			**/
			
			count++;
			themePosition = themePosition + THEME_ITEM_HEIGHT + THEME_GAP_HEIGHT;
			wordPosition = wordPosition + WORD_ITEM_HEIGHT + WORD_GAP_HEIGHT;
		}
		
//		relocationWordText();
		
		m_PassSprite = new Sprite(CAMERA_WIDTH / 2- (m_PassTexture.getWidth() / 2),
				CAMERA_HEIGHT / 2 - (this.m_PassTexture.getHeight()/2), this.m_PassTextureRegion);		
		
		m_FailSprite = new Sprite(CAMERA_WIDTH / 2- (m_PassTexture.getWidth() / 2),
				CAMERA_HEIGHT / 2 - (this.m_FailTexture.getHeight()/2), this.m_FailTextureRegion);		
		
		m_BackgroundSprite = new Sprite(0, 0, m_BackgroundTextureRegion); 
		
		/** to be deleted
		for(int i = 0; i < WORD_ITEM_COUNT; i++) {
			
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
		**/
		
	}
	

	@Deprecated
	private void setPointLayout(ThemeSprite theme, int count) {
		/**
		Log.d(TAG, "setPointLayout()");
		
//			m_Items[count] = new ThemeSprite(leftPosition, themePosition, m_ItemTextureRegion[count], m_tempString.get(value), m_QuizLine[count]) {
		m_Point[count] = new Sprite(theme.getX() + theme.getWidthScaled() - 160, 
									theme.getY() + theme.getHeightScaled()/2, m_PointTextureRegion[count]);
		theme.setPoint(m_Point[count]);
		**/
	}

	private void updateResultScreen() {
		boolean isCollide = true;
		int collideState = 1;
		
		for(int i = 0; i < THEME_ITEM_COUNT; i  ++) {
			isCollide &= m_Items[i].getLine().isCollide();
			collideState = collideState * m_Items[i].getLine().getCollideState();
		}
		
		if(isCollide) {	// Collided with All Items
			if(collideState == QuizLine.SUCCESS) {
				drawResult(m_PassSprite);
			}
			else {
				drawResult(m_FailSprite);
			}
		}
		else {
			removeResult();
		}
	}

	@Deprecated
	/*
	private void relocationWordText() {
		Log.d(TAG, "relocationWordText()");
		
		Random rnd;
		int tempNum 		= 0;
		int count 			= 0;
		float maxWidth 		= 0f;
		float heightGap 	= 0f;
		float marginRight 	= 50f;
		float leftPosition 	= 0f;
		float prevHeight	= 0f;
		ArrayList<Integer> randomNum = new ArrayList<Integer>();
		
		// calculator maxWidth
		for(int i = 0; i < WORD_ITEM_COUNT; i++) {
			float width = m_WordText[i].getWidthScaled();
			if(maxWidth < width)
				maxWidth = width;
		}
		Log.d(TAG, "maxWidth: " + maxWidth);
		
		leftPosition = CAMERA_WIDTH - (maxWidth + marginRight);
		Log.d(TAG, "leftPosition: " + leftPosition);
		
		heightGap = (CAMERA_HEIGHT - m_WordText[0].getHeight() * WORD_ITEM_COUNT) / (WORD_ITEM_COUNT + 1);
		Log.d(TAG, "heightGap: " + heightGap);
		Log.d(TAG, "height: " + m_WordText[0].getHeight());
		
		// set Text Position
		rnd = new Random(System.currentTimeMillis());
		
		while(true) {
			tempNum = Math.abs(rnd.nextInt(WORD_ITEM_COUNT));
			if(!randomNum.contains(tempNum)) {
				randomNum.add(tempNum);
				count++;
			}
			if(randomNum.size() == WORD_ITEM_COUNT)
				break;
		}
		
		prevHeight = heightGap;
		for(int i = 0; i < randomNum.size(); i++) {
			Log.d(TAG, "ArrayList[" + "i" + "]: " + randomNum.get(i));
			Log.d(TAG, "prevHeight: " + prevHeight);
			m_WordText[randomNum.get(i)].setPosition(leftPosition, prevHeight);
			m_WordTickerText[randomNum.get(i)].setPosition(leftPosition, prevHeight);
			prevHeight = prevHeight + m_WordText[0].getHeight() + heightGap;
		}
	}
	*/
	
	private void drawResult(Sprite sprite) {
		Log.d(TAG, "drawResult()");
		final SequenceShapeModifier shapeModifier = new SequenceShapeModifier(new AlphaModifier(2, 0, 1));
		
		m_CurrentResult = sprite;
		sprite.addShapeModifier(shapeModifier);
		sprite.setScale(1.5f);
		m_Scene.getTopLayer().addEntity(sprite);
	}
	
	private void removeResult() {
		Log.d(TAG, "removeResult()");
		Sprite targetSprite;
		targetSprite = getCurrentResult();
		
		if(targetSprite != null) {
			Log.d(TAG, "real Remove");
			mEngine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					Sprite targetSprite = getCurrentResult();
					m_Scene.getTopLayer().removeEntity(targetSprite);
					m_CurrentResult = null;
				}
			});
		}
	}
	
	private Sprite getCurrentResult() {
		Log.d(TAG, "getCurrentResult()");
		return m_CurrentResult;
	}

	@Override
	public void onLoadComplete() {
		Log.d(TAG, "onLoadComplete()");
	}

	@Override
	protected void onPause() {
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
