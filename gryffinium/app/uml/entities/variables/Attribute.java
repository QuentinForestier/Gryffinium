package uml.entities.variables;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.entities.variables.AttributeDto;
import dto.entities.variables.VariableDto;
import uml.Visibility;
import uml.ClassDiagram;
import uml.entities.Entity;

import javax.xml.bind.annotation.XmlAttribute;

public class Attribute extends Variable
{

    private boolean isStatic;
    private Visibility visibility;

    public Attribute(){
        super();
    }

    public Attribute(String name, boolean isConstant, boolean isStatic, Visibility visibility)
    {
        super(name, isConstant);
        this.isStatic = isStatic;
        this.visibility = visibility;
    }

    public Attribute(dto.entities.variables.AttributeDto ga, ClassDiagram cd)
    {
        super(ga, cd);
        if(ga.isStatic() == null)
        {
            throw new IllegalArgumentException("isStatic attribute is null");
        }
        if(ga.getVisibility() == null)
        {
            throw new IllegalArgumentException("visibility attribute is null");
        }
        fromDto(ga, cd);
    }

    public Attribute(String name)
    {
        this(name, false, false, Visibility.PRIVATE);
    }



    public void fromDto(AttributeDto ga, ClassDiagram cd)
    {
        super.fromDto(ga, cd);
        if(ga.isStatic() != null)
            this.setStatic(ga.isStatic());
        if(ga.getVisibility() != null){
            this.setVisibility(Visibility.valueOf(ga.getVisibility().toUpperCase()));
        }
    }

    @XmlAttribute
    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean aStatic)
    {
        isStatic = aStatic;
    }

    @XmlAttribute
    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }

    public VariableDto toDto(Entity e)
    {
        if(getParent() == null && e != null)
        {
            setParent(e);
        }
        return new AttributeDto(this, e);
    }

    public JsonNode getCreationCommand(Entity e)
    {
        return Command.createResponse(toDto(e), ElementTypeDto.ATTRIBUTE);
    }

    @Override
    public JsonNode getUpdateNameCommand()
    {
        AttributeDto dto = new AttributeDto();
        dto.setId(this.getId());
        dto.setType(getType().getName());
        dto.setParentId(this.getParent().getId());
        return Command.createResponse(dto, ElementTypeDto.ATTRIBUTE);
    }
}
