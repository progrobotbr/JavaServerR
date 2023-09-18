package com.jsv.ztesteperf;

public class table_btree {
	private long size;
	private node root;
	
	public static void main(String argv[]){
		int i,t,z;
		long lhi, lhf;
		table_btree ot=new table_btree();
		
		lhi=System.currentTimeMillis();
		//ot.insertRoot(0,0);
		//ot.insertRoot(1,1);
		//ot.insertRoot(2,2);
		t=1000000;
		for(i=0;i<t;i++){
		  if(i==21){
			  z=1;
		  }
		  ot.insertRoot(i,i);
		  //System.out.println("d:"+i);
		}
		//node_search ns = ot.searchRoot(20);
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		
		lhi=System.currentTimeMillis();
		for(i=0;i<t;i++){
			node_search ns = ot.searchRoot(i);
		}
		lhf=System.currentTimeMillis();
		System.out.println("Exec "+lhf + " " + lhi + " = " + ( lhf-lhi ));
		/*
		ot.insertRoot(11,1);
		ot.insertRoot(12,2);
		ot.insertRoot(13,3);
		ot.insertRoot(14,4);
		ot.insertRoot(14,5);
		ot.insertRoot(14,6);
		ot.insertRoot(14,7);
		*/
		
	}
	
	public table_btree(){
		this.createTree();
	}
	
	public void createTree(){
		root = createNode();
		root.leaf=true;
	}
	
	public void insertRoot(long pvalue, int pindex){
		int t;
		node n;
		node_search ns;
		
		if(pindex>size+1){
			return;
		}
		
	    ns=searchRoot(pindex);
	    n=ns.n;
	    t=ns.pos;
	    insert(pvalue,t,n,null,false);
	    
	}
	
	public void insert(long pvalue, int posinnode, node n1, node n2, boolean pquebra){
		int ipai, isum;
	    node p, nl1;
	    
	    if(n1.leaf==true){
	    	isum=n1.sum;
	    	this.nodePutValue(n1,pvalue,posinnode);
	    	if(n1.tam==node.TAMNODE){
	    		nl1 = this.quebra(n1);
	    		nl1.leaf=true;
	    		p=n1.parent;
	    		if(p==null){
	    			root=createNode();
	    			ipai=0;
	    			n1.parent=root;
	    			n1.parentidx=ipai;
	    			nl1.parent=root;
	    			nl1.parentidx=ipai+1;
	    			root.data[ipai]=n1;
	    			root.data[ipai+1]=nl1;
	    			root.sum=n1.sum+nl1.sum;
	    			root.tam=2;
	    			return;
	    		}
	    		p.sum-=isum;
	    		p.sum+=n1.sum;
	    		ipai=n1.parentidx;
	    	    ipai++;
	    		insert(0,ipai,p,nl1,true);
	    	}else{
	    		p=n1.parent;
	    		if(p==null){
	    			return;
	    		}
	    		p.sum++;
	    		insert(0,0,p,null,false);
	    	}
	    }else{
	    	if(pquebra==true){
	    		this.nodePut(n1,n2,posinnode);
	    		if(n1.tam==node.TAMNODE){
	    			nl1 = this.quebra(n1);
		    		p=n1.parent;
		    		if(p==null){
		    			root=createNode();
		    			ipai=0;
		    			n1.parent=root;
		    			n1.parentidx=ipai;
		    			nl1.parent=root;
		    			nl1.parentidx=ipai+1;
		    			root.data[ipai]=n1;
		    			root.data[ipai+1]=nl1;
		    			root.sum=n1.sum+nl1.sum;
		    			root.tam=2;
		    			return;
		    		}
		    		ipai=n1.parentidx;
		    		ipai++;
		    		insert(0,ipai,p,nl1,true);
		    	}else{
		    		p=n1.parent;
		    		if(p==null){
		    			return;
		    		}
		    		p.sum++;
		    		insert(0,0,p,null,false);
		    	}
	    	}else{
	    		p=n1.parent;
	    		if(p==null){
	    			return;
	    		}
	    		p.sum++;
	    		insert(0,0,p,null,false);
	    	}
	    		    	
	    }
	    
	}
	
	private void nodePutValue(node n, long pvalue, int pos){
	   	int i,t,z;
	   	node n1;
	   	n1=createNode();
	   	n1.value=pvalue;
	   	n1.sum=1;
	   	t=n.tam;
	   	z=t-1;
	   	for(i=t;i>pos;i--){
	   		n.data[i]=n.data[z];
	   		z--;
	   	}
	   	n.data[pos]=n1;
	   	n.tam++;
	   	n.sum++;
	   	n1.parentidx=pos;
	   	n1.parent=n;
	   	size++;
	}
	
	private void nodePut(node n1, node n2, int pos){
		int i,t,z;
		t=n1.tam;
	   	z=t-1;
	   	for(i=t;i>pos;i--){
	   		n1.data[i]=n1.data[z];
	   		z--;
	   	}
	   	n1.data[pos]=n2;
	   	n1.tam++;
	   	if(n2.leaf==true){
	   		n1.sum+=n2.sum;
	   	}else{
	   		n1.sum++;
	   	}
	   	n2.parent=n1;
	   	n2.parentidx=pos;
	}
	
	private node quebra(node n){
		int i,t,z;
		node n1=null;
		t=n.tam;
		n.tam=node.TAMNODE/2;
		n1=createNode();
		n1.tam=n.tam;
		n1.sum=0;
		z=0;
		//Quebra
		for(i=n.tam;i<t;i++){
			n1.data[z]=n.data[i];
			n.data[i].parentidx=z;
			n.data[i].parent=n1;
			n1.sum+=n.data[i].sum;
			n.data[i]=null;
			z++;
		}
		//Ajusta total do pai
		t=n.tam;
		n.sum=0;
		for(i=0;i<t;i++){
			n.sum+=n.data[i].sum;
		}
		return(n1);
		
	}
	

	
	public node_search searchRoot(int pindex){
		node_search n;
		n=search(root,pindex,0);
        return(n);		
	}
	
    public node_search search(node x, int pindex, int ipsum){
    	int i,t;
    	int isum, isum2;
    	node n=null;
    	node_search ns;
    	
    	t=x.tam;
    	isum2=isum=ipsum;
    	if(x.leaf==false){
    		for(i=0;i<t;i++){
    			n=x.data[i];
    			isum2+=n.sum;
    			if(isum2>=pindex){
    				break;
    			}
    			isum=isum2;
    		}
     		ns=search(n,pindex,isum);
    	}else{
    		t=pindex-ipsum;
    		ns=new node_search();
    		ns.n=x;
    		ns.pos=t;
    		
    	}
    	return(ns);
    }
	    
	private node createNode(){
		node n=new node();
		n.tam=0;
		n.leaf=false;
		return(n);
	}
	private node createLeaf(){
		node n=new node();
		n.tam=0;
		n.leaf=true;
		return(n);
	}
	private void set_end(node s, int i, node r){
	    s.data[i]=r;
	}



	public class node{
		public static final int TAMNODE=12; //bom 12
		public int sum;
		public int tam;
        public node pointer;
        public boolean leaf;
        public node[] data=new node[TAMNODE];
        public node parent;
        public int parentidx;
        public long value;
    }
	public class node_search{
		public node n;
		public int pos;
	}

}
