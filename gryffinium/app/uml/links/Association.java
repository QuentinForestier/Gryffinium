package uml.links;

import uml.entities.Entity;

public class Association
{
    class Role
    {
        private Multiplicity multiplicity;
        private String name;

        private Entity entity;

        Role(String name, Multiplicity multiplicity, Entity entity){
            this.entity = entity;
            this.multiplicity = multiplicity;
            this.name = name;
        }
    }

    private String name;

    public Association(String name)
    {
        this.name = name;
    }
}
