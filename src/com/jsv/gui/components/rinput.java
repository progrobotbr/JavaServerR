package com.jsv.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.w3c.dom.Node;

import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.trz.local.component.ScreenPainter;
import com.jsv.trz.local.component.ScreenPainter.attribComp;
import com.jsv.utils.Utils;

public class rinput {

	public static void ClientParseProtocolResponse(HashMap pCollection,  Node pNode){
		 JTextField oInp;
			
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		String sVl = Utils.decode(Utils.getAttribute(pNode,ComponentsDictionary.VL));
		
		oInp = (JTextField) pCollection.get(ComponentsDictionary.INP_ + sId);
		if(oInp == null) {
			Utils.log( ComponentsDictionary.INP_ + sId + ": Não Existe");
			return;
		}
		oInp.setText(sVl);
	}

	public static void ScreenPainterCreateProtocolP1P2(attribComp ac, StringBuilder sb, StringBuilder sb1){
		String stemp = ac.id.getText().toUpperCase(); //id
		//p1
		sb.append("<if id='");
		sb.append(stemp);
		sb.append("' wd='");
		sb.append(ac.wd.getText());
		sb.append("' nc='");
		sb.append(ac.nc.getText());
		sb.append("' x='");
		sb.append(ac.jc.getBounds().x);
		sb.append("' y='");
		sb.append(ac.jc.getBounds().y);
		sb.append("' ro='");
		sb.append(ac.ro.getText());
		sb.append("'/>");
		sb1.append("<if id='");sb1.append(stemp);sb1.append("' vl=''/>");
	}
	
	public static void ScreenPainterLoad(ScreenPainter pScrp, Node pNo){
		String sX, sY, sW, sH, s1, s2, s3, s4;
		int iX, iY, iW, iH;
		
		sX = Utils.getAttribute(pNo,"x");
		sY = Utils.getAttribute(pNo,"y");
		iX = Integer.parseInt(sX);
		iY = Integer.parseInt(sY);
		sW = Utils.getAttribute(pNo,"wd");
		iW = Integer.parseInt(sW);
		s1 = Utils.getAttribute(pNo,"id");
		s2 = Utils.getAttribute(pNo,"vl");
		s3 = Utils.getAttribute(pNo,"nc");
		s4 = Utils.getAttribute(pNo,"ro");
		LineBorder bd1 = new LineBorder(Color.LIGHT_GRAY,1);
		JTextField tf = new JTextField();
		bd1 = new LineBorder(Color.LIGHT_GRAY,1);
		tf.setBorder(bd1);
		tf.setName("INP");
		tf.setEditable(false);
		tf.setBackground(Color.WHITE);
		tf.setPreferredSize(new Dimension(iW,20));
		pScrp.addNext3(tf, 2, s1, "", s2, s3, iX, iY, iW,0, s4);
	}
	
	public static void ScreenPainterRedefine(attribComp atr){
		int i, t;
		JTextField tf = (JTextField)atr.jc;
		try{
			i=Integer.parseInt(atr.wd.getText()); 
		}catch(Exception ex){ i=(int)tf.getSize().getWidth(); }
		t = (int)tf.getSize().getHeight();
		tf.setSize(i,t);
		tf.setText(atr.tx.getText());
		//Cor do ReadOnly
		if(atr.ro.getText().trim().length()==0){
			tf.setBackground(Color.WHITE);
		}
	}

	public static String ClientGenerateProtocolRequest(String pId, String sVal){
		String s = "<if id='" + pId + "' vl='" + Utils.encode(sVal) + "'/>";
		return(s);
	}
		
}
