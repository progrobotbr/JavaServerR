package com.jsv.lang.vm;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.jsv.nativelib.base.INative;

import com.jsv.server.trz.base.Transaction;
import com.jsv.utils.Utils;

import com.jsv.lang.program.line;
import com.jsv.lang.program.program;
import com.jsv.lang.vm.dim.dimfactory;
import com.jsv.lang.vm.type.decltype;
import com.jsv.lang.vm.type.decltypeelement;
import com.jsv.lang.vm.type.parameterPair;
import com.jsv.lang.vm.type.parameterPairs;
import com.jsv.data.Variant;
import com.jsv.db.base.sqlbase;
import com.jsv.editor.BParser;

public class vm {
	
	/*
	** frame: referencia para uma estancia de uma função. Conterá todas
	**        as variáveis locais da função, sendo que as variáveis do 
	**        frame[0] são locais para o frame[0] e globais para os demais 
	**        frames (frame[n])
	** 
	** fr   : conjunto de frames. Quando o frame zero é descarregado,
	**        significa que o processamento chegou ao fim. Quando se chama 
	**        a primeira sub de processamento, exemplo MAIN, pode-se informar
	**        um parâmetro loadInitialSub(,[true|false]) no qual indicará que o 
	**        descarregamento do frame[0] será fim de processamento, quando 
	**        informado como falso ocorre fim de processamento no descarregamento
	**        do frame 1, deixando-se o frame[0] carregado e podendo carregar
	**        novas subs a partir daquele ponto
	**
	** clPrg : representa um programa carregado. Conterá sempre o programa 
	**         atual para processamento. CallSub e EndSub alteram clPrg com base
	**         nos clRef indicados nos frames ou na coleção de programas (cpPrgs)
	**
	** clPrgs: coleção de programas carregados. O primeiro programa carregado
	**         será armazenado com a chave "__base__" e será ele responsável 
	**         pelo carregamento do frame[0] com alguma sub, como a MAIN, ou 
	**         outra sub qualquer. O frame[0] conterá todas as declarações de tipos 
	**         e variáveis globais no qual outras subs poderão acessar, mesmo de 
	**         programas diferentes
	**
	** Função MAIN: primeira função a ser executada num programa. Esta função é formata,
	**              ou criada, pela formatação do programa e corresponderá às instruções
	**              "soltas" num programa. Quando o programa é carregado e iniciado para 
	**              execução, está é a primeira função a ser executada
	**
	*/
	private frame fr[];                   //Array de frames. O trabalho sobre este array é como uma pilha: último que entra, primeiro a sair.
	private int frSize;                   //Indica o tamanho "lógico" do array de frames. Verificar a utilidade desta variável
	private int frAtual;                  //Indica o frame de processamento atual
	private boolean bJump=false;          //Indica que houve jump e não será necessário fazer o incremento de instrução
	private boolean bMantainFrame0=false; //Indica que o frame 0 não será descarregado (true 0 não descarregado, false 0 será descarregado)
	public static boolean VM_MANTAIN_FRAME0 = true;
	public static boolean VM_NOT_MANTAIN_FRAME0 = false;
	private boolean bEndOfProcess=true;   //Indica que o programa já foi processado
	private static final int GLOBAL = 0;  //Indica qual o número do frame global, que é 0
	private static final int HEAP = 100;  //Número máximo de frames que podem ser utilizados.
	public static final boolean STARTPROCESSING = false; //Indica que o processamento iniciou
	public static final boolean STOPPROCESSING = true;   //Indica que o processamento finalizou
	public static final int PRG_END_WITH_SUCCESS = 4;    //Indica que o processamento do programa foi finalizado com sucesso
	public static final int PRG_LINE_SUCCESS = 0;        //Indica que o processamento da linha do programa foi executado com sucesso
	public static final int PRG_PROCESS_ERROR = -1;      //Indica que o processamento do programa foi finalizado (interrompido) com erro
    public static final int PRG_DEBUG_SUB_END_SUCCESS = 5;
    public static final int TRZ_DEBUG_STEP_START = 6;
	public static final int TRZ_DEBUG_STEP_SUCCESS = 7;
	public static final int TRZ_STEP_START = 9;
	public static final int TRZ_DEBUG_ON = 1;
	public static final int TRZ_DEBUG_OFF = 0;
	public static final int TRZ_FIRST_CALL = 10;
	public static final int TRZ_PLUS_CALL = 20;
	public static final int TRZ_PROCESS_START = 8;
	
