package uml.links;

public class Multiplicity
{
    public static Multiplicity ZERO = new Multiplicity('0', '0');
    public static Multiplicity ZERO_TO_N = new Multiplicity('0', '*');
    public static Multiplicity ONE = new Multiplicity('1', '1');
    public static Multiplicity ONE_TO_N = new Multiplicity('1', '*');
    public static Multiplicity N = new Multiplicity('*', '*');

    private char lowerBound;
    private char upperBound;

    public Multiplicity(char lowerBound, char upperBound)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public String toString()
    {
        return lowerBound == upperBound ? "" + lowerBound : lowerBound
                + ".." + upperBound;
    }
}
