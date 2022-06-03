package uml.links;

import uml.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class MultiAssociation extends Association
{
    private final ArrayList<Role> roles = new ArrayList<>();

    public MultiAssociation(String name, List<Entity> entites)
    {
        super(name);
        if(entites.size() < 3 ){
            throw new RuntimeException("MutliAssociation should have at least 3 entities linked");
        }

        for(Entity entity : entites){
            roles.add(new Role(entity.getName(), Multiplicity.N, entity));
        }
    }
}
