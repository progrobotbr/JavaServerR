%{
  import java.io.*;
%}
      
%start input

%token NL NUMINT NUMFLT NAME FIELD CALLPROG TABLE TYPEC TYPEI TYPEF TYPED TYPEB DIM AS IF ELSE POINT CALL STRING
%token ENDIF WHILE ENDWHILE SUB IN OUT ENDSUB TYPE ENDTYPE FOR EACH ENDFOR BREAK TRUE COMMA
%token FALSE LPAR RPAR EQ PLUS MINUS DIV TIMES AND OR LT LTE GT GTE NEQ COMMENT  
%token APPEND READ DELETE TO WHERE LOAD TBFIRST KEY SCREEN GOTO BRANCH NATIVECALL MODIFY
%token UPDATE SET SELECT INNER JOIN FROM VALUES ON INTO INSERT NAMESQLJ

%left MINUS PLUS
%left TIMES DIV

%left EQ
%left AND
%left OR
%left LTE
%left LT
%left GTE
%left GT
%left NEQ

%type <sval> sqllog TRUE FALSE
%type <sval> sqlname sqlfds sqlvls  sqltb sqlvtb sqlfield sqlpair sqlvar  
%type <sval> sqlpairs sqlwhere sqljoin sqljoininner sqltb sqlon sqlonpars sqlonpar
%type <sval> sqlfieldon 
%type <sval> OR AND EQ NEQ LT LTE GT GTE
%type <var> NUMFLT NUMINT STRING   
%type <sval> NAME NAMESQLJ FIELD nameconst
  
%nonassoc ELSE

%%

input:   /* Linha vazia */
       | input line
       ;
      
line:    NL       {  }
       | stmt NL  {  System.out.println("line");}
       | stmt2 NL
       ;
       
input2:
        | input2 line2
        ;
        
line2:   NL
       | stmt NL  
       ;

stmt:    DIM declvar POINT 
       | TYPE NAME declvars ENDTYPE POINT                         { System.out.println("DIM");       }
       | BREAK POINT                                              { System.out.println("BREAK");     }
       | attrib POINT                                             { System.out.println("ATTRIB");    }
       | WHILE log POINT input2 ENDWHILE POINT                    { System.out.println("WHILE");     }
       | IF log POINT input2 ENDIF POINT                          { System.out.println("IF S/ELSE"); }  
       | IF log POINT input2 ELSE POINT input2 ENDIF POINT        { System.out.println("IF C/ELSE"); } 
       | TBFIRST nametab POINT                                    { System.out.println("TABLEFIRST");} 
       | FOR EACH namestru IN nametab POINT input2 ENDFOR POINT   { System.out.println("FOREACH");   }
       | CALL namefunc2 ml callparam POINT                        { System.out.println("CALL");      }
       | APPEND namestru TO nametab POINT                         { System.out.println("APPEND");    }
       | READ nametab TO namestru logkey POINT                    { System.out.println("READ");      }
       | DELETE nametab logkey  POINT                             { System.out.println("DELETE");    }
       | LOAD NAME POINT                                          { System.out.println("LOAD");      }
       | GOTO SCREEN NUMINT POINT                                 { System.out.println("GOTOSCR");   }
       | BRANCH POINT                                             { System.out.println("BRANCH");    }
       | SELECT sqlfds FROM sqljoin INTO sqltb sqlwhere POINT        { System.out.println("SELECT " + $2 + " FROM " + $4 + " INTO " + $6 + $7 + "."); }
       | INSERT INTO sqltb LPAR sqlfds RPAR VALUES LPAR sqlvls RPAR POINT { System.out.println("INSERT"); }
       | UPDATE sqltb SET sqlpairs sqlwhere POINT                    { System.out.println("UPDATE"); }
       | MODIFY nametab FROM namestru logkey POINT                   { System.out.println("MODIFY"); } 
       | NATIVECALL NAME POINT                                       { System.out.println("NATIVECALL"); }
       ;

stmt2  : SUB namefunc ml parameters POINT input ENDSUB POINT
         ;
         
declvar:   NAME AS type { System.out.println("declvar");}
         | TABLE AS type 
         ;
         
declvars:  
          | declvars declvar1
          ;
          
declvar1:  NL
         | declvar
         ;
          
type :   TYPEC 
       | TYPEI 
       | TYPEF 
       | TYPEB
       | namestru
       ;
       
namestru: NAME
          ; 

nametab: NAME
         ;
         
namefunc: NAME
          ;
          
namefunc2:  NAME 
          | CALLPROG 
          ;  
          
attrib : var EQ exp 
         ;         
         
var :   NAME  { System.out.println("NAME"); }
      | FIELD 
      ;
      
exp:    exp PLUS exp   
      | exp MINUS exp  
      | exp TIMES exp  
      | exp DIV exp    
      | LPAR exp RPAR  
      | NUMINT         
      | NUMFLT         
      | NAME           
      | FIELD 
      | STRING
      ;      
      
log :   log OR  log 
      | log AND log
      | log EQ  log
      | log NEQ log
      | log LT  log
      | log LTE log
      | log GT  log
      | log GTE log
      | LPAR log RPAR
      | TRUE 
      | FALSE 
      | NUMINT
      | NUMFLT
      | STRING
      | var  
      ;
      

logtb : NAME EQ nameconst 
       ;
       
