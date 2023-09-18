package com.jsv.lang.vm.type;

import java.util.Vector;

import com.jsv.data.Variant;

import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;

public class decltype {

	private int idx=0;
	public String Name;
	private Vector<decltypeelement> vCampos;
	
	public decltype(String s){
		vCampos = new Vector<decltypeelement>();
		Name = s;
	}
	
	public decltype(){
		vCampos = new Vector<decltypeelement>();
		Name = "";
	}
	
	public void addElement(int pType, String sNome){
		decltypeelement el = new decltypeelement();
		el.iType = pType;
		el.sNome = sNome;
		vCampos.add(el);
	}
	
	public void addElement(decltypeelement pel){
		vCampos.add(pel);
	}
	
	public Variant generateVar(){
		int i;
		decltypeelement el;
		Variant v,r=null;
		structure st=new structure(Name);
		for(i=0;i<vCampos.size();i++){
			el = vCampos.get(i);
			switch(el.iType){
			case Variant.BOOL:
				v = new Variant(false);
				st.add(el.sNome, v);
				break;
			case Variant.INT :
				v = new Variant(0);
				st.add(el.sNome, v);
				break;
			case Variant.FLOAT :
				v = new Variant((float)0);
				st.add(el.sNome, v);
				break;
			case Variant.STRING :
				v = new Variant("");
				st.add(el.sNome, v);
				break;
			}
		}
		r = new Variant(st);
		return(r);
	}
	
	public void setIndex(int i){
		idx=i;
	}
	
	public decltypeelement getElem(){
		decltypeelement el=null;
		if(idx<vCampos.size()){
		  el = vCampos.get(idx);
		  idx++;
		}
		return(el);
	}


}
