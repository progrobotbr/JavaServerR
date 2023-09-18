package com.jsv.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.jsv.utils.Utils;
import com.jsv.data.SData;

public class ServerResponse {
	
	private OutputStream mOs;
	
	public ServerResponse(OutputStream pOs){
		mOs = pOs;
	}
	
	public boolean send(SData pData){
		boolean bRet = false;
		try{
			mOs.write(pData.toString().getBytes());
			mOs.flush();
			bRet = true;
		}catch(Exception ex){Utils.log(ex.toString());}
		return(bRet);
	}
	
	public boolean sendS(String pData){
		boolean bRet=false;
		SData sd = new SData(pData);
		bRet = this.send(sd);
		return(bRet);
	}

}
