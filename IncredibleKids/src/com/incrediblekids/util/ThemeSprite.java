package com.incrediblekids.util;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;

public class ThemeSprite extends WordSprite {
	
	private QuizLine m_Line;
	
	public ThemeSprite(float pX, float pY, TextureRegion pTextureRegion, String mId, Context mContext) {
		super(pX, pY, pTextureRegion, mId, mContext);
	}
	
	public QuizLine getLine() {
		return m_Line;
	}

	public void setLine(QuizLine pLine) {
		m_Line = pLine;
	}
}
