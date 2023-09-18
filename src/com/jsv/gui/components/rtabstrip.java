package com.jsv.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Node;

import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.ClientHandleRequest;
import com.jsv.data.Variant;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.server.trz.base.TransactionProtocolJava;
import com.jsv.trz.local.component.ScreenPainter;
import com.jsv.trz.local.component.ScreenPainter.attribComp;
import com.jsv.utils.Utils;

public class rtabstrip {

	public static final String X  = "x";
	public static final String Y  = "y";
	public static final String WD = "wd";
	public static final String HD = "hd";
	public static final String VL = "vl"; //valor que pode ser as abas = abap1,abap2,... ou o número da abap no rquest
	public static final String ID = "id"; //nome da tabstrip
	public static final String SCID = "scid";  //número da tela a ser apresentada - geralmente no response
	public static final String SCVL = "scvl";
	public static final String SCIDX = "scidx"; //índice do tabstrip a ser mostrado - geralmente no request
	public static final String SPT_ = "spt_"; //id do componente na memoria
	
	public String id="";
	public int idx;
	public String spd=""; //tabstrip data kkk,vvv;kkk,vvvv
	public String scid="";
	public String scvl="";
	public String[] scm=null; //meta dados
	public JTabbedPane mJjtp;
	
	private ClientHandleRequest events;
	
	public static void ScreenPainterCreate(ScreenPainter pScrp, String sId){
		
		//JPanel jpp = new JPanel(); //new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);;
		JTabbedPane jtp = new JTabbedPane();
		  
		jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		jtp.setPreferredSize(new Dimension(150,150));
		//jpp.setPreferredSize(new Dimension(150,150));
		//jpp.setLayout(new GridLayout(0,1));
		
		//jpp.add(jtp);
		//jtp.setName(sId);
		jtp.repaint();
		jtp.setName(sId);
		attribComp atrib = pScrp.addNext(jtp);
		
		atrib.Type=5;
	}
	
	public static void ScreenPainterRedefine(attribComp atr){
		String s, ss[], sss[];
		JPanel jj;
		//JTabbedPane jc=(JTabbedPane) atr.jc.getComponent(0);
		JTabbedPane jc=(JTabbedPane)atr.jc;
		int iwd=0, ihd=0,i;
		
		try{
			iwd=Integer.parseInt(atr.wd.getText()); 
			ihd=Integer.parseInt(atr.hd.getText()); 
		}catch(Exception ex){
			
		}
		s=atr.cl.getText();
		ss=s.split(";");
		jc.removeAll();
		for(i=0;i<ss.length;i++){
			jc.add(ss[i], null);
		}
		//atr.jc.setPreferredSize(new Dimension(iwd,ihd));
		//jc.setPreferredSize(new Dimension(iwd,ihd));
		//jc.validate();
		//jc.repaint();
		atr.jc.setSize(iwd,ihd);
		atr.jc.validate();
		atr.jc.repaint();
	
	}

	public static void ScreenPainterCreateProtocolP1P2(attribComp ac, StringBuilder sb, StringBuilder sb1){
		String sId, sWd, sHd, sTabs, sp1="", sp2="";
		sId=ac.id.getText().toUpperCase();
		sWd=ac.wd.getText().toUpperCase();
		sHd=ac.hd.getText().toUpperCase();
		sTabs=ac.cl.getText();
		sp1 += "<sp id=\""+sId+"\" vl=\""+sTabs+"\" scid=\""+ac.tx.getText()+"\" scvl=\"\" wd=\""+sWd+"\" hd=\""+sHd+"\" x=\""+ac.jc.getBounds().x + "\"  y=\""+ ac.jc.getBounds().y + "\"/>"; //vl=1,cadastro;2,lista
		sp2 += "<sp id=\""+sId+"\" vl=\"\" scid=\""+ac.tx.getText()+"\" scvl=\"\"/>";  //quando request envia o id da abap; no response envia a tela que deve ser mostrada
		sb.append(sp1);
		sb1.append(sp2);
	}
	
