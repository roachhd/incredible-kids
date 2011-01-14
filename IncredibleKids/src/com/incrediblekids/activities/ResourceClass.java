package com.incrediblekids.activities;

import java.util.Vector;

import android.util.Log;

public class ResourceClass {
	private final String TAG="PreviewWords";
	public static String m_sTheme;
	public Vector<Item> vItems = new Vector<Item>();
	
	public ResourceClass(String _theme) {
		Log.d(TAG, "theme = " + _theme);
		if (_theme.equals("Animal")) {
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.word_cat));
			vItems.add(new Item("cat", R.drawable.img_dog, R.drawable.word_dog));
			vItems.add(new Item("cat", R.drawable.img_bear, R.drawable.word_bear));
			vItems.add(new Item("cat", R.drawable.img_bird, R.drawable.word_bird));
			vItems.add(new Item("cat", R.drawable.img_lion, R.drawable.word_lion));
			vItems.add(new Item("cat", R.drawable.img_mouse, R.drawable.word_mouse));
			vItems.add(new Item("cat", R.drawable.img_monkey, R.drawable.word_monkey));			
		} else if (_theme.equals("ToyBox")) {
			vItems.add(new Item("cat", R.drawable.img_monkey, R.drawable.word_monkey));	
			vItems.add(new Item("cat", R.drawable.img_mouse, R.drawable.word_mouse));
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.word_cat));
			vItems.add(new Item("cat", R.drawable.img_dog, R.drawable.word_dog));
			vItems.add(new Item("cat", R.drawable.img_bear, R.drawable.word_bear));
			vItems.add(new Item("cat", R.drawable.img_bird, R.drawable.word_bird));
			vItems.add(new Item("cat", R.drawable.img_lion, R.drawable.word_lion));
		}
	}
	
	public class Item {
		public int iItemImgId;
		public int iWordImgId;
		public String iWordCharId;
		
		public Item(String _name, int _imgId, int _wordId){
			iWordCharId = _name;
			iItemImgId = _imgId;
			iWordImgId = _wordId;
		}
	}
	
	public static String getTheme() {
		return m_sTheme; 
	}
}
