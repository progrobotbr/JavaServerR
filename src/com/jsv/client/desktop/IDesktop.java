package com.jsv.client.desktop;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.main.ClientMain;

public interface IDesktop {
	
	public static final int MSGERR=1;
	public static final int MSGOK=0;
	public static final int MSGCLEAR=3;
	public static final String IPCM = "IPCM"; 
	
	public void   makeWidget(ClientBrowserTransaction ptransaction);
	public JPanel getDesktop();
	public JPanel getBody();
	public JPanel getHeader();
	public JPanel getBottom();
	public void   setDesktop(JPanel jp);
	public void   setHeader(JPanel jp);
	public void   setBody(JPanel jp);
	public void   setBottom(JPanel jp);
	public void   setMessage(int ptype, String s);
	public void   replaceBody(JPanel pbody);
	public void   resize(JFrame jf);
	
}
