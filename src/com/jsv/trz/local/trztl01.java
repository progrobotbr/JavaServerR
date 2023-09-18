package com.jsv.trz.local;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsr.util._;
import com.jsv.utils.Utils;
import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.ClientProtocol;
import com.jsv.client.ClientRequest;
import com.jsv.client.ClientResponse;
import com.jsv.client.ClientSession;
import com.jsv.client.IServerCommand;
import com.jsv.client.desktop.Desktop2;
import com.jsv.client.desktop.DesktopScreenPainter;
import com.jsv.client.desktop.GuiConstants;
import com.jsv.client.desktop.IDesktop;
import com.jsv.client.main.ClientMain;
import com.jsv.trz.local.component.ScreenPainter;
import com.jsv.dictionary.ComponentsDictionary;

public class trztl01 extends trzbase implements ActionListener, trzlocal {
	
	private ClientMain               Window; 
	private ClientBrowserTransaction Transaction;
	private IDesktop                 Desktop;
	
	//Variáveis e componentes globais
	ScreenPainter dsp;
	JTextField itTrz; 
	JTextField ifScreen;
	JTextField ifDescr;
	JTextField jtf1; 
	JTextField jtf2;
	JLabel lbMsg;
	DefaultMutableTreeNode mDmt;
	JTree mTree;
	JTextArea mTextMsg;
	RSyntaxTextArea mTextArea; // = new RSyntaxTextArea(20, 60);
	
	private static final String TRZID = "TL01";
	private static final String SCREEN = "1000";
	private static final String CIITRZ = "ittrz";
	private int rc=0;
	
	private static void createAndShowGUI() {
		//trzpt01 os = new trzpt01();
		//os.create();
	}

	public void create(ClientBrowserTransaction transaction){
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			//System.out.println(UIManager.getLookAndFeel().getName());
			//UIManager.setLookAndFeel("Metal");
		}catch(Exception ex){
			this.log(Utils.writeStackTrace(ex));
		}
		
		//Inicialização
		this.Window = transaction.Window; 
		super.Session = this.Window.Session;
		Transaction = transaction;
		Desktop = new Desktop2(Transaction);
		super.Desktop = Desktop;
		
		//Montagem da tela
		JPanel ptb   = new JPanel(); //Toolbar
		JPanel pn1 = Desktop.getBody();
		JPanel pn21  = new JPanel();
		JPanel pn22  = new JPanel();
		JPanel pn23  = new JPanel();
		JPanel pn11  = new JPanel(); //Esquerda superior
		JPanel pn12  = new JPanel();
		JPanel pn13  = new JPanel();
		JPanel pn131 = new JPanel();
		lbMsg=new JLabel(" ");
		
		//JPanel pnMid;
		
		dsp = new ScreenPainter();
		dsp.create();
		dsp.setLabelMsg(lbMsg);

		//Esquerda superior
		pn11.setLayout(new BorderLayout());
		JPanel pn11South = new JPanel();
		pn11South.setLayout(new GridLayout(2,1));
		JButton btPesq = new JButton("Pesq"); btPesq.setActionCommand("PESQ");btPesq.addActionListener(this);
		
		btPesq.setPreferredSize(new Dimension(70,20));
		itTrz = new JTextField();
		itTrz.setActionCommand(trztl01.CIITRZ);
		itTrz.setPreferredSize(new Dimension(100,20));
		itTrz.addActionListener(this);
		
		JPanel pn11South1 = new JPanel();
		JPanel pn11South2 = new JPanel();
		
		pn11South1.setLayout(new FlowLayout());
		pn11South2.setLayout(new GridLayout(4,1));
		
		pn11South1.add(itTrz);
		pn11South1.add(btPesq);
		//
		pn11South.add(pn11South1);
		pn11South.add(pn11South2);
		pn11.add(pn11South,BorderLayout.SOUTH);
		
		//
		pn12.setLayout(new GridLayout(1,1));
		
