package com.jsv.nativelib;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.lang.vm.frame;
import com.jsv.nativelib.base.INative;

public class CONCAT implements INative{
	
	public int rc;
	public String errmsg;
	
	public void setSqlBase(sqlbase pSqlBase){
		//mSqlDb=pSqlBase;
	}
	
	
	public void execute(frame f){
		Variant v1,v2,v3;
		String s="";
		rc=4;errmsg="";
		try{
			v1=f.loadVar("VAR1");
			v2=f.loadVar("VAR2");
			v3=f.loadVar("RES");
			if(v3.type == Variant.STRING){
			  v3.s=v1.toString()+v2.toString();
			  rc=0;
			}else{
			  errmsg="INative:Tipo de destino deve ser String";	
			}
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
