package com.wooram.sd;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class SpeedDialWidgetProvider extends AppWidgetProvider {
	// Request code for the contact picker activity

    private SharedPreferences prefs;
    
    private String phone1;
    private String phone2;
    private String phone3;		
    private String phone4;
    private String phone5;
    private String phone6;	
 
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.e("WOORAM", "onUpdate called");
		super.onUpdate(context, appWidgetManager, appWidgetIds);		
	}
	
	@Override
	public void onReceive(Context context, Intent intent0) {
		prefs = context.getSharedPreferences("MyPref", Context.MODE_WORLD_WRITEABLE);
		String action = intent0.getAction();    
		Log.e("WOORAM", "onReceive action:" + action);
		
		phone1 = prefs.getString(Const.PIC_PHONE_01, "empty");
		phone2 = prefs.getString(Const.PIC_PHONE_02, "empty");
		phone3 = prefs.getString(Const.PIC_PHONE_03, "empty");	
		phone4 = prefs.getString(Const.PIC_PHONE_04, "empty");
		phone5 = prefs.getString(Const.PIC_PHONE_05, "empty");
		phone6 = prefs.getString(Const.PIC_PHONE_06, "empty");	
		
		if(action.equals(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)){
			Intent intent = new Intent(context, SDConfigurationActivity.class);
			context.startActivity(intent);
		}		
		
		if(action.equals(Const.ACTION_OUT_CALL1)){

			if (prefs.getString(Const.PIC_NAME_01, "empty").equals("empty")){
				Toast.makeText(context, "Empty data", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + phone1));
				context.startActivity(intent);				
			}
		}
		if(action.equals(Const.ACTION_OUT_CALL2)){

			if (prefs.getString(Const.PIC_NAME_02, "empty").equals("empty")){
				Toast.makeText(context, "Empty data", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + phone2));
				context.startActivity(intent);				
			}
		}
		if(action.equals(Const.ACTION_OUT_CALL3)){

			if (prefs.getString(Const.PIC_NAME_03, "empty").equals("empty")){
				Toast.makeText(context, "Empty data", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + phone3));
				context.startActivity(intent);				
			}
		}
		if(action.equals(Const.ACTION_OUT_CALL4)){

			if (prefs.getString(Const.PIC_NAME_04, "empty").equals("empty")){
				Toast.makeText(context, "Empty data", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + phone4));
				context.startActivity(intent);				
			}
		}
		if(action.equals(Const.ACTION_OUT_CALL5)){

			if (prefs.getString(Const.PIC_NAME_05, "empty").equals("empty")){
				Toast.makeText(context, "Empty data", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + phone5));
				context.startActivity(intent);				
			}
		}
		if(action.equals(Const.ACTION_OUT_CALL6)){

			if (prefs.getString(Const.PIC_NAME_06, "empty").equals("empty")){
				Toast.makeText(context, "Empty data", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + phone6));
				context.startActivity(intent);				
			}
		}
		super.onReceive(context, intent0);
	}
}