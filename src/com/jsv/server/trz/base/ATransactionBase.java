package com.jsv.server.trz.base;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.lang.program.line;
import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;
import com.jsv.lang.vm.vm;
import com.jsv.server.ServerCommand;
import com.jsv.server.screen.ProtocolV1;
import com.jsv.server.vm.executor.VMExecutor;
import com.jsv.utils.Utils;
import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.dictionary.ComponentsDictionary;

public abstract class ATransactionBase extends MemoryOp {
	
	private static final String cDIR = "C:\\Renato\\Pessoal\\Projetos\\RenR3\\";
	private static final String SCREEN = "SCREEN";
	
	public int ps=0;                   //passo
	public int st=0;                   //step
	public int ft=vm.TRZ_FIRST_CALL;   //primeira vez
	public int dbg = vm.TRZ_DEBUG_OFF; //debug
	public int lt=0;                   //último comando
	public int sc=0;                   //mudou a tela
	private String  mTrzId;
	private String  mnrScreenTempPai="";
	private String  mnrScreen="";
	private HashMap<String,Variant> mMemGlobal;
	private HashMap mScreen;
	private String  mScreenWork;
	private String  mStru;
	private VMExecutor mVmexecutor;
	private sqlbase mSqlBase;
	private boolean isFirstTime=true;
	
	public ATransactionBase(){
		mMemGlobal=new HashMap();
		mScreen=new HashMap();
		mScreenWork="";
		mVmexecutor = new VMExecutor();
		mSqlBase = new sqlbase();
		mVmexecutor.setDB(mSqlBase);
		//super.setGlobalMemory(mMemGlobal);
	}
	
	//Clear Error
	public void clearErr(){
		mVmexecutor.clearErr();
	}
	//Transação
	public void begintran(){
		mSqlBase.beginTransaction();
		mSqlBase.clearRcSql();
	}
	public void commit(){
		if(mSqlBase.getRcSql()==0 && ps != 20){
			mSqlBase.commit();
		}else{
			mSqlBase.rollback();
		}
	}
	public void rollback(){
		mSqlBase.rollback();
	}
	//Fim Transação
	
	public void createVar(String pNome, boolean pBol){
		Variant v1 = new Variant(pBol);
		mMemGlobal.put(pNome,v1);
	}
	
	public void putScreen(String pScreenId, Screen pSc){
		mScreen.put(pScreenId, pSc);
	}
	
	//Connect a memória
	public void connectVMMemory(){
		mMemGlobal = mVmexecutor.getMemGlobal();
		super.setGlobalMemory(mMemGlobal);
	}
	
	public HashMap<String,Variant> getGlobalMemory(){
		return(mMemGlobal);
	}
	
	public int setScreen(){
		int i=0;
		Variant v;
		if(mMemGlobal.containsKey(ATransactionBase.SCREEN)){
			v = mMemGlobal.get(ATransactionBase.SCREEN);
			mnrScreen = v.getString();
		}else{
			v = new Variant("-1");
			mMemGlobal.put(ATransactionBase.SCREEN, v);
		}
		return(i);
	}
	
	public String getVMScreen(){
		String s="";
		Variant v;
		if(mMemGlobal.containsKey(ATransactionBase.SCREEN)){
			v = mMemGlobal.get(ATransactionBase.SCREEN);
			s = v.getString();
		}else{
			s=mnrScreen;
		}
		return(s);
	}
	
	public boolean isVMFirstLoadScreen(){
		boolean b;
		int i;
		Variant v;
		String s="SCN"+this.getVMScreen();
		v = mMemGlobal.get(s);
		i = v.i;
		if(i==0){
			b=true;
		}else{
			b=false;
		}
		return(b);
	}
	
	public void setVMScreenFirstExecuted(){
		String s = "SCN" + this.getVMScreen();
		Variant v = new Variant(1);
		mMemGlobal.put(s,v);
	}
	
	public Screen getScreen(String pScreenId){
		return((Screen)mScreen.get(pScreenId));
	}
	
	public void setId(String pId){
		mTrzId = pId;
	}
	
	public String getId(){
		return(mTrzId);
	}
	
