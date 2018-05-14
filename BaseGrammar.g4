grammar BaseGrammar;
import BaseLexerRules, Unidades;

// Expressions must end with ';'
// Expressions may or may not be separated by '\n' character
// Accepts empty lines
main: (e';'('\n')*)* EOF;

// General expression
e:
 // Print/Read variable
 command VAR
 // Value atribution to variable
 // (This also accepts values that are not the result of an operation)
 | VAR '=' operation
 ;



// Commands
command: 'Print'
       | 'Read'
       ;

operation:
    '(' n=operation ')'
    |left=operation op=('*'|'/') right=operation
    |left=operation op=('+'|'-') right=operation
    |value
    ;

// Value
value: REAL (pow?) unit;

signal: '-';

pow: 'e' (signal?) REAL;