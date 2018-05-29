public abstract class USymbol {
   public USymbol(String name, tipo type) {
      assert name != null;
      assert type != null;

      this.name = name;
      this.type = type;
   }


   public String name() {
      return name;
   }

   public tipo type(){
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
   protected final tipo type;

}

