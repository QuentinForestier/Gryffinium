package uml.types;

public class PrimitiveType extends Type
{
    public static PrimitiveType BOOLEAN_TYPE = new PrimitiveType("bool");
    public static PrimitiveType BYTE_TYPE = new PrimitiveType("byte");
    public static PrimitiveType SHORT_TYPE = new PrimitiveType("short");
    public static PrimitiveType INT_TYPE = new PrimitiveType("int");
    public static PrimitiveType LONG_TYPE = new PrimitiveType("long");
    public static PrimitiveType FLOAT_TYPE = new PrimitiveType("float");
    public static PrimitiveType DOUBLE_TYPE = new PrimitiveType("double");
    public static PrimitiveType CHAR_TYPE = new PrimitiveType("char");
    public static PrimitiveType STRING_TYPE = new PrimitiveType("string");

    private PrimitiveType(String name){
        super(name);
    }
}
