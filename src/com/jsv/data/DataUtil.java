package com.jsv.data;

public class DataUtil {

	public static String stringNorm(String s){
		int i,t;
		byte b[], b1[];
		String s1="";
		if(s==null) return(s1);
		i=s.length();
		if(i<2) return(s);
		t=i-2;
		b1=new byte[t];
		b=s.getBytes();
		System.arraycopy(b, 1, b1, 0, t);
		s1=new String(b1);
		return(s1);
	}
}
