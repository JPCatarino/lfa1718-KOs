grammar Unidades;
import BaseLexerRules;

main: statList EOF;

statList: (stat? ';')*;

stat:create
    |associate
    ;

create: 'create' 'unit' uname=unit 'named' NAME;

associate:'associate' NAME '->' NAME;

unit:
    NAME                              #unitUNIT
    |num=unit (':'|'*') den=unit      #unitDivMult
    |unit '**' INT                    #unitPow
    ;


WS: [ \t\r\n]+ -> skip;
NEWLINE: '\r'? '\n';


