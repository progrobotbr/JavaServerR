package com.jsv.lang.program;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
import java.sql.ResultSet;
import com.jsv.compiler.compiler;
import java.lang.StringBuilder;
import java.net.URLEncoder;

import com.jsv.db.base.sqlbase;

import com.jsv.utils.Utils;

import com.jsv.lang.vm.function;

public class program {
	
	private static final int TAM = 200;
	private static final int CIF = 0;
	private static final int CWHILE = 1;
	private static final int CFOR = 2;
	private static final int CBRANCH = 3;
		
	public int finalSize=TAM;
	private int idxLine=0;
	private int size=0;
	public line lines[]=null;
	public String fisicalProgram[]=null;
	public String fisicalProgram2=null;
	public int programCount=0;
	public String Name;
	private Stack<line> stk[] = new Stack[4]; //Monta uma pilha para marcação dos finais de comandos
	private Vector<function> substack; // =new Vector<line>();
	public HashMap<String,function> subAddress;
	
	public int rc;
	public String errmsg;
	
	public void init(String pFisicalProgram[]){
		lines = new line[TAM];
		fisicalProgram = pFisicalProgram;
		stk[CIF] = new Stack<line>(); //IF
		stk[CWHILE] = new Stack<line>(); //WHILE
		stk[CFOR] = new Stack<line>(); //FOREACH
		stk[CBRANCH] = new Stack<line>(); //BRANCH
		substack = new Vector<function>(); //Marcação de SUBs
		subAddress = new HashMap<String,function>();
	}
	
	public void setFisicalProgram(String s){
		fisicalProgram2 = s;
	}
	
	public void putLine(String pLine, int pFisicalLine){
		this.grown();
		line l = new line();
		lines[idxLine] = l; 
		l.lineData = pLine;
		l.logicalLine = idxLine;
		l.fisicalLine = pFisicalLine;
		//this.branch(l, idxLine);
		size = ++idxLine;
	}
	
	public void grown(){
		line l[];
		int i;
		if(idxLine == finalSize){
			i = ( finalSize * 3 ) / 2;
			finalSize = i;
			l = new line[i];
			System.arraycopy(lines,0,l,0,idxLine);
			lines = l;
		}
	}
	
	public String getLineData(int pIdx){
		if(pIdx<size)
			return(lines[pIdx].lineData);
		else return(null);
	}
	public int getLogicalIdx(int pIdx){
		if(pIdx<size)
			return(lines[pIdx].logicalLine);
		else return(-1);
	}
	
	//Calcula o ponto de branch (desvio) de um IF com ENDIF
	// Utiliza pilhas para controle de aninhamento
	private void branch(line l,int pIdx){
		int i;
		line l1;
		function f1;
		String s = l.lineData;
		i = this.compare(s);
		switch(i){
		case 1: stk[CIF].push(l); break;
		case 2: stk[CFOR].push(l); break;        
		case 3: stk[CWHILE].push(l); break;
		case 4: if(stk[CIF].isEmpty()) return;  //Else
			    l1 = stk[CIF].pop();
			    stk[CIF].push(l1); //Deve ser colocado de volta, pois quem retira é o ENDIF
		  	    l1.gotoLine = pIdx;
		  	    break;
		case 5: if(!stk[CBRANCH].isEmpty()){
					l1 = stk[CBRANCH].pop();
					l1.gotoLine = pIdx;
					stk[CIF].pop();
					return;
				}
			    if(stk[CIF].isEmpty()) return; //Endif
				l1 = stk[CIF].pop();
				if(l1.gotoLine==0){ //Quando é diferente de 0, significa que havia um else
				  l1.gotoLine = pIdx;
				}
				break;
		case 6: if(stk[CWHILE].isEmpty()) return;
				l1 = stk[CWHILE].pop();
				l1.gotoLine = pIdx;
				l.gotoLine = l1.logicalLine; //Força a subida
				break;
		case 7: if(stk[CFOR].isEmpty()) return;
				l1 = stk[CFOR].pop();
				l1.gotoLine = pIdx;
				l.gotoLine = l1.logicalLine; //Força a subida
				break;
		case 9: f1 = new function();
				f1.lineIdx = pIdx;
				f1.lineData = l.lineData;
				substack.add(f1);
				break;
		case 12: stk[CBRANCH].push(l); break;
		}
	}
	
