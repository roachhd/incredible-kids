package com.incrediblekids.util;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class AlphabetSprite extends Sprite {
	
	public int sequence;
	public boolean bFilled;
	public boolean bCorrect;
	public int filledAlphabetIndex;
	public boolean bCollied;
	public char alphabet;
	public char alphabetContainer;
	
	public AlphabetSprite(float pX, float pY, TextureRegion pTextureRegion, int seq, char alphabet) {
		super(pX, pY, pTextureRegion);
		this.sequence = seq;
		this.bCorrect = false;
		this.bFilled = false;
		this.bCollied = false;
		this.filledAlphabetIndex = -1;
		this.alphabet = alphabet;
	}
}
