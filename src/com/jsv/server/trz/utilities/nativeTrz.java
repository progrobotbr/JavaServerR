package com.jsv.server.trz.utilities;

import java.io.File;

import com.jsv.data.Variant;
import com.jsv.java.compile.compile;
import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;
import com.jsv.server.config.srvBeanConfig;
import com.jsv.server.trz.base.AScreenBaseJava;
import com.jsv.utils.Utils;

public class nativeTrz {
	
	public static int compilePrg(String pnmTrz, String pnmPrg, table tbsource, table tbmsg){
		int rc=0;
		String s;
		Variant v;
		structure st;
		StringBuffer sb=new StringBuffer();
		StringBuffer sb1=new StringBuffer();
		
		if(table.staticIsEmpty(tbsource)){
			return(4);
		}
		tbsource.setIndex(0);
		while((st=tbsource.foreach())!=null){
			sb.append(st.get("LINTX").getString());
		}
		s=pnmPrg.substring(0,3);
		s=pnmPrg.replace(s, s.toLowerCase());
		rc=nativeTrz.saveSource(pnmTrz, s, sb.toString());
		if(rc==0){
			if(compile.compObj2(sb1, pnmTrz, s)){
				rc=0;
			}else{
				rc=4;
			}
			st = tbmsg.getHeader();
			st = st.clone();
			v=st.get("LINTX");
			v.moveStringToInternal(sb1.toString());
			tbmsg.append(st, null);
		}
	    return(rc);
	}
	
	public static int createScreen(String pnmTrz, String pnmScreen, String pdescr){
		int rc=0;
		String sName = "scn" + pnmScreen + pnmTrz; 
		String s="import com.jsv.server.trz.base.AScreenBaseJava;\n"+
                 "import com.jsv.server.trz.base.ATransactionBaseJava;\n\n"+
				 "public class " + sName + " extends AScreenBaseJava{\n"+
                 "	public void onCreate(ATransactionBaseJava ctx){\n"+
                 "		ctx.storegVar(\"OKCODE\",\"\");"+
				 "	}\n\n"+
                 "	public void onPBO(ATransactionBaseJava ctx){\n"+
                 "	}\n\n"+
                 "	public void onPAI(ATransactionBaseJava ctx){\n"+
		         "	}\n"+
                 "}\n";
		//sName += srvBeanConfig.extPrgSource;  
		rc=nativeTrz.saveSource(pnmTrz, sName, s);
		return(rc);
	}
	
	public static int createTrz(String pnmTrz, String pdescr){
		int rc=0;
		String sName;
		File file;
		file = new File(srvBeanConfig.pathRep + "trz\\" + pnmTrz);
		file.mkdir();
		file = new File(srvBeanConfig.pathRep + "trz\\" + pnmTrz + "\\source");
		file.mkdir();
		file = new File(srvBeanConfig.pathRep + "trz\\" + pnmTrz + "\\protocol");
		file.mkdir();
		file = new File(srvBeanConfig.pathRep + "trz\\" + pnmTrz + "\\bin");
		file.mkdir();
		
		sName = "trz" + pnmTrz;
		String s="import com.jsv.server.trz.base.ITransactionJava;\n"+
                 "import com.jsv.server.trz.base.ATransactionBaseJava;\n"+
				 "\n"+
				 "public class " + sName + " implements ITransactionJava{\n"+
                 "	public void createVarScreen(ATransactionBaseJava ctx){\n"+
				 "	}\n"+
                 "	public void initTransaction(ATransactionBaseJava ctx){\n"+
                 "	}\n"+
                 "	public void endTransaction(ATransactionBaseJava ctx){\n"+
		         "	}\n"+
                 "}\n";
		//sName += srvBeanConfig.extPrgSource;  
		rc=nativeTrz.saveSource(pnmTrz, sName, s);
		return(rc);
	}
	
	public static int saveScreenStructure(String pnmTrz, String pnmPrg, table tbp1, table tbp2){
		int rc=0;
		String s="";
		structure st;
		StringBuffer sb=new StringBuffer();
		
		if(table.staticIsEmpty(tbp1) || table.staticIsEmpty(tbp2) ){
			return(4);
		}
		tbp1.setIndex(0);
		while((st=tbp1.foreach())!=null){
			sb.append(st.get("LINTX").getString());
		}
		//s="str"+pnmTrz;
		s="str@"+pnmTrz+"@"+pnmPrg.substring(3,7);
		rc=nativeTrz.saveProtocol(pnmTrz, s, sb.toString());
		if(rc!=0){
			return(rc);
		}
		
		tbp2.setIndex(0);
		sb=new StringBuffer();
		while((st=tbp2.foreach())!=null){
			sb.append(st.get("LINTX").getString());
		}
		s="rep@"+pnmTrz+"@"+pnmPrg.substring(3,7);
		rc=nativeTrz.saveProtocol(pnmTrz, s, sb.toString());
		
		return(rc);
	}
	