	private static final String PRG_BASE = "__base__";   //Nome do programa base que conterá a função MAIN (primeira de execução)
	
	private sqlbase mSqlDb;
	//private int rcsql;
	/*
	**prog é uma referencia para um programa, possui apontamento para as funções
	**  assim, podemos ter mais de um programa carregado e preparado para execução
	*/
	private HashMap<String,classProgram> clprgs = new HashMap<String,classProgram>();
	//Ponteiro de programa atual
	public classProgram clprg; //=new classProgram(); //__base__
	//Variáveis temporárias para conter os parâmetros de entrada e saída de uma sub
	// no momento de compilação
	private decltype declIn=null, declOut = null;
	//Variáveis temporárias para momento de passagem de parâmetros
	private parameterPairs passIn=null, passOut=null;
	/*
	** gDim é uma classe de apoio para criação de variáveis
	*/
	private dimfactory gDim = new dimfactory(); //Gerador de variáveis para o frame
	
	public boolean screencommand=false; //Indica que houve chamada a goto screen
	//Parser - referencia cruzada
	private BParser runner; //Parse e executor de linhas
	
	//Variáveis de erros
	public int rc=0;
	public String errmsg="";
	public String errinstr="";
	
	public void setParser(BParser prunner){
		runner = prunner;
	}
	
	//Inícialização
	public vm(){
		this.init();
	}
	public void init(){
		fr = new frame[HEAP];
	    frSize = 0;
	    frAtual = -1;
	    clprg = null;
	}
	
	//Operações com Frame
	public void pushFrame(function f, parameterPairs in, parameterPairs out){
		frAtual++;
		fr[frAtual] = new frame(f, in, out);
	}
	
	public void popFrame(){
		//Manter o frame 0
		if(bMantainFrame0==true && frAtual == 0){
			bEndOfProcess=vm.STOPPROCESSING;
			return;
		}
		fr[frAtual] = null;
		frAtual--;
		if(frAtual==-1){
			bEndOfProcess=vm.STOPPROCESSING;
		}else{
			this.clprg=fr[frAtual].clsref;
			//bJump=true;  //Para evitar saltos no final do endsub
		}
		if(bMantainFrame0==true && frAtual == 0){
			bEndOfProcess=vm.STOPPROCESSING;
		}
	}
	
	public HashMap<String,Variant> getMemGlobal(){
		HashMap<String,Variant> hp;
		hp=fr[GLOBAL].mem;
		return(hp);
	}
	
	//Cálculos Básicos
	public Variant calc(char cSinal, Variant v1, Variant v2){
		float f=0;
		Variant v;
		switch(cSinal){
			case '+': { f = v1.getNum() + v2.getNum(); break; }
			case '-': { f = v1.getNum() - v2.getNum(); break; }
			case '*': { f = v1.getNum() * v2.getNum(); break; }
			case '/': { f = v1.getNum() / v2.getNum(); break; }
		}
		v = new Variant(f);
		return(v);
	}
	
