package ac.ict.mobileinternet.flowmanagement;

import java.util.ArrayList;

public class FlowInfo {

	public static final String IFNAME_WIFI = "wlan0";
	public static final String IFNAME_WCDMA = "foo";	// a sample interface name

	public ArrayList<FlowInfoEntry> wifiFlowInfo = new ArrayList<FlowInfoEntry>();
	public ArrayList<FlowInfoEntry> wcdmaFlowInfo = new ArrayList<FlowInfoEntry>();

	public void refreshFlowInfo() {
		FlowInfoEntry[] flowEntries = getFlowInfo();
		wifiFlowInfo = new ArrayList<FlowInfoEntry>();
		wcdmaFlowInfo = new ArrayList<FlowInfoEntry>();
		for (FlowInfoEntry entry : flowEntries) {
			if (IFNAME_WIFI.equals(entry.ifname))
				wifiFlowInfo.add(entry);
			else if (IFNAME_WCDMA.equals(entry.ifname))
				wcdmaFlowInfo.add(entry);
			System.out.println(entry);
		}
	}

	private native FlowInfoEntry[] getFlowInfo();

	static {
		System.loadLibrary("flowinfo");
	}
}
