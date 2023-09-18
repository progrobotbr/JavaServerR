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
       ;

stmt:  TYPE NAME declvars ENDTYPE 
       ;
      
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
