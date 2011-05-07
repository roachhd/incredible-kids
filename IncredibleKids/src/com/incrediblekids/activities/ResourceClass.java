package com.incrediblekids.activities;

import java.util.Vector;

import android.util.Log;

import com.incrediblekids.util.Const;
import com.incrediblekids.util.Item;

public class ResourceClass {
	private final String TAG="PreviewWords";
	private boolean bSound = true;
	private String m_sCurrentTheme = null;
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
	
	public String getCurrentTheme(){
		if (m_sCurrentTheme == null)
			Log.d(TAG, "m_sCurrentTheme");
		return m_sCurrentTheme;
	}
	public void setTheme(String _theme) {
		Log.d(TAG, "theme = " + _theme);
		m_sCurrentTheme = _theme;
		vItems.clear();
		if (_theme.equals(Const.THEME_ANIMAL)) {
			vItems.add(new Item("dog", R.drawable.img_dog, R.drawable.word_dog, R.drawable.card_img_dog, R.drawable.card_word_dog));
			vItems.add(new Item("cat", R.drawable.img_cat, R.drawable.word_cat, R.drawable.card_img_cat, R.drawable.card_word_cat));
			vItems.add(new Item("pig", R.drawable.img_pig, R.drawable.word_pig, R.drawable.card_img_pig, R.drawable.card_word_pig));
			vItems.add(new Item("ant", R.drawable.img_ant, R.drawable.word_ant, R.drawable.card_img_ant, R.drawable.card_word_ant));
			vItems.add(new Item("fox", R.drawable.img_fox, R.drawable.word_fox, R.drawable.card_img_fox, R.drawable.card_word_fox));
			
			vItems.add(new Item("lion", R.drawable.img_lion, R.drawable.word_lion, R.drawable.card_img_lion, R.drawable.card_word_lion));
			vItems.add(new Item("bear", R.drawable.img_bear, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));
			vItems.add(new Item("fish", R.drawable.img_fish, R.drawable.word_fish, R.drawable.card_img_fish, R.drawable.card_word_fish));
			vItems.add(new Item("frog", R.drawable.img_frog, R.drawable.word_frog, R.drawable.card_img_frog, R.drawable.card_word_frog));
			vItems.add(new Item("bird", R.drawable.img_bird, R.drawable.word_bird, R.drawable.card_img_bird, R.drawable.card_word_bird));
			
			vItems.add(new Item("duck", R.drawable.img_duck, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			vItems.add(new Item("tiger", R.drawable.img_tiger, R.drawable.word_tiger, R.drawable.card_img_tiger, R.drawable.card_word_tiger));
			vItems.add(new Item("mouse", R.drawable.img_mouse, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));
			vItems.add(new Item("horse", R.drawable.img_horse, R.drawable.word_horse, R.drawable.card_img_horse, R.drawable.card_word_horse));
			vItems.add(new Item("snake", R.drawable.img_snake, R.drawable.word_snake, R.drawable.card_img_snake, R.drawable.card_word_snake));
			
			vItems.add(new Item("whale", R.drawable.img_whale, R.drawable.word_whale, R.drawable.card_img_whale, R.drawable.card_word_whale));
			vItems.add(new Item("shark", R.drawable.img_shark, R.drawable.word_shark, R.drawable.card_img_shark, R.drawable.card_word_shark));
			vItems.add(new Item("monkey", R.drawable.img_monkey, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
			vItems.add(new Item("turtle", R.drawable.img_turtle, R.drawable.word_turtle, R.drawable.card_img_turtle, R.drawable.card_word_turtle));
			vItems.add(new Item("rabbit", R.drawable.img_rabbit, R.drawable.word_rabbit, R.drawable.card_img_rabbit, R.drawable.card_word_rabbit));
		} else if (_theme.equals(Const.THEME_TOY)) {
			vItems.add(new Item("car", R.drawable.img_car, R.drawable.word_dog, R.drawable.card_img_dog, R.drawable.card_word_dog));
			vItems.add(new Item("bus", R.drawable.img_bus, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			vItems.add(new Item("bed", R.drawable.img_bed, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));			
			vItems.add(new Item("cup", R.drawable.img_cup, R.drawable.word_fish, R.drawable.card_img_fish, R.drawable.card_word_fish));
			vItems.add(new Item("sun", R.drawable.img_sun, R.drawable.word_snake, R.drawable.card_img_snake, R.drawable.card_word_snake));
			
			vItems.add(new Item("moon", R.drawable.img_moon, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
			vItems.add(new Item("ball", R.drawable.img_ball, R.drawable.word_cat, R.drawable.card_img_cat, R.drawable.card_word_cat));
			vItems.add(new Item("book", R.drawable.img_book, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));			
			vItems.add(new Item("ship", R.drawable.img_ship, R.drawable.word_pig, R.drawable.card_img_pig, R.drawable.card_word_pig));
			vItems.add(new Item("ring", R.drawable.img_ring, R.drawable.word_frog, R.drawable.card_img_frog, R.drawable.card_word_frog));
			
			vItems.add(new Item("desk", R.drawable.img_desk, R.drawable.word_turtle, R.drawable.card_img_turtle, R.drawable.card_word_turtle));
			vItems.add(new Item("fork", R.drawable.img_fork, R.drawable.word_ant, R.drawable.card_img_ant, R.drawable.card_word_ant));
			vItems.add(new Item("spoon", R.drawable.img_spoon, R.drawable.word_whale, R.drawable.card_img_whale, R.drawable.card_word_whale));
			vItems.add(new Item("train", R.drawable.img_train, R.drawable.word_horse, R.drawable.card_img_horse, R.drawable.card_word_horse));
			vItems.add(new Item("clock", R.drawable.img_clock, R.drawable.word_lion, R.drawable.card_img_lion, R.drawable.card_word_lion));
			
			vItems.add(new Item("plane", R.drawable.img_plane, R.drawable.word_shark, R.drawable.card_img_shark, R.drawable.card_word_shark));
			vItems.add(new Item("chair", R.drawable.img_chair, R.drawable.word_bird, R.drawable.card_img_bird, R.drawable.card_word_bird));
			vItems.add(new Item("robot", R.drawable.img_robot, R.drawable.word_fox, R.drawable.card_img_fox, R.drawable.card_word_fox));
			vItems.add(new Item("pencil", R.drawable.img_pencil, R.drawable.word_rabbit, R.drawable.card_img_rabbit, R.drawable.card_word_rabbit));
			vItems.add(new Item("eraser", R.drawable.img_eraser, R.drawable.word_tiger, R.drawable.card_img_tiger, R.drawable.card_word_tiger));
		} else if (_theme.equals(Const.THEME_FOOD)) {
			vItems.add(new Item("egg", R.drawable.img_egg, R.drawable.word_dog, R.drawable.card_img_dog, R.drawable.card_word_dog));
			vItems.add(new Item("rice", R.drawable.img_rice, R.drawable.word_fish, R.drawable.card_img_fish, R.drawable.card_word_fish));
			vItems.add(new Item("milk", R.drawable.img_milk, R.drawable.word_snake, R.drawable.card_img_snake, R.drawable.card_word_snake));
			vItems.add(new Item("pear", R.drawable.img_peer, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));
			vItems.add(new Item("apple", R.drawable.img_apple, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
			
			vItems.add(new Item("peach", R.drawable.img_peach, R.drawable.word_cat, R.drawable.card_img_cat, R.drawable.card_word_cat));			
			vItems.add(new Item("candy", R.drawable.img_candy, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			vItems.add(new Item("bread", R.drawable.img_bread, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));
			vItems.add(new Item("banana", R.drawable.img_banana, R.drawable.word_pig, R.drawable.card_img_pig, R.drawable.card_word_pig));
			vItems.add(new Item("cookie", R.drawable.img_cookie, R.drawable.word_frog, R.drawable.card_img_frog, R.drawable.card_word_frog));
		}else if (_theme.equals(Const.THEME_NUMBER)) {
			vItems.add(new Item("one", R.drawable.img_1, R.drawable.word_dog, R.drawable.card_img_dog, R.drawable.card_word_dog));
			vItems.add(new Item("two", R.drawable.img_2, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));			
			vItems.add(new Item("three", R.drawable.img_3, R.drawable.word_fish, R.drawable.card_img_fish, R.drawable.card_word_fish));
			vItems.add(new Item("four", R.drawable.img_4, R.drawable.word_snake, R.drawable.card_img_snake, R.drawable.card_word_snake));
			vItems.add(new Item("five", R.drawable.img_5, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			
			vItems.add(new Item("six", R.drawable.img_6, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));
			vItems.add(new Item("seven", R.drawable.img_7, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
			vItems.add(new Item("eight", R.drawable.img_8, R.drawable.word_cat, R.drawable.card_img_cat, R.drawable.card_word_cat));			
			vItems.add(new Item("nine", R.drawable.img_9, R.drawable.word_pig, R.drawable.card_img_pig, R.drawable.card_word_pig));
			vItems.add(new Item("ten", R.drawable.img_10, R.drawable.word_frog, R.drawable.card_img_frog, R.drawable.card_word_frog));
		}else if (_theme.equals(Const.THEME_COLOR)) {
			vItems.add(new Item("red", R.drawable.img_red, R.drawable.word_dog, R.drawable.card_img_dog, R.drawable.card_word_dog));
			vItems.add(new Item("pink", R.drawable.img_yellow, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
			vItems.add(new Item("blue", R.drawable.img_blue, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));			
			vItems.add(new Item("gray", R.drawable.img_blue, R.drawable.word_bear, R.drawable.card_img_bear, R.drawable.card_word_bear));
			vItems.add(new Item("green", R.drawable.img_green, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			
			vItems.add(new Item("write", R.drawable.img_orange, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));
			vItems.add(new Item("black", R.drawable.img_green, R.drawable.word_duck, R.drawable.card_img_duck, R.drawable.card_word_duck));
			vItems.add(new Item("orange", R.drawable.img_orange, R.drawable.word_mouse, R.drawable.card_img_mouse, R.drawable.card_word_mouse));
			vItems.add(new Item("purple", R.drawable.img_purple, R.drawable.word_snake, R.drawable.card_img_snake, R.drawable.card_word_snake));
			vItems.add(new Item("yellow", R.drawable.img_yellow, R.drawable.word_monkey, R.drawable.card_img_monkey, R.drawable.card_word_monkey));
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
