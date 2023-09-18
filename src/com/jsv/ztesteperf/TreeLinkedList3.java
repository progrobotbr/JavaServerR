package com.jsv.ztesteperf;

public class TreeLinkedList3 {

    public stackNode stack[];
    public int stackPointer;
    public int stackQtdElem;
    public int listQtdElem;
    public node list;
	
    public static void main(String argv[]){
    	String s=null;
    	boolean bDebug=false;
    	byte[] buffer = new byte[256];
    	byte[] buffer2 = new byte[256];
    	byte[] buffer3;
    	String s2="";
    	int i,z;
    	TreeLinkedList3 tl = new TreeLinkedList3();
    	
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
    	tl.addNew(12, 120);
    	tl.addNew(13, 130);
    	tl.addNew(14, 140);
    	tl.addNew(15, 150);
    	tl.addNew(16, 160);
    	tl.addNew(3, 441);
    	tl.addNew(3, 332);
    	tl.addNew(3, 333);
    	
    	tl.printTree();
    	
    	do{
    		try{
    			System.in.read(buffer);
    			s = new String(buffer);
    		}catch(Exception ex){}
    		if(s.toUpperCase().startsWith("I")){
    			z=0;
    			for(i=0;i<256;i++){
    				if(buffer[i]>=48 && buffer[i]<=57){
    					buffer2[z++]=buffer[i];
    				}
    			}
    			buffer3=new byte[z];
				for(i=0;i<z;i++){
					buffer3[i]=buffer2[i];
				}
    			s2 = new String(buffer3);
    			i=Integer.parseInt(s2);
    			tl.log("Número-> "+i);
    			if(bDebug==true){
    				bDebug=false;
    			}
    			tl.addNew(i,i);
    			tl.printTree();
    		}else if(s.toUpperCase().startsWith("D")){
    			z=0;
    			for(i=0;i<256;i++){
    				if(buffer[i]>=48 && buffer[i]<=57){
    					buffer2[z++]=buffer[i];
    				}
    			}
    			buffer3=new byte[z];
				for(i=0;i<z;i++){
					buffer3[i]=buffer2[i];
				}
    			s2 = new String(buffer3);
    			i=Integer.parseInt(s2);
    			tl.log("Número-> "+i);
    			if(bDebug==true){
    				bDebug=false;
    			}
    			tl.delete(i);
    			tl.printTree();
    		}else if(s.toUpperCase().startsWith("R")){
    			tl = new TreeLinkedList3();
    			for(i=1;i<=10;i++){
    				tl.addNew(i,i);
    			}
    			tl.printTree();
    		}else if(s.toUpperCase().startsWith("G")){
    			bDebug=true;
    		}else if(s.toUpperCase().startsWith("A")){
    			z=0;
    			for(i=0;i<256;i++){
    				if(buffer[i]>=48 && buffer[i]<=57){
    					buffer2[z++]=buffer[i];
    				}
    			}
    			buffer3=new byte[z];
				for(i=0;i<z;i++){
					buffer3[i]=buffer2[i];
				}
    			s2 = new String(buffer3);
    			z=Integer.parseInt(s2);
    			tl = new TreeLinkedList3();
    			for(i=1;i<=z;i++){
    				tl.addNew(i,i);
    			}
    			tl.printTree();
    			
    		}
    		
    	}
    	while(!s.toUpperCase().startsWith("QUIT"));	
    }
    
    public TreeLinkedList3(){
    	this.init();
    }
    
    public void init(){
    	list = new node();
    	list.value = -1;
    	stackNode sn = new stackNode();
    	stack=new stackNode[99];
    	stack[0] = sn;
    	sn.top = new node();
    	sn.top.value=-2;
    	stackPointer=0;
    	stackQtdElem=0;
    	listQtdElem=0;
    }
    
