package com.incrediblekids.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkConnInfo {

	final static String TAG = "YPCNetworkInfo";
	public static boolean IsWifiAvailable(Context context)
	{
		ConnectivityManager m_NetConnectMgr= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.e(TAG, "m_NetConnectMgr = " + m_NetConnectMgr);
		boolean bConnect = false;
		try
		{
			if( m_NetConnectMgr == null ) return false;

			NetworkInfo info = m_NetConnectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			bConnect = info.isConnected();
			Log.e(TAG, "###### WIFI Logging test: info.isConnected() = " + info.isConnected());

		}
		catch(Exception e)
		{
			return false;
		}

		return bConnect;
	}

	public static boolean Is3GAvailable(Context context)
	{  
		ConnectivityManager m_NetConnectMgr= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.e(TAG, "m_NetConnectMgr = " + m_NetConnectMgr);
		boolean bConnect = false;
		try
		{
			if( m_NetConnectMgr == null ) return false;
			NetworkInfo info = m_NetConnectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			bConnect = info.isConnected();
			Log.e(TAG, "###### 3G Logging test: info.isConnected() = " + info.isConnected());
		}
		catch(Exception e)
		{
			return false;
		}

		return bConnect;
	}
	
	public static String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                	Log.e(TAG, "IP Addr:"+inetAddress.getHostAddress().toString());
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e(TAG, ex.toString());
	    }
	    return null;
	}
}
