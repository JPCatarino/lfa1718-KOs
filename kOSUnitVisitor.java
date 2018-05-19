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
        ST res = stg.getInstanceOf("addunit");
        res.add("name",ctx.uname.getText());
        res.add("units",visit(ctx.unit()));
        res.add("symbol",ctx.symbol.getText());
        unitList.put(ctx.uname.getText(),ctx.symbol.getText());
        return res;
    }

//    @Override public ST visitUse(UnidadesParser.UseContext ctx) {
//        ST res = stg.getInstanceOf("addunit");
//        res.add("uname")
//    }

    @Override public ST visitUnitUNIT(UnidadesParser.UnitUNITContext ctx) {
        ST res = stg.getInstanceOf("symbol");
        res.add("sym",ctx.NAME().getText());
        return res;
    }

    @Override public ST visitUnitDivMult(UnidadesParser.UnitDivMultContext ctx) {
        ST res = stg.getInstanceOf("div");
        res.add("left",visit(ctx.num));
        res.add("right",visit(ctx.den));
        return res;
    }

//    @Override public ST visitUnitPow(UnidadesParser.UnitPowContext ctx) { return visitChildren(ctx); }

    protected STGroup stg = null;
}