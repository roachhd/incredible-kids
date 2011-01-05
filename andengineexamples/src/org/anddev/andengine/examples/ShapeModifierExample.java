package org.anddev.andengine.examples;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.DelayModifier;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationByModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier.IShapeModifierListener;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier.ILoopShapeModifierListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.LoopModifier;

import android.os.Handler;
import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ShapeModifierExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
	private TiledTextureRegion mFaceTextureRegion;
	private AnimatedSprite mFace;
	Handler handler1;
	
	private float mSpeed = 0.1f;

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
		handler1 = new Handler();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/face_box_tiled.png", 0, 0, 2, 1);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		mFace = new AnimatedSprite(centerX - 100, centerY, this.mFaceTextureRegion);
		mFace.animate(100);
		mFace.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		final LoopShapeModifier shapeModifier =
			new LoopShapeModifier(
					new IShapeModifierListener() {
						@Override
						public void onModifierFinished(final IModifier<IShape> pShapeModifier, final IShape pShape) {
							ShapeModifierExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ShapeModifierExample.this, "Sequence ended.", Toast.LENGTH_LONG).show();
								}
							});
						}
					},
					20,
					new ILoopShapeModifierListener() {
						@Override
						public void onLoopFinished(final LoopModifier<IShape> pLoopShapeModifier, final int pLoopsRemaining) {
							ShapeModifierExample.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(ShapeModifierExample.this, "Loops remaining: " + pLoopsRemaining, Toast.LENGTH_SHORT).show();
								}
							});
						}
					},
//					new SequenceShapeModifier(
							new RotationModifier(0.3f, 0, 90)//,
/*							new AlphaModifier(2, 1, 0),
							new AlphaModifier(1, 0, 1),
							new ScaleModifier(2, 1, 0.5f),
							new DelayModifier(0.5f),
							new ParallelShapeModifier(
									new ScaleModifier(3, 0.5f, 5),
									new RotationByModifier(3, 90)
							),
							new ParallelShapeModifier(
									new ScaleModifier(3, 5, 1),
									new RotationModifier(3, 180, 0)
							)*/
//					)
			);
/*		for(float i=0.1f; i < 10f;){
			RotationModifier rotModifier = new RotationModifier(i, 0, 360);
			mFace.addShapeModifier(rotModifier);//shapeModifier);
			i+= 0.1f;
		}*/
		scene.getTopLayer().addEntity(mFace);
		setTimer();
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	private void setTimer(){
		handler1.post(new Runnable() {

			public void run() {
				if(true) {
					// 15�ʸ��� �˷��ش�.


					RotationModifier rotModifier = new RotationModifier(mSpeed, 0, 360);
					mFace.addShapeModifier(rotModifier);//shapeModifier);
					mSpeed += 0.1f;

					handler1.postDelayed(this, 1000);
				} 
			}	
		});
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
