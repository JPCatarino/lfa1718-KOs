/**
 * Created by Media Markt on 05/06/2018.
 */
public class BGSymbol {
    public BGSymbol(String name, vartype type) {
        assert name != null;
        assert type != null;

        this.name = name;
        this.type = type;
    }


    public String name() {
        return name;
    }

    public vartype type(){
        return type;
    }

    public void setVarName(String varName) {
        assert varName != null;

        this.varName = varName;
    }

    public String varName() {
        return varName;
    }

    protected final String name;
    protected String varName;
    protected final vartype type;
}
