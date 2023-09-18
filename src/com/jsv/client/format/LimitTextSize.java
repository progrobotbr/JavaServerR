package com.jsv.client.format;

public class LimitTextSize extends javax.swing.text.PlainDocument {
	private int maxLen = -1;
	public LimitTextSize() {}
	public LimitTextSize(int maxLen) { this.maxLen = maxLen; }
	public void insertString(int param, String str, javax.swing.text.AttributeSet attributeSet) 
		                      throws javax.swing.text.BadLocationException {
		int i,t;
		String str2;
		
		if(str==null) return;
		
		i=this.getLength();
		
		if(maxLen <=i) return;
		
		t=maxLen-i;
		i = str.length();
		if(i<t) t=i;
		str2 = str.substring(0,t);
		/*
		if (str != null && maxLen > 0 && this.getLength() + str.length() > maxLen) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			str2 = str.substring(0,maxLen-1);
			super.
		}else{
			str2 = str;
		}
		super.getLength()
		*/
		super.insertString(param, str2, attributeSet);
	}
}
