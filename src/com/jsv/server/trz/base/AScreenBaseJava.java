package com.jsv.server.trz.base;

public abstract class AScreenBaseJava extends MemoryOp implements IScreenJava{
	
	private boolean isFirstLoad = true;
	private String  screenId    = "";
	private String  screenNext  = "";
	
	public final void setNextScreen(String s){
		screenNext=s;
	}
	
	public final String getNextScreen(){
		String s=screenNext;
		screenNext="";
		return(s);
	}
	
	public final void setScreenId(String s){
		screenId = s;
	}
	
	public final String getScreenId(){
		return(screenId);
	}
	
	public final void setIsFirstLoad(boolean b){
		isFirstLoad=b;
	}
	
	public final boolean getIsFirstLoad(){
		return(isFirstLoad);
	}
	
	public final boolean hasNextScreen(){
		if(screenNext.equals("")){
			return(false);
		}
		return(true);
	}
	
	//Subscreen
	public final void setSubScreen(ATransactionBaseJava ctx, String pcontainerName, String psubscreen){
		ctx.putSubScreen(this.screenId, pcontainerName, psubscreen);
	}
	
	public final void onScreenPbo(ATransactionBaseJava ctx, String pcontainer){
		String screen;
		AScreenBaseJava os;
		
		screen = ctx.loadGS(pcontainer);
		os = ctx.getScreenObj2(screen);
		if(os!=null){
			if(os.getIsFirstLoad()){
				os.onCreate(ctx);
				os.setIsFirstLoad(false);
			}
			os.onPBO(ctx);
		}
	}
	
	public final void onScreenPai(ATransactionBaseJava ctx, String pContainer){
		String screen;
		AScreenBaseJava os;
		screen = ctx.loadGS(pContainer);
		os = ctx.getScreenObj2(screen);
		if(os!=null){
			os.onPAI(ctx);
		}
	}
	
}
