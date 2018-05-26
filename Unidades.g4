grammar Unidades;
import BaseLexerRules;

@parser::header {
import java.util.Map;
import java.util.HashMap;
}

@parser::members {
static protected Map<String,USymbol> symbolTable = new HashMap<>();
}

main: statList EOF;

statList: (stat? ';')*;

stat:create
    |pow
    |compose
    ;

create: 'create' 'unit' uname=unit 'named' NAME;

pow: 'raise' NAME 'to power of' INT;

compose:'compose' composedUnit 'named' NAME;

unit returns[String varName]:
      NAME                                          #unitUNIT
      ;

composedUnit returns[String varName]:
              NAME                                                   #cUnitName
              |'(' p=composedUnit ')'                                  #cUnitParents
              |left=composedUnit op=(':'|'*') right=composedUnit     #cUnitDivMult
              ;

WS: [ \t\r\n]+ -> skip;
NEWLINE: '\r'? '\n';


