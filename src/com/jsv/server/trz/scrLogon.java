package com.jsv.server.trz;

import com.jsv.server.trz.base.Screen;

public class scrLogon extends Screen {
	
	public void onCreate(){};
	
	public void onProcessBeforeOutput(){
		//Lógica de programação
		int i = loadgVarI("li_numtrys");
		
		i = i * 10 * ( i + 2 );
		storegVar("li_numtrys",i);
		String s = loadgVarS("lc_usuario");
		storegVar("lc_usuario","Renato");
		log(s);
	}
	
	public void onProcessAfterInput(){};
}
