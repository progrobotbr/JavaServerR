package com.jsv.ztesteperf;

public class TesteTable {

	public static void main(String[] args) {
		long lhi, lhf;
		int i;
		TreeLinkedList3 da = new TreeLinkedList3();
		lhi=System.currentTimeMillis();
		for(i=0;i<1000000;i++){
			da.addNew(i, i);
		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		
		lhi=System.currentTimeMillis();
		for(i=1;i<=1000000;i++){
			//System.out.print(da.find(i).value);
			da.find(i);
		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		
		
		lhi=System.currentTimeMillis();
		for(i=1;i<=1000;i++){
			da.delete(1);
		}
		
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		
		
	    da.printTree();
	}
	
	

}
