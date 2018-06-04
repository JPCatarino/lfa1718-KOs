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
        res.add("cname", "prog1");
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
        ST res = stg.getInstanceOf("print");
        res.add("arg",ctx.NAME().getText());
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitAssignment(BaseGrammarParser.AssignmentContext ctx) {
        ST res = stg.getInstanceOf("assign");
        // Left
        ST var = stg.getInstanceOf("variable");
        var.add("name",ctx.NAME().getText());
        res.add("left",var);
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
        for(BaseGrammarParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitLoopWhile(BaseGrammarParser.LoopWhileContext ctx) {
        ST res = stg.getInstanceOf("while");
        res.add("condition",ctx.condition());
        for(BaseGrammarParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public ST visitLoopDoWhile(BaseGrammarParser.LoopDoWhileContext ctx) {
        ST res = stg.getInstanceOf("do_while");
        res.add("condition",ctx.condition());
        for(BaseGrammarParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

/*    // AINDA TENHO QUE FAZER...
    @Override public ST visitPar(BaseGrammarParser.ParContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public ST visitVal(BaseGrammarParser.ValContext ctx) { return visitChildren(ctx); }

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

//    @Override public ST visitCondition(BaseGrammarParser.ConditionContext ctx) {}

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