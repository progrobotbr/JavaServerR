package com.jsv.data;

public class SData {
	
	private StringBuilder msb;
	
	public SData(){
	  msb = new StringBuilder();	
	}
	
	public SData(String s){
		  msb = new StringBuilder();
		  msb.append(s);
	}
		
	public void init(){
		msb = new StringBuilder();
	}
	
	public void put(String s){
		msb = new StringBuilder();
		msb.append(s);
	}
	
	public void add(String s){
		msb.append(s);
	}
	
	public String toString(){
		return(msb.toString());
	}
	
	public int getSize(){
		return(msb.toString().length());
	}

}