	//Ajusta os pontos de Brach e numera as linhas lógicas
	public void putGoto(){
		int i;
		line l;
		for(i=0;i < lines.length; i++){
			l = lines[i];
			l.logicalLine = i;
			this.branch(l,l.logicalLine);
		}
	}
		
	// Organiza o programa, estruturando no formato:
	// sub main. <decls><stmts> endsub. <sub (x).> <decls><stmts> <endsub.>
	// A função principal de um programa é a SUB MAIN
	public void organize(){
		int t=0, flg=0;
		String s1=null;
		line l1;
		Vector<line> globals=new Vector<line>(), 
		       main=new Vector<line>(), 
		       subvar=null, 
		       sub=null, 
		       subcol=new Vector<line>();
		       		
		for(int i=0;i<size;i++){
			t=this.compare(lines[i].lineData);
			System.out.println(lines[i].lineData);
			switch(t){
			case 9: //Sub: 
				flg = 1; 
				sub = new Vector<line>();
				subvar = new Vector<line>();
				sub.add(lines[i]);
				//substack.add(lines[i]);
				break;
			case 11: //Type	
			case 10: //Dim
				if(flg == 0){
					globals.add(lines[i]);
				}else{
					subvar.add(lines[i]);
				}
				break;
			case 8: //Endsub:
				flg = 0;
				sub.add(lines[i]);
				addVectorSub(subcol,sub,subvar);
				subvar = null;
				sub = null;
				break;
			case 4:      //Else
				s1 = "BRANCH.\n"; //;Goto antes do else
				l1 = new line();
				l1.lineData = s1;
				if(flg == 0){
					main.add(l1);
					main.add(lines[i]);
				}else{
					sub.add(l1);
					sub.add(lines[i]);
				}
				break;
			case 2:
				s1 = this.putForEachFirst(lines[i].lineData);
				l1 = new line();
				l1.lineData = s1;
				if(flg == 0){
					main.add(l1);
					main.add(lines[i]);
				}else{
					sub.add(l1);
					sub.add(lines[i]);
				}
				break;
				
			default:
				if(flg == 0){
					main.add(lines[i]);
				}else{
					sub.add(lines[i]);
				}
				break;
			}
		}
		
		lines = addVectorToString(globals, main, subcol);
		size = lines.length;
		finalSize = size;
		
		this.putGoto();
		this.doSubAddress();
		
	}
	
	private void doSubAddress(){
		int i;
		String s,n;
		function f;
		for(i=0;i<substack.size();i++){
			f = substack.get(i);
			s=f.lineData;
			n=this.getName(4, s);
			subAddress.put(n, f);
		}
	}
	
	private String getName(int pIni, String pData){
		int i,z;
		byte b[]=pData.getBytes(), b2[];
		String s="";
		i=pIni;
		while(b[i]!='.' && b[i]!=' '){
			i++;
		}
		if(i>pIni){
			z=i-pIni;
			b2=new byte[z];
			System.arraycopy(b,pIni,b2,0,z);
			s=new String(b2);
		}
		return(s);
	}
	
	//Copia 2 vetores que contém <decls> <stmts> de uma sub respectivamente para outro 
	//  vetor, assim, a sub fica ordenadam ou seja, as primeiras linhas conterão as 
	//  declarações de variáveis e logo após virão as linhas de comandos de execução 
	private void addVectorSub(Vector<line> v1, Vector<line> v2, Vector<line> v3){
		line s;
		
		s = v2.get(0);
		v1.add(s);
		for(int t=0;t<v3.size();t++){
			s = v3.get(t);
			v1.add(s);
		}
		for(int i=1;i<v2.size();i++){
			s = v2.get(i);
			v1.add(s);
		}
	}

	//Cria a estrutura de programa, movendo-a para o array de linhas e ajustando os novos tamanhos 
	//  de fim de array
	private line[] addVectorToString(Vector<line> global, Vector<line> main, Vector<line> subcol){
		int i = global.size() + main.size() + subcol.size() + 2;
		int t, z;
		line s;
		line ss[] = new line[i];
		s = new line();
		s.lineData = "SUB MAIN.\n";
		ss[0] = s;
		z=1;
		for(t=0;t<global.size();t++){
			s = global.get(t);
			ss[z++] = s;
		}
		for(t=0;t<main.size();t++){
			s = main.get(t);
			ss[z++] = s;
		}
		s = new line();
		s.lineData = "ENDSUB.\n";
		ss[z++] = s;
		for(t=0;t<subcol.size();t++){
			s = subcol.get(t);
			ss[z++] = s;
		}
		
		return(ss);
	}
		
