package com.jsv.lang.vm;

import com.jsv.lang.vm.type.decltype;

public class function {
	
	public String SubName;
	public String PrgName;
	public int lineIdx;
	public String lineData;
	public decltype Inpar;
	public decltype Outpar;
	public classProgram ClsProgram;
	
	public function(){
		SubName="";
		PrgName="";
		lineIdx=0;
		lineData="";
		Inpar=null;
		Outpar=null;
		ClsProgram = null;
	}
	
	public function clone(){
		function f = new function();
		f.SubName = this.SubName;
		f.PrgName = this.PrgName;
		f.lineIdx = this.lineIdx;
		f.lineData = this.lineData;
		f.Inpar = this.Inpar;
		f.Outpar = this.Outpar;
		f.ClsProgram = this.ClsProgram;
		return(f);
	}

}
