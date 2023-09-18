package com.jsr.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import com.jsr.util.streamop;

public class jvmtrz {
	
	private static streamop zoio;
	
	public static void main(String args[]){
		
		System.out.println("inicio");
        		
		try{
			int i;
			byte[] buf = new byte[1000];
			jvmtrz.zoio = new streamop(icommands.IORIGCLT);
			
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			jvmtrz.zoio.putStream(null, System.in, System.out);
			
            String result = "";
            String line;
            File file = new File("c:\\renato\\logjvm.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            i=0;
            line="";
            while(true){
            	output.write("fase1");output.flush();
            	do{
            		line = jvmtrz.zoio.receiveSincronous();
            		output.write("fase2: "+line);output.flush();
            		System.out.print("eba" + i++);
            		if(line.equals("oi")){
                    	 System.out.print("recebi oi" + i++);
                    }
            		if(line.startsWith("shutdown")){
                    	break;
                    }
                }while(line!=null);
            	if(line.startsWith("shutdown")){
                	break;
                }
                 
            }
            
            output.close();
            System.out.print("saiu de jvmtrz" + i++);
		
		}catch(Exception ex){
		
		}
		
	}
}

