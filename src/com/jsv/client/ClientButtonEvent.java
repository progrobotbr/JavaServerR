package com.jsv.client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.jsv.utils.Utils;

public class ClientButtonEvent implements MouseListener{
	
	private String mOkCode ="";
	
	public void mouseClicked(MouseEvent e) {
	     mOkCode = e.toString();
	     Utils.log(mOkCode);
	}
	
	public void mousePressed(MouseEvent e) {
	}    

	public void mouseReleased(MouseEvent e) {
	      
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
	
	public void setOkCode(String pPar){
		mOkCode = "";
	}
	public String getOkCode(){
		return(mOkCode);
	}
}
