package com.wooram.sd;

import com.wooram.contact.ContactAccessor;
import com.wooram.contact.ContactInfo;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

public class SDConfigurationActivity extends Activity  {

    // Request code for the contact picker activity
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final String TAG = "SDConfigurationActivity";
    private static int currentSelectedPic = -1;
    private static int widgetId;
    
    private AppWidgetManager appWidgetManager;
    private RemoteViews views;

    private final ContactAccessor mContactAccessor = ContactAccessor.getInstance();
    private String name1;
    private String name2;
    private String name3;
    private String name4;
    private String name5;
    private String name6;

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.conf_layout);
        Log.e(TAG, "setContentView");
        
        SharedPreferences prefs = getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
		prefs = this.getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
		name1 = prefs.getString(Const.PIC_NAME_01, "empty");
		name2 = prefs.getString(Const.PIC_NAME_02, "empty");
		name3 = prefs.getString(Const.PIC_NAME_03, "empty");
		name4 = prefs.getString(Const.PIC_NAME_04, "empty");
		name5 = prefs.getString(Const.PIC_NAME_05, "empty");
		name6 = prefs.getString(Const.PIC_NAME_06, "empty");
		
		TextView tv_name1 = (TextView) findViewById(R.id.tv_pic01);
		TextView tv_name2 = (TextView) findViewById(R.id.tv_pic02);
		TextView tv_name3 = (TextView) findViewById(R.id.tv_pic03);
		TextView tv_name4 = (TextView) findViewById(R.id.tv_pic04);
		TextView tv_name5 = (TextView) findViewById(R.id.tv_pic05);
		TextView tv_name6 = (TextView) findViewById(R.id.tv_pic06);
		tv_name1.setText(name1);
		tv_name2.setText(name2);
		tv_name3.setText(name3);
		tv_name4.setText(name4);
		tv_name5.setText(name5);
		tv_name6.setText(name6);		
        
        appWidgetManager = AppWidgetManager.getInstance(this);
        views = new RemoteViews(getPackageName(), R.layout.main);
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
        	widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Button pickContact = (Button)findViewById(R.id.btn_pic01);
        pickContact.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	currentSelectedPic = 1;
                pickContact();
            }
        });
        Button pickContact2 = (Button)findViewById(R.id.btn_pic02);
        pickContact2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	currentSelectedPic = 2;
                pickContact();
            }
        });
        Button pickContact3 = (Button)findViewById(R.id.btn_pic03);
        pickContact3.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	currentSelectedPic = 3;
                pickContact();
            }
        });
        Button pickContact4 = (Button)findViewById(R.id.btn_pic04);
        pickContact4.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	currentSelectedPic = 4;
                pickContact();
            }
        });
        Button pickContact5 = (Button)findViewById(R.id.btn_pic05);
        pickContact5.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	currentSelectedPic = 5;
                pickContact();
            }
        });
        Button pickContact6 = (Button)findViewById(R.id.btn_pic06);
        pickContact6.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	currentSelectedPic = 6;
                pickContact();
            }
        });
        
        Intent intent1 = new Intent(Const.ACTION_OUT_CALL1);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(SDConfigurationActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button01, pendingIntent);
		
		Intent intent2 = new Intent(Const.ACTION_OUT_CALL2);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(SDConfigurationActivity.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button02, pendingIntent2);
		
		Intent intent3 = new Intent(Const.ACTION_OUT_CALL3);
		PendingIntent pendingIntent3 = PendingIntent.getBroadcast(SDConfigurationActivity.this, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button03, pendingIntent3);

		Intent intent4 = new Intent(Const.ACTION_OUT_CALL4);
		PendingIntent pendingIntent4 = PendingIntent.getBroadcast(SDConfigurationActivity.this, 0, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button04, pendingIntent4);
		
		Intent intent5 = new Intent(Const.ACTION_OUT_CALL5);
		PendingIntent pendingIntent5 = PendingIntent.getBroadcast(SDConfigurationActivity.this, 0, intent5, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button05, pendingIntent5);

		Intent intent6 = new Intent(Const.ACTION_OUT_CALL6);
		PendingIntent pendingIntent6 = PendingIntent.getBroadcast(SDConfigurationActivity.this, 0, intent6, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button06, pendingIntent6);
		
		Intent intent7 = new Intent(SDConfigurationActivity.this, SDConfigurationActivity.class);
		intent7.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
		PendingIntent pendingIntent7 = PendingIntent.getActivity(SDConfigurationActivity.this, 0, intent7, 0);
		views.setOnClickPendingIntent(R.id.widget_conf, pendingIntent7);
        
        Button finish = (Button)findViewById(R.id.btn_finish);

        finish.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	Intent resultValue = new Intent();   
            	
            	SharedPreferences prefs = getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
        		prefs = SDConfigurationActivity.this.getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
        		name1 = prefs.getString(Const.PIC_NAME_01, "empty");
        		name2 = prefs.getString(Const.PIC_NAME_02, "empty");
        		name3 = prefs.getString(Const.PIC_NAME_03, "empty");
        		name4 = prefs.getString(Const.PIC_NAME_04, "empty");
        		name5 = prefs.getString(Const.PIC_NAME_05, "empty");
        		name6 = prefs.getString(Const.PIC_NAME_06, "empty");
            	
            	views.setTextViewText(R.id.button01, name1);
            	views.setTextViewText(R.id.button02, name2);
            	views.setTextViewText(R.id.button03, name3);
            	views.setTextViewText(R.id.button04, name4);
            	views.setTextViewText(R.id.button05, name5);
            	views.setTextViewText(R.id.button06, name6);
                       		
        		
        		appWidgetManager.updateAppWidget(widgetId, views);
            	resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    protected void pickContact() {
    	Intent intent = mContactAccessor.getPickContactIntent();
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            loadContactInfo(data.getData());
        }
    }
 
    private void loadContactInfo(Uri contactUri) {

        AsyncTask<Uri, Void, ContactInfo> task = new AsyncTask<Uri, Void, ContactInfo>() {

            @Override
            protected ContactInfo doInBackground(Uri... uris) {
                return mContactAccessor.loadContact(getContentResolver(), uris[0]);
            }

            @Override
            protected void onPostExecute(ContactInfo result) {
                bindView(result);
            }
        };

        task.execute(contactUri);
    }

    protected void bindView(ContactInfo contactInfo) {
    	SharedPreferences prefs = getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor ed = prefs.edit();
    	
    	switch(currentSelectedPic){
    	case 1:
    		TextView displayNameView = (TextView) findViewById(R.id.tv_pic01);
            displayNameView.setText(contactInfo.getDisplayName());
            ed.putString(Const.PIC_NAME_01, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_01, contactInfo.getPhoneNumber());
        	ed.commit();
    		break;
    	case 2:
    		TextView displayNameView2 = (TextView) findViewById(R.id.tv_pic02);
    		displayNameView2.setText(contactInfo.getDisplayName());
    		ed.putString(Const.PIC_NAME_02, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_02, contactInfo.getPhoneNumber());
        	ed.commit();
    		break;
    	case 3:
    		TextView displayNameView3 = (TextView) findViewById(R.id.tv_pic03);
    		displayNameView3.setText(contactInfo.getDisplayName());
    		ed.putString(Const.PIC_NAME_03, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_03, contactInfo.getPhoneNumber());
        	ed.commit();
    		break;
    	case 4:
    		TextView displayNameView4 = (TextView) findViewById(R.id.tv_pic04);
    		displayNameView4.setText(contactInfo.getDisplayName());
            ed.putString(Const.PIC_NAME_04, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_04, contactInfo.getPhoneNumber());
        	ed.commit();
    		break;
    	case 5:
    		TextView displayNameView5 = (TextView) findViewById(R.id.tv_pic05);
            displayNameView5.setText(contactInfo.getDisplayName());
            ed.putString(Const.PIC_NAME_05, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_05, contactInfo.getPhoneNumber());
        	ed.commit();
    		break;
    	case 6:
    		TextView displayNameView6 = (TextView) findViewById(R.id.tv_pic06);
            displayNameView6.setText(contactInfo.getDisplayName());
            ed.putString(Const.PIC_NAME_06, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_06, contactInfo.getPhoneNumber());
        	ed.commit();
    		break;
    	default:
    		break;
    	}
        
    }
}