package com.incrediblekids.activities;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.incrediblekids.util.Const;

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
		public int iItemWordId;
		public int iCardImgId;
		public int iCardWordId;
		public String strWordCharId;
		
		public Item(String _name, int _imgId, int _wordId, int _cardImgId, int _cardWordId){
			strWordCharId = _name;
			iItemImgId = _imgId;
			iItemWordId = _wordId;
			iCardImgId = _cardImgId;
			iCardWordId = _cardWordId;			
		}
	}
	
	public void setTheme(String _theme) {
		Log.d(TAG, "theme = " + _theme);
		vItems.clear();
		if (_theme.equals(Const.THEME_ANIMAL)) {
			vItems.add(new Item("ant", R.drawable.img_ant, R.drawable.word_ant, R.drawable.card_img_ant, R.drawable.card_word_ant));
			vItems.add(new Item("turtle", R.drawable.img_turtle, R.drawable.word_turtle, R.drawable.card_img_turtle, R.drawable.card_word_turtle));
			vItems.add(new Item("whale", R.drawable.img_whale, R.drawable.word_whale, R.drawable.card_img_whale, R.drawable.card_word_whale));
			vItems.add(new Item("horse", R.drawable.img_horse, R.drawable.word_horse, R.drawable.card_img_horse, R.drawable.card_word_horse));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.word_lion, R.drawable.card_img_lion, R.drawable.card_word_lion));
			vItems.add(new Item("shark", R.drawable.img_shark, R.drawable.word_shark, R.drawable.card_img_shark, R.drawable.card_word_shark));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.word_bird, R.drawable.card_img_bird, R.drawable.card_word_bird));
			vItems.add(new Item("fox", R.drawable.img_fox, R.drawable.word_fox, R.drawable.card_img_fox, R.drawable.card_word_fox));
			vItems.add(new Item("rabbit", R.drawable.img_rabbit, R.drawable.word_rabbit, R.drawable.card_img_rabbit, R.drawable.card_word_rabbit));
			vItems.add(new Item("tiger", R.drawable.img_tiger, R.drawable.word_tiger, R.drawable.card_img_tiger, R.drawable.card_word_tiger));
			
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.word_dog, R.drawable.card_img_dog, R.drawable.card_word_dog));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));			
			vItems.add(new Item("fish", R.drawable.img_fish, R.drawable.word_fish, R.drawable.card_img_fish, R.drawable.card_word_fish));
			vItems.add(new Item("snake", R.drawable.img_snake, R.drawable.word_snake, R.drawable.card_img_snake, R.drawable.card_word_snake));
			vItems.add(new Item("duck", R.drawable.img_duck, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.word_cat, R.drawable.card_img_cat, R.drawable.card_word_cat));			
			vItems.add(new Item("pig", R.drawable.img_pig, R.drawable.word_pig, R.drawable.card_img_pig, R.drawable.card_word_pig));
			vItems.add(new Item("frog", R.drawable.img_frog, R.drawable.word_frog, R.drawable.card_img_frog, R.drawable.card_word_frog));

		} else if (_theme.equals(Const.THEME_TOY)) {
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.word_dog, R.drawable.card_img_dog, R.drawable.card_word_dog));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));			
			vItems.add(new Item("fish", R.drawable.img_fish, R.drawable.word_fish, R.drawable.card_img_fish, R.drawable.card_word_fish));
			vItems.add(new Item("snake", R.drawable.img_snake, R.drawable.word_snake, R.drawable.card_img_snake, R.drawable.card_word_snake));
			vItems.add(new Item("duck", R.drawable.img_duck, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.word_cat, R.drawable.card_img_cat, R.drawable.card_word_cat));			
			vItems.add(new Item("pig", R.drawable.img_pig, R.drawable.word_pig, R.drawable.card_img_pig, R.drawable.card_word_pig));
			vItems.add(new Item("frog", R.drawable.img_frog, R.drawable.word_frog, R.drawable.card_img_frog, R.drawable.card_word_frog));
			
			vItems.add(new Item("ant", R.drawable.img_ant, R.drawable.word_ant, R.drawable.card_img_ant, R.drawable.card_word_ant));
			vItems.add(new Item("turtle", R.drawable.img_turtle, R.drawable.word_turtle, R.drawable.card_img_turtle, R.drawable.card_word_turtle));
			vItems.add(new Item("whale", R.drawable.img_whale, R.drawable.word_whale, R.drawable.card_img_whale, R.drawable.card_word_whale));
			vItems.add(new Item("horse", R.drawable.img_horse, R.drawable.word_horse, R.drawable.card_img_horse, R.drawable.card_word_horse));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.word_lion, R.drawable.card_img_lion, R.drawable.card_word_lion));
			vItems.add(new Item("shark", R.drawable.img_shark, R.drawable.word_shark, R.drawable.card_img_shark, R.drawable.card_word_shark));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.word_bird, R.drawable.card_img_bird, R.drawable.card_word_bird));
			vItems.add(new Item("fox", R.drawable.img_fox, R.drawable.word_fox, R.drawable.card_img_fox, R.drawable.card_word_fox));
			vItems.add(new Item("rabbit", R.drawable.img_rabbit, R.drawable.word_rabbit, R.drawable.card_img_rabbit, R.drawable.card_word_rabbit));
			vItems.add(new Item("tiger", R.drawable.img_tiger, R.drawable.word_tiger, R.drawable.card_img_tiger, R.drawable.card_word_tiger));
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
