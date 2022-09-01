package uml;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.AssociationClassDto;
import dto.ElementTypeDto;
import play.libs.Json;
import tyrex.services.UUID;
import uml.entities.Class;
import uml.entities.Entity;
import uml.links.Association;
import uml.links.BinaryAssociation;
import uml.links.components.Role;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "AssociationClass")
public class AssociationClass
{

    private String id;

    Class associatedClass;
    BinaryAssociation association;

    public AssociationClass()
    {
        this.id = UUID.create();
    }

    public AssociationClass(AssociationClassDto dto, ClassDiagram cd)
    {
        this();
        fromDto(dto, cd);
    }

    @XmlAttribute
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlIDREF
    public Class getAssociatedClass()
    {
        return associatedClass;
    }

    public void setAssociatedClass(Class associatedClass)
    {
        this.associatedClass = associatedClass;
    }

    @XmlIDREF
    public BinaryAssociation getAssociation()
    {
        return association;
    }

    public void setAssociation(BinaryAssociation association)
    {
        this.association = association;
    }

    public AssociationClassDto toDto()
    {
        return new AssociationClassDto(this);
    }

    public void fromDto(AssociationClassDto dto, ClassDiagram cd)
    {
        if (dto.getId() != null)
        {
            this.id = dto.getId();
        }
        if (dto.getClassDto() != null)
        {
            if (this.associatedClass == null)
            {
                this.associatedClass = new Class(dto.getClassDto());
            }
            else
            {
                this.associatedClass.fromDto(dto.getClassDto());
            }
        }
        if (dto.getAssociationDto() != null)
        {
            if (this.association == null)
            {
                this.association =
                        new BinaryAssociation(dto.getAssociationDto(), cd);
            }
            else
            {
                this.association.fromDto(dto.getAssociationDto(), cd);
            }

        }
    }

    public ArrayNode getCreationsCommand()
    {
        try
        {
            ArrayNode result = Json.newArray();
            result.add(Command.createResponse(toDto(),
                    ElementTypeDto.ASSOCIATION_CLASS,
                    CommandType.SELECT_COMMAND));
            return result;
        }
        catch (Exception e)
        {
            return Json.newArray();
        }
    }
}

