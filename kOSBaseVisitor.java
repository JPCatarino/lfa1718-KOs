import java.io.File;
import org.stringtemplate.v4.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.Map;
import java.util.HashMap;


public class kOSBaseVisitor extends BaseGrammarBaseVisitor<ST> {

    @Override public ST visitMain(BaseGrammarParser.MainContext ctx) {
        stg = new STGroupFile("python.stg");
        ST res = stg.getInstanceOf("baseClass");
        res.add("stat",visit(ctx.statList()));
        return res;
    }

    @Override public ST visitStatList(BaseGrammarParser.StatListContext ctx) {
        ST res = stg.getInstanceOf("stats");
        for(BaseGrammarParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitCommand(BaseGrammarParser.CommandContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST print = stg.getInstanceOf("print");
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        if(s.type == vartype.unitVar){
            String tmpVar = newVarName();
            ST printUn = stg.getInstanceOf("valPrint");
            ST assign = stg.getInstanceOf("assign");
            printUn.add("val",s.varName());
            assign.add("left",tmpVar);
            assign.add("right",printUn.render());
            print.add("arg",tmpVar);
            res.add("stat",assign.render());
            res.add("stat",print.render());
            return res;
        }
        print.add("arg",s.varName());
        return print;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitAssignment(BaseGrammarParser.AssignmentContext ctx) {
        ST res = stg.getInstanceOf("assign");
        ctx.varName = newVarName();
        // Left
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        s.setVarName(ctx.varName);
        res.add("left",ctx.varName);
        // Right
        res.add("right",visit(ctx.operation()));
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitIf_else(BaseGrammarParser.If_elseContext ctx) {
        ST res = stg.getInstanceOf("if");
        res.add("condition",ctx.condition());
        for(BaseGrammarParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitLoopFor(BaseGrammarParser.LoopForContext ctx){
        ST res = stg.getInstanceOf("for");
        res.add("var",ctx.var);
        res.add("min",ctx.min);
        res.add("max",ctx.max);
        res.add("stat",visit(ctx.statList()));
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitLoopWhile(BaseGrammarParser.LoopWhileContext ctx) {
        ST res = stg.getInstanceOf("while");
        res.add("condition",visit(ctx.condition()));
        res.add("stat",visit(ctx.statList()));
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitLoopDoWhile(BaseGrammarParser.LoopDoWhileContext ctx) {
        ST res = stg.getInstanceOf("do_while");
        res.add("condition",visit(ctx.condition()));
        res.add("stat",visit(ctx.statList()));
        return res;
    }

/*   // AINDA TENHO QUE FAZER...
    @Override public ST visitPar(BaseGrammarParser.ParContext ctx) { return visitChildren(ctx); }
*/


    @Override public ST visitVal(BaseGrammarParser.ValContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.value()));
        return res;
    }
/*
    // AINDA TENHO QUE FAZER...
    @Override public ST visitOp(BaseGrammarParser.OpContext ctx) { return visitChildren(ctx); }*/

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitDecrement(BaseGrammarParser.DecrementContext ctx) {
        ST res = stg.getInstanceOf("sub");
        res.add("left",ctx.NAME());
        res.add("right",1);
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitAssignVar(BaseGrammarParser.AssignVarContext ctx) {
        ST var = stg.getInstanceOf("variable");
        var.add("name",ctx.NAME());
        return var;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitIncrement(BaseGrammarParser.IncrementContext ctx) {
        ST res = stg.getInstanceOf("sum");
        res.add("left",ctx.NAME());
        res.add("right",1);
        return res;
    }

    @Override public ST visitCompare(BaseGrammarParser.CompareContext ctx) {
        ST res = stg.getInstanceOf("condition");
        res.add("left", visit(ctx.left));
        res.add("op", ctx.CONDITIONAL_OPERATOR().getText());
        res.add("right", visit(ctx.right));
        return res;
    }

    //    @Override public ST visitCondiEValue(BaseGrammarParser.CondiEValueContext ctx) { }

    @Override public ST visitCondiEVar(BaseGrammarParser.CondiEVarContext ctx) {
        ST res = stg.getInstanceOf("variable");
        res.add("name",ctx.NAME().getText());
        return res;
    }


    @Override public ST visitValueUnit(BaseGrammarParser.ValueUnitContext ctx) {
        ST res = stg.getInstanceOf("val");
        res.add("uvalue",ctx.num.getText());
        ST unit = stg.getInstanceOf("dicUnit");
        unit.add("uname",ctx.NAME().getText());
        res.add("unit",unit.render());
        return res;
    }

    /*
    // AINDA TENHO QUE FAZER...
    @Override public ST visitValue(BaseGrammarParser.ValueContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public ST visitPow(BaseGrammarParser.PowContext ctx) { return visitChildren(ctx); }*/

    protected String newVarName() {
        varCount++;
        return "v"+varCount;
    }
    protected int varCount = 0;
    protected STGroup stg = null;

}