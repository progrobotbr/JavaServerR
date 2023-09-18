package com.jsv.zteste.bas;

import java.util.HashMap;

import com.jsv.data.Variant;

public class base1 extends mem {
	
	private HashMap mMemGlobal;
	private HashMap mScreen;
	private String  mScreenWork;
	
	public base1(){
		mMemGlobal = new HashMap();
		super.setMemory(mMemGlobal);
	}
	public void onCreate(){};
	
	public void onCreate(String pId){};
	
	public void onProcessBeforeOutput(){};
	
	public void onProcessAfterInput(){};
	
	public void createVar(String pNome, boolean pBol){
		Variant v1 = new Variant(pBol);
		mMemGlobal.put(pNome,v1);
	}
	
	/*
	public void storeVar(String pNome, boolean pb){	mMemGlobal.put(pNome,new Variant(pb));}
	public void storeVar(String pNome, int pi)    { mMemGlobal.put(pNome,new Variant(pi));}
	public void storeVar(String pNome, float pf)  { mMemGlobal.put(pNome,new Variant(pf));}
	public void storeVar(String pNome, String ps) { mMemGlobal.put(pNome,new Variant(ps));}
	
	
	public Variant loadVar(){ Variant v = (Variant) mMemGlobal.get(""); return(v);}
    */
}
