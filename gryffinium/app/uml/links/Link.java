package uml.links;

import com.fasterxml.jackson.databind.JsonNode;
import dto.links.LinkDto;
import tyrex.services.UUID;
import uml.ClassDiagram;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({LabeledLink.class, ClassRelationship.class, Inner.class})
public abstract class Link
{
    private String id;
    private JsonNode vertices;

    public Link()
    {
        this.id = UUID.create();
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

    @XmlElement
    public JsonNode getVertices()
    {
        return vertices;
    }

    public void setVertices(JsonNode vertices)
    {
        this.vertices = vertices;
    }

    public void fromDto(LinkDto dto, ClassDiagram cd)
    {
        if (dto.getId() != null)
            this.id = dto.getId();
        if (dto.getVertices() != null)
            this.vertices = dto.getVertices();
    }


    public abstract JsonNode getCreationCommands();

    public abstract LinkDto toDto();
}
