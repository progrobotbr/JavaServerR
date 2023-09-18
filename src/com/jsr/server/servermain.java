package com.jsr.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.jsr.client.serverclient;
import com.jsr.util._;

public class servermain {
	
	public static void main(String argv[]){
		
		servermain os = new servermain();
		os.execute();
		
	}
	
	public void execute(){
		
		ServerSocket ss;
		Socket       cs;
		serverclient sc;

		try{
			ss = new ServerSocket(icommands.ISERVERPORT);
			
			_.lg("Servidor iniciado");
			
			while(true){
				
				cs = ss.accept();
				sc = new serverclient();
				if(sc.init(cs)==icommands.IERROR){
				  _.lg("erro ao iniciar conexao cliente");
				}else{
					sc.start();
				}
				
			}
			
		}catch(Exception ex){
			_.lg(ex.toString());
		}
		
		
		
	}

}
