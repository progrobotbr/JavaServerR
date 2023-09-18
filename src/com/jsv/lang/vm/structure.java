package com.jsv.lang.vm;

import java.util.HashMap;
import java.util.Vector;

import com.jsv.data.Variant;

public class structure {
	
	public String Name=""; //Nome da Estrutura
	public int tam=0;
	private HashMap<String,Variant> col=new HashMap<String,Variant>();
	private Vector<String> ncol = new Vector<String>();
	
	public void add(String s, Variant v){ col.put(s, v); ncol.add(s); }
	public Variant get(String s) { return( col.get(s));}
	public Variant getWI(int i) { return(col.get(ncol.get(i)));} //GetWithIndex
	
	public structure(String s){
		Name = s;	
	}
	
	public int getSize(){
		return(ncol.size());
	}
	
	public void clear(){
		Variant v;
		for(int i=0; i<ncol.size();i++){
			v = col.get(ncol.get(i));
			v.clear();
		}
	}
	
	public Vector<String> getNameCols(){
		return(ncol);
	}
	
	public structure clone(){
		int i;
		Variant v=null,r=null;
		structure str=null;
		str = new structure(new String(this.Name));
		for(i=0;i<ncol.size();i++){
			v = col.get(ncol.get(i));
			r = v.clone();
			str.add(new String(ncol.get(i)), r);
		}
		return(str);
	}
	
}
