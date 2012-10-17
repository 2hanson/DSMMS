package com.example.dsmms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button startButton;
	private Button stopButton;
	private Button ctrlButton;
	private Process process;
	
	private int buttonStatus = 0;//0 is start; 1 is stop
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);        
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bar);
        
        //add listener
        this.startButton = (Button)findViewById(R.id.startbutton);
        this.stopButton = (Button)findViewById(R.id.stopbutton);
        this.ctrlButton = (Button)findViewById(R.id.ctrlbutton);
        this.startButton.setOnClickListener(onStartButtonClick);
        this.stopButton.setOnClickListener(onStopButtonClick);
        this.ctrlButton.setOnClickListener(onCtrlButtonClick);
    }

    private OnClickListener onStartButtonClick = new OnClickListener() {
		public void onClick(View v) {
			changeButtonStatus();

			try {
				startCommand("system/bin/mn");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
    private OnClickListener onStopButtonClick = new OnClickListener() {
		public void onClick(View v) {
			changeButtonStatus();
			try {
				stopCommand();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
    private OnClickListener onCtrlButtonClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, CtrlActivity.class);
			startActivity(intent);
			
			/*
			Intent intent = 
					new Intent("jackpal.androidterm.RUN_SCRIPT");
					//new Intent("jackpal.androidterm.OPEN_NEW_WINDOW");
			intent.addCategory(Intent.CATEGORY_PREFERENCE);
			
			String command = getString(R.string.default_script);
			
			intent.putExtra("jackpal.androidterm.iInitialCommand", command);
			startActivity(intent);*/
		}
	};
	
	void inputP() throws IOException
	{
    	OutputStream stdin = null;
    	stdin = this.process.getOutputStream();
    	String line = "help" + "\n";
    	try {
			stdin.write( line.getBytes() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	stdin.flush();
    	stdin.close();
	}
	
	private void changeButtonStatus()
	{
		this.buttonStatus = 1 - this.buttonStatus;
		if (this.buttonStatus == 0)
		{
			this.startButton.setVisibility(View.VISIBLE);
			this.stopButton.setVisibility(View.GONE);
			this.ctrlButton.setClickable(false);
		}
		else
		{
			this.stopButton.setVisibility(View.VISIBLE);
			this.startButton.setVisibility(View.GONE);
			this.ctrlButton.setClickable(true);
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // Executes UNIX command.
    private void startCommand(String command) throws IOException, InterruptedException 
    {
    	if (this.process != null)
    	{
    		this.process.destroy();
    	}

    	Runtime runtime = Runtime.getRuntime();
    	
    	this.process = runtime.exec(new String[]{ "/system/xbin/su", "-c", command});
    	StreamGobbler errorGobbler = new StreamGobbler(this.process.getErrorStream(), "Error");
    	StreamGobbler stdoutGobbler = new StreamGobbler(this.process.getInputStream(), "Output");
    	errorGobbler.start();
    	stdoutGobbler.start();
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
    
    private void stopCommand() throws IOException, InterruptedException
    {
    	if (this.process != null)
    	{
    		this.process.destroy();
    	}
    	
    	runtimeEXEC(new String[]{ "/system/xbin/su", "-c", "ps", "-U", "mn"}, true);
    }
}
