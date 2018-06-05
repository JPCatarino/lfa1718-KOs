import static java.lang.System.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class BaseGrammarSemanticCheck extends BaseGrammarBaseVisitor<Boolean> {
    @Override public Boolean visitCommand(BaseGrammarParser.CommandContext ctx){
        Boolean res = true;
        String name = ctx.NAME().getText();
        if (!BaseGrammarParser.symbolTable.containsKey(name)){
            ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
            res = false;
        }
        return res;
    }

    @Override public Boolean visitAssignment(BaseGrammarParser.AssignmentContext ctx){     
        Boolean res = true;
        String name = ctx.NAME().getText();
        if (BaseGrammarParser.symbolTable.containsKey(name)){
            ErrorHandling.printError(ctx, "Variable \""+name+"\" already declared!");
            res = false;
        }
        else{
            BaseGrammarParser.symbolTable.put(name, new VarAssign(name)); 
        }
        return res;
    }

    @Override public Boolean visitAssignVar(BaseGrammarParser.AssignVarContext ctx){     
        Boolean res = true;
        String name = ctx.NAME().getText();
        ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
        if (!BaseGrammarParser.symbolTable.containsKey(name)){
            ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
            res = false;
        }
        return res;
    }
}