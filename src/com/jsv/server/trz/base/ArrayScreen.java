package com.jsv.server.trz.base;

import java.util.HashMap;
import java.util.Vector;

public class ArrayScreen {
	
	private HashMap<String,HashMap<String,String>> screens = new HashMap<String,HashMap<String,String>>();
	
	public void put(String screen, String containerName, String subscreen){
		HashMap<String,String> subscreens=null;
		if(screens.containsKey(screen)){
			subscreens = screens.get(screen);
			subscreens.put(containerName, subscreen);
		}else{
			subscreens = new HashMap<String,String>();
			subscreens.put(containerName, subscreen);
			screens.put(screen, subscreens);
		}
		
	}
	
	public HashMap<String, String> getSubScreen(String screen){
		if(screens.containsKey(screen)){
			return(screens.get(screen));
		}else{
			return(null);
		}
	}
	

}
