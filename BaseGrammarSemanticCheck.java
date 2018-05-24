import static java.lang.System.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class BaseGrammarSemanticCheck extends BaseGrammarBaseVisitor<Boolean> {
      @Override public Boolean visitPrint_readVar(BaseGrammarParser.Print_readVarContext ctx)
      {
            Boolean res = true;
            String name = ctx.NAME().getText();
            if (!BaseGrammarParser.symbolTable.containsKey(name))
            {
            ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
            res = false;
            }
            return res;
      }

      @Override public Boolean visitAssigment(BaseGrammarParser.AssigmentContext ctx)
      {
            Boolean res = visit(ctx.operation());
            String name = ctx.NAME().getText();
            if (res)
            {
               if (!BaseGrammarParser.symbolTable.containsKey(name))
               {
                  ErrorHandling.printError(ctx, "Variable \""+name+"\" does not exist!");
                  res = false;
               }
               else
                   BaseGrammarParser.symbolTable.put(name, new VariableSymbol(id, ctx.operation().v));
                  }
         return res;
      }




}