	public static void ScreenPainterLoad(ScreenPainter pScrp, Node pNo){
		
		int ix,iy,iwd,ihd;
		String sx, sy, sId, sWd, sHd, sVl, sSc, ss[], sss;
		
		JPanel jpp = new JPanel(); //new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);;
		JTabbedPane jtp = new JTabbedPane();
		
		sId = Utils.getAttribute(pNo,"id");
		sSc = Utils.getAttribute(pNo,"scid");
		sx  = Utils.getAttribute(pNo,"x");  ix   = Integer.parseInt(sx);
		sy  = Utils.getAttribute(pNo,"y");  iy   = Integer.parseInt(sy);
		sWd = Utils.getAttribute(pNo,"wd"); iwd  = Integer.parseInt(sWd);
		sHd = Utils.getAttribute(pNo,"hd"); ihd  = Integer.parseInt(sHd);
		sVl = Utils.getAttribute(pNo,"vl");
		
		jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		//jtp.setPreferredSize(new Dimension(iwd,ihd));
		
		ss=sVl.split(";");
		for(int i=0;i<ss.length;i++){
			jtp.add(ss[i], null);
		}
		jtp.setPreferredSize(new Dimension(iwd,ihd));
		//jpp.setLayout(new GridLayout(0,1));
		
		//jpp.add(jtp);
		jtp.setName("TBS");
		jtp.repaint();
		//attribComp atrib = pScrp.addNext(jtp);
		
		//atrib.Type=ComponentsDictionary.TABSTRIP;
		attribComp atrib = pScrp.addNextTabStrip((JComponent)jtp, ComponentsDictionary.TABSTRIP, sId, sSc, sWd, sHd, sVl, ix, iy);
		atrib.Type=ComponentsDictionary.TABSTRIP;
		
	}
	
	public static void ServerGenerateProtocolResponse(TransactionProtocolJava ptpj, Node pNo){
		String sScId, sScVl;
		Variant vs;
		sScId = Utils.getAttribute(pNo, rtabstrip.SCID);
		vs = ptpj.loadOB(sScId);
		sScVl = vs.getString();
		Utils.setAttribute(pNo, rtabstrip.SCVL, sScVl);
	}

	public static void ServerParseProtocolRequest(TransactionProtocolJava ptpj, Node pNode){
		String sid, svl;
		sid=Utils.getAttribute(pNode,ComponentsDictionary.ID);
		svl=Utils.getAttribute(pNode,rtabstrip.VL);
		ptpj.storegVar(sid, svl);
	}
	
	public static void ClientParseProtocolCreateTabStrip(ClientHandleRequest mCHR,HashMap pCollection,String pScreenId, JPanel pTela, Node pNode){
		int ix,iy,iwd,ihd;
		String sId, sX, sY, shd, swd, svl;
		JPanel jpp = new JPanel(); //new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);;
		JTabbedPane jtp = new JTabbedPane();
		rtabstrip otabstrip = new rtabstrip();
		
		sX  =Utils.getAttribute(pNode,rtabstrip.X);
		sY  =Utils.getAttribute(pNode,rtabstrip.Y);
		swd =Utils.getAttribute(pNode,rtabstrip.WD); //largura da tabela
		shd =Utils.getAttribute(pNode,rtabstrip.HD); //altura  da tabela
		svl =Utils.getAttribute(pNode,rtabstrip.VL);
		sId =Utils.getAttribute(pNode,rtabstrip.ID); //id
		
		otabstrip.id = sId;
		otabstrip.scid = Utils.getAttribute(pNode,rtabstrip.SCID); 
		otabstrip.scm = svl.split(";");
		
	    ix  =Integer.parseInt(sX);
    	iy  =Integer.parseInt(sY);
    	iwd =Integer.parseInt(swd);
    	ihd =Integer.parseInt(shd);
    	
    	JPanel jp;
    	jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    	
    	if(otabstrip.scm!=null){
    		for(int i=0; i<otabstrip.scm.length;i++){
    			jp = new JPanel();
    			jp.setBounds(ix, iy, iwd, ihd);
    			jp.setLayout(null);
    			jtp.addTab(otabstrip.scm[i], jp); //jtp.setBackgroundAt(0,new Color(203, 219, 234));
    		}
    	}
    	jtp.setBounds(ix, iy, iwd, ihd);
		jpp.setBounds(ix, iy, iwd, ihd);
		jpp.setLayout(new GridLayout(0,1));
		
		otabstrip.mJjtp = jtp;
		jpp.add(jtp);
		
		//Controle da mudança de tabs
		jtp.setName(ComponentsDictionary.SCR_ + pScreenId + "_" + rtabstrip.SPT_ + sId);
		jtp.addChangeListener(mCHR);
		otabstrip.events = mCHR;
		
		pTela.add(jpp);
		
		pCollection.put(rtabstrip.SPT_ + sId, otabstrip);
		
		rgeral.setTam(pTela, ix+iwd, iy+ihd);
			
	}
	
