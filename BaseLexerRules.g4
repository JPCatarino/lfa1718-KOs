grammar BaseLexerRules;

// Units used for dimensional analysis
UNIT: ('m'|'kg'|'s'|'a'|'k'|'mol'|'cd')+;

// REAL Value (real/float)
REAL: INT ('.' INT)?;

// Integer Value (int)
INT: [0-9]+;

// Variable
// (Must start with a letter and may have digits)
VAR: [a-zA-Z_] [a-zA-Z_0-9]*;

// Ignore WhiteSpaces
WS: [ \t\r\n]+ -> skip;
