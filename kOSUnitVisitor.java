import java.io.File;
import org.stringtemplate.v4.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.HashMap;

public class kOSUnitVisitor extends UnidadesBaseVisitor<ST>{
    HashMap<String,String> unitList = new HashMap<String,String>();

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
        ST res = stg.getInstanceOf("createunit");
        res.add("uname",ctx.NAME().getText());
        res.add("unit",visit(ctx.uname));
        return res;
    }

    @Override public ST visitUnitUNIT(UnidadesParser.UnitUNITContext ctx) {
        ST res = stg.getInstanceOf("unit");
        res.add("uname",ctx.NAME().getText());
        res.add("upot","3");
        return res;
    }

    @Override public ST visitUnitDivMult(UnidadesParser.UnitDivMultContext ctx) {
        ST res = stg.getInstanceOf("div");
        res.add("left",visit(ctx.num));
        res.add("right",visit(ctx.den));
        return res;
    }

//    @Override public ST visitUnitPow(UnidadesParser.UnitPowContext ctx) {}

    protected STGroup stg = null;
}