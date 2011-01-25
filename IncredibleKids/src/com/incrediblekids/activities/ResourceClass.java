package com.incrediblekids.activities;

import java.util.Vector;

import com.incrediblekids.util.Const;

import android.util.Log;

public class ResourceClass {
	private final String TAG="PreviewWords";
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
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.img_cat_s, R.drawable.word_cat, R.drawable.word_cat_s));
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.img_dog_s, R.drawable.word_dog, R.drawable.word_dog_s));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.img_bear_s, R.drawable.word_bear, R.drawable.word_bear_s));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.img_bird_s, R.drawable.word_bird, R.drawable.word_bird_s));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.img_lion_s, R.drawable.word_lion, R.drawable.word_lion_s));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.img_mouse_s, R.drawable.word_mouse, R.drawable.word_mouse_s));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.img_monkey_s, R.drawable.word_monkey, R.drawable.word_monkey_s));			
		} else if (_theme.equals(Const.THEME_TOY)) {
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.img_bird_s, R.drawable.word_bird, R.drawable.word_bird_s));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.img_lion_s, R.drawable.word_lion, R.drawable.word_lion_s));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.img_mouse_s, R.drawable.word_mouse, R.drawable.word_mouse_s));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.img_monkey_s, R.drawable.word_monkey, R.drawable.word_monkey_s));			
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.img_cat_s, R.drawable.word_cat, R.drawable.word_cat_s));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.img_bird_s, R.drawable.word_bird, R.drawable.word_bird_s));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.img_lion_s, R.drawable.word_lion, R.drawable.word_lion_s));
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.img_dog_s, R.drawable.word_dog, R.drawable.word_dog_s));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.img_bear_s, R.drawable.word_bear, R.drawable.word_bear_s));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.img_mouse_s, R.drawable.word_mouse, R.drawable.word_mouse_s));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.img_monkey_s, R.drawable.word_monkey, R.drawable.word_monkey_s));			
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.img_cat_s, R.drawable.word_cat, R.drawable.word_cat_s));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.img_bird_s, R.drawable.word_bird, R.drawable.word_bird_s));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.img_lion_s, R.drawable.word_lion, R.drawable.word_lion_s));
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.img_dog_s, R.drawable.word_dog, R.drawable.word_dog_s));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.img_bear_s, R.drawable.word_bear, R.drawable.word_bear_s));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.img_mouse_s, R.drawable.word_mouse, R.drawable.word_mouse_s));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.img_monkey_s, R.drawable.word_monkey, R.drawable.word_monkey_s));			
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.img_cat_s, R.drawable.word_cat, R.drawable.word_cat_s));
		}
	}
	
	public int getAlphabetResourceId(char alphabet){
		switch(alphabet){
		case 'a':
			return R.drawable.a1;
		case 'b':
			return R.drawable.b1;
		case 'c':
			return R.drawable.c1;
		case 'd':
			return R.drawable.d1;
		case 'e':
			return R.drawable.e1;
		case 'f':
			return R.drawable.f1;
		case 'g':
			return R.drawable.g1;
		case 'h':
			return R.drawable.h1;
		case 'i':
			return R.drawable.i1;
		case 'j':
			return R.drawable.j1;
		case 'k':
			return R.drawable.k1;
		case 'l':
			return R.drawable.l1;
		case 'm':
			return R.drawable.m1;
		case 'n':
			return R.drawable.n1;
		case 'o':
			return R.drawable.o1;
		case 'p':
			return R.drawable.p1;
		case 'q':
			return R.drawable.q1;
		case 'r':
			return R.drawable.r1;
		case 's':
			return R.drawable.s1;
		case 't':
			return R.drawable.t1;
		case 'u':
			return R.drawable.u1;
		case 'v':
			return R.drawable.v1;
		case 'w':
			return R.drawable.w1;
		case 'x':
			return R.drawable.x1;
		case 'y':
			return R.drawable.y1;
		case 'z':
			return R.drawable.z1;
		}
		return 0;
	}
}