	//Máquina de estado finito que server para reconhecer algumas palabras chavas
	// Função utilizada para reconhecimento de comandos de blocos e desvio
	private int compare(String s){
		int i=0;
		byte b[] = s.getBytes();
		
		if(b[0] == 'I'){
			if(b[1]=='F')
				if(b[2]==' ') return(1); //If
		}else if(b[0]=='F') {
				if(b[1]=='O')
					if(b[2]=='R')
						if(b[3]==' ') return(2); //For
	    }else if(b[0]=='W'){
	    	    if(b[1]=='H')
	    		   if(b[2]=='I')
	    			  if(b[3]=='L')
	    				if(b[4]=='E')
	    					if(b[5]==' ') return(3); //While
	    }else if(b[0]=='T'){
	    	     if(b[1]=='Y')
 	    	        if(b[2]=='P')
	    	           if(b[3]=='E')
	    	        	   if(b[4]==' ') return(11); //Type
	    }else if(b[0]=='D'){
	    	     if(b[1]=='I')
    	    	    if(b[2]=='M')
	    	           if(b[3]==' ') return(10); //Dim
	    }else if(b[0]=='S'){
	    	     if(b[1]=='U')
	    	    	if(b[2]=='B')
	    	    	   if(b[3]==' ') return(9); //Sub
		}else if(b[0]=='B'){
				 if(b[1]=='R')
					if(b[2]=='A')
    	    	       if(b[3]=='N')
    	    	    	   if(b[4]=='C')
    	    	    		   if(b[5]=='H')
    	    	    			   if(b[6]=='.')  return(12); //Branch
		}else if(b[0]=='E')
				if(b[1]=='N'){
				   if(b[2]=='D')
					  if(b[3]=='S'){
						 if(b[4]=='U')
							 if(b[5]=='B')
								 if(b[6]=='.' || b[6]==' ') return(8);  //EndSub
					  }else if(b[3]=='I'){
						       if(b[4]=='F')
							      if(b[5]=='.' || b[5]==' ') return(5); //EndIf
					  }else if(b[3]=='F'){
						   	   if(b[4]=='O')
								  if(b[5]=='R')
									 if(b[6]=='.' || b[6]== ' ' ) return(7); //EndFor
					  }else if(b[3]=='W')
						       if(b[4]=='H')
						    	  if(b[5]=='I')
									 if(b[6]=='L')
										if(b[7]=='E') 
											if(b[8]=='.' || b[8]==' ') return(6); //EndWhile
				}else if(b[1]=='L')
						 if(b[2]=='S')
							if(b[3]=='E')
						 	   if(b[4]=='.' || b[4]==' ') return(4); //Else
		
		return(i);
	}
	
	private String putForEachFirst(String line){
		String s1=null;
		String s[] = line.split(" ");
		s1 = "TBFIRST " + s[4];
		return(s1);
	}
	
	public void saveProgram(){
		int i;
		String s;
		function f;
		line l;
		try{
			//Salva programa compilado
			FileOutputStream fstream = new FileOutputStream("C:\\Rep\\prog.bcp");
			s = "F-" + substack.size() + "\n\r";
			fstream.write(s.getBytes());
			for(i=0; i<substack.size(); i++){
				f = substack.get(i);
				s = "F-" + f.lineIdx + "@" + f.lineData;
				fstream.write(s.getBytes());
			}
			s = "L-" + lines.length + "\n\r";
			fstream.write(s.getBytes());
			for(i=0;i<lines.length;i++){
				l = lines[i];
				s = "L-" +l.fisicalLine + 
				    "-" + l.logicalLine + 
				    "-" + l.gotoLine + 
				    "-" + l.canDebug + 
				    "@" +l.lineData;
				fstream.write(s.getBytes());
			}
			fstream.close();
			
			//Salva programa original
			fstream = new FileOutputStream("C:\\Rep\\prog.bas");
			fstream.write(fisicalProgram2.getBytes());
			fstream.close();
			
			
		}catch(Exception e){ }
		
	}
	
