grammar BaseGrammar;
import BaseLexerRules, Unidades;
@header { import java.util.*;}
@members {protected Map<String, String> symbolTable = new HashMap<String, String>();}

// Instructions must end with ';'
// Instructions may or may not be separated by '\n' character
// Accepts empty lines
main: (stat ('\n')*)* EOF;
stat returns[String v]:
    loop
    | instruction {$v = $instruction.v;} ';'
    ;

// General intruction
instruction returns[String v]:
    // Print/Read variable
    COMMAND '(' NAME ')'                                    #print_readVar
    // Value atribution to variable
    // (This also accepts values that are not the result of an operation)
    | NAME '=' operation {$v = $operation.v; 
                          symbolTable.put($NAME.text, $v); 
                          System.out.println("assignment");} #assignment
    ;

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
    '(' n=operation ')'
    | left=operation NUMERIC_OPERATOR right=operation       #op
    | NAME { if(!symbolTable.containsKey($NAME.text)){ 
                System.out.println("Variable \""+$NAME.text+"\" does not exists!"); System.exit(1);
            }
            $v = symbolTable.get($NAME.text);
            }                   #assignVar
    | value { $v = $value.text;}        #val

    ;

// Conditions
condition:
    (value|NAME) CONDITIONAL_OPERATOR (value|NAME);

// Value
value: SIGNAL? (INT|REAL) pow? UNIT?;

// Equivalent to "*10^"
pow: 'e' (SIGNAL? (INT|REAL));