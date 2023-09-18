package com.jsr.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.jsr.process.zmsg;
import com.jsr.server.icommands;
import com.jsr.zdebug.zchannelMsg;

public class streamop extends Thread{

	private OutputStreamWriter osr=null;
	private BufferedReader     br=null;
	StringBuffer               sb;
	zchannelMsg                chn;
	public String              Nome="";
	public int                 Origem=0;
	InputStream   ips;
	
	public streamop(int i){
		Origem = i;
	}
	
	public void putStream(zchannelMsg pch, InputStream pis, OutputStream pos){
		
		chn = pch;
		osr = new OutputStreamWriter(pos);
        ips = pis;
        InputStreamReader isr = new InputStreamReader(pis);
        br = new BufferedReader(isr);
        
	}
	
	public int send(String s){
		int i;
		try{
		  osr.write(s);
          osr.flush();
          i=icommands.ISUCCESS;
		}catch(Exception ex){
		  i=icommands.IERROR;
		}
		return(i);
	}
	
	
	public void startAssincronous(){
		this.setName("process_ler");
		this.start();
	}
	
	public void run(){
		zmsg omsg;
		String sline = "";
		try{
			while(!sline.equals("END")){
				sline = this.receiveSincronous();
				omsg = zmsg.createMsgSuccess(sline);omsg.dir=Origem;
				chn.putB(omsg);
			}
		}catch(Exception ex){
			omsg = zmsg.createMsgError(ex.toString());
			chn.putB(omsg);
		}
	}
	
	
	public String receiveSincronous() throws Exception{
		int irec=0;
		byte buf[] = new byte[512];
		String line="";
		sb = new StringBuffer();
		while(irec>-1){
			irec=ips.read(buf);
			if(irec>0){
				sb.append(new String(buf,0,irec));
			}
			if(ips.available()==0){
			   irec = -1;
			}
		}
		
		//line = br.readLine();
		//sb.append(line);
		//_.lg("cmd "+Nome+" "+sb.toString());
		return(sb.toString());
		
	}
	
	
}
