package uml.links;

import uml.entities.Implementor;
import uml.entities.Interface;

public class Realization extends ClassRelationship
{
    private Implementor implementor;
    private Interface interfce;

    public Implementor getImplementor()
    {
        return implementor;
    }

    public void setImplementor(Implementor implementor)
    {
        this.implementor = implementor;
    }

    public Interface getInterfce()
    {
        return interfce;
    }

    public void setInterfce(Interface interfce)
    {
        this.interfce = interfce;
    }
}
