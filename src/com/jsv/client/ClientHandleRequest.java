package com.jsv.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jsv.client.desktop.Desktop;
import com.jsv.client.desktop.DesktopDebug;
import com.jsv.client.desktop.DesktopDump;
import com.jsv.client.desktop.DesktopEditor;
import com.jsv.client.desktop.IDesktop;
import com.jsv.client.main.ClientMain;
import com.jsv.client.validate.ValidRegex;
import com.jsv.utils.Utils;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.gui.components.rinput;
import com.jsv.gui.components.rtable;
import com.jsv.gui.components.rtabstrip;

public class ClientHandleRequest implements ActionListener, AdjustmentListener, ChangeListener {
	
	 private ClientBrowserTransaction Transaction;
	 private String  mOkCode;
	 private String  mXML="";
	 
	 public boolean enable = true;
	  
	 public ClientHandleRequest(ClientBrowserTransaction transaction){
		 mOkCode = "";
		 this.Transaction = transaction;
	 }
	 
	 //Events handler
	 //Botoes
	 public void actionPerformed(ActionEvent ae) {
		 mOkCode = ae.getActionCommand();
		 Utils.log(ae.getActionCommand()); 
		 /*
		  *  Operação Principal 
		  */
		 this.HandleAction();
	 } 
	 
	 //JScrollBar
	 public void adjustmentValueChanged(AdjustmentEvent arg0) {
	     String sName;
	     if(arg0.getValueIsAdjusting()==false){
	    	 JScrollBar jsb = (JScrollBar)arg0.getSource();
	    	 sName=jsb.getName();
	    	 if(sName!="" && sName.trim().length()>0){
	    		 mOkCode = sName;
	    		 Utils.log(sName);
	    		 /*
	    		  *  Operação Principal 
	    		  */
	    		 this.HandleAction();
	    	 }
	     }
	 }
	 
	//Para TabStrip
	public void stateChanged(ChangeEvent changeEvent) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		if(enable){
			mOkCode=sourceTabbedPane.getName();
			int index = sourceTabbedPane.getSelectedIndex();
			this.HandleAction();
			Utils.log("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
		}else{
			Utils.log("Sem submissão");
		}
	}

	 //Fim dos events handlers
	 
		 	 
	 /*
	  * 
	  * Rotina princiapal
	  * 
	  */
	 public void HandleAction(){
		 boolean b=false;
		 String sCmd="";
		 mXML = "";
		
		 if(mOkCode.length()>0){
			 mOkCode = mOkCode.substring(13,mOkCode.length()).toUpperCase();
			 if(mOkCode.equals("QUIT")){
				 this.logoff();
				 return;
			 }
		 }else{
			 
		 }
		 try{
			 this.generateXML();    //Transforma a tela atual em xml
			 b=true;
		 }catch(Exception ex){
			 Utils.log("Cliente ClientHandleRequest: " + Utils.writeStackTrace(ex));
		 }
		 if(b==true){
			 this.executeRequest(); //Executa o envio
			 sCmd=Transaction.Session.getResponse().getServerCommand();
			 if(sCmd.equals("debug")){
				 this.loadDebug();
			 }else{
				 this.loadResponse();   //Carrega a tela
			 }
		 }
		 
	 }
	  
	 public void generateXML() throws Exception{
		 boolean b=true;
		 String sTrzId, sScrName, sKey, sTpObj, sIdObj, sXml="";
		 
		 StringBuilder sb = new StringBuilder();
		 HashMap hScreen, hKey;
		 sTrzId         = Transaction.TransactionName; //(String)  Transaction.Modulo.get(ComponentsDictionary.TRZID);
		 sScrName       = Transaction.ScreenName;
		 hScreen        = (HashMap) Transaction.getMemScreen(Transaction.ScreenName);
		 
		 sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		 sb.append("<mr id='"+sTrzId+"'>");
		 sb.append("<gt cm='"+IServerCommand.GOTOSCR+"' vl='"+sScrName+"' okcode='"+mOkCode+"'/>");
		 sb.append("<sd id='" + sScrName + "'>");
		 
		 Iterator iter = hScreen.keySet().iterator();
		 while(iter.hasNext()){
			 sKey = (String) iter.next();
			 sTpObj = this.getTpObj(sKey);
			 sIdObj = this.getIdObj(sKey);
			 try{
				 sXml = this.getObjXml(hScreen,sTpObj,sIdObj);
			 }catch(Exception ex){
				 b=false;
			 }
			 sb.append(sXml);
		 }
		 
		 if(b==false){
			 throw(new Exception("Erro validação tela"));
		 }else{
			 Transaction.Desktop.setMessage(IDesktop.MSGCLEAR, null);
		 }
		 
		 sb.append("</sd></mr>");
		 
		 mXML = sb.toString();
		 Utils.log(mXML);
	 }	
	 
