package com.jsv.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.jsv.data.SData;
import com.jsv.utils.Utils;


public class ClientSession {
	
	private int rc;
	//Rede
	public ClientRequest  Request;
	public ClientResponse Response;
	
	private ClientProtocol mProtocol;
	private Socket         mSocket;
	
	//Streams
	private InputStream       mIs;
	private OutputStream      mOs;
	private InputStreamReader mIsr;
	private BufferedReader    mBr;
	
	public ClientSession() {
		mSocket   = null; 
		Request   = new ClientRequest();
		Response  = new ClientResponse();
		mProtocol = new ClientProtocol(this);
		Request.setSession(this);
		Response.setSession(this);
		rc=0;
	}
	
	public boolean connectToServer(){
		boolean b;
		rc=4;
		b=this.connect("localhost",5555);
		if(b==false){
			rc=1;
			return(b);
		}else{
			rc=this.sayHello();
			if(rc!=0){
				return(false);
			}else{
				return(true);
			}
		}
		
	}
	
	public int getRC(){
		return(rc);
	}
	
	public int sayNewTransaction(String sTrz){
		this.rc=mProtocol.CallTransaction(sTrz);
		return(this.rc);
	}
	
	public int sayGoto(){
		this.rc=mProtocol.Goto();
		return(this.rc);
	}
	
	private int sayHello(){
		int rc = mProtocol.Hello();
		return(rc);
	}
	
	
	private boolean connect(String pHost, int pPort){
		boolean bRet = false;
		try{
			mSocket = new Socket(pHost, pPort);
			mIs = mSocket.getInputStream();
			mOs = mSocket.getOutputStream();
			mIsr = new InputStreamReader(mIs);
			mBr = new BufferedReader(mIsr);
			//bRet = this.ping();
			bRet=true;
		} catch(Exception ex){ Utils.log(ex.toString());}
		return(bRet);
	}
	
	public boolean ping(){
		boolean bRet=false;
		SData sd = new SData();
		bRet = this.recv(sd);
		if(sd.toString().equals("OLA\n")){
			bRet = true;
		}else{
			return(false);
		}
		bRet = this.sendS("OLA\n");
		return(bRet);
	}
	
	public boolean send(SData pData){
		boolean bRet = false;
		try{
			mOs.write(pData.toString().getBytes());
			bRet = true;
		}catch(Exception ex){Utils.log(ex.toString());}
		return(bRet);
	}
	
	public boolean sendS(String pData){
		boolean bRet=false;
		SData sd = new SData(pData);
		bRet = this.send(sd);
		return(bRet);
	}

	public boolean recv(SData pData){
		boolean bRet = false;
		byte[] y = new byte[512];
		byte[] y2;
		int i;
		pData.init();
		try{
			do{
				i=mIs.read(y);
				if(i>0){
					y2 = new byte[i];
				    System.arraycopy(y,0,y2,0,i);
				    pData.add(new String(y2));
				}
				if(mIs.available()==0){
					i=0;
				}
				bRet=true;
			}while(i > 0);
		}catch(Exception ex){Utils.log(ex.toString());}	
		return(bRet);
	}
	
	public boolean disconnect(){
		boolean bRet = false;
		SData sd = new SData();
		String s = "END";
		bRet = this.sendS(s);
		//bRet = this.recv(sd);
		//Utils.log(sd.toString());
		bRet = this.close();
		return(bRet);
	}
	
	public boolean close(){
		boolean bRet = false;
		try{
			mBr.close();
			mIs.close();
			mOs.close();
			mSocket.close();
			bRet = true;
		}catch(Exception ex){Utils.log(ex.toString());}
		return(bRet);
	}
	
	public void setResponseData(String pData){
		Response.setResponseData(pData);
	}
	
	public ClientRequest getRequest(){
		return(Request);
	}
	
	public ClientResponse getResponse(){
		return(Response);
	}
}
