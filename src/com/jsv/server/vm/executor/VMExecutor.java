package com.jsv.server.vm.executor;

import java.util.HashMap;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.editor.BParser;
import com.jsv.lang.program.line;
import com.jsv.lang.vm.vm;

public class VMExecutor {
	
	private vm vmc;
	private BParser runner;
	private sqlbase mSqlDb;
	private line lnError = new line();
	
	public VMExecutor(){
		vmc = new vm();
		runner = new BParser();
	}
	
	public void setDB(sqlbase sqldb){
		mSqlDb=sqldb;
		vmc.setDb(mSqlDb);
	}
	
	/*
	public void clearRcSql(){
		vmc.clearRcSql();
	}
	
	public int getRcSql(){
	    return(vmc.getRcSql());	
	}
	*/
	public int load(String s){
		int i;
		i = vmc.loadProg(s);
		this.log(s);
		if(i==0){
			runner.setVM(vmc);
			vmc.setParser(runner);
		}
		return(i);
	}
	
	public int executeStep(){
		int i;
		i=vmc.executeLine();
		this.setMsg(i);
		return(i);
	}
	
	public int execMain(){
		int i;
		i = vmc.loadInitialSub("MAIN",vm.VM_MANTAIN_FRAME0);
		
		if(i==vm.PRG_LINE_SUCCESS){
			do{
				i=vmc.executeLine();
			}while(i==vm.PRG_LINE_SUCCESS);
			
		}
		return(i);
	}
    
	public int execSub(String s){
		int i;
		i = vmc.loadInitialSub(s,vm.VM_MANTAIN_FRAME0);
		
		if(i==vm.PRG_LINE_SUCCESS){
			do{
				i=vmc.executeLine();
			}while(i==vm.PRG_LINE_SUCCESS);
			
		}
		return(i);
	}
	
	public int execLineCallSub(String s){
		int i;
		i = vmc.loadInitialSub(s,vm.VM_MANTAIN_FRAME0);
		return(i);
	}
	
	public int execLine(){
		int i;
		i = vmc.executeLine();
		return(i);
	}
	
	public line getLineRead(){
		line l = vmc.getLineRead();
		return(l);
	}
	
	public void clearErr(){
		vmc.rc=0;
		vmc.errinstr="";
		vmc.errmsg="";
	}
    public line getError(){
    	lnError.lineData=vmc.errinstr;
    	return(lnError);
    }
	public void setError(String s){
		vmc.errinstr=s;
	}
	//Tratamento de mensagens
	public void setMsg(int i){
		switch(i){
		case vm.PRG_END_WITH_SUCCESS :
			lg("Processamento concluído");
			break;
		case vm.PRG_LINE_SUCCESS :
        	lg("Linha processada com sucesso");
        	break;
        default: lg("Erro ao processar a Linha");
        }
	}
	
	public HashMap<String,Variant> getMemGlobal(){
		HashMap<String,Variant> hp;
		hp=vmc.getMemGlobal();
		return(hp);
	}
	public void lg(String s){
		//System.out.println(s);
	}
	
	public String getVar(String s){
		String s1=null;
		Variant v;
		if(s.contains("-")){
			v=vmc.loadField(s);
		}else{
			v=vmc.load(s);
		}
		if(v!=null){
			s1=v.toString();
		}else{
			s1="#N/A";
		}
		return(s1);
	}
	
	public void log(String s){
		//System.out.println(s);
	}
	
	public void setIniScreenCommand(){
		vmc.screencommand=false;
	}
	
	public boolean getScreenCommand(){
		return(vmc.screencommand);
	}

}