logtbs:                 
        | logtbs logtb  
        ;
        
logkey: KEY logtbs 
        ;
        
parameters: funcins funcouts
            ;
funcins: 
        | funcin
        ;

funcin: IN declvars
        ;
        
funcouts:   
         | funcout
         ;

funcout: OUT declvars
         ;
         
callparam: callouts callins
	   ;

callouts:   
         | callout
         ;

callout: OUT callatribs
         ;
         
callins: 
         | callin
         ;
         
callin: IN callatribs
        ;

callatribs: 
            | callatribs callatrib ml
            ;

callatrib: NAME EQ nameconst 
           ;
           
nameconst :   NAME   {  $$ = $1; } 
            | STRING {  $$ = $1.toString(); }
            | NUMINT {  $$ = $1.toString(); } 
            | NUMFLT {  $$ = $1.toString(); } 
            | FIELD  {  $$ = $1; } 
            ;
            
ml: 
    | ml NL
    ;
 
sqlname:   NAME { $$ = $1; }
         | NAMESQLJ { $$ = $1; }
         ;
         
sqlfds:  sqlname { $$ = $1; }
        | sqlfds COMMA sqlname { $$ = $1 + "," + $3;  }
        ;

sqlvls:   nameconst { $$ = $1; }
        | sqlvls COMMA nameconst { $$ = $1 + "," + $3;  }
        ;
       
sqltb : NAME { $$ = $1; }
        ;
        
sqlvtb : NAME { $$ = $1; }
         ;

         
sqllog :   sqllog OR  sqllog  { $$ = $1 + " or " + $3; } 
         | sqllog AND sqllog  { $$ = $1 + " and " + $3; } 
         | sqllog EQ  sqllog  { $$ = $1 + " = " + $3; } 
         | sqllog NEQ sqllog  { $$ = $1 + " <> " + $3; } 
         | sqllog LT  sqllog  { $$ = $1 + " < " + $3; } 
         | sqllog LTE sqllog  { $$ = $1 + " <= " + $3; } 
         | sqllog GT  sqllog  { $$ = $1 + " > " + $3; } 
         | sqllog GTE sqllog  { $$ = $1 + " >= " + $3; } 
         | LPAR sqllog RPAR   { $$ = " (" + $2 +")"; } 
         | TRUE               { $$ = " true "; }
	 | FALSE              { $$ = " false "; }
	 | NUMINT             { $$ = " '" + $1.toString() + "' "; }
	 | NUMFLT             { $$ = " '" + $1.toString() + "' "; }
	 | STRING             { $$ = " " + $1.toString() + " "; }
	 | FIELD              { $$ = " '" + $1 + "' "; }
	 | sqlname            { $$ = $1; }
	 ;

sqlwhere:                 { $$ = " "; }
          | WHERE sqllog  { $$ = " where " + $2; }
          ;
    
sqlvar:   TRUE     { $$ = " true "; }
	| FALSE    { $$ = " false "; }
	| NUMINT   { $$ = " '" + $1.toString() + "' "; }
	| NUMFLT   { $$ = " '" + $1.toString() + "' "; }
	| STRING   { $$ = " '" + $1.toString() + "' "; }
	| NAME     { $$ = " '" + $1 + "' "; }
	| FIELD    { $$ = " '" + $1 + "' "; }
        ;
        
sqlfield: NAME { $$ = " " + $1 + " "; }
          ;

sqlpair : sqlfield EQ sqlvar { $$ = $1 + " eq " + $3; }
           ;

sqlpairs :   sqlpair { $$ = $1; }
           | sqlpairs COMMA sqlpair { $$ = $1 + "," + $3;  }
           ;
           


sqljoin : sqljoininner { $$ = $1; }
          ;
          
sqljoininner:  sqltb { $$ = $1; }
             | sqltb AS NAME { $$ = $1 + " as " + $3; }
             | sqljoininner INNER JOIN sqltb AS NAME sqlon { $$ = " " + $1 + " inner join " + $4 + " as " + $6 + $7 + " "; }
             ;
             
sqlon: ON sqlonpars { $$ = " on " + $2; }
       ;
       
sqlonpars:   sqlonpar { $$ = $1; }
           | sqlonpars AND sqlonpar { $$ = $1 + " and " + $3; }
           ;
           
sqlonpar: sqlfieldon EQ sqlfieldon { $$ = "" + $1 + " = " + $3; }
          ;
           
sqlfieldon : NAMESQLJ { $$ = $1; }
          ;
          
%%

  public Yylex lexer;
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


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }


  public Parser(Reader r, editor ed) {
      lexer = new AYylex(r, this);
      editor1 = ed;
  }
  
  private void lg(String s){
        System.out.println(s);
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

/*
   Exemplo de select
   SELECT DSD,FSDF FROM ( TB1 AS AV INNER JOIN TB2 AS BV ON CE = RE AND DSD = GFG ) INTO FDF WHERE FDFD = "VGGFG".
   insert bancotb1 (f1,f2,f3) values ("val1",12,var1).
   update tb1 set f1="val", f2=231, f3=var1 where chave1="renato" and chave2 = var2.
   select f1,f2,f3 from ( ( tb1 as al1 inner join tb2 as al2 on af1 = bf2 ) inner join tb3 as al3 on bf3 = cf1 ) into tbvar1.
   select a~f1 from tb1 into tbva1.
*/
