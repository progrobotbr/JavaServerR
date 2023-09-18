package com.jsv.lang.vm.type;

import com.jsv.data.Variant;

public class decltypeelement {
	/*
		Variant.BOOL
		Variant.INT
		Variant.FLOAT
		Variant.STRING
		Variant.STRUCTURE
		Variant.TABLE 
	*/
	public decltypeelement(){
		
	}
	public decltypeelement(String pNome, String pTpOriginal, int pType){
		iType = pType;
		sNome = pNome;
		sTpOriginal = pTpOriginal;
	}
	public int iType = 0;
	public String sNome = "";
	public String sTpOriginal="";

}
