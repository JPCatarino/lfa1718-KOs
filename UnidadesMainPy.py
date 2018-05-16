import sys
from antlr4 import *
from UnidadesLexer import UnidadesLexer
from UnidadesParser import UnidadesParser
from UnidadesListener import UnidadesListener
from collections import deque
import quantities as pq

class UnitConverter(UnidadesListener):
    parsedUnit = deque([])
    parsedNumber = deque([])

    def enterMain(self, ctx):
        print("this is working!")

    def exitMain(self,ctx):
        while UnitConverter.parsedNumber:
            print(UnitConverter.parsedNumber.popleft())

    def exitTest(self,ctx):
        temp = int(ctx.INT().getText()) * UnitConverter.parsedUnit.popleft()
        UnitConverter.parsedNumber.append(temp)

    def exitUnitDiv(self, ctx):
        num = 1*UnitConverter.parsedUnit.popleft()
        den = 1*UnitConverter.parsedUnit.popleft()
        UnitConverter.parsedUnit.append(num/den)

    def exitUnitPow(self,ctx):
        temp = UnitConverter.parsedUnit.pop()
        temp2 = temp**int(ctx.INT().getText())
        UnitConverter.parsedUnit.append(temp2)

    def exitUnitUNIT(self,ctx):
        if(ctx.UNIT().getText() == "m"):
            p = pq.m
            UnitConverter.parsedUnit.append(p)
        elif(ctx.UNIT().getText() == "g"):
            p = pq.g
            UnitConverter.parsedUnit.append(p)
        elif(ctx.UNIT().getText() == "s"):
            p = pq.s
            UnitConverter.parsedUnit.append(p)
        elif(ctx.UNIT().getText() == "A"):
            p = pq.A
            UnitConverter.parsedUnit.append(p)
        elif(ctx.UNIT().getText() == "K"):
            p = pq.K
            UnitConverter.parsedUnit.append(p)
        elif(ctx.UNIT().getText() == "mol"):
            p = pq.mol
            UnitConverter.parsedUnit.append(p)
        elif(ctx.UNIT().getText() == "cd"):
            p = pq.cd
            UnitConverter.parsedUnit.append(p)




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