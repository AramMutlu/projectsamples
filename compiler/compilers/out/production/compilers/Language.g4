grammar Language;

program: statement* EOF;

statement:
    variableDecl
    | assignment
    | if_statement
    | while_statement
    | print_statement
    | expression
;

variableDecl: dataType=DATATYPE ID (EQUALS expr=expression)? SEMICOLON;

assignment: ID EQUALS expr=expression SEMICOLON;

if_statement:
    'if' PAR_OPEN expr=expression PAR_CLOSE '{'ifblock=block'}' ('else' '{'elseblock=block'}')?;  //('else if' PAR_OPEN expression PAR_CLOSE '{'block'}')*;

while_statement:
    'while' PAR_OPEN expression PAR_CLOSE '{'block'}' ;

block:  statement*;

print_statement:
    'print ' expr=expression SEMICOLON ;

expression:
    leftexpr=expression op=('+'|'-') rightexpr=expression SEMICOLON?                        #ExAddOp
    | leftexpr=expression op=('*'|'/') rightexpr=expression SEMICOLON?                      #ExMulDiv
    | leftexpr=expression op=('<'|'<='|'=='|'!='|'>'|'>=') rightexpr=expression SEMICOLON?  #ExLogicalBoolean
    | INT                                                                                   #ExIntLiteral
    | BOOLEAN                                                                               #ExBoolean
    | ID                                                                                    #ExID
    ;

/*
type:
    INT         #TypeInt
    | ID        #TypeID
    | BOOLEAN   #TypeBoolean
    ;
*/

EQUALS : '=';

PAR_OPEN : '(';
PAR_CLOSE : ')';
SEMICOLON : ';';

DATATYPE: ( 'int' | 'boolean');

BOOLEAN: 'true' | 'false';
ID: [a-zA-Z_][a-zA-Z0-9_]*;
INT: '0' | [1-9][0-9]*;

WHITESPACE: [ \t\r\n] -> skip;