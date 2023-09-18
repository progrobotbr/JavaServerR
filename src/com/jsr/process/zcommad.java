package com.jsr.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.jsr.server.icommands;
import com.jsr.util._;
import com.jsr.zdebug.zchannelMsg;

public class zcommad {
	
	private zprocess ps;
	private zmsg omsg=null;
	private boolean running=false;
	private zchannelMsg chn;
	private int iMode=icommands.IMODERUN;
	
	public zcommad(zchannelMsg pchn){
		chn=pchn;
	}
	
	public boolean isRunning(){
		return(running);
	}
	
	public synchronized void setRunning(boolean b){
		running = b;
	}
	
	public int execute(int piacao, String s){
		int iret=icommands.IERROR;
		int icmd=0;
		String line;
		//zmsg omsg2 = zmsg.createMsgEmpty();
		omsg = zmsg.createMsgEmpty();
		//Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		iret = icommands.ISUCCESS;
		switch(iMode){
		case icommands.IMODERUN:
			switch(piacao){
			case icommands.INEWJVM:
				
				this.ps = new zprocess();
				if(this.ps.createVM(this.chn)!=icommands.ISUCCESS){
					return(icommands.IERROR);
				}
				
				this.ps.zresume();
				line=this.ps.readInput();
				if(line!=null && line.equals("inicio")){
					this.ps.startReadAssinchronous();
					this.processReceive(this.omsg);
					_.lg("inicio:"+this.omsg.smsg);
				}else{
					this.omsg=zmsg.createMsgError("Não foi possivel iniciar processo");
					iret=icommands.IERROR;
				}
				break;
			case icommands.IDEBUGAT:
				//ps.execCmd("stop at com.jsr.server.jvmtrz:34");
				this.ps.execCmd(this.getData(s));
				this.processReceive(this.omsg);
				break;
			case icommands.IENDVM:
				this.omsg = zmsg.createMsgSuccess(icommands.SJVMSHUTDOWN);
				this.ps.sendJVM(omsg);
				this.ps.execCmd("quit");
				break;
			case icommands.IDEBUGSTEP:
				this.ps.execCmd("step");
				this.processReceive(omsg);
				break;
			case icommands.IDEBUGCONT:
				this.ps.execCmd("cont");
				this.processReceive(omsg);
				this.iMode = icommands.IMODERUN;
				this.iMode = this.checkBreakEvent(this.iMode, this.omsg);
				break;
			default:
				this.omsg = zmsg.createMsgSuccess(s);
				this.ps.sendJVM(this.omsg);
				this.processReceive(this.omsg);
				this.checkIsDebugMode(this.omsg);
				iret=icommands.ISUCCESS;
				break;
			}
			break;
		case icommands.IMODEDEBUG:
			switch(piacao){
			case icommands.IDEBUGSTEP:
				this.ps.execCmd("step");
				this.processReceive(omsg);
				this.iMode = this.checkSuspend(this.iMode, this.omsg);
				//this.checkIsDebugMode(this.omsg);
				break;
			case icommands.IDEBUGSTEPUP:
				this.ps.execCmd("step up");
				this.processReceive(omsg);
				this.iMode = this.checkSuspend(this.iMode, this.omsg);
				//this.checkIsDebugMode(this.omsg);
				break;
			case icommands.IDEBUGCONT:
				this.ps.execCmd("cont");
				this.processReceive(this.omsg);
				this.iMode = icommands.IMODERUN;
				this.iMode = this.checkBreakEvent(this.iMode, this.omsg);
				//this.checkIsDebugMode(this.omsg);
				break;
			default:
				this.omsg=zmsg.createMsgError("comando nao entendido ou nao permitido");
				iret=icommands.IERROR;
			}
			break;
		case icommands.IMODESUSP:
			this.omsg = zmsg.createMsgSuccess(s);
			this.ps.sendJVM(this.omsg);
			this.processReceive(this.omsg);
			this.checkIsDebugMode(this.omsg);
			iret=icommands.ISUCCESS;
			break;
		}
		
		return(iret);
	}
	
	public String getResponse(){
		return(omsg.smsg);
	}

	public int getAcao(String s){
		int i=0;
		String s1;
		try{
			if(s==null){
				return(icommands.IENDVM);
			}
			s1 = s.substring(0, 3);
			i = Integer.parseInt(s1);
		}catch(Exception ex){
			i = icommands.IERROR;
		}
		return(i);
	}
	
	public String getData(String s){
		return(s.substring(4));
	}
	
	public void processReceive(zmsg pmsg){
		int iret=1;
		zmsg omsg=zmsg.createMsgEmpty();
		
		iret=ps.receive(pmsg);
		while(iret>0){
			iret=ps.receive(omsg);
			pmsg.smsg += "|" +omsg.smsg;
		}
		
	}
	
	private int checkBreakEvent(int pi, zmsg pmsg){
		int i=pi;
		if(pmsg.smsg.trim().startsWith("D005")){
			i=icommands.IMODEDEBUG;
		}
		return(i);
	}
	
	private int checkSuspend(int pi, zmsg pmsg){
		int i=pi;
		if(pmsg.smsg.trim().startsWith("D010")){
			i=icommands.IMODESUSP;
		}
		return(i);
	}
	
	private int checkIsDebugMode(zmsg pmsg){
		int i=0;
		if(pmsg.smsg.trim().startsWith("D005")){
			i=this.iMode=icommands.IMODEDEBUG;
		}
		if(pmsg.smsg.trim().startsWith("D010")){
			i=this.iMode=icommands.IMODESUSP;
		}
		return(i);
	}
}
