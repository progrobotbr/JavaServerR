package com.jsv.gui.components;

import java.awt.Dimension;
import javax.swing.JPanel;

public class rgeral {
	

	//Ajusta o tamanho da tela com base na maior posição do objeto interior
	public static void setTam(JPanel pTela, int w, int h){
		Dimension dd=pTela.getPreferredSize();
        if(dd.height<h){
			dd.height=h;
		}
        if(dd.width<w){
			dd.width=w;
		}
        pTela.setPreferredSize(dd);
	}
	


}
