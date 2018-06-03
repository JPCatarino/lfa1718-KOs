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

create: 'create' 'unit' uname=unit 'named' STRING;

pow: 'raise' STRING 'to power of' INT;

compose:'compose' composedUnit 'named' STRING;

unit returns[String varName]:
      STRING                                          #unitUNIT
      ;

composedUnit returns[String varName]:
              STRING                                                   #cUnitName
              |'(' p=composedUnit ')'                                  #cUnitParents
              |left=composedUnit op=(':'|'*') right=composedUnit     #cUnitDivMult
              ;

WS: [ \t\r\n]+ -> skip;
NEWLINE: '\r'? '\n';