	//Operações Booleanas
	public Variant bool(char op,Variant v1, Variant v2){
		// 1 AND, 2 OR, 3 =, 4 >, 5 <, 6 >=, 7<=, 8 <>
		boolean b=false;
		Variant v=null;
		switch(op){
		case 'A' : 
			switch(v1.type){
			case Variant.BOOL:
				if(v2.type==Variant.BOOL){ b = v1.getBoolean() && v2.getBoolean(); v = new Variant(b);}break;
			}
			break;
		case 'O' : 
			switch(v1.type){
			case Variant.BOOL:
				if(v2.type==Variant.BOOL){ b = v1.getBoolean() || v2.getBoolean(); v = new Variant(b);}break;
			}
			break;
		case '=' : 
			switch(v1.type){
			case Variant.BOOL:
				if(v2.type==Variant.BOOL){ b = v1.getBoolean() == v2.getBoolean(); v = new Variant(b);}break;
			case Variant.INT:
				switch(v2.type){
				case Variant.INT: b = v1.getInt() == v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getInt() == v2.getFloat(); v = new Variant(b); break;
				}break;
			case Variant.FLOAT:
				switch(v2.type){
				case Variant.INT: b = v1.getFloat() == v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getFloat() == v2.getFloat();  v = new Variant(b);break;
				}break;
			case Variant.STRING:
				if(v2.type==Variant.STRING){ b = v1.getString().equals(v2.getString()); v = new Variant(b);}break;
			}
			break;
			
		case '>':
			switch(v1.type){ //BOOL não entra
			case Variant.INT:
				switch(v2.type){
				case Variant.INT: b = v1.getInt() > v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getInt() > v2.getFloat(); v = new Variant(b); break;
				}break;
			case Variant.FLOAT:
				switch(v2.type){
				case Variant.INT: b = v1.getFloat() > v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getFloat() > v2.getFloat();  v = new Variant(b);break;
				}break;
			}
			break;
			
		case '<':
			switch(v1.type){ //BOOL não entra
			case Variant.INT:
				switch(v2.type){
				case Variant.INT: b = v1.getInt() < v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getInt() < v2.getFloat(); v = new Variant(b); break;
				}break;
			case Variant.FLOAT:
				switch(v2.type){
				case Variant.INT: b = v1.getFloat() < v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getFloat() < v2.getFloat();  v = new Variant(b);break;
				}break;
			}
			break;
			
		case 'M': // >= Maior igual
			switch(v1.type){ //BOOL não entra
			case Variant.INT:
				switch(v2.type){
				case Variant.INT: b = v1.getInt() >= v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getInt() >= v2.getFloat(); v = new Variant(b); break;
				}break;
			case Variant.FLOAT:
				switch(v2.type){
				case Variant.INT: b = v1.getFloat() >= v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getFloat() >= v2.getFloat();  v = new Variant(b);break;
				}break;
			}
			break;
			
		case 'N': // <= meNor igual
			switch(v1.type){ //BOOL não entra
			case Variant.INT:
				switch(v2.type){
				case Variant.INT: b = v1.getInt() <= v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getInt() <= v2.getFloat(); v = new Variant(b); break;
				}break;
			case Variant.FLOAT:
				switch(v2.type){
				case Variant.INT: b = v1.getFloat() <= v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getFloat() <= v2.getFloat();  v = new Variant(b);break;
				}break;
			}
			break;
			
		case 'D' : // <> Diferente
			switch(v1.type){
			case Variant.BOOL:
				if(v2.type==Variant.BOOL){ b = v1.getBoolean() != v2.getBoolean(); v = new Variant(b);}break;
			case Variant.INT:
				switch(v2.type){
				case Variant.INT: b = v1.getInt() != v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getInt() != v2.getFloat(); v = new Variant(b); break;
				}break;
			case Variant.FLOAT:
				switch(v2.type){
				case Variant.INT: b = v1.getFloat() != v2.getInt();  v = new Variant(b);break;
				case Variant.FLOAT: b = v1.getFloat() != v2.getFloat();  v = new Variant(b);break;
				}break;
			case Variant.STRING:
				if(v2.type==Variant.STRING){ b = v1.getString().equals(v2.getString()); v = new Variant(b);}break;
			}
			break;

		}
		return(v);
	}
	
	// Criar Variáveis
	// Local
	public void createLocalVariable(String pName, Variant v){
		frame lfr = fr[frAtual];
		lfr.storeVar(pName, v);
	}
	// Global
	public void createGlobalVariable(String pName, Variant v){
		frame lfr = fr[GLOBAL];
		lfr.storeVar(pName, v);
	}
	
