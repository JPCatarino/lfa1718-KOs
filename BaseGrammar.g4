grammar BaseGrammar;
import Unidades;

// Expressions must end with ';'
// Expressions may or may not be separated by '\n' character
main: (e';'('\n')?)* EOF;

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

// Valid operators
operator: '*'
        | '/'
        | '+'
        | '-'
        ;

// Operation
operation: value (operator value)*;

// Value
value: REAL UNIT;

// REAL Value (real/float)
REAL: INT ('.' INT)?;

// Integer Value (int)
INT: [0-9]+;

// Variable
// (Must start with a letter and may have digits)
VAR: [a-zA-Z_] [a-zA-Z_0-9]*;

// Ignore WhiteSpaces
WS: [ \t\r\n]+ -> skip;
