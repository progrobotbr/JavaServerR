package com.jsr.client;

import java.net.Socket;
import java.util.UUID;

import com.jsr.process.zcollenvprocess;
import com.jsr.process.zcommad;
import com.jsr.process.zmsg;
import com.jsr.server.icommands;
import com.jsr.util._;
import com.jsr.util.streamop;
import com.jsr.zdebug.zchannelMsg;

public class serverclient extends Thread implements Runnable {
	
	public Socket      sc;
	public streamop    streamClient;
	public zcommad     oEnvProcess;
	public String      sessionId;
	public zchannelMsg chn;
	
	public int init(Socket psc){
		int i;
		sc           = psc;
		sessionId    = UUID.randomUUID().toString();
		chn          = new zchannelMsg();
		streamClient = new streamop(icommands.IORIGCLT);
		streamClient.Nome = "strclient";
		try{
			streamClient.putStream(chn, psc.getInputStream(), psc.getOutputStream());
			oEnvProcess = new zcommad(chn);
			i=oEnvProcess.execute(icommands.INEWJVM, icommands.SNEWJVM);
			if(i==icommands.IERROR){
				return(i);
			}
			zcollenvprocess.factory().put(sessionId, oEnvProcess);
			i=icommands.ISUCCESS;
		}catch(Exception ex){
			i=icommands.IERROR;
			_.lg(ex.toString());
		}
		return(i);
	}
	
	public void run(){
		boolean b=true;
		int i, icmd, istatus;
		String sreq="";
		String sres="";
		zmsg omsg;
		
		streamClient.send("Handshake: id "+sessionId);
		
		if(checkHello()!=icommands.ISUCCESS){
    		_.lg("err505:comando não entendido");
			return;
    	}else{
    		streamClient.send("suc200:bemvindo");
    	}
		
		try{
		
			while(b){
			
				sreq = this.streamClient.receiveSincronous();
				this.oEnvProcess.setRunning(true);
				icmd = this.oEnvProcess.getAcao(sreq);
				i    = this.oEnvProcess.execute(icmd, sreq);
				sres = this.oEnvProcess.getResponse();
				_.lg("ret: "+sres);
				istatus  = this.streamClient.send(sres);
				this.oEnvProcess.setRunning(false);
			
			}
		}catch(Exception ex){
			i = this.oEnvProcess.execute(230, "230");
			_.lg(ex.toString());
		}
		
	}
	
	public int checkHello(){
		String sResp="";
		try{
		sResp = streamClient.receiveSincronous();
		if(sResp==null || !sResp.equals("cliente id " + sessionId)){
			return icommands.IERROR;
		}
		}catch(Exception ex){
			return icommands.IERROR;
		}
		return icommands.ISUCCESS;
	}

}
