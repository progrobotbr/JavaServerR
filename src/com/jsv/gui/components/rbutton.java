package com.jsv.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.w3c.dom.Node;

import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.trz.local.component.ScreenPainter;
import com.jsv.trz.local.component.ScreenPainter.attribComp;
import com.jsv.utils.Utils;

public class rbutton {

	public static void ClientParseProtocolResponse(HashMap pCollection,  Node pNode){
		JButton oBut;
			
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		String sVl = Utils.getAttribute(pNode,ComponentsDictionary.VL);
			
		oBut = (JButton) pCollection.get(ComponentsDictionary.BUT_ + sId);
		if(oBut == null) {
			Utils.log( ComponentsDictionary.BUT_ + sId + ": Não Existe");
			return;
		}
		oBut.setText(sVl);
		
	}
	
	public static void ScreenPainterCreateProtocolP1P2(attribComp ac, StringBuilder sb, StringBuilder sb1){
		sb.append("<bt id='");
		sb.append(ac.id.getText().toUpperCase());
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
		JButton bt = new JButton();
		bt.setName("BUT");
		bt.setText(s2);
		bt.setBorder(bd1);
		bt.setPreferredSize(new Dimension(iW,20));
		attribComp atrib=pScrp.addNext3(bt, 3, s1, s2, "", "", iX, iY, iW,0, null);
		atrib.tx.setText(s2);
	}
	
	public static void ScreenPainterRedefine(attribComp atr){
		int i, t;
		JButton bt = (JButton)atr.jc;
		try{
			i=Integer.parseInt(atr.wd.getText()); 
		}catch(Exception ex){ i=(int)bt.getSize().getWidth(); }
		t = (int)bt.getSize().getHeight();
		bt.setSize(i,t);
		bt.setText(atr.tx.getText());
	}
}
