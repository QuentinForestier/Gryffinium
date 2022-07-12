package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.links.AssociationDto;
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

    @Override
    public Role getRoleByEntityId(Integer entityId)
    {
        return null;
    }

    @Override
    public AssociationDto toDto()
    {
        return null;
    }

    @Override
    public ArrayNode getCreationCommands()
    {
        return null;
    }
}
