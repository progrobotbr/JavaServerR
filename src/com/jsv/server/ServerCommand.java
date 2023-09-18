package com.jsv.server;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.w3c.dom.Document;

import com.jsv.compiler.compiler;
import com.jsv.lang.program.program;
import com.jsv.lang.vm.vm;
import com.jsv.db.base.sqlbase;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.server.trz.base.ATransactionBaseJava;
import com.jsv.server.trz.base.ITransaction;
import com.jsv.server.trz.base.Transaction;
import com.jsv.server.trz.base.dump;
import com.jsv.server.vm.executor.VMExecutor;
import com.jsv.utils.Utils;
import com.jsv.server.trz.base.dump;

public class ServerCommand{

	public static final String CALLTRZ = "calltrz"; // New Transaction
	public static final String GOTOSCR = "gotoscr"; // Goto Screen
	public static final String LOGOFF  = "logoff";  // Logoff
	public static final String DEBUG   = "debug";   // Força o debug
	public static final String DEBUGF8 = "debugf8"; // Força o debug
	public static final String HELLO   = "hello";   // Saudação
	public static final String PRGSAVE = "prgsave"; // Grava programa
	public static final String PRGLOAD = "prgload"; // Carrega programa
	public static final String PRGCOMP = "prgcomp"; // Compila e grava programa
	public static final String PRGGETV = "prggetv"; // Pega variável em momento de debug
	public static final String TIPO    = "tipo";    // 

	private ServerSession  mSession;
	private ServerRequest  request;
	private ServerResponse response;
	private ServerProtocol protocol;
	private Transaction    transaction;
	private ATransactionBaseJava transactionJava;
	
	public ServerCommand(ServerSession pSession) {
		mSession = pSession;
		request =  mSession.getRequest();
		response = mSession.getResponse();
		protocol = mSession.getProtocol();
	}

	public boolean executeServerCommand() {
		boolean b = false;
		String sCmd;

		try{
			
			sCmd = protocol.getServerCommand();

			// b=false, faz saída do MainLoop
			if (sCmd.equals(ServerCommand.GOTOSCR)) {
				this.log("GOTO");
				b = this.GotoScreenJava();
			} else if (sCmd.equals(ServerCommand.CALLTRZ)) {
				this.log("CALL");
				b = this.CallTransactionJava();
			} else if (sCmd.equals(ServerCommand.LOGOFF)) {
				b=this.Logoff();
			} else if (sCmd.equals(ServerCommand.DEBUG)) {
				b = this.Debug();
			} else if (sCmd.equals(ServerCommand.DEBUGF8)) {
				b = this.DebugF8();
			} else if (sCmd.equals(ServerCommand.HELLO)) {
				this.log("HELLO");
				b = protocol.Hello();
			} else if (sCmd.equals(ServerCommand.PRGSAVE)) {
				b = this.SavePrg();
			} else if (sCmd.equals(ServerCommand.PRGLOAD)) {
				String sTip = protocol.getServerCommand(ServerCommand.TIPO);
				b = this.LoadPrg(sTip);
			} else if (sCmd.equals(ServerCommand.PRGCOMP)) {
				b = this.CompPrg();
			} else if (sCmd.equals(ServerCommand.PRGGETV)){
				b = this.GetVar();
			}
		
		}catch(Exception ex){
			b=protocol.sendScreen(dump.writeDump(ex));
			return(b);
		}
		return (b);
	}

	public boolean GotoScreen(){
		boolean b=false;
		String s;
		//try{
			Document doc = protocol.getDoc();
			  transaction.clearErr();
			b=transaction.putRequestOfScreen(doc);
			  transaction.begintran();
			b=transaction.process();
			  transaction.commit();
			s=transaction.getResponseOfScreen();
			b=protocol.sendScreen(s);
			
		//}catch(Exception ex){ 
			//b=false;
			//log(ex.toString());
		//}
		return(true);
	}
	
	public boolean GotoScreenJava(){
		boolean b=false;
		String s;
		
		//try{
		Document doc = protocol.getDoc();
			
		    transactionJava.clearRC();
		b = transactionJava.putRequestOfScreen(doc);
		b = transactionJava.process();
		s = transactionJava.getResponseOfScreen();
			
		b = protocol.sendScreen(s);
		
		return(b);
		
		/*
		}catch(Exception ex){
			b=protocol.sendScreen(dump.writeDump(ex));
			return(b);
		}
		*/
	}
	
	
	public boolean CallTransaction() {
		boolean b=false;
		int i;
		String s,s1;
		transaction = null;
		try{
			s=protocol.getServerCommand(ComponentsDictionary.VL);
			transaction = new Transaction(s);
			transaction.setId(s);
			s1 = transaction.getStructure();
			i  = transaction.load(s);
			//Muda o passo da transação para 1, inicial
			transaction.ps = 1;
			i  = transaction.setScreen();
			b  = protocol.SendStru(s1);
		}catch(Exception ex){ log(ex.toString());}	
		return(b);
	}
	
	public boolean CallTransactionJava() {
		boolean b=false;
		String s,s1, screen;
		transactionJava = null;
		
		try{
			
			s  = protocol.getServerCommand(ComponentsDictionary.VL);
			
			     transactionJava = new ATransactionBaseJava();
			     transactionJava.init(s);
			     transactionJava.execCreateVarScreenTransaction();
			     transactionJava.execInitTransaction();
	    screen = transactionJava.getScreen();
	        s1 = transactionJava.getStructure(screen);
					
			     
			b  = protocol.SendStru(s1);
			
		}catch(Exception ex){
			b=protocol.sendScreen(dump.writeDump(ex));
			return(b);
		}
		
		return(b);
	}