	/*
	**Criar variável no padrão:
	** DIM IDADE AS I
	** DIM NOTA AS F
	** DIM NOME AS C[30]
	** DIM PEDIDO_HEADER AS NOME_ESTRUTURA
	** DIM PEDIDOS[] AS NOME_ESTRUTURA
	** e coloca a variável no respectivo frame
	*/
	public void createVar(String pName, String pNameType){
		gDim.setFrames(fr[GLOBAL],fr[frAtual]);
		gDim.createVar(pName, pNameType);
	}
	
	public void createVar(decltypeelement pel){
		gDim.setFrames(fr[GLOBAL],fr[frAtual]);
		gDim.createVar(pel.sNome, pel.sTpOriginal);
	}
	
	//Armazena o tipo no frame atual
	public void putType(String pName, decltype pTyp){
		fr[frAtual].putType(pName, pTyp);
	}
		
	//Move Variáveis entre áreas considerando o tipo.
	//Move ll,gl,lg,gg
	//g=global e l=local
	public void move(String s1,String s2){
		Variant v1;
		Variant v2;
		
		v1 = load(s1);
		v2 = load(s2);
		v1.setNum(v2);
	}
	
	//Move uma Variant para uma área local ou global 
	public void move2(String s, Variant v1){
		Variant v2;
		v2=load(s);
		v2.setNum(v1);
	}
	
	public void move2Field(String s, Variant v1){
		Variant v2; //,v3;
		//v3=load("TB");
		v2=loadField(s);
		v2.setNum(v1);
	}
	
	public void move2NameOrField(String s, Variant v1, int i){
		if(i!=Variant.FIELD){
			move2(s,v1);
		}else{
			move2Field(s,v1);
		}
	}
	
	//Utilitário para carregar a variável da área global ou do ponto atual
	public Variant load(String pName){
		Variant v;
		frame lfr = fr[frAtual];
		v=lfr.loadVar(pName);
		if(v==null){
			lfr = fr[GLOBAL];
			v=lfr.loadVar(pName);
		}
		return(v);
	}
	
	//Utilitário para carregar um field da área global ou do ponto atual
	public Variant loadField(String pName){
		Variant v;
		frame lfr = fr[frAtual];
		v=lfr.loadVarField(pName);
		if(v==null){
			lfr = fr[GLOBAL];
			v=lfr.loadVarField(pName);
		}
		return(v);
	}
	
	public Variant loadNameOrField(String pName, int i){
		Variant v;
		if(i!=Variant.FIELD){
			v=load(pName);
		}else{
			v=loadField(pName);
		}	
		return(v);
	}
	
	//Ajusta lista de sub's - coloca os subs numa lista com a tipagem pronta
	//Funções utilitárias para geraçao da estrutura dos frames de função
	public int createSubDecl(program pprogram){
		int i=0;
		String sn;
		classProgram cp = new classProgram();
		classProgram cpBk;
		HashMap <String,function>fs;
		function f;
		//this.clprg.lines = pprogram.lines;
		//this.clprg.fx    = pprogram.subAddress;
		cp.lines = pprogram.lines;
		cp.fx = pprogram.subAddress;	
		//this.clprg = cp;
		//fs = this.clprg.fx;
		fs = cp.fx;
		if(this.clprgs.isEmpty()){
			sn = vm.PRG_BASE;
			cp.isBase = true;
		}else{
			sn = pprogram.Name;
			cp.isBase = false;
		}
		cp.Name  = pprogram.Name;
		
		this.clprgs.put(sn, cp);
		//pparser.setVM(this);
		Iterator <function> it = fs.values().iterator();
		cpBk=this.clprg;
		this.clprg = cp;
		while(it.hasNext()){
			f=it.next();
			i=runner.yyparse(f.lineData);
		}
		if(cpBk!=null){ //Quando for null, não há programa carregado
			this.clprg=cpBk;
		}
		return(i);
	}
	
	public void putSubDeclIn(decltype pdeclt){
		this.declIn = pdeclt;
	}
	
	public void putSubDeclOut(decltype pdeclt){
		this.declOut = pdeclt;
	}
	
