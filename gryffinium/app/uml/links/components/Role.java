package uml.links.components;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import dto.ElementTypeDto;
import dto.links.components.RoleDto;
import tyrex.services.UUID;
import uml.ClassDiagram;
import uml.entities.Entity;
import uml.links.Association;

import javax.xml.bind.annotation.*;

@XmlType(name = "Role")
public class Role
{
    private String id;
    private Multiplicity multiplicity;

    private Label nameLabel = new Label();
    private Label multiplicityLabel = new Label();

    private Entity entity;

    public Multiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity)
    {
        this.multiplicity = multiplicity;
    }

    @XmlElement
    private Label getNameLabel()
    {
        return nameLabel;
    }

    private void setNameLabel(Label nameLabel)
    {
        this.nameLabel = nameLabel;
    }

    @XmlElement
    private Label getMultiplicityLabel()
    {
        return multiplicityLabel;
    }

    private void setMultiplicityLabel(Label multiplicityLabel)
    {
        this.multiplicityLabel = multiplicityLabel;
    }

    @XmlAttribute
    @XmlID
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlTransient
    public String getName()
    {
        return getNameLabel().getName();
    }

    public void setName(String name)
    {
        this.getNameLabel().setName(name);
    }

    @XmlIDREF
    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    @XmlTransient
    public Double getDistanceName()
    {
        return getNameLabel().getDistance();
    }

    public void setDistanceName(double distanceName)
    {
        this.getNameLabel().setDistance(distanceName);
    }

    @XmlElement
    public JsonNode getOffsetName()
    {
        return getNameLabel().getOffset();
    }

    public void setOffsetName(JsonNode offsetName)
    {
        this.getNameLabel().setOffset(offsetName);
    }

    @XmlAttribute
    public Double getDistanceMultiplicity()
    {
        return getMultiplicityLabel().getDistance();
    }

    public void setDistanceMultiplicity(double distanceMultiplicity)
    {
        this.getMultiplicityLabel().setDistance(distanceMultiplicity);
    }

    @XmlElement
    public JsonNode getOffsetMultiplicity()
    {
        return getMultiplicityLabel().getOffset();
    }

    public void setOffsetMultiplicity(JsonNode offsetMultiplicity)
    {
        this.getMultiplicityLabel().setOffset(offsetMultiplicity);
    }

    public Role()
    {
    }

    public Role(String name, Multiplicity multiplicity, Entity entity)
    {
        this.entity = entity;
        this.multiplicity = multiplicity;
        this.getNameLabel().setName(name);
        this.id = UUID.create();
    }

    public Role(RoleDto rdto, ClassDiagram cd)
    {
        this.id = UUID.create();
        fromDto(rdto, cd);
    }

    public void fromDto(RoleDto rdto, ClassDiagram cd)
    {
        if (rdto.getName() != null)
            this.getNameLabel().setName(rdto.getName());

        if (rdto.getMultiplicity() != null)
        {
           this.setMultiplicity(Multiplicity.fromString(rdto.getMultiplicity()));
        }

        if (rdto.getDistanceName() != null)
            this.getNameLabel().setDistance(rdto.getDistanceName());
        if (rdto.getOffsetName() != null)
            this.getNameLabel().setOffset(rdto.getOffsetName());

        if (rdto.getDistanceMultiplicity() != null)
            this.getMultiplicityLabel().setDistance(rdto.getDistanceMultiplicity());
        if (rdto.getOffsetMultiplicity() != null)
            this.getMultiplicityLabel().setOffset(rdto.getOffsetMultiplicity());

        if (rdto.getId() != null)
            this.id = rdto.getId();

    }

    public RoleDto toDto(Association association)
    {
        return new RoleDto(this, association);
    }

    public JsonNode getCreationCommands(Association association)
    {
        return Command.createResponse(toDto(association),
                ElementTypeDto.ROLE);
    }
}
