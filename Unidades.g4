grammar Unidades;
import BaseLexerRules;

main: statList EOF;

statList: (stat? ';')*;

stat:create
    |use
    ;

create: 'create' 'unit' uname=NAME 'based on' unit 'with symbol' symbol=NAME;

use:'use' NAME;

unit:
    NAME                              #unitUNIT
    |num=unit (':'|'*') den=unit      #unitDivMult
    |unit '**' INT                    #unitPow
    ;


WS: [ \t\r\n]+ -> skip;
NEWLINE: '\r'? '\n';


