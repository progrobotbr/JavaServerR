package com.jsv.lang.program;

public class line {
	
	public int logicalLine = 0; //Conter� o indice da linha do programa formatado
	public int fisicalLine=0;   //Conter� o indice da linha do programa f�sico
	public int gotoLine=0;      //Indica linha de goto. Usado em if, while, for...
	public int canDebug=0;      //Indica se a linha � debug�vel
	public String lineData="";  //C�digo
	
}
