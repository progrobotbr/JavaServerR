package com.jsv.server.trz.base;

public class Transaction extends ATransactionBase 
                         implements ITransaction{
	
	/***************************************************************
	 *  Os m�todos abaixo podem ser implementados pelo usu�rio
	 *    de modo a realizar a programa��o b�sica da transa��o
	 ***************************************************************/
    public Transaction(String s){
    	this.setId(s);
    }
	public void onCreate(){  }
	
	public void onProcessBeforeOutput(){  }
	
	public void onProcessAfterInput(){  }
	
}
