package com.jsv.zteste.bas;

import java.util.HashMap;

import com.jsv.data.Variant;

public class mem {
	
	private HashMap mMemGlobal;
	
	public void setMemory(HashMap mem) { mMemGlobal = mem; }
	public void storeVar(String pNome, boolean pb){	mMemGlobal.put(pNome,new Variant(pb));}
	public void storeVar(String pNome, int pi)    { mMemGlobal.put(pNome,new Variant(pi));}
	public void storeVar(String pNome, float pf)  { mMemGlobal.put(pNome,new Variant(pf));}
	public void storeVar(String pNome, String ps) { mMemGlobal.put(pNome,new Variant(ps));}
	

}
