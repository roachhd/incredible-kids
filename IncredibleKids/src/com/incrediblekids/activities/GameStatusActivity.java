package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.incrediblekids.util.Const;

public class GameStatusActivity extends Activity {
	
	private final String TAG = "GameStatusActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game_status);
		
		ResourceClass m_ResourceClass = ResourceClass.getInstance();
		m_ResourceClass.vItems.size();
		final Intent intent = new Intent(GameStatusActivity.this, ThemeItemActivity.class);
		
		ImageView ivLevel_1 = (ImageView)findViewById(R.id.level_1);
		ImageView ivLevel_2 = (ImageView)findViewById(R.id.level_2);
		ImageView ivLevel_3 = (ImageView)findViewById(R.id.level_3);
		ImageView ivLevel_4 = (ImageView)findViewById(R.id.level_4);
		
		if (m_ResourceClass.getCurrentTheme().equals(Const.THEME_ANIMAL) 
				|| m_ResourceClass.getCurrentTheme().equals(Const.THEME_TOY)){
			ivLevel_3.setVisibility(View.VISIBLE);
			ivLevel_4.setVisibility(View.VISIBLE);
		}else{
			ivLevel_3.setVisibility(View.GONE);
			ivLevel_4.setVisibility(View.GONE);
		}
		
		ivLevel_1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				intent.putExtra(Const.CUR_LEVEL, Const.LEVEL_1);
				startActivity(intent);
			}
			
		});
		ivLevel_2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				intent.putExtra(Const.CUR_LEVEL, Const.LEVEL_2);
				startActivity(intent);
			}
			
		});
		ivLevel_3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				intent.putExtra(Const.CUR_LEVEL, Const.LEVEL_3);
				startActivity(intent);
			}
			
		});
		ivLevel_4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				intent.putExtra(Const.CUR_LEVEL, Const.LEVEL_4);
				startActivity(intent);
			}
			
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO DB update or preference
		super.onNewIntent(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
