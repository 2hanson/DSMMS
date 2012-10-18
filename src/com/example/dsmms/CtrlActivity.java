package com.example.dsmms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CtrlActivity extends Activity {
		
	private Process process;
	private Button autoTurnButton;
	private Button refreshButton;

	private TextView currentIpv4View;
	private TextView currentIpv6View;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_ctrl);
        
        this.autoTurnButton = (Button)findViewById(R.id.autoTurnButton);
        this.refreshButton = (Button)findViewById(R.id.refreshButton);
        
        this.currentIpv4View = (TextView)findViewById(R.id.currentIPV4);
        this.currentIpv6View = (TextView)findViewById(R.id.currentIPV6);
        this.autoTurnButton.setOnClickListener(onTestButtonClick);
        this.refreshButton.setOnClickListener(onRefreshButtonClick);
        
	}
	
    private OnClickListener onRefreshButtonClick = new OnClickListener() {
		public void onClick(View v) {
			refreshIPAddress();
		}
	};
	
	private void refreshIPAddress()
	{
		
		this.currentIpv4View.setText(IPAddressHelper.GetHostIpv4());
		
		this.currentIpv6View.setText(IPAddressHelper.GetHostIpv6());

	}
	
    private OnClickListener onTestButtonClick = new OnClickListener() {
		public void onClick(View v) {
	        try {
				startCommand("system/bin/yucngictrl");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			stopCommand();
			return false;
		}
		
		return false;
	}
	
	 // Executes UNIX command.
    private void startCommand(String command) throws IOException, InterruptedException 
    {
    	//just for debug
    	
    	if (this.process != null)
    	{
    		this.process.destroy();
    		this.process = null;
    	}

    	//Runtime runtime = Runtime.getRuntime();
    	//this.process = runtime.exec(new String[]{ "/system/xbin/su", "-c", command});
    	
    	runtimeEXEC(new String[]{ "/system/xbin/su", "-c", command}, false);
    	//OutputStream stdin = null;
    	//stdin = this.process.getOutputStream();
    	//String line = "help" + "\n";
    	//stdin.write( line.getBytes() );
    	//stdin.flush();
    	//stdin.close();
    	/*
    	StreamGobbler errorGobbler = new StreamGobbler(this.process.getErrorStream(), "Error");
    	StreamGobbler stdoutGobbler = new StreamGobbler(this.process.getInputStream(), "Output");
    	errorGobbler.start();
    	stdoutGobbler.start();*/
    }
    

    private void runtimeEXEC(String[] command, boolean handleOutput) throws IOException, InterruptedException
    {
    	if (this.process != null)
    	{
    		this.process.destroy();
    	}
    	
    	Runtime runtime = Runtime.getRuntime();
    	
    	this.process = runtime.exec(command);
    	
    	BufferedReader err = new BufferedReader(
		        new InputStreamReader(this.process.getErrorStream()));
    	String lineErr = null;
    	while ((lineErr = err.readLine()) != null) {
    	}
    	
    	BufferedReader in = new BufferedReader(
    		        new InputStreamReader(this.process.getInputStream()));
    	String line = null;
    	boolean skip = true;
    	while ((line = in.readLine()) != null) {
    		if (handleOutput == false)
    		{
    			continue;
    		}
    		//ine += line + "\n";
    		if (skip == true)
    		{
    			skip = false;
    		}
    		else
    		{
    			String pid = "";
    			for (int index = 0; index < line.length(); ++index)
    			{
    				char ch = line.charAt(index);
    				//if ()
    				if (Character.isDigit(ch))
    				{
    					while (Character.isDigit( ch = line.charAt(index) ))
    					{
        					pid = pid + ch;
        					++index;
    					}
        				break;
    				}
    			}
    			
    			if (pid != null && pid != "")
    			{
    				runtimeEXEC(new String[]{ "/system/xbin/su", "-c", "kill", pid}, false);
    			}
    		}
    	}
    }
    
    
    private void stopCommand()
    {
    	if (this.process != null)
    	{
    		this.process.destroy();
    	}
    }
    
}
