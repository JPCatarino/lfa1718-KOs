lexer grammar BaseLexerRules;

// Signal (negativa values)
SIGNAL: '-';

// Units used for dimensional analysis
UNIT: ('m'|'g'|'s'|'A'|'K'|'mol'|'cd')+;

// Accepted variable types
VARTYPE: 'void'
       | 'boolean'
       | 'int'
       | 'double'
       ;

// Commands
COMMAND: 'Print'
       | 'Read'
       ;

/* -----------------
 * OPERATORS SECTION
 * -----------------
 */
// Boolean Operators
BOOLEAN_OPERATOR: 'and'
                | 'or'
                | 'xor'
                | 'nand'
                | 'nor'
                | 'xnor'
                ;

// Numeric Operators
NUMERIC_OPERATOR: OP01
                | OP02
                | OP03
                ;
OP01: '^';
OP02: '*'|'/';
OP03: '+'|'-';

// Integer Value (int)
INT: [0-9]+;

// REAL Value (real/float)
REAL: INT ('.' INT)?;

// Variable
// (Must start with a letter and may have digits)
NAME: [a-zA-Z_] [a-zA-Z_0-9]*;

// Ignore WhiteSpaces
WS: [ \t\r\n]+ -> skip;

NEWLINE: '\r'? '\n';