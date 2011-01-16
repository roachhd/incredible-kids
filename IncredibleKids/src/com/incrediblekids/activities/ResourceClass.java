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
		public String strWordCharId;
		
		public Item(String _name, int _imgId, int _wordId){
			strWordCharId = _name;
			iItemImgId = _imgId;
			iWordImgId = _wordId;
		}
	}
	
	public void setTheme(String _theme) {
		Log.d(TAG, "theme = " + _theme);
		vItems.clear();
		if (_theme.equals(Const.THEME_ANIMAL)) {
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.word_cat));
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.word_dog));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.word_bear));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.word_bird));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.word_lion));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.word_mouse));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.word_monkey));			
		} else if (_theme.equals("ToyBox")) {
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.word_monkey));	
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.word_mouse));
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.word_cat));
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.word_dog));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.word_bear));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.word_bird));
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.word_lion));
		}
	}
}
