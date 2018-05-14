grammar BaseGrammar;
import Unidades;

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

// Valid operators
operator: op01
        | op02
        | op03
        ;

// Higher priority operator
op01: '^' ;

// Medium priority operator
op02: '*'
    | '/'
    ;

// Lower priority operator
op03: '+'
    | '-'
    ;

// Operation
operation:
         // Operation with parentheses
         (value operator)* '(' operation ')' ( operator '(' operation ')' )* (operator value)*
         // Higher priority operations (exponentiation)
         | (value (op02|op03) )* (value op01 value) (operator value)*
         // Medium priority operations (multiplication/division)
         | (value op03 )* (value op02 value) ((op02|op03) value)*
         // Lower priority operations (addition/subtraction) OR just a value
         | value (op03 value)*
         ;

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
