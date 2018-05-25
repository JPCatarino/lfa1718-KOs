
public class UnidadesSemanticCheck extends UnidadesBaseVisitor<Boolean> {
/*
    @Override
    public T visitMain(UnidadesParser.MainContext ctx) {
        return visitChildren(ctx);
    }
*/


/*    @Override
    public T visitStatList(UnidadesParser.StatListContext ctx) {
        return visitChildren(ctx);
    }*/


/*    @Override
    public T visitStat(UnidadesParser.StatContext ctx) {
        return visitChildren(ctx);
    }*/


    @Override
    public Boolean visitCreate(UnidadesParser.CreateContext ctx) {
        Boolean res = true;

        String id = ctx.NAME().getText();

        if (UnidadesParser.symbolTable.containsKey(id))
        {
            ErrorHandling.printError(ctx, "Variable \""+id+"\" already declared!");
            res = false;
        }
        else
            UnidadesParser.symbolTable.put(id, new UnitSymbol(id));
    return res;
    }


/*
    @Override
    public T visitPow(UnidadesParser.PowContext ctx) {
        return visitChildren(ctx);
    }
*/

    @Override
    public Boolean visitCompose(UnidadesParser.ComposeContext ctx) {
        Boolean res = true;

        String id = ctx.NAME().getText();

        if (UnidadesParser.symbolTable.containsKey(id))
        {
            ErrorHandling.printError(ctx, "Variable \""+id+"\" already declared!");
            res = false;
        }
        else
            UnidadesParser.symbolTable.put(id, new UnitSymbol(id));
        return res;
    }



/*    @Override
    public Boolean visitUnitUNIT(UnidadesParser.UnitUNITContext ctx) {
        return visitChildren(ctx);
    }*/


 /*   @Override
    public T visitCUnitDivMult(UnidadesParser.CUnitDivMultContext ctx) {
        return visitChildren(ctx);
    }


    @Override
    public T visitCUnitNAME(UnidadesParser.CUnitNAMEContext ctx) {
        return visitChildren(ctx);
    }*/
}
            