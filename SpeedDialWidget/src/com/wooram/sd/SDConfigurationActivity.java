package com.wooram.sd;

import com.wooram.contact.ContactAccessor;
import com.wooram.contact.ContactInfo;

import android.app.Activity;
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

/**
 * A simple activity that shows a "Pick Contact" button and two fields: contact's name
 * and phone number.  The user taps on the Pick Contact button to bring up
 * the contact chooser.  Once this activity receives the result from contact picker,
 * it launches an asynchronous query (queries should always be asynchronous) to load
 * contact's name and phone number. When the query completes, the activity displays
 * the loaded data.
 */
public class SDConfigurationActivity extends Activity  {

    // Request code for the contact picker activity
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final String TAG = "SDConfigurationActivity";
    private static int currentSelectedPic = -1;
    private static int widgetId;
    /**
     * An SDK-specific instance of {@link ContactAccessor}.  The activity does not need
     * to know what SDK it is running in: all idiosyncrasies of different SDKs are
     * encapsulated in the implementations of the ContactAccessor class.
     */
    private final ContactAccessor mContactAccessor = ContactAccessor.getInstance();

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.conf_layout);
        Log.e(TAG, "setContentView");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
        	widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // Install a click handler on the Pick Contact button
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
        Button finish = (Button)findViewById(R.id.btn_finish);
        finish.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	Intent resultValue = new Intent();
            	resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    /**
     * Click handler for the Pick Contact button.  Invokes a contact picker activity.
     * The specific intent used to bring up that activity differs between versions
     * of the SDK, which is why we delegate the creation of the intent to ContactAccessor.
     */
    protected void pickContact() {
    	Intent intent = mContactAccessor.getPickContactIntent();
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    /**
     * Invoked when the contact picker activity is finished. The {@code contactUri} parameter
     * will contain a reference to the contact selected by the user. We will treat it as
     * an opaque URI and allow the SDK-specific ContactAccessor to handle the URI accordingly.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            loadContactInfo(data.getData());
        }
    }

    /**
     * Load contact information on a background thread.
     */
    private void loadContactInfo(Uri contactUri) {

        /*
         * We should always run database queries on a background thread. The database may be
         * locked by some process for a long time.  If we locked up the UI thread while waiting
         * for the query to come back, we might get an "Application Not Responding" dialog.
         */
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

    /**
     * Displays contact information: name and phone number.
     */
    protected void bindView(ContactInfo contactInfo) {
    	SharedPreferences prefs = getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
    	SharedPreferences.Editor ed = prefs.edit();
    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
    	RemoteViews views = new RemoteViews(getPackageName(), R.layout.main);
    	switch(currentSelectedPic){
    	case 1:
    		TextView displayNameView = (TextView) findViewById(R.id.tv_pic01);
            displayNameView.setText(contactInfo.getDisplayName());
            ed.putString(Const.PIC_NAME_01, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_01, contactInfo.getPhoneNumber());
        	ed.commit();
        	
        	appWidgetManager.updateAppWidget(widgetId, views);
/*
            TextView phoneNumberView = (TextView) findViewById(R.id.phone_number_text_view);
            phoneNumberView.setText(contactInfo.getPhoneNumber());*/
    		break;
    	case 2:
    		TextView displayNameView2 = (TextView) findViewById(R.id.tv_pic02);
    		displayNameView2.setText(contactInfo.getDisplayName());
    		ed.putString(Const.PIC_NAME_02, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_02, contactInfo.getPhoneNumber());
        	ed.commit();
        	appWidgetManager.updateAppWidget(widgetId, views);
    		break;
    	case 3:
    		TextView displayNameView3 = (TextView) findViewById(R.id.tv_pic03);
    		displayNameView3.setText(contactInfo.getDisplayName());
    		ed.putString(Const.PIC_NAME_03, contactInfo.getDisplayName());
            ed.putString(Const.PIC_PHONE_03, contactInfo.getPhoneNumber());
        	ed.commit();
        	appWidgetManager.updateAppWidget(widgetId, views);
    		break;
    	default:
    		break;
    	}
        
    }
}