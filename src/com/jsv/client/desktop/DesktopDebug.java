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

import com.jsv.trz.local.trzlocal;
import com.jsv.utils.Utils;

import com.jsv.client.ClientHandleRequest;
import com.jsv.client.ClientProtocol;
import com.jsv.client.ClientSession;
import com.jsv.dictionary.ComponentsDictionary;

public class DesktopDebug implements ActionListener {

	private JPanel mDs=null;
	private JPanel mTd=null;
	private JPanel mMd=null;
	private JPanel mBd=null;
	private HashMap mModulo;
	private HashMap<String, JPanel> mDesktop;
	private ClientProtocol mProtocol=null;
	private ClientSession mSession=null;
	
	JTextPane textArea1=null;
	JTextField ifRepName=null;
	JTextField ifCmd=null;
	JLabel lbMsg=null;
	
	JTextField ifVar01, ifVal01,
	           ifVar02, ifVal02,
	           ifVar03, ifVal03;
	
	private String mRepName="";
	
	// Variáveis para transações locais
	private trzlocal mTrzLocal=null;
	private int mIsTrzLocal=0;
	private String mTrzLocalCommand="";
	// Fim transações locais
	
	private int rc;
	private String errmsg;
	
	private HashMap<String,colsource> mSource=new HashMap<String,colsource>();
	
	public DesktopDebug(){
		
	}
	
	public void create2(HashMap pModulo, ClientSession pSession){
		ClientProtocol lProtocol = new ClientProtocol(pSession);
		mSession = pSession;
		mModulo=pModulo;
		HashMap oDesktop=(HashMap) mModulo.get(ComponentsDictionary.DESKTOP);
		this.create(oDesktop, lProtocol);
	}
	
	public void create3(HashMap<String, JPanel> pDesktop, ClientProtocol pProtocol, trzlocal pTrzLocal, String pCommand){
		ClientSession oSession = pProtocol.getSession();
		mSession = oSession;
		mIsTrzLocal=1;
		mTrzLocalCommand=pCommand;
		mTrzLocal = pTrzLocal;
		this.create(pDesktop, pProtocol);
	}
	
