package com.jsv.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.w3c.dom.Node;

import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.trz.local.component.ScreenPainter;
import com.jsv.trz.local.component.ScreenPainter.attribComp;
import com.jsv.utils.Utils;

public class rlabel {
	
	public static void ClientParseProtocolResponse(HashMap pCollection,  Node pNode){
		
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

	public static void ScreenPainterCreateProtocolP1P2(attribComp ac, StringBuilder sb, StringBuilder sb1){
		String stemp = ac.id.getText().toUpperCase(); //id
		//p1
		sb.append("<lb id='");
		sb.append(stemp);
		sb.append("' wd='");
		sb.append(ac.wd.getText());
		sb.append("' tx='");
		sb.append(ac.tx.getText());
		sb.append("' x='");
		sb.append(ac.jc.getBounds().x);
		sb.append("' y='");
		sb.append(ac.jc.getBounds().y);
		sb.append("'/>");
	}
	
	public static void ScreenPainterLoad(ScreenPainter pScrp, Node pNo){
		String sX, sY, sW, sH, s1, s2;
		int iX, iY, iW, iH;
		
		sX = Utils.getAttribute(pNo,"x");
		sY = Utils.getAttribute(pNo,"y");
		iX = Integer.parseInt(sX);
		iY = Integer.parseInt(sY);
		sW = Utils.getAttribute(pNo,"wd");
		iW = Integer.parseInt(sW);
		s1 = Utils.getAttribute(pNo,"id");
		s2 = Utils.getAttribute(pNo,"tx");
		LineBorder bd1 = new LineBorder(Color.LIGHT_GRAY,1);
		JLabel lb = new JLabel();
		lb.setText(s2);
		lb.setName("LAB");
		lb.setBorder(bd1);
		lb.setPreferredSize(new Dimension(iW,20));
		pScrp.addNext3(lb, 1, s1, s2, "", "", iX,iY, iW, 0, null);
	}

	public static void ScreenPainterRedefine(attribComp atr){
		int i,t;
		JLabel lb=(JLabel) atr.jc;
		try{
			i=Integer.parseInt(atr.wd.getText()); 
		}catch(Exception ex){ i=(int)lb.getSize().getWidth(); }
		t = (int)lb.getSize().getHeight();
		lb.setSize(i,t);
		lb.setText(atr.tx.getText());
	}
}
