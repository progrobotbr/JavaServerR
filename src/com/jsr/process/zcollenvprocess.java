package com.jsr.process;

import java.util.HashMap;

public class zcollenvprocess {

	HashMap <String, zcommad>mprocess = new HashMap<String, zcommad>();
	
	public static zcollenvprocess INSTANCE=null;
	
	public static zcollenvprocess factory(){
		if(INSTANCE==null){
			INSTANCE = new zcollenvprocess();
		}
		return(INSTANCE);
	}
	public synchronized void put(String skey, zcommad penvps){
		mprocess.put(skey, penvps);
	}
	
	public zcommad get(String skey){
		zcommad envps = mprocess.get(skey);
		return(envps);
	}
}
