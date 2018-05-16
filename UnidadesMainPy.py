import sys
from antlr4 import *
from UnidadesLexer import UnidadesLexer
from UnidadesParser import UnidadesParser
from UnidadesListener import UnidadesListener

class UnitConverter(UnidadesListener):
    def enterMain(self, ctx):
        print("this is working!")

    def

def main(argv):
    input = FileStream(argv[1])
    lexer = UnidadesLexer(input)
    stream = CommonTokenStream(lexer)
    parser = UnidadesParser(stream)
    tree = parser.main()
    printer = UnitConverter()
    walker = ParseTreeWalker()
    walker.walk(printer,tree)

if __name__ == '__main__':
    main(sys.argv)