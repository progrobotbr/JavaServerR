package com.jsr.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jsr.zdebug.zchannelMsg;

public class zhandleInputStream implements Runnable{
	
	private Thread thread;
	private InputStreamReader isr;
	private BufferedReader br;
	private zchannelMsg mch;
	private boolean running = false;
	
	public zhandleInputStream(zchannelMsg pch, InputStream pis){
		
		this.thread = new Thread(this, "event-handler-stream");
		isr         = new InputStreamReader(pis);
		br          = new BufferedReader(isr);
		mch         = pch;
		this.thread.start();
     
	}
	
	public void run() {
		
		String line="";
		zmsg omsg=null;
		running = true;
		
		while(running==true){
			try{
				line = br.readLine();
				omsg = zmsg.createMsgSuccess(line);
				mch.putB(omsg);
			}catch(Exception ex){
				omsg = zmsg.createMsgError(ex.toString());
				mch.putB(omsg);
			}
		}
		
	}
	
	public void stopThread(){
		running = false;
	}
	
	public void lg(String s){
		System.out.println(s);
	}
	
	
}
