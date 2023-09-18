package com.jsv.client.desktop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class DebugProcessKey implements KeyListener {
	public DesktopDebug dbg;
	
	DebugProcessKey(DesktopDebug d){
		dbg=d;
	}
	public void keyTyped(KeyEvent e){
		
	}
	public void keyPressed(KeyEvent e){
		
	}
	public void keyReleased(KeyEvent e){
		int i;
		String s,n;
		JTextField tf;
		i=e.getKeyCode();
		switch(i){
		case 10 : 
			tf=(JTextField) e.getSource();
			s=tf.getText();
			n=tf.getName();
			dbg.GetVar(s,n);
			break;
		case 119:
			dbg.Pall();
			break;
		case 116:
			dbg.Step();
		}
		
	}
}
