package com.jsv.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.utils.Utils;
import com.jsv.data.SData;
import com.jsv.dictionary.ComponentsDictionary;

public class ServerRequest {
	
	private InputStream mIs;
	private String mData;
	
	public ServerRequest(InputStream pIs){
		mIs = pIs;
	}
	
	public String getData(){
		return(mData);
	}
	
	public void newRequest(byte[] y,int i){
		boolean b=false;
		byte[] y2;
		StringBuilder sb = new StringBuilder();
		y2 = new byte[i];
		
		mData="";
		System.arraycopy(y,0,y2,0,i);
		sb.append(new String(y2));
	    
		try{
			if(mIs.available()>0){
				b=this.recv();
			}
			if(b){
				sb.append(mData);
			}
			mData = sb.toString();
			
		}catch(Exception ex){Utils.log(ex.toString());}	
	}
	
	private  boolean recv(){
		boolean bRet = false;
		int i=1;
		byte[] y = new byte[512];
		byte[] y2;
		StringBuilder sbData = new StringBuilder();
		
		try{
			i = mIs.available();
			while(i > 0){
			  i=mIs.read(y);
			  if(i>0){
			    y2 = new byte[i];
			    System.arraycopy(y,0,y2,0,i);
			    sbData.append(new String(y2));
			  }
			  if(mIs.available()==0){
				  i=0;
			  }
			  mData = sbData.toString();
			  bRet=true;
			}
		}catch(Exception ex){Utils.log(ex.toString());}	
		return(bRet);
	}
	
	public boolean isAvailable(){
		boolean b;
		b = this.recv();
		return(b);
	}
	
	public Document getDoc(){
		Document doc=null;
		try{
			InputStream is = new ByteArrayInputStream(mData.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			doc = docBuilder.parse(is);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return(doc);

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
	
	public String getServerCommand(String pAttribute){ 
		String s="";
		try{
			
			InputStream is = new ByteArrayInputStream(mData.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			Element oElem = doc.getDocumentElement();
			NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.GT);
			Node oNo1 = oNl1.item(0);
			s = Utils.getAttribute(oNo1,pAttribute);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return(s);
	}

}
