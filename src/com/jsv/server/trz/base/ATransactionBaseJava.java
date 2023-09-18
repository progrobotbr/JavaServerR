package com.jsv.server.trz.base;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import com.jsr.util._;
import com.jsv.data.Variant;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.server.config.srvBeanConfig;
import com.jsv.utils.Utils;

public class ATransactionBaseJava extends TransactionProtocolJava{
	
	private String  sidScreenToProcess  = "";
	//private boolean isRequest           = false;
	private int     rc                  = 0;
	private HashMap<String,AScreenBaseJava> hashScreen=new HashMap<String,AScreenBaseJava> ();
	public ITransactionJava transaction = null;
	
	public void init(String pTrzId) throws Exception{	
		this.sTrzId = pTrzId;
		this.initMemGlobal();
		this.getTransacationClass();
		if(this.getRC()!=0){
			throw(new Exception("Erro ao carregar transação: "+pTrzId));
		}
	}
	
	public void beforeProcess(){ }
	public void afterProcess(){ }
	
	public boolean process(){
		
		String sFirstScreen=this.sidScreenPointer;
		String sReq="";
		HashMap <String,String>scn = new HashMap<String,String>();
		AScreenBaseJava os;
		
		this.setScreenProcess();
		
		do{
		
			os = this.getScreenObj();
					
			if(os==null){
				return(false);
			}
		
			os.setNextScreen("");
			
			if(os.getIsFirstLoad()){
				os.onCreate(this);
				os.setIsFirstLoad(false);
				this.sidScreenToProcess="";
				if(os.hasNextScreen()){
					this.sidScreenToProcess = os.getNextScreen();
					continue;
				}
			}
			sReq=scn.get(os.getScreenId());
			if(this.checkIsRequest() && sFirstScreen.equals(os.getScreenId()) && sReq==null){
				os.onPAI(this);
				scn.put(os.getScreenId(), "X");
				this.sidScreenToProcess="";
				if(os.hasNextScreen()){
					this.sidScreenToProcess = os.getNextScreen();
					continue;
				}
			}
			os.onPBO(this);
			this.sidScreenToProcess="";
			if(os.hasNextScreen()){
				this.sidScreenToProcess = os.getNextScreen();
				continue;
			}
		
		
		}while(!this.sidScreenToProcess.equals(""));
		
		this.sidScreenPointer = os.getScreenId();
		
		return(true);
	}
	
	private boolean checkIsRequest(){
		String s = this.loadgVarS("OKCODE");
		if(s!=null && s.trim().length()>0){
			return(true);
		}
		return(false);
	}
	
	private String getIdScreenObj(){
		String s="com."+this.sidScreenToProcess;
		return(s);
	}
	
	private void setScreenProcess(){
		this.sidScreenToProcess = this.sidScreenPointer;
	}
	
	private String getIdTrzObj(){
		String s="trz"+this.sTrzId; //srvBeanConfig.pathRep + "trz\\" + this.sTrzId + "\\bin\\" + "trz" + this.sTrzId + srvBeanConfig.extPrgCompiled;
		return(s);
	}
	
	private AScreenBaseJava getScreenObj(){
		AScreenBaseJava os=null;
		this.rc = 4;
		os=this.hashScreen.get(this.sidScreenToProcess);
		if(os!=null){
			this.rc=0;
			return(os);
		}
		try{
			String s0 = this.sTrzId;
			String s1 = srvBeanConfig.pathRep + "trz\\" + this.sTrzId + "\\bin";
			String s2 = "scn"+this.sidScreenToProcess+s0;
			File root = new File(s1);
			URLClassLoader classLoader = URLClassLoader.newInstance( new URL[] { root.toURI().toURL() });
			Class<?> cl = Class.forName(s2, true, classLoader); 
			Object ob = cl.newInstance();
			os = (AScreenBaseJava) ob;
			os.setScreenId(this.sidScreenToProcess);
			os.setGlobalMemory(this.getGlobalMemory());
			this.hashScreen.put(this.sidScreenToProcess,os);
			this.rc=0;
		}catch(Exception ex){
			this.rc=4;
		}
		this.sidScreenToProcess="";
		return(os);
	}
	
	public AScreenBaseJava getScreenObj2(String pscreen){
		AScreenBaseJava os=null;
		this.rc = 4;
		os=this.hashScreen.get(pscreen);
		if(os!=null){
			this.rc=0;
			return(os);
		}
		try{
			String s0 = this.sTrzId;
			String s1 = srvBeanConfig.pathRep + "trz\\" + this.sTrzId + "\\bin";
			String s2 = "scn"+pscreen+s0;
			File root = new File(s1);
			URLClassLoader classLoader = URLClassLoader.newInstance( new URL[] { root.toURI().toURL() });
			Class<?> cl = Class.forName(s2, true, classLoader); 
			Object ob = cl.newInstance();
			os = (AScreenBaseJava) ob;
			os.setScreenId(pscreen);
			os.setGlobalMemory(this.getGlobalMemory());
			this.hashScreen.put(pscreen,os);
			this.rc=0;
		}catch(Exception ex){
			this.rc=4;
		}
		return(os);
	}
	
	private void getTransacationClass(){
		ITransactionJava it=null;
		try{
			String s0 = this.sTrzId;
			String s1 =  srvBeanConfig.pathRep + "trz\\" + this.sTrzId + "\\bin"; //\\trz" + s0 + srvBeanConfig.extPrgCompiled;
			File root = new File(s1);
			URLClassLoader classLoader = URLClassLoader.newInstance( new URL[] { root.toURI().toURL() });
			Class<?> cls = Class.forName("trz"+s0, true, classLoader); 
			Object ob = cls.newInstance();
			it = (ITransactionJava) ob;
			this.rc=0;
		}catch(Exception ex){
			this.rc=4;
			_.lg(ex.toString());
		}
		this.transaction = it;
	}
	
	public void clearRC(){
		this.rc=0;
	}
	
	public int getRC(){
		return(this.rc);
	}
	
	/*
	public boolean setScreen(){
		this.sidScreenPointer = Utils.getNodeValue(this.sProtocolXml, ComponentsDictionary.GT, ComponentsDictionary.VL);
		return(true);
	}
	*/
	public void gotoScreen(String sTela){
		this.sidScreenPointer=sTela;
		this.sidScreenToProcess=sTela;
	}
	
	public String getScreen(){
		return(this.sidScreenPointer);
	}
	
	//Eventos da transação
	public void execInitTransaction(){
		transaction.initTransaction(this);
	}
	public void execCreateVarScreenTransaction(){
		transaction.createVarScreen(this);
	}
	public void execEndTransaction(){
		transaction.endTransaction(this);
	}

}
