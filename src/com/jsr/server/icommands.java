package com.jsr.server;

public interface icommands {

	public static int INEWTRZ = 100;
	public static int IENTER  = 13;
	public static int INEWSCREEN = 120;
	public static int ISHUTDOWN = 180;
	public static int INEWJVM = 190;
	public static int ISUCCESS = 200;
	public static int IERROR = 4;
	public static int IDEBUGTURNON = 150;
	public static int IDEBUGON = 155;
	public static int IDEBUGAT = 160;
	public static int ISERVERPORT = 5555;
	public static int IENDVM = 230;
	public static int IERROR2 = -1;
	public static int IDEBUGRESINIT = 500;
	public static int IDEBUGRESATV = 501;
	public static int IMODERUN = 700;
	public static int IMODEDEBUG = 701;
	public static int IMODESUSP = 702;
	public static int IDEBUGSTEP = 165;
	public static int IDEBUGSTEPUP = 167;
	public static int IDEBUGCONT = 166;
	public static int IORIGDEB = 5;
	public static int IORIGJVM = 10;
	public static int IORIGCLT = 15;
	
	
	public static String SNEWJVM = "<?xml version=\"1.0\"><data><command>"+icommands.INEWJVM+"</command></data>";
	public static String SDEBCONT = "cont\n";
	public static String SDEBSTEP = "step\n";
	public static String SRETERROR = "Zoou\n";
	public static String SCMDNENT = "comando nao entendido\n";
	public static String SJVMSHUTDOWN = "shutdown\n";

	
}
