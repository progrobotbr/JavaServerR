package com.jsv.ztesteperf;

public class LinkedList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i;
		long li,lfim,lhi,lhf;
        LinkedList top, cursor;
        lfim = 1000000;
        lhi=System.currentTimeMillis();
        top = new LinkedList("Top",0);
        cursor=top.addNext("prim", 0);
        for(li=1;li<lfim;li++){
        	cursor = cursor.addNext("", li);
        }
        lhf=System.currentTimeMillis();
        System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
        lhi=System.currentTimeMillis();
        for(i=0;i<1;i++){
        	cursor=top;
        	while(cursor.next!=null){
        		//System.out.println(cursor.l);
        		cursor = cursor.getNext();
        	}
        }
        lhf=System.currentTimeMillis();
        System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
        
        
	}
	
	public String nome;
	public long l;
	public LinkedList next;
	LinkedList(String s,long pl){
		nome = s;
		l=pl;
	}
	public LinkedList addNext(String s,long pl){
		next=new LinkedList("Top",pl);
		return next;
	}
	
	
	public LinkedList getNext(){
		return(next);
	}
}
