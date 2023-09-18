package com.jsv.client.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.jsr.util._;
import com.jsv.utils.Utils;
import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.ClientHandleResponse;
import com.jsv.client.ClientProtocol;
import com.jsv.client.ClientSession;
//import client.ClientCommand;
//import client.ClientRequest;
import com.jsv.client.desktop.Desktop;
import com.jsv.client.desktop.DesktopEditor;
import com.jsv.client.desktop.GuiConstants;
import com.jsv.client.desktop.IDesktop;
import com.jsv.dictionary.ComponentsDictionary;

public class ClientMain {

	public  ClientBrowserTransaction  Transaction;
	public  ClientSession             Session;      //Estabele a conexão
	public  static String             sCommand;
	
	public JFrame                    mGui;         //A janela atual ativa
		
	public static void main(String[] args) {
		if(args!=null && args.length>0){
			sCommand=args[0];
		}else{
			sCommand="";
		}
		
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
	        public void run() {
                createAndShowGUI(sCommand);
            }
        });
	}

	//Exibe a Gui
	private static void createAndShowGUI(String s) {
		Utils.log(s);
		ClientMain oGui = new ClientMain();
		oGui.Start(s);
	}

    /************************************
    ************************************
    *  Processamento inicial
    ************************************
    ************************************/
	public void Start(String sTransaction){
		boolean b;
		int rc;
		ClientBrowserTransaction transaction;
		this.Init();
		
		//Conecta no servidor
		b=Session.connectToServer();
		if(!b){
		  Utils.log("Não conectou");
		  System.exit(1);
		}
		
		rc=Session.getRC();
		if(rc==ClientProtocol.ERR ){
			Utils.log("Erro em handshake de saudação");
			System.exit(1);
		}

		if(Utils.hasStrData(sTransaction)){
			sTransaction = sTransaction.toUpperCase();
		}else{
			sTransaction = "TL01";
		}
		
		//Chama transação
		transaction = ClientBrowserTransaction.factoryNewTransaction(this, sTransaction);
		rc=transaction.rc;
		if(rc!=0){
			Utils.log("Erro ao montar estrutura do modulo");
			System.exit(1);
		}

		this.Show();
		
	}
	/*************************
	 *************************
     *  Fim Bloco Inicial 
     *************************
     *************************/
	
	//Inicializa objetos básicos e conecta com o servidor
	public void Init(){
		
		mGui      = new JFrame("RenServer Cliente");
		mGui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Session  = new ClientSession();
		
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception ex){
			Utils.log("Client: " + Utils.writeStackTrace(ex));
		}
		
		this.menu();
		
	}
	
	public void setTransaction(ClientBrowserTransaction ptransaction){
		this.Transaction = ptransaction;
		JPanel desktop=this.Transaction.Desktop.getDesktop();
		JPanel rootPanel = (JPanel) mGui.getContentPane();
		if(rootPanel.getComponentCount()>0){
			rootPanel.remove(0);
			
		}
		//desktop.revalidate();
		desktop.repaint();
		mGui.add(desktop);
		//mGui.pack();
		//mGui.revalidate();
		mGui.setVisible(true);
		//mGui.repaint();
	}
	
	public void myRepaint(){
		mGui.revalidate();
		mGui.repaint();
	}
	
	private void menu(){
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		
		menuBar = new JMenuBar();
		menu = new JMenu("Menu 001");
		menuBar.add(menu);
		menuItem = new JMenuItem("Texto do sub-menu1",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu2",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu3",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu4",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu5",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu6",KeyEvent.VK_T);menu.add(menuItem);
		menu = new JMenu("Menu 002");
		menuBar.add(menu);
		menuItem = new JMenuItem("Texto do sub-menu1",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu2",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu3",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu4",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu5",KeyEvent.VK_T);menu.add(menuItem);
		menuItem = new JMenuItem("Texto do sub-menu6",KeyEvent.VK_T);menu.add(menuItem);
		mGui.setJMenuBar(menuBar);
	}
	
	//Conexão com o servidor
	/*
	public boolean ConnectToServer(){
		boolean b;
		b=mSession.connect("localhost",5555);
		return(b);
	}
	*/
	
	//Retorna memoria do modulo
	/*
	public HashMap getMemModulo(){
		return(mModulo);
	}
	*/
	
	//Carrega a primeira a tela 
	/*
	private void loadResponse(){
		String sGotoScreen;
		sGotoScreen = ClientHandleResponse.loadResponse(Transaction);
		this.setScreen(sGotoScreen);
	}
	*/
	//Cria estrutura do modulo
	/*
	private boolean createModulo(){
		boolean bRet=false;
		bRet = Transaction.execute();
		if(bRet == true){
			mModulo = Transaction.getModulo();
		}
		return(bRet);
	}
	*/	
	
	/*
	//Vai para a tela indica na transação
	public void setScreen(String pScreen){
		int iTrzTp;
		Integer oIt;
		HashMap hScreen;
		JPanel oPn;
		
		oIt = (Integer) mModulo.get(ComponentsDictionary.TRZTP);
		iTrzTp = oIt.intValue();
		
		if(iTrzTp==ComponentsDictionary.TRZSERVER){
			
			hScreen = (HashMap) mModulo.get(pScreen);
			mModulo.put(ComponentsDictionary.SCNDEF, pScreen);
			oPn = (JPanel) hScreen.get(ComponentsDictionary.SCR_ + pScreen);
			this.putScreenPanel(oPn);
			
		}else{
			
			
		}
		
	}
	*/
	
	/*
	//Seta memoria em relação à Gui
	public void setGui(JFrame jf){
		mModulo.put(ComponentsDictionary.GUI,jf);
		mModulo.put(ComponentsDictionary.DESKTOP,mDesktop);
	}
	*/
	
	//Mostra a janela
	public void Show(){
		//this.assignDesktop();
		mGui.pack();
		mGui.setSize(600,500);
		mGui.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//Redimensionamento do Gui
		mGui.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				Transaction.Desktop.resize(mGui);
			}
		});
		mGui.setVisible(true);
		
	}
	
	/*
	//Montra estrutura de Desktop
	public void createDesktop(){
		Desktop de = new Desktop();
		de.create(mDesktop,mProtocol,mCBT);
	}
	*/
	
	/*
	//Coloca a tela dentro do desktop (MID ou no meio)
	public void putScreenPanel(JPanel op){
		JPanel ds = mDesktop.get(GuiConstants.DESKTO);
		JPanel md = mDesktop.get(GuiConstants.DESMID);
		ds.remove(md);
		ds.add(op,BorderLayout.CENTER);
		ds.repaint();
		ds.revalidate();
		mDesktop.remove(md);
		mDesktop.put(GuiConstants.DESMID,op);
	}
	
	*/
	
	/*
	//Associa o desktop à janela
	public void assignDesktop(){
		JPanel op = mDesktop.get(GuiConstants.DESKTO);
		op.revalidate();
		op.repaint();
		mGui.add(op);
	}
	*/
}

