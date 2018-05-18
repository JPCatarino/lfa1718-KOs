import java.io.File;
import org.stringtemplate.v4.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;


public class testVisitor extends BaseGrammarBaseVisitor<ST>{

    @Override public ST visitMain(BaseGrammarParser.MainContext ctx) {
        stg = new STGroupFile("python.stg");
        ST res = stg.getInstanceOf("module");
        for(BaseGrammarParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

    @Override public ST visitStat(BaseGrammarParser.StatContext ctx){
        ST res = stg.getInstanceOf("stats");
        res.add("stat", visit(ctx.instruction()));
        return res;
    }

    @Override public ST visitInstruction(BaseGrammarParser.InstructionContext ctx){
        ST res = stg.getInstanceOf("assign");
        String id = ctx.NAME().getText();
        res.add("left", id);
        res.add("right", visit(ctx.operation()));
        return res;
    }

    @Override public ST visitOperation(BaseGrammarParser.OperationContext ctx){
        return visit(ctx.value());
    }

    @Override public ST visitValue(BaseGrammarParser.ValueContext ctx){
        ST res = stg.getInstanceOf("unit");
        res.add("num", ctx.INT().getText());
        res.add("un", ctx.UNIT().getText());
        return res;
    }
    protected STGroup stg = null;
}


