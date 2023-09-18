package com.jsv.server.trz.base;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsr.util._;
import com.jsv.data.Variant;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.gui.components.rtable;
import com.jsv.gui.components.rtabstrip;
import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;
import com.jsv.server.config.srvBeanConfig;
import com.jsv.server.screen.ProtocolV1;
import com.jsv.utils.Utils;

public class TransactionProtocolJava extends MemoryOp{
	
	public  String  sTrzId             = "";
	public  String  sidScreenPointer   = "";
	public  String  sProtocolXml       = "";
	
	//////////////////////
	// Protocol
	//////////////////////
	
	////////////
	// Request
	////////////
	public boolean putRequestOfScreen(Document doc){
		boolean b=false;
		int i, t, tv;
		String sId, sVl, sName, sgd;
		Node oNo1, oNo2;
		NodeList oNl1, oNl2;
		Element oElem;
		Variant v, vs;
		
		oElem = doc.getDocumentElement();
		
		oNl1 = oElem.getElementsByTagName(ComponentsDictionary.GT);
		oNo1 = oNl1.item(0);
		sVl = Utils.getAttribute(oNo1,"okcode");
		v = this.getGlobalMemory().get("OKCODE");
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
				v = this.getGlobalMemory().get(sId);
				if(v==null) { continue; }
				if(sName.equals(ComponentsDictionary.SP)){
					rtabstrip.ServerParseProtocolRequest(this, oNo2);
				}else{
					switch(v.type){
					case Variant.STRING:
						v.s=sVl;
						break;
					case Variant.INT:
						tv = Integer.parseInt(sVl);
						v.i=tv;
						break;
					case Variant.TABLE:
						sgd = "GD"+sId;
						vs=this.getGlobalMemory().get(sgd);
						rtable.ServerParseProtocolRequest(v, vs, oNo2);
						//this.transferTable(v,oNo2);
					}
				}
			}
			
		}
		return(b);
	}
	
	//
	// Auxiliar tabela
	//
	/*
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
	*/
	
	////////////
	//Response 
	////////////
	public String getResponseOfScreen() {
		boolean b=false;
		int i, t, idx, ilp /* lines by page */, iqtr /* quantidade de registros */, ipos=0, itam=0;
		String s, sId, sVal, sRet, sP1, key, value;
		Variant v1, v2;
		table tb;
		structure st;
		Element oElem;
		Node oNo1, oNo2, oNo3Clone, oNo3Clone2;
		NodeList oNl1, oNl2, oNl3, oNl4;
		Document oXMLResp;
		ArrayList<String> al;
		HashMap<String,String> subscreens, subscreensold;
		Entry<String, String> pair;
		Iterator<Entry<String,String>> e;
		StringBuffer sb = new StringBuffer();
		int rc;
		sRet = "";
     
		subscreens = new HashMap<String,String>();
		subscreensold = this.getSubScreens(this.sidScreenPointer);
		al = new ArrayList<String>();
		if(subscreensold!=null){
			e = subscreensold.entrySet().iterator();
			while(e.hasNext()){
				pair = e.next();
				key=pair.getKey();
				value=pair.getValue();
				al.add(value);
			}
		}
		
		itam = al.size();
		while(ipos<itam){
			value = al.get(ipos);
			ipos++;
			if(!subscreens.containsKey(value)){
				subscreens.put(value, value);
				subscreensold = this.getSubScreens(value);
				if(subscreensold!=null){
					e = subscreensold.entrySet().iterator();
					while(e.hasNext()){
						pair = e.next();
						key=pair.getKey();
						value=pair.getValue();
						al.add(value);
						itam=al.size();
					}
				}
			}
		}
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			//if(sTrzId.equals("TL01") || sTrzId.equals("USRL")){
				s = srvBeanConfig.pathRep + "\\trz\\" + this.sTrzId + "\\protocol\\" + "rep@" + this.sTrzId + '@' + this.sidScreenPointer + ".xml";
				sb.append("<?xml version='1.0' encoding='ISO-8859-1'?><mp id='"+this.sTrzId+"'><gt cm='"+this.sidScreenPointer+"' vl='"+this.sidScreenPointer+"' rc='0'/>");
				rc = Utils.readFile(s, sb);
				
				////new
				if(subscreens!=null){
					e = subscreens.entrySet().iterator();
					while(e.hasNext()){
						pair = e.next();
						key=pair.getKey();
						value=pair.getValue();
						s = srvBeanConfig.pathRep + "\\trz\\" + this.sTrzId + "\\protocol\\" + "rep@" + this.sTrzId + '@' + value + ".xml";
						rc = Utils.readFile(s, sb);
					}
					
				}
				////new
				
				sb.append("</mp>");
				InputStream stream = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
				//oXMLResp = docBuilder.parse(new File(s));
				oXMLResp = docBuilder.parse(stream);
			//}else{
				//ProtocolV1 oP1 = new ProtocolV1();
				//oP1.setSqlDB(mSqlBase);
				//sP1 = oP1.makeP2(mTrzId, mScreenId);
				//InputStream is = new ByteArrayInputStream(sP1.getBytes());
				//oXMLResp = docBuilder.parse(is);
			//}
				
			oElem = oXMLResp.getDocumentElement();
			oNl1 = oElem.getElementsByTagName(ComponentsDictionary.SD);
			if (oNl1.getLength() > 0) {
				for(int ii=0; ii<oNl1.getLength(); ii++){
					oNo1 = oNl1.item(ii);
					oNl2 = oNo1.getChildNodes();
					for (i = 0; i < oNl2.getLength(); i++) {
						oNo1 = oNl2.item(i);
						if (oNo1.getNodeType() == 1) {
							sVal=oNo1.getNodeName();
							if (sVal.equals(ComponentsDictionary.IF)) {
								sId = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
								sVal = this.loadGS(sId);
								Utils.setAttribute(oNo1, ComponentsDictionary.VL, sVal);
							
							} else if (sVal.equals(ComponentsDictionary.SP)) {
								rtabstrip.ServerGenerateProtocolResponse(this, oNo1);
							
							} else if (sVal.equals(ComponentsDictionary.TB)) {
								rtable.ServerGenerateProtocolResponse(this, oNo1);
								/*
								sId = Utils.getAttribute(oNo1, ComponentsDictionary.ID);
								v1 = this.loadOB(sId);
								v2 = this.loadOB("GD" + sId);
								if (v1 != null && v2 != null) {
									tb = v1.getTable();
									iqtr = tb.getRowCount();
									st = v2.getStucture();
									v1 = st.get("INDEX");
									idx = v1.getInt();
									v1 = st.get("LINEBYPAGE");
									ilp = v1.getInt();
									tb.setIndex(idx);
									oNl3 = oNo1.getChildNodes();
									oNo2 = oNl3.item(0); // tr?
									oNo3Clone = oNo2.cloneNode(true);
									t = iqtr - idx;
									if(t>ilp){
										t=ilp;
									}
									st=tb.foreach();
									for(int y=0;y<t;y++){
										if(st==null){break;}
										if(y==0){
											oNo3Clone2 = oNo2;
										}else{
											oNo3Clone2 = oNo3Clone.cloneNode(true);
										}
									
										oNl3 = oNo3Clone2.getChildNodes();
										for (int z = 0; z < oNl3.getLength(); z++) {
											Node oNo4 = oNl3.item(z);
											sVal=Utils.getAttribute(oNo4,ComponentsDictionary.ID);
											v2=st.get(sVal);
											sVal = v2.getString();
											sVal = Utils.encode(sVal);
											Utils.setAttribute(oNo4, ComponentsDictionary.VL, sVal);
											sVal=""+(z+1);
											Utils.setAttribute(oNo4, ComponentsDictionary.PS, sVal);
										}
										oNo1.appendChild(oNo3Clone2);  
										st=tb.foreach();
									}
								}
								 */
							}
						}
					}
				}
			}
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			Source source = new DOMSource(oXMLResp);
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, result);
			sRet = sw.toString();
			_.lg(sRet);
		} catch (Exception ex) {
			_.lg(ex.toString());
		}
		return (sRet);
	}
	
	//Retorna a estrutura da transação
	public String getStructure(String psscrn){
		int i;
		String s="",s1, s2, stp="", sf[], sscrn;
		StringBuffer sb = new StringBuffer();
		if(Utils.hasStrData(psscrn)){
			sscrn = psscrn;
		}else{
			sscrn = "1000";
		}
		
		try{
			stp="0";
			if(this.sTrzId!=null && this.sTrzId.toUpperCase().equals("TL01")){
				stp="1";
				s1 = srvBeanConfig.pathRep + "trz\\" + this.sTrzId + "\\protocol\\" + "str" + this.sTrzId + ".xml";
				sb.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
				sb.append("<md id='");
				sb.append(this.sTrzId);
				sb.append("' trztp='"+stp+"'><gt cm='gotoscr' vl='"+sscrn+"' rc='0' vs='1'/>");
				Utils.readFile(s1, sb);
				sb.append("</md>");
			}else{
				s1 = srvBeanConfig.pathRep + "trz\\" + this.sTrzId + "\\protocol\\";
				sb.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
				sb.append("<md id='");
				sb.append(this.sTrzId);
				sb.append("' trztp='"+stp+"'><gt cm='gotoscr' vl='"+sscrn+"' rc='0' vs='1'/>");
				sf = Utils.readFiles(s1);
				if(sf!=null){
					for(i=0; i<sf.length; i++){
						s2=sf[i];
						if(s2.startsWith("str")){
							s2=s1+s2;
							Utils.readFile(s2, sb);
						}
					
					}
				}
				sb.append("</md>");
			}
			s = sb.toString();
			this.sProtocolXml = s;
		}catch(Exception ex){
			_.lg(ex.toString());
		}
		return(s);
	}

}
