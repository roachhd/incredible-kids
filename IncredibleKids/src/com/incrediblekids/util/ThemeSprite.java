package com.incrediblekids.util;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class ThemeSprite extends WordSprite {
	
	private QuizLine m_Line;

	
	public ThemeSprite(float pX, float pY, TextureRegion pTextureRegion, String mId, QuizLine mLine) {
		super(pX, pY, pTextureRegion, mId);
		m_Line = mLine;
	}

	public QuizLine getLine() {
		return m_Line;
	}

	public void setLine(QuizLine pLine) {
		m_Line = pLine;
	}
}
