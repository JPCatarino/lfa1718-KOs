lexer grammar BaseLexerRules;

// Signal (negativa values)
SIGNAL: '-';

// Units used for dimensional analysis
UNIT: ('m'|'g'|'s'|'A'|'K'|'mol'|'cd');

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
BOOLEAN_OPERATOR:
    'and'
    | 'or'
    | 'xor'
    | 'nand'
    | 'nor'
    | 'xnor'
    ;

// Numeric Operators
NUMERIC_OPERATOR:
    OP01
    | OP02
    | OP03
    ;
OP01: '^';
OP02: '*'|'/';
OP03: '+'|'-';

// Conditional Operators
CONDITIONAL_OPERATOR:
    // Equal
    '=='
    // Greater (or equal) OR Smaller (or equal)
    | ('>'|'<') ('=')?
    ;

// Integer Value (int)
INT: [0-9]+;

// REAL Value (real/float)
REAL: INT ('.' INT)?;

// Variable
// (Must start with a letter and may have digits)
NAME: [a-zA-Z] [a-zA-Z_0-9]*;


STRING: [a-zA-Z];

// Ignore WhiteSpaces
WS: [ \t\r\n]+ -> skip;

NEWLINE: '\r'? '\n';