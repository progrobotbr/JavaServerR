package com.jsv;
//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "parserBasicIni.y"
  import java.io.*;
  import com.jsv.data.Variant;
  import com.jsv.lang.vm.vm;
  import com.jsv.lang.vm.type.decltype;
  import com.jsv.lang.vm.type.decltypeelement;
  import com.jsv.lang.vm.type.parameterPair;
  import com.jsv.lang.vm.type.parameterPairs;
//#line 25 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short NUMINT=257;
public final static short NUMFLT=258;
public final static short NL=259;
public final static short NAME=260;
public final static short FIELD=261;
public final static short TABLE=262;
public final static short TYPEC=263;
public final static short TYPEI=264;
public final static short TYPEF=265;
public final static short TYPED=266;
public final static short TYPEB=267;
public final static short DIM=268;
public final static short AS=269;
public final static short IF=270;
public final static short ELSE=271;
public final static short POINT=272;
public final static short CALL=273;
public final static short STRING=274;
public final static short ENDIF=275;
public final static short WHILE=276;
public final static short ENDWHILE=277;
public final static short SUB=278;
public final static short IN=279;
public final static short OUT=280;
public final static short ENDSUB=281;
public final static short TYPE=282;
public final static short ENDTYPE=283;
public final static short FOR=284;
public final static short EACH=285;
public final static short ENDFOR=286;
public final static short BREAK=287;
public final static short TRUE=288;
public final static short FALSE=289;
public final static short LPAR=290;
public final static short RPAR=291;
public final static short EQ=292;
public final static short PLUS=293;
public final static short MINUS=294;
public final static short DIV=295;
public final static short TIMES=296;
public final static short AND=297;
public final static short OR=298;
public final static short LT=299;
public final static short LTE=300;
public final static short GT=301;
public final static short GTE=302;
public final static short NEQ=303;
public final static short COMMENT=304;
public final static short APPEND=305;
public final static short READ=306;
public final static short DELETE=307;
public final static short TO=308;
public final static short WHERE=309;
public final static short LOAD=310;
public final static short KEY=311;
public final static short SCREEN=312;
public final static short GOTO=313;
public final static short BRANCH=314;
public final static short CALLPROG=315;
public final static short TBFIRST=316;
public final static short NATIVECALL=317;
public final static short MODIFY=318;
public final static short UPDATE=319;
public final static short SET=320;
public final static short SELECT=321;
public final static short INNER=322;
public final static short JOIN=323;
public final static short FROM=324;
public final static short VALUES=325;
public final static short ON=326;
public final static short INTO=327;
public final static short INSERT=328;
public final static short NAMESQLJ=329;
public final static short COMMA=330;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,   38,   38,   39,   39,   39,   39,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,
    3,    3,    1,    1,    2,    2,    4,    4,    4,    4,
    4,   11,    5,   12,   13,   14,   14,    6,    6,   40,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    9,    9,    9,    8,    8,    8,    8,    8,    8,    8,
   10,   10,   10,   10,   10,   10,   10,   18,   19,   19,
   20,   42,   44,   44,   46,   45,   45,   47,   43,   48,
   48,   50,   49,   49,   51,   16,   16,   15,   17,   17,
   17,   17,   17,   41,   41,   21,   21,   22,   22,   23,
   23,   24,   25,   37,   37,   37,   37,   37,   37,   37,
   37,   37,   37,   37,   37,   37,   37,   37,   37,   30,
   30,   28,   28,   28,   28,   28,   28,   28,   26,   27,
   29,   29,   31,   32,   32,   32,   33,   34,   34,   35,
   36,
};
final static short yylen[] = {                            2,
    0,    2,    1,    2,    3,    5,    2,    2,    3,    2,
    3,    2,    2,    3,    6,    2,    5,    2,    5,    5,
    6,    4,    3,    4,    2,    8,   11,    6,    6,    3,
    3,    3,    0,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    3,    3,    3,    3,    3,    1,    1,    1,    1,    1,
    1,    3,    3,    1,    3,    3,    3,    3,    3,    3,
    1,    1,    1,    1,    1,    1,    3,    3,    0,    2,
    2,    2,    0,    1,    2,    0,    1,    2,    2,    0,
    1,    2,    0,    1,    2,    0,    3,    3,    1,    1,
    1,    1,    1,    0,    2,    1,    1,    1,    3,    1,
    3,    1,    1,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    0,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    1,    3,    1,    1,    3,    7,    2,    1,    3,    3,
    1,
};
final static short yydefred[] = {                         1,
    0,    3,   48,   49,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    2,    0,    0,    0,    0,    0,   74,   75,   73,   71,
   72,    0,   76,    0,    0,   64,   12,   46,   47,  104,
   13,    0,   10,   45,  104,   18,   33,    0,   16,    7,
   43,    0,   44,    0,    0,   42,    0,    0,   25,    0,
    0,    0,  112,    0,  106,  107,  108,    0,    0,    0,
    4,    8,    0,    0,    5,    0,    0,    0,    0,    0,
    0,    0,   11,    0,    0,    0,    9,    0,    0,    0,
    0,    0,   79,    0,   23,    0,   14,   30,    0,    0,
    0,    0,    0,   56,   57,   58,   59,   60,    0,    0,
   37,   38,   39,   40,   31,   41,   32,   77,   65,   67,
   68,   69,   70,   66,    0,    0,  105,   96,    0,    0,
   91,   33,    0,    0,   84,   35,    0,   34,   36,    0,
    0,    0,    0,   22,   24,    0,  139,    0,  141,    0,
    0,    0,    0,  109,    0,    0,    0,    0,    0,    0,
    0,   19,   96,   89,   94,    0,   17,   33,   82,   87,
    6,    0,   20,    0,    0,   80,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   55,    0,    0,   54,   53,
    0,  104,    0,    0,   15,   21,    0,   29,  134,  135,
  137,  138,  136,  132,  133,  140,  125,  126,  128,  127,
  123,  124,    0,  129,    0,  142,   28,  145,    0,    0,
    0,    0,    0,  101,  102,   99,  103,  100,   78,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   98,  122,    0,    0,    0,    0,    0,    0,    0,
  117,   26,    0,    0,    0,  110,    0,    0,  146,    0,
    0,  151,    0,  148,    0,   27,  111,    0,    0,  149,
  150,
};
final static short yydgoto[] = {                          1,
   99,  148,  149,  125,  126,   43,  120,   44,   45,   46,
   67,   64,   55,   50,  202,  171,  239,  186,  153,  104,
  224,   78,  267,   74,    0,  158,  159,  216,  160,  191,
  162,  163,  269,  273,  274,  275,  225,   31,   32,   33,
   96,  143,  139,  144,  179,  145,  180,  140,  174,  141,
  175,
};
final static short yysindex[] = {                         0,
 -213,    0,    0,    0, -177, -148, -233, -248, -219, -148,
 -185, -169, -173, -144, -165, -111, -106,  -86,  -78,  -78,
  -70, -119,  -68,  -78,  -41,  -78,  -40, -254,  -93,  -50,
    0,  -22,  -28,  -24,  -12,   12,    0,    0,    0,    0,
    0, -148,    0,  -27, -264,    0,    0,    0,    0,    0,
    0, -138,    0,    0,    0,    0,    0,  -86,    0,    0,
    0,  -15,    0,   -4,    3,    0,   19,   59,    0,   45,
   46,   -3,    0,    2,    0,    0,    0, -258,  -40, -239,
    0,    0,  -32,  -32,    0, -153, -148, -148, -148, -148,
 -148, -148,    0, -148, -148, -235,    0, -189,  -95,   40,
  -78,  -86,    0,   48,    0,   51,    0,    0,  -86,   64,
  -40, -254,   35,    0,    0,    0,    0,    0, -239,   15,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -27,  -27,    0,    0,   54,   49,
    0,    0,   55,   50,    0,    0,   57,    0,    0,  -78,
   60,    3,   71,    0,    0,    3,    0,   41,    0, -280,
   65,    8,   14,    0, -254,    1, -239, -239, -239, -239,
   77,    0,    0,    0,    0,   53,    0,    0,    0,    0,
    0,   66,    0,   67,   52,    0,   68, -139, -247,   64,
   69,   82,  -40,   20, -276,    0, -270, -270,    0,    0,
   56,    0,   77,   53,    0,    0,  -31,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -247,    0,  -39,    0,    0,    0,   36,  -40,
   21,  -31,   88,    0,    0,    0,    0,    0,    0,  -51,
 -247, -247, -247, -247, -247, -247, -247, -247,   78,   80,
   61,    0,    0,  -20,  -13,   -1,  -47,    4,   58, -122,
    0,    0,   92,  -31,   27,    0, -271,   25,    0,   83,
  -31,    0,   62,    0,   70,    0,    0,   25,   25,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -174,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -183,    0, -249,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   84,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -96,  -92,    0,    0,    0,   85,
    0,    0,    0,   86,    0,    0,    0,    0,    0,    0,
    0,    0,   91,    0,    0,    0,    0,    0,    0,   93,
 -220,    0,   33,    0,    0,    0,    0,    0,    0,    0,
 -147,    0,    0,    0,    0, -242,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -69,  -55,    0,    0,
    0,    0,   94,   95,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   96,    0,    0,    0,   93,    0,
    0,    0,  -85,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -211,  -76, -161, -114, -100, -145, -129,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -104,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
 -126,    0,  359,  285,  -14,  369,  -91,   34,   -5,  179,
    0,  -17,    0,    0,    0,  198, -215,    0,    0, -116,
  -26,  207,    0,  -79,    0,    0,  183,    0,    0,  145,
    0,    0,    0,    0,   97,   98,  -34,    0,    0,    0,
  -54,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static int YYTABLESIZE=377;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        113,
   98,   77,   65,   62,   52,   75,   70,   93,   72,  217,
  218,   48,   75,  219,  231,  176,  252,  114,  115,  270,
  116,  117,   83,  137,  169,  170,  220,  166,  189,   85,
   83,  161,   94,   95,  118,  184,   86,   85,   47,  187,
  221,  222,  223,  100,  138,    2,    3,    4,  266,  190,
  119,  204,   51,  112,    5,  277,    6,    7,  271,    8,
  116,    9,   10,   11,   12,  111,   49,   13,   14,  137,
   15,  112,   16,   17,   76,  197,  198,  199,  200,  116,
  116,   76,   34,  151,   35,  164,   53,  152,   90,  142,
   54,   18,   19,   20,  156,   90,   21,   61,   56,   22,
   23,  144,   24,   25,   26,   27,  144,   28,   37,   38,
  114,    3,    4,  229,   29,   57,   61,  209,  210,   58,
  211,  212,   61,   61,   92,   39,  120,  135,  136,  114,
  114,   92,  182,   97,  213,  114,  114,  128,   77,   40,
   41,   42,  121,   94,   95,  120,  120,  233,  214,  215,
  250,  120,  120,  120,  120,  120,  120,  118,   94,   95,
   59,  121,  121,  146,   34,   60,   35,  121,  121,  121,
  121,  119,  121,   61,   97,   63,  118,  118,  246,   62,
  248,   63,  118,  118,  118,  118,   97,  147,  240,   66,
  119,  119,   68,   97,   63,  115,  119,  119,   62,  119,
   63,   63,   51,   69,   62,   62,  254,  255,  256,  257,
  258,  259,  260,  261,  115,  115,   52,  147,   71,   73,
  115,   51,  147,   51,   51,  234,  235,   61,  236,  237,
  121,  122,  123,   79,  124,   52,   81,   52,   52,  253,
  241,   80,  238,   82,   83,  242,  243,  244,  245,  246,
  247,  248,  241,  246,  247,  248,   84,  242,  243,  244,
  245,  246,  247,  248,   87,  129,  130,  131,  132,  133,
  134,   88,   89,   90,   91,   92,  242,  243,  244,  245,
  246,  247,  248,   85,  243,  244,  245,  246,  247,  248,
  105,  196,  101,  167,  168,  169,  170,  244,  245,  246,
  247,  248,  244,  102,  246,  247,  248,  167,  168,  169,
  170,  146,   34,  103,   35,  106,  107,  108,  150,  154,
  109,  110,  155,  157,  165,  172,  177,  173,  181,  178,
  185,  183,  188,  192,  193,  194,  201,  205,  206,  208,
  227,  228,  230,  207,  189,  251,  137,  232,  263,  262,
  264,  265,  268,  272,  276,   50,   93,   86,  278,  143,
  248,  279,   81,   36,  130,   95,   88,  131,  127,   30,
  203,  195,  226,  249,  280,    0,  281,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         79,
   55,   28,   20,   18,   10,  260,   24,  272,   26,  257,
  258,  260,  260,  261,  291,  142,  232,  257,  258,  291,
  260,  261,  272,  259,  295,  296,  274,  119,  309,  272,
  280,  111,  297,  298,  274,  152,   42,  280,  272,  156,
  288,  289,  290,   58,  280,  259,  260,  261,  264,  330,
  290,  178,  272,  330,  268,  271,  270,  271,  330,  273,
  272,  275,  276,  277,  278,  324,  315,  281,  282,  259,
  284,  330,  286,  287,  329,  167,  168,  169,  170,  291,
  292,  329,  260,  101,  262,  112,  272,  102,  272,  279,
  260,  305,  306,  307,  109,  279,  310,  272,  272,  313,
  314,  322,  316,  317,  318,  319,  327,  321,  257,  258,
  272,  260,  261,  193,  328,  260,  291,  257,  258,  285,
  260,  261,  297,  298,  272,  274,  272,   94,   95,  291,
  292,  279,  150,  272,  274,  297,  298,  291,  165,  288,
  289,  290,  272,  297,  298,  291,  292,  202,  288,  289,
  230,  297,  298,  299,  300,  301,  302,  272,  297,  298,
  272,  291,  292,  259,  260,  272,  262,  297,  298,  299,
  300,  272,  302,  260,  260,  272,  291,  292,  301,  272,
  303,  260,  297,  298,  299,  300,  272,  283,  223,  260,
  291,  292,  312,  279,  291,  272,  297,  298,  291,  300,
  297,  298,  272,  272,  297,  298,  241,  242,  243,  244,
  245,  246,  247,  248,  291,  292,  272,  322,  260,  260,
  297,  291,  327,  293,  294,  257,  258,  260,  260,  261,
  263,  264,  265,  327,  267,  291,  259,  293,  294,  291,
  292,  292,  274,  272,  269,  297,  298,  299,  300,  301,
  302,  303,  292,  301,  302,  303,  269,  297,  298,  299,
  300,  301,  302,  303,  292,   87,   88,   89,   90,   91,
   92,  299,  300,  301,  302,  303,  297,  298,  299,  300,
  301,  302,  303,  272,  298,  299,  300,  301,  302,  303,
  272,  291,  308,  293,  294,  295,  296,  299,  300,  301,
  302,  303,  299,  308,  301,  302,  303,  293,  294,  295,
  296,  259,  260,  311,  262,  257,  272,  272,  279,  272,
  324,  320,  272,  260,  290,  272,  272,  279,  272,  280,
  260,  272,  292,  269,  327,  322,  260,  272,  272,  272,
  272,  260,  323,  292,  309,  325,  259,  292,  269,  272,
  290,  260,  326,  329,  272,  272,  272,  272,  297,  327,
  303,  292,  272,    5,  272,  272,  272,  272,   84,    1,
  173,  165,  190,  229,  278,   -1,  279,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=330;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"NUMINT","NUMFLT","NL","NAME","FIELD","TABLE","TYPEC","TYPEI",
"TYPEF","TYPED","TYPEB","DIM","AS","IF","ELSE","POINT","CALL","STRING","ENDIF",
"WHILE","ENDWHILE","SUB","IN","OUT","ENDSUB","TYPE","ENDTYPE","FOR","EACH",
"ENDFOR","BREAK","TRUE","FALSE","LPAR","RPAR","EQ","PLUS","MINUS","DIV","TIMES",
"AND","OR","LT","LTE","GT","GTE","NEQ","COMMENT","APPEND","READ","DELETE","TO",
"WHERE","LOAD","KEY","SCREEN","GOTO","BRANCH","CALLPROG","TBFIRST","NATIVECALL",
"MODIFY","UPDATE","SET","SELECT","INNER","JOIN","FROM","VALUES","ON","INTO",
"INSERT","NAMESQLJ","COMMA",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"line : NL",
"line : stmt NL",
"stmt : DIM declvar POINT",
"stmt : TYPE NAME declvars ENDTYPE POINT",
"stmt : BREAK POINT",
"stmt : attrib POINT",
"stmt : WHILE log POINT",
"stmt : ENDWHILE POINT",
"stmt : IF log POINT",
"stmt : ELSE POINT",
"stmt : ENDIF POINT",
"stmt : TBFIRST nametab POINT",
"stmt : FOR EACH namestru IN nametab POINT",
"stmt : ENDFOR POINT",
"stmt : SUB namefunc ml parameters POINT",
"stmt : ENDSUB POINT",
"stmt : CALL namefunc2 ml callparam POINT",
"stmt : APPEND namestru TO nametab POINT",
"stmt : READ nametab TO namestru logkey POINT",
"stmt : DELETE nametab logkey POINT",
"stmt : LOAD namefile POINT",
"stmt : GOTO SCREEN NUMINT POINT",
"stmt : BRANCH POINT",
"stmt : SELECT sqlfds FROM sqljoin INTO sqltb sqlwhere POINT",
"stmt : INSERT INTO sqltb LPAR sqlfds RPAR VALUES LPAR sqlvls RPAR POINT",
"stmt : UPDATE sqltb SET sqlpairs sqlwhere POINT",
"stmt : MODIFY nametab FROM namestru logkey POINT",
"stmt : NATIVECALL NAME POINT",
"declvar : NAME AS type",
"declvar : TABLE AS type",
"declvars :",
"declvars : declvars declvar1",
"declvar1 : NL",
"declvar1 : declvar",
"type : TYPEC",
"type : TYPEI",
"type : TYPEF",
"type : TYPEB",
"type : namestru",
"namefile : NAME",
"namestru : NAME",
"nametab : NAME",
"namefunc : NAME",
"namefunc2 : NAME",
"namefunc2 : CALLPROG",
"var : NAME",
"var : FIELD",
"attrib : var EQ exp",
"exp : exp PLUS exp",
"exp : exp MINUS exp",
"exp : exp TIMES exp",
"exp : exp DIV exp",
"exp : LPAR exp RPAR",
"exp : NUMINT",
"exp : NUMFLT",
"exp : NAME",
"exp : FIELD",
"exp : STRING",
"log : cmp",
"log : log OR cmp",
"log : log AND cmp",
"cmp : cmpterm",
"cmp : cmp EQ cmpterm",
"cmp : cmp NEQ cmpterm",
"cmp : cmp LT cmpterm",
"cmp : cmp LTE cmpterm",
"cmp : cmp GT cmpterm",
"cmp : cmp GTE cmpterm",
"cmpterm : TRUE",
"cmpterm : FALSE",
"cmpterm : STRING",
"cmpterm : NUMINT",
"cmpterm : NUMFLT",
"cmpterm : var",
"cmpterm : LPAR log RPAR",
"logtb : NAME EQ nameconst",
"logtbs :",
"logtbs : logtbs logtb",
"logkey : KEY logtbs",
"parameters : funcins funcouts",
"funcins :",
"funcins : funcin",
"funcin : IN declvars",
"funcouts :",
"funcouts : funcout",
"funcout : OUT declvars",
"callparam : callouts callins",
"callouts :",
"callouts : callout",
"callout : OUT callatribs",
"callins :",
"callins : callin",
"callin : IN callatribs",
"callatribs :",
"callatribs : callatribs callatrib ml",
"callatrib : NAME EQ nameconst",
"nameconst : NAME",
"nameconst : STRING",
"nameconst : NUMINT",
"nameconst : NUMFLT",
"nameconst : FIELD",
"ml :",
"ml : ml NL",
"sqlname : NAME",
"sqlname : NAMESQLJ",
"sqlfds : sqlname",
"sqlfds : sqlfds COMMA sqlname",
"sqlvls : nameconst",
"sqlvls : sqlvls COMMA nameconst",
"sqltb : NAME",
"sqlvtb : NAME",
"sqllog : sqllog OR sqllog",
"sqllog : sqllog AND sqllog",
"sqllog : sqllog EQ sqllog",
"sqllog : sqllog NEQ sqllog",
"sqllog : sqllog LT sqllog",
"sqllog : sqllog LTE sqllog",
"sqllog : sqllog GT sqllog",
"sqllog : sqllog GTE sqllog",
"sqllog : LPAR sqllog RPAR",
"sqllog : TRUE",
"sqllog : FALSE",
"sqllog : NUMINT",
"sqllog : NUMFLT",
"sqllog : STRING",
"sqllog : FIELD",
"sqllog : sqlname",
"sqlwhere :",
"sqlwhere : WHERE sqllog",
"sqlvar : TRUE",
"sqlvar : FALSE",
"sqlvar : NUMINT",
"sqlvar : NUMFLT",
"sqlvar : STRING",
"sqlvar : NAME",
"sqlvar : FIELD",
"sqlfield : NAME",
"sqlpair : sqlfield EQ sqlvar",
"sqlpairs : sqlpair",
"sqlpairs : sqlpairs COMMA sqlpair",
"sqljoin : sqljoininner",
"sqljoininner : sqltb",
"sqljoininner : sqltb AS NAME",
"sqljoininner : sqljoininner INNER JOIN sqltb AS NAME sqlon",
"sqlon : ON sqlonpars",
"sqlonpars : sqlonpar",
"sqlonpars : sqlonpars AND sqlonpar",
"sqlonpar : sqlfieldon EQ sqlfieldon",
"sqlfieldon : NAMESQLJ",
};