	public void setNrScreen(String s){
		mnrScreen = s;
	}
	
	public String getNrScreen(){
		return(mnrScreen);
	}
	public boolean isFirstTime(){ return(isFirstTime); }
	
	public void setNotFirstTime() { isFirstTime = false; }
	
	public void setScreen(String pScreenWork){
		mScreenWork = pScreenWork;
	}
	
	public String getVar(String s){
		String s1;
		s1 = mVmexecutor.getVar(s);
		return(s1);
	}
	//Comando que executa ação e processa tela 
	public void gotoScreen(String pScreenWork){
		/*
		 * Quando mover a tela, deverá executar o evento de onCreate e onProcessoBeforeOutput
		 */
		mScreenWork = pScreenWork;
		Screen os = (Screen) mScreen.get(pScreenWork);
		if(os.isFirstTime()){
			os.onCreate();
		}
		os.onProcessBeforeOutput();
	}
	
	public boolean processScreenEvent(){
		boolean b=false;
		try{
			Screen os = (Screen) mScreen.get(mScreenWork);
			if(os.isFirstTime()){
				os.onCreate();
				os.onProcessBeforeOutput();
				os.setNotFirstTime();
			}else{
				os.onProcessAfterInput();
				os.onProcessBeforeOutput();
			}
		}catch(Exception ex){ b=false; log(ex.toString());}
		return(b);
	}
	
	public void log(String s){
		System.out.println("Trz:"+mTrzId+">"+s);
	}
	
	//Retorna a estrutura da transação
	public String getStructure(){
		String s="",s1;
		try{
			if(mTrzId.equals("TL01") || mTrzId.equals("USRL")){
				//s1 = "C:\\Rep\\" + mTrzId + "\\" + "str" + mTrzId + ".xml";
				s1 = "C:\\Renato\\programas\\rserver\\Rep\\" + mTrzId + "\\" + "str" + mTrzId + ".xml";
				FileReader f = new FileReader(s1);
				BufferedReader in = new BufferedReader(f);
				StringBuilder sb = new StringBuilder();
				String str;
				while ((str = in.readLine()) != null) {
					sb.append(str);
				}
				in.close();
				s = sb.toString();
			}else{
				ProtocolV1 oP1 = new ProtocolV1();
				oP1.setSqlDB(mSqlBase);
				s = oP1.makeP1(mTrzId);
			}
		}catch(Exception ex){
			log(ex.toString());
		}
		return(s);
	}
	
	//Retorna o protocol da tela para o usuario
	public String getResponseOfScreen(){
		String s="";
		try{
			//Screen os = (Screen) mScreen.get(mScreenWork);
			if(ps==20){
				s=this.getError();
				log(s);
				return(s);
			}
			if(dbg==vm.TRZ_DEBUG_ON){
				s=this.getResponseDebug();
				return(s);
			}
			s = this.getVMScreen();
			Screen os = new Screen();
			os.setTrzId(this.getId());
			os.setScreenId(s);
			os.setGlobalMemory(this.getGlobalMemory());
			os.setSqlDb(this.mSqlBase);
			s = os.getRespScreenProtocol();
		}catch(Exception ex) { s = ex.toString(); }
		return(s);
	}
	
	public String getResponseDebug(){
		int i;
		String s="",sRepName;
		line l;
		l=mVmexecutor.getLineRead();
		//O nome do programa vem em l.lineData da seguinte forma:
		//  nome do programa:linha com instrução
		sRepName = l.lineData;
		i=sRepName.indexOf(':');
		sRepName = sRepName.substring(0,i);
		//s=ServerCommand.DebugStepCont(0, "Linha de execução", l.lineData, l.fisicalLine);
		s=ServerCommand.DebugStepCont(0, "Linha de execução", sRepName, l.fisicalLine);
		return(s);
	}

