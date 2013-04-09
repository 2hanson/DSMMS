#define MAJOR_NUM 241
#define IOCTL_GET_OUT_FLOWINFO _IOR(MAJOR_NUM, 1, char *)
#define IOCTL_GET_IN_FLOWINFO _IOR(MAJOR_NUM, 2, char *)

typedef struct
{
	unsigned long s_addr;
	unsigned long d_addr;
	unsigned short sport;
	unsigned short dport;
	char ifname[12];
	unsigned short flowID;
	unsigned char status;
	unsigned char protocol; /*TCP/UDP/ICMP/OTHER*/
} FlowInfo;

#define MAX_FLOW_NUM 30	/* the max number of flow entry get from the kernel every time */
#define LOCAL_BUF_LEN ((MAX_FLOW_NUM) * sizeof(FlowInfo) + 2 * sizeof(int))
