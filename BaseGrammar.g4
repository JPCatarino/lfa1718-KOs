grammar BaseGrammar;
import BaseLexerRules, Unidades;


// Instructions must end with ';'
// Instructions may or may not be separated by '\n' character
// Accepts empty lines
main: (stat ('\n')*)* EOF;
stat:
    loop
    | instruction ';'
    ;

// General intruction
instruction:
    // Print/Read variable
    COMMAND '(' NAME ')'
    // Value atribution to variable
    // (This also accepts values that are not the result of an operation)
    | NAME '=' operation
    ;

/* -------------
 * LOOPS SECTION
 * -------------
 */
loop:
    // FOR LOOP
    FOR '(' instruction ';' condition ';' instruction ')' '{' (stat ('\n')*)* '}'
    // WHILE LOOP
    | WHILE '(' condition ')' '{' (stat ('\n')*)* '}'
    // DO-WHILE LOOP
    | DO '{' (stat ('\n')*)* '}' WHILE '(' condition ')'
    ;

/* ------------------
 * OPERATIONS SECTION
 * ------------------
 */

// Operations
operation:
    '(' n=operation ')'
    | left=operation NUMERIC_OPERATOR right=operation
    | NAME
    | value
    ;

// Conditions
condition:
    (value|NAME) CONDITIONAL_OPERATOR (value|NAME);

// Value
value: SIGNAL? (INT|REAL) pow? UNIT?;

// Equivalent to "*10^"
pow: 'e' SIGNAL? (INT|REAL);