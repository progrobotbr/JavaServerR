package com.jsv.server.trz.base;

public interface IScreen {

	/*
	 *  Sequencia de eventos:
	 *    onCreate              : Este m�todo e acionado quando a tela � 
	 *    						  criada. Portanto este m�todo � chamado 
	 *    						  uma �nica vez
	 *    onProcessAfterOutput  : Este m�todo � acionado quando o usu�rio 
	 *    						  clica ou aciona algum evento na tela
	 *    onProcessBeforeOutput : Este m�todo � acionado quando ocorre o 
	 *    						  response para o browser do usu�rio
	 */
	
	public void onCreate();
	
	public void onProcessBeforeOutput();
	
	public void onProcessAfterInput();
}
