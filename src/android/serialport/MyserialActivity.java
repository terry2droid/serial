package android.serialport;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

import android.serialport.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyserialActivity extends Activity implements View.OnClickListener{
	
	EditText mHintMessage;
	FileOutputStream mOutputStream;
	FileInputStream mInputStream;
	SerialPort mSerialPort = null;
	final String PORTNUM = "/dev/ttyS4";
	final int BAUDRATE = 115200;
	final String SENDMESSAGE ="Hello";
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.main);
      
      final Button mBtnOpen = (Button)findViewById(R.id.btn_openconsole);
      final Button mBtnClose = (Button)findViewById(R.id.btn_closeconsole);
      final Button mBtnSend = (Button)findViewById(R.id.btn_send);
      final Button mBtnRec = (Button)findViewById(R.id.btn_rec);
      mHintMessage = (EditText)findViewById(R.id.edt_hint);
    
      mBtnOpen.setOnClickListener(this);
      mBtnClose.setOnClickListener(this);
      mBtnSend.setOnClickListener(this);
      mBtnRec.setOnClickListener(this);
      
    
    }
        
    void onDataReceived(final byte[] buffer, final int size) {
	   runOnUiThread(new Runnable() {
		    public void run() {
			  if (mHintMessage != null) {
				  mHintMessage.append(new String(buffer, 0, size));
			  }
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id =arg0.getId();
		switch(id){
		case R.id.btn_openconsole:
			
			if(mSerialPort == null){
			    try {
			    	  mSerialPort=new SerialPort(new File(PORTNUM),BAUDRATE);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}       
			    mInputStream=(FileInputStream) mSerialPort.getInputStream();
			}
			break;
		case R.id.btn_closeconsole:	
			
			if(mSerialPort!=null){		
				mSerialPort.close();
				mSerialPort = null;
			}
			break;
		case R.id.btn_send:	
			if(mSerialPort!=null){
				try {
					mOutputStream=(FileOutputStream) mSerialPort.getOutputStream();				
					mOutputStream.write(new String(SENDMESSAGE).getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			     Toast.makeText(getApplicationContext(), "send message",
			    		     Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_rec:
		    if(mSerialPort!=null){
				int size;	
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer, size);
						
					}
			    } catch (IOException e) {
				    e.printStackTrace();
				    return;
			    } 
		    }
			break;
		default:
			break;
		}
	}


    
}