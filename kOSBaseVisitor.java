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
        ST res = stg.getInstanceOf("assign");
        ctx.varName = newVarName();
        // Left
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        if(s.varName() == null) {
            s.setVarName(ctx.varName);
            res.add("left", ctx.varName);
        }
        else res.add("left",s.varName());
        // Right
        res.add("right",visit(ctx.operation()));
        return res;
    }


    @Override public ST visitIf_else(BaseGrammarParser.If_elseContext ctx) {
        ST res = stg.getInstanceOf("stats");
        ST ifCond = stg.getInstanceOf("if");
        ifCond.add("condition",visit(ctx.condition()));
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
        res.add("var",ctx.var.getText());
        res.add("min",ctx.min.getText());
        res.add("max",ctx.max.getText());
        res.add("stat",visit(ctx.statList()));
        return res;
    }


    @Override public ST visitLoopWhile(BaseGrammarParser.LoopWhileContext ctx) {
        ST res = stg.getInstanceOf("while");
        res.add("condition",visit(ctx.condition()));
        res.add("stat",visit(ctx.statList()));
        return res;
    }


    @Override public ST visitLoopDoWhile(BaseGrammarParser.LoopDoWhileContext ctx) {
        ST res = stg.getInstanceOf("do_while");
        res.add("condition",visit(ctx.condition()));
        res.add("stat",visit(ctx.statList()));
        return res;
    }

    @Override public ST visitPar(BaseGrammarParser.ParContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.operation()));
        ctx.ty = ctx.operation().ty;
        return res;
    }



    @Override public ST visitVal(BaseGrammarParser.ValContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat", visit(ctx.value()));
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
        if(ctx.left.ty == vartype.unitVar && ctx.right.ty == vartype.unitVar){
            ST res = stg.getInstanceOf("valConta");
            res.add("val1", left);
            res.add("val2", right);
            res.add("op",ctx.NUMERIC_OPERATOR().getText());
            ctx.ty = vartype.unitVar;
            return res;
        }
        //else if((ctx.left.ty == vartype.unitVar && ctx.right.ty == vartype.simpVar) || (ctx.left.ty == vartype.simpVar && ctx.right.ty == vartype.unitVar)){
        else if((ctx.left.ty == vartype.unitVar) ^ (ctx.right.ty == vartype.unitVar)){
            ST res = stg.getInstanceOf("valContaSimp");
            res.add("val1", left);
            res.add("val2",right);
            res.add("op",ctx.NUMERIC_OPERATOR().getText());
            ctx.ty = vartype.unitVar;
            return res;
        }
        ST res = stg.getInstanceOf("contaSimples");
        res.add("left",left);
        res.add("op",ctx.NUMERIC_OPERATOR().getText());
        res.add("right",right);
        ctx.ty = vartype.simpVar;
        return res;
    }


    @Override public ST visitDecrement(BaseGrammarParser.DecrementContext ctx) {
      vartype vt = BaseGrammarParser.symbolTable.get(ctx.NAME().getText()).type();
      ST res;
      String id = ctx.NAME().getText();
      BGSymbol s = BaseGrammarParser.symbolTable.get(id);
      if(vt == vartype.unitVar) { res = stg.getInstanceOf("valContaSimp"); ctx.ty = vartype.unitVar;} // unitVar
      else                      { res = stg.getInstanceOf("contaSimples"); ctx.ty = vartype.simpVar;} // simpVar
      res.add("left", s.varName());
      res.add("right", 1);
      res.add("op", "-");
      return res;
    }


    @Override public ST visitAssignVar(BaseGrammarParser.AssignVarContext ctx) {
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        ST var = stg.getInstanceOf("variable");
        var.add("name",s.varName());
        ctx.ty = s.type();
        return var;
    }


    @Override public ST visitIncrement(BaseGrammarParser.IncrementContext ctx) {
        vartype vt = BaseGrammarParser.symbolTable.get(ctx.NAME().getText()).type();
        ST res = stg.getInstanceOf("assign");
        ST op;
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        if(vt == vartype.unitVar) { op = stg.getInstanceOf("valContaSimp"); ctx.ty = vartype.unitVar; }
        else{ op = stg.getInstanceOf("contaSimples"); ctx.ty = vartype.simpVar; }
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
        if(ctx.left.type == vartype.unitVar && ctx.right.type == vartype.unitVar){
            ST res = stg.getInstanceOf("unitCondition");
            res.add("left", visit(ctx.left));
            res.add("op", ctx.CONDITIONAL_OPERATOR().getText());
            res.add("right", visit(ctx.right));
            return res;
        }
        ST res = stg.getInstanceOf("condition");
        res.add("left", visit(ctx.left));
        res.add("op", ctx.CONDITIONAL_OPERATOR().getText());
        res.add("right", visit(ctx.right));
        return res;
    }

    @Override public ST visitCondiEValue(BaseGrammarParser.CondiEValueContext ctx) {
        ST res = stg.getInstanceOf("stats");
        res.add("stat",visit(ctx.value()));
        ctx.type = ctx.value().typ;
        return res;
    }

    @Override public ST visitCondiEVar(BaseGrammarParser.CondiEVarContext ctx) {
        String id = ctx.NAME().getText();
        BGSymbol s = BaseGrammarParser.symbolTable.get(id);
        ST res = stg.getInstanceOf("variable");
        res.add("name",s.varName());
        ctx.type = s.type();
        return res;
    }


    @Override public ST visitValueUnit(BaseGrammarParser.ValueUnitContext ctx) {
        ST res = stg.getInstanceOf("val");
        res.add("uvalue",ctx.num.getText());
        ST unit = stg.getInstanceOf("dicUnit");
        unit.add("uname",ctx.NAME().getText());
        res.add("unit",unit.render());
        ctx.typ = vartype.unitVar;
        return res;
    }
    /*
    // AINDA TENHO QUE FAZER...
    @Override public ST visitValue(BaseGrammarParser.ValueContext ctx) { return visitChildren(ctx); }

    // AINDA TENHO QUE FAZER...
    @Override public ST visitPow(BaseGrammarParser.PowContext ctx) { return visitChildren(ctx); }*/

    @Override public ST visitValueUnitNeg(BaseGrammarParser.ValueUnitNegContext ctx) { return visitChildren(ctx); }

    @Override public ST visitValueS(BaseGrammarParser.ValueSContext ctx) {
        ST res = stg.getInstanceOf("variable");
        res.add("name",ctx.num.getText());
        ctx.typ = vartype.simpVar;
        return res;
    }

    @Override public ST visitValueSNeg(BaseGrammarParser.ValueSNegContext ctx) { return visitChildren(ctx); }

    protected String newVarName() {
        varCount++;
        return "v"+varCount;
    }
    protected int varCount = 0;
    protected STGroup stg = null;

}
