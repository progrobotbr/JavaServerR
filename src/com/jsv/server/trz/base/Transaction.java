package com.jsv.server.trz.base;

public class Transaction extends ATransactionBase 
                         implements ITransaction{
	
	/***************************************************************
	 *  Os métodos abaixo podem ser implementados pelo usuário
	 *    de modo a realizar a programação básica da transação
	 ***************************************************************/
    public Transaction(String s){
    	this.setId(s);
    }
	public void onCreate(){  }
	
	public void onProcessBeforeOutput(){  }
	
	public void onProcessAfterInput(){  }
	
}
