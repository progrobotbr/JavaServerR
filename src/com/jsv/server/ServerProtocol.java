package com.jsv.server;

import org.w3c.dom.Document;

import com.jsv.utils.Utils;

public class ServerProtocol {
	
	private ServerSession mSession;
	private ServerRequest request;
	public ServerResponse response;

	public ServerProtocol(ServerSession pSession){
		mSession = pSession;
	    request = mSession.getRequest();
	    response = mSession.getResponse();
	}
		
	public boolean Hello(){
		boolean b;
		Utils.log("HELLO:"+request.getData());
		b = response.sendS(ServerCommand.Hello());
		return(b);
	}
	
	public boolean Message(int rc, String pMsg, String pData){
		boolean b;
		Utils.log("MSG:"+request.getData());
		b = response.sendS(ServerCommand.Message(rc,pMsg,pData));
		return(b);
	}
	
	/*
	public boolean Logoff(){
		boolean b;
		Utils.log("LOGOFF:");
		b = response.sendS(ServerCommand.Logoff());
		return(mSession.Close());
	}*/
	
	public boolean sendScreen(String sData){
		boolean b;
		b = response.sendS(sData);
		return(b);
	}
	
	public boolean SendStru(String sData){
		boolean b;
		b = response.sendS(sData);
		return(b);
	}
	public String getServerCommand(){
		String s = request.getServerCommand();
		return(s);
	}
	
	public String getServerCommand(String pAttribute){
		String s = request.getServerCommand(pAttribute);
		return(s);
	}
	
	public Document getDoc(){
		Document doc = request.getDoc();
		return(doc);
	}
	
	
}
