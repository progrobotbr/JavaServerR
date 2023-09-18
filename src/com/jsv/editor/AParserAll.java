package com.jsv.editor;
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






//#line 2 "ParserBasic.y"
  import java.io.*;
//#line 19 "Parser.java"




public class AParserAll
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//Erros
public String errmsg="";
public int errposition=0;
public String getWord(){
	return(lexer.yytext());
}

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
public final static short NL=257;
public final static short NUMINT=258;
public final static short NUMFLT=259;
public final static short NAME=260;
public final static short FIELD=261;
public final static short CALLPROG=262;
public final static short TABLE=263;
public final static short TYPEC=264;
public final static short TYPEI=265;
public final static short TYPEF=266;
public final static short TYPED=267;
public final static short TYPEB=268;
public final static short DIM=269;
public final static short AS=270;
public final static short IF=271;
public final static short ELSE=272;
public final static short POINT=273;
public final static short CALL=274;
public final static short STRING=275;
public final static short ENDIF=276;
public final static short WHILE=277;
public final static short ENDWHILE=278;
public final static short SUB=279;
public final static short IN=280;
public final static short OUT=281;
public final static short ENDSUB=282;
public final static short TYPE=283;
public final static short ENDTYPE=284;
public final static short FOR=285;
public final static short EACH=286;
public final static short ENDFOR=287;
public final static short BREAK=288;
public final static short TRUE=289;
public final static short COMMA=290;
public final static short FALSE=291;
public final static short LPAR=292;
public final static short RPAR=293;
public final static short EQ=294;
public final static short PLUS=295;
public final static short MINUS=296;
public final static short DIV=297;
public final static short TIMES=298;
public final static short AND=299;
public final static short OR=300;
public final static short LT=301;
public final static short LTE=302;
public final static short GT=303;
public final static short GTE=304;
public final static short NEQ=305;
public final static short COMMENT=306;
public final static short APPEND=307;
public final static short READ=308;
public final static short DELETE=309;
public final static short TO=310;
public final static short WHERE=311;
public final static short LOAD=312;
public final static short TBFIRST=313;
public final static short KEY=314;
public final static short SCREEN=315;
public final static short GOTO=316;
public final static short BRANCH=317;
public final static short NATIVECALL=318;
public final static short MODIFY=319;
public final static short UPDATE=320;
public final static short SET=321;
public final static short SELECT=322;
public final static short INNER=323;
public final static short JOIN=324;
public final static short FROM=325;
public final static short VALUES=326;
public final static short ON=327;
public final static short INTO=328;
public final static short INSERT=329;
public final static short NAMESQLJ=330;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,   19,   19,   19,   22,   22,   23,   23,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   21,   24,   24,   25,   25,   37,   37,   36,   36,   36,
   36,   36,   29,   28,   34,   30,   30,   26,   38,   38,
   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,
   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,
   27,   27,   27,   27,   27,   40,   41,   41,   33,   35,
   42,   42,   44,   43,   43,   45,   32,   46,   46,   48,
   47,   47,   50,   49,   49,   51,   18,   18,   18,   18,
   18,   31,   31,    2,    2,    3,    3,    4,    4,    5,
    6,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,   11,   11,    9,
    9,    9,    9,    9,    9,    9,    7,    8,   10,   10,
   12,   13,   13,   13,   14,   15,   15,   16,   17,
};
final static short yylen[] = {                            2,
    0,    2,    1,    2,    2,    0,    2,    1,    2,    3,
    5,    2,    2,    6,    6,    9,    3,    9,    5,    5,
    6,    4,    3,    4,    2,    8,   11,    6,    6,    3,
    8,    3,    3,    0,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    1,    1,
    3,    3,    3,    3,    3,    1,    1,    1,    1,    1,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    1,
    1,    1,    1,    1,    1,    3,    0,    2,    2,    2,
    0,    1,    2,    0,    1,    2,    2,    0,    1,    2,
    0,    1,    2,    0,    3,    3,    1,    1,    1,    1,
    1,    0,    2,    1,    1,    1,    3,    1,    3,    1,
    1,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    0,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    1,    3,
    1,    1,    3,    7,    2,    1,    3,    3,    1,
};
final static short yydefred[] = {                         1,
    0,    3,   49,   50,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    2,    0,    0,    0,    0,    0,
    0,    0,   72,   73,   74,   70,   71,    0,    0,   75,
   46,   47,  102,    0,   45,  102,   34,    0,   12,   43,
    0,   44,    0,    0,    0,    0,    0,   25,    0,    0,
  110,    0,  104,  105,  106,    0,    0,    4,    5,   13,
    0,    0,    0,   10,    0,    6,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    6,    0,    0,    0,    0,
    0,   77,    0,   23,   17,    0,   30,    0,    0,    0,
    0,    0,   56,   57,   58,   59,   60,    0,    0,   38,
   39,   40,   41,   42,   32,   33,   69,    0,    0,    0,
    0,    0,    0,    0,    0,   64,  103,   94,    0,    0,
   89,    0,   34,    0,    0,   82,   36,    0,   37,   35,
    0,    0,    0,    0,   22,   24,    0,  137,    0,  139,
    0,  107,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    8,    0,    0,    0,    7,    0,   19,   94,   87,
   92,    0,    0,    1,   34,   80,   85,   11,    0,   20,
    0,    0,   78,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   55,    0,    0,   54,   53,    6,   15,    9,
    0,  102,    0,   14,    0,    0,    6,   21,    0,   29,
  132,  133,  135,  136,  134,  130,  131,  138,  140,  123,
  124,  126,  125,  121,  122,    0,    0,  127,   28,  143,
    0,    0,    0,    0,    0,    0,    0,    0,   99,  100,
   97,  101,   98,   76,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   96,   31,    0,
  120,    0,    0,    0,    0,    0,    0,    0,  115,   26,
    0,    0,   16,   18,    0,    0,  108,    0,  144,    0,
    0,  149,    0,  146,    0,  109,   27,    0,    0,  147,
  148,
};
final static short yydgoto[] = {                          1,
  227,  228,   66,  276,   62,    0,  149,  150,  218,  151,
  188,  154,  155,  279,  283,  284,  285,  244,   25,  165,
   27,  118,  166,  139,   88,   28,   39,   53,  114,   43,
   85,  129,   93,   46,  134,  115,  140,   40,  109,  183,
  144,  135,  176,  136,  177,  130,  170,  131,  167,  171,
  202,
};
final static short yysindex[] = {                         0,
  -10,    0,    0,    0, -124, -219, -102, -219, -231, -184,
 -152, -157, -118, -111, -111,  -91, -111, -165,  -78,  -81,
 -111,  -59, -255, -105,    0,  -25,  -23,  -27,  -37,   -7,
   -2,  -11,    0,    0,    0,    0,    0, -219, -190,    0,
    0,    0,    0,  -74,    0,    0,    0, -118,    0,    0,
  -33,    0,    7,  -29,   27,   38,   58,    0,   47,    3,
    0,    5,    0,    0,    0, -278,  -59,    0,    0,    0,
 -238,    6,    6,    0,  205,    0, -219, -219, -219, -219,
 -219, -219, -219, -219, -229,    0, -188, -227,   50, -111,
 -118,    0,   60,    0,    0,   63,    0, -118,   71, -255,
  -59,   45,    0,    0,    0,    0,    0, -238, -230,    0,
    0,    0,    0,    0,    0,    0,    0, -131,  238,  251,
   -9,  -21,   43,   33, -113,    0,    0,    0,   66,   69,
    0,   44,    0,   81,   84,    0,    0,  107,    0,    0,
 -111,  109,  -29,  124,    0,    0,  -29,    0,   91,    0,
 -158,    0,  117,   62,   68, -255,  -57, -238, -238, -238,
 -238,    0,  119,  120,  146,    0,  148,    0,    0,    0,
    0,  155, -183,    0,    0,    0,    0,    0,  161,    0,
  163,  125,    0,  165, -170,   71, -243,  168,  184,  -59,
  121, -142,    0, -191, -191,    0,    0,    0,    0,    0,
  153,    0,  148,    0,  -64, -183,    0,    0,  236,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -243,  231,    0,    0,    0,
  135,  -59,  126,   98,  236,  193,  180,  152,    0,    0,
    0,    0,    0,    0,  219, -243, -243, -243, -243, -243,
 -243, -243, -243,  181,  185,  166,  189,    0,    0,  190,
    0,  245,  257,  262,   20,   97,  162,  -94,    0,    0,
  197,  236,    0,    0,  151,  -14,    0,  143,    0,  236,
  202,    0,  201,    0,  186,    0,    0,  143,  143,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -242,    0, -254,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  206,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -126, -249,
 -215,   77,  149, -129,   41,    0,    0,    0,    0,  211,
    0,    0,    0,    0,  228,    0,    0,    0,    0,    0,
    0,    0,    0,  229,    0,    0,    0,    0,    0,    0,
  230,    0, -220,    0,  187,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -209,    0,    0,    0,
    0,    0, -247,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -93,  -79,    0,    0,    0,    0,    0,
    0,    0,  241,    0,    0,  243,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  244,    0,    0,    0,
  230,    0,    0,    0,    0,  -38,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -110, -171, -138,  131,  183,  -13,   95,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -203,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                       352,
  240,  -19,  371,    0,  -66,    0,    0,  342,    0,    0,
  298,    0,    0,    0,    0,  280,  281, -221,    0,    1,
    0,  -80,    0,  564, -122,    0,   17,   -8,   -5,    0,
  -43,    0,   65,    0,    0,  498,    0,   -1,  -98,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  403,    0,
    0,
};
final static int YYTABLESIZE=572;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         29,
  102,   26,   87,   65,   63,  132,   54,   51,   56,  157,
  173,  100,   60,  258,  220,  221,   63,  222,   81,  103,
  104,  105,  106,   62,   44,   83,   81,  127,   45,  137,
   88,  223,   30,   83,  153,   31,  107,   88,   33,   34,
    3,    4,   89,   62,   62,  224,  101,  225,  226,   62,
  277,  128,  206,  108,   75,   35,  138,   61,  286,  194,
  195,  196,  197,   90,  158,  159,  160,  161,  127,   36,
   90,   37,   38,  137,   64,   47,   30,   61,   61,   31,
  152,  142,   76,   61,   61,  143,   64,  211,  212,  213,
  214,  133,  147,  119,  120,  121,  122,  123,  124,  125,
  126,  113,  142,   77,  215,  160,  161,  142,   78,   79,
   80,   81,   82,   83,   84,   49,   29,  234,  216,  145,
  217,  113,  113,  231,  145,  162,  238,  113,    3,    4,
   29,  186,  179,   48,  112,   30,   65,    5,   31,    6,
  163,   50,    7,   67,  164,    8,   63,  100,   52,   57,
  233,   10,  187,   11,  112,  112,   12,   41,  236,   42,
  112,  112,  114,   67,   67,  255,   63,   63,   55,   67,
   67,   67,   67,   67,   67,   13,   14,   15,   59,   51,
   16,   17,  114,  114,   18,   19,   20,   21,   22,   82,
   23,   84,    2,   52,   58,    3,    4,   24,   86,   51,
   61,   51,   51,   29,    5,   26,    6,  181,  251,    7,
  253,  184,    8,   52,    9,   52,   52,  237,   10,   77,
   11,   95,   67,   12,   78,   79,   80,   81,   82,   83,
   84,   68,   29,   69,   95,  193,   29,  158,  159,  160,
  161,   95,   13,   14,   15,   70,    2,   16,   17,    3,
    4,   18,   19,   20,   21,   22,   71,   23,    5,  118,
    6,   74,   72,    7,   24,   50,    8,   73,    9,  110,
  111,  112,   10,  113,   11,  280,   90,   12,  281,  118,
  118,   82,   83,   84,   92,  118,  118,  118,  118,  118,
  118,   80,   81,   82,   83,   84,   13,   14,   15,   94,
  162,   16,   17,    3,    4,   18,   19,   20,   21,   22,
   95,   23,    5,   68,    6,   96,   91,    7,   24,   97,
    8,  172,  251,  252,  253,   99,   10,   98,   11,  141,
  148,   12,  145,   68,   68,  146,  156,   84,  168,   68,
   68,   68,   68,   80,   68,   82,   83,   84,  169,   65,
   13,   14,   15,  174,  162,   16,   17,    3,    4,   18,
   19,   20,   21,   22,  175,   23,    5,  119,    6,   65,
   65,    7,   24,  257,    8,   65,   65,   65,   65,  178,
   10,  180,   11,  182,  185,   12,  189,  119,  119,  190,
  191,  198,  199,  119,  119,  119,  119,  249,  119,  251,
  252,  253,  200,  116,   13,   14,   15,  201,  162,   16,
   17,    3,    4,   18,   19,   20,   21,   22,  209,   23,
    5,   66,    6,  116,  116,    7,   24,  204,    8,  116,
  116,  116,  116,  207,   10,  208,   11,  210,  260,   12,
  229,   66,   66,  230,  232,  187,  235,   66,   66,  127,
   66,  256,  259,  270,  271,  117,  275,  272,   13,   14,
   15,  273,  274,   16,   17,  245,  253,   18,   19,   20,
   21,   22,  282,   23,  287,  117,  117,  278,   48,  289,
   24,  117,  117,   91,  117,  262,  263,  264,  265,  266,
  267,  268,  269,  239,  240,  241,  242,  117,   77,  288,
   84,   79,  128,   78,   79,   80,   81,   82,   83,   84,
  243,  261,  246,   93,  141,   86,  129,  247,  248,  249,
  250,  251,  252,  253,  246,  205,  192,  219,  254,  247,
  248,  249,  250,  251,  252,  253,   78,   79,   80,   81,
   82,   83,   84,  247,  248,  249,  250,  251,  252,  253,
   79,   80,   81,   82,   83,   84,  248,  249,  250,  251,
  252,  253,  249,  250,  251,  252,  253,  290,   32,  291,
  116,  203,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          1,
   67,    1,   46,   23,  260,   86,   15,   13,   17,  108,
  133,  290,   21,  235,  258,  259,  260,  261,  273,  258,
  259,  260,  261,  273,    8,  273,  281,  257,  260,  257,
  273,  275,  260,  281,  101,  263,  275,  280,  258,  259,
  260,  261,   48,  293,  294,  289,  325,  291,  292,  299,
  272,  281,  175,  292,   38,  275,  284,  273,  280,  158,
  159,  160,  161,  273,  295,  296,  297,  298,  257,  289,
  280,  291,  292,  257,  330,  260,  260,  293,  294,  263,
  100,   90,  273,  299,  300,   91,  330,  258,  259,  260,
  261,  280,   98,   77,   78,   79,   80,   81,   82,   83,
   84,  273,  323,  294,  275,  297,  298,  328,  299,  300,
  301,  302,  303,  304,  305,  273,  118,  198,  289,  323,
  291,  293,  294,  190,  328,  257,  207,  299,  260,  261,
  132,  290,  141,  286,  273,  260,  156,  269,  263,  271,
  272,  260,  274,  273,  276,  277,  273,  290,  260,  315,
  293,  283,  311,  285,  293,  294,  288,  260,  202,  262,
  299,  300,  273,  293,  294,  232,  293,  294,  260,  299,
  300,  301,  302,  303,  304,  307,  308,  309,  260,  273,
  312,  313,  293,  294,  316,  317,  318,  319,  320,  303,
  322,  305,  257,  273,  273,  260,  261,  329,  273,  293,
  260,  295,  296,  205,  269,  205,  271,  143,  303,  274,
  305,  147,  277,  293,  279,  295,  296,  282,  283,  294,
  285,  260,  328,  288,  299,  300,  301,  302,  303,  304,
  305,  257,  234,  257,  273,  293,  238,  295,  296,  297,
  298,  280,  307,  308,  309,  273,  257,  312,  313,  260,
  261,  316,  317,  318,  319,  320,  294,  322,  269,  273,
  271,  273,  270,  274,  329,  260,  277,  270,  279,  264,
  265,  266,  283,  268,  285,  290,  310,  288,  293,  293,
  294,  303,  304,  305,  314,  299,  300,  301,  302,  303,
  304,  301,  302,  303,  304,  305,  307,  308,  309,  273,
  257,  312,  313,  260,  261,  316,  317,  318,  319,  320,
  273,  322,  269,  273,  271,  258,  310,  274,  329,  273,
  277,  278,  303,  304,  305,  321,  283,  325,  285,  280,
  260,  288,  273,  293,  294,  273,  292,  305,  273,  299,
  300,  301,  302,  301,  304,  303,  304,  305,  280,  273,
  307,  308,  309,  273,  257,  312,  313,  260,  261,  316,
  317,  318,  319,  320,  281,  322,  269,  273,  271,  293,
  294,  274,  329,  276,  277,  299,  300,  301,  302,  273,
  283,  273,  285,  260,  294,  288,  270,  293,  294,  328,
  323,  273,  273,  299,  300,  301,  302,  301,  304,  303,
  304,  305,  257,  273,  307,  308,  309,  260,  257,  312,
  313,  260,  261,  316,  317,  318,  319,  320,  294,  322,
  269,  273,  271,  293,  294,  274,  329,  273,  277,  299,
  300,  301,  302,  273,  283,  273,  285,  273,  287,  288,
  273,  293,  294,  260,  324,  311,  294,  299,  300,  257,
  302,  326,  273,  273,  270,  273,  260,  292,  307,  308,
  309,  273,  273,  312,  313,  226,  305,  316,  317,  318,
  319,  320,  330,  322,  273,  293,  294,  327,  273,  294,
  329,  299,  300,  273,  302,  246,  247,  248,  249,  250,
  251,  252,  253,  258,  259,  260,  261,  293,  294,  299,
  273,  273,  273,  299,  300,  301,  302,  303,  304,  305,
  275,  293,  294,  273,  328,  273,  273,  299,  300,  301,
  302,  303,  304,  305,  294,  174,  156,  186,  231,  299,
  300,  301,  302,  303,  304,  305,  299,  300,  301,  302,
  303,  304,  305,  299,  300,  301,  302,  303,  304,  305,
  300,  301,  302,  303,  304,  305,  300,  301,  302,  303,
  304,  305,  301,  302,  303,  304,  305,  288,    5,  289,
   73,  169,
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
null,null,null,"NL","NUMINT","NUMFLT","NAME","FIELD","CALLPROG","TABLE","TYPEC",
"TYPEI","TYPEF","TYPED","TYPEB","DIM","AS","IF","ELSE","POINT","CALL","STRING",
"ENDIF","WHILE","ENDWHILE","SUB","IN","OUT","ENDSUB","TYPE","ENDTYPE","FOR",
"EACH","ENDFOR","BREAK","TRUE","COMMA","FALSE","LPAR","RPAR","EQ","PLUS",
"MINUS","DIV","TIMES","AND","OR","LT","LTE","GT","GTE","NEQ","COMMENT","APPEND",
"READ","DELETE","TO","WHERE","LOAD","TBFIRST","KEY","SCREEN","GOTO","BRANCH",
"NATIVECALL","MODIFY","UPDATE","SET","SELECT","INNER","JOIN","FROM","VALUES",
"ON","INTO","INSERT","NAMESQLJ",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"line : NL",
"line : stmt NL",
"line : stmt2 NL",
"input2 :",
"input2 : input2 line2",
"line2 : NL",
"line2 : stmt NL",
"stmt : DIM declvar POINT",
"stmt : TYPE NAME declvars ENDTYPE POINT",
"stmt : BREAK POINT",
"stmt : attrib POINT",
"stmt : WHILE log POINT input2 ENDWHILE POINT",
"stmt : IF log POINT input2 ENDIF POINT",
"stmt : IF log POINT input2 ELSE POINT input2 ENDIF POINT",
"stmt : TBFIRST nametab POINT",
"stmt : FOR EACH namestru IN nametab POINT input2 ENDFOR POINT",
"stmt : CALL namefunc2 ml callparam POINT",
"stmt : APPEND namestru TO nametab POINT",
"stmt : READ nametab TO namestru logkey POINT",
"stmt : DELETE nametab logkey POINT",
"stmt : LOAD NAME POINT",
"stmt : GOTO SCREEN NUMINT POINT",
"stmt : BRANCH POINT",
"stmt : SELECT sqlfds FROM sqljoin INTO sqltb sqlwhere POINT",
"stmt : INSERT INTO sqltb LPAR sqlfds RPAR VALUES LPAR sqlvls RPAR POINT",
"stmt : UPDATE sqltb SET sqlpairs sqlwhere POINT",
"stmt : MODIFY nametab FROM namestru logkey POINT",
"stmt : NATIVECALL NAME POINT",
"stmt2 : SUB namefunc ml parameters POINT input ENDSUB POINT",
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
"namestru : NAME",
"nametab : NAME",
"namefunc : NAME",
"namefunc2 : NAME",
"namefunc2 : CALLPROG",
"attrib : var EQ exp",
"var : NAME",
"var : FIELD",
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
"log : log OR log",
"log : log AND log",
"log : log EQ log",
"log : log NEQ log",
"log : log LT log",
"log : log LTE log",
"log : log GT log",
"log : log GTE log",
"log : LPAR log RPAR",
"log : TRUE",
"log : FALSE",
"log : NUMINT",
"log : NUMFLT",
"log : STRING",
"log : var",
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

