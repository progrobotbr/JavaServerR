package com.jsv.client.desktop.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.desktop.Desktop;
import com.jsv.client.desktop.GuiConstants;
import com.jsv.client.desktop.IDesktop;
import com.jsv.client.main.ClientMain;
import com.jsv.dictionary.ComponentsDictionary;

public class DesktopBase {

	private ClientBrowserTransaction transaction;
	//Desktop grids
	private JPanel desktop;
	private JPanel bottom;
	private JPanel body;
	private JPanel header;
	private JScrollPane jscroll;
	
	//Mensagem
	private JLabel bottomMsg;
		
	public void makeWidget(ClientBrowserTransaction ptransaction){
		FlowLayout ofl;
		transaction = ptransaction;
		desktop = new JPanel();
		header  = new JPanel();
		body    = new JPanel();
		bottom  = new JPanel();
		jscroll = new JScrollPane();
		//jscroll.setAutoscrolls(true);
		//jscroll.setLayout(new ScrollPaneLayout());
		
		ofl = new FlowLayout();
		ofl.setAlignment(FlowLayout.LEFT);
		ofl.setHgap(1); // Esquerda
		header.setLayout(ofl);
		ofl = new FlowLayout();
		ofl.setAlignment(FlowLayout.LEFT);
		ofl.setHgap(1); // Esquerda
		bottom.setLayout(ofl);
		
		body.setLayout(null);
		body.add(jscroll);
		//jscroll.add(new JButton("Teste"));
		
		desktop.setLayout(new BorderLayout());
		
		desktop.add(header, BorderLayout.NORTH);
		desktop.add(body, BorderLayout.CENTER);
		desktop.add(bottom, BorderLayout.SOUTH);
		
		bottomMsg = new JLabel();
		bottomMsg.setName(GuiConstants.MSG);
		bottomMsg.setPreferredSize(new Dimension(1000,15));
		bottomMsg.setText("");
		bottom.add(bottomMsg);
		desktop.setVisible(true);
		desktop.repaint();
		desktop.revalidate();
	}
	
	//Replace body
	public void replaceBody(JPanel pbody){
		
		int x,y;
		JScrollPane js=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pbody.setAutoscrolls(true);
		js.setViewportView(pbody);
		
		x = transaction.Window.mGui.getHeight();
		y = transaction.Window.mGui.getWidth();
		js.setBounds(0,0,y-16,x-110);
		
		this.body.remove(jscroll);
		this.body.add(js);
		this.jscroll = js;
		pbody.setVisible(true);
		pbody.repaint();
		desktop.repaint();
		desktop.revalidate();
				
	}
	
	//Color
	public void setHeaderColor(Color pBkColor){
		header.setBackground(pBkColor);
	}
	public void setBodyColor(Color pBkColor){
		body.setBackground(pBkColor);
    }
	public void setBottomColor(Color pBkColor){
		bottom.setBackground(pBkColor);
	}
	
	//Messages
	public void setMessage(int ptype, String s){
		switch(ptype){
			case IDesktop.MSGERR:
				bottomMsg.setText(s);
				bottomMsg.setForeground(Color.RED);
				break;
			case IDesktop.MSGOK:
				bottomMsg.setText(s);
				bottomMsg.setForeground(Color.WHITE);
				break;
			case IDesktop.MSGCLEAR:
				bottomMsg.setText("");
				bottomMsg.setForeground(Color.WHITE);
				break;
		}
		
	}
	
	
	public JPanel getBody(){
		return(body);
	}
	public JPanel getHeader(){
		return(header);
	}
	public JPanel getBottom(){
		return(bottom);
	}
	public JPanel getDesktop(){
		return(desktop);
	}
	
	public JScrollPane getBodyScroll(){
		return(jscroll);
	}
	
	
}
