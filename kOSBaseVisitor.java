import java.io.File;
import org.stringtemplate.v4.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;

@parser::header {
import java.util.Map;
import java.util.HashMap;
}

@parser::members {
static protected Map<String,USymbol> symbolTable = new HashMap<>();
}

public class kOSBaseVisitor extends BaseGrammarVisitor<ST> {

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
    @Override public T visitCommand(BaseGrammarParser.CommandContext ctx) {
        ST res = stg.getInstanceOf("print");
        ret.add("arg",ctx.NAME());
        return res;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public T visitAssignment(BaseGrammarParser.AssignmentContext ctx) {
        ST res = stg.getInstanceOf("assign");
        // Left
        ST var = stg.getInstanceOf("variable");
        var.add("name",ctx.NAME());
        res.add("left",var);
        // Right
        res.add("right",visit(ctx.operation()));
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public T visitIf_else(BaseGrammarParser.If_elseContext ctx) {
        ST res = stg.getInstanceOf("if");
        res.add("condition",ctx.condition());
        for(BaseGrammarParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

    // AINDA TENHO QUE FAZER...
    @Override public T visitLoopFor(BaseGrammarParser.LoopForContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public T visitLoopWhile(BaseGrammarParser.LoopWhileContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public T visitLoopDoWhile(BaseGrammarParser.LoopDoWhileContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public T visitPar(BaseGrammarParser.ParContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public T visitVal(BaseGrammarParser.ValContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public T visitOp(BaseGrammarParser.OpContext ctx) { return visitChildren(ctx); }

    // FEITO
    // NÃO TESTADO!!!
    @Override public T visitDecrement(BaseGrammarParser.DencrementContext ctx) {
        ST res = stg.getInstanceOf("sub");
        res.add("left",ctx.NAME());
        res.add("right",1);
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public T visitAssignVar(BaseGrammarParser.AssignVarContext ctx) {
        ST var = stg.getInstanceOf("variable");
        var.add("name",ctx.NAME());
        return var;
    }

    // FEITO
    // NÃO TESTADO!!!
    @Override public T visitIncrement(BaseGrammarParser.IncrementContext ctx) {
        ST res = stg.getInstanceOf("sum");
        res.add("left",ctx.NAME());
        res.add("right",1);
    }

    // AINDA TENHO QUE FAZER...
    @Override public T visitCondition(BaseGrammarParser.ConditionContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public T visitValue(BaseGrammarParser.ValueContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public T visitPow(BaseGrammarParser.PowContext ctx) { return visitChildren(ctx); }

    protected String newVarName() {
        varCount++;
        return "v"+varCount;
    }
    protected int varCount = 0;
    protected STGroup stg = null;

}