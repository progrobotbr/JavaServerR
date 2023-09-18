package com.jsv.client;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.utils.Utils;

import com.jsv.dictionary.ComponentsDictionary;

public class ClientCommand {
	
	/*
	 *  Hello
	 *  Logon
	 *  CallTransaction SUSR
	 *  CancelTransaction
	 *  Logoff
	 */
	
	public static String Hello(){
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
				   "<mp>" +
				   "<gt cm='hello'/>" +
				   "</mp>";
		return(s);
	}
	
	public static String CallTransaction(String ps){
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
				   "<mp>" +
				   "<gt cm='calltrz' vl='"+ps+"'/>" +
				   "</mp>";
		return(s);
	}

	public static String Logon(){
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='logon'/>" +
		   "</mp>";
		return(s);
	}
	
	public static String Logoff(){
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='logoff'/>" +
		   "</mp>";
		return(s);
	}
	
	public static String CancelTrz(){
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='canceltrz'/>" +
		   "</mp>";
		return(s);
	}
	
	public static String Debug(){
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='debug'/>" +
		   "</mp>";
		return(s);
	}
	
	public static String Goto(String s){
		String s1 = "<?xml version='1.0' encoding='ISO-8859-1'?><md id='" +s+
		            "'><gt cm='gotoscr' vl='' rc='0'/></md>";
		return(s1);
	}
	
	public static String DebugF8(String s){
		String s1 = "<?xml version='1.0' encoding='ISO-8859-1'?><md id='" +s+
		            "'><gt cm='debugf8' vl='' rc='0'/></md>";
		return(s1);
	}
	
	public static String GotoStep(){
		String s1 = "<?xml version='1.0' encoding='ISO-8859-1'?><md id=''"+
		            "'><gt cm='gotoscr' vl='' rc='0'/></md>";
		return(s1);
	}
	
	public static String SavePrg(String pNome, String pTexto){
		String sTexto="";
		try{
			sTexto=URLEncoder.encode(pTexto,"ISO-8859-1");
		}catch(Exception ex){}
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='prgsave' nome='"+pNome+"' data='"+sTexto+"'/>" +
		   "</mp>";
		return(s);
	}
	
	public static String LoadPrg(String pNome, String pTip){
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='prgload' nome='"+pNome+"' tipo='"+pTip+"'/>" +
		   "</mp>";
		return(s);
	}

	public static String CompPrg(String pNome, String pTexto){
		String sTexto="";
		try{
			sTexto=URLEncoder.encode(pTexto,"ISO-8859-1");
		}catch(Exception ex){}
		String s = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='prgcomp' nome='"+pNome+"' data='"+sTexto+"'/>" +
		   "</mp>";
		return(s);
	}
	
	public static String GetVarInDebug(String s){
		String s1 = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		   "<mp>" +
		   "<gt cm='prggetv' nome='"+s+"'/>" +
		   "</mp>";
		return(s1);
	}
	
	/*
	public static String getServerCommand(String ps){
		String s="";
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(ps);
			Element oElem = doc.getDocumentElement();
			NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.GT);
			Node oNo1 = oNl1.item(0);
			s = Utils.getAttribute(oNo1,ComponentsDictionary.CM);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		return(s);
	}*/

}
