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
			vItems.add(new Item("ant", R.drawable.img_ant, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("turtle", R.drawable.img_turtle, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("whale", R.drawable.img_whale, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("horse", R.drawable.img_horse, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("shark", R.drawable.img_shark, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("fox", R.drawable.img_fox, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("rabbit", R.drawable.img_rabbit, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("tiger", R.drawable.img_tiger, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.img_cat_s, R.drawable.word_cat, R.drawable.word_cat_s));			
			vItems.add(new Item("fish", R.drawable.img_fish, R.drawable.img_pig_s, R.drawable.word_monkey, R.drawable.word_monkey_s));
			vItems.add(new Item("snake", R.drawable.img_snake, R.drawable.img_frog_s, R.drawable.word_frog, R.drawable.word_cat_s));
			vItems.add(new Item("duck", R.drawable.img_duck, R.drawable.img_frog_s, R.drawable.word_frog, R.drawable.word_cat_s));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.img_frog_s, R.drawable.word_frog, R.drawable.word_cat_s));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.img_cat_s, R.drawable.word_cat, R.drawable.word_cat_s));			
			vItems.add(new Item("pig", R.drawable.img_pig, R.drawable.img_pig_s, R.drawable.word_monkey, R.drawable.word_monkey_s));
			vItems.add(new Item("frog", R.drawable.img_frog, R.drawable.img_frog_s, R.drawable.word_frog, R.drawable.word_cat_s));

		} else if (_theme.equals(Const.THEME_TOY)) {
			vItems.add(new Item("car", R.drawable.img_car, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("ball", R.drawable.img_ball, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("robot", R.drawable.img_robot, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("ship", R.drawable.img_ship, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("bus", R.drawable.img_bus, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("plane", R.drawable.img_plane, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("clock", R.drawable.img_clock, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("train", R.drawable.img_train, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("moon", R.drawable.img_moon, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("ring", R.drawable.img_ring, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("spoon", R.drawable.img_spoon, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("pencil", R.drawable.img_pencil, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("chair", R.drawable.img_chair, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("eraser", R.drawable.img_eraser, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("house", R.drawable.img_house, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("book", R.drawable.img_book, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("table", R.drawable.img_table, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("bed", R.drawable.img_bed, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("fork", R.drawable.img_fork, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
			vItems.add(new Item("sun", R.drawable.img_sun, R.drawable.img_monkey_s, R.drawable.word_mouse, R.drawable.word_monkey_s));
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
