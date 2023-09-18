package com.jsv.server.trz.base;

public class ren extends AScreenBaseJava{
	
	public void onCreate(ATransactionBaseJava ctx){
		
	}
	
	public void onPAI(ATransactionBaseJava ctx){
		this.setNextScreen("qq");
	}

	public void onPBO(ATransactionBaseJava ctx){
		
	}


}
