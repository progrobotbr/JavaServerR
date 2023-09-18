package com.jsv.client;

import java.util.HashMap;

import com.jsv.utils.Utils;

public class ClientProtocol {
	
	public static final int ERR = 4;
	public static final int SUCCESS = 0;
	
	private ClientSession mSession;
	private ClientRequest request;
	private ClientResponse response;
	
	public ClientProtocol(ClientSession pSession){	
		this.setSession(pSession); 
	}
	
	public void setSession(ClientSession pSession){	
		mSession = pSession; 
		request  = mSession.getRequest();
		response = mSession.getResponse();
	}
	
	public ClientSession getSession(){
		return(mSession);
	}
	
	//Commandos 
	public int Hello(){
		int i = request.sendModuleProtocol(ClientCommand.Hello());
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int Logon(){
		int i = request.sendModuleProtocol(ClientCommand.Logon());
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int CallTransaction(String s){
		int i = request.sendModuleProtocol(ClientCommand.CallTransaction(s));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int Goto(){
		int i = request.sendModuleProtocol(ClientCommand.Goto(null));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int Debug(){
		int i = request.sendModuleProtocol(ClientCommand.Debug());
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int DebugF8(){
		int i = request.sendModuleProtocol(ClientCommand.DebugF8(null));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	/*
	public int CallScreen(HashMap pModulo){
		int i;
		ClientHandleRequest handleRequest = new ClientHandleRequest(mSession,pModulo);
		handleRequest.HandleAction();
		i = response.getSubRc();
		if(i!=0){
			Utils.log("Erro");
			System.exit(1);
		}
		return(i);
	}
	*/
	
	public int SaveProgram(String pNome, String pDescr, String pTexto){
		int i = request.sendModuleProtocol(ClientCommand.SavePrg(pNome, pTexto));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int LoadProgram(String pNome){
		int i = request.sendModuleProtocol(ClientCommand.LoadPrg(pNome,"P"));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int LoadProgramCompiled(String pNome){
		int i = request.sendModuleProtocol(ClientCommand.LoadPrg(pNome,"C"));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
		
	public int CompProgram(String pNome, String pTexto){
		int i = request.sendModuleProtocol(ClientCommand.CompPrg(pNome,pTexto));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public String getServerCommand(){
		return(response.getServerCommand());
	}
	
	public String getAttribute(String pAttrib){
		String s;
		s=response.getAttribute(pAttrib);
		return(s);
	}
	
	public int getVarInDebug(String pName){
		String s="";
		int i = request.sendModuleProtocol(ClientCommand.GetVarInDebug(pName));
		if(i==200){
			i=response.getSubRc();
		}
		return(i);
	}
	
	public int subRc(){
		return(response.getSubRc());
	}
	
	public ClientResponse geResponse(){
		return(response);
	}
}
