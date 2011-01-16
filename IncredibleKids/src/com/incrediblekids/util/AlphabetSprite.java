package com.incrediblekids.util;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class AlphabetSprite extends AnimatedSprite {
	
	public int sequence;
	public boolean bFilled;
	public boolean bCorrect;
	public int filledAlphabetIndex;
	public boolean bCollied;
	public char alphabet;
	public char alphabetContainer;
	
	public AlphabetSprite(float pX, float pY, TiledTextureRegion pTextureRegion, int seq, char alphabet) {
		super(pX, pY, pTextureRegion);
		this.sequence = seq;
		this.bCorrect = false;
		this.bFilled = false;
		this.bCollied = false;
		this.filledAlphabetIndex = -1;
		this.alphabet = alphabet;
	}
}
