package com.jsv.editor;

import java.util.Vector;

import com.jsv.lang.program.program;

public class Teste4 {
	
	public static void main(String argv[]){
		boolean bf = false; // Retira branco do inicio da frase. Desidenta, mas compacta o código. Retira tbém espaços desnecessários
		boolean bp = false; // Não deixa branco depois do ponto no início da frase
		boolean bLine = false;
		int z=0,t=1,iLine=1;
		String s[] = { "banana maca.fd.\n",
				       "\n",
				       "casa casa. janela\n janela. home.\n" };
		
		StringBuilder sb = new StringBuilder();
		program pg = new program();
//		Prg001 op = new Prg001();
		
		s = Teste4.getPrg();
		
		pg.init(s);
		
		for(int i=0;i<s.length; i++){
			sb.append(s[i]);
		}
		
		Vector <String>ed = new Vector<String>();
		String sr;
		byte b[] = sb.toString().getBytes();
		byte b1[] = new byte[1024]; 
		byte b2[];
		
		for(int i=0;i<b.length; i++){
			if(b[i]=='.'){
				b1[z++] = '.';
				b1[z++] = '\n';
				b2 = new byte[z];
				System.arraycopy(b1, 0, b2, 0, z);
				sr = new String(b2);
				ed.add(sr.toUpperCase());
				z=0;
				bp = true;
				bLine=true;
				System.out.println(iLine);
				pg.putLine(sr.toUpperCase(), iLine);
			}else if(b[i] == '\n'){
					if(bp==false) { b1[z++]=' '; }
					bf=true;t++;
				  }else if(b[i]==' '){
					       if(bf!=true){ b1[z++] = b[i]; bf=true; }
				        }else{                      				  
				        	b1[z++]=b[i];
				        	bf=false;
				        	bp=false;
				        	if(bLine==true) { 
				        		iLine = t; bLine=false;
				        	}
				    	}
		}
		if(z>0){
			b2 = new byte[z];
			System.arraycopy(b1, 0, b2, 0, z);
			sr = new String(b2);
			ed.add(sr.toUpperCase());
			System.out.println(iLine);
			pg.putLine(sr.toUpperCase(), iLine);
		}
		
		String ss[] = new String[ed.size()];
		
		for(int i=0;i<ed.size();i++){
			sr = ed.get(i);
			ss[i] = sr;
			System.out.print(sr);
		}
		
		pg.organize();
		
		int g;
		/*
		for(int i=0;i<dd.length;i++){
			pg.putLine(dd[i].toUpperCase(), i);
		}
		*/	
		g = 1;
		
	}
	
	public static String[] getPrg(){
		String s[]=new String[33];
		s[0] = "dim sname as c[30].\n";
		s[1] = "dim sendereco as c[50].\n";
		s[2] = "dim num1 as i.\n";
		s[3] = "dim num2 as i.\n";
		s[4] = "dim num3 as i.\n";
		s[5] = "num1 = 10.\n";
		s[6] = "num2 = 10.\n";
		s[7] = "num3 = num1 + num2.\n";
		s[8] = " sub calc\n";
		s[9] = "                     in pnum1 as i\n";
		s[10] = "    pnum2 as i\n";
		s[11] = "  out pres as i.\n";
		s[12] = "  pres =          pnum1 * pnum2.\n";
		s[13] = "  dim baba as i.\n";
		s[14] = "             endsub.\n";
		s[15] = "type ds\n";
		s[16] = "  ds as i\n";
		s[17] = "  rerer as i\n";
		s[18] = "  nome as c[30]\n";
		s[19] = "endtype.\n";
		s[20] = "if a > v\n.";
		s[21] = " dim fd as i.\n";
		s[22] = "fd = 1.\n";
		s[23] = "if fd <3.\n";
		s[24] = "dd=3+2.\n";
		s[25] = "endif.\n";
		s[26] = "fd=3.\n";
		s[27] = "endif.\n";
		s[28] = "sub caca.\n";
		s[29] = " fdd = ds + 32.\n";
		s[30] = " type fd";
		s[31] = "   gf as ew\n";
		s[32] = "endtype.\nendsub.\n";
			
		return(s);
	}
	
	

}
