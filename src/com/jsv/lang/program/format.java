package com.jsv.lang.program;
import java.util.Vector;

public class format {
	
	public String[] getPrg(){
		String s[]=new String[19];
		s[0] = "dim sname as c[30].\n";
		s[1] = "dim sendereco as c[50].\n";
		s[2] = "dim num1 as i.\n";
		s[3] = "dim num2 as i.\n";
		s[4] = "dim num3 as i.\n";
		s[5] = "num1 = 10.\n";
		s[6] = "num2 = 10.\n";
		s[7] = "num3 = num1 + num2.\n";
		s[8] = " sub calc\n";
		s[9] = "  in pnum1 as i\n";
		s[10] = "    pnum2 as i\n";
		s[11] = "  out pres as i.\n";
		s[12] = "  pres = pnum1 * pnum2.\n";
		s[13] = "endsub.\n";
		
		s[14] = "type ds\n";
		s[15] = "  ds as i\n";
		s[16] = "  rerer as i\n";
		s[17] = "  nome as c[30]\n";
		s[18] = "endtype.\n";
		
		return(s);
	}
	
	public String[] getDim(){
		String s[]=new String[5];
		s[0] = "dim sname as c[30]\n";
		s[1] = "dim sendereco as c[50]\n";
		s[2] = "dim num1 as i\n";
		s[3] = "dim num2 as i\n";
		s[4] = "dim num3 as i\n";
		return(s);
	}

	public String[] getType(){
		String s[]=new String[5];
		s[0] = "type ddname\n";
		s[1] = " sendereco as c[50]\n";
		s[2] = " num1 as i\n";
		s[3] = " num2 as i\n";
		s[4] = "endtype\n";
		return(s);
	}
	
	public program getEditor(String source){
		boolean bf = false; // retira o branco no inicio da frase. Desidenta, mas compacta o código
		boolean bp = false; // não deixa branco depois do ponto no início da frase 
		boolean bLine = false;
		byte b[], b1[], b2[];
		//char c;
		int z=0,t=1, iLine=0;
		String s[];
		String sr;
		
		Vector <String>ed = new Vector<String>();
		StringBuilder sb = new StringBuilder();
		program pg = new program();
				
		s=new String[1];
		pg.init(s);
		b = source.getBytes();
		b1 = new byte[1024]; 
		z=0;
		bf=true;
		bp=true;
		for(int i=0;i<b.length; i++){
			if(i==18){
				s=new String[1];
			}
			/*c = (char) b[i];
			if(i==352){
				int ooo = 1;
			}
			System.out.print(c);
			*/
			if(b[i]=='.'){
				b1[z++] = '.';
				b1[z++] = '\n';
				b2 = new byte[z];
				System.arraycopy(b1, 0, b2, 0, z);
				sr = new String(b2);
				ed.add(sr.toUpperCase());
				z=0;
				bp = true; //controle dos brancos
				bf = true; //controle dos brancos 
				bLine=true;
				pg.putLine(sr.toUpperCase(), iLine);
				//System.out.println(iLine);
			}else if(b[i] == '\n'){
					if(bp==false) { 
						b1[z++]=' '; 
					}
					bf=true;t++;
				  }else if(b[i]==' ' || b[i]=='\t'){
					       if(bf!=true){ 
					    	   b1[z++] = b[i]; 
					    	   bf=true; 
					    	}
				        }else if(b[i]== '\r'){ 
				        	//Ignora caracter
				        }else{
				        	b1[z++]=b[i];
				        	bf=false;
				        	bp=false;
				        	if(bLine==true) { 
				        		iLine = t; bLine=false;
				        	}
				    	}
		}
		if(z>0){ //Adiciona a sobra
			b2 = new byte[z];
			System.arraycopy(b1, 0, b2, 0, z);
			sr = new String(b2);
			ed.add(sr.toUpperCase());
			pg.putLine(sr.toUpperCase(), iLine);
		}
		
		/*
		String tt[] = new String[ed.size()];
		
		for(int i=0;i<ed.size();i++){
			sr = ed.get(i);
			tt[i] = sr;
		}
		*/
		pg.organize();
		pg.setFisicalProgram(source);
		return(pg);
		
	}
}
