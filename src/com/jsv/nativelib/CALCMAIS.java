package com.jsv.nativelib;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.lang.vm.frame;
import com.jsv.nativelib.base.INative;

public class CALCMAIS implements INative{

	public int rc;
	public String errmsg;
	
	public void setSqlBase(sqlbase pSqlBase){
		
	}
	
	public void execute(frame f){
		float fl;
		Variant v1,v2,r1,vt;
		rc=4;errmsg="";
		try{
			v1=f.loadVar("VALOR1");
			v2=f.loadVar("VALOR2");
			r1=f.loadVar("RESULTADO");
			fl=v1.getNum()+v2.getNum();
			if(r1.type==Variant.INT){
				r1.i=(int)fl;
			}else{
				r1.f=fl;
			}
			//f.storeVar("RESULTADO", r1);
			rc=0;
		}catch(Exception ex){
			errmsg="INative:"+ex.toString();
		}
	}
	
	public int getRc(){
		return(rc);
	}
	
	public String getErrmsg(){
		return(errmsg);
	}
}

