package com.jsv.server.trz.base;

public interface ITransactionJava{
	
	public void createVarScreen(ATransactionBaseJava ctx);
	public void initTransaction(ATransactionBaseJava ctx);
    public void endTransaction(ATransactionBaseJava ctx);
    
}
