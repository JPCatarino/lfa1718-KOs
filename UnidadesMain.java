import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class UnidadesMain {
   public static void main(String[] args) throws Exception {
      // create a CharStream that reads from standard input:
      CharStream input = CharStreams.fromStream(System.in);
      // create a lexer that feeds off of input CharStream:
      UnidadesLexer lexer = new UnidadesLexer(input);
      // create a buffer of tokens pulled from the lexer:
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // create a parser that feeds off the tokens buffer:
      UnidadesParser parser = new UnidadesParser(tokens);
      // replace error listener:
      //parser.removeErrorListeners(); // remove ConsoleErrorListener
      //parser.addErrorListener(new ErrorHandlingListener());
      // begin parsing at test rule:
      ParseTree tree = parser.test();
      if (parser.getNumberOfSyntaxErrors() == 0) {
         // print LISP-style tree:
         // System.out.println(tree.toStringTree(parser));
         //Creating walker and listener
         ParseTreeWalker walker = new ParseTreeWalker();
         unitConverter units = new unitConverter();
         walker.walk(units,tree);
         //print dos resultados da conversão, mais tarde isto vai ser substituido par enviar os resultados para um ficheiro
         while(!units.isEmpty()) {
            System.out.print(units.getResult() + "\n");
         }
      }
   }
}