	public boolean Debug(){
		boolean b=false;
		String s = ServerCommand.Message(0, "Debug Ativado", "");
		transaction.dbg = vm.TRZ_DEBUG_ON;
		b=protocol.sendScreen(s);
		return(b);
	}
	
	public boolean DebugF8(){
		boolean b=false;
		transaction.dbg = vm.TRZ_DEBUG_OFF;
		b=this.GotoScreen();
		return(b);
	}
	
	public static String Hello() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" + "<mp>"
				+ "<gt cm='hello' rc='0'/>" + "</mp>";
		return (s);
	}
	
	public static String Message(int rc, String pMsg, String pData) {
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" + "<mp>"
				+ "<gt cm='msg' rc='"+rc+"' msg='"+pMsg+"' data='"+pData+"'/>" + "</mp>";
		return (s);
	}
	
	public static String DebugStepCont(int rc, String pMsg, String pRepName, int iLine) {
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" + "<mp>"
				+ "<gt cm='debug' rc='"+rc+"' msg='"+pMsg+"' repname='"+pRepName+"' line='"+iLine+"' cont='y'/>" + "</mp>";
		return (s);
	}
	
	public static String DebugStepEnd(int rc, String pMsg) {
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" + "<mp>"
				+ "<gt cm='debug' rc='"+rc+"' msg='"+pMsg+"' cont='n'/>" + "</mp>";
		return (s);
	}
	
	public static String SendVar(int rc, String pVar) {
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" + "<mp>"
				+ "<gt cm='prgvarr' rc='"+rc+"' data='"+pVar+"'/>" + "</mp>";
		return (s);
	}
	
	public boolean Logoff() {
		transaction=null;
		protocol.sendScreen("BYE");
		mSession.Close();
		return(false);
	}
		
	public void log(String s){
		System.out.println("\n"+s);
	}
	
	public boolean SavePrg(){
		boolean b=false;
		int rc;
		String msg;
		String sName,sTexto;
		sTexto=protocol.getServerCommand(ComponentsDictionary.DATA);
		sName=protocol.getServerCommand(ComponentsDictionary.NOME);
		sqlbase oSqlBase = new sqlbase();
		
		if(sName==null || sName.length()==0){
			rc=4;
			msg = "Falta nome do arquivo";
			b=protocol.Message(rc,msg,"");
			return(b);
		}
		
		try{
			sTexto=URLDecoder.decode(sTexto,"ISO-8859-1");
		}catch(Exception ex){ }
		program pg = new program();
		rc=pg.saveProgramDB(oSqlBase, sName,compiler.PRGS, "", sTexto);
		msg=pg.errmsg;
		
		if(rc==0){
			oSqlBase.commit();
		}else{
			oSqlBase.rollback();
		}
		b=protocol.Message(rc,msg,"");
		return(b);
	}

	public boolean LoadPrg(String sTip){
		boolean b=false;
		int rc=0;
		String msg;
		String sName,sTexto="";
		sName=protocol.getServerCommand(ComponentsDictionary.NOME);
		
		if(sName==null || sName.length()==0){
			rc=4;
			msg = "Falta nome do arquivo";
			b=protocol.Message(rc,msg,"");
			return(b);
		}
		
		program pg = new program();
		if(sTip.equals("P")){
			sTexto=pg.loadPogramDB2(sName,compiler.PRGS);
		}else{
			sTexto=pg.loadPogramDB2(sName,compiler.PRGC);
		}
		rc=pg.rc;
		if(rc==0){
			try{
				sTexto=URLEncoder.encode(sTexto,"ISO-8859-1");
			}catch(Exception ex){ }
		}
		msg=pg.errmsg;
		b=protocol.Message(rc,msg,sTexto);
		return(b);
	}

	public boolean CompPrg(){
		boolean b=false;
		int rc;
		String msg;
		String sName,sTexto;
		compiler cp = new compiler();
		sqlbase oSqlBase = new sqlbase();
		sTexto=protocol.getServerCommand(ComponentsDictionary.DATA);
		sName=protocol.getServerCommand(ComponentsDictionary.NOME);
		
		if(sName==null || sName.length()==0){
			rc=4;
			msg = "Falta nome do arquivo";
			b=protocol.Message(rc,msg,"");
			return(b);
		}
		
		try{
			sTexto=URLDecoder.decode(sTexto,"ISO-8859-1");
		}catch(Exception ex){ }
		
		program pg = new program();
		rc=pg.saveProgramDB(oSqlBase, sName,compiler.PRGS, "", sTexto);
		msg=pg.errmsg;
		
		if(rc==0){
			String s=sTexto;
			String s1=sName;
			String s2=""; //Descricao
			if(s1.length()>0){
				cp.compile(oSqlBase,s,s1,s2);
				rc=cp.rc;
				msg=cp.errmsg;
			}else{
				rc=4;
				msg="Informar nome do programa";
			}
		}
		if(rc==0){
			oSqlBase.commit();
		}else{
			oSqlBase.rollback();
		}
		b=protocol.Message(rc,msg,"");
		return(b);
	}
	
	public boolean GetVar(){
		boolean b=false;
		String s;
		s=protocol.getServerCommand(ComponentsDictionary.NOME);
		try{
			s=transaction.getVar(s);
		}catch(Exception ex) { s = "#N/A"; }
		b=protocol.sendScreen(ServerCommand.SendVar(0, s));
		return(b);
	}

}
