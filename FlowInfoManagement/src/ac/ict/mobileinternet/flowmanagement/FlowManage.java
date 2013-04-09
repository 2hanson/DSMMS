package ac.ict.mobileinternet.flowmanagement;

import ac.ict.mobileinternet.flowmanagement.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class FlowManage extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	     //设置无标题  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR,
        		WindowManager.LayoutParams.TYPE_STATUS_BAR);
		
		setContentView(R.layout.main);
		initView();
		initViewAboutWifiInterface();
		initViewAboutWCDMAInterface();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateFlag = true;
		updateView();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		updateFlag = false;
	}

	private void initView() {
		/*
		mBtStartFlowManage = (Button) findViewById(R.id.bt_start_flow);
		mBtStartFlowManage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartFlowManage();
			}
		});
		*/
		mBtStartMobileManage = (Button) findViewById(R.id.bt_start_mobile);
		mBtStartMobileManage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartMobileManage();
			}
		});
		mBtWifiInterface = (Button) findViewById(R.id.bt_wifi_interfacce);
		mBtWifiInterface.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FlowManage.this,
						WifiManagement.class);
				startActivity(intent);
			}
		});

		mBtWCDMAInterface = (Button) findViewById(R.id.bt_wcdma_interfacce);
		mBtWCDMAInterface.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(FlowManage.this,
						WCDMAManagement.class);
				startActivity(intent);
			}
		});
	}

	private void StartFlowManage() {

	}

	private void StartMobileManage() {

	}

	private void initViewAboutWifiInterface() {
		mTvWifiName = (TextView) findViewById(R.id.tv_wifi_name);
		mTvWifiStatus = (TextView) findViewById(R.id.tv_wifi_status);
		mTvWifiFlowData = (TextView) findViewById(R.id.tv_wifi_flow_data);
		mTvWifiBandwitdh = (TextView) findViewById(R.id.tv_wifi_bandwidth);
	}

	private void initViewAboutWCDMAInterface() {
		mTvWCDMAName = (TextView) findViewById(R.id.tv_wcdma_name);
		mTvWCDMAStatus = (TextView) findViewById(R.id.tv_wcdma_status);
		mTvWCDMAFlowData = (TextView) findViewById(R.id.tv_wcdma_flow_data);
		mTvWCDMABandwitdh = (TextView) findViewById(R.id.tv_wcdma_bandwidth);
		mTvWCDMAHa = (TextView) findViewById(R.id.tv_ha);
		mTvWCDMAHaa = (TextView) findViewById(R.id.tv_haa);
	}

	private void updateView() {
		if (updateFlag) {

			flowInfo.refreshFlowInfo();
			// Wifi interface info update
			mTvWifiName.setText(FlowInfo.IFNAME_WIFI);
			mTvWifiFlowData.setText(Integer.toString(flowInfo.wifiFlowInfo
					.size()));
			if (!flowInfo.wifiFlowInfo.isEmpty()) {
				FlowInfoEntry firstWifiFlow = flowInfo.wifiFlowInfo.get(0);
				mTvWifiStatus.setText(Integer.toString(firstWifiFlow.status));
				mTvWifiBandwitdh.setText("unknown");
			}else
			{
				mTvWifiStatus.setText("null");
				mTvWifiBandwitdh.setText("null");
			}

			// WCDMA interface info update
			mTvWCDMAName.setText(FlowInfo.IFNAME_WCDMA);
			mTvWCDMAFlowData.setText(Integer.toString(flowInfo.wcdmaFlowInfo
					.size()));
			if (!flowInfo.wcdmaFlowInfo.isEmpty()) {
				FlowInfoEntry firstWCDMAFlow = flowInfo.wcdmaFlowInfo.get(0);
				mTvWCDMAStatus.setText(Integer.toString(firstWCDMAFlow.status));
				mTvWCDMABandwitdh.setText("unknown");
				mTvWCDMAHa.setText("unknown");
				mTvWCDMAHaa.setText("unknown");
			}else
			{
				mTvWCDMAStatus.setText("null");
				mTvWCDMABandwitdh.setText("null");
				mTvWCDMAHa.setText("unknown");
				mTvWCDMAHaa.setText("unknown");
			}

			Message msg = new Message();
			msg.what = MSG_UPDATE_VIEW;
			mHandler.sendMessageDelayed(msg, UPDATE_INTERVAL);
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_VIEW:
				updateView();
				break;
			}

		};
	};
	private FlowInfo flowInfo = new FlowInfo();
	private static final int MSG_UPDATE_VIEW = 99;
	private static final int UPDATE_INTERVAL = 2000;

	private boolean updateFlag = false;

	private Button mBtStartFlowManage, mBtStartMobileManage;
	private Button mBtWifiInterface, mBtWCDMAInterface;
	private TextView mTvWifiName, mTvWifiStatus, mTvWifiFlowData,
			mTvWifiBandwitdh;
	private TextView mTvWCDMAName, mTvWCDMAStatus, mTvWCDMAFlowData,
			mTvWCDMABandwitdh, mTvWCDMAHa, mTvWCDMAHaa;

}