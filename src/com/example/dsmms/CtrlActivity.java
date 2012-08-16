package com.example.dsmms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CtrlActivity extends Activity {

	private Process process;
	private Button testButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_ctrl);

        /*
        */

        try {
			startCommand("system/bin/cngictrl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        this.testButton = (Button)findViewById(R.id.testbutton);
        
        this.testButton.setOnClickListener(onTestButtonClick);
	}
	
    private OnClickListener onTestButtonClick = new OnClickListener() {
		public void onClick(View v) {
			
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
    	if (this.process != null)
    	{
    		this.process.destroy();
    		this.process = null;
    	}
    	Runtime runtime = Runtime.getRuntime();
    	
    	this.process = runtime.exec(command);
    	
    }
    
    private void stopCommand()
    {
    	if (this.process != null)
    	{
    		this.process.destroy();
    	}
    }
    
}
