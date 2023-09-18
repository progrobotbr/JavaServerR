package com.jsv.client.desktop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import com.jsv.utils.Utils;
import com.jsv.client.ClientBrowserTransaction;

public class DesktopProcessKey implements KeyListener {
	
	private ClientBrowserTransaction Transaction;
	
	public DesktopProcessKey(ClientBrowserTransaction ptransaction){
		Transaction=ptransaction;
	}
	public void keyTyped(KeyEvent e){
		Utils.log("Client DesltopProcessKey: ddd keytyped");
	}
	public void keyPressed(KeyEvent e){
		Utils.log("Client DesltopProcessKey: ddd keypressed");
	}
	public void keyReleased(KeyEvent e){
		int i;
		String s,n;
		JTextField tf;
		ClientBrowserTransaction oldTransaction;
		
		i=e.getKeyCode();
		switch(i){
		case 10 : //Enter
			tf=(JTextField) e.getSource();
			s=tf.getText();
			s=s.toUpperCase();
			n=tf.getName();
			if(n.equals("IPCM")){
				if(!s.startsWith("/N")){
					return;
				}
				s=s.substring(2,s.length());
				Transaction = ClientBrowserTransaction.factoryNewTransaction(Transaction.Window, s);
				if(Transaction.rc!=0){
					Utils.log("Erro ao chamar transação: " + Transaction.ErrorDump);
					Transaction.ErrorDump+="\n\n"+"Erro ao chamar transação: "+s;
					DesktopDump dd = new DesktopDump();
					dd.create(Transaction);
					Transaction.Desktop=dd;
					Transaction.setDesktopInWindow();
					/*
					 * Fazer tratamento
					 */
				}
				
				/*
				oProtocol = dsk.getProtocol();
				rc=oProtocol.CallTransaction(s);
				if(rc!=0){
					Utils.log("Client DesktopProcessKey: Erro ao montar estrutura do modulo");
					DesktopDump dd = new DesktopDump();
					oCBT = dsk.getCreateModulo();
					oModulo = oCBT.getModulo();
					dd.create((HashMap) oModulo.get(ComponentsDictionary.DESKTOP), oProtocol);
					dd.clearLastScreen();
					dd.setBodyText(oProtocol.geResponse().getData());
					return;
					//System.exit(1);
				}
				oCBT = dsk.getCreateModulo();
				//oCMCM.init();
                oModulo = oCBT.getModulo();
                
                //Limpa dados da transação
                oModulo.remove(ComponentsDictionary.TRZID);
                oModulo.remove(ComponentsDictionary.TRZTP);
                sChaves = oModulo.keySet();
                oChaves = sChaves.toArray();
                iTam=oChaves.length;
                for(x=0;x<iTam;x++){
                	isNum=false;
                	chave = (String) oChaves[x];
                	try{
                		iNum=Integer.parseInt(chave);
                		isNum=true;
                	}catch(Exception ex){}
                	if(isNum){
                		oHs = (HashMap) oModulo.get(chave);
                		oPn = (JPanel) oHs.get(ComponentsDictionary.SCR_+chave);
                		oPn.removeAll();
                		oPn.repaint();
                		oModulo.remove(chave);
                	}	
                }
               
                HashMap oDesktop = (HashMap) oModulo.get(ComponentsDictionary.DESKTOP);
				bRet = oCBT.execute(oDesktop, oProtocol);
				if(bRet != true){
					Utils.log("Client: DesktopProcessKey: Erro ao montar estrutura do modulo");
					return;
				}
				rc=oProtocol.Goto();
				if(rc!=0){
					Utils.log("Client: DesktopProcessKey: Erro na chamada para a transação");
					return;
				}
				
				//sGotoScreen = ClientManageResponse.loadResponse(oProtocol.getSession(), oModulo);
				Integer iTpTrz = (Integer) oModulo.get(ComponentsDictionary.TRZTP);
				x = iTpTrz.intValue();
				if(x==ComponentsDictionary.TRZSERVER){
					ClientHandleRequest oCHR = new ClientHandleRequest(oProtocol.getSession(),oModulo);
					oCHR.loadResponse();
				}
				*/
				tf.setText("");
			}
			break;
		case 119: //F8
			break;
		case 116: //F5
		}
		
	}
}
