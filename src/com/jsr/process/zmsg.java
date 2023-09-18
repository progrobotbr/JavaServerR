package com.jsr.process;

public class zmsg {

	public static int IEND=100;
	public static int IOK =200;
	public static int IERR=300;
	
	public int dir=0;
	public int rc=0;
	public String smsg="";
	
	public static zmsg createMsg(int prc, String smsg){
	   zmsg o = new zmsg();
	   o.rc=prc;
	   o.smsg=smsg;
	   return(o);
	}
	
	public void clone(zmsg pmsg){
		this.dir = pmsg.dir;
		this.rc = pmsg.rc;
		this.smsg = pmsg.smsg;
	}
	
	public static zmsg createMsgEmpty(){
		zmsg o = new zmsg();
		return(o);
	}
	
	public static zmsg createMsgError(String s){
		zmsg o = new zmsg();
		o.rc = zmsg.IERR;
		o.smsg = s;
		return(o);
	}
	
	public static zmsg createMsgSuccess(String s){
		zmsg o = new zmsg();
		o.rc = zmsg.IOK;
		o.smsg = s;
		return(o);
	}
	
	public void replaceMe(int prc, String pmsg){
		this.rc = prc;
		this.smsg = pmsg;
	}
	
}