	public void create(HashMap<String, JPanel> pDesktop, ClientProtocol pProtocol){
		
		JPanel ds = null;
		JPanel td = new JPanel();
		JPanel md = new JPanel();
		JPanel bd = new JPanel();
		DebugProcessKey dpk = new DebugProcessKey(this);
		
		FlowLayout ofl = new FlowLayout();
		ofl.setAlignment(FlowLayout.LEFT);
		ofl.setHgap(1);
		
		if(pDesktop!=null && pDesktop.containsKey(GuiConstants.DESKTO)){
			ds = pDesktop.get(GuiConstants.DESKTO);
			JPanel ltd = pDesktop.get(GuiConstants.DESTIT);mTd=ltd;
			JPanel lmd = pDesktop.get(GuiConstants.DESMID);mMd=lmd;
			JPanel lbd = pDesktop.get(GuiConstants.DESBOT);mBd=lbd;
			ds.remove(ltd);
			ds.remove(lmd);
			ds.remove(lbd);
			ds.removeAll();
			ds.repaint();
			ds.validate();
		}else{
			ds=new JPanel();
			ds.setLayout(new BorderLayout());
			pDesktop.put(GuiConstants.DESKTO, ds);
		}
		
		lbMsg = new JLabel("");
		
		JButton btCmd = new JButton("Comando");btCmd.setPreferredSize(new Dimension(80,15));
		JButton btStep = new JButton("F5-Passo");btStep.setPreferredSize(new Dimension(80,15));
		JButton btPall = new JButton("F8-Tudo");btPall.setPreferredSize(new Dimension(80,15));
		
		btCmd.setActionCommand("CMD");
		btStep.setActionCommand("STEP");
		btPall.setActionCommand("PALL");
		
		btCmd.addActionListener(this);
		btStep.addActionListener(this);
		btPall.addActionListener(this);
		
		btCmd.addKeyListener(dpk);
		btStep.addKeyListener(dpk);
		btPall.addKeyListener(dpk);
		
		textArea1 = new JTextPane();
		textArea1.setBackground(Color.getHSBColor(10,36,217));
		textArea1.setEditable(false);

		//Início Fonte
		Font font = new Font("Courier New", Font.PLAIN, 14);
		textArea1.setFont(font);
		//Fim Fonte 
		
		ifRepName = new JTextField();ifRepName.setPreferredSize(new Dimension(80,15));
		ifRepName.setName("RPTNAME");
		ifRepName.addKeyListener(dpk);
		ifCmd = new JTextField();ifCmd.setPreferredSize(new Dimension(50,15));
		ifCmd.addKeyListener(dpk);
		
		td.setLayout(ofl); 
		md.setLayout(new GridLayout(1,1));
		bd.setLayout(new GridLayout(1,1));
		
		td.addKeyListener(dpk);
		md.addKeyListener(dpk);
		bd.addKeyListener(dpk);
				
		ds.add(td,BorderLayout.NORTH);
		ds.add(md,BorderLayout.CENTER);
		ds.add(bd,BorderLayout.SOUTH);
		
		td.add(ifCmd);
		td.add(btCmd);
		td.add(ifRepName);
		td.add(btStep);
		td.add(btPall);
		
		textArea1.addKeyListener(dpk);
		//Variáveis
		ifVar01 = new JTextField();ifVar01.setName("var1");ifVar01.addKeyListener(dpk);
		ifVal01 = new JTextField();ifVal01.setName("var1l");ifVal01.addKeyListener(dpk);
        ifVar02 = new JTextField();ifVar02.setName("var2");ifVar02.addKeyListener(dpk);
        ifVal02 = new JTextField();ifVal02.setName("var2l");ifVal02.addKeyListener(dpk);
        ifVar03 = new JTextField();ifVar03.setName("var3");ifVar03.addKeyListener(dpk);
        ifVal03 = new JTextField();ifVal03.setName("var3l");ifVal03.addKeyListener(dpk);
        bd.setLayout(new GridLayout(3,3));
		//bd.add(lbMsg);
        bd.add(ifVar01);bd.add(ifVal01);bd.add(ifVar02);
        bd.add(ifVal02);bd.add(ifVar03);bd.add(ifVal03);
        
		pDesktop.remove(GuiConstants.DESTIT);
		pDesktop.remove(GuiConstants.DESMID);
		pDesktop.remove(GuiConstants.DESBOT);
		
		pDesktop.put(GuiConstants.DESMID, md);
		pDesktop.put(GuiConstants.DESBOT, bd);
		pDesktop.put(GuiConstants.DESKTO, ds);
		
		JScrollPane areaScrollPane = new JScrollPane(textArea1);
		md.add(areaScrollPane);
		
		pDesktop.put(GuiConstants.DESTIT, td);
		pDesktop.put(GuiConstants.DESMID, md);
		pDesktop.put(GuiConstants.DESBOT, bd);
		pDesktop.put(GuiConstants.DESKTO, ds);
		
		ds.repaint();
		ds.revalidate();	
		
		mProtocol = pProtocol;
		mDesktop = pDesktop;
		
		String sName = mProtocol.getAttribute("repname");
		String sLine = mProtocol.getAttribute("line");
		int iLine = Integer.parseInt(sLine);
		this.setLine(sName, iLine);
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		 String sOkCode = ae.getActionCommand();
		 
		 if(sOkCode==null){
			 return;
		 }
		 
		 if(sOkCode.equals("STEP")){
			 this.Step();
		 }else if(sOkCode.equals("PALL")){
			 this.Pall();
		/*	 
		 }else if(sOkCode.equals("LOAD")){
			 this.Load();
		 }else if(sOkCode.equals("COMP")){
			 this.Compile();*/
		 }
	 }
	
	public void GetVar(String s, String n){
		int i;
		String s1;
		JPanel bd;
		JTextField tf;
		Component ct[];
		if(s==null || s.length()==0) { return; }
		s=s.toUpperCase();
		i=mProtocol.getVarInDebug(s);
		if(i==0){
			s1=mProtocol.getAttribute("data");
			n+="l";
			bd = mDesktop.get(GuiConstants.DESBOT);
			ct = bd.getComponents();
			for(i=0;i<ct.length;i++){
				if(ct[i].getName() != null && ct[i].getName().equals(n)){
				 	tf= (JTextField) ct[i];
				 	tf.setText(s1);
				 	break;
				}
			}
		}
	}
	
	public void GetVars(){
		this.GetVar(ifVar01.getText(),ifVar01.getName());
		this.GetVar(ifVar02.getText(),ifVar02.getName());
		this.GetVar(ifVar03.getText(),ifVar03.getName());
	}
	
	
	public void Step(){
		boolean b=true;
		int i,iLine;
		String sRepName="";
		String sMsg="",sCmd,s;
		lbMsg.setText("");
		
		do{
			i=mProtocol.Goto();
			if(i==0){
				sCmd=mProtocol.getServerCommand();
				if(!sCmd.equals("debug")){
					b=false;
					break;
				}
				s=mProtocol.getAttribute("line");
				iLine=Integer.parseInt(s);
				if(iLine!=0){
					b=false;
					break;
				}
			}else{
				b=false;
			}
		}while(b==true);
		
		if(i==0){
			sCmd=mProtocol.getServerCommand();
			if(!sCmd.equals("debug")){ //Finaliza
				this.Back();
				if(mIsTrzLocal==1){
					mTrzLocal.execAction(mTrzLocalCommand);
				}
				return;
			}
			s=mProtocol.getAttribute("line");
			iLine=Integer.parseInt(s);
			sRepName=mProtocol.getAttribute("repname").toUpperCase();
			this.setLine(sRepName, iLine);
			this.GetVars();
		}else{
			lbMsg.setText("Erro");
		}
	}
	