	public void createMetaSub(String pName){
		function f;
		classProgram cp;
		HashMap<String,function> fs;
		fs = this.clprg.fx;
		f  = fs.get(pName);
		f.SubName = pName;
		f.PrgName = this.clprg.Name;
		f.Inpar   = this.declIn;
		f.Outpar  = this.declOut;
		f.ClsProgram = this.clprg;
		if(this.clprg.isBase!=true){
			cp = this.clprgs.get(vm.PRG_BASE);
			if(!cp.fx.containsKey(pName)){
				cp.fx.put(pName,f);
			}
		}
	}
	
	public void setNullSubDeclIn(){
		this.declIn = null;
	}
	public void setNullSubDeclOut(){
		this.declOut = null;
	}
	
	/*
	 * Funções utilitárias para montagem do CALL de uma função
	 */
	/*
	 * Regra do Call: pegar a declaração de tipos; 
	 *   criar as variáveis em modo local (frame atual);
	 *   passar os valores conforme a chamada do call: busca do método local ou do método global;
	 *   no call, algumas variáveis não são passadas: tratar;
	 *   retornar valores para OUT, para as variáveis informadas;
	 *   todos os parâmetros são obrigatórios
	 *   dar um jeito no parâmetro de entrada IN, só de deve ser passado por valor (criar o método clone)
	 *       o método clone pode gerar um overhead..., but será necessário, ver se há algum método clone 
	 */
	
	//Pega uma referência à variável da área global ou local
	public Variant getVariantReference(String pName){
		Variant v;
		frame local = fr[frAtual];
		frame global = fr[GLOBAL];
		if(pName.contains("-")){
			v=local.loadVarField(pName);
			if(v==null){
				v=global.loadVarField(pName);
			}
		}else{
			v=local.loadVar(pName);
			if(v==null){
				v=global.loadVar(pName);
			}
		}
		return(v);
	}
	
	/*
	 * Realiza a passagem de parâmetros
	 *   e cria as variáveis de parâmetros 
	 */
	
	public void passParametersIn(){
		String s;
		Variant v;
		frame lfr;
		parameterPairs lassIn = this.passIn;
		parameterPair  lpar;
		decltype ldec;
		decltypeelement ldecle;
		
		lfr = fr[frAtual];
		ldec = lfr.f.Inpar; //Tipagem dos parâmetros de entrada
		
		if(ldec==null){ return; }
		
		gDim.setFrames(fr[GLOBAL],lfr); //Faz o setup dos frames
		
		ldec.setIndex(0);
		ldecle=ldec.getElem();
		while(ldecle != null){
			//Tratamento para tipo tabela
			s = ldecle.sNome;
			s = s.replace("[","");
			s = s.replace("]","");
			//Fim tratamento tipo tabela
			lpar=lassIn.getElem(s);
			if(lpar==null){
			   gDim.createVar(ldecle.sNome, ldecle.sTpOriginal);
			}else{
			  //Colocar lógica para não clonar tabela	
			   v = lpar.vpar2.clone();
			   lfr.storeVar(lpar.spar1, v);
			}
			ldecle=ldec.getElem();
		}
	}
	//Array contendo => "nome de saída", Variant (referência);
	public void passParametersOut(){
		Variant v;
		frame lfr;
		parameterPairs lassOut = this.passOut;
		parameterPair  lpar;
		decltype ldec;
		decltypeelement ldecle;
		
		lfr = fr[frAtual];
		ldec = lfr.f.Outpar; //Tipagem dos parâmetros de entrada
		
		if(ldec==null){ return; }
		
		gDim.setFrames(fr[GLOBAL],lfr); //Faz o setup dos frames
		
		ldec.setIndex(0);
		ldecle=ldec.getElem();
		while(ldecle != null){
			lpar=lassOut.getElem(ldecle.sNome);
			if(lpar!=null){
			   v = lpar.vpar2;
			   lfr.storeVar(lpar.spar1, v);
			}
			ldecle=ldec.getElem();
		}
	}
	
	public void setParameterIn(parameterPairs p){
		this.passIn = p;
	}
	
	public void setNullParameterIn(){
		this.passIn=null;
	}
	
	public void setParameterOut(parameterPairs p){
		this.passOut = p;
	}
	
	public void setNullParameterOut(){
		this.passOut=null;
	}
	
