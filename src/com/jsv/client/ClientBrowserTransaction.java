package com.jsv.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarInputStream;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.plaf.metal.MetalScrollButton;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jsr.util._;
import com.jsv.client.desktop.Desktop;
import com.jsv.client.desktop.Desktop2;
import com.jsv.client.desktop.DesktopDebug;
import com.jsv.client.desktop.DesktopDump;
import com.jsv.client.desktop.IDesktop;
import com.jsv.client.format.LimitTextSize;
import com.jsv.client.main.ClientMain;
import com.jsv.client.validate.ValidRegex;
import com.jsv.trz.local.trzlocal;
import com.jsv.utils.Utils;
import com.jsv.dictionary.ComponentsDictionary;
import com.jsv.gui.components.rgeral;
import com.jsv.gui.components.rtable;
import com.jsv.gui.components.rtableCellEditorJTextField;
import com.jsv.gui.components.rtableCellRenderer;
import com.jsv.gui.components.rtabstrip;

public class ClientBrowserTransaction {

	public  int rc;
	public  IDesktop      Desktop;
	public  ClientSession Session;
	//public  HashMap       Modulo;
	public  ClientMain    Window;
	public  String        TransactionName="";
	public  int           TransactionType=0;
	public  String        ScreenName="";
	public  String        ErrorDump="";
	private HashMap       memScreen;
		
	// Constants
	private static final int cLINHAALT = 22;
	private static final int cCOMPESP = 1;
	private static final int cCOMPTAMDEFAULT = 50;
	private static final int cCOMPALTDEFAULT = 20;
	private static final int cTELATOPX = 10;
	private static final int cTELATOPY = 10;
	
	//Variáveis Globais
	private int mLinha=0;
	private int mTelaX=0;
	private int mTelaY=0;
	
	//Tratamento de Eventos
	private ClientHandleRequest mClientHandleRequest;

	//Principal - gerador da trasação
	public static ClientBrowserTransaction factoryNewTransaction(ClientMain pwindow, String ptransaction){
		boolean bRet;
		ClientBrowserTransaction transaction = new ClientBrowserTransaction(pwindow, ptransaction);
		bRet = transaction.createMain();
		if(bRet != true){
			transaction.rc = 5;
		}
		return(transaction);
	}
	
	public ClientBrowserTransaction(ClientMain window, String ptransaction){
		this.Window = window;
		this.Session = window.Session;
		this.TransactionName = ptransaction;
		this.memScreen = new HashMap();
		mClientHandleRequest = new ClientHandleRequest(this);
	}
	
 	/** Funções Básicas **/
	public boolean createMain(){
		boolean bRet = false;
		Document doc = null;
		rc=4;
		try{
			
			rc=Window.Session.sayNewTransaction(this.TransactionName);
			if(rc!=0){
				
			}
			doc = this.Session.Response.parseModuloStru2();
			bRet = this.createTransaction(doc);
			if(bRet==true){
				if(this.TransactionType == ComponentsDictionary.TRZSERVER){
					rc=Window.Session.sayGoto();
					if(rc==0){
						bRet=mClientHandleRequest.loadResponse();
						if(bRet==true){
							rc=0;
						}else{
							rc=3;
						}
					}else{
						rc=2;
					}
				}else{
					rc=0;
				}
			}else{
				rc=1;
			}
		}catch(Exception ex){ 
			rc=3;
			Utils.log("Client: ClientBrowserTransaction "+Utils.writeStackTrace(ex));
			this.ErrorDump = this.Session.getResponse().getData() + "\n" +
			                "Client: ClientBrowserTransaction "+Utils.writeStackTrace(ex);
					         
		}
		return(bRet);
	}
	
