package com.jsv.server.trz.base;

import java.util.Calendar;
import java.util.Date;

public class dump {
	
	public static String writeDump(Exception ex){
		
		StackTraceElement ste[];
		Date date=new Date();
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		StringBuilder sb = new StringBuilder();
		              sb.append("Server: data "+cd.get(Calendar.DAY_OF_MONTH)+
		                "-"+cd.get(Calendar.MONTH)+
		                "-"+cd.get(Calendar.YEAR)+
		                " "+cd.get(Calendar.HOUR)+
		                ":"+cd.get(Calendar.MINUTE)+
		                ":"+cd.get(Calendar.SECOND)+
		                ":"+cd.get(Calendar.MILLISECOND)+
		                "\n");
		sb.append(ex.toString()+"\n");
		ste=ex.getStackTrace();
		for(int i=0;i<ste.length;i++){
			sb.append(ste[i].toString()+"\n");
		}
		
		return(sb.toString());	
		
	}

}
