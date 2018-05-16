grammar BaseGrammar;
import BaseLexerRules, Unidades;


// Instructions must end with ';'
// Instructions may or may not be separated by '\n' character
// Accepts empty lines
main: (stat ('\n')*)* EOF;
stat: instruction ';';

// General intruction
instruction:
    // Print/Read variable
    COMMAND '(' NAME ')'
    // Value atribution to variable
    // (This also accepts values that are not the result of an operation)
    | NAME '=' operation
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

// Value
value: SIGNAL? (INT|REAL) pow? UNIT?;

// Equivalent to "*10^"
pow: 'e' SIGNAL? (INT|REAL);