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
    }

}