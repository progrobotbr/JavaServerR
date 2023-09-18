package com.jsv.nativelib;

import com.jsv.lang.program.program;
import com.jsv.lang.vm.frame;
import com.jsv.nativelib.base.INative;
import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;

public class CREATETRZ implements INative{

	public int rc;
	public String errmsg;
	
	public void setSqlBase(sqlbase pSqlBase){
		
	}
	
	
	/*
	 * IN  NAME  - char
	 * OUT MSG   - char
	 *     FRC   - int
	 */
	public void execute(frame f){
		//int i,t;
		String sTrz;
		Variant v1,r1,r2;
		program pg;
		rc=4;errmsg="";
		
		try{
			v1=f.loadVar("NAME");
			r1=f.loadVar("FRC");
			r2=f.loadVar("MSG");
			
			r1.i=rc;
			sTrz=v1.s;
			
		    if(sTrz.length()==0){
				errmsg = "Import parameters are empty";
				return;
			}
			
			pg = new program();
			
			rc=pg.createTransaction(sTrz, "");
			
			if(rc==0){
				errmsg = "Successful transaction created : "+sTrz;
			}else{
					errmsg=pg.errmsg + ": " + sTrz;
			}
			
			r1.i = rc;
			r2.s=errmsg;
			
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


