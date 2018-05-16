grammar Unidades;
import BaseLexerRules;

main: (test NEWLINE)* EOF;

test: (n = INT unit);

unit:
    UNIT                        #unitUNIT
    |den=unit ':' num=unit      #unitDiv
    |unit '**' INT              #unitPow
    ;


NEWLINE: '\r'? '\n';

/*TO DO: Esta gremática tem de aceitar a expressões completas como 2m * 2m ou 3g * 5(m^-1) e passar
para expressões tipo (2 * 2)m^2 e (3*5)g/m, em casos de subtração ou de adição terá de verificar as unidades
  e se forem diferentes avisar que a soma ou subtração é impossível. Ainda não sei o que devolver caso exista
  uma divisão de unidades iguais, mas aceito sugestões*/
