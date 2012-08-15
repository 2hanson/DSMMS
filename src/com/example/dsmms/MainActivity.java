package com.example.dsmms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
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
        this.startButton = (Button)findViewById(R.id.startbutton);
        this.stopButton = (Button)findViewById(R.id.stopbutton);
        this.ctrlButton = (Button)findViewById(R.id.ctrlbutton);
        this.startButton.setOnClickListener(onStartButtonClick);
        this.stopButton.setOnClickListener(onStopButtonClick);
    }

    private OnClickListener onStartButtonClick = new OnClickListener() {
		public void onClick(View v) {
			changeButtonStatus();
			//runCommand();
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
			stopCommand();
		}
	};
	
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
    private String startCommand(String command) throws IOException, InterruptedException 
    {
    	if (this.process != null)
    	{
    		this.process.destroy();
    		this.process = null;
    	}
    	StringBuilder retStr = new StringBuilder("");
    	retStr.append("start...");
    	//outPut(retStr.toString());
    	Runtime runtime = Runtime.getRuntime();
    	
    	this.process = runtime.exec(command);
    	
    	retStr.append("runing...");
    	//outPut(retStr.toString());
    	
    	InputStream is = process.getErrorStream();//if getInputStream(), readLine will throw a exception, due to cngi code fprintf.
    	InputStreamReader isr = new InputStreamReader(is);
    	BufferedReader br = new BufferedReader(isr);
    	String line = null;


	    while (null != (line = br.readLine()))/////////exception
	    {
	    	retStr.append(line+"\n");
	    	if (retStr.length() > 100)
	    		break;
	    }

    	retStr.append("end...");
    	//outPut(retStr.toString());
    	if (process != null)
    	{
    		//process.waitFor();
    	}
    	//process.destroy();     stop mn
    	return retStr.toString();
    }
    
    private void stopCommand()
    {
    	if (this.process != null)
    	{
    		this.process.destroy();
    	}
    }
}

