package com.jsv.zteste;

public class Teste5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       Teste5 ot = new Teste5();
       String s;
       s=ot.trata("Select nome from casa where casa = dsd and casa = renato ");
       System.out.println(s);
	}
	
	public String trata(String s){
		boolean bpal=false, bpalini=false;
		byte b;
		byte[] bs = s.getBytes();
		int i,tam,indiceini,indicefim,t;
		char c=' ';
		String saida="",sPal="", sVal="";
        
        tam=bs.length;
		
		for(i=0;i<tam;i++){
			b = bs[i];
			c = (char) b;
			if(bpal == true){
				if(c == '_' || c == '-' || ( c >= 'a' && c <= 'z' ) || ( c >= 'A' && c <= 'Z') || ( c >= '0' && c <= '9')){
					sPal = sPal + c;
				}else{
					t = descobre_o_tipo_da_palavra(sPal);
					switch(t){
						case 0: 
						case 1: sVal = pega_valor_na_vm(sPal);
								saida += sVal;
								break;
					}
					sPal = "";
					bpal=false;
					bpalini=false;
				}
			}else{
				saida += c;
			}
			if(c=='='){
				bpal = true;
			}
			
		}
		 
	return(saida);
		  
		}     
		
	

    public int descobre_o_tipo_da_palavra(String s){
    	return(0);
    }
    
    public String pega_valor_na_vm(String s){
    	return("Renato");
    	
    }
    
}
