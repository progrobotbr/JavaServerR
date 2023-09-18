package com.jsv.zteste.bas;

public class neta extends filha {

	public void onCreate(){
		super.onCreate("USRL");
		this.storeVar("isren",true);
		this.storeVar("lc_usuario","");
		this.storeVar("lc_senha","");
	}
	
	public static void main(String[] argv){
		neta on = new neta();
		on.onCreate();
		
	}
}
