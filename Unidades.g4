grammar Unidades;
import BaseLexerRules;

@header{import java.util.*;}

test: unit{System.out.println($unit.res);} NEWLINE EOF;

unit returns[String res]:
    den=unit '/' (num=unit)+ {if($den.res.equals($num.res)){
                                $res = "1";
                              }
                              }
    |unit '^' INT            {$res = $unit.res + "^" + $INT.text;}
    |UNIT                    {$res = $UNIT.text;}
    ;


NEWLINE: '\r'? '\n';