	public void Pall(){
		boolean b=false;
		int i;
		String sCmd;
		i=mProtocol.DebugF8();
		if(i==0){
			sCmd=mProtocol.getServerCommand();
			if(!sCmd.equals("debug")){
				b=false;
				this.Back();
			}
		}
	}
	
	public void setLine(String pName, int pLine){
		StyledDocument doc;
		Style style;
		String sSource;
		if(pLine==0){ return; }
		pLine--;
		if(!mSource.containsKey(pName)){
			sSource=Load(pName);
			if(rc!=0){
				return;
			}
			this.setSource(pName, sSource);
		}
	
		colsource cs = mSource.get(pName);
		
		if(!mRepName.equals(pName)){
			textArea1.setText(cs.Data);
			mRepName=pName;
			ifRepName.setText(mRepName);
		}
		
		line ldb = cs.Debuged;
		line l = cs.Lines[pLine];
		
		if(ldb!=null){
			doc = (StyledDocument)textArea1.getDocument();
			style = doc.addStyle("StyleName", null);
			StyleConstants.setBackground(style, textArea1.getBackground());
			StyleConstants.setForeground(style, textArea1.getForeground());
			doc.setCharacterAttributes(ldb.ini,ldb.fin-ldb.ini, style, true);
		}
		
		//line l = cs.Lines[23];
		
		cs.Debuged=l;
		doc = (StyledDocument)textArea1.getDocument();
		style = doc.addStyle("StyleName", null);
		textArea1.setCaretPosition(l.ini);
		StyleConstants.setBackground(style, Color.BLUE);
		StyleConstants.setForeground(style, Color.WHITE);
		doc.setCharacterAttributes(l.ini,l.fin-l.ini, style, true);
		
	}
	
	public void setSource(String pName, String pData){
		byte b[];
		line oline[]=null;
		colsource cs = new colsource();
		int i,iTam,iEnt=0,icount;
		line l;
		
		ifRepName.setText(pName);
		b=pData.getBytes();
		iTam=b.length;
		for(i=0;i<iTam;i++){
			if(b[i]==10){
				iEnt++;
			}
		}
		
		oline=new line[iEnt];
		l = new line();
		l.ini=0;
		iEnt=0;
		icount=0;
		for(i=0;i<iTam;i++){
			if(b[i]==10){
				icount++;
				l.fin=icount;
				oline[iEnt] = l;
				iEnt++;
				l=new line();
				l.ini=icount;
			}
			if(b[i]!=10 && b[i]!=13){
				icount++;
			}
		}
		
		cs.Name=pName;
		cs.Data=pData;
		cs.Lines = oline;
		cs.Debuged=null;
		mSource.put(pName,cs);
		
	}
	
	public String Load(String pName){
		int i;
		String sTexto="",sMsg="",sName="";
		JPanel md,td,bd;
		JScrollPane as;
		JTextPane ta;
		JTextField tf;
		Component ct[]=null;
		 
		rc=0;
		td = mDesktop.get(GuiConstants.DESTIT);
		ct = td.getComponents();
		sName=pName.toUpperCase();
		
		bd=mDesktop.get(GuiConstants.DESBOT);
		lbMsg.setText("");
		md = mDesktop.get(GuiConstants.DESMID);
		sTexto=textArea1.getText();
		
		//Envia
		i=mProtocol.LoadProgram(sName);
		sMsg=mProtocol.getAttribute("msg");
		if(i==0){
			sTexto=mProtocol.getAttribute("data");
			try{
				sTexto=URLDecoder.decode(sTexto,"ISO-8859-1");
			}catch(Exception ex){ }
			textArea1.setText(sTexto);
			lbMsg.setText("Programa carregado.");
		}else{
			rc=4;
			lbMsg.setText("Erro: "+sMsg);
		}
		return(sTexto);
	 
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
			 	sName = tf.getText();
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
			 	sName = tf.getText();
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
		int i;
		JPanel ds,td,md,bd;
		ClientHandleRequest ocl = new ClientHandleRequest();
		
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
			
			//Deixa mensagem em branco
			Component cp[] = mBd.getComponents();
			JLabel lb1;
			
			for(i=0;i<cp.length;i++){
				if(cp[i].getName().equals("msg")){
					lb1 = (JLabel) cp[i];
				    lb1.setText("");
				    break;
				}
			}
			
			if(mIsTrzLocal==1){
				mTrzLocal.execAction(mTrzLocalCommand);
			}else{
				ocl.setMemModulo(mModulo);
				ocl.setSession(mSession);
				ocl.loadResponse();
			}
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
