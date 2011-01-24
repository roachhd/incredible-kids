package com.incrediblekids.util;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.Context;
import android.util.Log;

import com.incrediblekids.activities.R;

public class WordSprite extends Sprite {
	
	private final String TAG = "WordSprite";
	private String m_Id;
	private Context m_Context;
	
	private PointSprite m_Point;
	private Texture m_PointTexture;
	private TextureRegion m_PointTextureRegion;

	public WordSprite(float pX, float pY, TextureRegion pTextureRegion, String mId, Context mContext) {
		super(pX, pY, pTextureRegion);
		m_Context = mContext;
		m_Id = mId;
		init();
	}

	private void init() {
		Log.d(TAG, "init()");
		m_PointTexture = new Texture(16, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		m_PointTextureRegion = TextureRegionFactory.createFromResource(m_PointTexture, m_Context, R.drawable.dot_16, 0, 0);
		m_Point = new PointSprite(0, 0, m_PointTextureRegion);
	}

	public String get_Id() {
		return m_Id;
	}

	public void set_Id(String mId) {
		m_Id = mId;
	}
	
	public PointSprite getPoint() {
		return m_Point;
	}

	public void setPoint(PointSprite mPoint) {
		m_Point = mPoint;
	}
	
	public TextureRegion getPointTR() {
		return m_PointTextureRegion;
	}
	
	public Texture getPointTexture() {
		return m_PointTexture;
	}
	
	public WordSprite getParentSprite() {
		return this;
	}
}
