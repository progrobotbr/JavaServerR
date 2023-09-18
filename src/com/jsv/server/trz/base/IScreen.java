package com.jsv.server.trz.base;

public interface IScreen {

	/*
	 *  Sequencia de eventos:
	 *    onCreate              : Este método e acionado quando a tela é 
	 *    						  criada. Portanto este método é chamado 
	 *    						  uma única vez
	 *    onProcessAfterOutput  : Este método é acionado quando o usuário 
	 *    						  clica ou aciona algum evento na tela
	 *    onProcessBeforeOutput : Este método é acionado quando ocorre o 
	 *    						  response para o browser do usuário
	 */
	
	public void onCreate();
	
	public void onProcessBeforeOutput();
	
	public void onProcessAfterInput();
}
