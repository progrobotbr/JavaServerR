package com.jsv.lang.vm.type;

import java.util.HashMap;
import java.util.Vector;

public class parameterPairs {
	
	private int idx=0;
	
	HashMap<String,parameterPair> melems = new HashMap<String,parameterPair>();
	Vector<parameterPair> elems = new Vector<parameterPair>();
	public void add(parameterPair pElem){
		elems.add(pElem);
		melems.put(pElem.spar1, pElem);
	}
	
	public void setIndex(int i){
		idx = i;
	}
	public parameterPair getElem(){
		if(idx<elems.size())
			return(elems.get(idx++));
		else
			return(null);
	}
	
	public parameterPair getElem(String s){
		if(melems.containsKey(s)){
			return(melems.get(s));
		}
		return(null);
	}

}