	public String getCompiledProgram(){
		int i;
		String s;
		StringBuffer sb=new StringBuffer();
		function f;
		line l;
		try{
			//Salva programa compilado
			s = "F-" + substack.size() + "\n\r";
			sb.append(s);
			for(i=0; i<substack.size(); i++){
				f = substack.get(i);
				s = "F-" + f.lineIdx + "@" + f.lineData;
				sb.append(s);
			}
			s = "L-" + lines.length + "\n\r";
			sb.append(s);
			for(i=0;i<lines.length;i++){
				l = lines[i];
				s = "L-" +l.fisicalLine + 
				    "-" + l.logicalLine + 
				    "-" + l.gotoLine + 
				    "-" + l.canDebug + 
				    "@" +l.lineData;
				sb.append(s);
			}
			
		}catch(Exception e){ }
		
		return(sb.toString());
	}
	
	public void loadPogram(String snp){
		int i,t;
		String p[],c[],sLine="",s1, sr, srnp;
		function f;
		try{
			//sr = "C:\\rep\\";
			sr = "C:\\Renato\\programas\\rserver\\Rep\\";
			srnp = sr+snp+".bcp";
			FileInputStream fstream = new FileInputStream(srnp);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			sLine = br.readLine();
			//i = Integer.parseInt(sLine);
			while(sLine.startsWith("F") || sLine.length()==0){
				sLine = br.readLine();
				if(sLine != null && sLine.length()>0 && sLine.startsWith("F")){
					p = sLine.split("@");
					c = p[0].split("-");
					f = new function();
					f.lineIdx = Integer.parseInt(c[1]);
					f.lineData = p[1]+"\n";
					substack.add(f);
				}
			}
			//sLine = br.readLine();
			p = sLine.split("-");
			i = Integer.parseInt(p[1]);
			lines = new line[i];
			t=0;
			while (sLine != null){
				sLine = br.readLine();
				if(sLine != null && sLine.length()>0){
					p = sLine.split("@");
					s1 = p[0];
					c = s1.split("-");
					lines[t] = new line();
					lines[t].fisicalLine = Integer.parseInt(c[1]);
					lines[t].logicalLine = Integer.parseInt(c[2]);
					lines[t].gotoLine = Integer.parseInt(c[3]);
					lines[t].canDebug = Integer.parseInt(c[4]);
					lines[t].lineData = p[1]+"\n";
					t++;
				}
			}
			in.close();
			
			this.doSubAddress();
			
		}catch(Exception e){ System.out.println(e.toString()); }
	}
	
	public int loadPogramDB(String snp){
		String ss[];
		rc=0;
		//System.out.println("program.loadprogramDB"+snp);
		ss=this.loadDB(snp,compiler.PRGC);
		//System.out.println("program.loadprogramDB"+snp);
		rc=this.loadToRun(ss);
		return(rc);
	}
	
	public String loadPogramDB2(String snp, String pType){
		String s="";
		rc=0;
		s=this.loadDB2(snp,pType);
		return(s);
	}
	
	public int loadToRun(String ss[]){
		int i,t,itam,idx;
		String p[],c[],sLine="",s1;
		function f;
		System.out.println(ss);
		if(ss==null) { rc=4;errmsg="Não há dados";return(rc); }
		if(ss.length==0) { rc=4;errmsg="Não há dados";return(rc); }
		try{
			i=0;
			idx=0;
			itam=ss.length;
			sLine = getLine(ss,idx,itam);idx++;
			while((sLine.startsWith("F") || sLine.length()==0) && sLine != null){
				sLine = getLine(ss,idx,itam);idx++;
				if(sLine != null && sLine.length()>0 && sLine.startsWith("F")){
					p = sLine.split("@");
					c = p[0].split("-");
					f = new function();
					f.lineIdx = Integer.parseInt(c[1]);
					f.lineData = p[1]+"\n";
					substack.add(f);
				}
				System.out.println(sLine);
			}
			//sLine = br.readLine();
			p = sLine.split("-");
			i = Integer.parseInt(p[1]);
			lines = new line[i];
			t=0;
			while (sLine != null){
				sLine = getLine(ss,idx,itam);idx++;
				if(sLine != null && sLine.length()>0){
					p = sLine.split("@");
					s1 = p[0];
					c = s1.split("-");
					lines[t] = new line();
					lines[t].fisicalLine = Integer.parseInt(c[1]);
					lines[t].logicalLine = Integer.parseInt(c[2]);
					lines[t].gotoLine = Integer.parseInt(c[3]);
					lines[t].canDebug = Integer.parseInt(c[4]);
					lines[t].lineData = p[1]+"\n";
					t++;
				}
			}
			this.doSubAddress();
			
		}catch(Exception e){ rc=4; errmsg=e.toString(); }
		return(rc);
	}
	
