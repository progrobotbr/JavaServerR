package com.jsv.dictionary;

public class ComponentsDictionary {
	
	//Gui
	public static final String DESKTOP = "desktop";
	public static final String GUI = "gui";
	public static final String SCNDEF = "scndef";
	
	public static final int LABEL = 1;
	public static final int INPUTFIELD = 2;
	public static final int BUTTON = 3;
	public static final int COMBOBOX = 4;
	public static final int TABLE = 5;
	public static final int NEWLINE = 6;
	public static final int BOX     = 7;
	public static final int TABSTRIP = 8;
	
	//Attributos do protocolo de comunicação que representão os objetos gráficos
	public static final String LB = "lb"; //label
	public static final String IF = "if"; //inputfield
	public static final String BT = "bt"; //botão
	public static final String SL = "sl"; //combobox
	public static final String TB = "tb"; //table
	public static final String SP = "sp"; //tabstrip
	public static final String NL = "nl"; //enter - devio para outra linha
	
		
	//Atributos do protocolo de comunicação
	public static final String ID = "id"; //id do objeto
	public static final String VL = "vl"; //valor de um objeto
	public static final String WD = "wd"; //tamanho do componenente na tela
	public static final String HD = "hd"; //altura do componenente na tela
	public static final String NC = "nc"; //número máximo de caracteres
	public static final String TX = "tx"; //texto do objeto, como a descrição do botão, a mensagem do label, etc
	public static final String SS = "ss"; //bloco que define uma tela 
	public static final String CM = "cm"; //command -> gotoscr
	public static final String GT = "gt"; //indica que será um comando como gotoscr
	public static final String RC = "rc"; //atributo que indica o return code
	public static final String MD = "md"; //bloco que define a estrutura de uma transação
	public static final String MP = "mp"; //a resposta do servidor de uma tela
	public static final String SD = "sd"; //bloco que representa a resposta de uma tela
	public static final String TH = "th";
	public static final String TC = "tc";
	public static final String TM = "tm"; //total de colunas na tabela
	public static final String TL = "tl";
	public static final String TT = "tt"; //total de registros
	public static final String PS = "ps"; //Posição da coluna na tabela????
	public static final String TP = "tp"; //tipo da transação 0 server, 1 local
	public static final String BX = "bx"; //quadro box
	public static final String RO = "ro"; //Indica ReadOnly para controles de edição
	public static final String SZ = "sz"; //Tamanho de dados (tabela qtde linhas)
	public static final String PG = "pg"; //Tamanho de dados (tabela qtde linhas)
	public static final String SC = "sc"; //Sub tela
	//public static final String SCID = "scid"; //Id sub tela 
	//public static final String SCVL = "scvl"; //Id sub tela 
	//public static final String SCPI = "scpi"; //Id sub tela 
	
	public static final String DATA = "data";
	public static final String NOME = "nome";
	
	//Ids
	public static final String BUT_ = "but_";
	public static final String LBL_ = "lbl_";
	public static final String INP_ = "inp_";
	public static final String TAB_ = "tab_";
	public static final String TAB_BAIX_ = "tab_baix_"; //tabela para baixo
	public static final String TAB_CIMA_ = "tab_cima_"; //tabela para cima
	public static final String TAB_PAGE_ = "tab_page_"; //tabela pagina
	public static final String TAM_ = "tam_"; //tabela - meta dados da tabela
	public static final String TAS_ = "tas_"; //tabela - scrollbar da tabela
	public static final String SCR_ = "scr_";
	public static final String SPI_ = "spi_";
	public static final String SPM_ = "spm_";
	public static final String SPS_ = "sps_";
	public static final String SCRID = "scrid";
	public static final String TRZID = "trzid";
	public static final String TRZTP = "trztp";
	public static final int TRZSERVER = 0;
	public static final int TRZCLIENT = 1;
	
	
	public static int dictionaryElement(String pNome) {
		int i = 0;
		if (pNome.equals(LB))
			i = LABEL;
		else if (pNome.equals(IF))
			i = INPUTFIELD;
		else if (pNome.equals(BT))
			i = BUTTON;
		else if (pNome.equals(SL))
			i = COMBOBOX;
		else if (pNome.equals(TB))
			i = TABLE;
		else if (pNome.equals(NL))
			i = NEWLINE;
		else if (pNome.equals(BX))
			i = BOX;
		else if (pNome.equals(SP))
			i = TABSTRIP;
		return(i);
	}
	
}
