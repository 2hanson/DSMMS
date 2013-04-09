#include <string.h>
#include <sys/ioctl.h>                /* ioctl */
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <arpa/inet.h>               //for inet_ntop
#include <jni.h>
#include "flowinfo.h"                 //chardevice
static int charDevFd;
static char inflow[LOCAL_BUF_LEN];
static char outflow[LOCAL_BUF_LEN];

int pull_flowinfo();

JNIEXPORT jobjectArray JNICALL Java_ac_ict_mobileinternet_flowmanagement_FlowInfo_getFlowInfo(
		JNIEnv* env, jobject obj)
{
	int i, flownum;
	int num_inflow, num_outflow;
	char addr[20];
	FlowInfo *ep_inflow, *ep_outflow, *ep; /* entry pointer*/

	jclass entryClass = (*env)->FindClass(env,
			"ac/ict/mobileinternet/flowmanagement/FlowInfoEntry");
	jmethodID entryConID = (*env)->GetMethodID(env, entryClass, "<init>",
			"()V");
	flownum = pull_flowinfo();
	if (flownum <= 0)
		return (*env)->NewObjectArray(env, 0, entryClass, 0); /* return an array of 0 entry*/

	jobjectArray jentry_arr = (*env)->NewObjectArray(env, flownum, entryClass,
			0);
	/********************* get field IDs *********************/
	jfieldID s_addrID = (*env)->GetFieldID(env, entryClass, "s_addr",
			"Ljava/lang/String;");
	jfieldID d_addrID = (*env)->GetFieldID(env, entryClass, "d_addr",
			"Ljava/lang/String;");
	jfieldID sportID = (*env)->GetFieldID(env, entryClass, "sport", "I");
	jfieldID dportID = (*env)->GetFieldID(env, entryClass, "dport", "I");
	jfieldID ifnameID = (*env)->GetFieldID(env, entryClass, "ifname",
			"Ljava/lang/String;");
	jfieldID flowIDID = (*env)->GetFieldID(env, entryClass, "flowID", "I");
	jfieldID statusID = (*env)->GetFieldID(env, entryClass, "status", "I");
	jfieldID protocolID = (*env)->GetFieldID(env, entryClass, "protocol",
			"Ljava/lang/String;");
	/***********************************************************/

	num_inflow = *(int*) inflow;
	ep_inflow = (FlowInfo*) (inflow + 2 * sizeof(int));
	num_outflow = *(int*) outflow;
	ep_outflow = (FlowInfo*) (outflow + 2 * sizeof(int));

	for (i = 0; i < flownum; i++)
	{
		if (i < num_inflow)
			ep = ep_inflow + i;
		else
			ep = ep_outflow + i - num_inflow;

		jobject entry = (*env)->NewObject(env, entryClass, entryConID);
		/* set addr */
		memset(addr, 0, sizeof(addr));
		inet_ntop(AF_INET, &(ep->s_addr), addr, sizeof(addr));
		(*env)->SetObjectField(env, entry, s_addrID,
				(*env)->NewStringUTF(env, addr));
		memset(addr, 0, sizeof(addr));
		inet_ntop(AF_INET, &(ep->d_addr), addr, sizeof(addr));
		(*env)->SetObjectField(env, entry, d_addrID,
				(*env)->NewStringUTF(env, addr));
		/* set ports */
		(*env)->SetIntField(env, entry, sportID, (int) ntohs(ep->sport));
		(*env)->SetIntField(env, entry, dportID, (int) ntohs(ep->dport));
		/* set ifname */
		(*env)->SetObjectField(env, entry, ifnameID,
				(*env)->NewStringUTF(env, ep->ifname));
		/* set flowID */
		(*env)->SetIntField(env, entry, flowIDID, (int) (ep->flowID));
		/* set status */
		(*env)->SetIntField(env, entry, statusID, (int) (ep->status));
		/* set protocol*/
		char * protocol;
		switch (ep->protocol)
		{
		case 0x01:
			protocol = "ICMP";
			break;
		case 0x06:
			protocol = "TCP";
			break;
		case 0x11:
			protocol = "UDP";
			break;
		default:
			protocol = "unknown";
		}
		(*env)->SetObjectField(env, entry, protocolID,
				(*env)->NewStringUTF(env, protocol));

		(*env)->SetObjectArrayElement(env, jentry_arr, i, entry);
	}

	return jentry_arr;
}

int pull_flowinfo()
{
	/*
	 * return values:
	 * 	chardev open failed			-1
	 * 	inflow ioctl() failed		-2
	 * 	outflow ioctl() failed		-3
	 * 	total num of flow entries	positive number
	 */

	int num_inflow, num_outflow;
	/* we don't have the root privilege which insmod needed */
	system("insmod /data/mm.ko");
	charDevFd = open("/dev/chardev", O_RDONLY);
	system("chmod 777 /dev/chardev");

	if (charDevFd < 0)
	{
		printf("chardev open field!\n");
		return -1;
	}
	//printf("charDevFd: %d\n",charDevFd);

	ioctl(charDevFd, IOCTL_GET_IN_FLOWINFO, inflow);
	ioctl(charDevFd, IOCTL_GET_OUT_FLOWINFO, outflow);

	close(charDevFd);

	num_inflow = *(int*) inflow;
	if (num_inflow < 0)
	{
		printf("inflow ioctl() failed!\n");
		return -2;
	}

	num_outflow = *(int*) outflow;
	if (num_outflow < 0)
	{
		printf("outflow ioctl() failed!\n");
		return -3;
	}

	return num_inflow + num_outflow;
}

