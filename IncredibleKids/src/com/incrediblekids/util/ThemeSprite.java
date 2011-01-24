package com.incrediblekids.util;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;

public class ThemeSprite extends WordSprite {
	
	private QuizLine m_Line;
	
	public ThemeSprite(float pX, float pY, TextureRegion pTextureRegion, String mId, Context mContext) {
		super(pX, pY, pTextureRegion, mId, mContext);
		init();
	}
	
	public QuizLine getLine() {
		return m_Line;
	}

	private void init() {
		m_Line = new QuizLine(0,0,0,0,5f);
		m_Line.setColor(0.0f,0.0f,0.0f);
	}
}
