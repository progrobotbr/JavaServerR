package com.jsv.nativelib;

import java.net.URLDecoder;

import com.jsv.compiler.compiler;

import com.jsv.lang.program.program;
import com.jsv.lang.vm.frame;
import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;
import com.jsv.nativelib.base.INative;
import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;

public class COMPILE implements INative{

	public int rc;
	public String errmsg;
	public sqlbase mSqlDb;
	
	public void setSqlBase(sqlbase pSqlBase){
		mSqlDb=pSqlBase;
	}
	
	/*
	 * IN  TBSOURCE  - tabela
	 *     PRGNOME   - string
	 * OUT FRC       - int
	 */
	public void execute(frame f){
		int i,t;
		String sNome, sPrg;
		Variant v1,v2,r1,r2,vt;
		StringBuilder sb;
		structure st;
		table tb;
		program pg;
		compiler cp;
		rc=4;errmsg="";
		
		try{
			v1=f.loadVar("TB");
			v2=f.loadVar("NAME");
			r1=f.loadVar("FRC");
			r2=f.loadVar("MSG");
			
			r1.i=rc;
			sNome = v2.s;
			sPrg="";
			sb=new StringBuilder();
		    
			tb = v1.tb;
		    t=tb.getColumnCount();
		    tb.setIndex(0);
		    st=tb.foreach();
		    while(st!=null){
		    	vt=st.get("LINTX");
		    	sb.append(vt.s);
		    	st=tb.foreach();
		    }
            sPrg=sb.toString();
            
		    if(sPrg.length()==0 || sNome.length()==0){
				errmsg = "Import parameters are empty";
				return;
			}
			
		    //sPrg=URLDecoder.decode(sPrg,"ISO-8859-1");
		    
			pg = new program();
			
			rc=pg.saveProgramDB(mSqlDb, sNome,compiler.PRGS, "", sPrg);
			
			errmsg=pg.errmsg+": "+sNome;
			
			if(rc==0){
				cp = new compiler();
				cp.compile(mSqlDb, sPrg,sNome,"");
				rc=cp.rc;
				if(rc==0){
					errmsg = "Successful compiled program: "+sNome;
				}else{
					errmsg=cp.errmsg + ": " + sNome;
				}
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