	 private String getNrScreen(String pId){
		 return(pId.substring(5,9));
	 }
	 
	 private String getIdObj(String pId){
		 int i;
		 i = pId.length();
		 return(pId.substring(4,i));
	 }
	 
	 private String getTpObj(String pId){
		 return(pId.substring(0,4));
	 }
	 
	 public String getObjXml(HashMap pScreen, String pTpObj, String pIdObj) throws Exception{
		 String s="",v,v2;
		 if(pTpObj.equals(ComponentsDictionary.INP_)){
			 v2 = ComponentsDictionary.INP_ + pIdObj;
			 JTextField oTx = (JTextField) pScreen.get(v2);
			 v = oTx.getText();
			 if(!ValidRegex.validInt(v)){
				 Transaction.Desktop.setMessage(IDesktop.MSGERR, "O campo "+pIdObj+" deve ser numérico inteiro");
				 oTx.setForeground(Color.RED);
				 throw(new Exception(""));
			 }else{
				 oTx.setForeground(Color.BLACK);
			 }
			 //s = "<if id='" + pIdObj + "' vl='" + v + "'/>";
			 s = rinput.ClientGenerateProtocolRequest(pIdObj, v);
		 }else if(pTpObj.equals(ComponentsDictionary.TAB_)){
			 v2 = ComponentsDictionary.TAB_ + pIdObj;
			 JTable tb = (JTable) pScreen.get(v2);
			 v2 = ComponentsDictionary.TAS_ + pIdObj;
			 JScrollBar jsb = (JScrollBar) pScreen.get(v2);
			 v2 = ComponentsDictionary.TAM_ + pIdObj;
			 String smeta[][] = (String[][]) pScreen.get(v2);
			 s = rtable.ClientGenerateProtocolRequest(tb, jsb, smeta, pIdObj);
	 	 }else if(pTpObj.equals(rtabstrip.SPT_)){
	 		 v2 = rtabstrip.SPT_ + pIdObj;
	 		 rtabstrip rtp = (rtabstrip) pScreen.get(v2);
	 		 s = rtabstrip.ClientGenerateProtocolRequest(rtp);
	 	 }
		 //else if(...)
		 return(s);
	 }
	 
	 public void executeRequest(){
		 int i;
		 String sXml="";
		 ClientRequest request   = Transaction.Session.getRequest();
		 ClientResponse response = Transaction.Session.getResponse();
		 i=request.sendModuleProtocol(mXML);
		 if(i==200){
		   sXml = response.getData();
		 }
		 Utils.log(sXml);
	 }
	 
	 public boolean loadResponse(){
		 boolean b=false;
		 String sGotoScreen;
		 try{
			 sGotoScreen = ClientHandleResponse.loadResponse(Transaction);
			 if(!sGotoScreen.equals(ClientHandleResponse.ERR)){
				 try{
					 
					 Transaction.setScreen();
					 
				 }catch(Exception ex){
					 Transaction.ErrorDump =  "Client ClientHandleRequest: " + Transaction.Session.getResponse().getData()+"\n"+Utils.writeStackTrace(ex);
					 DesktopDump dd = new DesktopDump();
					 dd.create(Transaction);
					 Transaction.setDesktopInWindow();
					 return(false);
				 }
			 }else{
				 DesktopDump dd = new DesktopDump();
				 Transaction.ErrorDump = Transaction.Session.getResponse().getData();
				 dd.create(Transaction);
				 Transaction.setDesktopInWindow();
				 return(false);
			 }
				 
			 b=true;
			 
		 }catch(Exception ex) { 
			 Utils.log("Client: " + Utils.writeStackTrace(ex)); 
			 b=false;
		 }
		 
		 return(b);
		 
	 }
	 	
	 public void logoff(){
		 ClientRequest request = Transaction.Session.getRequest();
		 request.logoff();
	 }
	 
	 public void loadDebug(){
		 DesktopDebug odb = new DesktopDebug();
		 //odb.create2(mModulo, mSession);
		 
	 }
     
}
