grammar BaseGrammar;
import Unidades.g4;

// Expressions must end with ';'
// Expressions may or may not be separated by '\n' character
main: (e';'('\n')?)* EOF;

// General expression
e:
 // Print/Read variable
 command var
 // Value atribution to variable
 // (This also accepts values that are not the result of an operation)
 | var '=' operation
 ;

// Variable
// (Must start with a letter and may have digits)
var: [a-zA-Z]+([0-9]*[a-zA-Z]*)*;

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

// Ignore WhiteSpaces
WS: [ \t\r\n]+ -> skip;
