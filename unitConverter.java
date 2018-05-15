import java.util.Stack;

/*este listener tem um bug porque só atualiza o resultado quando sai de test, logo só ficamos com último
resultado, se alguém souber uma forma de resolver, todo o prazer
*/
public class unitConverter extends UnidadesBaseListener{
    String SimpUnit;
    String result;
    // implementação do resultado com stack é para efeitos puramente demonstrativos e de teste
    private Stack<String> stack = new Stack<String>();

    public String getResult(){return stack.pop();}

    public boolean isEmpty(){
        return stack.empty();
    }
    //junta o numero com a unidade
    @Override public void exitTest(UnidadesParser.TestContext ctx) {
         stack.push("(" + ctx.n.getText() + ")" + SimpUnit);
    }
    //faz a divisão, neste momento só verifica se 2 unidades são iguais, implementar o resto(e não é o da divisão)
    @Override public void exitUnitDiv(UnidadesParser.UnitDivContext ctx) {

        if(ctx.den.getText().equals(ctx.num.getText())){
            SimpUnit = "1";
        }
        else if (ctx.den.getText().equals("m") && ctx.num.getText().equals("s")) SimpUnit = "m/s";
        else if (ctx.den.getText().equals("A") && ctx.num.getText().equals("m")) SimpUnit = "A/m";
        else if (ctx.den.getText().equals("N") && ctx.num.getText().equals("m")) SimpUnit = "N/m";
        else if (ctx.den.getText().equals("rad/s") && ctx.num.getText().equals("s")) SimpUnit = "m/s";
        else if (ctx.den.getText().equals("J") && ctx.num.getText().equals("K")) SimpUnit = "J/K";
        else if (ctx.den.getText().equals("V") && ctx.num.getText().equals("m")) SimpUnit = "V/m";
        else if (ctx.den.getText().equals("J") && ctx.num.getText().equals("mol")) SimpUnit = "J/mol";
        else if (ctx.den.getText().equals("m") && ctx.num.getText().equals("s")) SimpUnit = "m/s";
        else if (ctx.den.getText().equals("m") && ctx.num.getText().equals("s^2")) SimpUnit = "m/s^2";
        else if (ctx.den.getText().equals("Kg") && ctx.num.getText().equals("m^3")) SimpUnit = "Kg/m^3";
        else if (ctx.den.getText().equals("m^3") && ctx.num.getText().equals("Kg")) SimpUnit = "m^3/Kg";
        else if (ctx.den.getText().equals("A") && ctx.num.getText().equals("m^2")) SimpUnit = "A/m^2";
        else if (ctx.den.getText().equals("mol") && ctx.num.getText().equals("m^3")) SimpUnit = "mol/m^3";
        else if (ctx.den.getText().equals("cd") && ctx.num.getText().equals("m^2")) SimpUnit = "cd/m^2";
        else if (ctx.den.getText().equals("m^3") && ctx.num.getText().equals("Kg")) SimpUnit = "m^3/Kg";
        else if (ctx.den.getText().equals("rad") && ctx.num.getText().equals("s^2")) SimpUnit = "rad/s^2";
        else if (ctx.den.getText().equals("W") && ctx.num.getText().equals("m^2")) SimpUnit = "W/m^2";
        else if (ctx.den.getText().equals("m^3") && ctx.num.getText().equals("Kg")) SimpUnit = "m^3/Kg";
        else if (ctx.den.getText().equals("m^3") && ctx.num.getText().equals("s")) SimpUnit = "m^3/s";
        else if (ctx.den.getText().equals("m") && ctx.num.getText().equals("s^3")) SimpUnit = "m/s^3";
        else if (ctx.den.getText().equals("m") && ctx.num.getText().equals("s^4") ) SimpUnit = "m/s^4";
        else if (ctx.den.getText().equals("J") && ctx.num.getText().equals("rad") ) SimpUnit = "J/rad";
        else if (ctx.den.getText().equals("N") && ctx.num.getText().equals("s") ) SimpUnit = "N/s";
        else if (ctx.den.getText().equals("Kg") && ctx.num.getText().equals("m^2") ) SimpUnit = "Kg/m^2";
        else if (ctx.den.getText().equals("m^3") && ctx.num.getText().equals("Kg") ) SimpUnit = "m^3/Kg";
        else if (ctx.den.getText().equals("mol") && ctx.num.getText().equals("m^3") ) SimpUnit = "m3/mol";
        else if (ctx.den.getText().equals("J") && ctx.num.getText().equals("Kg") ) SimpUnit = "J/Kg";
        else if (ctx.den.getText().equals("J") && ctx.num.getText().equals("m^3") ) SimpUnit = "J/m^3";
        else if (ctx.den.getText().equals("J") && ctx.num.getText().equals("m^2") ) SimpUnit = "J/m^2";
        else if (ctx.den.getText().equals("m^2") && ctx.num.getText().equals("s") ) SimpUnit = "m^2/s";
        else if (ctx.den.getText().equals("C") && ctx.num.getText().equals("m") ) SimpUnit = "C/m";
        else if (ctx.den.getText().equals("C") && ctx.num.getText().equals("m^2") ) SimpUnit = "C/m^2";
        else if (ctx.den.getText().equals("C") && ctx.num.getText().equals("m^3") ) SimpUnit = "C/m^3";
        else if (ctx.den.getText().equals("A") && ctx.num.getText().equals("m^2") ) SimpUnit = "A/m^2";
        else if (ctx.den.getText().equals("S") && ctx.num.getText().equals("m") ) SimpUnit = "S/m";
        else if (ctx.den.getText().equals("F") && ctx.num.getText().equals("m") ) SimpUnit = "F/m";
        else if (ctx.den.getText().equals("H") && ctx.num.getText().equals("m") ) SimpUnit = "H/m";
        else if (ctx.den.getText().equals("C") && ctx.num.getText().equals("Kg") ) SimpUnit = "C/Kg";
        else if (ctx.den.getText().equals("Gy/s") && ctx.num.getText().equals("s") ) SimpUnit = "Gy/s";
        else if (ctx.den.getText().equals("Kg") && ctx.num.getText().equals("m") ) SimpUnit = "Kg/m";
        else if (ctx.den.getText().equals("mol") && ctx.num.getText().equals("Kg") ) SimpUnit = "mol/Kg";
        else if (ctx.den.getText().equals("Kg") && ctx.num.getText().equals("mol") ) SimpUnit = "Kg/mol";
        else if (ctx.den.getText().equals("m") && ctx.num.getText().equals("m^3") ) SimpUnit = "m^-2";
        else if (ctx.den.getText().equals("m^3") && ctx.num.getText().equals("Kg") ) SimpUnit = "m^3/Kg";
        else if (ctx.den.getText().equals("Kg") && ctx.num.getText().equals("s") ) SimpUnit = "Kg/s";
        else if (ctx.den.getText().equals("J") && ctx.num.getText().equals("T") ) SimpUnit = "m^3/Kg";
        else if (ctx.den.getText().equals("W") && ctx.num.getText().equals("m^3") ) SimpUnit = "W/m^3";
        else if (ctx.den.getText().equals("K") && ctx.num.getText().equals("W") ) SimpUnit = "K/W";
        else if (ctx.den.getText().equals("K") && ctx.num.getText().equals("m") ) SimpUnit = "K/m";
        else if (ctx.den.getText().equals("Wb") && ctx.num.getText().equals("m") ) SimpUnit = "Wb/m";
        else if (ctx.den.getText().equals("Hz") && ctx.num.getText().equals("s") ) SimpUnit = "Hz/s";
        else if (ctx.den.getText().equals("m") && ctx.num.getText().equals("H") ) SimpUnit = "m/H";
        else if (ctx.den.getText().equals("W") && ctx.num.getText().equals("sr") ) SimpUnit = "W/sr";
        else if (ctx.den.getText().equals("W") && ctx.num.getText().equals("m") ) SimpUnit = "W/m";




    }


}