package com.jsv.server;

//import java.io.BufferedInputStream;
import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

//import data.SData;
import com.jsv.server.vm.executor.VMExecutor;

import com.jsv.utils.Utils;

public class ServerClientNode extends Thread{

	private Socket         mSocket = null;
	private ServerSession  mSession;
	private ServerRequest  request;
	private ServerResponse response;
	private ServerProtocol protocol;
	private ServerCommand  command;
//	private VMExecutor     vmexecutor;
	
//	private InputStream mIs;
//	private OutputStream mOs;
//	private InputStreamReader mIsr;
//	private BufferedReader mBr;
	
	public ServerClientNode(Socket pSocket){
	    this.mSocket = pSocket;
	    mSession = new ServerSession(mSocket);
	    request  = mSession.getRequest();
	    response = mSession.getResponse();
	    protocol = mSession.getProtocol();
	    command  = mSession.getCommand();
	}
	
	public void run(){
		boolean b;
		try{
			b=this.MainLoop();
		}catch(Exception ex){ 
			Utils.log("ServerClientNode: " + Utils.writeStackTrace(ex));
		}
	}
	
	public boolean MainLoop(){
		boolean b=false;
		
		for(;;){
			b = mSession.Block();
			if(!b) {return(b);}
			b = command.executeServerCommand(); 
			if(!b) {return(b);}
		}
	}
	
}