	public boolean putRequestOfScreen(Document doc){
		boolean b=false;
		int i, t, tv;
		String sId, sVl, sName;
		Node oNo1, oNo2;
		NodeList oNl1, oNl2;
		Element oElem;
		Variant v;
		
		if(ps!=1){ return(true); } //Devido ao debug
		
		oElem = doc.getDocumentElement();
		
		oNl1 = oElem.getElementsByTagName(ComponentsDictionary.GT);
		oNo1 = oNl1.item(0);
		sVl = Utils.getAttribute(oNo1,"okcode");
		v = mMemGlobal.get("OKCODE");
		if(v!=null){
			v.s=sVl;
		}
		
		oNl1 = oElem.getElementsByTagName(ComponentsDictionary.SD);
		
		for (i = 0; i < oNl1.getLength(); i++) {
			
			oNo1 = oNl1.item(i);
			sId = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
			
			oNl2 = oNo1.getChildNodes();
			
			for (t = 0; t < oNl2.getLength(); t++) {
				oNo2 = oNl2.item(t);
				sName = oNo2.getNodeName();
				sId = Utils.getAttribute(oNo2,ComponentsDictionary.ID);
				sVl = Utils.getAttribute(oNo2,ComponentsDictionary.VL);
				v = mMemGlobal.get(sId);
				if(v==null) { continue; }
				switch(v.type){
				case Variant.STRING:
					v.s=sVl;
					break;
				case Variant.INT:
					tv = Integer.parseInt(sVl);
					v.i=tv;
					break;
				case Variant.TABLE: 
					this.transferTable(v,oNo2);
				}
			}
			
		}
		return(b);
	}
	
	//Rotina de Tabela
	public void transferTable(Variant pv, Node pNode){
		int i,t;
		String s, stcid, stcvl;
		Variant vv;
		structure st;
		table tb;
		Node no1,no2;
		NodeList nl1,nl2;
		
		tb=pv.tb;
		tb.clearTable();
		st=tb.getHeader();
		nl1=pNode.getChildNodes();
		for(i=0;i<nl1.getLength();i++){
			no1=nl1.item(i);
			s=no1.getNodeName();
			if(s.equals(ComponentsDictionary.TH)){
				nl2=no1.getChildNodes();
				st=st.clone();
				st.clear();
				for(t=0;t<nl2.getLength();t++){
					no2 = nl2.item(t);
					stcid=Utils.getAttribute(no2,ComponentsDictionary.ID);
					stcvl=Utils.getAttribute(no2,ComponentsDictionary.VL);
					try{
					stcvl=URLDecoder.decode(stcvl,"iso-8859-1");
					}catch(Exception ex){}
					vv=st.get(stcid);
					vv.moveStringToInternal(stcvl);
				}
				tb.append(st,null);
			}
		}
		
	}

	//Screen
	public String getScreenName(){
		return("");
	}
	
	//vmc
	public int load(String s){
		int i;
		String s1 = "trz"+s;
		this.log(s1);
		i=mVmexecutor.load(s1);
		if(i==0){
			i=mVmexecutor.execMain();
			if(i==vm.PRG_END_WITH_SUCCESS){
				this.connectVMMemory(); //Mapeia a memória da vm para a transação
			}
		}
		return(i);
	}
	
	//Fluxo de processamento
	public int trzCRT(){
		int i;
		i=mVmexecutor.execSub("CREATE");
		return(i);
	}
	public int trzCRT0Deb(){
		int i;
		i=mVmexecutor.execLineCallSub("CREATE");
		return(i);
	}
	
	//Método genérico para processar linha-a-linha
	public int xxxGEN1Deb(){
		int i;
		i=mVmexecutor.execLine();
		return(i);
	}
	
	public int trzPBO(){
		int i;
		i=mVmexecutor.execSub("PROCESSBEFOREOUTPUT");
		return(i);
	}
	
	public int trzPBO0Deb(){
		int i;
		i=mVmexecutor.execLineCallSub("PROCESSBEFOREOUTPUT");
		return(i);
	}
	
	public int trzPAI(){
		int i;
		i=mVmexecutor.execSub("PROCESSAFTERINPUT");
		return(i);
	}
	
	public int trzPAI0Deb(){
		int i;
		mVmexecutor.setIniScreenCommand();
		i=mVmexecutor.execLineCallSub("PROCESSAFTERINPUT");
		return(i);
	}
	