    public boolean delete(int pIdx){
    	boolean b=false, bMoveColumn=false;
    	int iL,iR, iF;
    	node n, np, nt, npL,npR, npC, nL1, nR1, nfL, nfR;
    	
    	//quando ficar somente 1 elemento, deverá reiniciar a tabela
    	if(pIdx==1 && listQtdElem<2){
    		if(listQtdElem==0){ return(false);};
    		listQtdElem--;
    		n = stack[0].top;
        	n.next = null;
        	list.next = null;
        	return(true);
    	}
    	
    	//procura o elemento
    	n = find(pIdx);
    	if(n==null){ return(b); } //C'est fini

    	//Verifica se a exclusão será num ponto onde 
    	//   há um nó que é marco para a árvore
    	//Deverá mover parents para nó anterior ou se for início de lista
    	//   mover parents para o nó posterior
    	if(n.parent!=null){
    		nt = n;
    		np = nt.parent;
    		nt = nt.prev;
    		//Início de lista, move para a direita
    		if(nt.value==-1){
    			
    			//   x
    			// o x 
    			nt=n.next;
    			np.marc = nt;
    			nt.parent = np;
    			n.parent=null;
    			npL=np;
    			npR=getPR(nt);
    			npR=getCR(npR);
    			nfL = nt;
    			nfR = nt.next;
    		//Regra normal, move para a esqueda	
    		}else{
    			
    			//     x
    			// ... o x x 
    			np.marc = nt;
    			nt.parent = np;
    			n.parent=null;
    			npL=getPL(nt.prev);
    			npR=getPR(nt);
    			nfL = nt.prev;
    			nfR = nt;
    		}
    		//Indica que houve movimentação. Servirá para o cálculo dos níveis superiores
        	bMoveColumn=true;
    	}else{
    		//Regra do filho sem pai
    		//  x     x
    		//  x o x x 
    		npL = getPL(n);
    		npR = getPR(n);
    		nfL = n.prev;
        	nfR = n.next;
        }
    	
    	//Ajusta nós, verifica se haverá encontro caso o nó seja excluído.
    	//Conforme regra, não poderá ter nós pais colados
    	//  exemplo:
    	//  x
    	//  xx
    	//  x0x 
        if(nfL.parent == npL && nfR.parent == npR) {
    		
        	//Mata o nó
        	n=killNode(n,true);
    		listQtdElem--;
    		
    		//Verifica colagem
    		if(npL.next==npR){
    			
    			//Conta nível, pois o menor nível será excluído
    			//Exemplo:
    			// x
    			// xo  <- morrerá
    			// xx  
    			iL=countLevel(npL);
    	    	iR=countLevel(npR);
    	    	if(iL<=iR){
    	    		nL1 = getCL(npL);
    	    		//Em alguns casos, quando se mata o nó colado, acaba ficando uma "trinca",
    	    		// e pela regra não poderá existir trincas, pois no meio da trinca vai um pai
    	    		// exemplo
    	    		//                            o 
    	    		//  x x x  -> deverá ficar  x x x 
    	    		iF=countOrfan(npL,0); //Verifica se haverá 3 orfãos
    	    		if(iF==3){
    	    			//Ao invés de matar, move para o meio considerando a direção (nesta caso esquerda)
    	    			moveNodeMiddle(npL,0);
    	    		}else{
    	    			//Mata o nó
    	    			killNode(npL,true);
    	    		}
    	    		npL=getPL(nL1);
    	    		npR=getPR(npR);
    	    	}else{
    	    		nR1 = getCR(npR);
    	    		//Em alguns casos, quando se mata o nó colado, acaba ficando uma "trinca",
    	    		// e pela regra não poderá existir trincas, pois no meio da trinca vai um pai
    	    		// exemplo
    	    		//                            o 
    	    		//  x x x  -> deverá ficar  x x x 
    	    		iF=countOrfan(npR,1); //Verifica se haverá 3 orfãos
   	    			if(iF==3){
   	    			    //Ao invés de matar, move para o meio considerando a direção (nesta caso direita)
    	    			moveNodeMiddle(npR,1);
   	    			}else{
   	    				//Mata o nó
   	    				killNode(npR,true);
   	    			}
    	    		npL=getPL(npL);
    	    		npR=getPR(nR1);
    	    	}
    		}
    		//
    		// Deverá ser feito o acerto da árvore de forma recursiva de baixo para cima,
    		//   sempre considerando a regra que entre 2 pais, deve haver ao menos um nó
    		//  intermediário, exemplo
    		//  x       x   
    		//  x   o   x  <- o é o nó intermediário, se matá-lo, deve-se rebancear a árvore
    		//  x x x x x  
    		
    		//
    		adjustTree(npL,npR);
    	}else{
    		
    		//Regra para nós sem pais. Mas é verificado se não houve 
    		//  movimentação no qual resultou em colagem de nós (bMoveColumn)
    		//
    		n=killNode(n,false);
    		listQtdElem--;
    		if(bMoveColumn==true){
    			n=n.prev;
    			//Esta lógica é para o caso
    			// x  x 
    			//x0xxx ->d2
    			// e
    			// x
    			//x0    ->d1 
    			if(n.value==-1){
    				n=n.next;
    				if(n.next!=null){
    					n=n.next;
    				}
    			}
    			
    		}
    		
    		//Ajusta os valores dos nós pais de forma ascendente
    		decreaseValueRight(n);
    	}
    	
    	return(b);
    }
    
