package com.jsv.lang.vm;

import java.util.HashMap;
import com.jsv.lang.vm.type.decltype;
import com.jsv.lang.vm.type.parameterPairs;
import com.jsv.data.Variant;

public class frame {
	
	public HashMap<String,Variant> mem=null;
	public HashMap<String,decltype> typ=null;
	public classProgram clsref=null;
	public String SubName="";
	public String PrgName="";
	public function f=null;
	public int programCount=0;
	public parameterPairs passIn=null, passOut=null;
	
	public frame(function pf, parameterPairs in, parameterPairs out){
		this.mem = new HashMap<String,Variant>();
		this.typ = new HashMap<String,decltype>();
		this.f=pf;
		this.SubName=f.SubName;
		this.PrgName=f.PrgName;
		this.passIn = in;
		this.passOut = out;
		this.programCount = pf.lineIdx;
		this.clsref = pf.ClsProgram;
	}
	
	public void storeVar(String pName, Variant pVar){
		mem.put(pName,pVar);
	}
	
	public Variant loadVar(String pName){
		Variant v;
		v=mem.get(pName);
		return(v);
	}
	
	public Variant loadVarField(String pName){
		Variant v;
		structure st;
		String f[] = pName.split("-");
		v=mem.get(f[0]);
		if(v==null){return(null);}
		st = v.getStucture();
		v=st.get(f[1]);
		return(v);
	}
	
	public boolean hasVariable(String pName){
		boolean b;
		b = mem.containsKey(pName);
		return(b);
	}
	
	//Tipos
    public void putType(String pName, decltype pTyp){
    	typ.put(pName, pTyp);
    }
    
    public decltype getTyp(String pName){
    	decltype t = typ.get(pName);
		return(t);
    }
    
    public boolean hasType(String pName){
		boolean b;
		b = typ.containsKey(pName);
		return(b);
	}
    
    //Controle de programa
    public void setProgramCount(int i){
    	programCount = i;
    }
}
