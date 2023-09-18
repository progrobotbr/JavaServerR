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

import com.jsv.client.ClientProtocol;

public class DesktopEditor implements ActionListener {

	private JPanel mDs=null;
	private JPanel mTd=null;
	private JPanel mMd=null;
	private JPanel mBd=null;
	private HashMap<String, JPanel> mDesktop;
	private ClientProtocol mProtocol=null;
	
	private JTextPane textArea1=null;
	JTextField ifRepName=null;
	JTextField ifCmd=null;
	
	public DesktopEditor(){
		
	}
	
	public void setBodyText(String s){
		textArea1.setText(s);
		textArea1.setEditable(false);
		textArea1.setBackground(new Color(192,192,192));
	}
	
	public void clearLastScreen(){
		//if(mTd!=null) mTd.removeAll();
		if(mMd!=null) mMd.removeAll();
		if(mBd!=null) mBd.removeAll();
		if(mDs!=null) {
			mDs.repaint();
		}
	}
	
	public void create(HashMap<String, JPanel> pDesktop, ClientProtocol pProtocol){
		
		JPanel ds = null;
		JPanel td = new JPanel();
		JPanel md = new JPanel();
		JPanel bd = new JPanel();
		
		FlowLayout ofl = new FlowLayout();
		ofl.setAlignment(FlowLayout.LEFT);
		ofl.setHgap(1);
		
		if(pDesktop!=null && pDesktop.containsKey(GuiConstants.DESKTO)){
			ds = pDesktop.get(GuiConstants.DESKTO);
			JPanel ltd = pDesktop.get(GuiConstants.DESTIT);mTd=ltd;
			JPanel lmd = pDesktop.get(GuiConstants.DESMID);mMd=lmd;
			JPanel lbd = pDesktop.get(GuiConstants.DESBOT);mBd=lbd;
			//if(ltd!=null)
				ds.remove(ltd);
			//if(lmd!=null) 
				ds.remove(lmd);
			//if(lbd!=null) 
				ds.remove(lbd);
			ds.removeAll();
			ds.repaint();
			ds.validate();
		}else{
			ds=new JPanel();
			ds.setLayout(new BorderLayout());
			pDesktop.put(GuiConstants.DESKTO, ds);
		}
		
		JLabel lbMsg = new JLabel("Teste de mensagem");
		
		JButton btLoad = new JButton("Carregar");btLoad.setPreferredSize(new Dimension(100,20));
		JButton btLoadC = new JButton("Carr Comp");btLoadC.setPreferredSize(new Dimension(100,20));
		JButton btComp = new JButton("Compilar");btComp.setPreferredSize(new Dimension(100,20));
		JButton btSave = new JButton("Gravar");btSave.setPreferredSize(new Dimension(100,20));
		JButton btCmd = new JButton("Comando");btCmd.setPreferredSize(new Dimension(100,20));
		JButton btBack = new JButton("Voltar");btBack.setPreferredSize(new Dimension(100,20));
		
		btLoad.setActionCommand("LOAD");
		btLoadC.setActionCommand("LOADC");
		btComp.setActionCommand("COMP");
		btSave.setActionCommand("SAVE");
		btCmd.setActionCommand("COMM");
		btBack.setActionCommand("BACK");
		
		btLoad.addActionListener(this);
		btLoadC.addActionListener(this);
		btComp.addActionListener(this);
		btSave.addActionListener(this);
		btCmd.addActionListener(this);
		btBack.addActionListener(this);
		
		textArea1= new JTextPane();
		textArea1.setBackground(new Color(255,255,255));
		ifRepName = new JTextField();ifRepName.setPreferredSize(new Dimension(80,20));
		ifRepName.setName("RPTNAME");
		ifCmd = new JTextField();ifCmd.setPreferredSize(new Dimension(50,20));
		
		//Início Fonte
		Font font = new Font("Courier New", Font.PLAIN, 14);
		textArea1.setFont(font);
		//Fim Fonte
		
		td.setLayout(ofl); 
		md.setLayout(new GridLayout(1,1));
		bd.setLayout(new GridLayout(1,1));
		
		ds.add(td,BorderLayout.NORTH);
		ds.add(md,BorderLayout.CENTER);
		ds.add(bd,BorderLayout.SOUTH);
		
		td.add(ifCmd);
		td.add(btCmd);
		td.add(ifRepName);
		td.add(btLoadC);
		td.add(btLoad);
		td.add(btComp);
		td.add(btSave);
		td.add(btBack);
		
		bd.add(lbMsg);
		
		//mDs=pDesktop.get(GuiConstants.DESKTO);if(mDs==null) mDs=ds;
		/*
		mDs=ds;
		mTd=pDesktop.get(GuiConstants.DESTIT);if(mTd==null) mTd=td;
		mMd=pDesktop.get(GuiConstants.DESMID);if(mMd==null) mMd=md;
		mBd=pDesktop.get(GuiConstants.DESBOT);if(mBd==null) mBd=bd;
		*/
		
		pDesktop.remove(GuiConstants.DESTIT);
		pDesktop.remove(GuiConstants.DESMID);
		pDesktop.remove(GuiConstants.DESBOT);
		
		pDesktop.put(GuiConstants.DESMID, md);
		pDesktop.put(GuiConstants.DESBOT, bd);
		pDesktop.put(GuiConstants.DESKTO, ds);
		
		JScrollPane areaScrollPane = new JScrollPane(textArea1);
		md.add(areaScrollPane);
		
		//Remove
		/*
		if(pDesktop!=null && pDesktop.containsKey(GuiConstants.DESKTO)){
			JPanel op = pDesktop.get(GuiConstants.DESKTO);
			pGui.remove(op);
		}
		*/
		pDesktop.put(GuiConstants.DESTIT, td);
		pDesktop.put(GuiConstants.DESMID, md);
		pDesktop.put(GuiConstants.DESBOT, bd);
		pDesktop.put(GuiConstants.DESKTO, ds);
		
		/*
		pGui.add(ds);
		pGui.validate();
		pGui.repaint();
		
		mGui = pGui;
		*/
		
		ds.repaint();
		ds.revalidate();	
		
		mProtocol = pProtocol;
		mDesktop = pDesktop;
		
	}
	
