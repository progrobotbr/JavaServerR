%{
  import java.io.*;
  import data.Variant;
  import lang.vm.vm;
  import lang.vm.type.decltype;
  import lang.vm.type.decltypeelement;
  import lang.vm.type.parameterPair;
  import lang.vm.type.parameterPairs;
%}
      
%start input

%token NUMINT 
%token NUMFLT 
%token NL NAME FIELD TABLE TYPEC TYPEI TYPEF TYPED TYPEB DIM AS IF ELSE POINT CALL STRING
%token ENDIF WHILE ENDWHILE SUB IN OUT ENDSUB TYPE ENDTYPE FOR EACH ENDFOR BREAK TRUE 
%token FALSE LPAR RPAR EQ PLUS MINUS DIV TIMES AND OR LT LTE GT GTE NEQ COMMENT       
%token APPEND READ DELETE TO WHERE LOAD KEY SCREEN GOTO BRANCH
%token CALLPROG TBFIRST NATIVECALL MODIFY
%token UPDATE SET SELECT INNER JOIN FROM VALUES ON INTO INSERT NAMESQLJ COMMA

%type <declt> declvars
%type <decl>  declvar1
%type <decl>  declvar
%type <sval>  NAME
%type <sval>  CALLPROG
%type <sval>  TABLE
%type <decl>  type
%type <sval>  TYPEB
%type <sval>  TYPEC
%type <sval>  TYPEI
%type <sval>  TYPEF
%type <decl>  namestru
%type <decl>  var
%type <var>   exp
%type <sval>  FIELD
%type <var>  NUMINT
%type <var>  NUMFLT
%type <var>  STRING
%type <var>   cmp
%type <var>   log
%type <var>   cmpterm
%type <sval>  namefile
%type <sval>  nametab
%type <sval>  namefunc
%type <sval>  namefunc2
%type <pair>  callatrib
%type <pairs> callatribs
%type <var>   nameconst
%type <pair>  logtb
%type <pairs> logtbs
%type <pairs> logkey

%type <sval> sqlname sqlfds sqlvls  sqltb sqlvtb sqlfield sqlpair sqlvar  
%type <sval> sqlpairs sqlwhere sqljoin sqljoininner sqltb sqlon sqlonpars sqlonpar
%type <sval> sqlfieldon sqllog NAMESQLJ TRUE FALSE
%type <sval> OR AND EQ NEQ LT LTE GT GTE

%left MINUS PLUS
%left TIMES DIV

%nonassoc ELSE

%left EQ
%left AND
%left OR
%left LTE
%left LT
%left GTE
%left GT
%left NEQ

%%

input:   /* Linha vazia */
       | input line
       ;
      
line:    NL       {  }
       | stmt NL  {  lg("line");}
       ;
       
stmt:    DIM declvar POINT                        { lg("DIM");      vmc.createVar($2);              }
       | TYPE NAME declvars ENDTYPE POINT         { lg("TYPE");     $3.Name=$2; vmc.putType($2,$3); }
       | BREAK POINT                              { lg("BREAK");                                    }
       | attrib POINT                             { lg("ATTRIB");                                   }
       | WHILE log POINT                          { lg("WHILE");    vmc.whileCmd($2);               }
       | ENDWHILE POINT                           { lg("ENDWHILE"); vmc.gotoCmd();                  }
       | IF log POINT                             { lg("IF");       vmc.ifCmd($2);                  }
       | ELSE POINT                               { lg("ELSE");                                     }
       | ENDIF POINT                              { lg("ENDIF");                                    }
       | TBFIRST nametab POINT                    { lg("TBFIRST");  vmc.tableFirst($2);             }
       | FOR EACH namestru IN nametab POINT       { lg("FOREACH");  vmc.forEach($3,$5);             }
       | ENDFOR POINT                             { lg("ENDFOR");   vmc.gotoCmd();                  }
       | SUB namefunc ml parameters POINT         { lg("SUB");      vmc.createMetaSub($2);          }
       | ENDSUB POINT                             { lg("ENDSUB");   vmc.endSub();                   }
       | CALL namefunc2 ml callparam POINT        { lg("CALL");     vmc.callSub($2);                }
       | APPEND namestru TO nametab POINT         { lg("APPEND");   vmc.appendTable($2,$4);         }
       | READ nametab TO namestru logkey POINT    { lg("READ");     vmc.readTable($4,$2,$5);        }
       | DELETE nametab logkey POINT              { lg("DELETE");   vmc.deleteTable($2,$3);         }
       | LOAD namefile POINT                      { lg("LOAD");     vmc.loadProg($2);               }
       | GOTO SCREEN NUMINT POINT                 { lg("GOTOSCR");  vmc.gotoScreen($3);             }
       | BRANCH POINT                             { lg("BRANCH");   vmc.gotoCmd();                  }
       | SELECT sqlfds FROM sqljoin INTO sqltb sqlwhere POINT             { lg("SELECT"); vmc.sqlSelect("select " + $2 + " FROM " + $4 + $7, $6); }
       | INSERT INTO sqltb LPAR sqlfds RPAR VALUES LPAR sqlvls RPAR POINT { lg("INSERT"); vmc.sqlInsert("insert into " + $3 + "( " + $5 + " ) values ( " + $9 + " )"); }
       | UPDATE sqltb SET sqlpairs sqlwhere POINT   { lg("UPDATE"); vmc.sqlUpdate("update " + $2 + " set " + $4 + $5); }
       | MODIFY nametab FROM namestru logkey POINT  { lg("MODIFY"); vmc.modifyTable($2,$4,$5); }
       | NATIVECALL NAME POINT                      { lg("NATIVECALL"); vmc.nativeCall($2); }
       ;
         
