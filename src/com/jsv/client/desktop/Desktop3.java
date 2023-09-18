package com.jsv.client.desktop;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.desktop.base.DesktopBase;


public class Desktop3 extends DesktopBase implements IDesktop{

	public Desktop3(ClientBrowserTransaction ptransaction){
		this.create(ptransaction);
	}
	
	public void create(ClientBrowserTransaction ptransaction) {
		JTextField ipCmd = new JTextField();
		
		this.makeWidget(ptransaction);
		ipCmd.setName(IDesktop.IPCM);
		ipCmd.setPreferredSize(new Dimension(50, 15));
		ipCmd.addKeyListener(new DesktopProcessKey(ptransaction));
		this.getHeader().setBackground(new Color(0, 0, 0));
		this.getHeader().add(ipCmd);
		this.getBottom().setBackground(new Color(255, 255, 255));
		
	}
	
	public void setMessage(int ptype, String s){
		super.setMessage(ptype, s);
	}
	
	public void  replaceBody(JPanel pbody){
		super.replaceBody(pbody);
	}
	
	public JPanel getDesktop(){
		return(super.getDesktop());
		//return(desktop);
	}
	
	public JPanel getBody(){
		return(super.getBody());
	}

	public JPanel getHeader() {
		//return null;
		return(super.getHeader());
	}

	public JPanel getBottom() {
		return(super.getBottom());
	}

	public void setDesktop(JPanel jp) {
	}

	public void setHeader(JPanel jp) {
	}

	public void setBody(JPanel jp) {
	}
	public void setBottom(JPanel jp) {
	}
	

}
