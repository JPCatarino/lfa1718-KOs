import java.io.File;
import org.stringtemplate.v4.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.HashMap;
import java.util.Map;

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
        res.add("upot","1");
        return res;
    }

    @Override public ST visitUnitDivMult(UnidadesParser.UnitDivMultContext ctx) {
        ST res = stg.getInstanceOf("glom");
        ST left = visit(ctx.left);
        ST right = visit(ctx.right);
        if(ctx.op.getText().equals(":")){
            Map attri = right.getAttributes();
            int rightP =  Integer.parseInt(attri.get("upot").toString());
            rightP = -rightP;
            right.remove("upot");
            right.add("upot",Integer.toString(rightP));
        }
        res.add("unit",left);
        res.add("unit",right);
        return res;
    }

    @Override public ST visitUnitPow(UnidadesParser.UnitPowContext ctx) {
        ST res = visit(ctx.unit());
        res.remove("upot");
        res.add("upot",ctx.INT().getText());
        return res;
    }

    protected STGroup stg = null;
}