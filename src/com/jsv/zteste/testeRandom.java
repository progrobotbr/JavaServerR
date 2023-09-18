package com.jsv.zteste;

import java.io.*;
public class testeRandom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testeRandom tr = new testeRandom();
		tr.main();
	}
	
	public void main(){
		try{
			byte b[]=new byte[256];
			int i;
			String s="";
			RandomAccessFile rf=null;
			lg("Digite comandos:");
			do{
				i=System.in.read(b);
				s=new String(b);
				if(s.startsWith("create")){
					this.create();
				}else if(s.startsWith("open")){
					rf=this.open();
				}else if(s.startsWith("close")){
					this.close(rf);
				}else if(s.startsWith("change"))
					this.change(s,rf);
			}while(!s.startsWith("quit"));
			lg("Fim");
		}catch(Exception ex){ lg(ex.toString());}
	}

	public void lg(String s){
		System.out.println(s);
	}
	
	public void create(){
		try{
			byte b[]= { 50,50,50,50,50 };
			RandomAccessFile rf = new RandomAccessFile("C:\\ren.t", "rw");
			rf.write(b);
			rf.close();
			lg("ok");
		}catch(Exception ex){ lg(ex.toString()); }
	}
	
	public RandomAccessFile open(){
		
		RandomAccessFile rf=null;
		try{
			rf = new RandomAccessFile("C:\\ren.t", "rw");
			lg("ok");
		}catch(Exception ex){ lg(ex.toString()); }
		return(rf);
	}
	
	public void close(RandomAccessFile rf){
		try{
		rf.close();
		lg("ok");
		}catch(Exception ex){ lg(ex.toString());}
	}
	
	public void change(String s, RandomAccessFile f){
		
		try{
			byte b[]=null;
			byte b1[] = new byte[1];
			String v1[] = s.split(":");
			String c1 = v1[2];
			b = c1.getBytes();
			b1[0]=b[0];
			long l = Long.parseLong(v1[1]);
			f.seek(l);
			f.write(b1);
			lg("ok");
		}catch(Exception ex){ lg(ex.toString());}
		
	}
}
