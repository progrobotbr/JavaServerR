package com.jsv.ztesteperf;

public class TreeLinkedList3_bk {

    public stackNode stack[];
    public int stackPointer;
    public int stackQtdElem;
    public int listQtdElem;
    public node list;
	
    public static void main(String argv[]){
    	TreeLinkedList3_bk tl = new TreeLinkedList3_bk();
    	/*
    	for(int i=1;i<100000;i++){
    		tl.addNew(i, i);
    	}
    	*/
    	//tl.addNew(0, 10);
    	
    	tl.addNew(1, 10);   tl.printTree();
    	tl.addNew(2, 20);   tl.printTree();
    	tl.addNew(3, 30);   tl.printTree();
    	tl.addNew(4, 40);   tl.printTree();
    	tl.addNew(5, 50);   tl.printTree();
    	tl.addNew(6, 60);   tl.printTree();
    	tl.addNew(7, 70);  	  tl.printTree();
    	tl.addNew(8, 80);  	  tl.printTree();
    	tl.addNew(9, 90);  	  tl.printTree();
    	tl.addNew(10, 100);	  tl.printTree();
    	tl.addNew(11, 110);	 tl.printTree();
    	tl.addNew(12, 120);
    	tl.addNew(13, 130);
    	tl.addNew(14, 140);
    	tl.addNew(15, 150);
    	tl.addNew(16, 160);
    	tl.addNew(3, 331);
    	tl.addNew(3, 332);
    	tl.addNew(3, 333);
    	
    	/*
    	tl.addNew(1, 1130);
    	tl.addNew(1, 1140);
    	tl.addNew(1, 1150);
    	*/
    	tl.addNew(2, 1180);
    	
    	tl.print();
    	
    }
    
    public TreeLinkedList3_bk(){
    	this.init();
    }
    
    public void init(){
    	list = new node();
    	list.value = -1;
    	stackNode sn = new stackNode();
    	stack=new stackNode[5];
    	stack[0] = sn;
    	sn.top = new node();
    	sn.top.value=-2;
    	stackPointer=0;
    	stackQtdElem=0;
    	listQtdElem=0;
    }
    
    public void addNew(int pi, int pvalue){
    	node nv;
    	nv = new node(pvalue);
    	recursivePutNode(pi, nv, 2);
    	
    }
    