	private boolean createTransaction(Document doc) {

		int i, t, iTrzTp;
		String sScreenId;
		String sTrzId;
		String sTrzTp;
		Integer oITrzTp;
		HashMap hScreen;
		Node oNo1, oNo2, oNo3;
		NodeList oNl1, oNl2;
		Element oElem;

		rc = 0;
		
		oElem = doc.getDocumentElement();
		oNl1 = oElem.getElementsByTagName(ComponentsDictionary.SS);

		sTrzId = this.getXMLRootValue(doc,ComponentsDictionary.MD, ComponentsDictionary.ID);
		sTrzTp = this.getXMLRootValue(doc,ComponentsDictionary.MD, ComponentsDictionary.TRZTP);
		iTrzTp = Integer.parseInt(sTrzTp);
		oITrzTp = new Integer(iTrzTp);
		this.TransactionName = sTrzId;
		this.TransactionType = oITrzTp;
		
		if(iTrzTp == ComponentsDictionary.TRZSERVER){
			
			Desktop = new Desktop2(this);
			//this.Desktop = Desktop;
			
			for (i = 0; i < oNl1.getLength(); i++) {
			
				mLinha = 0;
				mTelaX = cTELATOPX;
				mTelaY = cTELATOPY;
				
				hScreen = new HashMap();
				
				oNo1 = oNl1.item(i);
				JPanel oScr = this.clientCreateScreen(oNo1);
				oScr.setLayout(null);
				sScreenId = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
				oNl2 = oNo1.getChildNodes();
			
				/** Coloca elementos na tela **/
				for (t = 0; t < oNl2.getLength(); t++) {
					oNo3 = oNl2.item(t);
					this.clientCreateElement(hScreen, sScreenId, oScr, oNo3);
					//Utils.log(oNo3.getNodeName());
				}
				
				//TS
				/*
				JPanel jpp = new JPanel(); //new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);;
				
				JLabel ll = new JLabel("Teste do Renato");
				JPanel bb = new JPanel();
				//JScrollPane bb = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				JButton bt = new JButton();
				bt.setBounds(200, 200, 100, 20);
				JTextField jf = new JTextField();
				//jf.setPreferredSize(new Dimension(200, 20));
				jf.setBounds(50, 200, 100, 20);
				//bb.setAutoscrolls(true);
				
				//bb.setLayout(new ScrollPaneLayout());
				//bt.setPreferredSize(new Dimension(200, 20));
				//bt.setBounds(450, 200, 100, 20);
				bt.setText("Executar");
				bb.add(bt);
				bb.add(jf);
				bb.add(ll);
				
				bb.setLayout(null);
				JPanel cc = new JPanel();
				JTabbedPane jtp = new JTabbedPane();
				//jtp.setLayout(new GridLayout(0, 1));
				//jtp.setLayout(null);
				jtp.add("Dados Básicos", bb);
				jtp.add("Programa", new JPanel());
				jtp.add("Dados Básicos", new JPanel());
				jtp.add("Programa", new JPanel());
				jtp.add("Dados Básicos", new JPanel());
				jtp.add("Programa", cc);
				jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
				
				jtp.setBounds(50, 60, 300, 400);
				//jtp.setLayout(new GridLayout(0, 1));
				//jtp.setPreferredSize(new Dimension(1000, 1000));
				jpp.setBounds(50, 60, 300, 400);
				//bb.setBounds(10, 10, 290, 390);
				
				jpp.setLayout(new GridLayout(1,0));
				//jpp.setLayout(null);
				
				//jtp.g
				jpp.setAutoscrolls(true);
				jpp.add(jtp);
				oScr.add(jpp);
				*/
				/** Acerta ZOrder, onde BOX terá Box terá ZOrder mais alto **/
				this.setZOrder(oScr);
				
				/** oScr.setLayout(new GridLayout(mLinha,1,0,0)); **/
				hScreen.put(ComponentsDictionary.SCR_ + sScreenId,oScr);
				memScreen.put(sScreenId, hScreen);
				//this.Modulo.put(sScreenId, hScreen);
				//this.Modulo.put(ComponentsDictionary.SCNDEF, sScreenId);
			}
			
		}else{
			String sTrzNm;
			trzlocal oTrz;
			for (i = 0; i < oNl1.getLength(); i++) {
				oNo1 = oNl1.item(i);
				sScreenId = Utils.getAttribute(oNo1,ComponentsDictionary.ID);
				sTrzNm="com.jsv.trz.local.trz"+sTrzId;
				sTrzNm = sTrzNm.toLowerCase();
				try{
					Class oClass = ClassLoader.getSystemClassLoader().loadClass(sTrzNm);
					oTrz = (trzlocal) oClass.newInstance();
					oTrz.create( this );
					rc=0;
					return(true);
				}catch(Exception ex){
					Utils.log("ClientBrowserTransaction: " + Utils.writeStackTrace(ex));
					rc=1;
					return(false);
				}
				
			}
		}

		rc=0;
		this.setDesktopInWindow();
		return(true);

	}
	
	public void setScreen(){
		HashMap hScreen =  (HashMap) this.memScreen.get(this.ScreenName);
		JPanel scn = (JPanel) hScreen.get(ComponentsDictionary.SCR_ + this.ScreenName);
		this.Desktop.replaceBody(scn);
		this.Window.myRepaint();
	}
	
