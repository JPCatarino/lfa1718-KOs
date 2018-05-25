grammar Unidades;
import BaseLexerRules;

main: statList EOF;

statList: (stat? ';')*;

stat:create
    |pow
    |compose
    ;

create: 'create' 'unit' uname=unit 'named' NAME;

pow: 'unit' unit 'power of' INT;

compose:'compose' composedUnit;

unit returns[String varName]:
      NAME                                          #unitUNIT
      ;

composedUnit: left=NAME op=(':'|'*') right=NAME     #cUnitDivMult
              ;

WS: [ \t\r\n]+ -> skip;
NEWLINE: '\r'? '\n';


