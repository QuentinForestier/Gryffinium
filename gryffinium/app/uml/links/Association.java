package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.links.AssociationDto;
import play.libs.Json;
import uml.ClassDiagram;

public abstract class Association
{
    private Integer id;
    private String name;

    public Association(String name)
    {
        this.name = name;
    }

    public Association(AssociationDto graphicalAssociation, ClassDiagram cd)
    {
        this.setGraphical(graphicalAssociation, cd);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setGraphical(dto.links.AssociationDto ga, ClassDiagram cd)
    {
        if(ga.getName() != null)
            this.name = ga.getName();
    }

    public abstract AssociationDto toDto();
    public abstract ArrayNode getCreationCommands();
}
