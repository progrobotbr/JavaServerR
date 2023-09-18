package com.jsv.lang.program;

public class line {
	
	public int logicalLine = 0; //Conterá o indice da linha do programa formatado
	public int fisicalLine=0;   //Conterá o indice da linha do programa físico
	public int gotoLine=0;      //Indica linha de goto. Usado em if, while, for...
	public int canDebug=0;      //Indica se a linha é debugável
	public String lineData="";  //Código
	
}