	 public void actionPerformed(ActionEvent ae) {
		 String sOkCode = ae.getActionCommand();
		 
		 if(sOkCode==null){
			 return;
		 }
		 
		 if(sOkCode.equals("SAVE")){
			 this.Save();
		 }else if(sOkCode.equals("BACK")){
			 this.Back();
		 }else if(sOkCode.equals("LOAD")){
			 this.Load("P");
		 }else if(sOkCode.equals("LOADC")){
			 this.Load("C");
		 }else if(sOkCode.equals("COMP")){
			 this.Compile();
		 }
	 } 
	
	 public void Load(String sTip){
		int i;
		String sTexto,sMsg="",sName="";
		JPanel md,td,bd;
		JScrollPane as;
		JTextPane ta;
		JTextField tf;
		JLabel lb;
		Component ct[]=null;
		 
		td = mDesktop.get(GuiConstants.DESTIT);
		ct = td.getComponents();
		for(i=0;i<ct.length;i++){
			if(ct[i].getName() != null && ct[i].getName().equals("RPTNAME")){
			 	tf= (JTextField) ct[i];
			 	sName = tf.getText().toUpperCase();
			 	tf.setText(sName);
			 	break;
			}
		}
		bd=mDesktop.get(GuiConstants.DESBOT);
		lb = (JLabel) bd.getComponent(0);
		lb.setText("");
		md = mDesktop.get(GuiConstants.DESMID);
		sTexto=textArea1.getText();
		
		//Envia
		if(sTip.equals("P")){
			i=mProtocol.LoadProgram(sName);
		}else{
			i=mProtocol.LoadProgramCompiled(sName);
		}
		sMsg=mProtocol.getAttribute("msg");
		if(i==0){
			sTexto=mProtocol.getAttribute("data");
			try{
				sTexto=URLDecoder.decode(sTexto,"ISO-8859-1");
			}catch(Exception ex){ }
			textArea1.setText(sTexto);
			lb.setText("Programa carregado.");
		}else{
			lb.setText("Erro: "+sMsg);
		}
	 
	 }
	 
