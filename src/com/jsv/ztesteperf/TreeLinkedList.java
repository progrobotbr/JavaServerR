package com.jsv.ztesteperf;

public class TreeLinkedList {

    public stackNode stack[];
    public int stackPointer;
    public int stackQtdElem;
    public int listQtdElem;
    public node list;
	
    public static void main(String argv[]){
    	TreeLinkedList tl = new TreeLinkedList();
    	tl.addNew(0, 10);
    	tl.addNew(1, 20);
    	tl.addNew(2, 30);
    	tl.addNew(3, 40);
    	tl.addNew(4, 50);
    	tl.addNew(5, 60);
    	tl.print();
    }
    
    public TreeLinkedList(){
    	this.init();
    }
    
    public void init(){
    	list = new node();
    	list.value = -1;
    	stackNode sn = new stackNode();
    	stack=new stackNode[1000];
    	stack[0] = sn;
    	sn.top = new node();
    	stackPointer=0;
    	stackQtdElem=0;
    	listQtdElem=0;
    }
    
    public void addNew(int pi, int pvalue){
    	int iEnd = listQtdElem+1;
    	int iAnt = pi - 1;
    	int ie;
    	node np[];
    	
    	if(pi>iEnd){
    		log("Muito maior que o final");
    		return;
    	}
    	
    	//ie = getExp();
    	ie=0;
    	recursivePutNode(pi, ie, pvalue, null);
    	
    	//np = findPosition(iAnt);
    	
    	
    	
    	//putNode(np,pvalue);
    	
    	
    }
    
    public void recursivePutNode(int pIdx, int pLevel, int pValue, node pN[]){
    	int i=0;
    	node ns[]=new node[2];
    	node n, na=null /*nodeant*/, np=null/*nodepos*/, nl, nv, na2,na3;
    	stackNode sn;
    	if(pLevel == 0){
    		sn = stack[0];
    		n=sn.top;
    		na=n;
    		while(n!=null){
    			if(n.sum<=pIdx){
    				na=n;
    			}else{
    				np=n;
    				break;
    			}
    			n=n.next;
    		}
    		ns[0]=na; ns[1]=np;
    		nv = new node();
    		nv.value=pValue;
    		if(na.sum == 0){
    			list.next = nv;
    			na.marci = nv;
    			na.sum=1;
    			listQtdElem++;
    		}else if(na.sum==1){
    			n = na.marci;
    			nv.next = n.next;
    			n.next = nv;
    			na.marcf = nv;
    			na.sum = 2;
    			listQtdElem++;
    		}else if(na.sum==2){
    			n = new node();
    			na2 = na.marcf;
    			nv.next = na2.next;
    			na2.next = nv;
    			na.next = n;
    			n.sum=1;
    			n.marci = nv;
    			listQtdElem++;
    		}
    		return;
    		
    	}else{
    		if(pN == null)
    		recursivePutNode(pIdx, pValue, pLevel, pN);
    	}
    }
    
    public void putNode(node n[], int pvalue){
    	node nv;
    	nv = new node();
    	nv.value = pvalue;
    	nv.prev = n[0];
    	n[0].next = nv;
    	if(n[1]!=null){
    		n[1].prev = nv;
    		nv.next = n[1];
    	}
    }
    
    public int getExp(){
    	int ie=0, it=2;
    	while(it < stackQtdElem){
    		it *= 2; 
    		ie++;
    	}
    	return(ie);
    	
    }
    
    public void print(){
    	node n;
    	n=list;
    	while(n!=null){
    		log(n.value+"-");
    		n=n.next;
    	}
    }
    
    public node[] findPosition(int pIdx){
    	int ipointeri, ipointerf;
    	
    	node n[] = new node[2];
    	n[0]=null;
    	n[1]=null;
    	
    	ipointeri = 0;
    	ipointerf = stackPointer;
    	
    	if(pIdx == -1){
    		n[0] = stack[0].top;
    		return(n);
    	}
    	return(n);
    }
    
	public class node{
		public node next=null;
		public node prev=null;
		public node marci=null;
		public node marcf=null;
		public int sum=0;
		public int value=0;
		
	}
	
	public class stackNode{
		public int level=0;
		public node top=null;
	}
	
	public void log(String s){
		System.out.println(s);
	}
	
}