//#line 330 "parserBasicIni.y"

  public BYylex lexer;
  public vm vmc; // = new vm();

  public void setVM(vm pvm){
    vmc=pvm;
  }
  
  public void lg(String s){
    System.out.println(s);
  }

  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }


  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex Calculator Demo");

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Expression: ");
      interactive = true;
	    yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    
    if (interactive) {
      System.out.println();
      System.out.println("Have a nice day");
    }
  }
//#line 647 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 3:
//#line 79 "parserBasicIni.y"
{  }
break;
case 4:
//#line 80 "parserBasicIni.y"
{  lg("line");}
break;
case 5:
//#line 83 "parserBasicIni.y"
{ lg("DIM");      vmc.createVar(val_peek(1).decl);              }
break;
case 6:
//#line 84 "parserBasicIni.y"
{ lg("TYPE");     val_peek(2).declt.Name=val_peek(3).sval; vmc.putType(val_peek(3).sval,val_peek(2).declt); }
break;
case 7:
//#line 85 "parserBasicIni.y"
{ lg("BREAK");                                    }
break;
case 8:
//#line 86 "parserBasicIni.y"
{ lg("ATTRIB");                                   }
break;
case 9:
//#line 87 "parserBasicIni.y"
{ lg("WHILE");    vmc.whileCmd(val_peek(1).var);               }
break;
case 10:
//#line 88 "parserBasicIni.y"
{ lg("ENDWHILE"); vmc.gotoCmd();                  }
break;
case 11:
//#line 89 "parserBasicIni.y"
{ lg("IF");       vmc.ifCmd(val_peek(1).var);                  }
break;
case 12:
//#line 90 "parserBasicIni.y"
{ lg("ELSE");                                     }
break;
case 13:
//#line 91 "parserBasicIni.y"
{ lg("ENDIF");                                    }
break;
case 14:
//#line 92 "parserBasicIni.y"
{ lg("TBFIRST");  vmc.tableFirst(val_peek(1).sval);             }
break;
case 15:
//#line 93 "parserBasicIni.y"
{ lg("FOREACH");  vmc.forEach(val_peek(3).decl,val_peek(1).sval);             }
break;
case 16:
//#line 94 "parserBasicIni.y"
{ lg("ENDFOR");   vmc.gotoCmd();                  }
break;
case 17:
//#line 95 "parserBasicIni.y"
{ lg("SUB");      vmc.createMetaSub(val_peek(3).sval);          }
break;
case 18:
//#line 96 "parserBasicIni.y"
{ lg("ENDSUB");   vmc.endSub();                   }
break;
case 19:
//#line 97 "parserBasicIni.y"
{ lg("CALL");     vmc.callSub(val_peek(3).sval);                }
break;
case 20:
//#line 98 "parserBasicIni.y"
{ lg("APPEND");   vmc.appendTable(val_peek(3).decl,val_peek(1).sval);         }
break;
case 21:
//#line 99 "parserBasicIni.y"
{ lg("READ");     vmc.readTable(val_peek(2).decl,val_peek(4).sval,val_peek(1).pairs);        }
break;
case 22:
//#line 100 "parserBasicIni.y"
{ lg("DELETE");   vmc.deleteTable(val_peek(2).sval,val_peek(1).pairs);         }
break;
case 23:
//#line 101 "parserBasicIni.y"
{ lg("LOAD");     vmc.loadProg(val_peek(1).sval);               }
break;
case 24:
//#line 102 "parserBasicIni.y"
{ lg("GOTOSCR");  vmc.gotoScreen(val_peek(1).var);             }
break;
case 25:
//#line 103 "parserBasicIni.y"
{ lg("BRANCH");   vmc.gotoCmd();                  }
break;
case 26:
//#line 104 "parserBasicIni.y"
{ lg("SELECT"); vmc.sqlSelect("select " + val_peek(6).sval + " FROM " + val_peek(4).sval + val_peek(1).sval, val_peek(2).sval); }
break;
case 27:
//#line 105 "parserBasicIni.y"
{ lg("INSERT"); vmc.sqlInsert("insert into " + val_peek(8).sval + "( " + val_peek(6).sval + " ) values ( " + val_peek(2).sval + " )"); }
break;
case 28:
//#line 106 "parserBasicIni.y"
{ lg("UPDATE"); vmc.sqlUpdate("update " + val_peek(4).sval + " set " + val_peek(2).sval + val_peek(1).sval); }
break;
case 29:
//#line 107 "parserBasicIni.y"
{ lg("MODIFY"); vmc.modifyTable(val_peek(4).sval,val_peek(2).decl,val_peek(1).pairs); }
break;
case 30:
//#line 108 "parserBasicIni.y"
{ lg("NATIVECALL"); vmc.nativeCall(val_peek(1).sval); }
break;
case 31:
//#line 111 "parserBasicIni.y"
{ val_peek(0).decl.sNome = val_peek(2).sval; yyval.decl = val_peek(0).decl; }
break;
case 32:
//#line 112 "parserBasicIni.y"
{ val_peek(0).decl.sNome = val_peek(2).sval; yyval.decl = val_peek(0).decl; }
break;
case 33:
//#line 115 "parserBasicIni.y"
{ yyval.declt = null; }
break;
case 34:
//#line 116 "parserBasicIni.y"
{ if(val_peek(1).declt==null) { val_peek(1).declt = new decltype(); }; val_peek(1).declt.addElement(val_peek(0).decl); yyval.declt=val_peek(1).declt; }
break;
case 35:
//#line 119 "parserBasicIni.y"
{ yyval.decl = null; }
break;
case 36:
//#line 120 "parserBasicIni.y"
{ yyval.decl = val_peek(0).decl; lg("r");}
break;
case 37:
//#line 123 "parserBasicIni.y"
{ yyval.decl = new decltypeelement("",val_peek(0).sval,Variant.STRING);    }
break;
case 38:
//#line 124 "parserBasicIni.y"
{ yyval.decl = new decltypeelement("",val_peek(0).sval,Variant.INT);       }
break;
case 39:
//#line 125 "parserBasicIni.y"
{ yyval.decl = new decltypeelement("",val_peek(0).sval,Variant.FLOAT);     }
break;
case 40:
//#line 126 "parserBasicIni.y"
{ yyval.decl = new decltypeelement("",val_peek(0).sval,Variant.BOOL);      }
break;
case 41:
//#line 127 "parserBasicIni.y"
{ yyval.decl = val_peek(0).decl;                                           }
break;
case 43:
//#line 133 "parserBasicIni.y"
{ yyval.decl = new decltypeelement("",val_peek(0).sval, Variant.STRUCTURE); }
break;
case 48:
//#line 146 "parserBasicIni.y"
{ yyval.decl = new decltypeelement(val_peek(0).sval,"",Variant.NAME);  }
break;
case 49:
//#line 147 "parserBasicIni.y"
{ yyval.decl = new decltypeelement(val_peek(0).sval,"",Variant.FIELD); }
break;
case 50:
//#line 150 "parserBasicIni.y"
{ vmc.move2NameOrField(val_peek(2).decl.sNome,val_peek(0).var,val_peek(2).decl.iType); }
break;
case 51:
//#line 153 "parserBasicIni.y"
{ yyval.var = vmc.calc('+', val_peek(2).var, val_peek(0).var); }
break;
case 52:
//#line 154 "parserBasicIni.y"
{ yyval.var = vmc.calc('-', val_peek(2).var, val_peek(0).var); }
break;
case 53:
//#line 155 "parserBasicIni.y"
{ yyval.var = vmc.calc('*', val_peek(2).var, val_peek(0).var); }
break;
case 54:
//#line 156 "parserBasicIni.y"
{ yyval.var = vmc.calc('/', val_peek(2).var, val_peek(0).var); }
break;
case 55:
//#line 157 "parserBasicIni.y"
{ yyval.var = val_peek(1).var;                    }
break;
case 56:
//#line 158 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 57:
//#line 159 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 58:
//#line 160 "parserBasicIni.y"
{ yyval.var = vmc.load(val_peek(0).sval);          }
break;
case 59:
//#line 161 "parserBasicIni.y"
{ yyval.var = vmc.loadField(val_peek(0).sval);     }
break;
case 60:
//#line 162 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 61:
//#line 165 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 62:
//#line 166 "parserBasicIni.y"
{ yyval.var = vmc.bool('O',val_peek(2).var,val_peek(0).var);   }
break;
case 63:
//#line 167 "parserBasicIni.y"
{ yyval.var = vmc.bool('A',val_peek(2).var,val_peek(0).var);   }
break;
case 64:
//#line 170 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 65:
//#line 171 "parserBasicIni.y"
{ yyval.var = vmc.bool('=',val_peek(2).var,val_peek(0).var);   }
break;
case 66:
//#line 172 "parserBasicIni.y"
{ yyval.var = vmc.bool('D',val_peek(2).var,val_peek(0).var);   }
break;
case 67:
//#line 173 "parserBasicIni.y"
{ yyval.var = vmc.bool('<',val_peek(2).var,val_peek(0).var);   }
break;
case 68:
//#line 174 "parserBasicIni.y"
{ yyval.var = vmc.bool('N',val_peek(2).var,val_peek(0).var);   }
break;
case 69:
//#line 175 "parserBasicIni.y"
{ yyval.var = vmc.bool('>',val_peek(2).var,val_peek(0).var);   }
break;
case 70:
//#line 176 "parserBasicIni.y"
{ yyval.var = vmc.bool('M',val_peek(2).var,val_peek(0).var);   }
break;
case 71:
//#line 179 "parserBasicIni.y"
{ yyval.var = new Variant(true);     }
break;
case 72:
//#line 180 "parserBasicIni.y"
{ yyval.var = new Variant(false);    }
break;
case 73:
//#line 181 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 74:
//#line 182 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 75:
//#line 183 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                    }
break;
case 76:
//#line 184 "parserBasicIni.y"
{ yyval.var = vmc.loadNameOrField(val_peek(0).decl.sNome, val_peek(0).decl.iType); }
break;
case 77:
//#line 185 "parserBasicIni.y"
{ yyval.var = val_peek(1).var;                    }
break;
case 78:
//#line 188 "parserBasicIni.y"
{ yyval.pair = new parameterPair(val_peek(2).sval,val_peek(0).var); }
break;
case 79:
//#line 191 "parserBasicIni.y"
{ yyval.pairs = new parameterPairs(); }
break;
case 80:
//#line 192 "parserBasicIni.y"
{ val_peek(1).pairs.add(val_peek(0).pair); yyval.pairs = val_peek(1).pairs; }
break;
case 81:
//#line 195 "parserBasicIni.y"
{ yyval.pairs = val_peek(0).pairs; }
break;
case 83:
//#line 200 "parserBasicIni.y"
{ vmc.setNullSubDeclIn(); }
break;
case 84:
//#line 201 "parserBasicIni.y"
{ }
break;
case 85:
//#line 204 "parserBasicIni.y"
{ vmc.putSubDeclIn(val_peek(0).declt); }
break;
case 86:
//#line 207 "parserBasicIni.y"
{ vmc.setNullSubDeclOut(); }
break;
case 87:
//#line 208 "parserBasicIni.y"
{ }
break;
case 88:
//#line 211 "parserBasicIni.y"
{ vmc.putSubDeclOut(val_peek(0).declt); }
break;
case 90:
//#line 217 "parserBasicIni.y"
{ vmc.setNullParameterOut();}
break;
case 92:
//#line 221 "parserBasicIni.y"
{ vmc.setParameterOut(val_peek(0).pairs); }
break;
case 93:
//#line 224 "parserBasicIni.y"
{ vmc.setNullParameterIn();}
break;
case 95:
//#line 228 "parserBasicIni.y"
{ vmc.setParameterIn(val_peek(0).pairs); }
break;
case 96:
//#line 231 "parserBasicIni.y"
{ yyval.pairs = new parameterPairs(); }
break;
case 97:
//#line 232 "parserBasicIni.y"
{ val_peek(2).pairs.add(val_peek(1).pair); yyval.pairs = val_peek(2).pairs;       }
break;
case 98:
//#line 235 "parserBasicIni.y"
{ yyval.pair = new parameterPair(val_peek(2).sval,val_peek(0).var); }
break;
case 99:
//#line 238 "parserBasicIni.y"
{ yyval.var = vmc.getVariantReference(val_peek(0).sval); }
break;
case 100:
//#line 239 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                          }
break;
case 101:
//#line 240 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                          }
break;
case 102:
//#line 241 "parserBasicIni.y"
{ yyval.var = val_peek(0).var;                          }
break;
case 103:
//#line 242 "parserBasicIni.y"
{ yyval.var = vmc.getVariantReference(val_peek(0).sval); }
break;
case 106:
//#line 249 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 107:
//#line 250 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 108:
//#line 253 "parserBasicIni.y"
{ yyval.sval = ""+val_peek(0).sval+""; }
break;
case 109:
//#line 254 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).sval +"";  }
break;
case 110:
//#line 257 "parserBasicIni.y"
{ yyval.sval = val_peek(0).var.getString(); }
break;
case 111:
//#line 258 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).var.getString();  }
break;
case 112:
//#line 261 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 113:
//#line 264 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 114:
//#line 267 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " or " + val_peek(0).sval;            }
break;
case 115:
//#line 268 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " and " + val_peek(0).sval;           }
break;
case 116:
//#line 269 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " = " + val_peek(0).sval;             }
break;
case 117:
//#line 270 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " <> " + val_peek(0).sval;            }
break;
case 118:
//#line 271 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " < " + val_peek(0).sval;             }
break;
case 119:
//#line 272 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " <= " + val_peek(0).sval;            }
break;
case 120:
//#line 273 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " > " + val_peek(0).sval;             }
break;
case 121:
//#line 274 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " >= " + val_peek(0).sval;            }
break;
case 122:
//#line 275 "parserBasicIni.y"
{ yyval.sval = " (" + val_peek(1).sval +")";              }
break;
case 123:
//#line 276 "parserBasicIni.y"
{ yyval.sval = " true ";                    }
break;
case 124:
//#line 277 "parserBasicIni.y"
{ yyval.sval = " false ";                   }
break;
case 125:
//#line 278 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 126:
//#line 279 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 127:
//#line 280 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 128:
//#line 281 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).sval + "' ";            }
break;
case 129:
//#line 282 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval;                          }
break;
case 130:
//#line 285 "parserBasicIni.y"
{ yyval.sval = " "; }
break;
case 131:
//#line 286 "parserBasicIni.y"
{ yyval.sval = " where " + val_peek(0).sval; }
break;
case 132:
//#line 289 "parserBasicIni.y"
{ yyval.sval = " true "; }
break;
case 133:
//#line 290 "parserBasicIni.y"
{ yyval.sval = " false "; }
break;
case 134:
//#line 291 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 135:
//#line 292 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 136:
//#line 293 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 137:
//#line 294 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).sval + "' "; }
break;
case 138:
//#line 295 "parserBasicIni.y"
{ yyval.sval = " '" + val_peek(0).sval + "' "; }
break;
case 139:
//#line 298 "parserBasicIni.y"
{ yyval.sval = " " + val_peek(0).sval + " "; }
break;
case 140:
//#line 301 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " = " + val_peek(0).sval; }
break;
case 141:
//#line 304 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 142:
//#line 305 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;  }
break;
case 143:
//#line 308 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 144:
//#line 311 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 145:
//#line 312 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " as " + val_peek(0).sval; }
break;
case 146:
//#line 313 "parserBasicIni.y"
{ yyval.sval = " " + val_peek(6).sval + " inner join " + val_peek(3).sval + " as " + val_peek(1).sval + val_peek(0).sval + " "; }
break;
case 147:
//#line 316 "parserBasicIni.y"
{ yyval.sval = " on " + val_peek(0).sval; }
break;
case 148:
//#line 319 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 149:
//#line 320 "parserBasicIni.y"
{ yyval.sval = val_peek(2).sval + " and " + val_peek(0).sval; }
break;
case 150:
//#line 323 "parserBasicIni.y"
{ yyval.sval = "" + val_peek(2).sval + " = " + val_peek(0).sval; }
break;
case 151:
//#line 326 "parserBasicIni.y"
{ yyval.sval = val_peek(0).sval; }
break;
//#line 1348 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
