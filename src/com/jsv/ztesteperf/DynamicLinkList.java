package com.jsv.ztesteperf;

import com.jsv.ztesteperf.DynamicArray.Record;

public class DynamicLinkList {

	private int size=10;
	private int idx=-1;
	private int inull=0;
	private Record top;
	private Record cursor;
	private Record mIndex[] = new Record[100000];
	private int mIndexMarc;
	private int mIndexGap = 711;
	
	public static void main(String[] args) {
		int i,t;
		long lhi, lhf;
		Record rc;
		
		DynamicLinkList da = new DynamicLinkList();
		lhi=System.currentTimeMillis();
		t=1000000;
		for(i=0;i<t;i++){
			da.AddNew("T"+i);
			//System.out.println(i);
		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
//		da.toPrint();
		
		lhi=System.currentTimeMillis();
//		for(t=0;t<1;t++){
		    da.setFirst();
			for(i=0;i<t;i++){
				//rc=da.find(i);
				rc=da.getNext();
				//System.out.println(rc.nome);
			}
//		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
//		
		lhi=System.currentTimeMillis();
//		for(t=0;t<1;t++){
		int z1=1000,ii2=1;
		
			for(i=0;i<t;i++){
				//try{
				//if(i==5){
					//da.toPrint();
				//}
				/*
				if( (z1 * ii2)==i){
					ii2++;
					System.out.println("Saida :" +i);
				}
				*/
				da.Delete(t-i);
				//}catch(Exception ex){System.out.println("Erro:"+i); return;}
			}
//		}
			//da.Delete(t/2);
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		//da.toPrint();
	}
	
	public void toPrint(){
		Record rc=top;
		while(rc.next!=-1){
			rc = table[rc.next];
			System.out.println(rc.nome);
			
		}
	}
	DynamicLinkList(){
		table=new Record[size];
		top = new Record("");
		top.next=-1;
		cursor = top;
	}
	
	public Record getRecord(int pi){
		return(table[pi]);
	}
	
	public Record getNext(){
		cursor = table[cursor.next];
		return(cursor);
	}
	
	public void setFirst(){
		cursor = top;
	}
	
	Record table[];
	
	public void AddNew(String s){
		int i, inull, idxold;
		Record table2[]=null, rc;
		idxold=idx;
		idx++;
		if(idx>=size){
			size = size * 5 / 3 ;
			table2 = new Record[size];
			for(i=0;i<=idxold;i++){
				table2[i]=table[i];
			}
			table=table2;
		}
		inull = findNull();
		//rc = findAnt(idx);
		rc=cursor;
		rc.next = inull;
		cursor = table[inull] = new Record(s);
		
		//Acerta indice
	    int mod = idx % mIndexGap;
	    if(mod==0){
	    	if(idx > (mIndexGap-1)){
	    	  mIndex[mIndexMarc] = cursor;
	    	  mIndexMarc++;
	    	}
	    }
		
	}
	
	public Record findAnt(int pidx){
		int i=-1;
		Record rc;
		
		pidx--;
		
		if(pidx == -1) { return top; }
		
		rc = top;
		
		for(i=0;i<=pidx;i++){
			rc = table[rc.next];
		}
		return(rc);
	}
	
	public Record find(int pidx){
		int i;
		Record rc;
		
		rc = top;
		
		for(i=0;i<=pidx;i++){
			rc = table[rc.next];
		}
		return(rc);
	}
	
	public int findNull(){
		int i=-1;
		for(i=inull;i<size;i++){
			if(table[i]==null){
				inull = i;
				return(i);
			}
		}
		for(i=0;i<=inull;i++){
			if(table[i]==null){
				inull = i;
				return(i);
			}
		}
		
		return(i);
	}
	
	public void Delete(int idx){
		int t,i,idxAnt,idxAtu,calc;
		Record table2[]=null;
		Record rcAnt=null,rc=null;

		idxAnt = idx-1;
		rcAnt=top;
		for(i=0,t=0;t<mIndexMarc;t++){
			i+=mIndexGap;
			if(i>idxAnt){
				i-=mIndexGap;
				rcAnt = mIndex[t];
				break;
			}
			/*if(t>1){
				System.out.println("T");
			}*/
		}
		
		for(;i<=idxAnt;i++){
			if(rcAnt.next==-1) break;
			rcAnt = table[rcAnt.next];
		}
		
		if(idxAnt==-1){
			rcAnt=top;
		}
		if(rcAnt.next == -1) return;
		
		rc=table[rcAnt.next];
		idxAtu = rcAnt.next;
		rcAnt.next = rc.next;
		table[idxAtu]=null;
		--this.idx;
		--size;
		calc = table.length * 3 / 5;
		
		if(size <= calc){
			table2 = new Record[size];
			rcAnt = rc = top;
			i=0;
			mIndexMarc=0;
			int mod;
			while(rc.next != -1){
				rc = table[rc.next];
				table2[i] = rc;
			    rcAnt.next = i;
			    rcAnt = rc;
			    i++;
		
			    //Acerto dos indices
			    mod = i % mIndexGap;
			    if(mod==0){
			    	if(i > (mIndexGap-1)){
			    	  mIndex[mIndexMarc] = rc;
			    	  mIndexMarc++;
			    	}
			    }
			    
			}
			table = table2;
			
		}
			
		
	}
	
	public class Record{
		
		public Record(String s){
			nome=s;
			next = -1;
		}
		public Record(){
			
		}
		public String nome;
		public int next;
	}
}