declvar:   NAME  AS type { $3.sNome = $1; $$ = $3; }
         | TABLE AS type { $3.sNome = $1; $$ = $3; }
         ;
         
declvars:  { $$ = null; }
          | declvars declvar1 { if($1==null) { $1 = new decltype(); }; $1.addElement($2); $$=$1; }
          ;
          
declvar1:  NL { $$ = null; } 
         | declvar { $$ = $1; lg("r");}
         ;
          
type :   TYPEC    { $$ = new decltypeelement("",$1,Variant.STRING);    } 
       | TYPEI    { $$ = new decltypeelement("",$1,Variant.INT);       }
       | TYPEF    { $$ = new decltypeelement("",$1,Variant.FLOAT);     }
       | TYPEB    { $$ = new decltypeelement("",$1,Variant.BOOL);      }
       | namestru { $$ = $1;                                           }
       ;
       
namefile: NAME
          ;
          
namestru: NAME    { $$ = new decltypeelement("",$1, Variant.STRUCTURE); }
          ; 

nametab:  NAME
          ;
         
namefunc  : NAME
          ; 

namefunc2:  NAME 
          | CALLPROG 
          ;                

var :   NAME   { $$ = new decltypeelement($1,"",Variant.NAME);  }
      | FIELD  { $$ = new decltypeelement($1,"",Variant.FIELD); }
      ;

attrib : var EQ exp { vmc.move2NameOrField($1.sNome,$3,$1.iType); }
         ;         

exp:    exp PLUS exp   { $$ = vmc.calc('+', $1, $3); }
      | exp MINUS exp  { $$ = vmc.calc('-', $1, $3); }
      | exp TIMES exp  { $$ = vmc.calc('*', $1, $3); }
      | exp DIV exp    { $$ = vmc.calc('/', $1, $3); }
      | LPAR exp RPAR  { $$ = $2;                    }
      | NUMINT         { $$ = $1;                    }
      | NUMFLT         { $$ = $1;                    }
      | NAME           { $$ = vmc.load($1);          }
      | FIELD          { $$ = vmc.loadField($1);     }
      | STRING         { $$ = $1;                    }
      ;      
 
log :   cmp            { $$ = $1;                    }
      | log OR  cmp    { $$ = vmc.bool('O',$1,$3);   }
      | log AND cmp    { $$ = vmc.bool('A',$1,$3);   }
      ;
      
cmp:   cmpterm            { $$ = $1;                    }
     | cmp EQ  cmpterm    { $$ = vmc.bool('=',$1,$3);   }
     | cmp NEQ cmpterm    { $$ = vmc.bool('D',$1,$3);   }
     | cmp LT  cmpterm    { $$ = vmc.bool('<',$1,$3);   }
     | cmp LTE cmpterm    { $$ = vmc.bool('N',$1,$3);   }
     | cmp GT  cmpterm    { $$ = vmc.bool('>',$1,$3);   }
     | cmp GTE cmpterm    { $$ = vmc.bool('M',$1,$3);   }
     ;
     