    //Inicio Delete: Funções auxiliares
    
    public void moveNodeMiddle(node pN, int pI){ //0 esquerda, 1 direita
    	
    	node n, nM; //nM -> nodeMiddle
    	n=pN.marc;
    	if(pI==0){
    		if(n.sum==0||n.sum==-1){
    			pN.sum--;
    		}else if(n.sum>0){
    			pN.sum-=n.sum;
    		}
    		nM=n.prev;
    		n.parent = null;
    		pN.marc = nM;
    		nM.parent = pN;
    	}else{
    		if(n.sum>0){
    			//pN.sum+=n.sum;
    			pN.sum=pN.sum+(n.sum);
    			pN.next.sum=pN.next.sum-n.sum;
    		}else if(n.sum==-1 || n.sum==0){
    			//pN.sum++;
    			if(pN.next!=null){
    				pN.next.sum--;
    			}
    		}
    		nM=n.next;
    		n.parent = null;
    		pN.marc = nM;
    		nM.parent = pN;
    	}
    	
    }
    
    public void moveNodeMiddle2(node pN, int pI){ //0 esquerda, 1 direita
    	
    	node n, nM; //nM -> nodeMiddle
    	n=pN.marc;
    	if(pI==0){
    		if(n.sum==0){
    			pN.sum--;
    		}else if(n.sum>0){
    			//pN.sum-=(n.sum-1);
    			pN.sum = pN.sum - n.sum;
    			if(pN.next!=null){
    				//pN.next.sum++;
    				pN.next.sum+=(n.sum-1);
    			}
    			
    		}
    		nM=n.prev;
    		n.parent = null;
    		pN.marc = nM;
    		nM.parent = pN;
    	}else{
    		if(n.sum>0){
    			
    			if(n.next!=null){
    				pN.sum=pN.sum+(n.next.sum-1);
    				pN.next.sum=pN.next.sum-n.next.sum;
    			}
    			/*
    			if(pN.prev.value==-2){
    				pN.sum=pN.sum+n.sum+1;
        			pN.next.sum=pN.next.sum-n.sum-2;
    			}else{
    				pN.sum=pN.sum+(n.sum);
    				pN.next.sum=pN.next.sum-n.sum-1;
    			}
    			*/
    		}
    		nM=n.next;
    		n.parent = null;
    		pN.marc = nM;
    		nM.parent = pN;
    	}
    	
    }
    
    //
    // Conta, numa determinada direção a quantidade de orfãos, 
    //  exemplo:
    //  x     x
    //  x o o x <- 2  
    public int countOrfan(node pN, int pI){ //0 esquerda, 1 direita
    	int i=1;
    	node nf;
    	nf=pN.marc;
    	if(pI==0){
    		nf=nf.prev;
    		while(nf!=null && nf.parent==null && nf.value!=-1){
    			i++;
    			nf=nf.prev;
    		}
    	}else{
    		nf=nf.next;
    		while(nf.parent==null && nf.next!=null){
        		i++;
        		nf=nf.next;
        	}
    	}
    	return(i);
    }
   