	public void callSub(String sFuncName){
		/*
		 * sFuncName pode ser => "prg->sub"
		 *                 ou => sub
		 *  quando for sub, pega do programa base                
		 */
		String s[] = sFuncName.split("->");
		function f;
		
		if(s.length>1){
			this.clprg = clprgs.get(s[0]);
			f = this.clprg.fx.get(s[1]);
		}else{
			this.clprg = clprgs.get(vm.PRG_BASE);
			f = this.clprg.fx.get(sFuncName);
		}
		
		f = f.clone();
		this.clprg = f.ClsProgram;
		this.pushFrame(f, this.passIn, this.passOut);
		this.passParametersIn();
		this.passParametersOut();
		//fr[frAtual].programCount++;
	}
	
	public void endSub(){
		this.passParametersOut();
		this.popFrame();
		
		//
		try{
		Variant v = this.load("TB");
		log2(v.tb.toString());
		}catch(Exception e){}
	}
	
	/**
	 *  Controla a execução
	 */
	public int executeLine(){
		int i=vm.PRG_PROCESS_ERROR;
		String s;
		if(bEndOfProcess == vm.STOPPROCESSING){
			return(vm.PRG_END_WITH_SUCCESS);
		}
		s=this.getLine();
		//System.out.print(s);
		try{
		i = runner.yyparse(s);
		}catch(Exception ex){
			i=vm.PRG_PROCESS_ERROR;
			rc=0;
			errinstr = s;
			return(i);
		}
		if(i==0 && bEndOfProcess != vm.STOPPROCESSING && bJump == false){
			fr[frAtual].programCount++;
		} else { this.bJump = false; }
		return(i);
	}
	
	public int loadInitialSub(String s, boolean b){
		int i=vm.PRG_PROCESS_ERROR;
		bMantainFrame0 = b;
		bEndOfProcess = vm.STARTPROCESSING;
		s = "CALL " + s +".\n";
		this.log(s);
		try{
		  i = runner.yyparse(s);
		}catch(Exception ex){ 
		  i=vm.PRG_PROCESS_ERROR;
		  rc=vm.PRG_PROCESS_ERROR;
		  errinstr = s;
		  return(i);
		}
		if(i==0){ fr[frAtual].programCount++; }
		return(i);
	}
	
	private String getLine(){
		int i;
		String s;
		i=fr[frAtual].programCount;
		s=clprg.lines[i].lineData;
		log(clprg.Name+":"+i+":"+s);
		return(s);
	}
	
	public line getLineRead(){
		int i;
		line l,l1;
		String s;
		i=fr[frAtual].programCount;
		l=clprg.lines[i];
		l1 = new line();
		l1.fisicalLine=l.fisicalLine;
		l1.lineData=clprg.Name + ":" + clprg.lines[i].lineData; //vou passar o nome do programa no linedata
		return(l1);
	}
	/*** Comandos básicos ***/
	public void gotoCmd(){
		int i = fr[frAtual].programCount;
		fr[frAtual].programCount = clprg.lines[i].gotoLine;
		this.bJump = true;
	}
	
	//Pula uma linha a mais.
	public void gotoCmd2(){
		int i = fr[frAtual].programCount;
		fr[frAtual].programCount = clprg.lines[i].gotoLine+1;
		this.bJump = true;
	}
	
	public void ifCmd(Variant v){
		if(v.getBoolean()==false){
			this.gotoCmd();
		}
	}
	
	public void whileCmd(Variant v){
		if(v.getBoolean()==false){
			this.gotoCmd2();
		}
	}
	
	/*** Comandos para manipulação de tabelas ***/
	public void tableFirst(String pNameTab){
		Variant vtb;
		table tb;
		vtb = load(pNameTab);
		tb=vtb.getTable();
		tb.setIndex(0);
		
	}
	
	public void forEach(decltypeelement pNameStru, String pNameTab){
		Variant vst, vtb;
		structure st;
		table tb;
		vst = load(pNameStru.sTpOriginal);
		vtb = load(pNameTab);
		st=vst.getStucture();
		tb=vtb.getTable();
		st = tb.foreach();
		if(st==null){
			this.gotoCmd2();
		}else{
			vst.setStru(st);
		}
	}