	public JPanel getScreenPanel(String pid){
		HashMap hs = (HashMap) this.memScreen.get(pid);
		JPanel scn = (JPanel) hs.get(ComponentsDictionary.SCR_ + pid);
		return(scn);
	}
	
	public HashMap getMemScreen(String pid){
		HashMap hs = (HashMap) this.memScreen.get(pid);
		return(hs);
	}
	
	public HashMap getMemGlobsalScreen(){
		return(this.memScreen);
	}
	
	public void setDesktopInWindow(){
		this.Window.setTransaction(this);
	}
		
	public String getXMLRootValue(Document d, String tag, String id){
		
		//String tag -> não é utilizado
		String s="";
		Element oElem;

		oElem = d.getDocumentElement();
		s = oElem.getAttribute(id);
		return(s);
	}
	
	private JPanel clientCreateScreen(Node pNode) {
		JPanel oPanel = new JPanel();
		oPanel.setBackground(new Color(203, 219, 234));
		return (oPanel);

	}
		
	private void clientCreateElement(HashMap pScreen, String pScreenId, JPanel pTela, Node pNode) {
		int i;
		String oNome;
		
		oNome = pNode.getNodeName();
		i = ComponentsDictionary.dictionaryElement(oNome);
		if (i == 0) { // Erro }
			return;
		}
		switch (i) {
		case ComponentsDictionary.LABEL:
			this.putLabel(pScreen, pScreenId, pTela, pNode);
			break;
		case ComponentsDictionary.INPUTFIELD:
			this.putInputField(pScreen, pScreenId, pTela, pNode);
			break;
		case ComponentsDictionary.BUTTON:
			this.putButton(pScreen, pScreenId, pTela, pNode);
			break;
		case 4:
			this.putSelectOptions(pScreen, pScreenId, pTela, pNode);
			break;
		case ComponentsDictionary.TABLE:
			rtable.ClientParseProtocolCreateTable(mClientHandleRequest,pScreen, pScreenId, pTela, pNode);
			break;
		case 6:
			mLinha++;
			mTelaX = cTELATOPX;
			mTelaY = cTELATOPY + ( mLinha * cLINHAALT);
		case ComponentsDictionary.BOX:
			putBox(pScreen, pScreenId, pTela, pNode);
			break;
		case ComponentsDictionary.TABSTRIP:
			rtabstrip.ClientParseProtocolCreateTabStrip(mClientHandleRequest, pScreen, pScreenId, pTela, pNode);
			break;
		}

	}

	/** ZOrder, observando BOX **/
	private void setZOrder(JPanel pTela){
		int w,h,y,x;
		Component cps[];
		cps=pTela.getComponents();
        w=h=cps.length;
        h--;
        y=0;
		for(x=0;x<w;x++){
        	if(cps[x] instanceof JPanel){
        		pTela.setComponentZOrder(cps[x], h--);
        	}else{
        		pTela.setComponentZOrder(cps[x], y++);
        	}
        }	
	}
	
	/** Cria Componentes **/
	
