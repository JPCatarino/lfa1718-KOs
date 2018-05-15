import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import static java.lang.System.*;
import java.util.Scanner;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class UnidadesMain {
   public static void main(String[] args) throws Exception {
      InputStream in_str = null;
      try{
         in_str = new FileInputStream(new File(args[0]));
      }
      catch(FileNotFoundException e){
         err.println("error reading file");
         System.exit(1);
      }
      // create a CharStream that reads from standard input:
      CharStream input = CharStreams.fromStream(in_str);
      // create a lexer that feeds off of input CharStream:
      UnidadesLexer lexer = new UnidadesLexer(input);
      // create a buffer of tokens pulled from the lexer:
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // create a parser that feeds off the tokens buffer:
      UnidadesParser parser = new UnidadesParser(tokens);
      // replace error listener:
      //parser.removeErrorListeners(); // remove ConsoleErrorListener
      //parser.addErrorListener(new ErrorHandlingListener());
      // begin parsing at main rule:
      ParseTree tree = parser.main();
      if (parser.getNumberOfSyntaxErrors() == 0) {
         // print LISP-style tree:
         // System.out.println(tree.toStringTree(parser));
         //Creating walker and listener
         ParseTreeWalker walker = new ParseTreeWalker();
         unitConverter units = new unitConverter();
         walker.walk(units,tree);
         //print dos resultados da conversão, mais tarde isto vai ser substituido par enviar os resultados para um ficheiro
         Stack<String> stack = new Stack<String>();
         Queue<String> fila = new LinkedList<String>();
         while(!units.isEmpty()) {
             // System.out.print(units.getResult() + "\n");
             String a = units.getResult();
             stack.push(a);
         }
        toText(stack);
      }
}
   public static void toText (Stack<String> amont)throws IOException
	{	
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter("test.txt"));
		while(!amont.isEmpty())
		{	
		   System.out.printf("%s\n",amont.peek());
		   outputWriter.write(amont.peek()+"\n");
		   amont.pop();

		}
	   outputWriter.flush();  
 	   outputWriter.close();  	
	}
}
