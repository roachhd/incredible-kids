package com.test;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class AlphabetSprite extends Sprite {
	
	private int sequence;
	private boolean bFilled;
	private boolean bCorrect;

	public boolean isCorrect() {
		return bCorrect;
	}
	public void setbCorrect(boolean bCorrect) {
		this.bCorrect = bCorrect;
	}
	public AlphabetSprite(float pX, float pY, TextureRegion pTextureRegion, int seq) {
		super(pX, pY, pTextureRegion);
		this.sequence = seq;
	}
	public int getSequence(){
		return sequence;
	}
	public boolean isFilled() {
		return bFilled;
	}
	public void setbFilled(boolean bFilled) {
		this.bFilled = bFilled;
	}
	
}
