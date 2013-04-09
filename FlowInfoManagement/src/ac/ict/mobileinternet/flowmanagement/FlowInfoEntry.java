package ac.ict.mobileinternet.flowmanagement;

public class FlowInfoEntry {

	String s_addr;
	String d_addr;
	int sport;
	int dport;
	String ifname;
	int flowID;
	int status;
	String protocol;

	public FlowInfoEntry() {

	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "s_addr:"+s_addr+" d_addr:"+d_addr+" sport:"+sport+" dport:"+dport+" ifname:"
				+ifname+" protocol:"+protocol+"\n";
	}
}
