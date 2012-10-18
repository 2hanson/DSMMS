package com.example.dsmms;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPAddressHelper {

	public static String GetHostIpv4() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();)
				{
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {  
		                //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {  
		                    return inetAddress.getHostAddress().toString();      
					}
				}
			}
		} 
		catch (SocketException ex) {
		} 
		catch (Exception e) {
		}
		return "";
	}
	
	public static String GetHostIpv6() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();)
				{
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {  
		                //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {  
		                    return inetAddress.getHostAddress().toString();      
					}
				}
			}
		} 
		catch (SocketException ex) {
		} 
		catch (Exception e) {
		}
		return "";
	}
	
}
