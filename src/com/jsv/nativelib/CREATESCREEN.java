package com.jsv.nativelib;

import com.jsv.lang.program.program;
import com.jsv.lang.vm.frame;
import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.nativelib.base.INative;

public class CREATESCREEN implements INative {

		public int rc;
		public String errmsg;
		private sqlbase mSqlDb;
		
		public void setSqlBase(sqlbase pSqlBase){
			mSqlDb=pSqlBase;
		}
		
		/*
		 * IN  TRZ  - char
		 *     SCREEN
		 *     DESCRIPTION
		 * OUT MSG   - char
		 *     FRC   - int
		 */
		public void execute(frame f){
			//int i,t;
			String sTrz, sScreen, sDesc;
			Variant v1,v2,v3,r1,r2;
			program pg;
			rc=4;errmsg="";
			
			try{
				v1=f.loadVar("TRZ");
				v2=f.loadVar("SCR");
				v3=f.loadVar("DESC");
				r1=f.loadVar("FRC");
				r2=f.loadVar("MSG");
				
				r1.i=rc;
				sTrz=v1.s;
				sScreen=v2.s;
				sDesc=v3.s;
				
			    if(sTrz.length()==0 || sScreen.length()==0){
					errmsg = "Import parameters are empty";
					return;
				}
				
				pg = new program();
				
				rc=pg.createScreen(mSqlDb, sTrz, sScreen, sDesc);
				
				if(rc==0){
					errmsg = "Successful screen created : "+sScreen;
				}else{
						errmsg=pg.errmsg + ": " + sScreen;
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

