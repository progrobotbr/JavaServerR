package com.jsv.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.utils.Utils;
import com.jsv.dictionary.ComponentsDictionary;


public class ClientResponse {
	
	private static final int ERR = 4;
	private static final int SUCCESS = 0;
	
	private String mData;
	private ClientSession mSession;
	
	public void setSession(ClientSession pSession){
		mSession = pSession;
	}
	
	private void init(){
		
	}
	
    public void setResponseData(String pData){
    	mData = pData;
    	this.init();
    }
    
   public String getData(){
	   return(mData);
   }
   
   public String getServerCommand(){
		String s="";
		try{
			InputStream is = new ByteArrayInputStream(mData.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			Element oElem = doc.getDocumentElement();
			NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.GT);
			Node oNo1 = oNl1.item(0);
			s = Utils.getAttribute(oNo1,ComponentsDictionary.CM);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return(s);
	}
   
   public int getSubRc(){
	    int i=ERR; //Erro
		String s="";
		
		//Colocar no xml o subrc
		try{
			InputStream is = new ByteArrayInputStream(mData.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			Element oElem = doc.getDocumentElement();
			NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.GT);
			Node oNo1 = oNl1.item(0);
			s = Utils.getAttribute(oNo1,ComponentsDictionary.RC);
			i = Integer.parseInt(s);
		}catch(Exception ex){
			i = ERR;
			System.out.println(ex.toString());
		}
		return(i);
	}
   
    public String getAttribute(String pAttrib){
	    int i=ERR; //Erro
		String s="";
		
		//Colocar no xml o subrc
		try{
			InputStream is = new ByteArrayInputStream(mData.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			Element oElem = doc.getDocumentElement();
			NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.GT);
			Node oNo1 = oNl1.item(0);
			s = Utils.getAttribute(oNo1,pAttrib);
			//i = Integer.parseInt(s);
		}catch(Exception ex){
			i = ERR;
			Utils.log("Client:  " + ex.toString() + "\n"+Utils.writeStackTrace(ex));
		}
		return(s);
	}
    
    public Document parseModuloStru2() throws Exception {
		String s;
		s = mData;
		InputStream is = new ByteArrayInputStream(s.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(is);
		return (doc);
	}
}
