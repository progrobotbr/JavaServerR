package com.jsv.client.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jsv.utils.Utils;
import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.ClientProtocol;
import com.jsv.dictionary.ComponentsDictionary;

public class Desktop implements ActionListener {

	private JPanel mDs = null;
	private JPanel mTd = null;
	private JPanel mMd = null;
	private JPanel mBd = null;
	private HashMap<String, JPanel> mDesktop;
	private ClientProtocol mProtocol;
	private ClientBrowserTransaction mCBT;

	JLabel lbMsg = null;
	JTextField ipCmd = null;

	public Desktop() {
	}

	public void create(HashMap<String, JPanel> pDesktop,ClientProtocol pProtocol,ClientBrowserTransaction pCBT) {

		FlowLayout ofl = new FlowLayout();
		JPanel ds = null;
		JPanel td = null;
		JPanel md = null;
		JPanel bd = null;

		td = new JPanel();
		md = new JPanel();
		bd = new JPanel();
		//td.setBackground(new Color(235, 101, 141));
		td.setBackground(new Color(0, 0, 0));
		bd.setBackground(new Color(255, 255, 255));
		
		//Criação de módulos
		mCBT = pCBT;
		
		//md.setBackground(new Color(254, 199, 203));
		if (pDesktop != null && pDesktop.containsKey(GuiConstants.DESKTO)) {
			ds = pDesktop.get(GuiConstants.DESKTO);
			JPanel ltd = pDesktop.get(GuiConstants.DESTIT);
			mTd = ltd;
			JPanel lmd = pDesktop.get(GuiConstants.DESMID);
			mMd = lmd;
			JPanel lbd = pDesktop.get(GuiConstants.DESBOT);
			mBd = lbd;
			ds.remove(ltd);
			ds.remove(lmd);
			ds.remove(lbd);
			ds.removeAll();
		} else {
			ds = new JPanel();
			ds.setLayout(new BorderLayout());
			pDesktop.put(GuiConstants.DESKTO, ds);

		}

		ofl.setAlignment(FlowLayout.LEFT);
		ofl.setHgap(1); // Esquerda
        
		JButton btCmd = new JButton("Comando");
		btCmd.setPreferredSize(new Dimension(80, 15));
		JButton btEdi = new JButton("Editor");
		btEdi.setPreferredSize(new Dimension(80, 15));

		DesktopProcessKey dpk = new DesktopProcessKey(this);
		JTextField ipCmd = new JTextField();
		ipCmd.setPreferredSize(new Dimension(50, 15));
		ipCmd.addKeyListener(dpk);	
		
		//btCmd.setActionCommand("COMM");
		btCmd.setActionCommand("COMM");
		btCmd.setName("COMM");
		btCmd.addActionListener(this);
			
		//btEdi.setActionCommand("EDIT");
		btEdi.setActionCommand("EDIT");
		btEdi.setName("EDIT");
		btEdi.addActionListener(this);
		ipCmd.setName("IPCM");

		td.setLayout(ofl);
		md.setLayout(null);
		bd.setLayout(ofl);

		ds.add(td, BorderLayout.NORTH);
		ds.add(md, BorderLayout.CENTER);
		ds.add(bd, BorderLayout.SOUTH);

		td.add(ipCmd);
		td.add(btCmd);
		td.add(btEdi);
		lbMsg = new JLabel();
		lbMsg.setName(GuiConstants.MSG);
		lbMsg.setPreferredSize(new Dimension(1000,15));
		//lbMsg.setText(".");
		bd.add(lbMsg);

		pDesktop.remove(GuiConstants.DESTIT);
		pDesktop.remove(GuiConstants.DESMID);
		pDesktop.remove(GuiConstants.DESBOT);

		pDesktop.put(GuiConstants.DESTIT, td);
		pDesktop.put(GuiConstants.DESMID, md);
		pDesktop.put(GuiConstants.DESBOT, bd);

		ds.repaint();
		ds.revalidate();

		mDesktop = pDesktop;
		mProtocol = pProtocol;

	}

	public void actionPerformed(ActionEvent ae) {
		String sOkCode = ae.getActionCommand();

		if (sOkCode == null) {
			return;
		}

		if (sOkCode.equals("EDIT")) {
			this.Edit();
		} else if (sOkCode.equals("COMM")) {
			this.Debug();
		}
	}

	public void Edit() {
		DesktopEditor de = new DesktopEditor();
		lbMsg.setText("");
		de.create(mDesktop, mProtocol);
	}

	public void Debug() {
		int i;
		i = mProtocol.Debug();
		if (i == 0) {
			lbMsg.setText("Debug ativado");
		} else {
			lbMsg.setText("Erro ao ativar debug");
		}
	}
	
	public ClientProtocol getProtocol(){
		return(mProtocol);
	}
	
	public ClientBrowserTransaction getCreateModulo(){
		return(mCBT);
	}
	
	private static void setErrMessage(JLabel lb, String s){
		lb.setText(s);
		lb.setForeground(Color.RED);
	}
	private static void setOkMessage(JLabel lb, String s){
		lb.setText(s);
	}
	private static void clearMessage(JLabel lb){
		lb.setText("");
		lb.setForeground(Color.WHITE);
	}
	
	public static void setMessage(HashMap hs, int ptype, String s){
		int i;
		Component[] cp;
		JLabel lb;
		JPanel jp;
		HashMap <String, JPanel> mds;
		if(hs.containsKey(ComponentsDictionary.DESKTOP)){
			mds = (HashMap<String, JPanel>) hs.get(ComponentsDictionary.DESKTOP);
			jp = mds.get(GuiConstants.DESBOT);
			cp = jp.getComponents();
			for(i=0;i<cp.length;i++){
				if(cp[i].getName().equals(GuiConstants.MSG)){
					lb=(JLabel)cp[i];
					switch(ptype){
					case IDesktop.MSGERR:
						Desktop.setErrMessage(lb,s);
						break;
					case IDesktop.MSGOK:
						Desktop.setOkMessage(lb,s);
						break;
					case IDesktop.MSGCLEAR:
						Desktop.clearMessage(lb);
						break;
					}
				}
			}
			
		}
		
	}
	
	
}