	public void Save(){
		int i;
		String sTexto,sName="", sMsg="";
		JPanel md,td,bd;
		JScrollPane as;
		JTextPane ta;
		JTextField tf;
		JLabel lb;
		Component ct[]=null;
		 
		td = mDesktop.get(GuiConstants.DESTIT);
		ct = td.getComponents();
		for(i=0;i<ct.length;i++){
			if(ct[i].getName() != null && ct[i].getName().equals("RPTNAME")){
			 	tf= (JTextField) ct[i];
			 	sName = tf.getText().toUpperCase();
			 	tf.setText(sName);
			 	break;
			}
		}
		bd=mDesktop.get(GuiConstants.DESBOT);
		lb = (JLabel) bd.getComponent(0);
		lb.setText("");
		md = mDesktop.get(GuiConstants.DESMID);
		sTexto=textArea1.getText();
		
		//Envia
		i=mProtocol.SaveProgram(sName, "",  sTexto);
		sMsg=mProtocol.getAttribute("msg");
		if(i==0){
			lb.setText("Sucesso");
		}else{
			lb.setText("Erro: " + sMsg);
		}
	}
	
	public void Compile(){
		int i;
		String sTexto,sName="", sMsg="";
		JPanel md,td,bd;
		JScrollPane as;
		JTextPane ta;
		JTextField tf;
		JLabel lb;
		Component ct[]=null;
		 
		td = mDesktop.get(GuiConstants.DESTIT);
		ct = td.getComponents();
		for(i=0;i<ct.length;i++){
			if(ct[i].getName() != null && ct[i].getName().equals("RPTNAME")){
			 	tf= (JTextField) ct[i];
			 	sName = tf.getText().toUpperCase();
			 	tf.setText(sName);
			 	break;
			}
		}
		bd=mDesktop.get(GuiConstants.DESBOT);
		lb = (JLabel) bd.getComponent(0);
		lb.setText("");
		md = mDesktop.get(GuiConstants.DESMID);
		sTexto=textArea1.getText();
		
		//Envia
		i=mProtocol.CompProgram(sName, sTexto);
		sMsg=mProtocol.getAttribute("msg");
		if(i==0){
			lb.setText("Gravado e Compilado com Sucesso");
		}else{
			lb.setText("Erro: " + sMsg);
		}
	}
	public void Back(){
		//Remove
		JPanel ds,td,md,bd;
		
		if(mDesktop!=null && mDesktop.containsKey(GuiConstants.DESKTO)){
			ds = mDesktop.get(GuiConstants.DESKTO);
			td = mDesktop.get(GuiConstants.DESTIT);
			md = mDesktop.get(GuiConstants.DESMID);
			bd = mDesktop.get(GuiConstants.DESBOT);
			ds.remove(td);
			ds.remove(md);
			ds.remove(bd);
			ds.removeAll();
			ds.repaint();
			ds.validate();
			ds.add(mTd,BorderLayout.NORTH);
			ds.add(mMd,BorderLayout.CENTER);
			ds.add(mBd,BorderLayout.SOUTH);
			ds.repaint();
			ds.validate();
			mDesktop.remove(GuiConstants.DESTIT);
			mDesktop.remove(GuiConstants.DESMID);
			mDesktop.remove(GuiConstants.DESBOT);
			
			mDesktop.put(GuiConstants.DESTIT, mTd);
			mDesktop.put(GuiConstants.DESMID, mMd);
			mDesktop.put(GuiConstants.DESBOT, mBd);
			
		}
		
	}
	
	private void markLine(){
		StyledDocument doc = (StyledDocument)textArea1.getDocument();
		Style style = doc.addStyle("StyleName", null);
		StyleConstants.setBackground(style, Color.BLUE);
		StyleConstants.setForeground(style, Color.WHITE);
		doc.setCharacterAttributes(0,19, style, true);
		textArea1.getCaretPosition();
		System.out.println(textArea1.getCaretPosition());
	}
	
}

