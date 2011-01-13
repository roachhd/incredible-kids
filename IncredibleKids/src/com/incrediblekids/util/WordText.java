package com.incrediblekids.util;

import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.HorizontalAlign;

public class WordText extends Text {
	
	private String m_Text;

	public WordText(float pX, float pY, Font pFont, String pText, HorizontalAlign pHorizontalAlign) {
		super(pX, pY, pFont, pText, pHorizontalAlign);
		m_Text = pText;
	}
	
	public String getText() {
		return m_Text;
	}
}
