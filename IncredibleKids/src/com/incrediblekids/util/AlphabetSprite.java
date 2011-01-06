package com.incrediblekids.util;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class AlphabetSprite extends Sprite {
	
	private int sequence;
	private boolean bFilled;
	private boolean bCorrect;
	private int filledAlphabetIndex;
	private boolean bCollied;

	public boolean isbCollied() {
		return bCollied;
	}
	public void setbCollied(boolean bCollied) {
		this.bCollied = bCollied;
	}
	public boolean isCorrect() {
		return bCorrect;
	}
	public void setbCorrect(boolean bCorrect) {
		this.bCorrect = bCorrect;
	}
	public AlphabetSprite(float pX, float pY, TextureRegion pTextureRegion, int seq) {
		super(pX, pY, pTextureRegion);
		this.sequence = seq;
		this.bCorrect = false;
		this.bFilled = false;
		this.bCollied = false;
		this.filledAlphabetIndex = -1;
	}
	public int getFilledAlphabetIndex() {
		return filledAlphabetIndex;
	}
	public void setFilledAlphabetIndex(int filledAlphabetIndex) {
		this.filledAlphabetIndex = filledAlphabetIndex;
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
