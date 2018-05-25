import java.io.File;
import org.stringtemplate.v4.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.HashMap;
import java.util.Map;

public class kOSUnitVisitor2 extends UnidadesBaseVisitor<ST>{

    @Override public ST visitMain(UnidadesParser.MainContext ctx) {
        stg = new STGroupFile("python.stg");
        ST res = stg.getInstanceOf("unitClass");
        res.add("cname", "prog1");
        res.add("stat",visit(ctx.statList()));
        return res;
    }
     
    @Override public ST visitStatList(UnidadesParser.StatListContext ctx) {
        ST res = stg.getInstanceOf("stats");
        for(UnidadesParser.StatContext sc: ctx.stat())
            res.add("stat", visit(sc));
        return res;
    }

     
    @Override public ST visitCreate(UnidadesParser.CreateContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST cunit = stg.getInstanceOf("createunit");
        cunit.add("uname",ctx.NAME().getText());
        ST unit = visit(ctx.uname);
        cunit.add("unit",ctx.uname.varName);
        res.add("stat",unit);
        res.add("stat",cunit);
        return res;
    }
     
    @Override public ST visitPow(UnidadesParser.PowContext ctx) { return visitChildren(ctx); }
     
    @Override public ST visitCompose(UnidadesParser.ComposeContext ctx) { return visitChildren(ctx); }
     
    @Override public ST visitUnitUNIT(UnidadesParser.UnitUNITContext ctx) {
        ctx.varName = newVarName();
        ST res = stg.getInstanceOf("assign");
        ST un = stg.getInstanceOf("unit");
        un.add("uname",ctx.NAME().getText());
        un.add("upot","1");
        res.add("left",ctx.varName);
        res.add("right",un);
        return res;
    }
     
    @Override public ST visitCUnitDivMult(UnidadesParser.CUnitDivMultContext ctx) { return visitChildren(ctx); }


    protected String newVarName() {
        varCount++;
        return "v"+varCount;
    }
    protected int varCount = 0;
    protected STGroup stg = null;
}