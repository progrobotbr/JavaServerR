package com.jsv.zteste;

import com.jsv.server.trz.USRL;
import com.jsv.server.trz.base.ITransaction;

public class TesteTrz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			Class cl = Class.forName("server.trz.USRL");
			Object ob = cl.newInstance();
			ITransaction tx = (ITransaction) ob;
			
			tx.onCreate();
			tx.onProcessAfterInput();
			tx.onProcessBeforeOutput();
			
		}catch(Exception ex){ 
			System.out.println(ex.toString());
		} 
		
		System.exit(0);

	}

}
