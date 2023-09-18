package com.jsv.client.desktop;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.desktop.base.DesktopBase;


public class Desktop2 extends DesktopBase implements IDesktop{

	public Desktop2(ClientBrowserTransaction ptransaction){
		this.create(ptransaction);
		ptransaction.Desktop = this;
	}
	
	public void create(ClientBrowserTransaction ptransaction) {
		JTextField ipCmd = new JTextField();
		
		this.makeWidget(ptransaction);
		ipCmd.setName(IDesktop.IPCM);
		ipCmd.setPreferredSize(new Dimension(50, 15));
		ipCmd.addKeyListener(new DesktopProcessKey(ptransaction));
		//this.getHeader().setBackground(new Color(0, 0, 0));
		this.getHeader().setBackground(new Color(64, 128, 128));
		this.getHeader().add(ipCmd);
		this.getBottom().setBackground(new Color(255, 255, 255));
		
	}
	
	public void resize(JFrame jf){
		int x,y;
		x = jf.getHeight();
		y = jf.getWidth();
		this.getBodyScroll().setBounds(0,0,y-16,x-110); //transaction.Window.mGui.getHeight(), transaction.Window.mGui.getWidth());
		}
	
	public void setMessage(int ptype, String s){
		super.setMessage(ptype, s);
	}
	
	public void  replaceBody(JPanel pbody){
		super.replaceBody(pbody);
	}
	
	public JPanel getDesktop(){
		return(super.getDesktop());
	}
	
	public JPanel getBody(){
		return(super.getBody());
	}

	public JPanel getHeader() {
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