	public static table getScreenSource(String pnmTrz, String pnmScreen){
		int rc;
		String sn="", sTrz="";
		table tb=null;
		structure st=null;
		Variant v;
		String s;
		StringBuffer sb=new StringBuffer();
		sTrz=pnmTrz; //pnmScreen.substring(7,11);
		if(pnmScreen!=null && pnmScreen.startsWith("S")){
			sn="scn"+pnmScreen.substring(3,7)+sTrz+srvBeanConfig.extPrgSource;
		}else{
			sn="trz"+sTrz+srvBeanConfig.extPrgSource;
		}
		s=srvBeanConfig.pathRep + "trz\\" + sTrz + "\\source\\" + sn;
		rc=Utils.readFile(s, sb);
		if(rc==0){
			tb=nativeTrz.createTableSource();
			st=tb.getHeader();
			st=st.clone();
			v=st.get("LINTX");
			v.moveStringToInternal(sb.toString());
			tb.append(st, null);
		}
		return(tb);
	}
	
	public static table getScreenStructure(String pnmTrz, String pnmScreen){
		table tb=null;
		int i;
		String  sTrz="";
		Variant v1;
		structure st=null;
		sTrz=pnmTrz; //pnmScreen.substring(7,11);
		String s = "str@"+sTrz+"@"+pnmScreen.substring(3,7)+srvBeanConfig.extXML;
		String s1 = srvBeanConfig.pathRep + "trz\\" + sTrz + "\\protocol\\"+s;
		StringBuffer sb=new StringBuffer();
		i=Utils.readFile(s1, sb);
		tb=nativeTrz.createTableSource("TBP1");
		if(i==0){
			st=tb.getHeader();
			st=st.clone();
			v1=st.get("LINTX");
			v1.s=sb.toString();
			tb.append(st, null);
		}
		return(tb);
	}
	
	public static table getTreeTrz(String pnmTrz){
		int i;
		String s="", sTela="";
		String s1 = srvBeanConfig.pathRep + "trz\\" + pnmTrz + "\\source\\";
		Variant v1;
		table tb=null;
		structure st=null;
		String ffs[] = Utils.readFiles(s1);
		tb=nativeTrz.createTableTree();
		for(i=0; i<ffs.length; i++){
			st=tb.getHeader();
			st=st.clone();
			s=ffs[i];
			s=s.replaceAll(srvBeanConfig.extPrgSource, "");
			s=s.toUpperCase();
			//sTela=s.substring(s.length()-4, s.length());
			//if(s.toUpperCase().startsWith("SCN")){
				//s="SCN"+sTela+pnmTrz;
			//}else{
				//s="TRZ"+pnmTrz;
			//}
			v1=st.get("LFATCD"); v1.s=pnmTrz;
			v1=st.get("LCHDCD"); v1.s=s;
			v1=st.get("LCHDTP"); v1.s="PRGC";
			v1=st.get("LCHDSC"); v1.s="";
			
			tb.append(st, null);
		}
		return(tb);
	}
	
	public static int saveSource(String nmTrz, String pnmPrg, String pSource){
		String s1 = srvBeanConfig.pathRep + "trz\\" + nmTrz + "\\source\\" + pnmPrg + srvBeanConfig.extPrgSource;
		return(Utils.saveFile(s1, pSource));
	}
	
	public static int saveProtocol(String pnmTrz, String pName, String pSource){
		String s1 = srvBeanConfig.pathRep + "trz\\" + pnmTrz + "\\protocol\\" + pName + srvBeanConfig.extXML;
		return(Utils.saveFile(s1, pSource));
	}
		
	public static table createTableSource(){
		Variant v1, v2;
		table tb;
		structure st = new structure("TBSOURCE");
		v1 = new Variant(0);
		v2 = new Variant("");
		st.add("LINNR", v1);
		st.add("LINTX", v2);
		tb = new table(st);
		tb.Name="TBSOURCE";
		return(tb);
	}
	
	public static table createTableSource(String pName){
		Variant v1, v2;
		table tb;
		structure st = new structure(pName);
		v1 = new Variant(0);
		v2 = new Variant("");
		st.add("LINNR", v1);
		st.add("LINTX", v2);
		tb = new table(st);
		tb.Name=pName;
		return(tb);
	}
	