		mDmt = new DefaultMutableTreeNode("USRL: Usuário");
		DefaultMutableTreeNode dmt2 = new DefaultMutableTreeNode("quarto");
		//dmt.add(dmt2);
		//for(int i=0;i<30;i++){
		    mDmt.add(new DefaultMutableTreeNode("Trz  USRL: Cadastro de Usuários"));
		    mDmt.add(new DefaultMutableTreeNode("Tela 1000: Cadastro de Usuários"));
		    mDmt.add(new DefaultMutableTreeNode("Tela 2000: Consulta de Perfil"));
		    mDmt.add(new DefaultMutableTreeNode("Tela 3000: Ativar Usuário"));
		    mDmt.add(new DefaultMutableTreeNode("Tela 4000: Dados Básicos de Endereço"));
		    mDmt.add(new DefaultMutableTreeNode("Tela 5000: Atividade Básicas"));
		//}
		//dmt2.add(new DefaultMutableTreeNode("mesa"));
		
		mTree = new JTree(mDmt);
		mTree.addMouseListener(new MouseAdapter() {
		      public void mouseClicked(MouseEvent me) {
		    	  if(me.getClickCount()==2){
		    		  treeClicked(me);
		    	  }	 
		      }
		});

		JScrollPane jsp = new JScrollPane(mTree);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		pn12.add(jsp);
		
		JTabbedPane jtp = new JTabbedPane();
		jtp.add("Dados Básicos", pn21);
		jtp.add("Programa", pn23);
		jtp.add("Layout", pn22);
		
		//Dados Básicos
		pn21.setLayout(new BorderLayout());
		JPanel pn211 = new JPanel();
		pn211.setLayout(new BorderLayout());
		
		ifScreen = new JTextField();               //ifScreen.setBounds(300, 30, 80, 20);
		ifDescr = new JTextField();                //ifDescr.setBounds(30, 30, 80, 40);
		JLabel jlScreen = new JLabel("Tela");      //jlScreen.setBounds(30, 30, 80,20);
		JLabel jlDescr  = new JLabel("Descrição"); //jlDescr.setBounds(30, 70, 80,200);
		//ifScreen.setSize(300, 20);
		//ifDescr.setSize(300, 20);
		//ifDescr.setSize(300, 80);
		JPanel pn2111 = new JPanel();
		//pn2111.setLayout(null);
		pn2111.setLayout(new GridLayout(2,2));
		//pn2111.setLayout(new FlowLayout());
		//pn2111.setSize(500,500);
		//JPanel pn2112 = new JPanel();
		//pn2112.setLayout(new GridLayout(2,2));
		
		pn2111.add(jlScreen);
		pn2111.add(ifScreen);
		pn2111.add(jlDescr);
		pn2111.add(ifDescr);
		
		pn211.add(pn2111, BorderLayout.LINE_START); //.LINE_START);
	    //	pn211.add(pn2112, BorderLayout.WEST);
		
		pn21.setLayout(new BorderLayout());
		pn21.add(pn211,BorderLayout.NORTH);
		
		//pn22.add(new JButton("teste"));
		pn22.setLayout(new GridLayout(1,1));
		pn22.add(dsp);
		
		pn23.setLayout(new GridLayout(1,1));
		//mTextArea = new JTextArea();
		mTextArea = new RSyntaxTextArea(20, 60);
		mTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		mTextArea.setCodeFoldingEnabled(true);
		mTextArea.setAntiAliasingEnabled(true);
		
		//Evento Contador de Linha
		mTextArea.addCaretListener(new CaretListener() {
			 public void caretUpdate(CaretEvent e) {
				 JTextArea editArea = (JTextArea)e.getSource();
				 int linenum = 1;                 
				 int columnnum = 1;                 
				 try {                                  
					 int caretpos = editArea.getCaretPosition();
					 Rectangle caretCoords = editArea.modelToView(caretpos);
					 int lineHeight = editArea.getFontMetrics(editArea.getFont()).getHeight();
					 int posLine = ((int)caretCoords.getY() / lineHeight) + 1; 
					 lbMsg.setText("Caracter: "+caretpos+" Linha: "+posLine);
					 
					} catch(Exception ex) { }
				 }
			 });
		//Fim Evento Contador de Linha
		
