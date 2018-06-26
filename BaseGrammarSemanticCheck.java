import static java.lang.System.*;


import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.Map;
import java.util.HashMap;


public class BaseGrammarSemanticCheck extends BaseGrammarBaseVisitor<Boolean> {


    /*
    @Override public Boolean visitCheckVar(BaseGrammarParser.CheckVarContext ctx){

    @Override public Boolean visitCommand(BaseGrammarParser.CommandContext ctx){

        Boolean res = true;
        String name = ctx.NAME().getText();
        if (!BaseGrammarParser.symbolTable.containsKey(name)){
            ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
            res = false;
        }
        return res;
    }
    */

    //verificar se é simple var ou unit var
    //simVar i = 2kg -> erro
    @Override
    public Boolean visitAssignment(BaseGrammarParser.AssignmentContext ctx) {
        Boolean res = true;
        String id = ctx.NAME().getText();


        if (!BaseGrammarParser.symbolTable.containsKey(id)) {
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" is not declared!");
            res = false;
        } else {
            //if(ctx.varType().getText().equals("simpVar")){
            String abc = "ola";

        }
        //else {
        //  String abc = "ola";

        //}


        //}
        return res;

    }


    /*


    @Override public Boolean visitDecrement(BaseGrammarParser.DecrementContext ctx){     
        Boolean res = true;
        String name = ctx.NAME().getText();
        if (!BaseGrammarParser.symbolTable.containsKey(name)){
            ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
            res = false;
        }
        return res;
    }

    @Override public Boolean visitIncrement(BaseGrammarParser.IncrementContext ctx){     
        Boolean res = true;
        String name = ctx.NAME().getText();

        if (!BaseGrammarParser.symbolTable.containsKey(name)){
            ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
            res = false;
        }
        return res;
    }
    */

    /*
    @Override public Boolean visitSimple(BaseGrammarParser.AssignVarContext ctx){
        Boolean res = true;

        String id = ctx.STRING().getText();

        BGSymbol s = BaseGrammarParser.symbolTable.get(id);


        if(s.type != vartype.simpVar){
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" should be Simple!");
            res = false;

        }
        return res;
    }
    */
    @Override
    public Boolean visitVarDec(BaseGrammarParser.VarDecContext ctx) {
        Boolean res = true;
        String id = ctx.NAME().getText();
        if (BaseGrammarParser.symbolTable.containsKey(id)) {
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" already declared!");
            res = false;
        } else {
            if (ctx.varType().getText().equals("simpVar")) {
                BaseGrammarParser.symbolTable.put(id, new BaseGrammarSymbol(id, vartype.simpVar));
            } else
                BaseGrammarParser.symbolTable.put(id, new BaseGrammarSymbol(id, vartype.unitVar));
        }
        return res;
    }

    //@Override
    // public Boolean visitAssignVar(BaseGrammarParser.AssignVarContext ctx) {

    //}
    @Override
    public Boolean visitOp(BaseGrammarParser.OpContext ctx) {
        Boolean res = true;

        visit(ctx.right);
        visit(ctx.left);
        String operator = ctx.NUMERIC_OPERATOR().getText();
        if (ctx.left.ty == ctx.right.ty) {


        }

        if (ctx.left.ty.equals(vartype.simpVar) && ctx.right.ty.equals(vartype.unitVar)) {
            if (operator.equals("/") || operator.equals("+") || operator.equals("-")) {
                ErrorHandling.printError(ctx, "You cannot make that operation between a simple variable and an unit variable!");
                res = false;

            }


        }

        if (ctx.left.ty.equals(vartype.unitVar) && ctx.right.ty.equals(vartype.simpVar)) {
            if (operator.equals("+") || operator.equals("-")) {
                ErrorHandling.printError(ctx, "You cannot make that operation between a simple variable and an unit variable!");
                res = false;

            }
        }
        res = true;

        return res;
    }

    @Override
    public Boolean visitValueUnit(BaseGrammarParser.ValueUnitContext ctx) {
        Boolean res = true;
        ctx.typ = vartype.unitVar;
        return res;

    }

    @Override
    public Boolean visitValueUnitNeg(BaseGrammarParser.ValueUnitNegContext ctx) {
        Boolean res = true;
        ctx.typ = vartype.unitVar;
        return res;

    }

    @Override
    public Boolean visitValueS(BaseGrammarParser.ValueSContext ctx) {
        Boolean res = true;
        ctx.typ = vartype.simpVar;
        return res;

    }

    @Override
    public Boolean visitValueSNeg(BaseGrammarParser.ValueSNegContext ctx) {
        Boolean res = true;
        ctx.typ = vartype.simpVar;
        return res;

    }


    @Override
    public Boolean visitAssignVar(BaseGrammarParser.AssignVarContext ctx){
        Boolean res = true;
        String id = ctx.NAME().getText();

        if (BaseGrammarParser.symbolTable.containsKey(id)) {
            BGSymbol s = BaseGrammarParser.symbolTable.get(id);
            ctx.ty = s.type;
        } else {
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" does not exist!");
            res = false;
        }

        return res;
    }


}