	private void putBox(HashMap pCollection, String pScreenId, JPanel pTela, Node pNode){
		int x,y, h, w;
		String sX, sY, sW, sH, sT;
		Dimension dd;
		sX = Utils.getAttribute(pNode,"x");
		sY = Utils.getAttribute(pNode,"y");
		sW = Utils.getAttribute(pNode,"wd");
		sH = Utils.getAttribute(pNode,"hd");
		x = Utils.ctoi(sX);
		y = Utils.ctoi(sY);
		h = Utils.ctoi(sH);
		w = Utils.ctoi(sW);
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		sT = Utils.getAttribute(pNode,"tx");
		JPanel oBox = new JPanel();
		//oBox.setBackground(new Color(185, 198, 198));
		oBox.setBackground(new Color(203, 219, 234));
		oBox.setBounds(x,y,w,h);
		//oBox.setBorder(BorderFactory.createTitledBorder(sT)); 
		oBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.MAGENTA), sT));
		pTela.add( oBox );
		
		rgeral.setTam(pTela, x+w, y+h);
		//pTela.setComponentZOrder(oBox, 6);
		
	}
	
	private void putLabel(HashMap pCollection, String pScreenId, JPanel pTela, Node pNode) {
		int x=0,y=0;
		String sX, sY, sW, sH;
		sX = Utils.getAttribute(pNode,"x");
		sY = Utils.getAttribute(pNode,"y");
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		JLabel oLbl = new JLabel();
        int i = Utils.ctoi(Utils.getAttribute(pNode,ComponentsDictionary.WD));
        if(i==0) i = cCOMPTAMDEFAULT;
        String sText = Utils.getAttribute(pNode,ComponentsDictionary.TX);
        //oLbl.setPreferredSize( new Dimension( i, 20 ) );
        if(sX!=null && sX.length()>0 && sY!=null && sY.length()>0){
        	x=Integer.parseInt(sX);
        	y=Integer.parseInt(sY);
        	oLbl.setBounds(x,y,i,cCOMPALTDEFAULT);
        }else{
        	oLbl.setBounds(mTelaX,mTelaY,i,cCOMPALTDEFAULT);
        }
        mTelaX = mTelaX + i + cCOMPESP;
        oLbl.setText(sText);
        //Font f= oLbl.getFont();
        //oLbl.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        pTela.add(oLbl);
        pCollection.put(ComponentsDictionary.LBL_ + sId, oLbl);
        
        rgeral.setTam(pTela, x+i, y+cCOMPALTDEFAULT);
        
	}

	private void putInputField(HashMap pCollection, String pScreenId, JPanel pTela, Node pNode) {
		int x=0,y=0;
		String sX, sY, sRo;
		LimitTextSize oSizeText;
		sX = Utils.getAttribute(pNode,"x");
		sY = Utils.getAttribute(pNode,"y");
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		JTextField oTf = new JTextField();
		int i = Utils.ctoi(Utils.getAttribute(pNode,ComponentsDictionary.WD));
		int w = Utils.ctoi(Utils.getAttribute(pNode,ComponentsDictionary.NC));
		sRo = Utils.getAttribute(pNode,ComponentsDictionary.RO);
		if(i==0) i = cCOMPTAMDEFAULT;
		if(sX!=null && sX.length()>0 && sY!=null && sY.length()>0){
        	x=Integer.parseInt(sX);
        	y=Integer.parseInt(sY);
        	oTf.setBounds(x,y,i,cCOMPALTDEFAULT);
		}else{
			oTf.setBounds(mTelaX,mTelaY,i,cCOMPALTDEFAULT);
		}
		oSizeText = new LimitTextSize(w);
		oTf.setDocument(oSizeText);
		//ReadOnly
		if(sRo!=null && sRo.trim().length()!=0) {
			oTf.setBackground(Color.YELLOW);
			oTf.setEditable(false);
		}
		mTelaX = mTelaX + i + cCOMPESP;
		//oTf.setPreferredSize( new Dimension( i, 20 ) ); 
		pTela.add(oTf);
		pCollection.put(ComponentsDictionary.INP_ + sId, oTf);
		
		rgeral.setTam(pTela, x+i, y+cCOMPALTDEFAULT);
				
	}

	private void putButton(HashMap pCollection, String pScreenId, JPanel pTela, Node pNode){
		int x=0,y=0;
		String sX, sY;
		JButton oBt = new JButton();
		int i = Utils.ctoi(Utils.getAttribute(pNode,ComponentsDictionary.WD));
		sX = Utils.getAttribute(pNode,"x");
		sY = Utils.getAttribute(pNode,"y");
		if(i==0) i = cCOMPTAMDEFAULT;
		String sTexto = Utils.getAttribute(pNode,ComponentsDictionary.TX);
		String sId = Utils.getAttribute(pNode,ComponentsDictionary.ID);
		if(sX!=null && sX.length()>0 && sY!=null && sY.length()>0){
        	x=Integer.parseInt(sX);
        	y=Integer.parseInt(sY);
        	oBt.setBounds(x,y,i,cCOMPALTDEFAULT);
		}else{
			oBt.setBounds(mTelaX,mTelaY,i,cCOMPALTDEFAULT);
		}
		oBt.setText(sTexto);
		oBt.setActionCommand(ComponentsDictionary.SCR_ + pScreenId + "_" + ComponentsDictionary.BUT_ + sId);
		//oBt.addMouseListener(mCBE);
		oBt.addActionListener(mClientHandleRequest);
		mTelaX = mTelaX + i + cCOMPESP;
		//oBt.setPreferredSize( new Dimension( i, 20 ) ); 
		pTela.add(oBt);
		pCollection.put(ComponentsDictionary.BUT_ + sId, oBt);
		
		rgeral.setTam(pTela, x+i, y+cCOMPALTDEFAULT);
	}

	private void putSelectOptions(HashMap pCollection, String pScreenId, JPanel pTela, Node pNode) {
		JComboBox oBox = new JComboBox();
	}
	
	/*
	public HashMap getModulo(){
		return(Modulo);
	}
	*/
		
}
