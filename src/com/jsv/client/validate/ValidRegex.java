package com.jsv.client.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jsv.utils.Utils;

public class ValidRegex {
	
	public static boolean validBase(String s, String pre){
		Pattern re;
		re = Pattern.compile(pre);
		Matcher mt = re.matcher(s);
		if(mt.find()){
			return(true);
		}else{
			return(false);
		}
	}
	public static boolean validInt(String s){
		if(Utils.hasStrData(s)){
			return(true); //ValidRegex.validBase(s.trim(), "^([0-9]{1,5})$"));
			//return(ValidRegex.validBase(s.trim(), "^([0-9]*)$"));
		}
		return(true);
	}
	public static boolean validDec(String s){
		if(Utils.hasStrData(s)){
			return(true); //ValidRegex.validBase(s.trim(), "^[0-9]+,[0-9]+$"));
		}
		return(true);
	}


}
