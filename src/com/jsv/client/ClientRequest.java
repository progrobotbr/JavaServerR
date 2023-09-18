package com.jsv.client;
import com.jsv.data.SData;
import com.jsv.utils.Utils;


public class ClientRequest {
	
	private ClientSession mSession;
	
	public void setSession(ClientSession pSession){
		mSession = pSession;
	}
	
	public int sendModuleProtocol(String pXML){
		
		boolean bRet=false;
		SData sData = new SData();
		
		sData.add(pXML);
		bRet = mSession.send(sData); if(!bRet) return(10); //->Envia
		bRet = mSession.recv(sData); if(!bRet) return(20); //->Recebe
		Utils.log(sData.toString());
		mSession.setResponseData(sData.toString());
		
		return(200);
	}
	
	public void logoff(){
		mSession.disconnect();
	}
	
	
}
