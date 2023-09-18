package com.jsv.trz.local;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.jsv.client.ClientProtocol;
import com.jsv.client.ClientRequest;
import com.jsv.client.ClientResponse;
import com.jsv.client.ClientSession;
import com.jsv.client.IServerCommand;
import com.jsv.client.desktop.DesktopDebug;
import com.jsv.client.desktop.IDesktop;
import com.jsv.utils.Utils;

public class trzbase {
	
	public Document mDoc;
	//public JLabel lbMsg;
	//public ClientProtocol mProtocol;
	public ClientSession Session;
	public StringBuilder sb;
	public int lastResponse;
	public String XMLResponse;
	public int RC=4;
	public IDesktop Desktop;
	
	trzbase(){
		Desktop = null;
		lastResponse = 0;
		sb = new StringBuilder();
		XMLResponse = "";
		Session=null;
	}
	public void print(String s){
		Desktop.setMessage(IDesktop.MSGOK, s);
	}
	
	public void clearMsg(){
		Desktop.setMessage(IDesktop.MSGCLEAR, null);
	}
	
	public void log(String s ){
		Utils.log(s);
	}
	
	public void generateXMLHeader(String pCommand, String pTrzId, String pScreen){
		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		sb.append("<mr id='"+pTrzId+"'>");
		sb.append("<gt cm='"+IServerCommand.GOTOSCR+"' vl='"+pScreen+"' okcode='"+pCommand+"'/>");
		sb.append("<sd id='" + pScreen + "'>");
	}
	
	public void generateXMLVarValue(String pTp, String pVar, String pValue){
		sb.append("<");
		sb.append(pTp);
		sb.append(" id='");
		sb.append(pVar);
		sb.append("' vl='");
		sb.append(pValue);
		sb.append("'/>");
	}
	
	public void generateXMLTableHeader(String pNameId, String pQtdColumn){
		sb.append("<tb id='"+pNameId+"' tq='"+pQtdColumn+"'>");
	}
	public void generateXMLTableHeaderClose(){
		sb.append("</tb>");
	}
	
	
	public void generateXMLTableLine(String pIdx){
		sb.append("<th ps='"+pIdx+"'>");
	}
	public void generateXMLTableLineClose(){
		sb.append("</th>");
	}
	
	public void generateXMLTableColumn(String pNameColumn, String pValue, String pIdx){
		String s="";
		try{
		  s = URLEncoder.encode(pValue,"ISO-8859-1");
		}catch(Exception ex){}
		
		sb.append("<tc id='"+ pNameColumn+ "' vl='"+s+"' ps='"+pIdx+"'/>");
	}
	
	public void generateXMLFooter(){
		sb.append("</sd></mr>");
	}
	
	public void executeRequest(){
		RC = 4;
		mDoc=null;
		String sXml = sb.toString();
		Utils.log(sXml);
		sb=new StringBuilder();
		ClientSession session   = Session; //mProtocol.getSession();
		ClientRequest request   = session.getRequest();
		ClientResponse response = session.getResponse();
		lastResponse=request.sendModuleProtocol(sXml);
		if(lastResponse==200){
			RC = response.getSubRc();
			XMLResponse = response.getData();
			mDoc = generateDoc();
		}
	}
	
	public Document generateDoc(){
		Document doc=null;
		try{
			InputStream is = new ByteArrayInputStream(XMLResponse.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			doc = docBuilder.parse(is);
		}catch(Exception ex){ }
		
		return(doc);
	}
	
	public Document getMem2(){
		Document doc=null;
		byte b[];
		try{
			b=Session.getResponse().getData().getBytes();
			InputStream is = new ByteArrayInputStream(b);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			doc = docBuilder.parse(is);
		}catch(Exception ex){ }
		
		return(doc);
	}
	
	public Document getMem(){
		return(mDoc);
	}
	
	public String getServerCommand(){
		String sCmd;
		ClientSession session;
		session = Session; //mProtocol.getSession();
		sCmd=session.getResponse().getServerCommand();
		return(sCmd);
	}
	
	/*
	public void loadDebug(HashMap<String, JPanel> pDesktop, trzlocal pTrzLocal, String pCommand){
		DesktopDebug odb = new DesktopDebug();
		odb.create3(pDesktop, mProtocol, pTrzLocal, pCommand);
	}
	*/
	
	//public


}
