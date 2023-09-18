
%%

%byaccj

%{
  private Parser yyparser;

  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

NL       = \n | \r | \r\n
NUMINT   = [0-9]+ { yyparser.yylval = new ParserVal(Integer.parseInt(yytext())); }
NUMFLT   = [0-9]+ ("." [0-9]+)?
NAME     = [A-Z]+
FIELD    = [A-Z]+ "." [A-Z]+
TABLE    = [A-Z]+"[]"
TYPEC    = "C[" [0-9]+ "]" 
TYPEI    = "I"
TYPEF    = "F"
TYPED    = "D"
AS       = "AS"
IF       = "IF"
ELSE     = "ELSE"
ENDIF    = "ENDIF"     
WHILE    = "WHILE"        
ENDWHILE = "ENDWHILE"   
SUB      = "SUB"         
IN       = "IN"          
OUT      = "OUT"         
ENDSUB   = "ENDSUB"       
TYPE     = "TYPE"          
ENDTYPE  = "ENDTYPE"        
FOR      = "FOR"          
EACH     = "EACH"        
ENDFOR   = "ENDFOR" 
TRUE     = "TRUE"
FALSE    = "FALSE"
BREAK    = "BREAK"
LPAR     = "("
RPAR     = ")"
EQ       = "="
PLUS     = "+"
MINUS    = "-"
DIV      = "/"
TIMES    = "*"
COMMENT = "'"

%%

[ \t]+ { }
{NUMINT} { return Parser.NUMINT; }
{NUMFLT} { return Parser.NUMFLT; }
{NL}  { System.out.println("Enter");return Parser.NL; }
"DIM" { System.out.println("Dim");return Parser.DIM; }
{AS} { System.out.println("As");return Parser.AS; }
{IF} { System.out.println("If");return Parser.IF; }
{ELSE} { System.out.println("Else");return Parser.ELSE; }
{ENDIF} { System.out.println("Endif");return Parser.ENDIF; }
{WHILE} { System.out.println("While");return Parser.WHILE; }
{ENDWHILE} { return Parser.ENDWHILE; }
{SUB} { return Parser.SUB; }
{IN} { return Parser.IN; }
{OUT} { return Parser.OUT; } 
{ENDSUB} { return Parser.ENDSUB; } 
{TYPE} { return Parser.TYPE; }      
{ENDTYPE} { return Parser.ENDTYPE; }      
{FOR} { return Parser.FOR; }         
{EACH} { return Parser.EACH; }
{ENDFOR} { return Parser.ENDFOR; }
{BREAK} { return Parser.BREAK; }
{TRUE} { return Parser.TRUE; }
{FALSE} { return Parser.FALSE; }
{TABLE} { return Parser.TABLE; }
{TYPEC} { System.out.println("Type C");return Parser.TYPEC; }
{TYPEI} { System.out.println("Type I");return Parser.TYPEI; }
{TYPEF} { System.out.println("Type F");return Parser.TYPEF; }
{TYPED} { System.out.println("Type D");return Parser.TYPED; }
{FIELD} { System.out.println("Field");return Parser.FIELD; }
{NAME} { System.out.println("Name");return Parser.NAME; }
{LPAR} { return Parser.LPAR; }
{RPAR} { return Parser.RPAR; }
{EQ} { return Parser.EQ; }
{PLUS} { return Parser.PLUS; }
{MINUS} { return Parser.MINUS; }
{DIV} { return Parser.DIV; }
{TIMES} { return Parser.TIMES; }
"AND"  { return Parser.AND; }
"OR"  { return Parser.OR; }
"<" { return Parser.LT; }
"<=" { return Parser.LTE; }
">" { System.out.println(">");return Parser.GT; }
">=" { return Parser.GTE; }
"<>" { return Parser.NEQ; }
{COMMENT} { return Parser.COMMENT; }
.            { throw new Error("Unexpected character ["+yytext()+"]"); }
