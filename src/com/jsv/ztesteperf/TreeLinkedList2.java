package com.jsv.ztesteperf;

public class TreeLinkedList2 {

    public stackNode stack[];
    public int stackPointer;
    public int stackQtdElem;
    public int listQtdElem;
    public node list;
	
    public static void main(String argv[]){
    	TreeLinkedList2 tl = new TreeLinkedList2();
    	/*
    	for(int i=1;i<100000;i++){
    		tl.addNew(i, i);
    	}
    	*/
    	//tl.addNew(0, 10);
    	
    	tl.addNew(1, 10);
    	tl.addNew(2, 20);
    	tl.addNew(3, 30);
    	tl.addNew(4, 40);
    	tl.addNew(5, 50);
    	tl.addNew(6, 60);
    	tl.addNew(7, 70);
    	tl.addNew(8, 80);
    	tl.addNew(9, 90);
    	tl.addNew(10, 100);
    	tl.addNew(11, 110);
    	
    	/*
    	tl.addNew(1, 1130);
    	tl.addNew(1, 1140);
    	tl.addNew(1, 1150);
    	*/
    	tl.addNew(2, 1180);
    	
    	tl.print();
    	
    }
    
    public TreeLinkedList2(){
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
    	node nv;
    	
    	if(pi>iEnd){
    		log("Muito maior que o final");
    		return;
    	}
    	
    	nv = new node(pvalue);
    	recursivePutNode(pi, nv, 2);
    	
    }
    
    public boolean recursivePutNode(int pIdx, node pV, int pLevel){
    	int i, is=0, iEnd;
    	double piece;
    	node n, n1, n2, na=null, np=null, nv, nt;
    	stackNode sn;
    	
    	iEnd = listQtdElem+1;
    	if(pIdx>iEnd){
    		return(false);
    	}
    	
    	i=pLevel;
    	sn=stack[i];
    	if(sn==null) {
    		stack[i]=new stackNode();
    		sn=stack[i]; 
    		sn.top = new node(); 
    	}
    	n=sn.top;
    	piece = Math.pow(2,(double)(i+1));
    	while(n!=null){
    		is+=n.sum;
    		if(is==pIdx){
    			if(n.sum == piece){
    				np=n.next;
    				if(np.sum==piece){
    					nv=new node();
       					nv.marc = n.marc;
       					nv.sum=1;
       					this.conNode(n.marc.prev, n.marc, pV);
       					this.conNode(n, np, nv);
       					n.marc = pV;
       					listQtdElem++;
    				}else{
    					//n.sum=2;
    					n.sum++;
       					this.conNode(n.marc.prev, n.marc, pV);
       					n.marc=pV;
        				listQtdElem++;
    				}
    			}else{
    				//n.sum=2;
    				n.sum++;
    				this.conNode(n.marc.prev, n.marc, pV);
    				listQtdElem++;
				}
    			return(true);
    		}else if(is>pIdx){
    			na=n.prev;
				if(n.sum == piece){
					int t,y;
					if(pLevel>0){
						t=is-pIdx;
						if(n.prev!=null){
							t=pIdx-(is-n.sum);
							y=n.sum-pIdx;
						}else{
							t=n.sum-pIdx;
							y=n.sum-t;
						}
					}else{
						t=1;
						y=n.sum;
					}
					nv=new node();
    				nv.marc = pV;
    				//nv.sum=1;
    				//nv.sum++;
    				n.sum=y;
    				nv.sum=t;
    				if(na==null){
    					this.conNode(n.marc.prev.prev, n.marc.prev, pV);
    					n.prev = nv;
    					nv.next = n;
    					sn.top=nv;
    					listQtdElem++;
    				}else{
    					this.conNode(na.marc, na.marc.next, pV);
    					this.conNode(n.prev, n, nv);
    					listQtdElem++;
    				}
    			}else{
    				//n.sum=2;
    				n.sum++;
    				this.conNode(na.marc, na.marc.next, pV);
    				na.marc=pV;
    				listQtdElem++;
    			}
    			return(true);
    		}
    		na=n;
    		n=n.next;
    	}
    		
    	//Fora
    	if(is<=pIdx){
    		if(na.sum<piece){
				if(is==pIdx){
					n1 = na.marc;
					if(n1==null){
						list.next=pV;
						pV.prev = list;
						na.marc=pV;
					}else{
						n2 = n1.prev;
						if(n2!=null){
							n2.next = pV;
						}
						n1.prev = pV;
						pV.next = n1;
					}
					na.sum++;
				}else{
					n1 = na.marc;
					if(n1==null){
						list.next=pV;
						pV.prev = list;
						na.marc=pV;
					}else{
						n2 = n1.next;
						n1.next = pV;
						pV.prev = n1;
						pV.next = n2;
						na.marc=pV;
					}
					na.sum++;
					listQtdElem++;
				}
			}else{
				if(is==pIdx){
					nv=new node();
					nv.next = np;
					na.next = nv;
					n1 = nv.marc = na.marc;
					nv.sum++;
					na.marc=pV;
					n1.prev = pV;
					pV.next = n1;
					listQtdElem++;
				}else{
					if(na.sum == piece){
						nv=new node();
						na.next=nv;
						nv.prev = na;
						n1 = na.marc;
						n1.next = pV;
						pV.prev = n1;
						nv.marc = pV;
						nv.sum++;
						listQtdElem++;
					}else{
						n1 = na.marc;
						n1.next = pV;
						pV.prev = n1;
						na.marc = pV;
						na.sum++;
						listQtdElem++;
					}
				}
			}
    	}
    	return(true);
    	
    }
    