	public int scnCRT(){
		int i;
		String scr, scmd;
		scr = this.getVMScreen();
		scmd = "SCN"+scr+mTrzId+"->CREATE";
		i=mVmexecutor.execSub(scmd);
		return(i);
	}
	
	public int scnCRT0Deb(){
		int i;
		String scr, scmd;
		scr = this.getVMScreen();
		scmd = "SCN"+scr+mTrzId+"->CREATE";
		i=mVmexecutor.execLineCallSub(scmd);
		return(i);
	}
	
	public int scnPBO(){
		int i;
		String scr, scmd;
		scr = this.getVMScreen();
		scmd = "SCN"+scr+mTrzId+"->PROCESSBEFOREOUTPUT";
		i=mVmexecutor.execSub(scmd);
		return(i);
	}
	
	public int scnPBO0Deb(){
		int i;
		String scr, scmd;
		scr = this.getVMScreen();
		scmd = "SCN"+scr+mTrzId+"->PROCESSBEFOREOUTPUT";
		i=mVmexecutor.execLineCallSub(scmd);
		return(i);
	}
	
	public int scnPAI(){
		int i;
		String scr, scmd;
		scr = this.getVMScreen();
		scmd = "SCN"+scr+mTrzId+"->PROCESSAFTERINPUT";
		i=mVmexecutor.execSub(scmd);
		return(i);
	}
	
	public int scnPAI0Deb(){
		int i;
		String scr, scmd;
		scr = this.getVMScreen();
		scmd = "SCN"+scr+mTrzId+"->PROCESSAFTERINPUT";
		i=mVmexecutor.execLineCallSub(scmd);
		return(i);
	}
		
	public boolean process(){
		boolean b=false;
		int i=vm.TRZ_PROCESS_START;
		//try{
			do{
				/*
				 * Aqui a ordem dos if's são importantes, pois if pode soltar o ps=1 e deverá
				 *  ser imediatamente capturado pelo segundo if. Se não for capturado, o processamento
				 *  será finalzado
				 *  ps=1  -> início de ciclo
				 *  ps=20 -> ocorreu erro no processamento 
				 */
				if(ft==vm.TRZ_FIRST_CALL){
					//Utilizado para chamar o método Create da Transação
					i=this.processDialogDebugCreate(); //Depois colocar esta lógica dentro do processDialogDebug()
					if(ps==20) { i=vm.PRG_PROCESS_ERROR; b=false; break; }
				}
				if(ft!=vm.TRZ_FIRST_CALL){
					i=this.processDialogDebug();
				}
			}while(dbg!=vm.TRZ_DEBUG_ON && ps!=1 && ps!=20);
			b=true;
		//}catch(Exception ex) { 
			//i=vm.PRG_PROCESS_ERROR; 
			//b=false;
			//this.log(ex.toString());
		//}
		lt=i;
		return(b);
	}
	
	/*
	public boolean process(){
		boolean b=false;
		int i=vm.TRZ_PROCESS_START;
		try{
			if(ft==vm.TRZ_FIRST_CALL){
				if(dbg==vm.TRZ_DEBUG_ON){
					i=this.processDialogFirstDebug();
				}else{
					i=this.processDialogFirst();
				}
			}else{
				if(dbg==vm.TRZ_DEBUG_ON){
					i=this.processDialogDebug();
				}else{
					i=this.processDialog();
				}
			}
			b=true;
		}catch(Exception ex) { i=vm.PRG_PROCESS_ERROR; b=false;}
		lt=i;
		return(b);
	}
	*/
	
	public int processDialogFirst(){
		int i=vm.TRZ_STEP_START;
		st=1;
		if(ps==1);i=this.trzCRT(); if(i!=vm.PRG_END_WITH_SUCCESS) { return(i);} ps++; this.ft = vm.TRZ_PLUS_CALL;
		if(ps==2);i=this.trzPBO(); if(i!=vm.PRG_END_WITH_SUCCESS) { return(i);} ps++;
		if(ps==3);i=this.scnSTP(); if(i!=vm.PRG_END_WITH_SUCCESS) { return(i);} ps=1;
		
		return(i);
	}
	
