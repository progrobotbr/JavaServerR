package com.jsv;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.jsv.client.ClientCommand;
import com.jsv.utils.Utils;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Client oClient = new Client();
		oClient.exec2();
		System.exit(0);
		
	}
		
    public void exec(){
		try {
			int i;
			byte b[] = new byte[5];
			StringBuffer oSb = new StringBuffer();
			Socket oSocket = new Socket("localhost", 5555);
			InputStream oIs = oSocket.getInputStream();
			do {
				this.clearBuffer(b,5);
				i = oIs.read(b);
				oSb.append(new String(b));
			} while (i > 0);
			
			Utils.log(oSb.toString());
		} catch (Exception ex) {
			Utils.log("Client " + Utils.writeStackTrace(ex));
		}

	}
    
    @SuppressWarnings("deprecation")
	public void exec1(){
		try {
			Socket oSocket = new Socket("localhost", 5555);
			
			InputStream oIs = oSocket.getInputStream();
			OutputStream oOs = oSocket.getOutputStream();
			
			BufferedInputStream oBis = new BufferedInputStream(oIs);
			BufferedOutputStream oBos = new BufferedOutputStream(oOs);
			
			DataInputStream oDis = new DataInputStream(oBis);
			DataOutputStream oDos = new DataOutputStream(oBos);
			
			//while(oDis.available() != 0){
				//System.out.println(oDis.readLine());
			//}
			
			//for(int t=0;t<50;t++) {
				//String tt = new String("Hello from client" + t + "\n");
			String tt = ClientCommand.Hello();
			oOs.write(tt.getBytes());
			oOs.flush();
			//}
			byte[] y = new byte[1024];
			
			int i = oDis.read(y);
			String sLinha = new String(y);
			
			Utils.log(sLinha);
						
			for(int z=0;z<2;z++){
				
				oOs.write(ClientCommand.Hello().getBytes(),0,ClientCommand.Hello().getBytes().length);
				oOs.flush();
				i = oDis.read(y);
				sLinha = new String(y);
				System.out.println(sLinha);
			}
			
			oOs.write(ClientCommand.CallTransaction("USRL").getBytes());
			oOs.flush();
			i = oDis.read(y);
			sLinha = new String(y);
			Utils.log(sLinha);
			
			String s = "<?xml version='1.0' encoding='ISO-8859-1'?><md id='USRL'><gt cm='gotoscr' vl='Logon' rc='0'/></md>";
			oOs.write(s.getBytes());
			oOs.flush();
			
			i = oDis.read(y);
			sLinha = new String(y);
			System.out.println(sLinha);
			
			s = "<?xml version='1.0' encoding='ISO-8859-1'?><md id='USRL'><gt cm='gotoscr' vl='Logon' rc='0'/></md>";
			oOs.write(s.getBytes());
			oOs.flush();
			
			i = oDis.read(y);
			sLinha = new String(y);
			Utils.log(sLinha);
			
			s = "<?xml version='1.0' encoding='ISO-8859-1'?><md id='USRL'><gt cm='gotoscr' vl='Logon' rc='0'/></md>";
			oOs.write(s.getBytes());
			oOs.flush();
			
			oIs.close();
			oOs.close();
			oSocket.close();
				
		} catch (Exception ex) {
		}

	}
    
    InputStream oIs;
    OutputStream oOs;
    BufferedInputStream oBis;
    BufferedOutputStream oBos;
    DataInputStream oDis;
    DataOutputStream oDos;
    
    public void exec2(){
		try {
			Socket oSocket = new Socket("localhost", 5555);
			
			oIs = oSocket.getInputStream();
			oOs = oSocket.getOutputStream();
			
			oBis = new BufferedInputStream(oIs);
			oBos = new BufferedOutputStream(oOs);
			
			oDis = new DataInputStream(oBis);
			oDos = new DataOutputStream(oBos);
			
			String sLine, s;
			
			this.put(ClientCommand.Hello());
			this.get();
			
			do{
				sLine = this.getCommand();
				if(sLine.equals("debug")){
					this.put(ClientCommand.Debug());
					this.get();
				}else if(sLine.equals("goto")){
					this.put(ClientCommand.Goto("USRL"));
					this.get();
				}else if(sLine.equals("call")){
					this.put(ClientCommand.CallTransaction("USRL"));
					this.get();
				}else if(sLine.equals("logoff")){
					this.put(ClientCommand.Logoff());
					this.get();
				}
				lg(sLine);
			}while(!sLine.equals("quit"));
			
			lg("BYE");
			
			oIs.close();
			oOs.close();
			oSocket.close();
				
		} catch (Exception ex) {
		}

	}
    
    public void exec3(){
    	int i,t;
    	InputStream okb=System.in;
    	byte bb[] = new byte[1024];
    	byte bb2[];
    	String sLine;
    	try{
    	do{
			i=okb.read(bb);
			t=i-2;
			bb2 = new byte[t];
			System.arraycopy(bb, 0, bb2, 0,t);
			sLine = new String(bb2);
			lg(sLine);
		}while(!sLine.equals("quit"));
    	}catch(Exception ex){lg(ex.toString());}
    	lg("BYE");
    }
	
    public String getCommand(){
    	int i,t;
    	InputStream okb=System.in;
    	byte bb[] = new byte[1024];
    	byte bb2[];
    	String sLine="";
    	try{
    		i=okb.read(bb);
			t=i-2;
			bb2 = new byte[t];
			System.arraycopy(bb, 0, bb2, 0,t);
			sLine = new String(bb2);
			lg(sLine);
    	}catch(Exception ex){lg(ex.toString());}
   	    return(sLine);
    }
    
    public void put(String s){
    	try{
    		oOs.write(s.getBytes());
    		oOs.flush();
    	}catch(Exception ex){System.out.println(ex.toString());}
    	lg(s);
    }
    
    public void get(){
    	byte[] y = new byte[1024];
    	byte[] bb;
		String sLinha="";
		try{
			int i = oDis.read(y);
			bb = new byte[i];
			System.arraycopy(y, 0, bb, 0,i);
			sLinha = new String(bb);
		}catch(Exception ex){lg(ex.toString());}
		lg(sLinha);
    }
    
    public void lg(String s){
    	System.out.println(s);
    }
    
	private void clearBuffer(byte b[],int i){
		for(int t=0;t<i;t++)
			b[t]='\0';
	}

}
