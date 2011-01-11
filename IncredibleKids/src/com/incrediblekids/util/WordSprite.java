package com.incrediblekids.util;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;


public class WordSprite extends Sprite {
	
	private String m_Id;

	public WordSprite(float pX, float pY, TextureRegion pTextureRegion, String mId) {
		super(pX, pY, pTextureRegion);
		m_Id = mId;
	}

	public String get_Id() {
		return m_Id;
	}

	public void set_Id(String mId) {
		m_Id = mId;
	}
}
