package com.jsr.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.jsr.server.icommands;
import com.jsr.util.streamop;
import com.jsr.zdebug.TTY;
import com.jsr.zdebug.zchannelMsg;

public class zprocess {
	
	private String             mTransactionName;
	private zchannelMsg        mchannel;
	public TTY                 mtty;
	private OutputStreamWriter osrjvm;
	public BufferedReader      ibrjvm;
	public streamop            streamClientThread;
	
	
	public int createVM(zchannelMsg pch){
			
		
		String s[] = { zijvmconstantes.CLASSPATH,  zijvmconstantes.DIRBIN, zijvmconstantes.LAUNCH, zijvmconstantes.FIRSTCLASS };
		
		this.mchannel = pch;
		
    	TTY otty = new TTY();
    	otty.classMain(s, this.mchannel);
    	this.mtty = otty;
    	streamClientThread = new streamop(icommands.IORIGJVM);
    	streamClientThread.Nome = "process";
    	streamClientThread.putStream(this.mchannel, otty.getProcessInputStream(), otty.getProcessOutputStream());
    	//streamClientThread.startAssincronous();
    	this.osrjvm = new OutputStreamWriter(otty.getProcessOutputStream());
    	
    	InputStreamReader isr = new InputStreamReader(otty.getProcessInputStream());
    	ibrjvm = new BufferedReader(isr);
    	
    	
    	return(icommands.ISUCCESS);
		
	}
	
	public void zresume(){
		this.mtty.zresume();
	}
	
	public void execCmd(String s){
		this.mtty.execCmd(s);
	}
	
	public String readInput(){
		
		String line;
		try{
			line=ibrjvm.readLine();
		}catch(Exception ex){
			line="";
		}
		return(line);
	}
	
	public void startReadAssinchronous(){
		streamClientThread.startAssincronous();
	}
	
	public boolean sendJVM(zmsg pMsg){
		boolean b=false;
		try{
			this.osrjvm.write(pMsg.smsg);
			this.osrjvm.flush();
			b=true;
		}catch(Exception ex){
			b=false;
		}
		return(b);
    }
	
	public boolean sendDBG(zmsg pMsg){
		boolean b=false;
		this.mchannel.putA(pMsg);
	    return(b);
	}
	
	public int receive(zmsg pMsg){
		
		int i = icommands.IERROR2;
		zmsg ozmsg;
		ozmsg = this.mchannel.getB();
		i=this.mchannel.getIB();
		pMsg.clone(ozmsg);
		return(i);
		
	}

}
