package com.jsr.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class _ {
	public static void lg(String s){
		System.out.println(s);
	}
	
	public static int saveFile(String pNome, String pDados){
		try{
			File file = new File(pNome);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(pDados);
			output.close();
			return(0);
		}catch(Exception ex){
			return(4);
		}
	}
	
}
