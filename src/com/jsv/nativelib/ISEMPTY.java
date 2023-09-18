package com.jsv.nativelib;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.lang.vm.frame;
import com.jsv.nativelib.base.INative;

public class ISEMPTY implements INative{
	
	public int rc;
	public String errmsg;
	
	public void setSqlBase(sqlbase pSqlBase){
		//mSqlDb=pSqlBase;
	}
	
	
	public void execute(frame f){
		Variant v1,v2;
		rc=4;errmsg="";
		try{
			v1=f.loadVar("VARIAVEL");
			v2=f.loadVar("FRC"); //Função RC
			v2.b = v1.isEmpty();
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
