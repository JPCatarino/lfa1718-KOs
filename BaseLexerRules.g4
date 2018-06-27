lexer grammar BaseLexerRules;

// Accepted variable types
VARTYPE: 'void'
       | 'boolean'
       | 'int'
       | 'double'
       ;

/* -----------------
 * OPERATORS SECTION
 * -----------------
 */

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

// Ignore WhiteSpaces
WS:[ \t\r\n]+ -> skip;

NEWLINE: '\r'? '\n';