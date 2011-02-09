package com.wooram.sd;

import com.wooram.contact.ContactAccessor;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class SpeedDialWidgetProvider extends AppWidgetProvider {
	// Request code for the contact picker activity

    private SharedPreferences prefs;
    private String name1;
    private String name2;
    private String name3;
    private String phone1;
    private String phone2;
    private String phone3;		
    
    @Override
    public void onEnabled(Context context){
    	Log.e("WOORAM", "onEnabledonEnabledonEnabledonEnabledonEnabledonEnabledonEnabledonEnabled");
    	updateView(context);
    }
    
    private void updateView(Context context){
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
		
		prefs = context.getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
		name1 = prefs.getString(Const.PIC_NAME_01, "empty");
		name2 = prefs.getString(Const.PIC_NAME_02, "empty");
		name3 = prefs.getString(Const.PIC_NAME_03, "empty");
		
		phone1 = prefs.getString(Const.PIC_PHONE_01, "empty");
		phone2 = prefs.getString(Const.PIC_PHONE_02, "empty");
		phone3 = prefs.getString(Const.PIC_PHONE_03, "empty");		
		
		Intent intent = new Intent(Const.ACTION_OUT_CALL1);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button01, pendingIntent);

		Intent intent2 = new Intent(Const.ACTION_OUT_CALL2);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button02, pendingIntent2);
		
/*		Intent intent3 = new Intent(context,SDConfigurationActivity.class);
		intent3.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
		PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 0, intent3, 0);
		views.setOnClickPendingIntent(R.id.config, pendingIntent3);*/
		
		Log.e("WOORAM", name1 + "  " + phone1);
		views.setTextViewText(R.id.button01, name1);
		views.setTextViewText(R.id.button02, name2);
		views.setTextViewText(R.id.button03, name3);
		
    }
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		
		
		
		/*        views.setOnClickPendingIntent(R.id.button03, pendingIntent);
        views.setOnClickPendingIntent(R.id.button04, pendingIntent);
        views.setOnClickPendingIntent(R.id.button05, pendingIntent);
        views.setOnClickPendingIntent(R.id.button06, pendingIntent);*/
		//appWidgetManager.updateAppWidget(appWidgetIds, views);
		super.onUpdate(context, appWidgetManager, appWidgetIds);		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();    
		Log.e("WOORAM", "onReceive action:" + action);
/*		if(action.equals(Const.ACTION_CONFIGURE)){
			Intent intent4 = new Intent(context,SDConfigurationActivity.class);
			context.startActivity(intent4);
		}*/
		if(action.equals(Const.ACTION_OUT_CALL1)){

			if (prefs.getString("OUT_CALL1", "empty").equals("empty")){
				Toast.makeText(context, "Empty data", Toast.LENGTH_SHORT).show();
			}else{
				Log.e("WOORAM", "call to :"+name1 + "  " + phone1);
				Intent intent3 = new Intent(Intent.ACTION_CALL);
				intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent3.setData(Uri.parse("tel:" + phone1));
				context.startActivity(intent3);				
			}
		}
		if(action.equals(Const.ACTION_OUT_CALL2)){
			Intent intent3 = new Intent(Intent.ACTION_CALL);
			intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent3.setData(Uri.parse("tel:02-6413-5382"));
			context.startActivity(intent3);
		}
		super.onReceive(context, intent);
	}
}