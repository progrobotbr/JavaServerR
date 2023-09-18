package com.jsv.trz.local;

import java.util.HashMap;

import javax.swing.JPanel;

import com.jsv.client.ClientBrowserTransaction;
import com.jsv.client.ClientProtocol;
import com.jsv.client.main.ClientMain;

public interface trzlocal {

	public void create(ClientBrowserTransaction transaction);
	
	public void onLoad();
	
	public void onUnLoad();
	
    public void execAction(String s);
}
