package com.jsv.gui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.w3c.dom.Node;

import com.jsv.trz.local.component.ScreenPainter;
import com.jsv.trz.local.component.ScreenPainter.attribComp;
import com.jsv.utils.Utils;

public class rbox {
	
	public static void ScreenPainterCreate(ScreenPainter pScrp, String sId){
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder("Novo"));
		jp.setPreferredSize(new Dimension(100,100));
		jp.setName(sId);
		attribComp atrib = pScrp.addNext(jp);
		atrib.ln.setText("100");
		atrib.id.setText("Novo");
		atrib.Type=5;
	}
	
	public static void ScreenPainterCreateProtocolP1P2(attribComp ac, StringBuilder sb, StringBuilder sb1){
		sb.append("<bx id='");
		sb.append(ac.id.getText().toUpperCase());
		sb.append("' wd='");
		sb.append(ac.wd.getText());
		sb.append("' hd='");
		sb.append(ac.ln.getText());
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
		sH = Utils.getAttribute(pNo,"hd");
		iH = Integer.parseInt(sH);
		s1 = Utils.getAttribute(pNo,"id");
		s2 = Utils.getAttribute(pNo,"tx");
		LineBorder bd1 = new LineBorder(Color.LIGHT_GRAY,1);
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder(s2));
		jp.setPreferredSize(new Dimension(iW,iH));
		jp.setName("BOX");
		attribComp atrib=pScrp.addNext3(jp, 5, s1, s2, "", "", iX, iY, iW, iH, null);
		atrib.Type=5;
		pScrp.setVisible(true);
	}
		
	public static void ScreenPainterRedefine(attribComp atr){
		int i, t;
		Border bd;
		try{
			i=Integer.parseInt(atr.wd.getText()); 
		}catch(Exception ex){ i=(int)atr.jc.getSize().getWidth(); }
		try{
			t=Integer.parseInt(atr.ln.getText()); 
		}catch(Exception ex){ t=(int)atr.jc.getSize().getHeight(); }
		bd=BorderFactory.createTitledBorder(atr.tx.getText());
		atr.OrignalBorder=bd;
		atr.jc.setBorder(bd);
		atr.jc.setSize(i,t);
	}
}