cmpterm:   TRUE           { $$ = new Variant(true);     } 
         | FALSE          { $$ = new Variant(false);    }
         | STRING         { $$ = $1;                    }
         | NUMINT         { $$ = $1;                    }
         | NUMFLT         { $$ = $1;                    }
         | var            { $$ = vmc.loadNameOrField($1.sNome, $1.iType); }
         | LPAR log RPAR  { $$ = $2;                    }   
         ;

logtb : NAME EQ nameconst { $$ = new parameterPair($1,$3); }
       ;
       
logtbs:                 { $$ = new parameterPairs(); }
        | logtbs logtb  { $1.add($2); $$ = $1; }
        ;
        
logkey: KEY logtbs { $$ = $2; } 
        ;

parameters: funcins funcouts
            ;
funcins:         { vmc.setNullSubDeclIn(); }
        | funcin { }
        ;

funcin: IN declvars { vmc.putSubDeclIn($2); }
        ;
        
funcouts:          { vmc.setNullSubDeclOut(); } 
         | funcout { }
         ;

funcout: OUT declvars { vmc.putSubDeclOut($2); }
         ;
         
callparam: callouts callins
	   ;

callouts:          { vmc.setNullParameterOut();}
         | callout
         ;

callout: OUT callatribs { vmc.setParameterOut($2); } 
         ;
         
callins:          { vmc.setNullParameterIn();}
         | callin
         ;
         
callin: IN callatribs { vmc.setParameterIn($2); } 
        ;

callatribs:                           { $$ = new parameterPairs(); }
            | callatribs callatrib ml { $1.add($2); $$ = $1;       }
            ;
            
callatrib: NAME EQ nameconst { $$ = new parameterPair($1,$3); }
           ;
           
nameconst :   NAME    { $$ = vmc.getVariantReference($1); }
            | STRING  { $$ = $1;                          }
            | NUMINT  { $$ = $1;                          }
            | NUMFLT  { $$ = $1;                          }
            | FIELD   { $$ = vmc.getVariantReference($1); }
            ;
           
ml: 
    | ml NL
    ;
 
sqlname:   NAME { $$ = vmc.getSqlValField($1); }
         | NAMESQLJ { $$ = $1; }
         ;
         
sqlfds:  sqlname { $$ = ""+$1+""; }
        | sqlfds COMMA sqlname { $$ = $1 + "," + $3 +"";  }
        ;

sqlvls:   nameconst { $$ = $1.getString(); }
        | sqlvls COMMA nameconst { $$ = $1 + "," + $3.getString();  }
        ;
       
sqltb : NAME { $$ = $1; }
        ;
        
sqlvtb : NAME { $$ = $1; }
         ;
         
sqllog :   sqllog OR  sqllog  { $$ = $1 + " or " + $3;            } 
         | sqllog AND sqllog  { $$ = $1 + " and " + $3;           } 
         | sqllog EQ  sqllog  { $$ = $1 + " = " + $3;             } 
         | sqllog NEQ sqllog  { $$ = $1 + " <> " + $3;            } 
         | sqllog LT  sqllog  { $$ = $1 + " < " + $3;             } 
         | sqllog LTE sqllog  { $$ = $1 + " <= " + $3;            } 
         | sqllog GT  sqllog  { $$ = $1 + " > " + $3;             } 
         | sqllog GTE sqllog  { $$ = $1 + " >= " + $3;            } 
         | LPAR sqllog RPAR   { $$ = " (" + $2 +")";              } 
         | TRUE               { $$ = " true ";                    }
	 | FALSE              { $$ = " false ";                   }
	 | NUMINT             { $$ = " '" + $1.toString() + "' "; }
	 | NUMFLT             { $$ = " '" + $1.toString() + "' "; }
	 | STRING             { $$ = " '" + $1.toString() + "' "; }
	 | FIELD              { $$ = " '" + $1 + "' ";            }
	 | sqlname            { $$ = $1;                          }
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

sqlpair : sqlfield EQ sqlvar { $$ = $1 + " = " + $3; }
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
