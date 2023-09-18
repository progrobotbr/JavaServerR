package com.jsv.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.jsv.data.SData;

import com.jsv.server.vm.executor.VMExecutor;
import com.jsv.utils.Utils;

public class ServerSession {

	private ServerRequest  request;
	private ServerResponse response;
	private ServerProtocol protocol;
	private ServerCommand  command;
	private VMExecutor     vmexecutor;
	
	private Socket mSocket;
		
	private InputStream mIs;
	private OutputStream mOs;
	
	public ServerSession(Socket pSocket){
		mSocket = pSocket;
		this.redirecSocketStream();
		request  = new ServerRequest(mIs);
		response = new ServerResponse(mOs);
		protocol = new ServerProtocol(this);
		command  = new ServerCommand(this);
		vmexecutor = new VMExecutor();
	}
	
	private boolean redirecSocketStream(){
		boolean bRet=false;
		try{
			mIs = mSocket.getInputStream();
			mOs = mSocket.getOutputStream();
		}catch(Exception ex){ Utils.log(ex.toString());}
		return(bRet);
	}
	
	public boolean Block(){
		boolean b = false;
		int i;
		byte[] y = new byte[512];
		try{
			y = new byte[512];
			i=mIs.read(y);
			if(i==-1) return(false);
			request.newRequest(y, i);
			b=true;
		}catch(Exception ex){Utils.log(ex.toString());}
		return(b);
	}
	
	public boolean Close(){
		boolean b=false;
		try{
			mIs.close();
			mOs.close();
			mSocket.close();
			b=true;
		}catch(Exception ex){ Utils.log("1:" + ex.toString());}
		return(b);
	}
	
	public ServerRequest getRequest(){
		return(request);
	}
	
	public ServerResponse getResponse(){
		return(response);
	}
	
	public ServerProtocol getProtocol(){
		return(protocol);
	}
	
	public ServerCommand getCommand(){
		return(command);
	}
}
