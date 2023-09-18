package com.jsv.server.trz.base;

import java.util.HashMap;
import java.util.Vector;

import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;
import com.jsv.data.Variant;

public class MemoryOp {
	
	/** Constants **/
	public static final String SNIL=""; // String vazia. Algo parecida como "String Nula"
	
	/** Área de Memória **/
	private HashMap <String, Variant>mMemGlobal; //Área da Memoria da Transação
	private HashMap <String, Variant>mMemLocal;
	private ArrayScreen mScreens; //Utilizado para subscreen
	
	
	public void initMemGlobal(){
		mMemGlobal=new HashMap<String, Variant>();
		mScreens=new ArrayScreen();
	}
	
	public HashMap<String,String>getSubScreens(String screen){
		return(mScreens.getSubScreen(screen));
	}
	public void putSubScreen(String screen, String containerName, String subscreen){
		this.storegVar(containerName, subscreen);
		mScreens.put(screen, containerName, subscreen);
	}
	
	public void setGlobalMemory(HashMap <String, Variant>pMem){ mMemGlobal = pMem;	}
	public void setLocalMemory(HashMap <String, Variant>pMem){ mMemLocal = pMem;	}
	
	public HashMap <String, Variant> getGlobalMemory(){ return(mMemGlobal); }
	
	public void storegVar(String pNome, boolean pb)    { mMemGlobal.put(pNome,new Variant(pb));}
	public void storegVar(String pNome, int pi)        { mMemGlobal.put(pNome,new Variant(pi));}
	public void storegVar(String pNome, float pf)      { mMemGlobal.put(pNome,new Variant(pf));}
	public void storegVar(String pNome, double pd)     { mMemGlobal.put(pNome,new Variant(pd));}
	public void storegVar(String pNome, String ps)     { mMemGlobal.put(pNome,new Variant(ps));}
	public void storegVar(String pNome, table pt)      { mMemGlobal.put(pNome,new Variant(pt));}
	public void storegVar(String pNome, structure st)  { mMemGlobal.put(pNome,new Variant(st));}
	public void storegVar(String pNome, Object ob)     { mMemGlobal.put(pNome,new Variant(ob));}
	
	public boolean   loadgVarB(String s){ Variant v = (Variant) mMemGlobal.get(s); return(v.getBoolean());}
	public int       loadgVarI(String s){ Variant v = (Variant) mMemGlobal.get(s); return(v.getInt());}
	public float     loadgVarF(String s){ Variant v = (Variant) mMemGlobal.get(s); return(v.getFloat());}
	public String    loadgVarS(String s){ Variant v = (Variant) mMemGlobal.get(s); return(v.getString());}
	public table     loadgVarT(String s){ Variant v = (Variant) mMemGlobal.get(s); return(v.getTable());}
	public structure loadgVarE(String s){ Variant v = (Variant) mMemGlobal.get(s); return(v.getStucture());}
	public Object    loadgVarO(String s){ Variant v = (Variant) mMemGlobal.get(s); return(v.getObject());}
	
	public String loadGS(String s){
		String sRet="";
		if(!mMemGlobal.containsKey(s)){
			return(sRet);
		}
		Variant v = (Variant) mMemGlobal.get(s);
		if(v.type == Variant.INT)
			sRet = ""+v.getInt();
		else if(v.type == Variant.STRING)
			sRet = v.getString();
		else if(v.type == Variant.BOOL)
			sRet = ""+v.getBoolean();
		else if(v.type == Variant.FLOAT)
			sRet = ""+v.getFloat();
		return(sRet);
	}
	
	public Variant loadOB(String s){
		String sRet="";
		if(!mMemGlobal.containsKey(s)){
			return(null);
		}
		Variant v = (Variant) mMemGlobal.get(s);
		return(v);
	}
	
	public void storelVar(String pNome, boolean pb){ mMemLocal.put(pNome,new Variant(pb));}
	public void storelVar(String pNome, int pi)    { mMemLocal.put(pNome,new Variant(pi));}
	public void storelVar(String pNome, float pf)  { mMemLocal.put(pNome,new Variant(pf));}
	public void storelVar(String pNome, String ps) { mMemLocal.put(pNome,new Variant(ps));}
	public void storelVar(String pNome, Object ob) { mMemLocal.put(pNome,new Variant(ob));}
	
	public boolean loadlVarB(String s){ Variant v = (Variant) mMemLocal.get(s); return(v.getBoolean());}
	public int     loadlVarI(String s){ Variant v = (Variant) mMemLocal.get(s); return(v.getInt());}
	public float   loadlVarF(String s){ Variant v = (Variant) mMemLocal.get(s); return(v.getFloat());}
	public String  loadlVarS(String s){ Variant v = (Variant) mMemLocal.get(s); return(v.getString());}
	public Object  loadlVarO(String s){ Variant v = (Variant) mMemLocal.get(s); return(v.getObject());}
	
	//operandos
	public boolean eqs(String s1, String s2){
		return(s1!=null && s1.equals(s2));
	}
	public boolean isNull(String s){
		if(s==null || s.equals("")){
			return(true);
		}
		return(false);
	}
	
}
