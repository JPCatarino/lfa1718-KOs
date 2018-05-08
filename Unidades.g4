grammar Unidades;

unit:
    UNIT
    |UNIT '^' INT
    |UNIT '/' UNIT
    ;



INT: [0-9]+;
UNIT: 'm'|'kg'|'s'|'a'|'k'|'mol'|'cd';
WS:  [ \t]+ -> skip;

