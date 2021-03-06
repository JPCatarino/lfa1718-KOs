import java.io.File;
import org.stringtemplate.v4.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.Map;
import java.util.HashMap;


public class kOSBaseVisitor extends BaseGrammarBaseVisitor<ST> {

    HashMap<String,HashMap<String,Integer>> outMap = new HashMap<String,HashMap<String,Integer>>();

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

    @Override public ST visitInstPrint(BaseGrammarParser.InstPrintContext ctx) { return visit(ctx.print());}

    @Override public ST visitPrint(BaseGrammarParser.PrintContext ctx) {
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


    @Override public ST visitAssignment(BaseGrammarParser.AssignmentContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.operation()));
        ST ass = stg.getInstanceOf("assign");
        ctx.varName = newVarName();
        // Left
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        if(s.varName() == null) {
            s.setVarName(ctx.varName);
            ass.add("left", ctx.varName);
        }
        else ass.add("left",s.varName());
        // Right
        ass.add("right",ctx.operation().varGen);
        res.add("stat",ass);
        return res;
    }

    @Override public ST visitConvValue(BaseGrammarParser.ConvValueContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST conVal = stg.getInstanceOf("getConv");
        visit(ctx.src);
        conVal.add("dsrc",ctx.src.unit);
        conVal.add("ddtn",ctx.dest.getText());
        ST ass = stg.getInstanceOf("assign");
        String varName = newVarName();
        ass.add("left",varName);
        ass.add("right",conVal);
        res.add("stat",ass);
        ST conMul = stg.getInstanceOf("valContaSimp");
        conMul.add("val1",varName);
        conMul.add("op",'*');
        conMul.add("val2",ctx.src.nr);
        ST vp = stg.getInstanceOf("valPrint");
        vp.add("val",conMul);
        ST prin = stg.getInstanceOf("print");
        prin.add("arg","\"O valor Convertido é:\" + " + vp.render());
        res.add("stat",prin);
        return res;
    }



    @Override public ST visitIf_else(BaseGrammarParser.If_elseContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST ifCond = stg.getInstanceOf("if");
        if(ctx.bc == null) {
            res.add("stat",visit(ctx.condition()));
            ifCond.add("condition", ctx.condition().varGen);
        }
        else {
            res.add("stat",visit(ctx.booleanCondition()));
            ifCond.add("condition", ctx.booleanCondition().varGen);
        }
        ifCond.add("stat", visit(ctx.ifA));
        res.add("stat",ifCond);
        if(ctx.elseA != null) {
            ST els = stg.getInstanceOf("else");
            els.add("stat", visit(ctx.elseA));
            res.add("stat", els);
        }
        return res;
    }

    @Override public ST visitIfStatList(BaseGrammarParser.IfStatListContext ctx) {
        ST res = stg.getInstanceOf("stats");
        if(visit(ctx.statList())==null) { res.add("stat", stg.getInstanceOf("pass")); }
        else                            { res.add("stat", visit(ctx.statList())); }
        return visit(ctx.statList());
    }

    @Override public ST visitIfStat(BaseGrammarParser.IfStatContext ctx) { return visitChildren(ctx); }


    @Override public ST visitLoopFor(BaseGrammarParser.LoopForContext ctx){
        ST res = stg.getInstanceOf("for");
        String id = ctx.var.getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        s.setVarName(newVarName());
        res.add("var",s.varName());
        res.add("min",ctx.min.getText());
        res.add("max",ctx.max.getText());
        res.add("stat",visit(ctx.statList()));
        return res;
    }


    @Override public ST visitLoopWhile(BaseGrammarParser.LoopWhileContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST whl = stg.getInstanceOf("while");
        if(ctx.bc == null) {
            res.add("stat", visit(ctx.condition()));
            whl.add("condition", ctx.condition().varGen);
        }
        else {
            res.add("stat", visit(ctx.booleanCondition()));
            whl.add("condition", ctx.booleanCondition().varGen);
        }
        whl.add("stat",visit(ctx.statList()));
        res.add("stat",whl.render());
        return res;
    }


    @Override public ST visitLoopDoWhile(BaseGrammarParser.LoopDoWhileContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST dwhl = stg.getInstanceOf("do_while");
        if(ctx.bc == null) {
            res.add("stat", visit(ctx.condition()));
            dwhl.add("condition", ctx.condition().varGen);
        }
        else {
            res.add("stat", visit(ctx.booleanCondition()));
            dwhl.add("condition", ctx.booleanCondition().varGen);
        }
        dwhl.add("stat",visit(ctx.statList()));
        res.add("stat",dwhl.render());
        return res;
    }

    @Override public ST visitPar(BaseGrammarParser.ParContext ctx) {
        ctx.varGen = newVarName();
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.operation()));
        ST ass = stg.getInstanceOf("assign");
        ass.add("left",ctx.varGen);
        ass.add("right","(" + ctx.operation().varGen +")");
        ctx.ty = ctx.operation().ty;
        res.add("stat",ass.render());
        return res;
    }



    @Override public ST visitVal(BaseGrammarParser.ValContext ctx) {
        ST val = stg.getInstanceOf("stats");
        val.add("stat", visit(ctx.value()));
        ST res = stg.getInstanceOf("assign");
        ctx.varGen = newVarName();
        res.add("left",ctx.varGen);
        res.add("right",val.render());
        if (ctx.value().typ == vartype.unitVar) {
            ctx.ty = vartype.unitVar;
        }
        if(ctx.value().typ == vartype.simpVar){
            ctx.ty = vartype.simpVar;
        }
        return res;
    }


    @Override public ST visitOp(BaseGrammarParser.OpContext ctx) {
        ST left = visit(ctx.left);
        ST right = visit(ctx.right);
        ST res = stg.getInstanceOf("stats");
        res.add("stat",left.render());
        res.add("stat",right.render());
        if(ctx.left.ty == vartype.unitVar && ctx.right.ty == vartype.unitVar){
            ST ass = stg.getInstanceOf("assign");
            ctx.varGen = newVarName();
            ass.add("left",ctx.varGen);
            ST cont = stg.getInstanceOf("valConta");
            cont.add("val1", ctx.left.varGen);
            cont.add("val2", ctx.right.varGen);
            cont.add("op",ctx.NUMERIC_OPERATOR().getText());
            ctx.ty = vartype.unitVar;
            ass.add("right",cont);
            res.add("stat",ass);
            return res;
        }
        //else if((ctx.left.ty == vartype.unitVar && ctx.right.ty == vartype.simpVar) || (ctx.left.ty == vartype.simpVar && ctx.right.ty == vartype.unitVar)){
        else if((ctx.left.ty == vartype.unitVar) ^ (ctx.right.ty == vartype.unitVar)){
            ST ass = stg.getInstanceOf("assign");
            ctx.varGen = newVarName();
            ass.add("left",ctx.varGen);
            ST contSimp = stg.getInstanceOf("valContaSimp");
            if(ctx.left.ty == vartype.simpVar && ctx.right.ty == vartype.unitVar){
                contSimp.add("val1", ctx.right.varGen);
                contSimp.add("val2",ctx.left.varGen);
                contSimp.add("op",ctx.NUMERIC_OPERATOR().getText());
                ctx.ty = vartype.unitVar;
                ass.add("right",contSimp);
                res.add("stat",ass);
                return res;
            }
            contSimp.add("val1", ctx.left.varGen);
            contSimp.add("val2",ctx.right.varGen);
            contSimp.add("op",ctx.NUMERIC_OPERATOR().getText());
            ctx.ty = vartype.unitVar;
            ass.add("right",contSimp);
            res.add("stat",ass);
            return res;
        }
        ST ass = stg.getInstanceOf("assign");
        ctx.varGen = newVarName();
        ass.add("left",ctx.varGen);
        ST contSimp = stg.getInstanceOf("contaSimples");
        contSimp.add("left",ctx.left.varGen);
        contSimp.add("op",ctx.NUMERIC_OPERATOR().getText());
        contSimp.add("right",ctx.right.varGen);
        ctx.ty = vartype.simpVar;
        ass.add("right",contSimp);
        res.add("stat",ass);
        return res;
    }


    @Override public ST visitDecrement(BaseGrammarParser.DecrementContext ctx) {
        vartype vt = BaseGrammarParser.symbolTable.get(ctx.NAME().getText()).type();
        ST res = stg.getInstanceOf("assign");
        ST op;
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        if(vt == vartype.unitVar) { op = stg.getInstanceOf("valContaSimp"); ctx.ty = vartype.unitVar; }
        else                      { op = stg.getInstanceOf("contaSimples"); ctx.ty = vartype.simpVar; }
        if(ctx.ty == vartype.unitVar){
            op.add("val1", s.varName());
            op.add("val2", 1);
            op.add("op", "-");
        }
        else {
            op.add("left", s.varName());
            op.add("right", 1);
            op.add("op", "-");
        }
        res.add("left", s.varName());
        res.add("right", op);
        return res;
    }


    @Override public ST visitAssignVar(BaseGrammarParser.AssignVarContext ctx) {
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        ST var = stg.getInstanceOf("variable");
        var.add("name",s.varName());
        ctx.varGen = newVarName();
        ST ass = stg.getInstanceOf("assign");
        ass.add("left",ctx.varGen);
        ass.add("right",var.render());
        ctx.ty = s.type();
        return ass;
    }


    @Override public ST visitIncrement(BaseGrammarParser.IncrementContext ctx) {
        vartype vt = BaseGrammarParser.symbolTable.get(ctx.NAME().getText()).type();
        ST res = stg.getInstanceOf("assign");
        ST op;
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        if(vt == vartype.unitVar) { op = stg.getInstanceOf("valContaSimp"); ctx.ty = vartype.unitVar; }
        else                      { op = stg.getInstanceOf("contaSimples"); ctx.ty = vartype.simpVar; }
        op.add("left", s.varName());
        op.add("right", 1);
        op.add("op", "+");
        res.add("left", s.varName());
        res.add("right", op);
        return res;
    }

    @Override public ST visitCompare(BaseGrammarParser.CompareContext ctx) {
        ST left = visit(ctx.left);
        ST right = visit(ctx.right);
        ctx.varGen = newVarName();
        ST res = stg.getInstanceOf("stats");
        res.add("stat",left);
        res.add("stat",right);
        if(ctx.left.type == vartype.unitVar && ctx.right.type == vartype.unitVar){
            ST ass = stg.getInstanceOf("assign");
            ass.add("left",ctx.varGen);
            ST uCond = stg.getInstanceOf("unitCondition");
            uCond.add("left", ctx.left.varGen);
            uCond.add("op", ctx.CONDITIONAL_OPERATOR().getText());
            uCond.add("right", ctx.right.varGen);
            ass.add("right",uCond.render());
            res.add("stat",ass);
            return res;
        }
        ST ass = stg.getInstanceOf("assign");
        ass.add("left",ctx.varGen);
        ST cond = stg.getInstanceOf("condition");
        cond.add("left", ctx.left.varGen);
        cond.add("op", ctx.CONDITIONAL_OPERATOR().getText());
        cond.add("right", ctx.right.varGen);
        ass.add("right",cond.render());
        res.add("stat",ass);
        return res;
    }

    @Override public ST visitBoolNotCond(BaseGrammarParser.BoolNotCondContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.condition()));
        ST ass = stg.getInstanceOf("assign");
        ctx.varGen = newVarName();
        ass.add("left",ctx.varGen);
        ST not = stg.getInstanceOf("boolNot");
        not.add("val",ctx.condition().varGen);
        ass.add("right",not);
        res.add("stat",ass);
        return res;
    }

    @Override public ST visitBoolCondOp(BaseGrammarParser.BoolCondOpContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST ass = stg.getInstanceOf("assign");
        res.add("stat",visit(ctx.left));
        res.add("stat",visit(ctx.right));
        ctx.varGen = newVarName();
        ass.add("left",ctx.varGen);
        ST boOp = stg.getInstanceOf("boolCondition");
        boOp.add("left",ctx.left.varGen);
        boOp.add("op",ctx.BOOLEAN_OPERATOR().getText());
        boOp.add("right",ctx.right.varGen);
        ass.add("right",boOp.render());
        res.add("stat",ass);
        return res;
    }

    @Override public ST visitBoolCond(BaseGrammarParser.BoolCondContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.condition()));
        ctx.varGen = ctx.condition().varGen;
        return res;
    }

    @Override public ST visitSoloCond(BaseGrammarParser.SoloCondContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.conditionE()));
        ctx.varGen = ctx.conditionE().varGen;
        return res;
    }


    @Override public ST visitCondiEValue(BaseGrammarParser.CondiEValueContext ctx) {
        ST ass = stg.getInstanceOf("assign");
        ctx.varGen = newVarName();
        ass.add("left",ctx.varGen);
        ST res = stg.getInstanceOf("stats");
        ass.add("right",visit(ctx.value()));
        ctx.type = ctx.value().typ;
        res.add("stat",ass);
        return res;
    }

    @Override public ST visitCondiEVar(BaseGrammarParser.CondiEVarContext ctx) {
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        ST res = stg.getInstanceOf("stats");
        ST ass = stg.getInstanceOf("assign");
        ctx.varGen = newVarName();
        ass.add("left",ctx.varGen);
        ass.add("right",s.varName());
        ctx.type = s.type();
        res.add("stat",ass.render());
        return res;
    }


    @Override public ST visitPow(BaseGrammarParser.PowContext ctx) {
        ST res = stg.getInstanceOf("pow");

        if(ctx.min != null)
            res.add("exponent",  "-" + ctx.exp.getText());
        else
            res.add("exponent", ctx.exp.getText());

        return res;
    }

    @Override public ST visitValueUnit(BaseGrammarParser.ValueUnitContext ctx) {
        ST res = stg.getInstanceOf("val");
        if(ctx.pow() != null)
            res.add("uvalue",ctx.num.getText() + visit(ctx.pow()).render());
        else
            res.add("uvalue",ctx.num.getText());
        ST unit = stg.getInstanceOf("dicUnit");
        unit.add("uname",ctx.NAME().getText());
        res.add("unit",unit.render());
        ctx.typ = vartype.unitVar;
        ctx.unit = ctx.NAME().getText();
        ctx.nr = ctx.num.getText();
        return res;
    }

    @Override public ST visitValueUnitNeg(BaseGrammarParser.ValueUnitNegContext ctx) {
        ST res = stg.getInstanceOf("signedVal");
        res.add("sign", "-");
        if(ctx.pow() != null)
            res.add("uvalue",ctx.num.getText() + visit(ctx.pow()).render());
        else
            res.add("uvalue",ctx.num.getText());
        ST unit = stg.getInstanceOf("dicUnit");
        unit.add("uname",ctx.NAME().getText());
        res.add("unit",unit.render());
        ctx.typ = vartype.unitVar;
        return res;
    }

    @Override public ST visitValueS(BaseGrammarParser.ValueSContext ctx) {
        ST res = stg.getInstanceOf("variable");
        if(ctx.pow() != null)
            res.add("name",ctx.num.getText() + visit(ctx.pow()).render());
        else
            res.add("name",ctx.num.getText());
        ctx.typ = vartype.simpVar;
        return res;
    }

    @Override public ST visitValueSNeg(BaseGrammarParser.ValueSNegContext ctx) {
        ST res = stg.getInstanceOf("signedVariable");
        res.add("sign", "-");
        if(ctx.pow() != null)
            res.add("unsigVal",ctx.num.getText() + visit(ctx.pow()).render());
        else
            res.add("unsigVal",ctx.num.getText());
        ctx.typ = vartype.simpVar;
        return res;
    }

    protected String newVarName() {
        varCount++;
        return "v"+varCount;
    }
    protected int varCount = 0;
    protected STGroup stg = null;

}
