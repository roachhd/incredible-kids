package com.incrediblekids.activities;

import com.incrediblekids.util.Const;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PopupActivity extends Activity {
	
	private final String TAG = "PopupActivity";
	
	private LinearLayout m_ll;
	private ImageView m_Ok;
	private ImageView m_Cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.retry_dialog);
		
		String action = getIntent().getAction();
		
		m_ll = (LinearLayout)findViewById(R.id.main_bg);
		
		if(action == null || !action.equals(Const.MATCH_QUIZ)) {
			m_ll.setBackgroundResource(R.drawable.popup_game_retry);
		}
		
		m_Ok 	 = (ImageView)findViewById(R.id.ivOk);
		m_Cancel = (ImageView)findViewById(R.id.ivCancel);
		
		m_Ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				setResult(RESULT_OK);
				finish();
			}
		});
		
		m_Cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
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
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}
	
	
}
