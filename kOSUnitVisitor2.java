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
        cunit.add("uname",ctx.STRING().getText());
        ST unit = visit(ctx.uname);
        String id = ctx.STRING().getText();
        USymbol s = UnidadesParser.symbolTable.get(id);
        s.setVarName(ctx.uname.varName);
        cunit.add("unit",ctx.uname.varName);
        res.add("stat",unit);
        res.add("stat",cunit);
        return res;
    }
     
    @Override public ST visitPow(UnidadesParser.PowContext ctx) {
        ST res = stg.getInstanceOf("assign");
        String id = ctx.STRING().getText();
        USymbol s = UnidadesParser.symbolTable.get(id);
        res.add("left",s.varName()+".pot");
        res.add("right",ctx.INT().getText());
        return res;
    }
     
    @Override public ST visitCompose(UnidadesParser.ComposeContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.composedUnit()));
        ST cunit = stg.getInstanceOf("createunit");
        String id = ctx.STRING().getText();
        USymbol s = UnidadesParser.symbolTable.get(id);
        s.setVarName(newVarName());
        ST assign = stg.getInstanceOf("assign");
        assign.add("left",s.varName());
        assign.add("right",ctx.composedUnit().varName);
        res.add("stat",assign);
        cunit.add("uname",ctx.STRING().getText());
        cunit.add("unit",s.varName());
        res.add("stat",cunit);
        return res;
    }
     
    @Override public ST visitUnitUNIT(UnidadesParser.UnitUNITContext ctx) {
        ctx.varName = newVarName();
        ST res = stg.getInstanceOf("assign");
        ST un = stg.getInstanceOf("unit");
        un.add("uname",ctx.STRING().getText());
        un.add("upot","1");
        res.add("left",ctx.varName);
        res.add("right",un);
        return res;
    }

    @Override public ST visitCUnitParents(UnidadesParser.CUnitParentsContext ctx) {
        ST res = visit(ctx.composedUnit());
        ctx.varName = ctx.p.varName;
        return res;
    }


    @Override public ST visitCUnitDivMult(UnidadesParser.CUnitDivMultContext ctx) {
        ST left = visit(ctx.left);
        ST right = visit(ctx.right);
        ST res = stg.getInstanceOf("stats");
        res.add("stat",left);
        res.add("stat",right);
        String lvar = ctx.left.varName;
        String rvar = ctx.right.varName;
        if(ctx.op.getText().equals(":")){
            ST inv = stg.getInstanceOf("invertPot");
            inv.add("name",rvar);
            res.add("stat",inv);
        }
        ctx.varName = newVarName();
        ST group = stg.getInstanceOf("sum");
        group.add("left",lvar);
        group.add("right",rvar);
        ST nAssign = stg.getInstanceOf("assign");
        nAssign.add("left", ctx.varName);
        nAssign.add("right",group);
        res.add("stat",nAssign);
        return res;
    }

    @Override public ST visitCUnitName(UnidadesParser.CUnitNameContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST baseUnit = stg.getInstanceOf("tmpUnit");
        String id = ctx.STRING().getText();
        USymbol s = UnidadesParser.symbolTable.get(id);
        baseUnit.add("uname",s.varName()+".unit");
        baseUnit.add("upot",s.varName()+".pot");
        String tmp = newVarName();
        ST assign = stg.getInstanceOf("assign");
        assign.add("left",tmp);
        assign.add("right",baseUnit.render());
        ST group = stg.getInstanceOf("glom");
        res.add("stat",assign);

        ST assign2 = stg.getInstanceOf("assign");
        ctx.varName = newVarName();
        assign2.add("left",ctx.varName);
        group.add("unit",tmp);
        assign2.add("right",group);
        res.add("stat",assign2);
        return res;
    }


    protected String newVarName() {
        varCount++;
        return "v"+varCount;
    }
    protected int varCount = 0;
    protected STGroup stg = null;
}