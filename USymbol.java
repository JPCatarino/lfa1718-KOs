public abstract class USymbol
{
   public USymbol(String name) {
      assert name != null;

      this.name = name;
   }


   public String name(){
      return name;
   }

   public void setVarName(String varName) {
      assert varName != null;

      this.varName = varName;
   }

   public String varName(){
      return varName;
   }

   protected final String name;
   protected String varName;
}

