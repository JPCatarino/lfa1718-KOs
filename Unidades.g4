grammar Unidades;
import BaseLexerRules;

main: (oper NEWLINE)* EOF;

oper: expr;
expr: '(' expr ')'
    | expr NUMERIC_OPERATOR expr
    | test  //num&unit
    ;
test: (n = INT unit); // ( INT unit ) ^2

unit:
    den=unit '/' (num=unit)+
    unit '^' INT
    |UNIT
    ;

NEWLINE: '\r'? '\n';

/*TO DO: Esta gremática tem de aceitar a expressões completas como 2m * 2m ou 3g * 5(m^-1) e passar
para expressões tipo (2 * 2)m^2 e (3*5)g/m, em casos de subtração ou de adição terá de verificar as unidades
  e se forem diferentes avisar que a soma ou subtração é impossível. Ainda não sei o que devolver caso exista
  uma divisão de unidades iguais, mas aceito sugestões*/
