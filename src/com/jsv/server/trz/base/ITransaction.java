package com.jsv.server.trz.base;

public interface ITransaction {
	
	/*
	 *  Sequencia de eventos:
	 *    onCreate              : Este m�todo � acionado quando a transa��o
	 *    						  � criada. Portanto este m�todo � chamado
	 *    						  uma �nica vez
	 *    onProcessAfterOutput  : Este m�todo � acionado quando o usu�rio 
	 *    						  clica ou aciona algum evento na tela
	 *    onProcessBeforeOutput : Este m�todo � acionado quando ocorre o 
	 *    						  response para o browser do usu�rio
	 */
	
	public void onCreate();
	
	public void onProcessAfterInput();
	
	public void onProcessBeforeOutput();
	
}
