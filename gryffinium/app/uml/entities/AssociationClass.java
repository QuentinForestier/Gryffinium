package uml.entities;

import uml.links.BinaryAssociation;

public class AssociationClass extends Class
{
    private BinaryAssociation association;

    public AssociationClass(String name, Entity from, Entity to)
    {
        this(name, new BinaryAssociation(from, to));
    }

    public AssociationClass(String name, BinaryAssociation association)
    {
        super(name);
        this.association = association;
    }
}
