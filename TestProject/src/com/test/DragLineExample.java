package com.test;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
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

public class DragLineExample extends BaseGameActivity {
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private Camera mCamera;
	private Texture mBoxTexture;
	private TextureRegion mBoxTextureRegion;
	private Sprite mbox;
	private Scene mScene = null;
	private Line line;
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mBoxTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBoxTextureRegion = TextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box1.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBoxTexture);
		
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		mScene = new Scene(1);
		mScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		line = new Line(0,0,0,0,5f);
		line.setColor(0.0f,0.0f,0.0f);

		final int centerX = (CAMERA_WIDTH - this.mBoxTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mBoxTextureRegion.getHeight()) / 2;
		
		mbox = new Sprite(centerX, centerY, this.mBoxTextureRegion) {
			
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				line.setPosition(mbox.getX(), mbox.getY(), pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
				return true;
			}
		};	
		mbox.setScale(4);
		mScene.getTopLayer().addEntity(mbox);
		mScene.getTopLayer().addEntity(line);
		mScene.registerTouchArea(mbox);
		mScene.setTouchAreaBindingEnabled(true);
		
		return mScene;
	}
	
}