	public int processDialog(){
		int i=vm.TRZ_STEP_START;
		st=2;
		if(ps==1);i=this.trzPAI(); if(i!=vm.PRG_END_WITH_SUCCESS) { return(i);} ps++;
		if(ps==2);i=this.scnSTP(); if(i!=vm.PRG_END_WITH_SUCCESS) { return(i);} ps++;
		if(ps==3);i=this.trzPBO(); if(i!=vm.PRG_END_WITH_SUCCESS) { return(i);} ps=1;
		ps=1;
		return(i);
	}
	
	//Debug
	public int processDialogFirstDebug(){
		int i=vm.TRZ_DEBUG_STEP_START;
		st=3;
		switch(ps){
			case 1: i=this.trzCRT0Deb();
			        if(i==vm.PRG_END_WITH_SUCCESS) { ps=2; }
			        break;
			case 2: i=this.xxxGEN1Deb(); 
			        if(i==vm.PRG_END_WITH_SUCCESS) { ps=3; this.ft = vm.TRZ_PLUS_CALL; }
			        break;
			case 3: i=this.trzPBO0Deb();
			        if(i==vm.PRG_END_WITH_SUCCESS) { ps=4; }
			        break;
			case 4: i=this.xxxGEN1Deb();
			        if(i==vm.PRG_END_WITH_SUCCESS) { ps=5; }
			        break;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:  i=this.scnSTP0Deb(); 
			         break;
		}
		switch(ps){
			case 10: ps=1;
				     dbg=vm.TRZ_DEBUG_OFF;
				     break;
			case 20: //Erro
				     break;
		}
		return(i);
	}
	
	public int processDialogDebugCreate(){
	    int i=vm.TRZ_DEBUG_STEP_START;
		switch(ps){
		case 1: i=this.trzCRT0Deb();
		        if(i==vm.PRG_LINE_SUCCESS) { ps=2; } else { ps=20; }
		        break;
		case 2: i=this.xxxGEN1Deb();
		        if(i==vm.PRG_END_WITH_SUCCESS) { ps=10; ft=vm.TRZ_PLUS_CALL; } else { if(i!=vm.PRG_LINE_SUCCESS) { ps=20;} }
		        break;
		}
		return(i);
	
	}
	
	public int processDialogDebug(){
		int i=vm.TRZ_DEBUG_STEP_START;
		st=4;
		switch(ps){
			case 1: i=this.trzPAI0Deb();
			        if(i==vm.PRG_LINE_SUCCESS) { ps=2; }
			        break;
			case 2: i=this.xxxGEN1Deb();
			        if(i==vm.PRG_END_WITH_SUCCESS) { ps=3; }
			        break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:  i=this.scnSTP0Deb();
			         break;
			case 10: i=this.trzPBO0Deb();
			         if(i==vm.PRG_LINE_SUCCESS) { ps=11; }
			         break;
			case 11: i=this.xxxGEN1Deb();
			         if(i==vm.PRG_END_WITH_SUCCESS) { ps=1; dbg=vm.TRZ_DEBUG_OFF; }
			         break;
		}
		if(ps==20){
			//Erro
		}
		return(i);
	}
	
	public int scnSTP(){
		boolean b;
		int i=0;
		//s conterá tela de processamento anterior e sNew conterá a tela que foi feita o GOTO
		String s,sNew; 
		
		/*
		 * Se for 3, veio do ProcessFirst
		 * Se for 2, veio do ProcessNormal
		 */
		switch(ps){
			case 2: ps = 5;      //Vai para o PAI
			        break;
			case 3: b=this.isVMFirstLoadScreen();
			        if( b==true ){
						ps=4;    //Vai para o CRT
					}else{
						ps=6;    //Vai para o PBO
					}
					break;
		}
		
		while(ps<7){
			s = this.getVMScreen();
			switch(ps){
				case 4: b=this.isVMFirstLoadScreen();
				        if(b!=true){
				        	ps=6;
				        	break;
				        }
					    i=this.scnCRT();
						if(i==vm.PRG_END_WITH_SUCCESS){
							this.setVMScreenFirstExecuted();
							ps=6;  //Manda para o PBO
						}else{
							ps=20; //Erro
							return(i);
						}
						break;
				case 5: i=this.scnPAI();
						if(i==vm.PRG_END_WITH_SUCCESS){
							ps=6;  //Manda para o PBO
						}else{
							ps=20; //Erro
							return(i);
						}
						break;
				case 6: i=this.scnPBO();
						if(i==vm.PRG_END_WITH_SUCCESS){
							ps=7;  //Saída
						}else{
							ps=20; //Erro
							return(i);
						}
						break;
			}
			
			/*
			 * Se s <> sNew, significará que a tela de trabalho mudou,
			 *    portanto deverá começar novamente a análise do CRT, PBO
			 */
			sNew = this.getVMScreen();
			if(!s.equals(sNew)){
				ps=4;
			}
		}
		
		return(i);
	}
	
