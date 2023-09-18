package com.jsv.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.jsv.utils.Utils;

public class ServerMain extends Thread {

	public static void main(String[] args) {

		ServerMain oServer = new ServerMain();
		oServer.start();
	}

	public void run() {

		ServerSocket mServerSocket = null;
		Socket oSocket = null;

		try {
			mServerSocket = new ServerSocket(5555);
		} catch (Exception ex) { 
			Utils.log("Server: Erro ao ativar servidor em porta 5555");
			Utils.log("Server: " + Utils.writeStackTrace(ex));
			return;
		}

		Utils.log("\nServidor em operacao...\n");
		
		do {
			try {
				oSocket = mServerSocket.accept();
			} catch (Exception ex) { 
				Utils.log("Server: Server.java: "+Utils.writeStackTrace(ex));
			}

			ServerClientNode oNode = new ServerClientNode(oSocket);
			
			oNode.start();

		} while (true);
		
		//mServerSocket.close();

	}

}
