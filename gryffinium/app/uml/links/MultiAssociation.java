package uml.links;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.links.MultiAssociationDto;
import dto.links.UnaryAssociationDto;
import play.libs.Json;
import tyrex.services.UUID;
import uml.ClassDiagram;

import javax.xml.bind.annotation.*;
import java.awt.*;
import java.util.ArrayList;


@XmlType(name = "MultiAssociation")
public class MultiAssociation
{
    private String id;
    private ArrayList<UnaryAssociation> unaryAssociations = new ArrayList<>();

    private Double x;
    private Double y;


    public MultiAssociation()
    {
        this.id = UUID.create();
    }

    public MultiAssociation(MultiAssociationDto dto, ClassDiagram cd)
    {
        this();
        fromDto(dto, cd);
    }


    @XmlElement(name = "UnaryAssociation")
    public ArrayList<UnaryAssociation> getUnaryAssociations()
    {
        return unaryAssociations;
    }

    public void setUnaryAssociations(ArrayList<UnaryAssociation> unaryAssociations)
    {
        this.unaryAssociations = unaryAssociations;
    }

    @XmlID
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    @XmlAttribute
    public Double getX()
    {
        return x;
    }

    @XmlAttribute
    public Double getY()
    {
        return y;
    }

    public void setX(Double x)
    {
        this.x = x;
    }

    public void setY(Double y)
    {
        this.y = y;
    }

    public void addUnaryAssociation(UnaryAssociation unaryAssociation)
    {
        unaryAssociations.add(unaryAssociation);
    }

    public UnaryAssociation getUnaryAssociation(String id)
    {
        for (UnaryAssociation unaryAssociation : unaryAssociations)
        {
            if (unaryAssociation.getId().equals(id))
            {
                return unaryAssociation;
            }
        }
        return null;
    }

    public void removeUnaryAssociation(UnaryAssociation unaryAssociation)
    {
        unaryAssociations.remove(unaryAssociation);
    }

    public MultiAssociationDto toDto()
    {
        return new MultiAssociationDto(this);
    }

    public void fromDto(MultiAssociationDto dto, ClassDiagram cd)
    {
        if (dto.getId() != null)
        {
            this.setId(dto.getId());
        }
        if (dto.getTargets() != null)
        {
            this.unaryAssociations.clear();

            if(dto.getTargets().size() < 3)
            {
                throw new IllegalArgumentException("MultiAssociation must have at least 3 targets");
            }

            for (JsonNode unaryAssociationId : dto.getTargets())
            {
                UnaryAssociationDto uadto = new UnaryAssociationDto();
                uadto.setTargetId(unaryAssociationId.asText());
                uadto.setName("unary");
                UnaryAssociation unaryAssociation = new UnaryAssociation(uadto, cd);
                unaryAssociation.setParent(this);
                this.addUnaryAssociation(unaryAssociation);
            }
        }

        if (dto.getX() != null)
        {
            this.setX(dto.getX());
        }

        if (dto.getY() != null)
        {
            this.setY(dto.getY());
        }
    }

    public ArrayNode getCreationCommands()
    {
        ArrayNode result = Json.newArray();
        result.add(Command.createResponse(toDto(),
                ElementTypeDto.MULTI_ASSOCIATION, CommandType.SELECT_COMMAND));
        for (UnaryAssociation unaryAssociation : unaryAssociations)
        {
            result.addAll(unaryAssociation.getCreationCommands());
        }
        return result;
    }
}
