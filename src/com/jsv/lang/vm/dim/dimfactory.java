package com.jsv.lang.vm.dim;

import com.jsv.lang.vm.frame;
import com.jsv.lang.vm.table;
import com.jsv.lang.vm.type.decltype;
import com.jsv.data.Variant;

public class dimfactory {

	private frame frglobal, frlocal;

	public dimfactory(frame pglobal, frame plocal) {
		frglobal = pglobal;
		frlocal = plocal;
	}
	
	public dimfactory(){
		frglobal = frlocal = null;
	}

	public void setFrames(frame pglobal, frame plocal){
		frglobal = pglobal;
		frlocal = plocal;
	}
	
	public void createVar(String pName, String pNameType) {
		String sName[];
		Variant v=null;
		table tb;
		decltype typ;
		if (pName.endsWith("[]")) { //Tabela
			if(frlocal.hasType(pNameType)){
				typ = frlocal.getTyp(pNameType);
			}else{
				typ = frglobal.getTyp(pNameType);
			}
            v = typ.generateVar();
            tb = new table(v.getStucture());
            pName = pName.substring(0,pName.length()-2);
            tb.Name = pName;
            v = new Variant(tb);
		} else if(pNameType.equals("I")){ //I
			v = new Variant((int)0);
		} else if(pNameType.equals("F")){ //F
			v = new Variant((float)0);
		} else if(pNameType.startsWith("B")){ //B
			v = new Variant(false);
		} else if(pNameType.startsWith("C[")){ //C[]
			v = new Variant("");
			int i = pNameType.length();
			String s = pNameType.substring(2,i-1);
			i = Integer.parseInt(s);
			v.setStringSize(i);
		} else { //Estrutura
			if(frlocal.hasType(pNameType)){
				typ = frlocal.getTyp(pNameType);
			}else{
				typ = frglobal.getTyp(pNameType);
			}
            v = typ.generateVar();
		}
		
		frlocal.storeVar(pName, v);
	}

}