	public static String ClientGenerateProtocolRequest(rtabstrip rtb){
		int i;
		String s;
		StringBuilder sb=new StringBuilder();
		i=rtb.mJjtp.getSelectedIndex();
		sb.append("<sp id=\""+rtb.id+"\" vl=\""+i+"\" scid=\"\" scvl=\"\"/>");
		s=sb.toString();
		return(s);
	}
	
	public static void ClientParseProtocolResponse(ClientBrowserTransaction pTransaction, HashMap pCollection,  Node pNode){
		int i;
		String sid, sscvl;
		rtabstrip rtb;
		JTabbedPane jtp;
		HashMap hm;
		JPanel sc;
		sid=Utils.getAttribute(pNode,rtabstrip.ID);
		sscvl=Utils.getAttribute(pNode,rtabstrip.SCVL);
		rtb = (rtabstrip) pCollection.get(rtabstrip.SPT_+sid);
		jtp = rtb.mJjtp;
		hm = pTransaction.getMemScreen(sscvl);
		sc = (JPanel) hm.get(ComponentsDictionary.SCR_+sscvl);
		i = jtp.getSelectedIndex();
		int t = jtp.getTabCount();
		JPanel pp;
		//ChangeListener[] ls = jtp.getChangeListeners();
		//jtp.removeChangeListener(ls[0]);
		rtb.events.enable=false;
		for(int z=0; z<t; z++){
			pp=new JPanel();
			
			jtp.setComponentAt(z, pp);
			//if(i==z){
				//jtp.setComponentAt(z, sc);
			//}
		}
		JScrollPane js=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js.setViewportView(sc);
		js.setBounds(0,0,sc.getHeight(),sc.getWidth());
		jtp.setComponentAt(i, js);
		rtb.events.enable=true;
		//jtp.addChangeListener(ls[0]);
		
		//pp.add(sc);
		/*
		JPanel pp = (JPanel) jtp.getComponent(i+3);
		pp.add(sc);
		pp.setVisible(true);
		sc.setVisible(true);
		pp.repaint();
		sc.repaint();
		jtp.repaint();
		*/
		//jtp.setComponentAt(i, sc);
		//pp.setLayout(null);
		//pp.add(sc);
		
	}
	
	/*
	public static void putSubScreenMem(String tela, TransactionProtocolJava ptpj){
		Variant v;
		v = ptpj.loadOB(rtabstrip.nomeMemTela(rtabstrip.SCID, tela));
	}
	*/
	
	private static rtabstrip getTabMem(String s, TransactionProtocolJava ptpj){
		Variant v;
		v=ptpj.loadOB(s);
		if(v!=null){
			if(v.ob!=null){
				if(v.ob instanceof rtabstrip){
					return((rtabstrip)v.ob);
				}
			}
		}
		return null;
	}
	
	/*
	private static String nomeMemTela(String sufix, String tela){
		if(sufix!=null && tela !=null){
			return(sufix+"_"+tela);
		}
		return("");
	}
	*/
	
}
