package uml;

public enum Visibility
{
    PUBLIC('+'),
    PACKAGE('~'),
    PROTECTED('#'),
    PRIVATE('-');

    private final char symbol;

    Visibility(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol()
    {
        return symbol;
    }
}
