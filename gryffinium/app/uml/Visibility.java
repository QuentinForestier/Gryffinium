package uml;

public enum Visibility
{
    PUBLIC("public",'+'),
    PACKAGE("package",'~'),
    PROTECTED("protected",'#'),
    PRIVATE("private",'-');

    private final char symbol;
    private final String name;

    Visibility(String name, char symbol){
        this.name = name;
        this.symbol = symbol;
    }

    public char getSymbol()
    {
        return symbol;
    }

    public String getName()
    {
        return name;
    }
}
