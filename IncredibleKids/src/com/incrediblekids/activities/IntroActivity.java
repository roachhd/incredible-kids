package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class IntroActivity extends Activity{
	Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);
        
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

			public void run() {
				findViewById(R.id.intro).setVisibility(ImageView.INVISIBLE);
				intent = new Intent(IntroActivity.this, MainThemeActivity.class);		
				startActivity(intent);
				finish();
			}
        }, 10);
    }
}
