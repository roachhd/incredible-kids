package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class GameStatusActivity extends Activity {
	
	private final String TAG = "GameStatusActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.game_status);
		
		final Intent intent = new Intent(GameStatusActivity.this, ThemeItemActivity.class);
		
		Handler tempHandler = new Handler();
		
		tempHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(intent);
			}
		}, 3000);
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
