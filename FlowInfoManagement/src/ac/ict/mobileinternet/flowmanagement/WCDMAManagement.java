package ac.ict.mobileinternet.flowmanagement;

import ac.ict.mobileinternet.flowmanagement.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WCDMAManagement extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 //设置无标题  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR,
        		WindowManager.LayoutParams.TYPE_STATUS_BAR);
		
		setContentView(R.layout.wcdma);
		
		
		
		initView();

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
		mTvWCDMAName = (TextView) findViewById(R.id.tv_wcdma_name);
		mTvWCDMAStatus = (TextView) findViewById(R.id.tv_wcdma_status);
		mTvWCDMAFlowData = (TextView) findViewById(R.id.tv_wcdma_flow_data);
		mTvWCDMABandwitdh = (TextView) findViewById(R.id.tv_wcdma_bandwidth);
		mTvWCDMASig = (TextView) findViewById(R.id.tv_wcdma_sig);
		mTvWCDMAIp = (TextView) findViewById(R.id.tv_wcdma_ip);
		mBtProtocol[0] = (Button) findViewById(R.id.bt_handoff1);
		mBtProtocol[1] = (Button) findViewById(R.id.bt_handoff2);
		mBtProtocol[2] = (Button) findViewById(R.id.bt_handoff3);
		mBtProtocol[3] = (Button) findViewById(R.id.bt_handoff4);
		mBtProtocol[4] = (Button) findViewById(R.id.bt_handoff5);

		for (int i = 0; i < 5; i++) {
			mBtProtocol[i].setOnClickListener(mOnClickListener);
		}

		mTvSourceAddr[0] = (TextView) findViewById(R.id.tv_sa1);
		mTvSourceAddr[1] = (TextView) findViewById(R.id.tv_sa2);
		mTvSourceAddr[2] = (TextView) findViewById(R.id.tv_sa3);
		mTvSourceAddr[3] = (TextView) findViewById(R.id.tv_sa4);
		mTvSourceAddr[4] = (TextView) findViewById(R.id.tv_sa5);

		mTvDestAddr[0] = (TextView) findViewById(R.id.tv_da1);
		mTvDestAddr[1] = (TextView) findViewById(R.id.tv_da2);
		mTvDestAddr[2] = (TextView) findViewById(R.id.tv_da3);
		mTvDestAddr[3] = (TextView) findViewById(R.id.tv_da4);
		mTvDestAddr[4] = (TextView) findViewById(R.id.tv_da5);

		mTvSourcePort[0] = (TextView) findViewById(R.id.tv_sp1);
		mTvSourcePort[1] = (TextView) findViewById(R.id.tv_sp2);
		mTvSourcePort[2] = (TextView) findViewById(R.id.tv_sp3);
		mTvSourcePort[3] = (TextView) findViewById(R.id.tv_sp4);
		mTvSourcePort[4] = (TextView) findViewById(R.id.tv_sp5);

		mTvDestPort[0] = (TextView) findViewById(R.id.tv_dp1);
		mTvDestPort[1] = (TextView) findViewById(R.id.tv_dp2);
		mTvDestPort[2] = (TextView) findViewById(R.id.tv_dp3);
		mTvDestPort[3] = (TextView) findViewById(R.id.tv_dp4);
		mTvDestPort[4] = (TextView) findViewById(R.id.tv_dp5);

		mOnClickListener = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickHandOff(v.getId());
			}
		};

	}

	private void clickHandOff(int btId) {
		switch (btId) {
		case R.id.bt_handoff1:
			break;
		case R.id.bt_handoff2:
			break;
		case R.id.bt_handoff3:
			break;
		case R.id.bt_handoff4:
			break;
		case R.id.bt_handoff5:
			break;
		}
	}

	private void updateView() {
		if (updateFlag) {

			mTvWCDMAName.setText(FlowInfo.IFNAME_WCDMA);
			mTvWCDMAFlowData.setText(Integer.toString(flowInfo.wcdmaFlowInfo
					.size()));
			if (!flowInfo.wcdmaFlowInfo.isEmpty()) {
				FlowInfoEntry firstWCDMAFlow = flowInfo.wcdmaFlowInfo.get(0);
				mTvWCDMAStatus.setText(Integer.toString(firstWCDMAFlow.status));
				mTvWCDMABandwitdh.setText("unknown");
				mTvWCDMASig.setText("unknown");
				mTvWCDMAIp.setText("unknown");
			} else {
				mTvWCDMAStatus.setText("null");
				mTvWCDMABandwitdh.setText("null");
				mTvWCDMASig.setText("null");
				mTvWCDMAIp.setText("unknown");
			}

			// TODO add the flow info update below
			
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

	private TextView mTvWCDMAName, mTvWCDMAStatus, mTvWCDMAFlowData,
			mTvWCDMABandwitdh, mTvWCDMASig, mTvWCDMAIp;

	private Button mBtProtocol[] = new Button[5];
	private OnClickListener mOnClickListener;
	private TextView mTvSourceAddr[] = new TextView[5];
	private TextView mTvDestAddr[] = new TextView[5];
	private TextView mTvSourcePort[] = new TextView[5];
	private TextView mTvDestPort[] = new TextView[5];

	private static final int MSG_UPDATE_VIEW = 99;
	private static final int UPDATE_INTERVAL = 2000;

	private boolean updateFlag = false;

}