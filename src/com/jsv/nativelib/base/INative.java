package com.jsv.nativelib.base;

import com.jsv.db.base.sqlbase;
import com.jsv.lang.vm.frame;

public interface INative {
	
	public void execute(frame f);
	public int getRc();
	public String getErrmsg();
	public void setSqlBase(sqlbase pSqlBase);
	
	/*
	 *  Exemplo de programa bsic, repare a chamada nativecall,
	 *   esta chamada pega as variáveis do frame corrente, pois
	 *   a função nativa possui como parâmetro o frame atual
	 *
	 	dim v1 as i.
		dim v2 as i.
		dim r2 as i.
		dim x1 as i.
		v1 = 5.
		v2 = 7.
		call calm out resultado = r2
          		  in  valor1 = v1
              		  valor2 =  v2.
		x1 = r2.
		
		sub calm in valor1 as i
            		valor2 as i
         		out resultado as i.
   			nativecall calcmais.
		endsub.
	  
	 */
}
