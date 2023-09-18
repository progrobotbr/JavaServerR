package com.jsv.nativelib;

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.jsv.compiler.compiler;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.lang.program.program;
import com.jsv.lang.vm.frame;
import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;
import com.jsv.nativelib.base.INative;

public class SAVEPROTOCOL implements INative{
	public int rc;
	public String errmsg;
	public sqlbase mSqlDb;
	
	public void setSqlBase(sqlbase pSqlBase){
		mSqlDb=pSqlBase;
	}
	
	
	/*
	 * IN  TBMAP     - tabela
	 *     PRGNOME   - string
	 * OUT FRC       - int
	 *     MSG       - char
	 */
	public void execute(frame f){
		int i,t;
		String sNome, sDesc, sP1, sP2;
		Variant v1,v2,v3,r1,r2,vt;
		StringBuilder sb;
		structure st;
		table tb;
		program pg;
		rc=4;errmsg="";
		
		try{
			v1=f.loadVar("TBP1");
			v2=f.loadVar("TBP2");
			v3=f.loadVar("NAME");
			r1=f.loadVar("FRC");
			r2=f.loadVar("MSG");
			
			r1.i=rc;
			sNome = v3.s;
			
			/*
			 * Grava se for diferente de TRZ. 
			 *   Pois o tipo TRZ não possui tela 
			 */
			if(sNome!=null && sNome.startsWith("TRZ")){
				rc=0;
				r1.i=0;
				return;
			}
			
			sb=new StringBuilder();
		    
			sDesc="";
			
			tb = v1.tb;
		    t=tb.getColumnCount();
		    tb.setIndex(0);
		    st=tb.foreach();
		    while(st!=null){
		    	vt=st.get("LINTX");
		    	sb.append(vt.s);
		    	st=tb.foreach();
		    }
		    
            sP1=sb.toString();
            
            sb=new StringBuilder();
            tb = v2.tb;
		    t=tb.getColumnCount();
		    tb.setIndex(0);
		    st=tb.foreach();
		    while(st!=null){
		    	vt=st.get("LINTX");
		    	sb.append(vt.s);
		    	st=tb.foreach();
		    }
            sP2=sb.toString();
            
            if(sP1.length()==0 && sP2.length()==0){
            	rc=0;
            	r1.i=rc;
            	return;
            }
            
		    if(sP1.length()==0 || sP2.length()==0 || sNome.length()==0){
				errmsg = "Import parameters are empty";
				return;
			}
			
		    sP1 = URLEncoder.encode(sP1, "iso-8859-1");
		    pg = new program();
			rc=pg.saveProgramDB(mSqlDb, sNome,"PCL1", sDesc, sP1);
			if(rc==0){
				sP2 = URLEncoder.encode(sP2, "iso-8859-1");
				rc=pg.saveProgramDB(mSqlDb, sNome,"PCL2", sDesc, sP2);
				if(rc==0){
					errmsg ="Successful saved protocol: "+sNome;
				}else{
					errmsg="Failed to save protocol p2";
				}
			}else{
				errmsg="Failed to save protocol p1";
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
