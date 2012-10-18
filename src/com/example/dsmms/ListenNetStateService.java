package com.example.dsmms;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ListenNetStateService extends Service {
	
	 private ConnectivityManager connectivityManager;
	    
	 	private NetworkInfo info;
		
	    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

			@Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	                Log.d("mark", "网络状态已经改变");
	                connectivityManager = (ConnectivityManager)      

	                                         getSystemService(Context.CONNECTIVITY_SERVICE);
	                info = connectivityManager.getActiveNetworkInfo();  
	                if(info != null && info.isAvailable()) {
	                    String name = info.getTypeName();
	                    Log.d("mark", "当前网络名称：" + name);
	                    //R.id.currentIPV4
	                    
	                } else {
	                    Log.d("mark", "没有可用网络");
	                }
	                //www.feeditout.com/android-custom-toast-view-from-service
	                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
	                View layout = inflater.inflate(R.layout.activity_ctrl, null);
	                
	                ((TextView)layout.findViewById(R.id.currentIPV4)).setText(IPAddressHelper.GetHostIpv4());
	        		
	                ((TextView)layout.findViewById(R.id.currentIPV6)).setText(IPAddressHelper.GetHostIpv6());
	            }
	        }
	    };

	    @Override
	    public IBinder onBind(Intent intent) {
	        return null;
	    }

	    @Override
	    public void onCreate() {
	        super.onCreate();
	        IntentFilter mFilter = new IntentFilter();
	        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	        registerReceiver(mReceiver, mFilter);
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        unregisterReceiver(mReceiver);
	    }

}
