package com.jsv.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsr.util._;

public class Utils {

	public static String getAttribute(Node pNode, String pNome){
		String s="";
		NamedNodeMap nMap = pNode.getAttributes();
		if(nMap !=null ){
		  Node nId = nMap.getNamedItem(pNome);
		  if(nId !=null)
			s = nId.getTextContent();
		}
		return(s);
	}
	
	public static void setAttribute(Node pNode, String pNome, String pVal){
		NamedNodeMap nMap = pNode.getAttributes();
		if(nMap !=null ){
		  Node nId = nMap.getNamedItem(pNome); 
		  if(nId !=null)
			nId.setTextContent(pVal);
		}
	}
	
	public static int ctoi(String pValor){
		int i=0;
		try{
		 i = Integer.parseInt(pValor);
		}catch(Exception ex){}
		return(i);
	}
	
	public static void log(String s){
		System.out.println(s);
	}
	
	public Vector<String> block(String sData, int pTam){
		boolean b;
		int i,t,iSize,li,lf,v;
		String s="",ss="";
		StringBuffer sb = new StringBuffer();
		Vector<String> hm = new Vector<String>();
		
		if(sData==null || pTam==0) { return(null);}
			
		iSize = pTam;
		
		//for(i=0;i<sData.length;i++){
		//	sb.append(sData[i]);
		//}
		
		//ss=sb.toString();
		ss=sData;
		b=true;
		li=lf=0;
		//if(ss==null) { return(null); }
		t=ss.length();
		do{
			if(t>lf){
				v = t - ( li+iSize);
				if(v<0){
					lf = li+(t - li);
				}else{
					lf = li+iSize;
				}
				s = ss.substring(li, lf);
				li = lf;
				if(v<=0){ 
					b=false; 
				}
			}
			hm.add(s);
		}while(b!=false);
			
		return(hm);
	}
	
	public static String decode(String pTexto){
		String s="";
		try{
			s=URLDecoder.decode(pTexto,"ISO-8859-1");
		}catch(Exception ex){}
		return(s);
	}
	public static String encode(String pTexto){
		String s="";
		try{
			s=URLEncoder.encode(pTexto,"ISO-8859-1");
		}catch(Exception ex){}
		return(s);
	}
	
	
	public static String getNodeValue(String sXml, String sNode, String pAttribute){ 
		String s="";
		try{
			
			InputStream is = new ByteArrayInputStream(sXml.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			Element oElem = doc.getDocumentElement();
			NodeList oNl1 = oElem.getElementsByTagName(sNode);
			Node oNo1 = oNl1.item(0);
			s = Utils.getAttribute(oNo1,pAttribute);
		}catch(Exception ex){
			_.lg(ex.toString());
		}
		return(s);
	}
	
	public static int saveFile(String pNome, String pDados){
		try{
			File file = new File(pNome);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(pDados);
			output.close();
			return(0);
		}catch(Exception ex){
			return(4);
		}
	}
	
	public static int readFile(String pNome, StringBuffer sb){
	    String sline;	
		if(sb==null){
			return(4);
		}
		try{
			File file = new File(pNome);
			BufferedReader input = new BufferedReader(new FileReader(file));
			while((sline = input.readLine())!=null){
				sb.append(sline+"\n");
			}
			input.close();
			return(0);
		}catch(Exception ex){
			return(4);
		}
	}
	
	public static String[] readFiles(String pNome){
		int i;
		String s[]=null;
		File file;
		File ff = new File(pNome);
		File ffs[] = ff.listFiles();
		s = new String[ffs.length];
		for( i=0; i<ffs.length; i++){
			file=ffs[i];
			s[i] = file.getName();
		}
		return(s);
	}
	
	public static String writeStackTrace(Exception ex){
		int i;
		StringBuilder sb=new StringBuilder();
		StackTraceElement[] st=ex.getStackTrace();
		sb.append(ex.toString()+"\n");
		for(i=0;i<st.length;i++){
			sb.append(st[i].toString()+"\n");
		}
		return(sb.toString());
		
	}
	
	public static boolean hasStrData(String s){		
		if(!(s!=null && s.trim().length()>0)){
			return(false);
		}
		return(true);
	}
	    
}
