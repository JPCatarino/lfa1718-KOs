import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.stringtemplate.v4.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;



public class BaseGrammarMain {
   public static void main(String[] args) throws Exception {
      // create a CharStream that reads from standard input:
      CharStream input = CharStreams.fromStream(new FileInputStream(args[0]));
      // create a lexer that feeds off of input CharStream:
      BaseGrammarLexer lexer = new BaseGrammarLexer(input);
      // create a buffer of tokens pulled from the lexer:
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // create a parser that feeds off the tokens buffer:
      BaseGrammarParser parser = new BaseGrammarParser(tokens);
      // replace error listener:
      //parser.removeErrorListeners(); // remove ConsoleErrorListener
      //parser.addErrorListener(new ErrorHandlingListener());
      // begin parsing at main rule:
      ParseTree tree = parser.main();
      if (parser.getNumberOfSyntaxErrors() == 0) {
         // print LISP-style tree:
         // System.out.println(tree.toStringTree(parser));
         testVisitor vis = new testVisitor();
         ST code = vis.visit(tree);
         String filename = "Output.py";
         try
         {
            PrintWriter pw = new PrintWriter(new File(filename));
            pw.print(code.render());
            pw.close();
         }
         catch(IOException e)
         {
            System.err.println("ERROR: unable to write in file "+filename);
            System.exit(3);
         }
      }
   }
}
