package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainThemeActivity extends Activity implements OnClickListener {
	
	private final String TAG = "MainThemeActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        
        setContentView(R.layout.main_theme);
        final ImageView iv_theme1 = (ImageView)findViewById(R.id.ImageView_theme1);
        final ImageView iv_theme2 = (ImageView)findViewById(R.id.ImageView_theme2);
        final ImageView iv_theme3 = (ImageView)findViewById(R.id.ImageView_theme3);
        final ImageView iv_theme4 = (ImageView)findViewById(R.id.ImageView_theme4);
        
        iv_theme1.setOnClickListener(this);
        iv_theme2.setOnClickListener(this);
        iv_theme3.setOnClickListener(this);
        iv_theme4.setOnClickListener(this);
    }
    
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * ImageView Click하였을 때 실행되는 call back method
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.ImageView_theme1:
			Log.d(TAG, "theme1");
			intent = new Intent(this, ThemeItemActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ImageView_theme2:
			Log.d(TAG, "theme2");
			intent = new Intent(this, ThemeItemActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ImageView_theme3:
			Log.d(TAG, "theme3");
			intent = new Intent(this, ThemeItemActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ImageView_theme4:
			Log.d(TAG, "theme4");
			intent = new Intent(this, SummaryQuiz.class);
			startActivity(intent);
			break;
		default:
			Log.d(TAG, "Error");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}