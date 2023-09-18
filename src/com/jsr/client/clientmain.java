package com.jsr.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.jsr.server.icommands;
import com.jsr.util._;
import com.jsr.util.streamop;

public class clientmain {

	public String sessionId="";
	public streamop osktin = new streamop(icommands.IORIGCLT);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		clientmain ocm = new clientmain();
		ocm.execute();
	}
	
	public void execute(){
		try{
			String line = "";
			String cmd = "";
			Socket os = new Socket("localhost",icommands.ISERVERPORT);
			//PrintWriter out =  new PrintWriter(os.getOutputStream());
			//BufferedReader in = new BufferedReader(new InputStreamReader(os.getInputStream()));
			osktin.putStream(null, os.getInputStream(),os.getOutputStream());
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			_.lg("cliente: inicio handshaking");
			line = osktin.receiveSincronous();
			sessionId = line.substring(line.indexOf("id")).replace("id ", "");
			_.lg(line);
			osktin.send("cliente id "+sessionId);
			do{ //!(line = in.readLine()).equals("end")){
				line = osktin.receiveSincronous();
				lg(line);
				if(line==null){
					break;
				}
				if(line.equals("end")){
					break;
				}
				cmd = stdIn.readLine();
				lg("aa"+cmd);
				if(cmd.length()>0){
					osktin.send(cmd);
				}
			}while(true);
				
			os.close();
			
		}catch(Exception ex){
			lg(ex.toString());
		}
		
	}
	
	public void lg(String s){
		System.out.println(s);
	}

}