		//Início Fonte
		//Font font = new Font("Courier New", Font.PLAIN, 14);
		Font font = new Font("Courier New", Font.TRUETYPE_FONT, 14);
		//Font font = new Font("Consolas", Font.TRUETYPE_FONT, 10);
		//*********
		//mTextArea.setFont(font);
		//********
		mTextArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});
		//Fim Fonte
		
		JPanel pn231 = new JPanel();
		JPanel pn232 = new JPanel();
		pn231.setLayout(new GridLayout());
		pn232.setLayout(new GridLayout());
		
		JSplitPane vt2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pn231, pn232);
		//pn232.setSize(0, 40);
		vt2.setDividerSize(6);
		vt2.setDividerLocation(1000);
		
		mTextMsg = new JTextArea();
		//mTextMsg.setEnabled(false);//.setEditable(false);
		//mTextMsg.setBackground(Color.lightGray);
		//mTextMsg.setPreferredSize(new Dimension(100,20));
		//pn23.add(new JScrollPane(mTextArea));
		
		RTextScrollPane sp = new RTextScrollPane(mTextArea);
		sp.setFoldIndicatorEnabled(true);
		////////////////////pn231.add(new JScrollPane(mTextArea));
		pn231.add(sp);
		
		JScrollPane ssp1=new JScrollPane(mTextMsg);
		//ssp1.setMinimumSize(new Dimension(100,60));
		pn232.add(ssp1);
		//pn232.setMinimumSize(new Dimension(100,60));
		pn23.add(vt2); //,BorderLayout.CENTER);
		
		JSplitPane vt = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pn11, pn12);
		vt.setDividerSize(6);
		vt.setDividerLocation(80);
		
		pn13.setLayout(new BorderLayout());
		pn13.add(jtp,BorderLayout.CENTER);
		
		FlowLayout fll = new FlowLayout();
		fll.setAlignment(FlowLayout.LEFT);
		pn131.setLayout(fll);
		jtf1 = new JTextField(); 
		jtf1.setPreferredSize(new Dimension(100,20));
		jtf1.setEnabled(false);
		jtf2 = new JTextField();
		jtf2.setPreferredSize(new Dimension(100,20));
		jtf2.setEnabled(false);
		
		pn131.add(new JLabel("Transação:"));
		pn131.add(jtf1);
		pn131.add(new JLabel("Tela:"));
		pn131.add(jtf2);
		
		pn13.add(pn131,BorderLayout.NORTH);
		
		JSplitPane ht = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vt, pn13);
		ht.setDividerSize(6);
		ht.setDividerLocation(200);
				
		pn1.setLayout(new BorderLayout());
		
		JButton btSave = new JButton("Gravar");btSave.setActionCommand("GRVA");btSave.addActionListener(this);
		JButton btComp = new JButton("Compilar");btComp.setActionCommand("COMP");btComp.addActionListener(this);
		JButton btNew = new JButton("Nova Transacao"); btNew.setActionCommand("NTRZ");btNew.addActionListener(this);
		JButton btScn = new JButton("Nova Tela");btScn.setActionCommand("NSCN");btScn.addActionListener(this);
		JButton btDelT = new JButton("Excluir Transacao");btDelT.setActionCommand("DELT");btDelT.addActionListener(this);
		JButton btDelS = new JButton("Excluir Tela");btDelS.setActionCommand("DELS");btDelS.addActionListener(this);
		
		btSave.setPreferredSize(new Dimension(120,20));
		btComp.setPreferredSize(new Dimension(120,20));
		btNew.setPreferredSize(new Dimension(120,20));
		btScn.setPreferredSize(new Dimension(120,20));
		btDelT.setPreferredSize(new Dimension(120,20));
		btDelS.setPreferredSize(new Dimension(120,20));
		
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		ptb.setLayout(fl);
		//ptb.add(btCrea);
		ptb.add(btNew);
		ptb.add(btScn);
		ptb.add(btDelT);
		ptb.add(btDelS);
		ptb.add(btSave);
		ptb.add(btComp);
		
		pn1.add(ptb, BorderLayout.NORTH);
		pn1.add(ht,BorderLayout.CENTER);
		pn1.add(lbMsg,BorderLayout.SOUTH);
		pn1.setVisible(true);
		
		this.onLoad();
		this.print("Carregado com sucesso");
		
		Transaction.setDesktopInWindow();
		
	}
	
	private void writeMsg(String s){
		lbMsg.setText(s);
	}
	
	/*
	 *  Controlador de eventos
	 */
	public void actionPerformed(ActionEvent ae){
		String s = ae.getActionCommand();
		this.clearMsg();
		if(s.equals("PESQ")){
			this.clickSearchTrz();
		}else if(s.equals("NTRZ")){
			this.clickNewTrz();
		}else if(s.equals("NSCN")){
			this.clickNewScreen();
		}else if(s.equals("DELT")){
			this.clickDelTrz();
		}else if(s.equals("DELS")){
			this.clickDelScreen();
		}else if(s.equals("GRVA")){
			this.clickSaveTrz();
		}else if(s.equals("COMP")){
			this.clickCompileScr();
		}else if(s.equals(trztl01.CIITRZ)){
			this.clickSearchTrz();
		}
		
	}

	public void execAction(String s){
		if(s.equals("PESQ")){
			this.getServerMsg();
			this.fillTree();
		}else if(s.equals("COMP")){
			this.getServerMsg();
		}else if(s.equals("GPRG")){
			this.getServerMsg();
			this.fillEdit();
			this.fillPaint();
		}else if(s.equals("NTRZ")){
			this.getServerMsg();
			this.fillTree();
		}else if(s.equals("NSCN")){
			this.getServerMsg();
			this.fillTree();
		}
	}
	
	//Validacoes
	private boolean validTrzName(String s){
		boolean b=false;
		if(s.length()>0){
			b=true;
		}else{
			this.print("Informe o nome da transação");
		}
		return(b);
		
	}
	//Eventos
	public void onLoad(){
	}
	
	public void onUnLoad(){
		
	}
	
	private void clickSearchTrz(){
		rc=4;
		String sCmd;
		String s = itTrz.getText().toUpperCase();
		itTrz.setText(s);
		
		try{
			this.generateXMLHeader("TRZSRC", TRZID, SCREEN);
			this.generateXMLVarValue(ComponentsDictionary.IF, "PESQNMTRZ", s);
			this.generateXMLFooter();
			this.executeRequest();
			sCmd = this.getServerCommand();
			if(sCmd.equals("debug")){
				//this.loadDebug(mDesktop,this,"PESQ");
				return;
			}
			this.getServerMsg();
			if(rc==0){
				if(this.fillTree()){
					jtf1.setText(s);
				}
			}else{
				this.writeMsg("Transação não encontrada");
			}
		}catch(Exception ex){
		   this.writeMsg("Erro ao carregar transação: "+itTrz.getText());	
		}
	}
	
	private void clickTrz(){ //carrega a tela de edição do programa base
	}
	
    //--> Get Screen
	public void treeClicked(MouseEvent me){
		String s;
		Object bb[];
		TreePath tp = mTree.getPathForLocation(me.getX(), me.getY());
	    if (tp != null){
	    	bb = tp.getPath();
	    	if(bb!=null && bb.length==2){
	    		dsp.clearPainter();
	    		s=bb[1].toString();
	    		lbMsg.setText(s);
	    		this.getScreen(s);
	    	}
	    }else
		   	lbMsg.setText("");
	}
	
	public void getScreen(String s){
		int i;
		String sName="",t,d;
		String sTrz = itTrz.getText().toUpperCase();
		itTrz.setText(sTrz);
		
		if(s.startsWith("SCN")){
			t=s.substring(3,7);
			i=s.lastIndexOf(":");
			d=s.substring(++i);
			ifScreen.setText(t);
			ifDescr.setText(d);
			jtf1.setText(itTrz.getText());
			jtf2.setText(t);
			sName="SCN"+t+itTrz.getText();
		}else{
			ifScreen.setText("");
			ifDescr.setText("");
			jtf1.setText(itTrz.getText());
			jtf2.setText("");
			i=s.lastIndexOf(":");
			sName=s.substring(0,i);
		}
		this.getProgram(sTrz, sName);
	}
	
	private void getProgram(String pTrz, String pName){ //carrega a tela para edição de telas
		String sCmd;
		rc=4;
		this.generateXMLHeader("SCRGET", TRZID, SCREEN);
		this.generateXMLVarValue(ComponentsDictionary.IF, "DISPNMSCN", pName);
		this.generateXMLVarValue(ComponentsDictionary.IF, "DISPNMTRZ", pTrz);
		this.generateXMLFooter();
		this.executeRequest();
		sCmd = this.getServerCommand();
		if(sCmd.equals("debug")){
			//this.loadDebug(mDesktop,this,"GPRG");
			return;
		}
		this.getServerMsg();
		if(rc==0){
			this.fillEdit();
			this.fillPaint();
		}
	}
	
	//--> Fim Get Screen
	
	private void clickNewTrz(){
		rc=4;
		String sCmd;
		String s = itTrz.getText().toUpperCase();
		itTrz.setText(s);
		this.generateXMLHeader("TRZNEW", TRZID, SCREEN);
		this.generateXMLVarValue(ComponentsDictionary.IF, "PESQNMTRZ", s);
		this.generateXMLFooter();
		this.executeRequest();
		sCmd = this.getServerCommand();
		if(sCmd.equals("debug")){
			//this.loadDebug(mDesktop,this,"NTRZ");
			return;
		}
		this.getServerMsg();
		if(rc==0){
			if(this.fillTree()){
				jtf1.setText(s);
			}
		}
	}
	
	private void clickNewScreen(){
		rc=4;
		String sCmd;
		String st = itTrz.getText().toUpperCase();
		String sc = ifScreen.getText();
		
		if(sc.trim().length()==0){
			lbMsg.setText("Inform the screen number");
			return;
		}
		
		this.generateXMLHeader("SCRNEW", TRZID, SCREEN);
		this.generateXMLVarValue(ComponentsDictionary.IF, "EDITNMTRZ", st);
		this.generateXMLVarValue(ComponentsDictionary.IF, "EDITNMSCN", sc);
		this.generateXMLFooter();
		this.executeRequest();
		sCmd = this.getServerCommand();
		if(sCmd.equals("debug")){
			//this.loadDebug(mDesktop,this,"NSCN");
			return;
		}
		this.getServerMsg();
		if(rc==0){
			this.fillTree();
		}
		
	}
	private void clickDelTrz(){
		String s;
		s=jtf2.getText();
		dsp.setScreenNr(s);
		dsp.metadados(null);
		log(dsp.mMetaDadosP1);
	}
    private void clickDelScreen(){
    }
    private void clickSaveTrz(){
    }
	private void clickCompileScr(){
		//int i,t,z, idx;
		String sCmd;
		String sScreen;
		String ss = jtf2.getText();  //Screen
		String sTrz = jtf1.getText(); //Trz
		String sText = mTextArea.getText();
		String sP1, sP2;
		
		lbMsg.setText("");
		//Formata o nome do programa
		if(ss.length()==0){
			ss="TRZ"+jtf1.getText();
			sP1="";
			sP2="";
		}else{
			sScreen=ss;
			ss="SCN"+ss+jtf1.getText();
			if(dsp.metadados(sScreen+"")!=true){
				return;
			}
			sP1 = dsp.getProtocolP1(sScreen);
			sP2 = dsp.getProtocolP2(sScreen);
		}
		
		this.generateXMLHeader("SCRCOM", TRZID, SCREEN);
		this.generateXMLVarValue(ComponentsDictionary.IF, "EDITNMTRZ", sTrz);
		this.generateXMLVarValue(ComponentsDictionary.IF, "DISPNMTRZ", ss);
		this.putServerTbSource("TBSOURCE", sText);
		this.putServerTbSource("TBP1", sP1);
		this.putServerTbSource("TBP2", sP2);
		this.generateXMLFooter();
		this.executeRequest();
		sCmd = this.getServerCommand();
		if(sCmd.equals("debug")){
			//this.loadDebug(mDesktop,this,"COMP");
			return;
		}
		
		//Retorno
		this.getServerMsg();
		this.fillCompMsg();
		//Retorno
	}
	
	public void putServerTbSource(String pNameTb, String pText){
		int i,t,z,idx;
		String sLin;
		this.generateXMLTableHeader(pNameTb, "2");
		
		/*** regra de passar tudo na primeira linha colocada em 01/09/2015 ***/
		this.generateXMLTableLine(""+1);
		this.generateXMLTableColumn("LINNR", "", "1");
		this.generateXMLTableColumn("LINTX", pText, "2");
		this.generateXMLTableLineClose();
		
		this.generateXMLTableHeaderClose();
		
		if(1!=2){
			return;
		}
		
		/*** regra de passar tudo na primeira linha colocada em 01/09/2015 ***/
		
		t = pText.length();
		i=0;
		if(t<80){z=t;}else{z=80;}
		idx=1;
		while(i<t){
			sLin = pText.substring(i, z);
			this.generateXMLTableLine(""+idx);
			this.generateXMLTableColumn("LINNR", "", "1");
			this.generateXMLTableColumn("LINTX", sLin, "2");
			this.generateXMLTableLineClose();
			i=z;
			if(z==t){ break; }
			z+=80;
			if(z>t){
				z-=80;
				z=z+(t-z);
				idx++;
				sLin = pText.substring(i, z);
				this.generateXMLTableLine(""+idx);
				this.generateXMLTableColumn("LINNR", "", "1");
				this.generateXMLTableColumn("LINTX", sLin, "2");
				this.generateXMLTableLineClose();
				break;
			}
			idx++;
		}
		this.generateXMLTableHeaderClose();
	}
	
	public void getServerMsg(){
		int i,t;
		String sLin;
		Document omem=super.getMem2();
		Element oElem = omem.getDocumentElement();
		NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.IF);
		Node oNo1;
		t=oNl1.getLength();
		for(i=0;i<t;i++){
			oNo1 = oNl1.item(i);
			sLin = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
			if(sLin.equals("GMESS")){
				sLin = Utils.getAttribute(oNo1,ComponentsDictionary.VL);
				lbMsg.setText(sLin);
			}else if(sLin.equals("RC")){
				sLin = Utils.getAttribute(oNo1,ComponentsDictionary.VL);
				rc = Integer.parseInt(sLin);
			}
		}
	}
	//
	public boolean  fillTree(){
		String s;
		Document omem;
		mDmt.removeAllChildren();
		DefaultTreeModel model = (DefaultTreeModel) mTree.getModel(); 
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot(); 
		root.removeAllChildren();
		DefaultMutableTreeNode root2;
		//mTree.validate();
		if(super.RC!=0){
			return(false);
		}
		omem=super.getMem2();
		Element oElem = omem.getDocumentElement();
		NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.TB);
		Node oNo1 = oNl1.item(0);
		s = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
		if(!s.equals("TBTREE")){
			return(false);
		}
		root2 = new DefaultMutableTreeNode(itTrz.getText());
		int t,i,i2,t2;
		NodeList oNl2 = oNo1.getChildNodes();
		Node oNo2,oNo3;
		NodeList oNl3;
		oNl2 = oNo1.getChildNodes();
		t=oNl2.getLength();
		for(i=0;i<t;i++){
			oNo2 = oNl2.item(i); // tr?
			oNl3 = oNo2.getChildNodes();
			t2=oNl3.getLength();
			//for (i2=3; i2 < 4; i2++) {
				oNo3=oNl3.item(1);
				s=Utils.getAttribute(oNo3,ComponentsDictionary.VL);
				oNo3=oNl3.item(3);
				s+=":"+Utils.getAttribute(oNo3,ComponentsDictionary.VL);
				//tm.setValueAt(s,i,i2);
				root2.add( new DefaultMutableTreeNode(s));
				
			//}
		}
		model.setRoot(root2);
		model.reload();
		return(true);
		//mDmt.add(new DefaultMutableTreeNode("Trz  USRL: Cadastro de Usuários"));
		
	}
	
	public void fillEdit(){
		String s,tx;
		Document omem;
		
		if(super.RC!=0){
			return;
		}
		tx="";
		mTextArea.setText(tx);
		mTextMsg.setText(tx);
		
		omem=super.getMem2();
		Element oElem = omem.getDocumentElement();
		NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.TB);
		Node oNo1 = oNl1.item(0);
		s = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
		if(!s.equals("TBSOURCE")){
			return;
		}
		int t,i,i2,t2;
		NodeList oNl2 = oNo1.getChildNodes();
		Node oNo2,oNo3;
		NodeList oNl3;
		oNl2 = oNo1.getChildNodes();
		t=oNl2.getLength();
		for(i=0;i<t;i++){
			oNo2 = oNl2.item(i); // tr?
			oNl3 = oNo2.getChildNodes();
			t2=oNl3.getLength();
		
			oNo3=oNl3.item(1);
			s=Utils.getAttribute(oNo3,ComponentsDictionary.VL);
			try{
				s=URLDecoder.decode(s,"ISO-8859-1");
				tx+=s;
			}catch(Exception ex){}
			
		}
		
		mTextArea.setText(tx);
		log("prog:"+tx);
		mTextArea.setCaretPosition(1);
		
	}
	
	public void fillCompMsg(){
		String s,tx;
		Document omem;
		
		if(super.RC!=0){
			return;
		}
		tx="";
		mTextMsg.setText(tx);
		
		omem=super.getMem2();
		Element oElem = omem.getDocumentElement();
		NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.TB);
		Node oNo1 = oNl1.item(0);
		s = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
		if(!s.equals("TBMSG")){
			return;
		}
		int t,i,i2,t2;
		NodeList oNl2 = oNo1.getChildNodes();
		Node oNo2,oNo3;
		NodeList oNl3;
		oNl2 = oNo1.getChildNodes();
		t=oNl2.getLength();
		for(i=0;i<t;i++){
			oNo2 = oNl2.item(i); // tr?
			oNl3 = oNo2.getChildNodes();
			t2=oNl3.getLength();
		
			oNo3=oNl3.item(1);
			s=Utils.getAttribute(oNo3,ComponentsDictionary.VL);
			try{
				s=URLDecoder.decode(s,"ISO-8859-1");
				tx+=s;
			}catch(Exception ex){}
			
		}
		
		mTextMsg.setText(tx);
		
	}
	
	private void fillPaint(){
		String s,tx;
		StringBuilder sb=new StringBuilder();
		Document omem;
		
		if(super.RC!=0){
			return;
		}
		tx="";
		omem=super.getMem2();
		Element oElem = omem.getDocumentElement();
		NodeList oNl1 = oElem.getElementsByTagName(ComponentsDictionary.TB);
		Node oNo1 = oNl1.item(1);
		s = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
		
		if(!s.equals("TBP1")){
			return;
		}
		
		int t,i,i2,t2;
		NodeList oNl2 = oNo1.getChildNodes();
		Node oNo2,oNo3;
		NodeList oNl3;
		oNl2 = oNo1.getChildNodes();
		t=oNl2.getLength();
		for(i=0;i<t;i++){
			oNo2 = oNl2.item(i); // tr?
			oNl3 = oNo2.getChildNodes();
			t2=oNl3.getLength();
		
			oNo3=oNl3.item(1);
			s=Utils.getAttribute(oNo3,ComponentsDictionary.VL);
			sb.append(s);
		}
		try{
			s = sb.toString();
			tx=URLDecoder.decode(URLDecoder.decode(s,"iso-8859-1"),"iso-8859-1");
		}catch(Exception ex){ log(ex.toString());}
		
		tx="<?xml version='1.0' encoding='ISO-8859-1'?>"+tx;
		log("prot:"+tx);
		dsp.loadObjects(tx);
	
	}
	
	public static void main(String s[]){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	
}
