

public class UnidadesSemanticCheck extends UnidadesBaseVisitor<Boolean> {

    @Override
    public Boolean visitCreate(UnidadesParser.CreateContext ctx) {
        Boolean res = true;

        String id = ctx.NAME().getText();


        if (UnidadesParser.symbolTable.containsKey(id)) {
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" already declared!");
            res = false;
        } else
            UnidadesParser.symbolTable.put(id, new UnitSymbol(id, tipo.simples));
        return res;
    }


    @Override
    public Boolean visitPow(UnidadesParser.PowContext ctx) {
        Boolean res = true;

        String id = ctx.NAME().getText();

        USymbol s = UnidadesParser.symbolTable.get(id);

        if (s.type != tipo.simples) {
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" should be Simple!");
            res = false;
        }

        return res;
    }


    @Override
    public Boolean visitCompose(UnidadesParser.ComposeContext ctx) {
        Boolean res = true;

        String id = ctx.NAME().getText();

        if (UnidadesParser.symbolTable.containsKey(id)) {
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" already declared!");
            res = false;
        } else
            UnidadesParser.symbolTable.put(id, new UnitSymbol(id, tipo.composta));
        res = visitChildren(ctx);
        return res;
    }

    @Override //verificar (garantir) se é simples
    public Boolean visitCUnitName(UnidadesParser.CUnitNameContext ctx) {
        Boolean res = true;

        String id = ctx.NAME().getText();

        USymbol s = UnidadesParser.symbolTable.get(id);


        if (s.type != tipo.simples) {
            ErrorHandling.printError(ctx, "Variable \"" + id + "\" should be Simple!");
            res = false;
        }

        return res;
    }
}
            