	public void appendTable(decltypeelement pNameStru, String pNameTab){
		Variant vst, vtb;
		structure st;
		table tb;
		this.setRC(vm.PRG_PROCESS_ERROR);
		vst = load(pNameStru.sTpOriginal);
		vtb = load(pNameTab);
		st=vst.getStucture();
		tb=vtb.getTable();
		if(tb!=null){
			tb.append(st,this);
			this.setRC(vm.PRG_LINE_SUCCESS);
		}
	}
	
	public void readTable(decltypeelement pNameStru, String pNameTab, parameterPairs pKeys){
		Variant vst, vtb;
		table tb;
		this.setRC(vm.PRG_PROCESS_ERROR);
		vtb = load(pNameTab);
		tb=vtb.getTable();
		vst = tb.read(pKeys,this);
		if(vst!=null){
			this.move2(pNameStru.sTpOriginal, vst);
			this.setRC(vm.PRG_LINE_SUCCESS);
		}
		
	}
	
	public void deleteTable(String pNameTab, parameterPairs pKeys){
		Variant vtb;
		table tb;
		this.setRC(vm.PRG_PROCESS_ERROR);
		vtb = load(pNameTab);
		tb=vtb.getTable();
		if(tb.delete(pKeys,this)==true){
			this.setRC(vm.PRG_PROCESS_ERROR);
		}
	}
	
	public void modifyTable(String pNameTab,decltypeelement pNameStru, parameterPairs pKeys){
		Variant vst, vtb, v1, v2;
		structure st=null, st2=null;
		table tb;
		Vector<String> nameCol1,nameCol2;
		
		this.setRC(vm.PRG_PROCESS_ERROR);
		
		vtb = load(pNameTab);
		tb=vtb.getTable();
		st = tb.read2(pKeys,this);
		if(st==null){
			return;
		}
		vst=load(pNameStru.sTpOriginal);
		if(vst==null){
			return;
		}
		st2 = vst.st;
		if(st.getSize() != st2.getSize()){
			return;
		}
		
		nameCol1 = st2.getNameCols();
		nameCol2 = st.getNameCols();
		for(int i=0; i<st.getSize();i++){
		  if(! nameCol1.get(i).equals(nameCol2.get(i))){
			  return;
		  }
		}
		for(int i=0; i<st.getSize();i++){
			v1=st.getWI(i);
			v2=st2.getWI(i);
			v1.setNum(v2);
		}
		
		this.setRC(vm.PRG_LINE_SUCCESS);
		
	}
	
	//Variáveis de sistema
	//RC
	public void setRC(int i){
		Variant v = new Variant(i);
		if(fr[GLOBAL]!=null){
			fr[GLOBAL].storeVar("RC", v);
		}
	}
	
	//Tratamento variável RCSQL
	/*
	public void clearRcSql(){
		rcsql = 0;
	}
	public void setRcSql(int i){
		if(rcsql == 0) {
			rcsql = i;
		}
	}
	public int getRcSql(){
		return(rcsql);
	}
	*/
	//Fim 
	//Screen
	public void gotoScreen(int pi){
		int i,t;
		Variant v;
		String s = "0000" + pi;
		t = s.length();
		i = t-4;
		s = s.substring(i, t);
		v=new Variant(s);
		fr[GLOBAL].storeVar("SCREEN", v);
		screencommand = true;
	}
	
	//Operação de carregar programas
	public int loadProgram(BParser runner, String s){
		int i;
		program p = new program();
		p.Name = s;
		this.log("EE"+p.Name);
		
		String s1[] = new String[1];
		p.init(s1);
		//p.loadPogram(s);
		p.loadPogramDB(s);
		runner.setVM(this);
		this.setParser(runner);
		i = this.createSubDecl(p);
		return(i);
	}
	
	public int loadProg(String s){
		int i=1;
		this.setRC(vm.PRG_PROCESS_ERROR);
		s=s.toUpperCase();
		this.log("vm.loadProg"+s);
		BParser lrunner = new BParser();
		i=this.loadProgram(lrunner, s);
		this.setRC(i);
		return(i);
	}
	
