package com.jsv.java.compile;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.jsr.util._;
import com.jsv.server.config.srvBeanConfig;

public class compile {
	
	public static String CTRZ = "trz";
	public static String CSCN = "scn";
	
	public static boolean base(String sFile, StringBuffer psb){
		
		String ss="";
		ByteArrayOutputStream ous = new ByteArrayOutputStream();
		if(psb==null) psb=new StringBuffer();
		try{
			
			//ss = srvBeanConfig.pathPrgSource+sFile+srvBeanConfig.extPrgSource;
			ss=sFile;
			System.setProperty("java.home", srvBeanConfig.javaHome);
			System.setProperty("java.class.path", "c:\\users\\renato\\workspace\\javaserverr\\javaserverr.jar");
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			compiler.run(null, null, ous, ss);
			if(ous.toString().length()>0){
				psb.append(ous.toString());
				_.lg(ous.toString());
				return(false);
			}
			psb.append("");
			return(true);
			
		}catch(Exception ex){
			_.lg(ex.toString());
			psb.append(ous.toString());
			return(true);
		}
	}
	
	public static boolean moveFile(String pOrigem, String pDestino){
		String sDest;
		File fileOrig = new File(pOrigem);
		sDest = pDestino + fileOrig.getName();
		
		File fileDest = new File(sDest);
		fileDest.delete();  //Deleta arquivo destino, se houver algum
		if(!fileOrig.renameTo(new File(pDestino + fileOrig.getName()))){
			fileOrig.delete(); //falhou, deleta 
			return(false);
		}
		return(true);
	}
	
	public static boolean compObj(StringBuffer psb, String pTpObj, String pTrz, String pObj){
		boolean b;
		//StringBuffer sb = new StringBuffer();
		String s = srvBeanConfig.pathRep + "trz\\" + pTrz + "\\source\\" + pTpObj + pObj + pTrz + srvBeanConfig.extPrgSource;
		b=compile.base(s, psb);
		if(b){
			String s1 = srvBeanConfig.pathRep + "trz\\" + pTrz + "\\source\\" + pTpObj + pObj + pTrz + srvBeanConfig.extPrgCompiled;
			String s2 = srvBeanConfig.pathRep + "trz\\" + pTrz + "\\bin\\";
			b=compile.moveFile(s1, s2);
			if(!b){
				psb.append("Arquivo compilado, mas não foi mover o arquivo");
			}
		}
		return(b);
		
	}
	
	public static boolean compObj2(StringBuffer psb, String pTrz, String pPrg){
		boolean b;
		String s = srvBeanConfig.pathRep + "trz\\" + pTrz + "\\source\\" + pPrg + srvBeanConfig.extPrgSource;
		//StringBuffer sb = new StringBuffer();
		
		b=compile.base(s, psb);
		if(b){
			String s1 = srvBeanConfig.pathRep + "trz\\" + pTrz + "\\source\\" + pPrg + srvBeanConfig.extPrgCompiled;
			String s2 = srvBeanConfig.pathRep + "trz\\" + pTrz + "\\bin\\";
			b=compile.moveFile(s1, s2);
			if(!b){
				psb.append("Arquivo compilado, mas não foi possível mover o arquivo");
			}
		}
		return(b);
		
	}
	
	public static void main(String argv[]){
		StringBuffer psb = new StringBuffer();
		if(!compile.compObj(psb, compile.CTRZ,"TL01","")){
			_.lg("erro ao compilar o arquivo");
		}else{
			_.lg("sucesso");
		}
	}

}
