package com.jsv.ztesteperf;

public class DynamicArray {

	private int size=10;
	private int idx=-1;
	public static void main(String[] args) {
		int i,t;
		long lhi, lhf;
		Record rc;
		DynamicArray da = new DynamicArray();
		lhi=System.currentTimeMillis();
		for(i=0;i<20000;i++){
			da.AddNew("T", i);
		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec Insert "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		
		lhi=System.currentTimeMillis();
		for(t=0;t<1;t++){
			for(i=0;i<20000;i++){
				rc=da.getRecord(i);
				//System.out.println(rc.l);
			}
		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec Search "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		
		lhi=System.currentTimeMillis();
		for(t=0;t<1;t++){
			for(i=0;i<20000;i++){
				da.Delete(5);
			}
		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec Delete "+lhf + " " + lhi + " = " + ( lhf-lhi ));
	}
	
	DynamicArray(){
		table=new Record[size];
	}
	
	public Record getRecord(int pi){
		return(table[pi]);
	}
	
	Record table[];
	
	public void AddNew(String s, long pidx){
		int i, idxold;
		Record table2[]=null;
		idxold=idx;
		idx++;
		if(idx>=size){
			size = size * 5 / 3 ;
			table2 = new Record[size];
			System.arraycopy(table, 0, table2,0,idxold); //arg1, arg2, arg3, arg4)
			//for(i=0;i<=idxold;i++){
			//	table2[i]=table[i];
			//}
			table=table2;
		}
		table[idx] = new Record("T",pidx);
	}
	
	public void Delete(int idx){
		int i,z,calc;
		Record table2[]=null;
				
		if(idx>=size)return;
		--size;
		z=idx-1;
		calc = table.length * 3 / 5;
		if(size <= calc){
			table2 = new Record[size];
			i=0;
			System.arraycopy(table, i, table2, 0, idx);
			//for(;i<z;i++){
			//	table2[i] = table[i];
			//}
			i = z;
			z=idx+1;
			System.arraycopy(table, z, table2, idx, size-idx);
			//for(;i<size;i++){
			//	table2[i] = table[z++];
			//}
			table = table2;
		}else{
			i=0;
			if(z>-1)
				System.arraycopy(table, i, table, 0, z);
			//for(;i<z;i++){
			//	table[i] = table[i];
			//}
			
			z=idx+1;
			i=z;
			System.arraycopy(table, i, table, idx,size-idx);
			//for(;i<size;i++){
			//	table[i] = table[z++];
			//}
		}
			
		
	}
	
	public class Record{
		
		public Record(String s, long idx){
			nome=s;
			l=idx;
		}
		public Record(){
			
		}
		public String nome;
		public long l;
	}
}