    //
    // Função principal para a juste da árvore
    // A chamada dela ocorre de forma recursiva
    //
    public void adjustTree(node pnL, node pnR){
    	node nL, nL1, nR, nR1;
    	int iL, iR, iF;
    	
    	//Conta se num determinado ha um nó
    	//exemplo:
    	//x o x  <-retorna true, pois tem nó no intervalo
    	if(checkHasNodeInterval(pnL, pnR)==true){
    		decreaseValueRight(pnR.marc);
    		return;
    	}
    	
    	iL=countLevel(pnL);
    	iR=countLevel(pnR);
    	
    	if(iL==0 || iR==0){
    		return;
    	}
    	
    	if(iL<=iR && iL!=-2){
    		
    		//Pega pai do lado esquerdo
    		nL=getPL(pnL);
    		//Pega pai do lado direito
    		nR=getPR(pnR);
    		
    		//Conta orfãos
    		iF=countOrfan(pnL,0);
			if(iF==3){
				moveNodeMiddle2(pnL,0);
			}else{
				//Mata o nó do lado esquerdo
	    		killNode(pnL,true);
			}
    		
    		//Recursão, passando-se os pais
    		adjustTree(nL,nR);
    		
    	}else{
    		
    		//Pega pai do lado esquerdo
    		nL=getPL(pnL);
    		//Pega pai do lado direito
    		nR=getPR(pnR);
    		
    		//Chegou no fim da lista
    		if(nR == null){
    			if(checkHasNodeInterval(pnL, pnR)==false){
    				killNode(pnR,true);
    				stack[stackPointer]=null;
    				stackPointer--;
    				return;
    			}
    			return;
    		}
    		
    		//Conta orfãos
    		iF=countOrfan(pnR,1);
			if(iF==3){
				moveNodeMiddle2(pnR,1);
			}else{
				killNode(pnR,true);
			}
    		
			//Chama recursão
    		adjustTree(nL,nR);
    	}
    }
    
    
    //
    //Mata o nó
    //
    public node killNode(node pN, boolean pCalc){
    	node n, n0;
    	n=pN;
    	n0 = n.prev;
    	if(n.parent!=null){
    		//Mata o nó
    		if(n0==null){
    			n0=n;
    		}
    		//Ajuta pais e filhos
    		n0.parent = n.parent;
    		n.parent.marc = n0;
    		n0.marc = n.marc;
    		if(n0.marc!=null){
    			n0.marc.parent = n0;
    			sumValueRight(n);
    		}
    	}else{
    		if(n.marc!=null){
    			n.marc.parent = null;
    		}
    		if(pCalc==true){
    			sumValueRight(n);
    		}
    	}
    	if(n.next!=null){
    		n.next.prev = n0;
    		n0.next = n.next;
    	}else{
    		n0.next=null;
    		n=null;
    		return(n0);
    	}
    	
    	return(n0.next);
    }
    
    //
    //Pega irmão do nó do lado direito
    //
    public node getCL(node pN){   //getCousingLeft
    	node n=null;
    	if(pN.value == -2){
    		return(pN);
    	}
    	n = pN.prev;
    	return(n);
    }
    
    //
    //Pega irmão do nó do lado esquerdo
    //
    public node getCR(node pN){   //getCousingLeft
    	node n=null;
    	if(pN.next == null){
    		return(pN);
    	}
    	n = pN.next;
    	return(n);
    }
    
    //
    //Sobre na árvore, decrementando 1 de cada nó pai
    //
    public void decreaseValueRight(node pN){
    	node n;
    	n=pN;
    	n=getPR(n);
    	while(n!=null){
    		n.sum--;
    		n=getPR(n);
    	}	
    }
    
    //
    // Soma valores do nó informado, no irmão da direita
    //
    public void sumValueRight(node pN){
    	node n;
    	n=pN;
    	if(n.next != null){
    		n.next.sum+=n.sum;
    		n.next.sum--;
    	}
    }
    
    
    //
    //Conta níveis de pais ou coluna
    //
    public int countLevel(node n){
    	int i=0;
    	if(n.value==-2){
    		return(-2);
    	}
    	while(n!=null){
    		n=n.parent;
    		i++;
    	}
    	return(i);
    }
    
    
    //
    //Pega pai de um nó pelo lado direito
    //
    public node getPR(node pN){ //getParentRight
    	node n=null;
    	n=pN;
    	while(n!=null){
    		if(n.parent !=null){
    			n=n.parent;
    			break;
    		}
    		n=n.next;
    	}
    	return(n);
    }
    
    //
    //Pega pai de um nó pelo lado esquerdo
    //
    public node getPL(node pN){ //getParenteLeft
    	node n=null;
    	n=pN;
    	if(n.value==-1){
			n=stack[0].top;
			return(n);
		}
    	if(pN.value==-2){
    		return(pN.parent);
    	}
    	while(n!=null){
    		if(n.parent !=null){
    			n=n.parent;
    			break;
    		}
    		n=n.prev;
    		if(n.value==-1){
    			n=stack[0].top;
    			break;
    		}
    		if(n.value==-2){ //Chegou no início top
    			n=n.parent;
    			break;
    		}
    	}
    	return(n);
    }
    
