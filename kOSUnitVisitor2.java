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
        String id = ctx.NAME().getText();
        USymbol s = UnidadesParser.symbolTable.get(id);
        s.setVarName(ctx.uname.varName);
        s.setConversion(false);
        cunit.add("unit",ctx.uname.varName);
        res.add("stat",unit);
        res.add("stat",cunit);
        return res;
    }

    @Override public ST visitPow(UnidadesParser.PowContext ctx) {
        ST res = stg.getInstanceOf("assign");
        String id = ctx.NAME().getText();
        USymbol s = UnidadesParser.symbolTable.get(id);
        res.add("left",s.varName()+".pot");
        res.add("right",ctx.INT().getText());
        return res;
    }

    @Override public ST visitCompose(UnidadesParser.ComposeContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.composedUnit()));
        ST cunit = stg.getInstanceOf("createunit");
        String id = ctx.NAME().getText();
        USymbol s = UnidadesParser.symbolTable.get(id);
        s.setVarName(newVarName());
        s.setConversion(false);
        ST assign = stg.getInstanceOf("assign");
        assign.add("left",s.varName());
        assign.add("right",ctx.composedUnit().varName);
        res.add("stat",assign);
        cunit.add("uname",ctx.NAME().getText());
        cunit.add("unit",s.varName());
        res.add("stat",cunit);
        return res;
    }

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
        String id = ctx.NAME().getText();
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

    @Override public ST visitSetConvValue(UnidadesParser.SetConvValueContext ctx) {
        ST res = stg.getInstanceOf("stats");

        // Direct conversion
        ST dic_dir = stg.getInstanceOf("iniD");
        String id_dir = ctx.src.getText();
        USymbol s_dir = UnidadesParser.symbolTable.get(id_dir);
        String varName_dir = newVarName();
        dic_dir.add("var",varName_dir);
        res_dir.add("stat",dic_dir);
        ST tmpD_dir = stg.getInstanceOf("addD");
        tmpD_dir.add("dName",varName_dir);
        tmpD_dir.add("dVal",visit(ctx.dtn));
        tmpD_dir.add("dUnit",ctx.dtn.uniNa);
        res.add("stat",tmpD_dir);
        ST convD_dir = stg.getInstanceOf("addD");
        if(!s_dir.isVarConversion()){
            convD_dir.add("dName","conv");
            convD_dir.add("dUnit",ctx.src.getText());
            convD_dir.add("dVal","{}");
            s_dir.setConversion(true);
            res.add("stat",convD_dir);
        }
        ST merge_dir = stg.getInstanceOf("addD");
        merge_dir.add("dName","conv");
        merge_dir.add("dUnit",ctx.src.getText());
        ST mergeVal_dir = stg.getInstanceOf("merge");
        mergeVal_dir.add("d1","conv[\'" + ctx.src.getText() + "\']");
        mergeVal_dir.add("d2",varName_dir);
        merge_dir.add("dVal",mergeVal.render());
        res.add("stat",merge_dir);

        // Inverted setConversion
        ST dic_inv = stg.getInstanceOf("iniD");
        String id_inv = ctx.dtn.uniNa;
        USymbol s_inv = UnidadesParser.symbolTable.get(id_inv);
        String varName_inv = newVarName();
        dic_inv.add("var",varName_inv);
        res.add("stat",dic_inv);
        ST tmpD_inv = stg.getInstanceOf("addD");
        tmpD_inv.add("dName",varName_inv);

        ST val_inv = stg.getInstanceOf("val");
        val_inv.add("uvalue", ""+1/Integer.parseInt(ctx.dtn.n));
        val_inv.add("unit","UnitDic[\'" + ctx.src.getText() + "\']");

        tmpD_inv.add("dVal",val_inv);
        tmpD_inv.add("dUnit",ctx.src.getText());
        res.add("stat",tmpD_inv);
        ST convD_inv = stg.getInstanceOf("addD");
        if(!s_inv.isVarConversion()){
            convD_inv.add("dName","conv");
            convD_inv.add("dUnit",ctx.dtn.uniNa));
            convD_inv.add("dVal","{}");
            s_inv.setConversion(true);
            res.add("stat",convD_inv);
        }
        ST merge_inv = stg.getInstanceOf("addD");
        merge_inv.add("dName","conv");
        merge_inv.add("dUnit",ctx.dtn.uniNa);
        ST mergeVal_inv = stg.getInstanceOf("merge");
        mergeVal_inv.add("d1","conv[\'" + ctx.dtn.uniNa + "\']");
        mergeVal_inv.add("d2",varName_inv);
        merge_inv.add("dVal",mergeVal.render());
        res.add("stat",merge_inv);
        return res;
    }

    @Override public ST visitValue(UnidadesParser.ValueContext ctx) {
        ST res = stg.getInstanceOf("val");
        res.add("uvalue",ctx.INT().getText());
        res.add("unit","UnitDic[\'" + ctx.NAME().getText() + "\']");
        ctx.uniNa = ctx.NAME().getText();

        return res;
    }



    protected String newVarName() {
        varCount++;
        return "v"+varCount;
    }
    protected int varCount = 0;
    protected STGroup stg = null;
}
