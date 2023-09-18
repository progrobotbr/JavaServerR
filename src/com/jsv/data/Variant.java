package com.jsv.data;

import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;

public class Variant {
	
	public static final int BOOL      = 1;
	public static final int INT       = 2;
	public static final int FLOAT     = 3;
	public static final int STRING    = 4;
	public static final int STRUCTURE = 5;
	public static final int TABLE     = 6;
	public static final int FIELD     = 7; //Não utilizado aqui, mas em outros lugares
	public static final int NAME      = 8 ; //Não utilizado aqui, mas em outros lugares
	public static final int DOUBLE    = 9;
	public static final int OBJECT    = 10;
	
	public  int type=0;
	
	public boolean b;
	public int i;
	public float f;
	public double d;
	public String s=null;
	public structure st;
    public table tb;
    public Object ob;
    
    private boolean bnull = true;
    private byte t;
       
    private int stringTam=0;
    
    public Variant() {}
    public Variant(boolean pb)    { b = pb; type=BOOL;  bnull=false; }
    public Variant(int pi)        { i = pi; type=INT;   bnull=false; }
    public Variant(float pf)      { f = pf; type=FLOAT; bnull=false; }
    public Variant(double pd)     { d = pd; type=DOUBLE; bnull=false; }
    public Variant(String ps)     { s = ps; type=STRING;bnull=false; }
    public Variant(structure pst) { st = pst; type=STRUCTURE;bnull=false; }
    public Variant(table ptb)     { tb = ptb; type=TABLE;bnull=false; }
    public Variant(Object pob)    { ob = pob; type=OBJECT;bnull=false; }
    
    public void setStringSize(int pTam){ stringTam = pTam; } 
    
    public boolean getBoolean()   { return(b); }
    public int getInt()           { return(i); }
    public float getFloat()       { return(f); }
    public double getDouble()     { return(d); }
    public String getString()     { return(s); }
    public structure getStucture(){ return(st); }
    public table getTable()       { return(tb); }
    public Object getObject()     { return(ob); }
    
    public boolean isNull()       { return(bnull);}
    
    public float getNum(){
    	float lf;
    	switch(type){
    	case INT: lf=i; return(lf);
    	case FLOAT: return(f);
    	}
    	return(0);
    }
    
    public void setNum(Variant v){
    	switch(v.type){
    	case BOOL:  this.b = v.getBoolean(); break;
    	case INT:  
    		switch(type){
    			case INT: this.i = v.getInt(); break;
    			case FLOAT: this.f = v.getInt(); break;
    			case DOUBLE: this.d = v.getInt(); break;
    			case STRING: this.s = "" + v.getInt(); break;
    		}
    		break;
    	case FLOAT: 
    		switch(type){
				case INT: this.i = (int) v.getFloat(); break;
				case FLOAT: this.f = v.getFloat(); break;
				case DOUBLE: this.d = v.getFloat(); break;
				case STRING: this.s = "" + v.getFloat(); break;
    		}
    		break;
    	case DOUBLE: 
    		switch(type){
				case INT: this.i = (int) v.getDouble(); break;
				case FLOAT: this.f = (float) v.getDouble(); break;
				case DOUBLE: this.d = v.getDouble(); break;
				case STRING: this.s = "" + v.getDouble(); break;
    		}
    		break;
    	case STRING: { this.s = v.getString(); break; }
    	case STRUCTURE: { this.st = v.getStucture(); break; }
    	}
    }

    public void setStru(structure pst){
    	this.st = pst;
    }
    
    public void clear(){
    	switch(type){
    	case BOOL: b = false; return;
    	case INT: i = 0; return;
    	case FLOAT: f=0; return;
    	case STRING: s=""; return;
    	case STRUCTURE: st.clear();return;
    	case TABLE: tb=null;
    	}
    }
    
    public Variant clone(){
    	Variant v=null;
    	switch(type){
    		case BOOL: v=new Variant(b); break;
    		case INT:  v=new Variant(i); break;
    		case FLOAT:  v=new Variant(f); break;
    		case STRING:  v=new Variant(s); break;
    		case STRUCTURE: v=new Variant(st); break;
    		case TABLE: v=new Variant(tb); break;
    	}
		return(v);
    }
    
    public String toString(){
    	String string=null;
    	switch(type){
    		case BOOL: s=""+b;break;
    		case INT:  s=""+i; break;
    		case FLOAT:  s=""+f; break;
    		case STRING:  s=""+s; break;
    		case STRUCTURE: s="<EstruturaLinha>"+st.Name; break;
    		case TABLE: s="<Tabela>"+tb.Name; break;
    	}
		return(s);
    }

    public boolean isEmpty(){
    	boolean bf=false;
    	byte bb[];
    	int ii,zz;
    	switch(type){
			case BOOL:   if(b==false) bf=true;break;
			case INT:    if(i==0) bf=true; break;
			case FLOAT:  if(f==0) bf=true; break;
			case STRING: bf=true;
				 		 if(s!=null && s.length()>0){
			 			 	zz=s.length();
			 			 	bb=s.getBytes();
			 			 	for(ii=0; ii<zz;i++){
			 			 		if(bb[ii]!=32){
			 			 			return(false);
			 			 		}
			 			 	}
						 }
						 break;
			//case STRUCTURE: s="<EstruturaLinha>"+st.Name; break; não implemntado
			case TABLE:  if(!(tb!=null && tb.getRowCount()>0)) bf=true; break;
			case OBJECT: if(ob==null) bf=true; break;
    	}
    	return(bf);	
    }
    
    public void moveStringToInternal(String ps){
    	if(ps.length()==0){return;}
    	switch(type){
		//case BOOL : if(b==false) bf=true;break;
		case INT  : i = Integer.parseInt(ps); break;
		case FLOAT: f = Float.parseFloat(ps); break;
		case STRING: s = ps; break;
    	}
    }
}