	public static table createTableTree(){
		Variant v1, v2, v3, v4;
		table tb;
		structure st = new structure("TBTREE");
		v1 = new Variant("");
		v2 = new Variant("");
		v3 = new Variant("");
		v4 = new Variant("");
		st.add("LFATCD", v1);
		st.add("LCHDCD", v2);
		st.add("LCHDTP", v3);
		st.add("LCHDSC", v4);
		tb = new table(st);
		tb.Name="TBTREE";
		return(tb);
	}
	
	public static structure createIdxTable(int index, int linebypage){
		Variant v1, v2;
		table tb;
		structure st = new structure("TBSOURCE");
		v1 = new Variant(index);
		v2 = new Variant(linebypage);
		st.add("INDEX", v1);
		st.add("LINEBYPAGE", v2);
		//tb = new table(st);
		//tb.append(st, null);
		return(st);
	}
	
	public static structure createIdxTable(String pName, int index, int linebypage){
		Variant v1, v2;
		table tb;
		structure st = new structure(pName);
		v1 = new Variant(index);
		v2 = new Variant(linebypage);
		st.add("INDEX", v1);
		st.add("LINEBYPAGE", v2);
		//tb = new table(st);
		//tb.append(st, null);
		return(st);
	}
	
	public static table createTable(String pName, String pStructure ) throws Exception{
		char c=' ';
		int i;
		float f=0;
		String sc[],stp[];
		Variant v1;
		table tb;
		
		if(!(pName!=null && pName.trim()!="")){
			throw(new Exception("Informe o nome da tabela"));
		}
		
		if(!(pStructure!=null && pStructure.trim()!="")){
			throw(new Exception("Informe os campos da tabela: <campo1,tipo;campo2,tipo;...>"));
		}
		
		sc=pStructure.split(";");
		structure st = new structure("pName");
		for(i=0;i<sc.length;i++){
			stp=sc[i].split(",");
			if(stp[1].equals("C")){
				v1=new Variant(c);
			}else if(stp[1].equals("S")){
				v1=new Variant("");
			}else if(stp[1].equals("I")){
				v1=new Variant(0);
			}else if(stp[1].equals("F")){
				v1=new Variant(f);
			}else{
				throw(new Exception("Tipo não identificado"));
			}
			st.add(stp[0].toUpperCase(), v1);
		}
		tb=new table(st);
		tb.Name = pName;
		return(tb);
	}
	
	public static void tableNextPage(table ptbdata, structure ptbidx){
		int tam, pg, idx, next;
		structure st;
		Variant v;
		
		st=ptbidx;
		
		tam=ptbdata.getRowCount();
		pg=st.get("LINEBYPAGE").i;
		idx=st.get("INDEX").i;
		next=idx+pg;
		if(next<tam){
			v=st.get("INDEX");
			v.i=next;
		}
	}
	
	public static void tablePreviousPage(table ptbdata, structure ptbidx){
		int tam, pg, idx, prev;
		structure st;
		Variant v;
		
		st=ptbidx;
		
		tam=ptbdata.getRowCount();
		pg=st.get("LINEBYPAGE").i;
		idx=st.get("INDEX").i;
		prev=idx-pg;
		if(prev>=0){
			v=st.get("INDEX");
			v.i=prev;
		}
	}
	
	public static void tableSetPage(table ptbdata, structure ptbidx, int pPage){
		int tam, pg, idx, prev, icalc;
		structure st;
		Variant v;
		
		st=ptbidx;
		
		tam=ptbdata.getRowCount();
		pg=st.get("LINEBYPAGE").i;
		
		icalc = pg * pPage;
		
		if(icalc < tam){
			v=st.get("INDEX");
			v.i=icalc;
		}
		
	}
	
	public static void tablePages(AScreenBaseJava pScr, String pCmd){
		table tb;
		structure gtb;
		String s[],stab;
		int t;
		
		if(pCmd==null || pCmd.trim().length()<5){
			return;
		}
		
		s=pCmd.split("_");
		
		if(s==null || s.length<2){
			return;
		}
		
		stab = s[1];
		
		if(pCmd.equals("BAIX_"+stab)){
			gtb=pScr.loadgVarE("GD"+stab);
			tb=pScr.loadgVarT(stab);
			nativeTrz.tableNextPage(tb,gtb);
			
		}else if(pCmd.equals("CIMA_"+stab)){
			gtb=pScr.loadgVarE("GD"+stab);
			tb=pScr.loadgVarT(stab);
			nativeTrz.tablePreviousPage(tb,gtb);
		}
		
	}
	
	
		
}
