package uml.links;


import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandType;
import dto.ElementTypeDto;
import dto.links.MultiAssociationDto;
import dto.links.UnaryAssociationDto;
import play.libs.Json;
import tyrex.services.UUID;
import uml.ClassDiagram;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import java.awt.*;
import java.util.ArrayList;


@XmlType(name = "MultiAssociation")
public class MultiAssociation
{
    private String id;
    private ArrayList<UnaryAssociation> unaryAssociations = new ArrayList<>();

    private Point position;


    public MultiAssociation()
    {
        this.id = UUID.create();
    }

    public MultiAssociation(MultiAssociationDto dto, ClassDiagram cd)
    {
        this();
        fromDto(dto);
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

    @XmlElement
    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }

    public Double getX()
    {
        return position.getX();
    }

    public Double getY()
    {
        return position.getY();
    }

    public void setX(Double x)
    {
        position.setLocation(x, position.getY());
    }

    public void setY(Double y)
    {
        position.setLocation(position.getX(), y);
    }

    public void addUnaryAssociation(UnaryAssociation unaryAssociation)
    {
        unaryAssociations.add(unaryAssociation);
    }

    public void removeUnaryAssociation(UnaryAssociation unaryAssociation)
    {
        unaryAssociations.remove(unaryAssociation);
    }

    public MultiAssociationDto toDto()
    {
        return new MultiAssociationDto(this);
    }

    public void fromDto(MultiAssociationDto dto)
    {
        if (dto.getId() != null)
        {
            this.setId(dto.getId());
        }
        if (dto.getUnaryAssociations() != null)
        {
            for (String unaryAssociationId : dto.getUnaryAssociations())
            {
                UnaryAssociation unaryAssociation = new UnaryAssociation();
                unaryAssociation.setId(unaryAssociationId);
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
                ElementTypeDto.MUTLI_ASSOCIATION, CommandType.SELECT_COMMAND));
        return result;
    }
}
