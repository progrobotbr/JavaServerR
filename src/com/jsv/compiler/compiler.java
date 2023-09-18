package com.jsv.compiler;

import java.io.StringReader;

import com.jsv.db.base.sqlbase;

import com.jsv.lang.program.format;
import com.jsv.lang.program.program;
import com.jsv.editor.AParserAll;
import com.jsv.editor.editor;

public class compiler {
	
	public static final String PRGC = "PRGC";
	public static final String PRGS = "PRGS";
	public static final String MAPS = "MAPS";
	
	public int rc; 
	public String errmsg;
	
	public void compile(sqlbase pSqlBase, String pSource, String pName, String pDescr){
		int i=0;
		
		clearRc();
		
		String s=pSource;
		StringReader st = new StringReader(s);
		format source = new format();
		program oprogram;
		AParserAll yyparser = new AParserAll(st);
		i=yyparser.yyparse();
		if(i==0){
			oprogram = source.getEditor(s);
			s=oprogram.getCompiledProgram();
			rc=oprogram.saveProgramDB(pSqlBase, pName, compiler.PRGC, pDescr, s);
			errmsg=oprogram.errmsg;
		}else{
			rc=i;
			errmsg="Erro: posição("+yyparser.errposition+")+ última palavra("+yyparser.getWord()+")";
		}
	}
	
	private void clearRc(){
		rc=0;
		errmsg="";
	}
}
