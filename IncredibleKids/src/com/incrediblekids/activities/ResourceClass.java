package com.incrediblekids.activities;

import java.util.Vector;

import com.incrediblekids.util.Const;

import android.util.Log;

public class ResourceClass {
	private final String TAG="PreviewWords";
	private boolean bSound = true;
	public static ResourceClass m_ResourceClass;
	
	public Vector<Item> vItems = new Vector<Item>();
	
	public Vector<Item> getvItems() {
		return vItems;
	}

	public static ResourceClass getInstance() {
		if (m_ResourceClass == null)
			m_ResourceClass = new ResourceClass();
		return m_ResourceClass;
	}
	
	private ResourceClass() {}
	
	public class Item {
		public int iItemImgId;
		public int iWordImgId;
		public int iSmallItemImgId;
		public int iSmallWordImgId;
		public String strWordCharId;
		
		public Item(String _name, int _imgId, int _smallImgId, int _wordId, int _smallWordId){
			strWordCharId = _name;
			iItemImgId = _imgId;
			iSmallItemImgId = _smallImgId;
			iWordImgId = _wordId;
			iSmallWordImgId = _smallWordId;
		}
	}
	
	public void setTheme(String _theme) {
		Log.d(TAG, "theme = " + _theme);
		vItems.clear();
		if (_theme.equals(Const.THEME_ANIMAL)) {
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.img_cat_s, R.drawable.word_cat, R.drawable.word_cat_s));			
			vItems.add(new Item("pig", R.drawable.img_pig, R.drawable.img_pig_s, R.drawable.word_monkey, R.drawable.word_monkey_s));
			vItems.add(new Item("frog", R.drawable.img_frog, R.drawable.img_frog_s, R.drawable.word_frog, R.drawable.word_cat_s));
		} else if (_theme.equals(Const.THEME_TOY)) {
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.img_bird_s, R.drawable.word_bird, R.drawable.word_bird_s));
		}
	}
	
	public int getAlphabetResourceId(char alphabet){
		switch(alphabet){
		case 'a':
			return R.drawable.alphabet_a;
		case 'b':
			return R.drawable.alphabet_b;
		case 'c':
			return R.drawable.alphabet_c;
		case 'd':
			return R.drawable.alphabet_d;
		case 'e':
			return R.drawable.alphabet_e;
		case 'f':
			return R.drawable.alphabet_f;
		case 'g':
			return R.drawable.alphabet_g;
		case 'h':
			return R.drawable.alphabet_h;
		case 'i':
			return R.drawable.alphabet_i;
		case 'j':
			return R.drawable.alphabet_j;
		case 'k':
			return R.drawable.alphabet_k;
		case 'l':
			return R.drawable.alphabet_l;
		case 'm':
			return R.drawable.alphabet_m;
		case 'n':
			return R.drawable.alphabet_n;
		case 'o':
			return R.drawable.alphabet_o;
		case 'p':
			return R.drawable.alphabet_p;
		case 'q':
			return R.drawable.alphabet_q;
		case 'r':
			return R.drawable.alphabet_r;
		case 's':
			return R.drawable.alphabet_s;
		case 't':
			return R.drawable.alphabet_t;
		case 'u':
			return R.drawable.alphabet_u;
		case 'v':
			return R.drawable.alphabet_v;
		case 'w':
			return R.drawable.alphabet_w;
		case 'x':
			return R.drawable.alphabet_x;
		case 'y':
			return R.drawable.alphabet_y;
		case 'z':
			return R.drawable.alphabet_z;
		}
		return 0;
	}
	
	public void setSound(boolean _status) {
		bSound = _status;
	}
	public boolean getSound() {
		return bSound;
	}
}