	//Processa screen em debug
	public int scnSTP0Deb(){
		boolean b=false;
		int i=0;
		String s, sNew;
		
		try{
			b=this.isVMFirstLoadScreen();
		}catch(Exception ex){
			ps=20;
			s="Erro na chamada da Tela: SCN("+this.getVMScreen()+")";
			mVmexecutor.setError(s);
			return(vm.PRG_PROCESS_ERROR);
		}
		s = this.getVMScreen();
		
		if( ps==5 ){
			if( b==true ){
				ps=6;
			}else{
				ps=8;
			}
		}
			
		switch(ps){
		case 3: //PAI
			i=this.scnPAI0Deb();
			if(i==vm.PRG_LINE_SUCCESS){
				ps=4;
			}else{
				ps=20;
				return(i);
			}
			break;
		case 4:
			i=this.xxxGEN1Deb();
			if(i!=vm.PRG_LINE_SUCCESS && i!=vm.PRG_END_WITH_SUCCESS){
				ps=20;
				return(i);
			}
			if(i==vm.PRG_END_WITH_SUCCESS){
				ps=5;
				if(sc==1){ ps=6; sc=5; }
			}
			break;
		case 6: //CREATE
			//Verifica se o create já foi executado
			if(this.isVMFirstLoadScreen()==false){
				ps=8;
				return(vm.PRG_LINE_SUCCESS);
			}
			i=this.scnCRT0Deb();
			if(i==vm.PRG_LINE_SUCCESS){
				ps=7;
			}else{
				ps=20;
				return(i);
			}
			break;
		case 7: 
			i=this.xxxGEN1Deb();
			
			if(i!=vm.PRG_LINE_SUCCESS && i!=vm.PRG_END_WITH_SUCCESS){
				ps=20;
				return(i);
			}
			if(i==vm.PRG_END_WITH_SUCCESS){
				ps=8;
				this.setVMScreenFirstExecuted();
				if(sc==1){ ps=6; sc=0; }
			}
			break;
		case 8: //PBO
			i=this.scnPBO0Deb();
			if(i==vm.PRG_LINE_SUCCESS){
				ps=9;
			}else{
				ps=20;
				return(i);
			}
			break;
		case 9: 
			i=this.xxxGEN1Deb();
			if(i!=vm.PRG_LINE_SUCCESS && i!=vm.PRG_END_WITH_SUCCESS){
				ps=20;
				return(i);
			}
			if(i==vm.PRG_END_WITH_SUCCESS){
				ps=10;
				if(sc==1){ ps=6; sc=0; } 
			}
			break;
		}
		
		/*
		 * Se s <> sNew, significará que a tela de trabalho mudou,
		 *    portanto deverá começar novamente a análise do CRT, PBO
		 */
		sNew = this.getVMScreen();
		if(!s.equals(sNew)){
			//ps=6;
			sc=1; //Indica que a tela mudou
		}
		return(i);
	}
	
	public String getError(){
		line l;
		l = mVmexecutor.getError();
		String s="<?xml version='1.0' encoding='ISO-8859-1'?><error><linnr>"+l.fisicalLine+"</linnr><linsrc>"+l.lineData+"</linsrc></error>";
		return(s);
	}
	
}