    public boolean recursivePutNode(int pIdx, node pV, int pLevel){
    	boolean bput = false;
    	int i, i1, i2, i3, is=0, is0, is1, iEnd;
    	node n, n0, n1, n2, nv;
    	stackNode sn=null;
    	node mstack[][] = new node[100][3];
    	int mpstack=-1;
    	
    	iEnd = listQtdElem+1;
    	if(pIdx>iEnd){
    		return(false);
    	}
    	
    	is0 = 0;
    	is1 = 0;
    	n0=null;
    	n1=null;
    	for(i=stackPointer;i>=0;i--){
    		if(n0==null||is1==0){
    			sn=stack[i];
    			n1=sn.top;
    		}else{
    			n1=n0.marc;
    		}
    		n2=new node();
    		/*
    		while(n1!=null){
    			if(is1 >= pIdx){
    				break;
    			}
    			is0 = is1;
    			is1 += n1.sum;
    			n0 = n1;
    			n1 = n1.next;
    		}*/
    		while(n1!=null){
    			if(is1 >= pIdx){
    				break;
    			}
    			is0 = is1;
    			is1 += n1.sum;
    			n0 = n1;
    			n1 = n1.next;
    		}
    		if(is1>=pIdx){
    			is1 = is0;
    		}
    		n2.sum=is1;
    		mpstack++;
    		mstack[mpstack][0]=n0;
    		mstack[mpstack][1]=n1;
    		mstack[mpstack][2]=n2;
    		
    		if(is1 >= pIdx){
    			break;
    		}
    	}
   	
    	//0
    	n0=mstack[mpstack][0];
		n1=mstack[mpstack][1];
		n2=mstack[mpstack][2];
		if(n2.sum==0){
			nv = new node();
			nv.level = 1;
			nv.marc = pV;
			n0.next = nv;
			pV.parent = nv;
			nv.prev = n0;
			nv.sum=1;
			list.next = pV;
			pV.prev = list;
			listQtdElem++;
			bput = true;
		}else{
			i2 = n2.sum;
			if(n1==null){ n1=n0; }
			n=n1.marc;
			for(i=i2;i>pIdx;i--){
				n=n.prev;
			}
			if(pIdx>i2){
				n.next = pV;
				pV.prev = n;
				n1.marc = pV;
				pV.parent = n1;
			}else{
				this.conNode(n.prev, n, pV);
			}
			listQtdElem++;
			n1.sum++;
			bput = true;
			if(n1.sum==4){
				n=n1.marc;
				for(i=0;i<2;i++){
					n=n.prev;
				}
				nv=new node();
				nv.level = 1;
				nv.sum=2;
				nv.marc = n;
				n.parent = nv;
				n1.sum=2;
				if(n1.prev==null){
					sn.top = nv;
					nv.next = n1;
					n1.prev = nv;
					
				}else{
					this.conNode(n1.prev, n1, nv);
				}
			}
			
		}
		
		//1..n-1
    	for(i=mpstack-1; i>=0; i--){
    		n0=mstack[i][0];
    		n1=mstack[i][1];
    		n2=mstack[i][2];
    		if(bput == true){
    			if(n1==null){
    				n0.sum++;
    			}else{
    				n1.sum++;
    			}
    		}
    		i1=0;
    		if(n1==null){
    			n=n0.marc;
    			n2=n0;
    		}else{
    			n=n1.marc;
    			n2=n1;
    		}
    		for(i2=0; i2<3; i2++){
    			n = n.prev;
    			if(n.parent != null || n.value==-2){
    				break;
    			}
    		}
    		if(i2==3){
    			n = n.next;
    			nv=new node();
    			nv.level= stackPointer-i;
    			nv.sum = n.sum + n.prev.sum;
    			nv.marc = n;
    			n.parent = nv;
    			n2.sum = n2.sum - nv.sum;
    			this.conNode(n2.prev, n2, nv);
    		}
    	}
    	
    	//n
    	sn=stack[stackPointer];
    	n = sn.top;
    	is1=0;
    	for(i=0;i<2;i++){
    		n=n.next;
    		if(n==null){
    			break;
    		}
    		is1+=n.sum;
    	}
    	if(i==2){
    		stackPointer++;
    		sn = new stackNode();
    		stack[stackPointer]=sn;
    		sn.top = new node();
    		n1=sn.top;
    		n1.value=-2;
    		nv=new node();
    		nv.level = stackPointer;
    		nv.marc = n;
    		nv.sum=is1;
    		n.parent = nv;
    		n1.next = nv;
    		nv.prev=n1;
    		
    	}
    		
     	return(true);
    	
    }
       
    public void conNode(node o1, node d1, node vl){
    	
    	o1.next = vl;
    	vl.prev = o1;
    	vl.next = d1;
    	d1.prev = vl;
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
		public node marc=null;
		public node parent=null;
		public node child=null;
		public int level=0;
		public int sum=0;
		public int value=0;
		
		public node(int pi){
			value = pi;
		}
		
		public node(){
			
		}
		
	}
	
	public class stackNode{
		public int level=0;
		public node top=null;
	}
	
	public void log(String s){
		System.out.println(s);
	}
	public void lg2(String s){
		System.out.print(s);
	}
	public void lge(){
		System.out.println("");
	}
	
	public void printTree(){
		int i;
		node n;
		n=list;
		n=n.next;
		log("**INI**");
		while(n!=null){
			lg2("x_");
			n=n.next;
		}
		lge();
		for(i=0;i<=stackPointer;i++){
			n=stack[i].top;
			n=n.next;
			while(n!=null){
				lg2("x_");
				n=n.next;
			}
			lge();
		}
	}
	
}