    //
    //Verifica se tem um nó num intervalo
    //
    public boolean checkHasNodeInterval(node n1, node n2){
    	boolean b=false;
    	node nc1, nc2;
    	
    	nc1=n1.marc;
    	nc2=n2.marc;
    	if(nc1.next != nc2){
    		b=true;
    	}
    	
    	return(b);
    }
    ////////////////////////////////
    //Fim Delete: Funções auxiliares
    ////////////////////////////////
    
    
    ////////////////////////////////
    //Início - Procura
    ////////////////////////////////
    public node find(int pIdx){
    	int i, t, is, istack;
    	node n=null,n1=null, ns=null;
    	stackNode sn=null;
    	
    	if(pIdx>listQtdElem || pIdx < 1){
    		return(null);
    	}else if(pIdx < 3){
    		if(pIdx==1){
    			n=list.next; //1
    		}else{
    			n=list.next; //2
    			n=n.next;
    		}
    		return(n);
    	}
    	is=0;
    	istack=stackPointer;
    	sn=stack[istack];
    	n=sn.top;
    	while(n!=null){
    		
    		if(is<pIdx){
    			n1=n;
        		n=n.next;
        		is+=n.sum;
    		}else if(is == pIdx){
    			if(n.marc==null){
    				break;
    			}
    			n=n.marc;
    		}else if(is>pIdx){
    			is-=n.sum;
    			if(is==0){
    				//evita o estouro
    				if(istack!=0){
    					istack--;
    				}else{
    					break;
    				}
    				sn=stack[istack];
    		    	n=sn.top;
    		    	continue;
    			}
    			ns = n1.marc;
    			
    			if(ns.marc==null){
    				n=ns;
    				for(i=is; i<pIdx; i++){
    					n=n.next;
    				}
    				break;
    			}else{
    				n=n1.marc;
    			}
    		}
    	}
    			
    	return(n);
    }
    
    ////////////////////////////////
    //Fim Procura
    ////////////////////////////////
    
    ////////////////////////////////
    //Início Alteração
    ////////////////////////////////
    public boolean update(int pInd, int pValue){
    	boolean b=false;
    	node n;
    	n=find(pInd);
    	if(n!=null){
    		n.value = pValue;
    		b=true;
    	}
    	return(b);
    }
    ////////////////////////////////
    //Fim Alteração
    ////////////////////////////////
    
    ////////////////////////////////
    //Início Inserção
    ////////////////////////////////
    public boolean addNew(int pIdx, int pvalue){
    	boolean bput = false;
    	int i, i1, i2, i3, is=0, is0, is1, iEnd;
    	node n, n0, n1, n2, nv, pV;
    	stackNode sn=null;
    	node mstack[][] = new node[100][3];
    	int mpstack=-1;
    	
    	iEnd = listQtdElem+1;
    	if(pIdx>iEnd){
    		return(false);
    	}
    	
    	pV = new node(pvalue);
    	
    	is0 = 0;
    	is1 = 0;
    	n0=null;
    	n1=null;
    	
    	for(i=stackPointer;i>=0;i--){
    		if(n0==null||is1==0){
    			sn=stack[i];
    			n0=sn.top;
    			n1=n0.next;
    		}else{
    			n0=n0.marc;
    			n1=n0.next;
    		}
    		n2=new node();
    		while(n1!=null){
    			
    			is0 = is1;
    			is1 += n1.sum;
    			if(is1 >= pIdx){
    				break;
    			}
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
		if(n2.sum==0 && n1==null){
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
			else { i2 = i2 +n1.sum;}
			n=n1.marc;
			for(i=i2;i>pIdx;i--){
				n=n.prev;
			}
			if(pIdx>i2){
				//i2=is1+n1.sum;
				//if(pIdx<=i2){
				//	n=n.prev;
				//}
				n.next = pV;
				pV.prev = n;
				n1.marc.parent=null;
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
    		node snOld;
    		snOld=stack[stackPointer].top;
    		stackPointer++;
    		sn = new stackNode();
    		stack[stackPointer]=sn;
    		sn.top = new node();
    		snOld.parent = sn.top;
    		sn.top.marc = snOld;
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
    
    //
    //Funções de apoio
    //
    
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
    
    ////////////////////////////////
    //Fim Inserção
    ////////////////////////////////
    
    ////////////////////////////////
    //Funçõies de Impressão
    ////////////////////////////////
    
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
		int i,l,t;
		node n;
		log("Tree");
		for(i=stackPointer;i>=0;i--){
			n=stack[i].top;
			n=n.next;
			while(n!=null){
				t=n.sum;
				lg2("_");
				for(l=0;l<t-1;l++){
					lg2("__");
				}
				lg2("x");
				n=n.next;
			}
			lge();
		}
		n=list;
		n=n.next;
		while(n!=null){
			lg2(" x");
			n=n.next;
		}
		
		lge();
	}
	
	////////////////////////////////
    // Classe Nó
    ////////////////////////////////
    
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

    ////////////////////////////////
	//Classe stackNode
    ////////////////////////////////
	public class stackNode{
		public int level=0;
		public node top=null;
	}
	
}