    public boolean recursivePutNode2(int pIdx, node pV){
    	int i,t,is=0, is2=0, iEnd;
    	double piece;
    	node n, n1, n2, na=null, np=null, nv, nt;
    	stackNode sn;
    	
    	iEnd = listQtdElem+1;
    	if(pIdx>iEnd){
    		return(false);
    	}
    	for(i=stackPointer;i>=0;i--){
    		sn=stack[i];
    		n=sn.top;
    		piece = Math.pow(2,(double)(i+1));
    		while(n!=null){
    			is+=n.sum;
    			if(is==pIdx){
    				if(n.sum == piece){
    					np=n.next;
    					if(np.sum==piece){
    						nv=new node();
        					nv.marc = n.marc;
        					nv.sum=1;
        					this.conNode(n.marc.prev, n.marc, pV);
        					this.conNode(n, np, nv);
        					n.marc = pV;
        					listQtdElem++;
    					}else{
    						n.sum=2;
        					this.conNode(n.marc.prev, n.marc, pV);
        					n.marc=pV;
        					listQtdElem++;
    					}
    				}else{
    					n.sum=2;
    					this.conNode(n.marc.prev, n.marc, pV);
    					listQtdElem++;
					}
    				return(true);
    			}else if(is>pIdx){
    				na=n.prev;
					if(n.sum == piece){
    					nv=new node();
    					nv.marc = pV;
    					nv.sum=1;
    					if(na==null){
    						this.conNode(n.marc.prev.prev, n.marc.prev, pV);
    						n.prev = nv;
    						nv.next = n;
    						sn.top=nv;
    						listQtdElem++;
    					}else{
    						this.conNode(na.marc, na.marc.next, pV);
    						this.conNode(n.prev, n, nv);
    						listQtdElem++;
    					}
    				}else{
    					n.sum=2;
    					this.conNode(na.marc, na.marc.next, pV);
    					na.marc=pV;
    					listQtdElem++;
    				}
    				return(true);
    			}
    			na=n;
    			n=n.next;
    		}
    		
    		//Fora
    		if(is<=pIdx){
    			if(na.sum<piece){
					if(is==pIdx){
						n1 = na.marc;
						if(n1==null){
							list.next=pV;
							pV.prev = list;
							na.marc=pV;
						}else{
							n2 = n1.prev;
							if(n2!=null){
								n2.next = pV;
							}
							n1.prev = pV;
							pV.next = n1;
						}
						na.sum++;
					}else{
						n1 = na.marc;
						if(n1==null){
							list.next=pV;
							pV.prev = list;
							na.marc=pV;
						}else{
							n2 = n1.next;
							n1.next = pV;
							pV.prev = n1;
							pV.next = n2;
							na.marc=pV;
						}
						na.sum++;
						listQtdElem++;
					}
				}else{
					if(is==pIdx){
						nv=new node();
						nv.next = np;
						na.next = nv;
						n1 = nv.marc = na.marc;
						nv.sum++;
						na.marc=pV;
						n1.prev = pV;
						pV.next = n1;
						listQtdElem++;
					}else{
						if(na.sum == piece){
							nv=new node();
							na.next=nv;
							nv.prev = na;
							n1 = na.marc;
							n1.next = pV;
							pV.prev = n1;
							nv.marc = pV;
							nv.sum++;
							listQtdElem++;
						}else{
							n1 = na.marc;
							n1.next = pV;
							pV.prev = n1;
							na.marc = pV;
							na.sum++;
							listQtdElem++;
						}
						
					}
				}
    		}
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
	
}
