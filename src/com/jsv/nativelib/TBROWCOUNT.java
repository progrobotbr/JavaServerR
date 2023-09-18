package com.jsv.nativelib;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.lang.vm.frame;
import com.jsv.lang.vm.table;
import com.jsv.nativelib.base.INative;

public class TBROWCOUNT implements INative{
	
	public int rc;
	public String errmsg;
	
	public void setSqlBase(sqlbase pSqlBase){
		//mSqlDb=pSqlBase;
	}
	
	
	public void execute(frame f){
		String s;
		Variant v1,r1;
		table tb;
		rc=4;errmsg="";
		try{
			v1=f.loadVar("TAB");
			tb=v1.getTable();
			r1=f.loadVar("SIZE");
			r1.i=tb.getRowCount();
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
