package uml.links.components;

import javax.xml.bind.annotation.XmlAttribute;

public class Multiplicity
{
    public static Multiplicity ZERO = new Multiplicity('0', '0');
    public static Multiplicity ZERO_TO_N = new Multiplicity('0', '*');
    public static Multiplicity ONE = new Multiplicity('1', '1');
    public static Multiplicity ONE_TO_N = new Multiplicity('1', '*');
    public static Multiplicity N = new Multiplicity('*', '*');

    private char lowerBound;
    private char upperBound;

    public Multiplicity()
    {

    }

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

    @XmlAttribute
    public char getLowerBound()
    {
        return lowerBound;
    }

    public void setLowerBound(char lowerBound)
    {
        this.lowerBound = lowerBound;
    }

    @XmlAttribute
    public char getUpperBound()
    {
        return upperBound;
    }

    public void setUpperBound(char upperBound)
    {
        this.upperBound = upperBound;
    }

    static public Multiplicity fromString(String s)
    {
        if (s.equals("0"))
            return ZERO;
        if (s.equals("0..*"))
            return ZERO_TO_N;
        if (s.equals("1"))
            return ONE;
        if (s.equals("1..*"))
            return ONE_TO_N;
        if (s.equals("*"))
            return N;

        String[] bound = s.split("[.][.]");

        // TODO check if bound is valid

        return new Multiplicity(bound[0].charAt(0),
                bound.length < 2 ? bound[0].charAt(0) :
                        bound[1].charAt(0));
    }
}