//#line 294 "ParserBasic.y"

  public AYylex lexer;
  public editor editor1;
  
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


  public AParserAll(Reader r) {
    lexer = new AYylex(r, this);
  }


  public AParserAll(Reader r, editor ed) {
      lexer = new AYylex(r, this);
      editor1 = ed;
  }
  
  private void lg(String s){
        System.out.println(s);
  }

  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex Calculator Demo");

    AParserAll yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new AParserAll(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Expression: ");
      interactive = true;
	    yyparser = new AParserAll(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    
    if (interactive) {
      System.out.println();
      System.out.println("Have a nice day");
    }
    
  }

/*
   Exemplo de select
   SELECT DSD,FSDF FROM ( TB1 AS AV INNER JOIN TB2 AS BV ON CE = RE AND DSD = GFG ) INTO FDF WHERE FDFD = "VGGFG".
   insert bancotb1 (f1,f2,f3) values ("val1",12,var1).
   update tb1 set f1="val", f2=231, f3=var1 where chave1="renato" and chave2 = var2.
   select f1,f2,f3 from ( ( tb1 as al1 inner join tb2 as al2 on af1 = bf2 ) inner join tb3 as al3 on bf3 = cf1 ) into tbvar1.
   select a~f1 from tb1 into tbva1.
*/
//#line 691 "Parser.java"
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
public int yyparse()
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
            errposition = lexer.getTextPosition();
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
//#line 41 "ParserBasic.y"
{  }
break;
case 4:
//#line 42 "ParserBasic.y"
{  System.out.println("line");}
break;
case 11:
//#line 55 "ParserBasic.y"
{ System.out.println("DIM");       }
break;
case 12:
//#line 56 "ParserBasic.y"
{ System.out.println("BREAK");     }
break;
case 13:
//#line 57 "ParserBasic.y"
{ System.out.println("ATTRIB");    }
break;
case 14:
//#line 58 "ParserBasic.y"
{ System.out.println("WHILE");     }
break;
case 15:
//#line 59 "ParserBasic.y"
{ System.out.println("IF S/ELSE"); }
break;
case 16:
//#line 60 "ParserBasic.y"
{ System.out.println("IF C/ELSE"); }
break;
case 17:
//#line 61 "ParserBasic.y"
{ System.out.println("TABLEFIRST");}
break;
case 18:
//#line 62 "ParserBasic.y"
{ System.out.println("FOREACH");   }
break;
case 19:
//#line 63 "ParserBasic.y"
{ System.out.println("CALL");      }
break;
case 20:
//#line 64 "ParserBasic.y"
{ System.out.println("APPEND");    }
break;
case 21:
//#line 65 "ParserBasic.y"
{ System.out.println("READ");      }
break;
case 22:
//#line 66 "ParserBasic.y"
{ System.out.println("DELETE");    }
break;
case 23:
//#line 67 "ParserBasic.y"
{ System.out.println("LOAD");      }
break;
case 24:
//#line 68 "ParserBasic.y"
{ System.out.println("GOTOSCR");   }
break;
case 25:
//#line 69 "ParserBasic.y"
{ System.out.println("BRANCH");    }
break;
case 26:
//#line 70 "ParserBasic.y"
{ System.out.println("SELECT " + val_peek(6).sval + " FROM " + val_peek(4).sval + " INTO " + val_peek(2).sval + val_peek(1).sval + "."); }
break;
case 27:
//#line 71 "ParserBasic.y"
{ System.out.println("INSERT"); }
break;
case 28:
//#line 72 "ParserBasic.y"
{ System.out.println("UPDATE"); }
break;
case 29:
//#line 73 "ParserBasic.y"
{ System.out.println("MODIFY"); }
break;
case 30:
//#line 74 "ParserBasic.y"
{ System.out.println("NATIVECALL"); }
break;
case 32:
//#line 80 "ParserBasic.y"
{ System.out.println("declvar");}
break;
case 49:
//#line 115 "ParserBasic.y"
{ System.out.println("NAME"); }
break;
case 97:
//#line 199 "ParserBasic.y"
{  yyval.sval = val_peek(0).sval; }
break;
case 98:
//#line 200 "ParserBasic.y"
{  yyval.sval = val_peek(0).var.toString(); }
break;
case 99:
//#line 201 "ParserBasic.y"
{  yyval.sval = val_peek(0).var.toString(); }
break;
case 100:
//#line 202 "ParserBasic.y"
{  yyval.sval = val_peek(0).var.toString(); }
break;
case 101:
//#line 203 "ParserBasic.y"
{  yyval.sval = val_peek(0).sval; }
break;
case 104:
//#line 210 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 105:
//#line 211 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 106:
//#line 214 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 107:
//#line 215 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;  }
break;
case 108:
//#line 218 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 109:
//#line 219 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;  }
break;
case 110:
//#line 222 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 111:
//#line 225 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 112:
//#line 229 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " or " + val_peek(0).sval; }
break;
case 113:
//#line 230 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " and " + val_peek(0).sval; }
break;
case 114:
//#line 231 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " = " + val_peek(0).sval; }
break;
case 115:
//#line 232 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " <> " + val_peek(0).sval; }
break;
case 116:
//#line 233 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " < " + val_peek(0).sval; }
break;
case 117:
//#line 234 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " <= " + val_peek(0).sval; }
break;
case 118:
//#line 235 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " > " + val_peek(0).sval; }
break;
case 119:
//#line 236 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " >= " + val_peek(0).sval; }
break;
case 120:
//#line 237 "ParserBasic.y"
{ yyval.sval = " (" + val_peek(1).sval +")"; }
break;
case 121:
//#line 238 "ParserBasic.y"
{ yyval.sval = " true "; }
break;
case 122:
//#line 239 "ParserBasic.y"
{ yyval.sval = " false "; }
break;
case 123:
//#line 240 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 124:
//#line 241 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 125:
//#line 242 "ParserBasic.y"
{ yyval.sval = " " + val_peek(0).var.toString() + " "; }
break;
case 126:
//#line 243 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).sval + "' "; }
break;
case 127:
//#line 244 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 128:
//#line 247 "ParserBasic.y"
{ yyval.sval = " "; }
break;
case 129:
//#line 248 "ParserBasic.y"
{ yyval.sval = " where " + val_peek(0).sval; }
break;
case 130:
//#line 251 "ParserBasic.y"
{ yyval.sval = " true "; }
break;
case 131:
//#line 252 "ParserBasic.y"
{ yyval.sval = " false "; }
break;
case 132:
//#line 253 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 133:
//#line 254 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 134:
//#line 255 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).var.toString() + "' "; }
break;
case 135:
//#line 256 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).sval + "' "; }
break;
case 136:
//#line 257 "ParserBasic.y"
{ yyval.sval = " '" + val_peek(0).sval + "' "; }
break;
case 137:
//#line 260 "ParserBasic.y"
{ yyval.sval = " " + val_peek(0).sval + " "; }
break;
case 138:
//#line 263 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " eq " + val_peek(0).sval; }
break;
case 139:
//#line 266 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 140:
//#line 267 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;  }
break;
case 141:
//#line 272 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 142:
//#line 275 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 143:
//#line 276 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " as " + val_peek(0).sval; }
break;
case 144:
//#line 277 "ParserBasic.y"
{ yyval.sval = " " + val_peek(6).sval + " inner join " + val_peek(3).sval + " as " + val_peek(1).sval + val_peek(0).sval + " "; }
break;
case 145:
//#line 280 "ParserBasic.y"
{ yyval.sval = " on " + val_peek(0).sval; }
break;
case 146:
//#line 283 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 147:
//#line 284 "ParserBasic.y"
{ yyval.sval = val_peek(2).sval + " and " + val_peek(0).sval; }
break;
case 148:
//#line 287 "ParserBasic.y"
{ yyval.sval = "" + val_peek(2).sval + " = " + val_peek(0).sval; }
break;
case 149:
//#line 290 "ParserBasic.y"
{ yyval.sval = val_peek(0).sval; }
break;
//#line 1140 "Parser.java"
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
public AParserAll()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public AParserAll(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
