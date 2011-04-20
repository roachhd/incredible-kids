package com.incrediblekids.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.incrediblekids.util.Const;

public class RetryPopupActivity extends Activity {
	
	private final String TAG = "RetryPopupActivity";
	
	private ImageView m_Ok;
	private ImageView m_Cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.retry_dialog);
		
		m_Ok 	 = (ImageView)findViewById(R.id.ivOk);
		m_Cancel = (ImageView)findViewById(R.id.ivCancel);
		
		m_Ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
//				Intent intent = new Intent(RetryPopupActivity.this, MatchQuiz.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivityForResult(intent, Const.RETRY_DIALOG_RESULT);
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
