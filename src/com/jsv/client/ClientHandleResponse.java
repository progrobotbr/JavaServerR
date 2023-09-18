package com.jsv.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.utils.Utils;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.gui.components.rbutton;
import com.jsv.gui.components.rinput;
import com.jsv.gui.components.rlabel;
import com.jsv.gui.components.rtable;
import com.jsv.gui.components.rtabstrip;


public class ClientHandleResponse {

	public static final String ERR = "E";
	
	private String mGotoScreen="";
	private ClientBrowserTransaction Transaction;
	
	public ClientHandleResponse(ClientBrowserTransaction transaction){
		this.Transaction = transaction;
	}
	
	public static String loadResponse(ClientBrowserTransaction ptransaction){
		boolean b=false;
		String sGotoScreen;
		ClientHandleResponse oResponse = new ClientHandleResponse(ptransaction);
		b=oResponse.manageResponse();
		if(b==true){
			sGotoScreen = oResponse.getGotoScreen();
			ptransaction.ScreenName = sGotoScreen;
			return(sGotoScreen);
		}else{
			return(ClientHandleResponse.ERR);
		}
		
	}
	
	public String getGotoScreen(){
		return(mGotoScreen);
	}
	
	public boolean manageResponse(){
		int iTrzTp;
		Document doc;
		try{
			iTrzTp = Transaction.TransactionType; //(Integer) Transaction.Modulo.get(ComponentsDictionary.TRZTP);
			if( iTrzTp == ComponentsDictionary.TRZSERVER){ 
				doc = this.parseTelaData2();
				this.loadVariables(doc);
			}
		}catch(Exception ex){ 
			Utils.log("ClientManageResponse: " + Utils.writeStackTrace(ex));
			return(false);
		}
		
		return(true);
	}
	
	/*
	private Document parseTelaData() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(
				"C:\\Renato\\Pessoal\\Projetos\\RenR3\\Response.xml"));
		return (doc);
	}
	*/
	
	private Document parseTelaData2() throws Exception {
		String s;
		s = Transaction.Session.Response.getData();
		InputStream is = new ByteArrayInputStream(s.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(is);
		return (doc);
	}
	
	private void loadVariables(Document pDoc){
		
		// Variáveis
		int i, t;
		String sId;
		HashMap hScreen;
		Node oNo1, oNo3;
		NodeList oNl1, oNl2;
		Element oElem;
		
		oElem = pDoc.getDocumentElement();
		this.setGotoScreen(oElem);
		
		oNl1 = oElem.getElementsByTagName(ComponentsDictionary.SD);
		
		for (i = 0; i < oNl1.getLength(); i++) {
			
			oNo1 = oNl1.item(i);
			sId = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
			hScreen = (HashMap) Transaction.getMemScreen(sId); //(Transaction.ScreenName); //Transaction.Modulo.get(sId);
			
			oNl2 = oNo1.getChildNodes();
			
			for (t = 0; t < oNl2.getLength(); t++) {
				oNo3 = oNl2.item(t);
				this.clientPutDataElement(hScreen, oNo3);
			}
			
		}
		
	}
	
	private void setGotoScreen(Element pElem){
		
		Node oNo1;
		NodeList oNl1;
		oNl1 = pElem.getElementsByTagName(ComponentsDictionary.GT);
		oNo1 = oNl1.item(0);
		mGotoScreen = Utils.getAttribute(oNo1,ComponentsDictionary.CM);
		Transaction.ScreenName = mGotoScreen;
	}
	
	public void clientPutDataElement(HashMap pCollection, Node pNode){
		int i;
		String oNome;
		
		oNome = pNode.getNodeName();
		i = ComponentsDictionary.dictionaryElement(oNome);
		if (i == 0) { // Erro }
			return;
		}
		switch (i) {
		case 1:
			rlabel.ClientParseProtocolResponse(pCollection, pNode);
			//this.setVarLabel(pCollection, pNode);
			break;
		case 2:
			rinput.ClientParseProtocolResponse(pCollection, pNode);
			//this.setVarInput(pCollection, pNode);
			break;
		case 3:
			rbutton.ClientParseProtocolResponse(pCollection, pNode);
			//this.setVarButton(pCollection, pNode);
			break;
		case ComponentsDictionary.TABLE:
			//this.setVarTable(pCollection, pNode);
			rtable.ClientParseProtocolResponse(pCollection, pNode);	
			break;
		case ComponentsDictionary.TABSTRIP:
			rtabstrip.ClientParseProtocolResponse(Transaction, pCollection, pNode);
		}	
	}
	
	/*
	private void setVarLabel(HashMap pCollection,  Node pNode) {
		
		JLabel oLbl;
		
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		String sVl = Utils.getAttribute(pNode,ComponentsDictionary.VL);
		
		oLbl = (JLabel) pCollection.get(ComponentsDictionary.LBL_ + sId);
		if(oLbl == null) {
			Utils.log( ComponentsDictionary.LBL_ + sId + ": Não Existe");
			return;
		}
		oLbl.setText(sVl);
		
	}
	*/
	
	/*
	private void setVarInput(HashMap pCollection,  Node pNode) {
	    JTextField oInp;
		
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		String sVl = Utils.getAttribute(pNode,ComponentsDictionary.VL);
		
		oInp = (JTextField) pCollection.get(ComponentsDictionary.INP_ + sId);
		if(oInp == null) {
			Utils.log( ComponentsDictionary.INP_ + sId + ": Não Existe");
			return;
		}
		oInp.setText(sVl);
		
	}
	*/
	
	/*
	private void setVarButton(HashMap pCollection,  Node pNode) {
	    JButton oBut;
		
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		String sVl = Utils.getAttribute(pNode,ComponentsDictionary.VL);
		
		oBut = (JButton) pCollection.get(ComponentsDictionary.BUT_ + sId);
		if(oBut == null) {
			Utils.log( ComponentsDictionary.BUT_ + sId + ": Não Existe");
			return;
		}
		oBut.setText(sVl);
	}
	*/
}