	//Funções SQL
	public void sqlSelect(String ssql, String tbname){
		//A estrutura de entrada deve ser igual a de saida
		int i,t,y,z;
		table tb;
		structure st,ln;
		Variant v;
		ResultSet rs;
		//Inicia com erro
		this.setRC(vm.PRG_PROCESS_ERROR);
		v=this.load(tbname);
		if(v==null) {
			//Tratarerro
		    return;
		}
		z=0;
		//sqlbase sql = new sqlbase();
		sqlbase sql = mSqlDb; 
		tb = v.tb;
		tb.clearTable();
		ssql = ssql.replaceAll("~", ".");
		rs=sql.select(ssql);
		if(sql.rc==0){
			tb.setIndex(0);
			st=tb.getHeader();
			t=st.getSize();
			try{
				while(rs.next()){
					z=1;
					ln=st.clone();
					for(i=0;i<t;i++){
						v=ln.getWI(i);
						switch(v.type){
						case Variant.BOOL:
							break;
						case Variant.INT:
							v.i=rs.getInt(i+1);
							break;
						case Variant.FLOAT:
							v.f=rs.getFloat(i+1);
							break;
						case Variant.STRING:
							v.s=rs.getString(i+1);
							break;
						}
						z++;
					}
					tb.append(ln, this);
				}
				if(z!=0) {
					this.setRC(vm.PRG_LINE_SUCCESS); 
				}
			}catch(Exception ex){ 
				this.log(ex.toString());
			}
		}
		sql.dispose();
	}

	public void sqlInsert(String ssql){
		int i;
		this.setRC(vm.PRG_PROCESS_ERROR);
		//sqlbase sql = new sqlbase();
		sqlbase sql = mSqlDb;
		i=sql.change(ssql, true);
		//this.setRcSql(i);
		this.setRC(i);
		/*
		if(i==0){
			i=sql.commit();
			this.setRC(i);
		}else{
			sql.rollback();
		}
		*/
	}
	
	public void sqlUpdate(String ssql){
		int i;
		this.setRC(vm.PRG_PROCESS_ERROR);
		//sqlbase sql = new sqlbase();
		sqlbase sql = mSqlDb;
		i=sql.change(ssql, true);
		//this.setRcSql(i);
		this.setRC(i);
		/*
		if(i==0){
			i = sql.commit();
			this.setRC(i);
		}else{
			sql.rollback();
		}
		*/
	}
	
	public void sqlDelete(String ssql){
		int i;
		this.setRC(vm.PRG_PROCESS_ERROR);
		//sqlbase sql = new sqlbase();
		sqlbase sql = mSqlDb;
		i=sql.change(ssql, true);
		//this.setRcSql(i);
		this.setRC(i);
		/*
		if(i==0){
			i=sql.commit();
			this.setRC(i);
		}else{
			sql.rollback();
		}
		*/
	}
	
	public String getSqlValField(String s){
		String s1="";
		Variant v;
		s1=s.replaceAll("\"", "");
		v=this.load(s1);
		if(v==null){
			v=this.loadField(s1);
		}
		if(v!=null){
			s1="'"+v.toString()+"'";
		}
		return(s1);
		
	}
	
	public void setDb(sqlbase sqldb){
		mSqlDb=sqldb;
	}
	//Chamadas Nativas
	public void nativeCall(String s){
		String s1="com.jsv.nativelib."+s;
		this.setRC(vm.PRG_PROCESS_ERROR);
		try{
			Class cl = Class.forName(s1);
			Object ob = cl.newInstance();
			INative oNat = (INative) ob;
			oNat.setSqlBase(mSqlDb);
			oNat.execute(fr[frAtual]);
			rc=oNat.getRc();
			errmsg=oNat.getErrmsg();
			this.setRC(rc);
		}catch(Exception ex){
			rc=4;
			errmsg = ex.toString();
			log("NariveCallErr:"+ex.toString());
		}
	}
	
	//log
	public void log(String s){
		//System.out.print(s);
	}
	public void log2(String s){
		System.out.print(s);
	}
}
