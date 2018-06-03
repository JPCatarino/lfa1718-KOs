grammar BaseGrammar;
import BaseLexerRules;
@header { import java.util.*;}
@parser::members {protected Map<String, String> symbolTable = new HashMap<String, String>();}

// Instructions must end with ';'
// Instructions may or may not be separated by '\n' character
// Accepts empty lines
main: statList EOF;

statList: (stat)*;

stat returns[String v]:
    loop
    |instruction ';' {$v = $instruction.v;}
    ;

// General intruction
instruction returns[String v]:
    // Print/Read variable
    COMMAND '(' NAME ')'                                    #print_readVar
    // Value atribution to variable
    // (This also accepts values that are not the result of an operation)
    | NAME '=' operation                                 #assignment
    // Operation without storing result or (most common) variable increment/decrement
    | operation                                          #soloOp
    ;

/* --------------------
 * CONDITIONALS SECTION
 * --------------------
 */
if_else:
    'if' '(' condition ')' (('{'statList'}')|stat?) ('else' (if_else|('{'statList'}')|stat?))?;

/* -------------
 * LOOPS SECTION
 * -------------
 */
loop:
    // FOR LOOP
    'for' '(' instruction ';' condition ';' instruction ')' '{' (stat ('\n')*)* '}'     #loopFor
    // WHILE LOOP
    | 'while' '(' condition ')' '{' (stat ('\n')*)* '}'                                 #loopWhile
    // DO-WHILE LOOP
    | 'do' '{' (stat ('\n')*)* '}' 'while' '(' condition ')'                            #loopDoWhile
    ;

/* ------------------
 * OPERATIONS SECTION
 * ------------------
 */
// Operations
operation returns[String v]:
    '(' n=operation ')'                                     #par
    | NAME '++'                                             #increment
    | NAME '--'                                             #decrement
    | left=operation NUMERIC_OPERATOR right=operation       #op
    | NAME                                                  #assignVar
    | value { $v = $value.text;}                            #val

    ;

// Conditions
condition:
    (value|NAME) CONDITIONAL_OPERATOR (value|NAME);

// Value
value: SIGNAL? (INT|REAL) pow? STRING?;

// Equivalent to "*10^"
pow: 'e' (SIGNAL? (INT|REAL));

WS: [ \t\r\n]+ -> skip;
