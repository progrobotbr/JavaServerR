package com.jsv.server.trz;

import com.jsv.server.trz.base.*;

public class USRL extends Transaction{

	// Constantes
	private static final String cUSRL = "USRL";
	private static final String telaLOGON = "Logon";
	
	// Vari�veis Globais
	private static final String LC_USUARIO    = "lc_usuario", 
								LC_PASSWORD   = "lc_password",
								LI_NUMTRYS    = "li_numtrys";
	
	// Inicializa��o da classe
	public USRL(){
		super(cUSRL);
		// Indica o ID da transa��o
		this.setId(cUSRL);
		
		// Cria e associa as telas
		// 1 - Tela Logon
		scrLogon scnLogon = new scrLogon(); 
		scnLogon.Init(cUSRL,telaLOGON,this);
		this.putScreen(telaLOGON,scnLogon);
		// 2 - Tela ...
		
		// Indica a tela que ser� enviada para o usu�rio
		this.setScreen(telaLOGON);
		
	}
	
	/*
	 * Eventos livres para programa��o 
	 */
	public void onCreate(){
		storegVar(LC_USUARIO,"Otaner");
		storegVar(LC_PASSWORD,"Carcaju");
		storegVar(LI_NUMTRYS,10);
	}
	
	public void onProcessAfterInput(){
        int i = loadgVarI("li_numtrys");
		i -= 5;
		storegVar("li_numtrys",i);
	}
	
	public void onProcessBeforeOutput(){
		
		//Comando opcional. Foi colocado somente para mostrar 
		//  como se executa um comando call screen
		this.gotoScreen(telaLOGON);
		
		//L�gica de programa��o
		String s = loadgVarS(LC_USUARIO);
		log("Transacao:" + s);
		Screen o = getScreen(telaLOGON);
		
		o.onProcessBeforeOutput();
		
		s = loadgVarS(LC_USUARIO);
		log("Transacao:" + s);
		
		int i = loadgVarI("li_numtrys");
		i = -5;
		storegVar("li_numtrys",i);
				
		log(o.getRespScreenProtocol());
		
	}
	

}
