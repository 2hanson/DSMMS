package com.example.dsmms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
	InputStream is;
	String type;
	
	StreamGobbler(InputStream is, String type)
	{
		this.is = is;
		this.type = type;
	}
	
	public void run()
	{
		String errorLogFile = "system/bin/errorLog";
		String debugLogFile = "system/bin/debugLog";
		
		File errorLog = new File(errorLogFile);
		File debugLog = new File(debugLogFile);
		
		if (!errorLog.exists())
		{
			try {
				errorLog.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (!debugLog.exists())
		{
			try {
				debugLog.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			//String errorInfo = null;
			//String debugInfo = null;

			while ((line = br.readLine()) != null)
			{
				if (type.equals("Error"))
				{
					writeByOutputStream(errorLogFile, line);
					//errorInfo += line;
					//writeByFileWrite(errorLogFile, line);
				}
				else
				{
					writeByOutputStream(debugLogFile, line);
					//debugInfo += line;
					//writeByFileWrite(debugLogFile, line);
				}
			}
			
			//if (type.equals("Error"))
			//{
				
			//}
			//else
			//{
				
			//}
		}
		catch (IOException ioe)
		{}
	}
	
	public static void writeByOutputStream(String destFile, String content) throws IOException
	{
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(destFile, true);
			fos.write(content.getBytes());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally {
			if (fos != null)
			{
				fos.close();
				fos = null;
			}
		}
	}
}
