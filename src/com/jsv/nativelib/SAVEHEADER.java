package com.jsv.nativelib;

import java.net.URLDecoder;

import com.jsv.compiler.compiler;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.nativelib.base.INative;
import com.jsv.lang.program.program;
import com.jsv.lang.vm.frame;
import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;

public class SAVEHEADER implements INative {
	public int rc;
	public String errmsg;
	
	public void setSqlBase(sqlbase pSqlBase){
		//mSqlDb=pSqlBase;
	}
	
	
	/*
	 * IN  NAME      - char
	 *     DESC      - char
	 * OUT FRC       - int
	 *     MSG       - char
	 */
	public void execute(frame f){
		int i,t;
		String sNome, sDesc, sPrg;
		Variant v1,v2,r1,r2,vt;
		StringBuilder sb;
		structure st;
		table tb;
		program pg;
		rc=4;errmsg="";
		
		try{
			v1=f.loadVar("DESC");
			v2=f.loadVar("NAME");
			r1=f.loadVar("FRC");
			r2=f.loadVar("MSG");
			
			r1.i=rc;
			sNome = v2.s;
			sDesc = v1.s;
			
			if(sNome.length()==0){
				errmsg = "Inform object name";
				return;
			}
			
			//Implementar a lógica sa gravação do header
		    pg = new program();
			rc=pg.saveHeader(sNome, sDesc);
			rc=0;
			
			if(rc==0){
				errmsg ="Successful saved map: "+sNome;
			}else{
				errmsg=pg.errmsg+": "+sNome;
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