	private String getLine(String ss[], int iDx, int iSize){
		if(iDx<iSize){
			return(ss[iDx]);
		}else{
			return(null);
		}
	}
	
	public String[] loadDB(String snp, String pType){
		int isize=0;
		String s;
		StringBuilder sb = new StringBuilder();
		sqlbase sql = new sqlbase();
		ResultSet rs;
		Vector<String> v=new Vector<String>();
		rc=0;
		if(snp==null) { errmsg="Informar nome do arquivo"; return(null); }
		if(snp.length()==0) { errmsg="Informar nome do arquivo"; return(null); }
		s="Select isize from prhd where objcd='"+snp+"' and objtp='"+pType+"';";
		rs=sql.select(s);
		try{
		  if(!rs.next()) { rc=4; errmsg="Programa não encontrado"; sql.commit(); return(null);}
		  isize = rs.getInt(1);
		}catch(Exception ex){ rc=4; errmsg=ex.toString(); sql.commit(); return(null);}
		
		s="Select linnr, lintx from prsc where objcd='"+snp+"' and objtp='"+compiler.PRGC+"';";
		rs=sql.select(s);
		try{
			while(rs.next()){
				sb.append(rs.getString("lintx"));
			}
		}catch(Exception ex){ rc=4; errmsg=ex.toString(); sql.commit(); return(null);}
		
		sql.dispose();
		
		System.out.println(s);
		s=sb.toString().replaceAll("\r","");
		String ss[] = s.split("\n");
		return(ss);
	}
	
	public String loadDB2(String snp, String pType){
		int isize=0;
		String s="";
		StringBuilder sb = new StringBuilder();
		sqlbase sql = new sqlbase();
		ResultSet rs;
		Vector<String> v=new Vector<String>();
		
		clearRc();
		
		if(snp==null) { errmsg="Informar nome do arquivo"; return(null); }
		if(snp.length()==0) { errmsg="Informar nome do arquivo"; return(null); }
		s="Select isize from prhd where objcd='"+snp+"' and objtp='"+pType+"';";
		rs=sql.select(s);
		try{
		  if(!rs.next()) { rc=4; errmsg="Programa não encontrado"; sql.commit(); return(null);}
		  isize = rs.getInt(1);
		}catch(Exception ex){ rc=4; errmsg=ex.toString(); sql.commit(); return(null);}
		
		s="Select linnr, lintx from prsc where objcd='"+snp+"' and objtp='"+pType+"';";
		rs=sql.select(s);
		try{
			while(rs.next()){
				sb.append(rs.getString("lintx"));
			}
		}catch(Exception ex){ rc=4; errmsg=ex.toString(); sql.commit(); return(null);}
		s=sb.toString();
		
		sql.dispose();
		
		return(s);
	}
	
