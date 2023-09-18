package com.jsv.server.trz.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.jsv.lang.vm.structure;
import com.jsv.lang.vm.table;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsv.server.screen.ProtocolV1;
import com.jsv.utils.Utils;

import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.dictionary.ComponentsDictionary;

public abstract class AScreenBase extends MemoryOp {

	private static final String cDIR = "C:\\Renato\\programas\\rserver\\Rep\\";

	private HashMap mMemLocal;
	private Document mXMLResp = null;
	private String mScreenId;
	private String mTrzId;
	private boolean isFirstTime = true;
	private sqlbase mSqlBase;

	public AScreenBase() {
		mMemLocal = new HashMap();
		isFirstTime = true;
		super.setLocalMemory(mMemLocal);
		// super.setGlobalMemory(pMemGlobal);
	}

	public void Init(String pTrzId, String pScreenId, ATransactionBase pTrz) {
		this.setTrzId(pTrzId);
		this.setScreenId(pScreenId);
		this.setGlobalMemory(pTrz.getGlobalMemory());
		this.loadResponseStructure();
	}

	public boolean isFirstTime() {
		return (isFirstTime);
	}

	public void setNotFirstTime() {
		isFirstTime = false;
	}

	public void setGlobalMemory(HashMap pMemGlobal) {
		super.setGlobalMemory(pMemGlobal);
	}

	public String getXmlData() {
		return ("");
	}; // Retorna o xml correspondente aos dados da tela

	public String getScreenId() {
		return (mScreenId);
	};

	public String getTrzId() {
		return (mTrzId);
	};

	public void setScreenId(String s) {
		mScreenId = s;
	};

	public void setTrzId(String s) {
		mTrzId = s;
	}

	public void setVariableStructures() {
	};

	public void loadResponseStructure() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			mXMLResp = docBuilder.parse(new File(cDIR + mTrzId + "\\" + "rep@"
					+ mTrzId + '@' + mScreenId + ".xml"));
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	public String getRespScreenProtocol() {
		int i, t, idx, ilp /* lines by page */, iqtr /* quantidade de registros */;
		String s, sId, sVal, sRet, sP1;
		Variant v1, v2;
		table tb;
		structure st;
		Element oElem;
		Node oNo1, oNo2, oNo3Clone, oNo3Clone2 ;
		NodeList oNl1, oNl2, oNl3, oNl4;
		Document oXMLResp;

		sRet = "";

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			if(mTrzId.equals("TL01") || mTrzId.equals("USRL")){
				s = cDIR + mTrzId + "\\" + "rep@" + mTrzId + '@' + mScreenId + ".xml";
				oXMLResp = docBuilder.parse(new File(s));
			}else{
				ProtocolV1 oP1 = new ProtocolV1();
				oP1.setSqlDB(mSqlBase);
				sP1 = oP1.makeP2(mTrzId, mScreenId); //mScreenId
				InputStream is = new ByteArrayInputStream(sP1.getBytes());
				oXMLResp = docBuilder.parse(is);
			}
				
			oElem = oXMLResp.getDocumentElement();
			oNl1 = oElem.getElementsByTagName(ComponentsDictionary.SD);
			if (oNl1.getLength() > 0) {
				oNo1 = oNl1.item(0);
				oNl2 = oNo1.getChildNodes();
				for (i = 0; i < oNl2.getLength(); i++) {
					oNo1 = oNl2.item(i);
					if (oNo1.getNodeType() == 1) {
						sVal=oNo1.getNodeName();
						if (sVal.equals(ComponentsDictionary.IF)) {
							sId = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
							sVal = this.loadGS(sId);
							Utils.setAttribute(oNo1, ComponentsDictionary.VL, sVal);
						} else if (sVal.equals(ComponentsDictionary.TB)) {
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
			log(sRet);
		} catch (Exception ex) {
			log(ex.toString());
		}
		return (sRet);
	}
	
	public void setSqlDb(sqlbase psqlbase){
		mSqlBase = psqlbase;
	}

	public void log(String s) {
		System.out.println("Trz:" + mTrzId + "_Scr:" + mScreenId + ">" + s);
	}

}
