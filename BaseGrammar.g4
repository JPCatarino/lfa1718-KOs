grammar BaseGrammar;
import BaseLexerRules;
@parser::header {
import java.util.Map;
import java.util.HashMap;
}

@parser::members {static protected Map<String,VarAssign> symbolTable = new HashMap<>();
}

// Instructions must end with ';'
// Instructions may or may not be separated by '\n' character
// Accepts empty lines
main: statList EOF;

statList: (stat)*;

stat:
    loop
    |instruction
    ;

// Value
value:
    (INT|REAL) pow? NAME?
    | '(' '-' (INT|REAL) pow? NAME? ')'
    ;

// General intruction
instruction:
    // Variable declaration
    varType NAME                                        #varDec
    // Print/Read variable
    | COMMAND '(' NAME ')'                              #command
    // Value atribution to variable
    // (This also accepts values that are not the result of an operation)
    | NAME '=' operation                                #assignment
    // Operation without storing result or (most common) variable increment/decrement
    | operation                                         #soloOp
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
    'for' '(' var=NAME ';' min=INT ';' max=INT ')' '{' statList '}'              #loopFor
    // WHILE LOOP
    | 'while' '(' condition ')' '{' statList '}'                                 #loopWhile
    // DO-WHILE LOOP
    | 'do' '{' statList '}' 'while' '(' condition ')'                            #loopDoWhile
    ;

/* ------------------
 * OPERATIONS SECTION
 * ------------------
 */
// Operations
operation:
    '(' n=operation ')'                                     #par
    | NAME '++'                                             #increment
    | NAME '--'                                             #decrement
    | left=operation NUMERIC_OPERATOR right=operation       #op
    | NAME                                                  #assignVar
    | value                                                 #val

    ;

// Conditions
condition:
    left=conditionE CONDITIONAL_OPERATOR right=conditionE              #compare
    |conditionE                                                        #soloCond
    ;

conditionE:
    value           #condiEValue
    |NAME           #condiEVar
    ;

// Variable Types
varType:
    'simpVar'   #simple
    | 'unitVar' #unit
    ;

// Equivalent to "*10^"
pow: 'e' ('-')? (INT|REAL);

// Commands
COMMAND: 'Print';

NAME: [a-zA-Z] [a-zA-Z_0-9]*;
WS: [ \t\r\n]+ -> skip;