	public int saveProgramDB(sqlbase pSqlDb, String pName, String pType, String pDescr, String pSource){
		int i, isize=0;
		String s,s1;
		Utils ou = new Utils();
		Vector<String> v;
		//sqlbase sql = new sqlbase();
		sqlbase sql = pSqlDb;
		
		clearRc();
		
		s1 = "delete from prhd where objcd='"+pName+"' and objtp = '" + pType + "';";
		rc = sql.change(s1,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		s1 = "delete from prsc where objcd='"+pName+"' and objtp = '" + pType + "';"; 
		rc=sql.change(s1); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		isize = pSource.length();
		s1 = "insert into prhd (objcd, objtp, descr, isize) values ('"+pName+"','"+pType+"','"+pDescr+"','"+isize+"');";
		rc=sql.change(s1); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		v=ou.block(pSource, 80);
		
		for(i=0;i<v.size();i++){
			s=v.get(i);
			s1 = "insert into prsc (objcd, objtp, linnr, lintx) values ("+
			     "'"+pName+"','"+pType+"','"+i+"','"+s+"');";
			rc=sql.change(s1);
			if(rc!=0){
				break;
			}	
		}
		
		/*
		if(rc==0){
			rc=sql.commit();
		}else{
			rc=sql.rollback();
		}
		*/
		errmsg = sql.errmsg;
		
		return(rc);
	}
	
	public int saveHeader(String sNome, String sDesc){
		/*
		int i, isize=0;
		String s,s1;
		Utils ou = new Utils();
		Vector<String> v;
		sqlbase sql = new sqlbase();
		
		clearRc();
		
		s1 = "insert into prhd (objcd, objtp, descr, isize) values ('"+pName+"','"+pType+"','"+pDescr+"','"+isize+"');";
		rc=sql.change(s1); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		v=ou.block(pSource, 80);
		
		for(i=0;i<v.size();i++){
			s=v.get(i);
			s1 = "insert into prsc (objcd, objtp, linnr, lintx) values ("+
			     "'"+pName+"','"+pType+"','"+i+"','"+s+"');";
			rc=sql.change(s1);
			if(rc!=0){
				break;
			}	
		}
		
		if(rc==0){
			rc=sql.commit();
		}else{
			rc=sql.rollback();
		}
		errmsg = sql.errmsg;
		*/
		return(rc);
	}
	
	public int createTransaction(String pTrz, String pDesc){
		String s,sn,sp[];
		ResultSet rs;
		sqlbase sql = new sqlbase();
		
		this.clearRc();
		s = "select objcd from prhd where objcd = '"+pTrz+"' and objtp='TRZZ'";
		rs=sql.select(s);
		
		try{
		  if(rs.next()) { rc=4; errmsg="Transaction already exist: "+pTrz; sql.commit(); return(1);}
		}catch(Exception ex){ rc=4; errmsg=ex.toString(); sql.commit(); return(1);}
		
		s = "insert into prhd (objcd, objtp, descr) values ('"+pTrz+"','TRZZ','"+pDesc+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		sn="TRZ"+pTrz;
		s = "insert into prhd (objcd, objtp, descr) values ('"+sn+"','"+compiler.PRGS+"','"+pDesc+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		s = "insert into prhd (objcd, objtp, descr) values ('"+sn+"','"+compiler.PRGC+"','"+pDesc+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		s = "insert into prtr (obfcd, obftp, obccd, obctp) values ('"+pTrz+"','TRZZ','"+sn+"','"+compiler.PRGC+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		sp=this.getTemplateTrz();
		
		s = "insert into prsc (objcd, objtp, linnr, lintx) values ('"+sn+"','"+compiler.PRGS+"','0','"+sp[0]+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
	
		s = "insert into prsc (objcd, objtp, linnr, lintx) values ('"+sn+"','"+compiler.PRGS+"','1','"+sp[1]+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		if(rc==0){
			rc=sql.commit();
		}else{
			rc=sql.rollback();
		}
		errmsg = sql.errmsg;
		
		return(rc);
		
	}
	
	private String[] getTemplateTrz(){
		String s[] = new String[2];
		s[0]="Sub Create.\nEndsub.\nSub ProcessBeforeOutput.\nEndsub.\nSub ProcessAfterInput.\nEndsu";
		s[1]="b.\n";
		return(s);
	}
	
	public int createScreen(sqlbase pSqlBase, String pTrz, String pScreen, String pDesc){
		
		String s,sn,sp[];
		ResultSet rs;
		//sqlbase sql = new sqlbase();
		sqlbase sql = pSqlBase;
		
		this.clearRc();
		
		sn = "SCN"+pScreen+pTrz;
		s = "select objcd from prhd where objcd = '"+sn+"' and objtp='PRGS'";
		rs=sql.select(s);
		
		try{
		  if(rs.next()) { rc=4; errmsg="Screen already exist: "+pScreen; sql.commit(); return(1);}
		}catch(Exception ex){ rc=4; errmsg=ex.toString(); sql.commit(); return(1);}
		
		s = "insert into prhd (objcd, objtp, descr) values ('"+sn+"','PRGS','"+pDesc+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		s = "insert into prhd (objcd, objtp, descr) values ('"+sn+"','PRGC','"+pDesc+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		s = "insert into prtr (obfcd, obftp, obccd, obctp) values ('"+pTrz+"','TRZZ','"+sn+"','"+compiler.PRGC+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		sp=this.getTemplateTrz();
		
		s = "insert into prsc (objcd, objtp, linnr, lintx) values ('"+sn+"','"+compiler.PRGS+"','0','"+sp[0]+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
	
		s = "insert into prsc (objcd, objtp, linnr, lintx) values ('"+sn+"','"+compiler.PRGS+"','1','"+sp[1]+"')";
		rc = sql.change(s,true); if(rc!=0) { errmsg=sql.errmsg; sql.rollback(); return(rc); }
		
		if(rc==0){
			rc=sql.commit();
		}else{
			rc=sql.rollback();
		}
		errmsg = sql.errmsg;
		
		return(rc);
	}
	
	private void clearRc(){
		rc=0;
		errmsg="";
	}
}

