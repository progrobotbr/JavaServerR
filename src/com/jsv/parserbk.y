%{
  import java.io.*;
%}
      
%start input

%token NUMINT 
%token NUMFLT 
%token NL NAME FIELD TABLE TYPEC TYPEI TYPEF TYPED DIM AS IF ELSE
%token ENDIF WHILE ENDWHILE SUB IN OUT ENDSUB TYPE ENDTYPE FOR EACH ENDFOR BREAK TRUE 
%token FALSE LPAR RPAR EQ PLUS MINUS DIV TIMES AND OR LT LTE GT GTE NEQ COMMENT       


%left '-' '+'
%left '*' '/'

%right '='
%right AND
%right OR

  
%nonassoc ELSE

%%

input:   /* Linha vazia */
       | input line
       ;
      
line:    NL       {  }
       | stmt NL  {  System.out.println("line");}
       | stmt2 NL 
       ;

stmt:   DIM declvar
      | TYPE NAME declvars ENDTYPE 
      | BREAK
      | IF log input2 ENDIF
      | IF log input2 ELSE input2 ENDIF
      | WHILE log input2 ENDWHILE
      | FOR EACH namestru IN nametab input2 ENDFOR
      | attrib
      ;
      
input2:
        | input2 line2
        ;
        
line2:   NL
       | stmt NL
       ;
       
stmt2  : SUB namefunc parameters input2 ENDSUB 

declvar:   NAME AS type { System.out.println("declvar");}
         | TABLE AS type 
         ;
       
declvar1:  NL
         | declvar
         ;
       
declvars:  
          | declvars declvar1
          ;

            
type :   TYPEC 
       | TYPEI { System.out.println("typei");}
       | TYPEF
       | namestru
       ;
       
namestru: NAME
          ;
       
nametab: NAME
         ;
         
namefunc: NAME
          ;
          
attrib : var EQ exp
         ;

log :   log1
      | log OR ml log1
      | log AND ml log1
      ;
     
log1 :   log2
       | log1 EQ ml log2 
       | log1 NEQ ml log2
       | log1 LT ml log2
       | log1 LTE ml log2
       | log1 GT ml log2
       | log1 GTE ml log2
      ;
      
log2:   log3
      | LPAR log ml RPAR
      ;
      
log3:   TRUE
      | FALSE
      | var { }
      ;

var :   NAME 
      | FIELD 
      ;
     
parameters: ml funcin ml funcout
            ;

ml : 
     | ml ml1
     ;
     
ml1 : NL
      ;
      
funcin: 
        | IN declvars
        ;

         
funcout: 
         | OUT declvars
         ;
      

attrib : NAME EQ exp { }
         ;
         
exp:     NUMINT         { }
       | NUMFLT
       | NAME
       | FIELD
       | exp PLUS exp   {  }
       | exp MINUS exp  {  }
       | exp TIMES exp  {  }
       | exp DIV exp    {  }
       | LPAR exp RPAR  {  }
       ;

%%

  public Yylex lexer;


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
