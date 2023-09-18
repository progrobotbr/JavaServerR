package com.jsv.client.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.jsv.utils.Utils;
import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.ClientProtocol;
import com.jsv.client.desktop.base.DesktopBase;

public class DesktopDump extends DesktopBase implements IDesktop {

	private ClientBrowserTransaction Transaction;
	private JTextPane textArea1=null;
	JTextField ifRepName=null;
	JTextField ifCmd=null;
	
	
	public void create(ClientBrowserTransaction ptransaction) {
		
		this.Transaction = ptransaction;
		this.Transaction.Desktop = (IDesktop) this;
		
		JTextField ipCmd = new JTextField();
		this.makeWidget(ptransaction);
		ipCmd.setName(IDesktop.IPCM);
		ipCmd.setPreferredSize(new Dimension(50, 15));
		ipCmd.addKeyListener(new DesktopProcessKey(ptransaction));
		this.getHeader().setBackground(new Color(0, 0, 0));
		this.getBody().setBackground(new Color(255, 255, 255));
		this.getHeader().add(ipCmd);
		JLabel jl = new JLabel();
			
		this.getHeader().add(jl);
		textArea1= new JTextPane();
		textArea1.setBackground(new Color(255,255,255));
		Font font = new Font("Courier New", Font.PLAIN, 14);
		textArea1.setFont(font);
		textArea1.setText(Transaction.ErrorDump);
		JScrollPane areaScrollPane = new JScrollPane(textArea1);
		this.getBody().add(areaScrollPane);
		this.getBody().setLayout(new GridLayout(0, 1));
		//this.getBody().setVisible(true);
		//this.getBody().revalidate();
		//this.getBody().repaint();
		//this.getDesktop().repaint();
		//this.getDesktop().setVisible(true);
	}
	

	
	public JPanel getBody(){
		return(super.getBody());
	}
	
	public JPanel getHeader() {
		return(super.getHeader());
	}
	
	@Override
	public JPanel getBottom() {
		return(super.getBottom());
	}

	public JPanel getDesktop() {
		return(super.getDesktop());
	}
	@Override
	public void setDesktop(JPanel jp) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setHeader(JPanel jp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBody(JPanel jp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBottom(JPanel jp) {
		// TODO Auto-generated method stub
	}
	
}

