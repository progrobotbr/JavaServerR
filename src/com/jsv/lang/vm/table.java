package com.jsv.lang.vm;

import java.util.Vector;

import com.jsv.lang.vm.type.parameterPair;
import com.jsv.lang.vm.type.parameterPairs;

import com.jsv.data.Variant;

public class table {

	public String Name; //Nome da tabela
	private Vector<structure> lin;
	private structure col;
	private int tam=0;
	private int coltam=0;
	private int index=0;
	private int page=0;
	
	public int getPage(){
		return(page);
	}
	public void setPage(int i){
		page = i;
	}
	
	public boolean isEmpty(){
		if(lin==null || lin.isEmpty()){
			return(true);
		}else{
			return(false);
		}
	}
	
	public table(structure s){
		col = s;
		lin = new Vector<structure>();
	}
	
	public void setHeader(structure s){
		col = s;
	}
	
	public structure getHeader(){
		return(col);
	}
	
	public void setIndex(int i){
		index = i;
	}
	public structure foreach(){
		int i;
		structure s=null;
		i = index;
		if(i<tam){
			s=lin.get(index++);
		}
		return(s);
	}
	
	public void deleteIndice(int indice){
		lin.remove(indice);
	}
	
	public int getIndice(){
		return(index);
	}
	
	public void clearTable(){
		lin = new Vector<structure>();
		tam = 0;
		index = 0;
		this.clearHeader();
	}
	
	public int getRowCount(){
		return(tam);
	}
	
	public int getColumnCount(){
		return(col.getSize());
	}
	
	public void clearHeader(){
		col.clear();
	}
	
	public void append( structure st, vm vmc ){
		structure s1 = null;
		s1 = st.clone();
		lin.add(s1);
		tam++;
		//System.out.println("Dentro: "+this.toString());
	}
	
	public structure read2(parameterPairs pKeys, vm vmc){
		boolean b=false;
		int i,t;
		Variant v1,v2,r1;
		structure st=null;
		parameterPair pKey;
		t=lin.size();
		for(i=0;i<t;i++){
			st = lin.get(i);
			pKeys.setIndex(0);
			pKey = pKeys.getElem();
			while(pKey!=null){
				v1=st.get(pKey.spar1);
				v2=pKey.vpar2;
				r1 = vmc.bool('=', v1, v2);
				b = r1.getBoolean();
			    if(b==false){
			    	break;
			    }
			    pKey = pKeys.getElem();
			}
			if(b==true){
				return(st);
			}
		}
		//vmc.setRC(vm.PRG_PROCESS_ERROR);
		return(null);
	}

	public Variant read(parameterPairs pKeys, vm vmc){
		boolean b=false;
		int i,t;
		Variant v1,v2,r1,r2=null;
		structure st;
		parameterPair pKey;
		t=lin.size();
		for(i=0;i<t;i++){
			st = lin.get(i);
			pKeys.setIndex(0);
			pKey = pKeys.getElem();
			while(pKey!=null){
				v1=st.get(pKey.spar1);
				v2=pKey.vpar2;
				r1 = vmc.bool('=', v1, v2);
				b = r1.getBoolean();
			    if(b==false){
			    	break;
			    }
			    pKey = pKeys.getElem();
			}
			if(b==true){
				r2 = new Variant(st.clone());
				//vmc.setRC(vm.PRG_LINE_SUCCESS);
				return(r2);
			}
		}
		//vmc.setRC(vm.PRG_PROCESS_ERROR);
		return(r2);
	}
	public boolean delete(parameterPairs pKeys, vm vmc){
		boolean b=false;
		int i,t;
		Variant v1,v2,r1;
		structure st;
		parameterPair pKey;
		t=lin.size();
		for(i=0;i<t;i++){
			st = lin.get(i);
			pKeys.setIndex(0);
			pKey = pKeys.getElem();
			while(pKey!=null){
				v1=st.get(pKey.spar1);
				v2=pKey.vpar2;
				r1 = vmc.bool('=', v1, v2);
				b = r1.getBoolean();
			    if(b==false){
			    	break;
			    }
			    pKey = pKeys.getElem();
			}
			if(b==true){
				lin.remove(i);
				tam--;
				//vmc.setRC(vm.PRG_LINE_SUCCESS);
				return(true);
			}
		}
		//vmc.setRC(vm.PRG_PROCESS_ERROR);
		return(false);
	}
	
	public String toString(){
		int i,t;
		String s="",nm;
		Variant v;
		structure st = this.getHeader();
		Vector oh = st.getNameCols();
		t = oh.size();
		
		try{
		s="TABELA :" +this.Name;
		for(i=0;i<t;i++){
			nm = (String) oh.get(i);
			s+=nm+" ";
		}
		s+="\n";
		this.setIndex(0);
		st=this.foreach();
		while(st!=null){
			for(i=0;i<t;i++){
				v = st.getWI(i);
				s+=v.toString()+"|";
			}
			s+="\n";
			st=this.foreach();
		}
		}catch(Exception e){}
		return(s);
	}
	
	public static boolean staticIsEmpty(table tb){
		if(tb==null || tb.isEmpty()){
			return(true);
		}
		return(false);
	}
}
