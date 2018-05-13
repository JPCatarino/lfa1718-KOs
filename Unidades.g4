grammar Unidades;

unit:
    UNIT+ '/' UNIT+
    |UNIT+ '^' INT
    |UNIT+
    ;



INT: [0-9]+;
UNIT: 'm'|'kg'|'s'|'a'|'k'|'mol'|'cd';
WS: [ \t\r\n]+ -> skip;
